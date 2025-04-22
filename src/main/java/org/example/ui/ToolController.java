package org.example.ui;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class ToolController {
    @FXML private VBox cursorIcon;
    @FXML private VBox pencilIcon;
    @FXML private VBox eraserIcon;
    @FXML private Canvas canvas;

    private String activeTool = "cursor";

    public ToolController() {
        System.out.println("ToolController создан");
    }

    @FXML
    public void initialize() {
        selectTool(activeTool);
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
    public void selectTool(String tool){
        this.activeTool = tool;
        updateIconStyles();
        updateCanvasCursor();
        System.out.println("Active Tool (forced): " + activeTool);
    }

    public String getActiveTool(){
        return activeTool;
    }

    private void toggleTool(String tool) {
        if (activeTool != null && activeTool.equals(tool)) {
            activeTool = null;
        } else {
            activeTool = tool;
        }
        updateIconStyles();
        updateCanvasCursor();
        System.out.println("Active Tool: " + activeTool);
    }

    private void updateIconStyles() {
        if (cursorIcon != null) cursorIcon.getStyleClass().remove("active");
        if (pencilIcon != null) pencilIcon.getStyleClass().remove("active");
        if (eraserIcon != null) eraserIcon.getStyleClass().remove("active");

        if (activeTool != null && !activeTool.isEmpty()) {
            switch (activeTool) {
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
            switch (activeTool) {
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
}