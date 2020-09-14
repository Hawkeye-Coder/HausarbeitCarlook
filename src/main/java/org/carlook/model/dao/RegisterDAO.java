package org.carlook.model.dao;

import org.carlook.model.objects.dto.UserDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

    //Fügt einen neuen User in die DB ein
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

    //Fügt einen neuen Kunden in die DB ein
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

    //Fügt einen neuen Vertriebler in die DB ein
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
                "FROM carlook.user u " +
                "WHERE u.id = ? ;";
        try {
            PreparedStatement statement = this.getPreparedStatement(sql);
            statement.setInt(1, userDTO.getId());
            statement.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger((RegisterDAO.class.getName())).log(Level.SEVERE, null, ex);
        }
    }
    //Löscht Vertriebler mit allen erstellten Autos
    public void deleteVertriebler(UserDTO user) {
        String sql1 = "SELECT id_auto " +
                "FROM carlook.auto_to_vertriebler atv " +
                "WHERE atv.id_vertriebler = ? ;";
        List<Integer> list = new ArrayList<>();
        try {

            PreparedStatement statement = this.getPreparedStatement(sql1);
            statement.setInt(1, user.getId());
            ResultSet rs = null;
            rs = statement.executeQuery();

            while (rs.next()) {
                list.add(rs.getInt(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger((RegisterDAO.class.getName())).log(Level.SEVERE, null, ex);
        }
        for (int i : list) {
            String sql2 = "DELETE " +
                    "FROM carlook.auto a " +
                    "WHERE a.id = ? ;";
            PreparedStatement statement = this.getPreparedStatement(sql2);
            try {
                statement.setInt(1, i);
                statement.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger((RegisterDAO.class.getName())).log(Level.SEVERE, null, ex);
            }
        }
        this.deleteUser(user);
    }
}
