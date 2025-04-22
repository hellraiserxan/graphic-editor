package org.example.ui.topMenu;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import org.example.ui.CanvasController;
import javafx.beans.value.ObservableValue;
public class PencilSettingsController {
    @FXML private Spinner<Integer> pencilSizeSpinner;
    private CanvasController canvasController;
    public PencilSettingsController() {}
    public void setCanvasController(CanvasController canvasController) {
        this.canvasController = canvasController;
        pencilSizeSpinner.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                if (canvasController != null) {
                    canvasController.setPencilSize(newValue);
                }
            }
        });
    }
}
