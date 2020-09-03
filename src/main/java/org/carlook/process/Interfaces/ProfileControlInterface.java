package org.carlook.process.Interfaces;

import org.carlook.model.objects.dto.KundeDTO;
import org.carlook.model.objects.dto.UserDTO;
import org.carlook.model.objects.dto.VertrieblerDTO;
import org.carlook.process.exceptions.ProfileException;

import java.sql.SQLException;

public interface ProfileControlInterface {

    void updateKundeDaten(KundeDTO kundeDTO) throws ProfileException;

    void updateVertrieblerDaten(VertrieblerDTO vertrieblerDTO) throws ProfileException;

    KundeDTO getKunde(UserDTO userDTO) throws SQLException;

    VertrieblerDTO getVertriebler(UserDTO userDTO) throws SQLException;


}
