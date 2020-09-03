package org.carlook.process.proxy;

import org.carlook.model.objects.dto.AutoDTO;
import org.carlook.model.objects.dto.KundeDTO;
import org.carlook.model.objects.dto.UserDTO;
import org.carlook.process.Interfaces.ReservierungControlInterface;
import org.carlook.process.control.ReservierungControl;
import org.carlook.process.exceptions.DatabaseException;
import org.carlook.process.exceptions.ReservierungException;

import java.sql.SQLException;

public class ReservierungControlProxy implements ReservierungControlInterface {
    private static ReservierungControlProxy bewerbungControl = null;

    private ReservierungControlProxy() {

    }

    public static ReservierungControlProxy getInstance() {
        if (bewerbungControl == null) {
            bewerbungControl = new ReservierungControlProxy();
        }
        return bewerbungControl;
    }

    public void autoReservieren(AutoDTO autoDTO, int id_kunde) throws DatabaseException {
        ReservierungControl.getInstance().autoReservieren(autoDTO, id_kunde);
    }

    public void checkAlreadyApplied(AutoDTO autoDTO, UserDTO userDTO) throws ReservierungException, DatabaseException, SQLException {
        ReservierungControl.getInstance().checkAlreadyApplied(autoDTO, userDTO);

    }

    public int getAnzahlRes(AutoDTO autoDTO) throws DatabaseException, SQLException {
        return ReservierungControl.getInstance().getAnzahlRes(autoDTO);
    }
    public void deleteReservierung(int auto_id, KundeDTO kundeDTO) throws ReservierungException {
        ReservierungControl.getInstance().deleteReservierung(auto_id, kundeDTO);
    }
}
