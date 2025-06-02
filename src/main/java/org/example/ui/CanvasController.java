package org.example.ui;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.layout.Region;
import org.example.model.Line;
import org.example.model.Point;

import java.util.ArrayList;
import java.util.List;

public class CanvasController {
    @FXML private Canvas canvas;
    private ToolController toolController;
    private GraphicsContext gc;
    private double pencilSize = 3;
    private Color color;
    private double scale = 1;
    private final double scaleIncrement = 0.05;
    private double deltaX = 0;
    private double deltaY = 0;
    private boolean isDrawing = false;
    private double lastX, lastY;

    private Line currentLine;
    private final List<Line> lines = new ArrayList<>();
    private double eraserSize = 10; // Установите начальный размер ластика

    public CanvasController() {
    }

    @FXML
    public void initialize() {
        if (canvas != null) {
            this.gc = canvas.getGraphicsContext2D();
            initCanvas();
        }
    }

    public void initCanvas() {
        if (gc != null && canvas != null && canvas.getParent() instanceof Region) {
            gc.setImageSmoothing(false);

            AnchorPane parent = (AnchorPane) canvas.getParent();
            canvas.widthProperty().bind(parent.widthProperty());
            canvas.heightProperty().bind(parent.heightProperty());

            canvas.widthProperty().addListener((obs, oldVal, newVal) -> redraw());
            canvas.heightProperty().addListener((obs, oldVal, newVal) -> redraw());

            redraw();
        }
    }

    @FXML
    public void startDrawing(MouseEvent e) {
        if (toolController != null && gc != null) {
            double x = e.getX() / scale;
            double y = e.getY() / scale;
            switch (toolController.getActiveTool()) {
                case "pencil":
                    gc.setStroke(color);
                    currentLine = new Line(color, pencilSize);
                    lastX = x;
                    lastY = y;
                    gc.beginPath();
                    gc.moveTo(lastX, lastY);
                    isDrawing = true;
                    break;
                case "eraser":
                    lastX = x;
                    lastY = y;
                    isDrawing = true;
                    erase(x, y);
                    break;
            }
        }
    }

    @FXML
    public void draw(MouseEvent e) {
        if (!isDrawing || gc == null || toolController == null) return;

        double x = e.getX() / scale;
        double y = e.getY() / scale;
        System.out.println("Draw: scale=" + scale + ", pencilSize=" + pencilSize + ", lineWidth=" + (pencilSize / scale));
        gc.setLineWidth(pencilSize);
        switch (toolController.getActiveTool()) {
            case "pencil":
                gc.setStroke(color);
                currentLine.addPoint(lastX, lastY);
                currentLine.addPoint(x, y);
                gc.lineTo(x, y);
                gc.stroke();
                lastX = x;
                lastY = y;
                break;
            case "eraser":
                erase(x, y);
                lastX = x;
                lastY = y;
                break;
        }
    }

    @FXML
    public void endDrawing(MouseEvent e) {
        isDrawing = false;
        if (gc != null) gc.closePath();
        if (currentLine != null) {
            lines.add(currentLine);
            currentLine = null;
        }
    }

    @FXML
    public void handleMouseScroll(ScrollEvent e) {
        if (canvas != null) {
            double oldScale = scale;
            double zoomFactor = (e.getDeltaY() > 0) ? (1 + scaleIncrement) : (1 - scaleIncrement);
            scale *= zoomFactor;
            scale = Math.max(0.1, Math.min(scale, 20.0));

            double pivotX = e.getX();
            double pivotY = e.getY();
            deltaX = pivotX - (pivotX - deltaX) * (scale / oldScale);
            deltaY = pivotY - (pivotY - deltaY) * (scale / oldScale);

            redraw();
            e.consume();
        }
    }

    @FXML
    private void erase(double x, double y) {
        if (gc != null) {
            double size = eraserSize / scale; // Применяем масштаб к размеру ластика
            gc.setFill(Color.web("#9da1a4"));
            gc.fillRect(x - size / 2, y - size / 2, size, size);
        }
    }

    private void redraw() {
        if (gc != null && canvas != null) {
            double width = canvas.getWidth();
            double height = canvas.getHeight();
            gc.setTransform(scale, 0, 0, scale, 0, 0);
            gc.clearRect(0, 0, width / scale, height / scale);
            setBackgroundColor("#9da1a4");
            gc.setLineWidth(1.0 / scale); // Масштабирование толщины сетки (если есть)

            for (Line line : lines) {
                gc.setStroke(line.getColor());
                gc.setLineWidth(line.getThickness());
                List<Point> points = line.getPoints();
                for (int i = 1; i < points.size(); i++) {
                    Point p1 = points.get(i - 1);
                    Point p2 = points.get(i);
                    gc.strokeLine(p1.x, p1.y, p2.x, p2.y);
                }
            }
        }
    }

    @FXML
    public void setBackgroundColor(String hexColor) {
        if (gc != null && canvas != null) {
            gc.setFill(Color.web(hexColor));
            gc.fillRect(0, 0, canvas.getWidth() / scale, canvas.getHeight() / scale);
        }
    }
    public Canvas getCanvas() {
        return canvas;
    }
    public void setToolController(ToolController toolController) {
        this.toolController = toolController;
    }
    public void setPencilSize(Integer size) {
        if (size != null && size > 0) {
            this.pencilSize = size;
            System.out.println("Размер кисти установлен: " + size);
        }
    }
    public void setEraserSize(Integer size) {
        this.eraserSize = size;
        System.out.println("Размер ластика установлен: " + size);
    }
    public void setColor(Color color){
        this.color = color;
        gc.setStroke(this.color);
        System.out.println("Цвет установлен: " + color);
    }
}