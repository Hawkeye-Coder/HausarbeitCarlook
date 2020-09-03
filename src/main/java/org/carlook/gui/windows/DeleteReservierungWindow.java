package org.carlook.gui.windows;

import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import org.carlook.model.objects.dto.KundeDTO;
import org.carlook.process.exceptions.ReservierungException;
import org.carlook.process.proxy.ReservierungControlProxy;
import org.carlook.services.util.Views;

public class DeleteReservierungWindow extends DeleteWindow {
    //Window zum Löschen von Autoreservierungen

    public DeleteReservierungWindow(KundeDTO kundeDTO, int auto_id) {
        this.setText("Sind Sie sicher, dass Sie die Reservierung rückgängig machen wollen? <br>" );
        this.setDto(kundeDTO);
        this.setListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    ReservierungControlProxy.getInstance().deleteReservierung(auto_id, kundeDTO);
                } catch (ReservierungException e) {
                    Notification.show("DB-Fehler", "Löschen war nicht erfolgreich!", Notification.Type.ERROR_MESSAGE);
                }
                Notification.show("Reservierung gelöscht!", Notification.Type.HUMANIZED_MESSAGE);
                UI.getCurrent().getNavigator().navigateTo(Views.RESERVIERUNG);
                for (Window w : UI.getCurrent().getWindows()) {
                    w.close();
                }
            }
        });
    }
}

