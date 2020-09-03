package org.carlook.gui.windows;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.carlook.model.objects.dto.AbstractDTO;

public class DeleteWindow extends Window {
    private String text;
    private AbstractDTO dto;
    private Button.ClickListener listener;
    //Allgemeines Fenster für Löschvorgänge
    public DeleteWindow() {

    }

    public DeleteWindow(DeleteWindow window) {
        center();

        VerticalLayout verticalLayout = new VerticalLayout();
        Panel panel = new Panel();
        panel.setWidth("700");
        panel.setContent(new Label( window.getText(), ContentMode.HTML));
        verticalLayout.addComponent(panel);

        //OK Button
        Button okButton = new Button("Ok");
        okButton.addClickListener(window.getListener());

        //Abbruch Button
        Button abortButton = new Button("Abbrechen");
        abortButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                close();
            }
        });

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponent(okButton);
        horizontalLayout.addComponent(abortButton);
        verticalLayout.addComponent(horizontalLayout);
        verticalLayout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);
        setContent(verticalLayout);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public AbstractDTO getDto() {
        return dto;
    }

    public void setDto(AbstractDTO dto) {
        this.dto = dto;
    }

    public Button.ClickListener getListener() {
        return listener;
    }

    public void setListener(Button.ClickListener listener) {
        this.listener = listener;
    }
}
