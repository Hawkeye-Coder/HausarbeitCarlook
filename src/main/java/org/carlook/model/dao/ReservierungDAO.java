package org.carlook.model.dao;

import com.vaadin.ui.Notification;
import org.carlook.model.objects.dto.AutoDTO;
import org.carlook.model.objects.dto.KundeDTO;
import org.carlook.model.objects.dto.UserDTO;
import org.carlook.process.exceptions.DatabaseException;
import org.carlook.process.exceptions.ReservierungException;
import org.carlook.services.db.JDBCConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReservierungDAO extends AbstractDAO {
    private static ReservierungDAO reservierungDAO = null;

    private ReservierungDAO() {

    }

    public static ReservierungDAO getInstance() {
        if (reservierungDAO == null) {
            reservierungDAO = new ReservierungDAO();
        }
        return reservierungDAO;
    }
    //Reserviert ein Auto für einen Kunden
    public void autoReservieren(AutoDTO autoDTO, int id_kunde) throws DatabaseException {
        String sql = "INSERT INTO carlook.reservierung (id_kunde, id_auto) " +
                "VALUES (?, ?);";
        PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(sql);
        try {
            statement.setInt(1, id_kunde);
            statement.setInt(2, autoDTO.getAuto_id());
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger((ReservierungDAO.class.getName())).log(Level.SEVERE, null, ex);
        }
    }


    //Überprüft ob ein Kunde ein Auto bereits reserviert hat
    public void checkAlreadyApplied(AutoDTO autoDTO, UserDTO userDTO) throws DatabaseException, SQLException, ReservierungException {
        String sql = "SELECT id_kunde " +
                "FROM carlook.reservierung " +
                "WHERE id_kunde = ? " +
                "AND id_auto = ?";
        PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(sql);
        ResultSet rs = null;

        try {
            statement.setInt(1, userDTO.getId());
            statement.setInt(2, autoDTO.getAuto_id());
            rs = statement.executeQuery();
            if (rs.next()) {
                throw new ReservierungException();
            }

        } catch (SQLException e) {
            Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte kontaktieren Sie den Administrator!", Notification.Type.ERROR_MESSAGE);
        } finally {
            assert rs != null;
            rs.close();
        }
    }
    //Zählt die Anzahl an Reservierungen für ein Auto
    public int getAnzahlRes(AutoDTO autoDTO) throws DatabaseException, SQLException {
        int anzahl_res = 0;
        String sql = "SELECT count(id_auto) " +
                "FROM carlook.reservierung " +
                "WHERE id_auto = ? ;";
        ResultSet rs;
        PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(sql);
        try {
            statement.setInt(1, autoDTO.getAuto_id());
            rs = statement.executeQuery();
        } catch (SQLException throwables) {
            throw new DatabaseException("Fehler im SQL-Befehl: Bitte den Programmierer informieren!");
        }
        try {
            if (rs.next()) {
                anzahl_res = rs.getInt(1);
            }
        } catch (SQLException e) {
            Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!", Notification.Type.ERROR_MESSAGE);
        } finally {

            rs.close();
        }

        return anzahl_res;
    }
    //Löscht eine Reservierung
    public boolean deleteReservierung(int auto_id, KundeDTO kundeDTO) {
        String sql = "DELETE " +
                "FROM carlook.reservierung " +
                "WHERE id_kunde = ? AND id_auto = ? ";
        PreparedStatement statement = this.getPreparedStatement(sql);
        try {
            statement.setInt(1, kundeDTO.getId());
            statement.setInt(2, auto_id);
            statement.executeUpdate();
            return true;

        } catch (SQLException ex) {
            Logger.getLogger((ReservierungDAO.class.getName())).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
