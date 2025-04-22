package org.example.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import org.example.ui.topMenu.EraserSettingsController;
import org.example.ui.topMenu.PencilSettingsController;

import java.io.IOException;


public class MainController {

    @FXML private HBox toolSettingsPanel;

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

            drawingCanvasController.setToolController(toolPanelIncludeController);

        }
        if (toolPanelIncludeController != null) {
            toolPanelIncludeController.selectTool("cursor");
            toolPanelIncludeController.getActiveToolProperty().addListener((observable, oldValue, newValue) -> {
                loadToolSettings(newValue);
            });
        }
        if (toolController != null) {
            toolController.selectTool("cursor");
        }
        if (windowController != null && menuBar != null) {
            menuBar.setOnMousePressed(windowController::handleMousePressed);
            menuBar.setOnMouseDragged(windowController::handleMouseDragged);
        }
    }
    private void loadToolSettings(String tool) {
        toolSettingsPanel.getChildren().clear();
        if (tool != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                Node settingsNode = null;
                Object controller = null;

                switch (tool) {
                    case "pencil":
                        loader.setLocation(getClass().getResource("/view/top-menu-bar/pencil-settings.fxml"));
                        settingsNode = loader.load();
                        PencilSettingsController pencilController = loader.getController();
                        pencilController.setCanvasController(drawingCanvasController);
                        controller = pencilController;
                        break;
                    case "eraser":
                        loader.setLocation(getClass().getResource("/view/top-menu-bar/eraser-settings.fxml"));
                        settingsNode = loader.load();
                        EraserSettingsController eraserController = loader.getController();
                        controller = eraserController;
                        break;
                    case "cursor":
                    default:
                        break;
                }

                if (settingsNode != null) {
                    toolSettingsPanel.getChildren().add(settingsNode);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
