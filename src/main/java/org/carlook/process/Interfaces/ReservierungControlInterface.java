package org.carlook.process.Interfaces;

import org.carlook.model.objects.dto.AutoDTO;
import org.carlook.model.objects.dto.KundeDTO;
import org.carlook.model.objects.dto.UserDTO;
import org.carlook.process.exceptions.DatabaseException;
import org.carlook.process.exceptions.ReservierungException;

import java.sql.SQLException;

public interface ReservierungControlInterface {

    void autoReservieren(AutoDTO stellenanzeige, int id_bewerbung) throws DatabaseException;
    void checkAlreadyApplied(AutoDTO autoDTO, UserDTO userDTO) throws ReservierungException, DatabaseException, SQLException;
    int getAnzahlRes(AutoDTO autoDTO) throws DatabaseException, SQLException;
    void deleteReservierung(int id_auto, KundeDTO kundeDTO) throws ReservierungException;
}
