package org.example;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class MainController {
    private double lastX;
    private double lastY;
    private boolean isDrawing = false;
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private VBox  cursorIcon;
    @FXML
    private VBox pencilIcon;
    @FXML
    private VBox  eraserIcon;
    @FXML
    private Canvas drawingCanvas;
    private String activeTool = "cursor";
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
        selectTool(activeTool);
        redraw();

        if (menuBar != null) {
            menuBar.setOnMousePressed(this::handleMousePressed);
            menuBar.setOnMouseDragged(this::handleMouseDragged);
        }
        drawingCanvas.setOnMousePressed(this::startDrawing);
        drawingCanvas.setOnMouseDragged(this::draw);
        drawingCanvas.setOnMouseReleased(this::endDrawing);
    }
    @FXML
    private void selectCursorTool(MouseEvent event) {
        if (activeTool.equals("cursor")) {
            selectTool(null); // Или selectTool("");
            drawingCanvas.setStyle("-fx-cursor: default;");
        } else {
            selectTool("cursor");
        }
    }

    @FXML
    private void selectPencilTool(MouseEvent event) {
        if (activeTool.equals("pencil")) {
            selectTool(null); // Или selectTool("");
            drawingCanvas.setStyle("-fx-cursor: default;");
        } else {
            selectTool("pencil");
        }
    }

    @FXML
    private void selectEraserTool(MouseEvent event) {
        if (activeTool.equals("eraser")) {
            selectTool(null); // Или selectTool("");
            drawingCanvas.setStyle("-fx-cursor: default;");
        } else {
            selectTool("eraser");
        }
    }

    private void selectTool(String tool) {
        activeTool = tool;
        cursorIcon.getStyleClass().remove("active");
        pencilIcon.getStyleClass().remove("active");
        eraserIcon.getStyleClass().remove("active");

        if (tool != null && !tool.isEmpty()) {
            switch (tool) {
                case "cursor":
                    cursorIcon.getStyleClass().add("active");
                    drawingCanvas.setStyle("-fx-cursor: default;");
                    break;
                case "pencil":
                    pencilIcon.getStyleClass().add("active");
                    drawingCanvas.setStyle("-fx-cursor: hand;");
                    break;
                case "eraser":
                    eraserIcon.getStyleClass().add("active");
                    drawingCanvas.setStyle("-fx-cursor: crosshair;");
                    break;
            }
        }
    }
    private void redraw() {
        setBackgroundColor("#9da1a4");
    }

    public void setBackgroundColor(String hexColor) {
        if (drawingCanvas != null && gc != null) {
            gc.setFill(Color.web(hexColor));
            gc.fillRect(0, 0, drawingCanvas.getWidth(), drawingCanvas.getHeight());
        }
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
    @FXML
    private void startDrawing(MouseEvent event) {
        if (activeTool.equals("pencil")) {
            gc.beginPath();
            lastX = event.getX();
            lastY = event.getY();
            gc.moveTo(lastX, lastY);
            isDrawing = true;
        } else if (activeTool.equals("eraser")) {
            lastX = event.getX();
            lastY = event.getY();
            isDrawing = true;
            erase(lastX, lastY);
        }
    }

    @FXML
    private void draw(MouseEvent event) {
        if (isDrawing) {
            if (activeTool.equals("pencil")) {
                double x = event.getX();
                double y = event.getY();
                gc.lineTo(x, y);
                gc.stroke();
                lastX = x;
                lastY = y;
            } else if (activeTool.equals("eraser")) {
                double x = event.getX();
                double y = event.getY();
                erase(x, y);
                lastX = x;
                lastY = y;
            }
        }
    }

    @FXML
    private void endDrawing(MouseEvent event) {
        isDrawing = false;
        gc.closePath();
    }

    private void erase(double x, double y) {
        double eraserSize = 10;
        gc.setFill(Color.web("#9da1a4"));
        gc.fillRect(x - eraserSize / 2, y - eraserSize / 2, eraserSize, eraserSize);
    }
}
