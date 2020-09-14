package org.carlook.gui.views;


import com.vaadin.data.Binder;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.carlook.gui.components.TopPanel;
import org.carlook.model.objects.dto.UserDTO;
import org.carlook.process.exceptions.DatabaseException;
import org.carlook.process.exceptions.NoSuchUserOrPassword;
import org.carlook.process.proxy.LoginControlProxy;
import org.carlook.services.util.Views;

import java.sql.SQLException;

public class LoginView extends VerticalLayout implements View {
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.setUp();
    }

    private void setUp() {
        //Top Layer
        this.addComponent(new TopPanel());
        Label line = new Label("<hr>", ContentMode.HTML);
        this.addComponent(line);
        line.setSizeFull();

        //Login Felder
        final TextField userLogin = new TextField("Email:");
        userLogin.focus();
        userLogin.setPlaceholder("Email-Adresse");
        Binder<UserDTO> binder = new Binder<>();
        binder.forField(userLogin)
                .withValidator(new EmailValidator("Bitte geben Sie eine gültige Emailadresse ein!"))
                .bind(UserDTO::getEmail, UserDTO::setEmail);
        final PasswordField passwordField = new PasswordField("Passwort:");
        passwordField.setPlaceholder("Passwort");

        //Login Button
        Button loginButton = new Button("Login");
        loginButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        loginButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                String email = userLogin.getValue();
                String password = passwordField.getValue();

                try {
                    //Daten prüfen
                    LoginControlProxy.getInstance().checkAuthentification(email, password);
                    //Rückmeldungen abfangen
                } catch (NoSuchUserOrPassword noSuchUserOrPassword) {
                    Notification.show("Benutzer-Fehler", "Es konnte kein User mit diesen Daten gefunden werden, bitte überpüfen Sie ihre Eingabe!", Notification.Type.ERROR_MESSAGE);
                } catch (DatabaseException e) {
                    Notification.show("DB-Fehler", e.getReason(), Notification.Type.ERROR_MESSAGE);
                } catch (SQLException e) {
                    Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!", Notification.Type.ERROR_MESSAGE);
                }
            }
        });

        //RegisterButton
        Button registerButton = new Button("Zur Registration");
        registerButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                UI.getCurrent().getNavigator().navigateTo(Views.REGISTRATION);
            }
        });

        //HorizontalLayout
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponent(loginButton);
        horizontalLayout.addComponent(registerButton);

        //Vertical Layout
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addComponent(userLogin);
        verticalLayout.addComponent(passwordField);
        verticalLayout.addComponent(horizontalLayout);

        //Login Panel
        Panel panel = new Panel( "Bitte Login-Daten eingeben:");
        panel.setContent(verticalLayout);
        panel.setSizeUndefined();

        //Einfügen
        this.addComponent(panel);
        this.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);


    }
}
