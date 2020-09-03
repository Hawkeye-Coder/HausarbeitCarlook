package org.carlook.process.control;

import org.carlook.model.dao.ReservierungDAO;
import org.carlook.model.objects.dto.AutoDTO;
import org.carlook.model.objects.dto.KundeDTO;
import org.carlook.model.objects.dto.UserDTO;
import org.carlook.process.Interfaces.ReservierungControlInterface;
import org.carlook.process.exceptions.DatabaseException;
import org.carlook.process.exceptions.ReservierungException;

import java.sql.SQLException;

public class ReservierungControl implements ReservierungControlInterface {
    private static ReservierungControl reservierungControl = null;

    private ReservierungControl() {

    }

    public static ReservierungControl getInstance() {
        if (reservierungControl == null) {
            reservierungControl = new ReservierungControl();
        }
        return reservierungControl;
    }

    public void autoReservieren(AutoDTO autoDTO, int id_kunde) throws DatabaseException {
        ReservierungDAO.getInstance().autoReservieren(autoDTO, id_kunde);
    }

    public void checkAlreadyApplied(AutoDTO autoDTO, UserDTO userDTO) throws DatabaseException, SQLException, ReservierungException {
    ReservierungDAO.getInstance().checkAlreadyApplied(autoDTO, userDTO);
    }

    public int getAnzahlRes(AutoDTO autoDTO) throws DatabaseException, SQLException {
        return ReservierungDAO.getInstance().getAnzahlRes(autoDTO);
    }
    public void deleteReservierung(int auto_id, KundeDTO kundeDTO) throws ReservierungException {
        boolean result = ReservierungDAO.getInstance().deleteReservierung(auto_id, kundeDTO);
        if (result) {
            return;
        }
        throw new ReservierungException();
    }
}
