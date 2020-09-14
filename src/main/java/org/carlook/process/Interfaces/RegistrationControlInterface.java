package org.carlook.process.Interfaces;

import org.carlook.model.objects.dto.UserDTO;
import org.carlook.process.exceptions.*;

import java.sql.SQLException;

public interface RegistrationControlInterface {

    void checkValid(String vorname, boolean vornameBool, String nachname, boolean nachnameBool, String email,
                    boolean emailBool, String password1, String password2, boolean password1Bool,
                    boolean password2Bool, boolean roleButtonBool, String roleButton) throws NoEqualPasswordException, DatabaseException, EmailInUseException, EmptyFieldException, SQLException, NoVertrieblerException;

    //User registrieren
    void registerUser( UserDTO userDTO ) throws DatabaseException, SQLException;

    //User Löschen
    void deleteUser(UserDTO userDTO);

    void deleteVertriebler(UserDTO user);
}
