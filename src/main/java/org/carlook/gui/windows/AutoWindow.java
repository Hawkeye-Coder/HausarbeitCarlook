package org.carlook.gui.windows;

import com.vaadin.ui.*;
import org.carlook.model.objects.dto.AutoDTO;
import org.carlook.model.objects.dto.UserDTO;
import org.carlook.model.objects.dto.VertrieblerDTO;
import org.carlook.process.exceptions.AutoException;
import org.carlook.process.exceptions.DatabaseException;
import org.carlook.process.exceptions.ReservierungException;
import org.carlook.process.proxy.AutoControlProxy;
import org.carlook.process.proxy.ReservierungControlProxy;

import java.sql.SQLException;
import java.util.List;

public class AutoWindow extends Window {
    private TextField marke;
    private TextField baujahr;
    private TextArea beschreibung;

    public AutoWindow(AutoDTO autoDTO, UserDTO userDTO) {

        super(autoDTO.getMarke());
        center();

        //Marke
        marke = new TextField("Marke");
        marke.setValue(autoDTO.getMarke());
        marke.setReadOnly(true);

        //Baujahr
        baujahr = new TextField("Baujahr");
        int baujahrvalue = autoDTO.getBaujahr();
        String setBj = String.valueOf(baujahrvalue);
        baujahr.setValue(setBj);
        baujahr.setReadOnly(true);

        //Beschreibung
        beschreibung = new TextArea("Beschreibung");
        beschreibung.setValue(autoDTO.getBeschreibung());
        beschreibung.setReadOnly(true);

        //OkButton
        Button okButton = new Button("Ok");
        okButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                close();
            }
        });

        //BewerbenButton
        Button reservierenButton = new Button("Reservieren");

        reservierenButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    ReservierungControlProxy.getInstance().checkAlreadyApplied(autoDTO, userDTO);
                    ReservierungControlProxy.getInstance().autoReservieren(autoDTO, userDTO.getId());
                } catch (ReservierungException e) {
                    e.printStackTrace();
                } catch (DatabaseException e) {
                    e.printStackTrace();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                close();
            }
        });

        //Horizontal
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponent(okButton);
        horizontalLayout.addComponent(reservierenButton);

        //Vertikal
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout = this.buildVerticalLayout(verticalLayout, marke, baujahr, beschreibung, horizontalLayout);
        setContent(verticalLayout);
    }

    public AutoWindow(AutoDTO auto, Grid<AutoDTO> grid, VertrieblerDTO vertrieblerDTO) {
        super("Auto bearbeiten");
        center();

        //Marke
        marke = new TextField("Marke");
        marke.setValue(auto.getMarke());

        //Baujahr
        baujahr = new TextField("Baujahr");
        int bj = auto.getBaujahr();
        String s = String.valueOf(bj);
        baujahr.setValue(s);

        //Beschreibung
        beschreibung = new TextArea("Beschreibung");
        beschreibung.setValue(auto.getBeschreibung());

        //SaveButton
        Button saveButton = new Button("Speichern");
        saveButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                auto.setMarke(marke.getValue());
                auto.setBaujahr(Integer.parseInt(baujahr.getValue()));
                auto.setBeschreibung(beschreibung.getValue());

                try {
                    AutoControlProxy.getInstance().updateAuto(auto);
                } catch (AutoException e) {
                    Notification.show("Es ist ein Fehler beim Update aufgetreten. Bitte versuchen Sie es erneut!", Notification.Type.ERROR_MESSAGE);
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
        verticalLayout = this.buildVerticalLayout(verticalLayout, marke, baujahr, beschreibung, horizontalLayout);
        setContent(verticalLayout);
    }
    public VerticalLayout buildVerticalLayout(VerticalLayout verticalLayout, TextField marke, TextField baujahr,
                                               TextArea beschreibung, HorizontalLayout horizontalLayout ){
        verticalLayout.addComponent(marke);
        verticalLayout.addComponent(baujahr);
        verticalLayout.addComponent(beschreibung);
        verticalLayout.addComponent(horizontalLayout);
        verticalLayout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);
        return verticalLayout;
    }
}
