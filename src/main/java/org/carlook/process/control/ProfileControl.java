package org.carlook.process.control;

import org.carlook.model.dao.KundeDAO;
import org.carlook.model.dao.VertrieblerDAO;
import org.carlook.model.objects.dto.KundeDTO;
import org.carlook.model.objects.dto.UserDTO;
import org.carlook.model.objects.dto.VertrieblerDTO;
import org.carlook.process.Interfaces.ProfileControlInterface;
import org.carlook.process.exceptions.ProfileException;

import java.sql.SQLException;

public class ProfileControl implements ProfileControlInterface {
    private static ProfileControl profileControl = null;

    private ProfileControl() {
    }

    public static ProfileControl getInstance() {
        if (profileControl == null) {
            profileControl = new ProfileControl();
        }
        return profileControl;
    }


    public void updateKundeDaten(KundeDTO kundeDTO) throws ProfileException {
        boolean result =  KundeDAO.getInstance().updateKunde(kundeDTO);
        if (result) {
            return;
        }
        throw new ProfileException();
    }

    public void updateVertrieblerDaten(VertrieblerDTO vertrieblerDTO) throws ProfileException {
        boolean result = VertrieblerDAO.getInstance().updateVertriebler(vertrieblerDTO);
        if (result) {
            return;
        }
        throw new ProfileException();
    }

    public KundeDTO getKunde(UserDTO userDTO) throws SQLException {
        return KundeDAO.getInstance().getAllDataKunde(userDTO);
    }

    public VertrieblerDTO getVertriebler(UserDTO userDTO) throws SQLException {
        return VertrieblerDAO.getInstance().getAllDataVertriebler(userDTO);
    }


}
