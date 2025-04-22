package org.example.ui.topMenu;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import org.example.ui.CanvasController;
import javafx.beans.value.ChangeListener;
public class EraserSettingsController {
    private CanvasController canvasController;
    @FXML private Spinner<Integer> eraserSizeSpinner;
    public EraserSettingsController(){}
    public void setCanvasController(CanvasController canvasController) {
        this.canvasController = canvasController;
        eraserSizeSpinner.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                if (canvasController != null) {
                    canvasController.setPencilSize(newValue);
                }
            }
        });
    }
}

