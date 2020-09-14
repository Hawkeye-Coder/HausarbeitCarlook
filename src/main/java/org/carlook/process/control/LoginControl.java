package org.carlook.process.control;

import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import org.carlook.gui.ui.MyUI;
import org.carlook.model.dao.RegisterDAO;
import org.carlook.model.dao.UserDAO;
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
        UserDAO.getInstance().checkAuthentification(email, password);
    }

    public void logoutUser() {
        UI.getCurrent().close();
        UI.getCurrent().getPage().setLocation(Views.MAIN);
    }
}
