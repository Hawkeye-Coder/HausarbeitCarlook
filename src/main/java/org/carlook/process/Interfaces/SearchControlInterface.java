package org.carlook.process.Interfaces;

import org.carlook.model.objects.dto.AutoDTO;

import java.sql.SQLException;
import java.util.List;

public interface SearchControlInterface {

    List<AutoDTO> getAnzeigenForUser() throws SQLException;

    List<AutoDTO> getAnzeigenForSearch(String suchtext, String filter) throws SQLException;
}
