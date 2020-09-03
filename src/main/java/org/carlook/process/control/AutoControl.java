package org.carlook.process.control;

import com.vaadin.ui.UI;
import org.carlook.gui.ui.MyUI;
import org.carlook.model.dao.AutoDAO;
import org.carlook.model.objects.dto.AutoDTO;
import org.carlook.model.objects.dto.KundeDTO;
import org.carlook.model.objects.dto.UserDTO;
import org.carlook.model.objects.dto.VertrieblerDTO;
import org.carlook.process.Interfaces.AutoControlInterface;
import org.carlook.process.exceptions.AutoException;

import java.sql.SQLException;
import java.util.List;

public class AutoControl implements AutoControlInterface {
    private static AutoControl search = null;

    public static AutoControl getInstance() {
        if (search == null) {
            search = new AutoControl();
        }
        return search;
    }

    private AutoControl() {    }

    public List<AutoDTO> getAutoForVertriebler(VertrieblerDTO vertrieblerDTO) throws SQLException {
        return AutoDAO.getInstance().getAutoVertriebler(vertrieblerDTO);
    }

    public List<AutoDTO> getAutoForKunde(KundeDTO kundeDTO) throws SQLException {
        return AutoDAO.getInstance().bereitsReservierteAutos(kundeDTO);

    }

    public void createAuto(AutoDTO autoDTO) throws AutoException {
        UserDTO userDTO = ((MyUI) UI.getCurrent()).getUserDTO();
        boolean result = AutoDAO.getInstance().createAuto(autoDTO, userDTO);
        if (result) {
            return;
        }
        throw new AutoException();
    }

    public void updateAuto(AutoDTO autoDTO) throws AutoException {
        boolean result = AutoDAO.getInstance().updateAuto(autoDTO);
        if (result) {
            return;
        }
        throw new AutoException();
    }

    public void deleteAuto(AutoDTO autoDTO) throws AutoException {
        boolean result = AutoDAO.getInstance().deleteAuto(autoDTO);
        if (result) {
            return;
        }
        throw new AutoException();
    }

    public List<AutoDTO> getAutoForSearch(String suchtext, String filter) throws SQLException {
        return AutoDAO.getInstance().getAutoForSearch(suchtext, filter);
    }


}
