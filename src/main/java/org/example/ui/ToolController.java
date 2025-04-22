package org.example.ui;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class ToolController {
    @FXML private VBox cursorIcon;
    @FXML private VBox pencilIcon;
    @FXML private VBox eraserIcon;
    @FXML private Canvas canvas;

    private StringProperty activeTool = new SimpleStringProperty("cursor");

    public ToolController() {
        System.out.println("ToolController создан");
    }

    @FXML
    public void initialize() {
        selectTool(activeTool.get());
    }

    @FXML
    public void selectCursorTool(MouseEvent e) {
        toggleTool("cursor");
    }

    @FXML
    public void selectPencilTool(MouseEvent e) {
        toggleTool("pencil");
    }

    @FXML
    public void selectEraserTool(MouseEvent e) {
        toggleTool("eraser");
    }

    @FXML
    public void selectTool(String  tool){
        this.activeTool.set(tool);
        updateIconStyles();
        updateCanvasCursor();
        System.out.println("Active Tool (forced): " + activeTool);
    }

    public String getActiveTool() {
        return activeTool.get();
    }

    private void toggleTool(String tool) {
        if (activeTool.get() != null && activeTool.get().equals(tool)) {
            activeTool.set(null);
        } else {
            activeTool.set(tool);
        }
        updateIconStyles();
        updateCanvasCursor();
    }

    private void updateIconStyles() {
        if (cursorIcon != null) cursorIcon.getStyleClass().remove("active");
        if (pencilIcon != null) pencilIcon.getStyleClass().remove("active");
        if (eraserIcon != null) eraserIcon.getStyleClass().remove("active");

        if (activeTool != null && !activeTool.get().isEmpty()) {
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
                    canvas.setStyle("-fx-cursor: default;"); // если null и что-то еще
                    break;
            }
        }
    }
    public StringProperty  getActiveToolProperty() {
        return activeTool;
    }
}