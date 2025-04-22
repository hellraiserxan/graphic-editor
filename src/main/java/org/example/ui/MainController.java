package org.example.ui;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.VBox;
import javafx.scene.Node;


public class MainController {

    @FXML private VBox cursorIcon;
    @FXML private VBox pencilIcon;
    @FXML private VBox eraserIcon;


    @FXML private Node menuBar;

    @FXML private ToolController toolController;

    @FXML private WindowController windowController;
    @FXML private ToolController toolPanelIncludeController;
    @FXML private CanvasController drawingCanvasController;
    @FXML
    public void initialize() {
        if (drawingCanvasController != null) {
            drawingCanvasController.initCanvas();
            Canvas drawingCanvas = drawingCanvasController.getCanvas();
            if (drawingCanvas != null) {
                drawingCanvas.setOnMousePressed(drawingCanvasController::startDrawing);
                drawingCanvas.setOnMouseDragged(drawingCanvasController::draw);
                drawingCanvas.setOnMouseReleased(drawingCanvasController::endDrawing);
                drawingCanvas.setOnScroll(drawingCanvasController::handleMouseScroll);
            }
            System.out.println("MainController: toolPanelIncludeController is null: " + (toolPanelIncludeController == null)); // Добавили вывод
            drawingCanvasController.setToolController(toolPanelIncludeController);
            System.out.println("MainController: ToolController set in CanvasController"); // Добавили вывод

        }
        if (toolController != null) {
            toolController.selectTool("cursor");
        }
        if (windowController != null && menuBar != null) {
            menuBar.setOnMousePressed(windowController::handleMousePressed);
            menuBar.setOnMouseDragged(windowController::handleMouseDragged);
        }
    }
}
