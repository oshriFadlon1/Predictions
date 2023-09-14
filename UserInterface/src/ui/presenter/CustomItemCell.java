package ui.presenter;

import javafx.scene.control.ListCell;

public class CustomItemCell extends ListCell<SimulationPresenter> {
    @Override
    protected void updateItem(SimulationPresenter item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
        } else {
            // Customize the text representation of the item
            String text = item.getSimulationId() + ") " + (item.getSimulationState().toLowerCase());
            setText(text);
        }
    }
}
