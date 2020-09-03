package org.carlook.model.dao;

import org.carlook.model.objects.dto.UserDTO;
import org.carlook.model.objects.dto.VertrieblerDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VertrieblerDAO extends AbstractDAO {

    private static VertrieblerDAO dao = null;

    private VertrieblerDAO() {

    }

    public static VertrieblerDAO getInstance() {
        if (dao == null) {
            dao = new VertrieblerDAO();
        }
        return dao;
    }
    //Aktualisiert Vertriebler und User Daten
    public boolean updateVertriebler(VertrieblerDTO vertrieblerDTO) {

        String sql = "UPDATE carlook.vertriebler " +
                "SET vorname = ?, name= ?, anrede = ? " +
                "WHERE id = ? ;";

        PreparedStatement statement = this.getPreparedStatement(sql);

        try {

            statement.setString(1, vertrieblerDTO.getVorname());
            statement.setString(2, vertrieblerDTO.getName());
            statement.setString(3, vertrieblerDTO.getAnrede());
            statement.setInt(4, vertrieblerDTO.getId());
            statement.executeUpdate();

        } catch (SQLException ex) {
            return false;
        }

        String sql2 = "UPDATE carlook.user " +
                "SET email = ?, password = ? " +
                "WHERE id = ? ;";

        statement = this.getPreparedStatement(sql2);

        try {
            statement.setString(1, vertrieblerDTO.getEmail());
            statement.setString(2, vertrieblerDTO.getPassword());
            statement.setInt(3, vertrieblerDTO.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    //Sucht Vertriebler- und Userdaten aus der Datenbank
    public VertrieblerDTO getAllDataVertriebler(UserDTO userDTO) throws SQLException {
        VertrieblerDTO vertrieblerDTO = new VertrieblerDTO(userDTO);
        String sql = "SELECT * " +
                "FROM carlook.vertriebler " +
                "WHERE id = ? ;";

        PreparedStatement statement = this.getPreparedStatement(sql);
        ResultSet rs;

        try {
            statement.setInt(1, userDTO.getId());
            rs = statement.executeQuery();

        } catch (SQLException ex) {
            Logger.getLogger((VertrieblerDAO.class.getName())).log(Level.SEVERE, null, ex);
            return null;
        }

        try {

            while (rs.next()) {
                vertrieblerDTO.setId(rs.getInt(1));
                vertrieblerDTO.setAnrede(rs.getString(4));
                vertrieblerDTO.setVorname(rs.getString(2));
                vertrieblerDTO.setName(rs.getString(3));
            }

        } catch (SQLException ex) {
            Logger.getLogger((VertrieblerDAO.class.getName())).log(Level.SEVERE, null, ex);
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
                vertrieblerDTO.setEmail(rs.getString(2));
                vertrieblerDTO.setPassword(rs.getString(3));

            }

            return vertrieblerDTO;

        } catch (SQLException ex) {
            Logger.getLogger((KundeDAO.class.getName())).log(Level.SEVERE, null, ex);
            return null;

        } finally {
            rs.close();
        }
    }
}
