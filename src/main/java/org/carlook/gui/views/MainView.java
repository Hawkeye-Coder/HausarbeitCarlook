package org.carlook.gui.views;

import com.vaadin.data.HasValue;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.*;
import org.carlook.gui.components.TopPanel;
import org.carlook.gui.ui.MyUI;
import org.carlook.gui.windows.AutoWindow;
import org.carlook.model.objects.dto.AutoDTO;
import org.carlook.model.objects.dto.UserDTO;
import org.carlook.process.proxy.SearchControlProxy;
import org.carlook.services.util.BuildGrid;

import java.sql.SQLException;
import java.util.List;

public class MainView extends VerticalLayout implements View {

    private AutoDTO selektiert = null;
    private List<AutoDTO> list;
    private String suchtext;


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

        UserDTO userDTO = ( (MyUI)UI.getCurrent() ).getUserDTO();
        this.setUp();
    }

    private void setUp() {
        UserDTO userDTO = ( (MyUI)UI.getCurrent() ).getUserDTO();
        //Top Layer
        this.addComponent( new TopPanel() );
        Label line = new Label("<hr>", ContentMode.HTML);
        this.addComponent(line);
        line.setSizeFull();
        setStyleName("schrift-profil");
        //Tabelle
        final Grid<AutoDTO> grid = new Grid<>("Ihre Treffer");
        grid.setSizeFull();
        grid.setHeightMode(HeightMode.UNDEFINED);
        BuildGrid.buildGrid(grid);
        SingleSelect<AutoDTO> selection = grid.asSingleSelect();
        grid.setStyleName("schrift-tabelle");
        //DetailButton
        Button reservierenButton = new Button("Reservieren", VaadinIcons.ENTER);
        reservierenButton.setEnabled(false);
        reservierenButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if (selection.getValue() == null) {
                    reservierenButton.setEnabled(false);
                }
                selektiert = selection.getValue();
                UI.getCurrent().addWindow( new AutoWindow(selektiert, userDTO) );
            }
        });

        //SearchButton
        Button searchButton = new Button("Suchen", VaadinIcons.SEARCH);
        searchButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        //Combobox
        final ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setPlaceholder("Filtern nach");
        comboBox.setItems("Marke", "Baujahr", "Beschreibung");
        comboBox.setSelectedItem("Marke");
        //SelectionListener Tabelle
        grid.addSelectionListener(new SelectionListener<AutoDTO>() {
            @Override
            public void selectionChange(SelectionEvent<AutoDTO> event) {
                if (selection.getValue() == null) {
                    reservierenButton.setEnabled(false);
                } else {
                    System.out.println("Zeile selektiert: " + selection.getValue());
                    selektiert = selection.getValue();
                    reservierenButton.setEnabled(true);
                }
            }
        });

        //Suchfeld
        final TextField search = new TextField();
        search.setWidth("300");
        search.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
            @Override
            public void valueChange(HasValue.ValueChangeEvent<String> valueChangeEvent) {
                search(search, comboBox, grid, reservierenButton);
            }
        });

        //SearchButton Config
        searchButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                suchtext = search.getValue();
                if(suchtext.equals("")) {
                    try {
                        list = SearchControlProxy.getInstance().getAnzeigenForSearch(suchtext, comboBox.getValue());
                    } catch (SQLException e) {
                        Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!");
                    }
                    grid.setItems(list);
                    addComponent(grid);
                    addComponent(reservierenButton);
                    setComponentAlignment(reservierenButton, Alignment.MIDDLE_CENTER);
                } else {
                    search(search, comboBox, grid, reservierenButton);
                }
            }
        });

        //Horizontal Layout
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponent(comboBox);
        horizontalLayout.addComponent(search);
        horizontalLayout.addComponent(searchButton);
        horizontalLayout.setComponentAlignment(search, Alignment.MIDDLE_CENTER);
        horizontalLayout.setComponentAlignment(searchButton, Alignment.MIDDLE_CENTER);

        //Darstellen
        this.addComponent(horizontalLayout);
        this.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);
    }

    private void search(TextField search, ComboBox<String> comboBox, Grid<AutoDTO> grid, Button detailButton) {
        if (search.getValue().length() > 1) {
            suchtext = search.getValue();
            String filter = comboBox.getValue();
            try {
                list = SearchControlProxy.getInstance().getAnzeigenForSearch(suchtext, filter);
            } catch (SQLException e) {
                Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!");
            }
            grid.setItems();
            grid.setItems(list);
            addComponent(grid);
            addComponent(detailButton);
            setComponentAlignment(detailButton, Alignment.MIDDLE_CENTER);
        } else {
            removeComponent(grid);
            removeComponent(detailButton);
        }
    }


}
