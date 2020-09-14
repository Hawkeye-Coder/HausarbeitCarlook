package org.carlook.gui.windows;

import com.vaadin.ui.Button;
import org.carlook.model.objects.dto.UserDTO;
import org.carlook.process.proxy.LoginControlProxy;
import org.carlook.process.proxy.RegistrationControlProxy;
import org.carlook.services.util.Roles;

public class DeleteProfileWindow extends DeleteWindow {

    //Window zum Löschen von Usern
    public DeleteProfileWindow(UserDTO user) {
        if(user.hasRole(Roles.KUNDE)) {
            this.setText("Sind Sie sicher, dass Sie Ihr Profil löschen wollen? <br>" +
                    "Dieser Vorgang ist endgültig und löscht auch alle mit Ihrem Profil reservierten Autos!");
        }
        else{
            this.setText("Sind Sie sicher, dass Sie Ihr Profil löschen wollen? <br>" +
                    "Dieser Vorgang ist endgültig und löscht auch alle mit Ihrem Profil erzeugten Autos!");
        }
        this.setListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if(user.hasRole(Roles.KUNDE)) {
                    RegistrationControlProxy.getInstance().deleteUser(user);
                    LoginControlProxy.getInstance().logoutUser();
                } else{
                    RegistrationControlProxy.getInstance().deleteVertriebler(user);
                    LoginControlProxy.getInstance().logoutUser();
                }
            }
        });
    }
}
