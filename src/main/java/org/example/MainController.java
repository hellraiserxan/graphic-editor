package org.example;



import javafx.fxml.FXML;

import javafx.scene.Node;

import javafx.scene.canvas.Canvas;

import javafx.scene.canvas.GraphicsContext;

import javafx.scene.input.MouseEvent;

import javafx.scene.input.ScrollEvent;

import javafx.scene.layout.Region;

import javafx.scene.layout.VBox;

import javafx.scene.paint.Color;

import javafx.stage.Stage;

import javafx.geometry.Rectangle2D;

import javafx.stage.Screen;



import java.util.ArrayList;

import java.util.List;



public class MainController {

    private double lastX;

    private double lastY;

    private boolean isDrawing = false;

    private double xOffset = 0;

    private double yOffset = 0;

    private double scale = 1; // Коэффициент масштабирования

    private double scaleIncrement = 0.05;
    private double deltaX = 0; // Добавьте это поле
    private double deltaY = 0;
    private Line line; // Шаг изменения масштаба

    @FXML

    private VBox cursorIcon;

    @FXML

    private VBox pencilIcon;

    @FXML

    private VBox eraserIcon;

    @FXML

    private Canvas drawingCanvas;

    private String activeTool = "cursor";

    private GraphicsContext gc;

    private List<Line> lines = new ArrayList<>();

    private static class Line {

        List<Point> points;

        Color color;



        Line(Color color) {

            this.color = color;

            this.points = new ArrayList<>();

        }



        void addPoint(double x, double y) {

            points.add(new Point(x, y));

        }

    }

    private static class Point {

        double x, y;



        Point(double x, double y) {

            this.x = x;

            this.y = y;

        }

    }

// Класс для хранения координат линии



    @FXML

    private Node menuBar;



    @FXML

    public void initialize() {
        gc = drawingCanvas.getGraphicsContext2D();
        gc.setImageSmoothing(false); // Попробуйте отключить сглаживание
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
        drawingCanvas.setOnScroll(this::handleMouseScroll);
    }

    @FXML
    private void selectCursorTool(MouseEvent event) {
        selectTool("cursor");
    }



    @FXML
    private void selectPencilTool(MouseEvent event) {
        selectTool("pencil");
    }



    @FXML
    private void selectEraserTool(MouseEvent event) {
        selectTool("eraser");
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
        double width = drawingCanvas.getWidth();
        double height = drawingCanvas.getHeight();
        gc.setTransform(scale, 0, 0, scale, 0, 0);
        gc.clearRect(0, 0, width / scale, height / scale);
        setBackgroundColor("#9da1a4");
        gc.setLineWidth(1.0 / scale);

        for (Line line : lines) {
            gc.setStroke(line.color);
            for (int i = 1; i < line.points.size(); i++) {
                Point p1 = line.points.get(i - 1);
                Point p2 = line.points.get(i);
                gc.strokeLine(p1.x, p1.y, p2.x, p2.y);
            }
        }
    }

    private void handleMouseScroll(ScrollEvent event) {
        double zoomFactor = (event.getDeltaY() > 0) ? (1 + scaleIncrement) : (1 - scaleIncrement);
        double oldScale = scale;
        scale *= zoomFactor;
        scale = Math.max(0.1, Math.min(scale, 20.0)); // Увеличен предел масштаба

        double pivotX = event.getX(); // Координата X курсора относительно канваса
        double pivotY = event.getY(); // Координата Y курсора относительно канваса

        // Корректируем смещение, чтобы масштабирование происходило относительно курсора
        deltaX = pivotX - (pivotX - deltaX) * (scale / oldScale);
        deltaY = pivotY - (pivotY - deltaY) * (scale / oldScale);

        redraw();
        event.consume();
    }
    public void setBackgroundColor(String hexColor) {
        if (drawingCanvas != null && gc != null) {
            gc.setFill(Color.web(hexColor));
            gc.fillRect(0, 0, drawingCanvas.getWidth() / scale, drawingCanvas.getHeight() / scale); // Рисуем фон в масштабированных координатах

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
            line = new Line(Color.BLACK);
            gc.beginPath();
            lastX = event.getX() / scale;
            lastY = event.getY() / scale;
            gc.moveTo(lastX, lastY); // Используем скорректированные координаты
            isDrawing = true;
        } else if (activeTool.equals("eraser")) {
            lastX = event.getX() / scale;
            lastY = event.getY() / scale;
            isDrawing = true;
            erase(lastX, lastY);
        }
    }

    @FXML
    private void draw(MouseEvent event) {
        if (isDrawing) {
            double x = event.getX() / scale;
            double y = event.getY() / scale;
            if (activeTool.equals("pencil")) {
                line.addPoint(lastX, lastY);
                line.addPoint(x, y);
                gc.beginPath();
                gc.moveTo(lastX, lastY);
                gc.lineTo(x, y);
                gc.stroke();
                lastX = x;
                lastY = y;
            } else if (activeTool.equals("eraser")) {
                erase(x, y);
                lastX = x;
                lastY = y;
            }
        }
    }

    @FXML
    private void endDrawing(MouseEvent event) {
        if (activeTool.equals("pencil")) {
            lines.add(line);
        }
        isDrawing = false;
        gc.closePath();
        line = new Line(Color.BLACK);
    }
    private void erase(double x, double y) {
        double eraserSize = 10 / scale; // Размер ластика также зависит от масштаба
        gc.setFill(Color.web("#9da1a4"));
        gc.fillRect(x - eraserSize / 2, y - eraserSize / 2, eraserSize, eraserSize);
    }
}