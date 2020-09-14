package org.carlook.process.proxy;

import org.carlook.model.objects.dto.UserDTO;
import org.carlook.process.Interfaces.RegistrationControlInterface;
import org.carlook.process.control.RegistrationControl;
import org.carlook.process.exceptions.*;

import java.sql.SQLException;

public class RegistrationControlProxy implements RegistrationControlInterface {

    private static RegistrationControlProxy registration = null;
    private RegistrationControlProxy(){
    }

    public static RegistrationControlProxy getInstance(){
        if(registration == null){
            registration = new RegistrationControlProxy();
        }
        return registration;
    }

    public void checkValid(String vorname, boolean vornameBool, String nachname, boolean nachnameBool, String email,
                           boolean emailBool, String password1, String password2, boolean password1Bool,
                           boolean password2Bool, boolean roleButtonBool, String roleButton) throws NoEqualPasswordException, DatabaseException, EmailInUseException, EmptyFieldException, SQLException, NoVertrieblerException {
        RegistrationControl.getInstance().checkValid(vorname, vornameBool, nachname, nachnameBool, email, emailBool, password1, password2, password1Bool, password2Bool, roleButtonBool, roleButton);
    }

    //User registrieren
    public void registerUser( UserDTO userDTO ) throws DatabaseException, SQLException {
        RegistrationControl.getInstance().registerUser(userDTO);
    }

    //User Löschen
    public void deleteUser(UserDTO userDTO){
        RegistrationControl.getInstance().deleteUser(userDTO);
    }

    @Override
    public void deleteVertriebler(UserDTO user) {
        RegistrationControl.getInstance().deleteVertriebler(user);
    }
}
