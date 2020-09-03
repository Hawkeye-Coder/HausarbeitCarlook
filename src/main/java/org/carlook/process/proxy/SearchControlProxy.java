package org.carlook.process.proxy;

import org.carlook.model.objects.dto.AutoDTO;
import org.carlook.process.Interfaces.SearchControlInterface;
import org.carlook.process.control.SearchControl;

import java.sql.SQLException;
import java.util.List;

public class SearchControlProxy implements SearchControlInterface {
    private static SearchControlProxy search = null;

    public static SearchControlProxy getInstance() {
        if (search == null) {
            search = new SearchControlProxy();
        }
        return search;
    }

    private SearchControlProxy() {

    }

    public List<AutoDTO> getAnzeigenForUser() throws SQLException {
        return SearchControl.getInstance().getAnzeigenForUser();
    }

    public List<AutoDTO> getAnzeigenForSearch(String suchtext, String filter) throws SQLException {
        return SearchControl.getInstance().getAnzeigenForSearch(suchtext, filter);
    }
}
