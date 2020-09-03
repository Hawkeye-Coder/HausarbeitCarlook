package org.carlook.services.util;

import com.vaadin.ui.Grid;
import org.carlook.model.objects.dto.AutoDTO;

public class BuildGrid {
    public static void buildGrid(Grid<AutoDTO> grid) {
        grid.removeAllColumns();
        grid.addColumn(AutoDTO::getAuto_id).setCaption("ID");
        grid.addColumn(AutoDTO::getMarke).setCaption("Marke");
        grid.addColumn(AutoDTO::getBaujahr).setCaption("Baujahr");
        grid.addColumn(AutoDTO::getBeschreibung).setCaption("Beschreibung");

    }
}
