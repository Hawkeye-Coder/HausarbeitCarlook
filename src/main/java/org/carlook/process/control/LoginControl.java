package org.carlook.process.control;

import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import org.carlook.gui.ui.MyUI;
import org.carlook.model.objects.dto.KundeDTO;
import org.carlook.model.objects.dto.UserDTO;
import org.carlook.model.objects.dto.VertrieblerDTO;
import org.carlook.process.Interfaces.LoginControlInterface;
import org.carlook.process.exceptions.DatabaseException;
import org.carlook.process.exceptions.NoSuchUserOrPassword;
import org.carlook.services.db.JDBCConnection;
import org.carlook.services.util.Roles;
import org.carlook.services.util.Views;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginControl implements LoginControlInterface {
    private static LoginControl loginControl = null;

    private LoginControl(){
    }
    public static LoginControl getInstance(){
        if(loginControl == null){
            loginControl = new LoginControl();
        }
        return loginControl;
    }

    public void checkAuthentification( String email, String password) throws NoSuchUserOrPassword, DatabaseException, SQLException {
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
        } catch (SQLException throwables) {
            Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!", Notification.Type.ERROR_MESSAGE);
        }
        finally {
            JDBCConnection.getInstance().closeConnection();
            rs.close();
        }
        ((MyUI) UI.getCurrent() ).setUserDTO(userDTO); //Mockito zum Testen
        UI.getCurrent().getNavigator().navigateTo(Views.MAIN);
    }

    public void logoutUser() {
        UI.getCurrent().close();
        UI.getCurrent().getPage().setLocation(Views.MAIN);
    }
}
