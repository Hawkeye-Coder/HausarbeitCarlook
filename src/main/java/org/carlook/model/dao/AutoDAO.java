package org.carlook.model.dao;

import com.vaadin.ui.Notification;
import org.carlook.model.objects.dto.AutoDTO;
import org.carlook.model.objects.dto.KundeDTO;
import org.carlook.model.objects.dto.UserDTO;
import org.carlook.process.exceptions.DatabaseException;
import org.carlook.process.proxy.ReservierungControlProxy;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AutoDAO extends AbstractDAO {
    private static AutoDAO dao = null;

    private AutoDAO() {
    }

    public static AutoDAO getInstance() {
        if (dao == null) {
            dao = new AutoDAO();
        }
        return dao;
    }

    //Erzeugt eine Liste der Autos, die ein Vertriebler erstellt hat
    public List<AutoDTO> getAutoVertriebler(UserDTO userDTO) throws SQLException {
        String sql = "SELECT id, marke, baujahr, beschreibung " +
                "FROM carlook.auto a, carlook.auto_to_vertriebler atv " +
                "WHERE a.id = atv.id_auto AND atv.id_vertriebler = ? ";

        PreparedStatement statement = this.getPreparedStatement(sql);
        ResultSet rs = null;
        try {
            statement.setInt(1, userDTO.getId());
            rs = statement.executeQuery();

        } catch (SQLException e) {
            Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!");
        }
        List<AutoDTO> list = new ArrayList<>();
        assert rs != null;
        buildList(rs, list);
        return list;
    }


    //Erstellt ein neues Auto in der Datenbank
    public boolean createAuto(AutoDTO auto, UserDTO userDTO) {
        String sql = "INSERT INTO carlook.auto(id, marke, baujahr, beschreibung)" +
                "VALUES (default, ?, ?, ?)";

        PreparedStatement statement = this.getPreparedStatement(sql);

        try {
            statement.setString(1, auto.getMarke());
            statement.setInt(2, auto.getBaujahr());
            statement.setString(3, auto.getBeschreibung());
            statement.executeUpdate();
        } catch (SQLException ex) {
            return false;
        }
        try {
            int autoID = AutoDAO.getInstance().getMaxIDAuto();
            AutoDAO.getInstance().connectAutoWithVertriebler(autoID, userDTO.getId());
            return true;
        } catch (SQLException ex) {
            return false;
        }


    }

    //Trägt Vertriebler in die Liste ein
    private void connectAutoWithVertriebler(int idAuto, int idVertriebler) throws SQLException {
        String sql = "INSERT INTO carlook.auto_to_vertriebler (id_vertriebler, id_auto) VALUES (?,?)";
        PreparedStatement statement = this.getPreparedStatement(sql);
        try {
            statement.setInt(1, idVertriebler);
            statement.setInt(2, idAuto);
            statement.executeQuery();
        } catch (SQLException ex) {
            //Notification.show("SQL-Fehler bei der Zuordnung von Auto und Vertriebler");
        }
    }

    //Verändert ein bestehendes Auto in der Datenbank
    public boolean updateAuto(AutoDTO auto) {
        String sql = "UPDATE carlook.auto " +
                "SET marke = ?, baujahr = ?, beschreibung = ? " +
                "WHERE id = ? ;";
        PreparedStatement statement = this.getPreparedStatement(sql);
        try {
            statement.setString(1, auto.getMarke());
            statement.setInt(2, auto.getBaujahr());
            statement.setString(3, auto.getBeschreibung());
            statement.setInt(4, auto.getAuto_id());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }


    //Löscht ein Auto aus der Datenbank
    public boolean deleteAuto(AutoDTO auto) {
        String sql = "DELETE " +
                "FROM carlook.auto " +
                "WHERE id = ? ;";
        PreparedStatement statement = this.getPreparedStatement(sql);
        try {
            statement.setInt(1, auto.getAuto_id());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }
    //Durchsucht die DB nach Suchkriterien
    public List<AutoDTO> getAutoForSearch(String suchtext, String filter) throws SQLException {
        filter = filter.toLowerCase();
        PreparedStatement statement;
        ResultSet rs = null;
        if (suchtext.equals("") || filter.equals("name")) {
            String sql = "SELECT id, marke, baujahr, beschreibung " +
                    "FROM carlook.auto ; ";
            statement = this.getPreparedStatement(sql);
            try {
                rs = statement.executeQuery();
            } catch (SQLException e) {
                Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!");
            }
        } else if (filter.equals("baujahr")) {
            String sql = "SELECT id, marke, baujahr, beschreibung " +
                    "FROM carlook.auto " +
                    "WHERE " + filter + " = ? ;";
            statement = this.getPreparedStatement(sql);
            try {
                statement.setInt(1, Integer.parseInt(suchtext));
                rs = statement.executeQuery();
            } catch (SQLException e) {
                Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!");
            }
        } else {
            String sql = "SELECT id, marke, baujahr, beschreibung " +
                    "FROM carlook.auto " +
                    "WHERE " + filter + " like ? ;";
            statement = this.getPreparedStatement(sql);


            try {
                statement.setString(1, "%" + suchtext + "%");
                rs = statement.executeQuery();
            } catch (SQLException e) {
                Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!");
            }
        }

        List<AutoDTO> list = new ArrayList<>();

        assert rs != null;
        buildList(rs, list);
        return list;
    }

    //Zeigt alle Reserverierungen eines Kunden an
    public List<AutoDTO> bereitsReservierteAutos(KundeDTO kundeDTO) throws SQLException {
        String sql = "SELECT id, marke, baujahr, beschreibung " +
                "FROM carlook.auto a, carlook.reservierung r " +
                "WHERE a.id = r.id_auto AND r.id_kunde = ? ";
        PreparedStatement statement = this.getPreparedStatement(sql);
        ResultSet rs = null;
        List<AutoDTO> listAuto = new ArrayList<>();
        try {
            statement.setInt(1, kundeDTO.getId());
            rs = statement.executeQuery();
        } catch (SQLException e) {
            Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!");
        }
        assert rs != null;
        buildList(rs, listAuto);
        return listAuto;
    }

    //Erzeugt die gewünschte Liste an gesuchten Autos
    private void buildList(ResultSet rs, List<AutoDTO> autoDTOList) throws SQLException {

        AutoDTO autoDTO;
        try {
            while (rs.next()) {

                autoDTO = new AutoDTO();
                autoDTO.setAuto_id(rs.getInt(1));
                autoDTO.setMarke(rs.getString(2));
                autoDTO.setBaujahr(rs.getInt(3));
                autoDTO.setBeschreibung(rs.getString(4));

                try {

                    autoDTO.setAnzahl_res(ReservierungControlProxy.getInstance().getAnzahlRes(autoDTO));

                } catch (DatabaseException e) {

                    Notification.show("Es ist ein Datenbankfehler aufgetreten. Bitte informieren Sie einen Administrator!");

                }
                autoDTOList.add(autoDTO);
            }
        } catch (SQLException e) {
            Notification.show("Es ist ein schwerer SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!");
        } finally {
            assert rs != null;
            rs.close();
        }
    }
    //Sucht die größte Auto-ID
    public int getMaxIDAuto() throws SQLException {
        String sql = "SELECT max(id) " +
                "FROM carlook.auto ;";
        PreparedStatement statement = getPreparedStatement(sql);
        ResultSet rs = null;

        try {
            rs = statement.executeQuery();
        } catch (SQLException throwables) {
            System.out.println("Fehler 1 ");
        }
        int currentValue = 0;

        try {
            assert rs != null;
            rs.next();
            currentValue = rs.getInt(1);
        } catch (SQLException throwables) {
            System.out.println("Fehler 2 ");
        } finally {
            assert rs != null;
            rs.close();
        }
        return currentValue;

    }
}


