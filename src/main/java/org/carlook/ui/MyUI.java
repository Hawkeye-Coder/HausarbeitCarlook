package org.carlook.ui;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import org.carlook.gui.views.*;
import org.carlook.model.objects.dto.UserDTO;
import org.carlook.services.util.Views;

import javax.servlet.annotation.WebServlet;


@Theme("mytheme")
@Title("Carlook ltd.")
@PreserveOnRefresh
public class MyUI extends UI {
    private UserDTO userDTO = null;

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        //Hintergrundbild
        setStyleName("hintergrundbild");

        Navigator navi = new Navigator(this,this);

        navi.addView(Views.MAIN, MainView.class);
        navi.addView(Views.PROFILE, ProfileView.class);
        navi.addView(Views.REGISTRATION, RegistrationView.class);
        navi.addView(Views.LOGIN, LoginView.class);
        navi.addView(Views.AUTO, AutoView.class);
        navi.addView(Views.RESERVIERUNG, ReservierungView.class);

        UI.getCurrent().getNavigator().navigateTo(Views.LOGIN);
    }

    public  MyUI getMyUI() {
        return (MyUI) UI.getCurrent();
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
