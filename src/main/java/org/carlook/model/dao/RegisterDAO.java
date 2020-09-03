package org.carlook.model.dao;

import org.carlook.model.objects.dto.UserDTO;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class RegisterDAO extends AbstractDAO {
    private static RegisterDAO dao = null;

    private RegisterDAO() {

    }

    public static RegisterDAO getInstance() {
        if (dao == null) {
            dao = new RegisterDAO();
        }
        return dao;
    }
    //Registriert User in der DB
    public boolean addUser(UserDTO userDTO) {
        String sql = "INSERT INTO carlook.user VALUES (default,?,?)";
        PreparedStatement statement = this.getPreparedStatement(sql);

        try {
            statement.setString(1, userDTO.getEmail());
            statement.setString(2, userDTO.getPassword());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }
    //Registriert Kunden in der DB
    public boolean addKunde(UserDTO userDTO) {
        String sql = "INSERT INTO carlook.kunden VALUES (?,?,?,?)";
        PreparedStatement statement = this.getPreparedStatement(sql);

        try {
            statement.setInt(1, userDTO.getId());
            statement.setString(2, userDTO.getVorname());
            statement.setString(3, userDTO.getName());
            statement.setString(4, userDTO.getAnrede());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }
    //Registriert Vertriebler in der DB
    public boolean addVertriebler(UserDTO userDTO) {
        String sql = "INSERT INTO carlook.vertriebler VALUES (?,?,?,?)";
        PreparedStatement statement = this.getPreparedStatement(sql);

        try {
            statement.setInt(1, userDTO.getId());
            statement.setString(2, userDTO.getVorname());
            statement.setString(3, userDTO.getName());
            statement.setString(4, userDTO.getAnrede());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }
    //Lösche User
    public void deleteUser(UserDTO userDTO) {
            String sql = "DELETE " +
                  "FROM carlook.user u" +
                  "WHERE u.id = ? ;";
        try {
            PreparedStatement statement = this.getPreparedStatement(sql);
            statement.setInt(1, userDTO.getId());
            statement.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger((RegisterDAO.class.getName())).log(Level.SEVERE, null, ex);
        }
    }
}
