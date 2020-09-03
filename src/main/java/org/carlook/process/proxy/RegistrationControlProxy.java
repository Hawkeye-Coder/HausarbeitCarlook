package org.carlook.process.proxy;

import org.carlook.model.objects.dto.UserDTO;
import org.carlook.process.Interfaces.RegistrationControlInterface;
import org.carlook.process.control.RegistrationControl;
import org.carlook.process.exceptions.DatabaseException;
import org.carlook.process.exceptions.EmailInUseException;
import org.carlook.process.exceptions.EmptyFieldException;
import org.carlook.process.exceptions.NoEqualPasswordException;

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

    public void checkValid(String email, boolean emailBool, String password1, String password2, boolean password1Bool, boolean password2Bool, boolean checkBox) throws NoEqualPasswordException, DatabaseException, EmailInUseException, EmptyFieldException, SQLException {
        RegistrationControl.getInstance().checkValid(email, emailBool, password1, password2, password1Bool, password2Bool, checkBox);
    }

    //User registrieren
    public void registerUser( UserDTO userDTO ) throws DatabaseException, SQLException {
        RegistrationControl.getInstance().registerUser(userDTO);
    }

    //User Löschen
    public void deleteUser(UserDTO userDTO){
        RegistrationControl.getInstance().deleteUser(userDTO);
    }
}
