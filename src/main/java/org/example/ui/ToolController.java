package org.example.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.example.ui.topMenu.EraserSettingsController;
import org.example.ui.topMenu.PencilSettingsController;

public class ToolController {
    @FXML private VBox cursorIcon;
    @FXML private VBox pencilIcon;
    @FXML private VBox eraserIcon;
    @FXML private Canvas canvas;
    private PencilSettingsController pencilSettingsController; // Ссылка на контроллер для настроек карандаша
    private StringProperty activeTool = new SimpleStringProperty("cursor");

    public ToolController() {
        System.out.println("ToolController создан");
    }

    @FXML
    public void initialize() {
        selectTool(activeTool.get());
    }

    // Устанавливаем PencilSettingsController
    public void setPencilSettingsController(PencilSettingsController pencilSettingsController) {
        this.pencilSettingsController = pencilSettingsController;
    }


    @FXML
    public void selectCursorTool(MouseEvent e) {
        selectTool("cursor");
    }

    @FXML
    public void selectPencilTool(MouseEvent e) {
        selectTool("pencil");
        if (pencilSettingsController != null) {
            pencilSettingsController.hideSlider(); // Скрываем слайдер, если выбран карандаш
        }
    }

    @FXML
    public void selectEraserTool(MouseEvent e) {
        selectTool("eraser");
    }

    @FXML
    public void selectTool(String tool) {
        this.activeTool.set(tool);
        updateIconStyles();
        updateCanvasCursor();
        System.out.println("Active Tool (forced): " + activeTool);
    }

    public String getActiveTool() {
        return activeTool.get();
    }

    private void updateIconStyles() {
        if (cursorIcon != null) cursorIcon.getStyleClass().remove("active");
        if (pencilIcon != null) pencilIcon.getStyleClass().remove("active");
        if (eraserIcon != null) eraserIcon.getStyleClass().remove("active");

        if (activeTool != null && activeTool.get() != null && !activeTool.get().isEmpty()) {
            switch (activeTool.get()) {
                case "cursor":
                    if (cursorIcon != null) cursorIcon.getStyleClass().add("active");
                    break;
                case "pencil":
                    if (pencilIcon != null) pencilIcon.getStyleClass().add("active");
                    break;
                case "eraser":
                    if (eraserIcon != null) eraserIcon.getStyleClass().add("active");
                    break;
            }
        }
    }

    private void updateCanvasCursor() {
        if (canvas != null) {
            switch (activeTool.get()) {
                case "cursor":
                    canvas.setStyle("-fx-cursor: default;");
                    break;
                case "pencil":
                    canvas.setStyle("-fx-cursor: hand;");
                    break;
                case "eraser":
                    canvas.setStyle("-fx-cursor: crosshair");
                    break;
                default:
                    canvas.setStyle("-fx-cursor: default;");
                    break;
            }
        }
    }

    public StringProperty getActiveToolProperty() {
        return activeTool;
    }
}
