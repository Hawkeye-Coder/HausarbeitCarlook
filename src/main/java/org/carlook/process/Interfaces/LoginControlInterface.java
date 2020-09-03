package org.carlook.process.Interfaces;

import org.carlook.process.exceptions.DatabaseException;
import org.carlook.process.exceptions.NoSuchUserOrPassword;

import java.sql.SQLException;

public interface LoginControlInterface {

    void checkAuthentification( String email, String password) throws NoSuchUserOrPassword, DatabaseException, SQLException;
    void logoutUser();
}
