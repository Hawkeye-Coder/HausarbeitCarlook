package org.carlook.gui.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.carlook.gui.components.TopPanel;
import org.carlook.gui.ui.MyUI;
import org.carlook.gui.windows.ConfirmationWindow;
import org.carlook.gui.windows.DeleteProfileWindow;
import org.carlook.gui.windows.DeleteWindow;
import org.carlook.model.objects.dto.KundeDTO;
import org.carlook.model.objects.dto.UserDTO;
import org.carlook.model.objects.dto.VertrieblerDTO;
import org.carlook.process.exceptions.ProfileException;
import org.carlook.process.proxy.ProfileControlProxy;
import org.carlook.services.util.Roles;

import java.sql.SQLException;

public class ProfileView extends VerticalLayout implements View {

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        UserDTO userDTO = ((MyUI) UI.getCurrent()).getUserDTO();
        this.setUp();
    }

    private void setUp() {
        //Top Layer
        this.addComponent(new TopPanel());
        Label line = new Label("<hr>", ContentMode.HTML);
        this.addComponent(line);
        line.setSizeFull();
        this.addStyleName("schrift-profil");
        UserDTO userDTO = ((MyUI) UI.getCurrent()).getUserDTO();
        setStyleName("schrift-profil");
        //Felder Kunde erzeugen

        final NativeSelect<String> anrede = new NativeSelect<>("Anrede");
        anrede.setItems("-", "Herr", "Frau");

        final TextField vorname = new TextField("Vorname");
        vorname.setPlaceholder("Max");

        final TextField name = new TextField("Name");
        name.setPlaceholder("Mustermann");
        final TextField email = new TextField("Email");
        final PasswordField passwordField = new PasswordField("Passwort");
        Label meinProfil = new Label("Mein Profil");


        if (userDTO.hasRole(Roles.KUNDE)) {
            //Werte einsetzen
            KundeDTO kundeDTO = new KundeDTO(userDTO);
            try {
                kundeDTO = ProfileControlProxy.getInstance().getKunde(userDTO);
            } catch (SQLException e) {
                Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!", Notification.Type.ERROR_MESSAGE);
            }
            if (kundeDTO.getAnrede() != null) {
                anrede.setValue(kundeDTO.getAnrede());
            }
            if (kundeDTO.getVorname() != null) {
                vorname.setValue(kundeDTO.getVorname());
            }
            if (kundeDTO.getName() != null) {
                name.setValue(kundeDTO.getName());
            }
            if (kundeDTO.getEmail() != null) {
                email.setValue(kundeDTO.getEmail());
            }
            if (kundeDTO.getPassword() != null) {
                passwordField.setValue(kundeDTO.getPassword());
            }

        } else {
            //Werte Setzen
            VertrieblerDTO vertrieblerDTO = new VertrieblerDTO(userDTO);
            try {
                vertrieblerDTO = ProfileControlProxy.getInstance().getVertriebler(userDTO);
            } catch (SQLException e) {
                Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!", Notification.Type.ERROR_MESSAGE);
            }
            if (vertrieblerDTO.getAnrede() != null) {
                anrede.setValue(vertrieblerDTO.getAnrede());
            }
            if (vertrieblerDTO.getVorname() != null) {
                vorname.setValue(vertrieblerDTO.getVorname());
            }
            if (vertrieblerDTO.getName() != null) {
                name.setValue(vertrieblerDTO.getName());
            }
            if (vertrieblerDTO.getEmail() != null) {
                email.setValue(vertrieblerDTO.getEmail());
            }
            if (vertrieblerDTO.getPassword() != null) {
                passwordField.setValue(vertrieblerDTO.getPassword());
            }

        }

        //Event Nutzer löschen
        Button deleteButton = new Button("Profil löschen");
        deleteButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                DeleteProfileWindow deleteProfileWindow = new DeleteProfileWindow();
                UI.getCurrent().addWindow(new DeleteWindow(deleteProfileWindow));
            }
        });

        //Event Nutzerdaten updaten
        Button overwriteButton = new Button("Daten aktualisieren");
        overwriteButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {

                if (userDTO.hasRole(Roles.KUNDE)) {
                    //UI.getCurrent().addWindow(new ConfirmationWindow("Sollen alle Daten aktualisiert werden?"));
                    KundeDTO kundeDTO = new KundeDTO(userDTO);
                    kundeDTO.setAnrede(anrede.getValue());
                    kundeDTO.setVorname(vorname.getValue());
                    kundeDTO.setName(name.getValue());
                    kundeDTO.setEmail(email.getValue());
                    kundeDTO.setPassword(passwordField.getValue());
                    try {
                        ProfileControlProxy.getInstance().updateKundeDaten(kundeDTO);
                        UI.getCurrent().addWindow(new ConfirmationWindow("Ihr Profil wurde erfolgreich aktualisiert!"));
                    } catch (ProfileException e) {
                        Notification.show("Es ist ein Fehler aufgetreten. Bitte versuchen Sie es erneut!", Notification.Type.ERROR_MESSAGE);
                    }

                } else {

                    VertrieblerDTO vertrieblerDTO = new VertrieblerDTO(userDTO);
                    vertrieblerDTO.setAnrede(anrede.getValue());
                    vertrieblerDTO.setVorname(vorname.getValue());
                    vertrieblerDTO.setName(name.getValue());
                    vertrieblerDTO.setEmail(email.getValue());
                    vertrieblerDTO.setPassword(passwordField.getValue());
                    try {
                        ProfileControlProxy.getInstance().updateVertrieblerDaten(vertrieblerDTO);
                        UI.getCurrent().addWindow(new ConfirmationWindow("Ihr Profil wurde erfolgreich aktualisiert!"));
                    } catch (ProfileException e) {
                        Notification.show("Es ist ein Fehler aufgetreten. Bitte versuchen Sie es erneut!", Notification.Type.ERROR_MESSAGE);
                    }

                }
            }
        });

        //Layout generieren
        HorizontalLayout horizontalLayoutName = new HorizontalLayout();
        horizontalLayoutName.addComponent(anrede);
        horizontalLayoutName.addComponent(vorname);
        horizontalLayoutName.addComponent(name);
        horizontalLayoutName.addComponent(email);
        horizontalLayoutName.addComponent(passwordField);

        //Komponenten hinzufügen
        this.addComponent(meinProfil);
        this.addComponent(horizontalLayoutName);
        this.addComponent(overwriteButton);
        this.addComponent(deleteButton);


    }
}
