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
            String text = item.getSimulationId() + ")" + (item.getIsSimulationFinished() == false? "Running": "Finished");
            //String text = item.getSimulationId() + ") running(" + item.getIsSimulationRunning() + ")";
            setText(text);
        }
    }
}
