package org.carlook.process.Interfaces;

import org.carlook.model.objects.dto.AutoDTO;
import org.carlook.model.objects.dto.KundeDTO;
import org.carlook.model.objects.dto.VertrieblerDTO;
import org.carlook.process.exceptions.AutoException;

import java.sql.SQLException;
import java.util.List;

public interface AutoControlInterface {

    List<AutoDTO> getAutoForVertriebler(VertrieblerDTO vertrieblerDTO) throws SQLException;

    List<AutoDTO> getAutoForKunde(KundeDTO kundeDTO) throws SQLException;

    void createAuto(AutoDTO autoDTO) throws AutoException;

    void updateAuto(AutoDTO autoDTO) throws AutoException;

    void deleteAuto(AutoDTO autoDTO) throws AutoException;

    List<AutoDTO> getAutoForSearch(String suchtext, String filter) throws SQLException;


}
