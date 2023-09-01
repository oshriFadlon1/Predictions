package ui.sceneController.detailsController;

import dto.DtoResponsePreviewTermination;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import pointCoord.PointCoord;

public class TreminationComponentController {

    @FXML
    private Label Seconds;

    @FXML
    private Label Ticks;

    @FXML
    private Label User;

    @FXML
    private Label col;

    @FXML
    private Label row;


    public void updateLabelTerm(DtoResponsePreviewTermination dtoResponseTermination, PointCoord worldSize){

        if (dtoResponseTermination.getTicks() == -1) {
            Ticks.setText("");
        }
        else {
            Ticks.setText("Ticks: " + dtoResponseTermination.getTicks());
        }
        if (dtoResponseTermination.getSeconds() == -1) {
            Seconds.setText("");
        }
        else {
            Seconds.setText("Seconds: " + dtoResponseTermination.getSeconds());
        }

        if (dtoResponseTermination.getTicks() == -1 && dtoResponseTermination.getSeconds() == -1 ){
            User.setText("End by user choice");
        }else {
            User.setText("");
        }

        col.setText("Column: " + worldSize.getCol());
        row.setText("Rows: " + worldSize.getRow());

    }
}
