package org.carlook.process.Interfaces;

import org.carlook.model.objects.dto.UserDTO;
import org.carlook.process.exceptions.DatabaseException;
import org.carlook.process.exceptions.EmailInUseException;
import org.carlook.process.exceptions.EmptyFieldException;
import org.carlook.process.exceptions.NoEqualPasswordException;

import java.sql.SQLException;

public interface RegistrationControlInterface {

    void checkValid(String email, boolean emailBool, String password1, String password2, boolean password1Bool, boolean password2Bool, boolean checkBox) throws NoEqualPasswordException, DatabaseException, EmailInUseException, EmptyFieldException, SQLException;

    //User registrieren
    void registerUser( UserDTO userDTO ) throws DatabaseException, SQLException;

    //User Löschen
    void deleteUser(UserDTO userDTO);
}
