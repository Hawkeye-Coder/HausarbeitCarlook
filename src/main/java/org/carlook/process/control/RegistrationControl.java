package org.carlook.process.control;

import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import org.carlook.gui.ui.MyUI;
import org.carlook.gui.windows.ConfirmationWindow;
import org.carlook.model.dao.RegisterDAO;
import org.carlook.model.dao.RoleDAO;
import org.carlook.model.dao.UserDAO;
import org.carlook.model.objects.dto.UserDTO;
import org.carlook.process.Interfaces.RegistrationControlInterface;
import org.carlook.process.exceptions.*;
import org.carlook.services.db.JDBCConnection;
import org.carlook.services.util.Roles;
import org.carlook.services.util.Views;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistrationControl implements RegistrationControlInterface {

    private static RegistrationControl registration = null;
    private RegistrationControl(){
    }

    public static RegistrationControl getInstance(){
        if(registration == null){
            registration = new RegistrationControl();
        }
        return registration;
    }

    public void checkValid(String vorname, boolean vornameBool, String nachname, boolean nachnameBool, String email,
                           boolean emailBool, String password1, String password2, boolean password1Bool,
                           boolean password2Bool, boolean roleButtonBool, String roleButton) throws NoEqualPasswordException, DatabaseException, EmailInUseException, EmptyFieldException, SQLException, NoVertrieblerException {
        String emailCheck = email+" ";
        //Eingabecheck
        if (!vornameBool || !nachnameBool || !emailBool || !password1Bool  || !password2Bool || !roleButtonBool) {
            throw new EmptyFieldException("Bitte ergänzen Sie Ihre Eingaben in den markierten Bereichen!");
        }
        if (roleButton.equals("Vertriebler") && !emailCheck.equals(vorname.toLowerCase()+ "." + nachname.toLowerCase() + "@carlook.de ")) {
            throw new NoVertrieblerException("Ihre Emailadresse entspricht nicht den Anforderungen um sich als Vertriebler registrieren zu können! \n " +
                                            "(Beispiel: vorname.nachname@carlook.de) \n" +
                                            "Bitte überprüfen Sie ihre Email-Adresse oder registrieren Sie sich als Kunde!");
        }
        //Passwortcheck
        if ( !password1.equals(password2) ) {
            throw new NoEqualPasswordException("Passwörter stimmen nicht überein!");
        }

        //DB Zugriff Emailcheck
        String sql = "SELECT email " +
                     "FROM carlook.user " +
                     "WHERE email = ? ;";
        ResultSet rs = null;
        PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(sql);

        try {
            statement.setString(1,email);
            rs = statement.executeQuery();
        } catch (SQLException throwables) {
            Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!", Notification.Type.ERROR_MESSAGE);
        }

        try {
            assert rs != null;
            if (rs.next()) {
                throw new EmailInUseException("Die Email wird bereits benutzt!");
            }
        } catch (EmailInUseException emailInUseException) {
            throw new EmailInUseException("Die Email wird bereits benutzt!");
        } finally {
            assert rs != null;
            rs.close();
        }
    }

    //User registrieren
    public void registerUser( UserDTO userDTO ) throws DatabaseException, SQLException {
        boolean registerUser;
        RegisterDAO.getInstance().addUser(userDTO);
        userDTO.setId(UserDAO.getInstance().getMaxID());

        if (userDTO.getRole().equals(Roles.KUNDE)) {
            RegisterDAO.getInstance().addKunde(userDTO);
            registerUser = RoleDAO.getInstance().setRolesForKunde(userDTO);
        } else {
            RegisterDAO.getInstance().addVertriebler(userDTO);
            registerUser = RoleDAO.getInstance().setRolesForVertriebler(userDTO);
        }

        if (registerUser) {
            UI.getCurrent().addWindow( new ConfirmationWindow("Registration erfolgreich!") );
            ( (MyUI)UI.getCurrent() ).setUserDTO(userDTO);
            UI.getCurrent().getNavigator().navigateTo(Views.PROFILE);
        } else {
            throw new DatabaseException("Fehler bei Abschluß der Registration");
        }

    }

    //User Löschen
    public void deleteUser(UserDTO userDTO){
        RegisterDAO.getInstance().deleteUser(userDTO);
    }

    //Vertriebler löschen
    @Override
    public void deleteVertriebler(UserDTO user) {
        RegisterDAO.getInstance().deleteVertriebler(user);
    }
}
