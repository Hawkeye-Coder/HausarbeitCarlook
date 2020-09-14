package org.carlook.model.dao;

import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import org.carlook.gui.ui.MyUI;
import org.carlook.model.objects.dto.KundeDTO;
import org.carlook.model.objects.dto.UserDTO;
import org.carlook.model.objects.dto.VertrieblerDTO;
import org.carlook.process.control.ProfileControl;
import org.carlook.process.exceptions.DatabaseException;
import org.carlook.process.exceptions.NoSuchUserOrPassword;
import org.carlook.services.db.JDBCConnection;
import org.carlook.services.util.Roles;
import org.carlook.services.util.Views;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO extends AbstractDAO {
    private static UserDAO dao = null;

    private UserDAO() {

    }

    public static UserDAO getInstance() {
        if (dao == null) {
            dao = new UserDAO();
        }
        return dao;
    }
    //Liefter die größte ID aus der User DB
    public int getMaxID() throws SQLException {
        String sql = "SELECT max(id) " +
        "FROM carlook.user ;";
        PreparedStatement statement = getPreparedStatement(sql);
        ResultSet rs = null;

        try {
            rs = statement.executeQuery();
        } catch (SQLException throwables) {
            System.out.println("Fehler 1");
        }

        int currentValue = 0;

        try {
            assert rs != null;
            rs.next();
            currentValue = rs.getInt(1);
        } catch (SQLException throwables) {
            System.out.println("Fehler 2");
        } finally {
            assert rs != null;
            rs.close();
        }
        return currentValue;
    }
    //Führt den Login Vorgang aus
    public void checkAuthentification(String email, String password) throws DatabaseException, SQLException, NoSuchUserOrPassword {
        //Check User vorhanden?
        String sql = "SELECT id " +
                "FROM carlook.user " +
                "WHERE email = ? "+
                "AND password = ? ;";
        ResultSet rs;
        PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(sql);
        try {
            statement.setString(1, email);
            statement.setString(2, password);
            rs = statement.executeQuery();
        } catch (SQLException throwables) {
            throw new DatabaseException("Fehler im SQL-Befehl: Bitte den Programmierer informieren!");
        }

        UserDTO userDTO = null;

        try {
            if( rs.next() ) {
                userDTO = new UserDTO();
                userDTO.setId(rs.getInt(1));
                userDTO.setEmail(email);
                if ( userDTO.hasRole(Roles.KUNDE) ) {
                    userDTO = ProfileControl.getInstance().getKunde(new KundeDTO(userDTO));
                }
                else {
                    userDTO = ProfileControl.getInstance().getVertriebler(new VertrieblerDTO(userDTO));
                }
            }
            else {
                throw new NoSuchUserOrPassword();
            }
        } catch (SQLException | NoSuchUserOrPassword throwables) {
            throw new NoSuchUserOrPassword();
        }
        finally {
            JDBCConnection.getInstance().closeConnection();
            rs.close();
        }
        ((MyUI) UI.getCurrent() ).setUserDTO(userDTO);
        //Login erfolgreich zu ProfileView navigieren
        UI.getCurrent().getNavigator().navigateTo(Views.PROFILE);
    }
}
