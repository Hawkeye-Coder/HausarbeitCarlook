package org.carlook.model.dao;

import org.carlook.model.objects.dto.KundeDTO;
import org.carlook.model.objects.dto.UserDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KundeDAO extends AbstractDAO {

    private static KundeDAO dao = null;

    private KundeDAO() {

    }

    public static KundeDAO getInstance() {
        if (dao == null) {
            dao = new KundeDAO();
        }
        return dao;
    }

    // Aktualisiert die Daten eines Kunden
    public boolean updateKunde(KundeDTO kundeDTO) {
        String sql = "UPDATE carlook.kunden " +
                "SET vorname = ?, name = ?, anrede = ? " +
                "WHERE id = ?;";
        PreparedStatement statement = this.getPreparedStatement(sql);
        try {
            statement.setString(1, kundeDTO.getVorname());
            statement.setString(2, kundeDTO.getName());
            statement.setString(3, kundeDTO.getAnrede());
            statement.setInt(4, kundeDTO.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            return false;
        }
        String sql2 = "UPDATE carlook.user " +
                "SET email = ?, password = ? " +
                "WHERE id = ? ;";
        statement = this.getPreparedStatement(sql2);
        try {
            statement.setString(1, kundeDTO.getEmail());
            statement.setString(2, kundeDTO.getPassword());
            statement.setInt(3, kundeDTO.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }

    }
    // Sucht Kunden und User Daten aus der Datenbank
    public KundeDTO getAllDataKunde(UserDTO userDTO) throws SQLException {
        KundeDTO kundeDTO = new KundeDTO(userDTO);
        String sql = "SELECT * " +
                "FROM carlook.kunden " +
                "WHERE id = ? ;";
        PreparedStatement statement = this.getPreparedStatement(sql);
        ResultSet rs;
        try {
            statement.setInt(1, userDTO.getId());
            rs = statement.executeQuery();

        } catch (SQLException ex) {
            Logger.getLogger((KundeDAO.class.getName())).log(Level.SEVERE, null, ex);
            return null;
        }

        try {

            while (rs.next()) {
                kundeDTO.setAnrede(rs.getString(4));
                kundeDTO.setVorname(rs.getString(2));
                kundeDTO.setName(rs.getString(3));
                kundeDTO.setId(rs.getInt(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger((KundeDAO.class.getName())).log(Level.SEVERE, null, ex);
            return null;

        }
        String sql2 = "SELECT * " +
                "FROM carlook.user " +
                "WHERE id = ? ;";
        statement = this.getPreparedStatement(sql2);
        try {
            statement.setInt(1, userDTO.getId());
            rs = statement.executeQuery();

        } catch (SQLException ex) {
            Logger.getLogger((KundeDAO.class.getName())).log(Level.SEVERE, null, ex);
            return null;
        }
        try {
            while (rs.next()) {
                kundeDTO.setEmail(rs.getString(2));
                kundeDTO.setPassword(rs.getString(3));

            }
            return kundeDTO;
        } catch (SQLException ex) {
            Logger.getLogger((KundeDAO.class.getName())).log(Level.SEVERE, null, ex);
            return null;

        } finally {
            rs.close();
        }


    }

}
