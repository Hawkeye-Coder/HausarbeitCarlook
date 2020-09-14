package org.carlook.gui.windows;

import com.vaadin.ui.*;
import org.carlook.model.objects.dto.AutoDTO;
import org.carlook.model.objects.dto.VertrieblerDTO;
import org.carlook.process.exceptions.AutoException;
import org.carlook.process.proxy.AutoControlProxy;

import java.sql.SQLException;
import java.util.List;

public class CreateAutoWindow extends Window {

    public CreateAutoWindow(AutoDTO autoDTO, Grid<AutoDTO> grid, VertrieblerDTO vertrieblerDTO) {
        super("Neues Auto");
        center();

        //Art
        TextField marke = new TextField("Automarke");
        marke.setValue(autoDTO.getMarke());

        //Baujahr
        TextField baujahr = new TextField("Baujahr");
        int bj = autoDTO.getBaujahr();
        String setBj = String.valueOf(bj);
        baujahr.setValue(setBj);

        //Beschreibung
        TextArea beschreibung = new TextArea("Beschreibung");
        beschreibung.setValue(autoDTO.getBeschreibung());


        //Speichern Button Konfiguration
        Button saveButton = new Button("Speichern");
        saveButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {

                autoDTO.setMarke(marke.getValue());
                autoDTO.setBaujahr(Integer.parseInt(baujahr.getValue()));
                autoDTO.setBeschreibung(beschreibung.getValue());

                try {
                    AutoControlProxy.getInstance().createAuto(autoDTO);
                } catch (AutoException e) {
                    Notification.show("Es ist ein Fehler aufgetreten. Bitte versuchen Sie es erneut!", Notification.Type.ERROR_MESSAGE);
                }
                UI.getCurrent().addWindow(new ConfirmationWindow("Auto erfolgreich gespeichert"));
                List<AutoDTO> list = null;
                try {
                    list = AutoControlProxy.getInstance().getAutoForVertriebler(vertrieblerDTO);
                } catch (SQLException e) {
                    Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!", Notification.Type.ERROR_MESSAGE);
                }
                grid.setItems();
                grid.setItems(list);
                close();
            }
        });

        //Abbrechen Button Konfiguration
        Button abortButton = new Button("Abbrechen");
        abortButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                close();
            }
        });

        //Horizontal
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponent(saveButton);
        horizontalLayout.addComponent(abortButton);

        //Vertikal
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addComponent(marke);
        verticalLayout.addComponent(baujahr);
        verticalLayout.addComponent(beschreibung);
        verticalLayout.addComponent(horizontalLayout);
        verticalLayout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);

        setContent(verticalLayout);
    }
}
