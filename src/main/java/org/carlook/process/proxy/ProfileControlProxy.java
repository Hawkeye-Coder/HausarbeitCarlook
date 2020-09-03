package org.carlook.process.proxy;

import org.carlook.model.objects.dto.KundeDTO;
import org.carlook.model.objects.dto.UserDTO;
import org.carlook.model.objects.dto.VertrieblerDTO;
import org.carlook.process.Interfaces.ProfileControlInterface;
import org.carlook.process.control.ProfileControl;
import org.carlook.process.exceptions.ProfileException;

import java.sql.SQLException;

public class ProfileControlProxy implements ProfileControlInterface {
    private static ProfileControlProxy profileControl = null;

    private ProfileControlProxy() {
    }

    public static ProfileControlProxy getInstance() {
        if (profileControl == null) {
            profileControl = new ProfileControlProxy();
        }
        return profileControl;
    }


    public void updateKundeDaten(KundeDTO kundeDTO) throws ProfileException {
        ProfileControl.getInstance().updateKundeDaten(kundeDTO);
    }

    public void updateVertrieblerDaten(VertrieblerDTO vertrieblerDTO) throws ProfileException {
        ProfileControl.getInstance().updateVertrieblerDaten(vertrieblerDTO);
    }

    public KundeDTO getKunde(UserDTO userDTO) throws SQLException {
        return ProfileControl.getInstance().getKunde(userDTO);
    }

    public VertrieblerDTO getVertriebler(UserDTO userDTO) throws SQLException {
        return ProfileControl.getInstance().getVertriebler(userDTO);
    }


}
