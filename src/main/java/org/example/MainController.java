package org.example;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class MainController {
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private Canvas drawingCanvas;

    private GraphicsContext gc;

    @FXML
    private Node menuBar;

    @FXML
    public void initialize() {
        gc = drawingCanvas.getGraphicsContext2D();

        drawingCanvas.widthProperty().bind(((Region) drawingCanvas.getParent()).widthProperty());
        drawingCanvas.heightProperty().bind(((Region) drawingCanvas.getParent()).heightProperty().subtract(30));

        drawingCanvas.widthProperty().addListener((obs, oldVal, newVal) -> redraw());
        drawingCanvas.heightProperty().addListener((obs, oldVal, newVal) -> redraw());

        redraw();

        if (menuBar != null) {
            menuBar.setOnMousePressed(this::handleMousePressed);
            menuBar.setOnMouseDragged(this::handleMouseDragged);
        }
    }

    private void redraw() {
        setBackgroundColor("#42464e");
        drawRectangle(50, 50, 100, 100);
    }

    public void setBackgroundColor(String hexColor) {
        if (drawingCanvas != null && gc != null) {
            gc.setFill(Color.web(hexColor));
            gc.fillRect(0, 0, drawingCanvas.getWidth(), drawingCanvas.getHeight());
        }
    }

    public void drawRectangle(double x, double y, double width, double height) {
        gc.setFill(Color.BLUE);
        gc.fillRect(x, y, width, height);
    }

    @FXML
    public void closeWindow(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handleMaximizeButton(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
    }

    @FXML
    public void minimizeWindow(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void handleMousePressed(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    @FXML
    private void handleMouseDragged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }
}
