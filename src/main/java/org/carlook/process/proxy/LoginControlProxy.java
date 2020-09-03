package org.carlook.process.proxy;

import org.carlook.process.Interfaces.LoginControlInterface;
import org.carlook.process.control.LoginControl;
import org.carlook.process.exceptions.DatabaseException;
import org.carlook.process.exceptions.NoSuchUserOrPassword;

import java.sql.SQLException;

public class LoginControlProxy implements LoginControlInterface {
    private static LoginControlProxy loginControl = null;

    private LoginControlProxy(){
    }
    public static LoginControlProxy getInstance(){
        if(loginControl == null){
            loginControl = new LoginControlProxy();
        }
        return loginControl;
    }

    public void checkAuthentification( String email, String password) throws NoSuchUserOrPassword, DatabaseException, SQLException {
        LoginControl.getInstance().checkAuthentification(email, password);
    }

    public void logoutUser() {
        LoginControl.getInstance().logoutUser();
    }
}
