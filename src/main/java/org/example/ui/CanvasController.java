package org.example.ui;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.layout.Region;
import org.example.model.Line; // Убедитесь, что у вас есть этот класс
import org.example.model.Point; // Убедитесь, что у вас есть этот класс

import java.util.ArrayList;
import java.util.List;

public class CanvasController {
    @FXML private Canvas canvas;
    private ToolController toolController; // Убедитесь, что у вас есть этот класс
    private GraphicsContext gc;
    private double pencilSize = 3;
    private Color color; // Текущий цвет карандаша
    private double scale = 1;
    private final double scaleIncrement = 0.05;
    private double deltaX = 0; // Сдвиг по X (панорамирование)
    private double deltaY = 0; // Сдвиг по Y (панорамирование)
    private boolean isDrawing = false;
    private double lastX, lastY;
    private boolean isPanning = false;
    private double panStartX, panStartY;
    private Line currentLine;
    private final List<Line> lines = new ArrayList<>();
    private double eraserSize = 10;
    private Color backgroundColor = Color.web("#9da1a4");

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
            gc.setImageSmoothing(false); // Отключаем сглаживание для более четких пикселей

            AnchorPane parent = (AnchorPane) canvas.getParent();
            canvas.widthProperty().bind(parent.widthProperty()); // Привязываем ширину канваса к ширине родителя
            canvas.heightProperty().bind(parent.heightProperty()); // Привязываем высоту канваса к высоте родителя

            canvas.widthProperty().addListener((obs, oldVal, newVal) -> redraw()); // Перерисовываем при изменении ширины
            canvas.heightProperty().addListener((obs, oldVal, newVal) -> redraw()); // Перерисовываем при изменении высоты

            redraw(); // Первоначальная отрисовка
        }
    }

    @FXML
    public void startDrawing(MouseEvent e) {
        if (e.getButton() == MouseButton.SECONDARY || (toolController != null && ("cursor".equals(toolController.getActiveTool())))) {
            isPanning = true;
            panStartX = e.getX();
            panStartY = e.getY();
            return;
        }
        if (toolController != null && gc != null) {

            double x = (e.getX() - deltaX) / scale;
            double y = (e.getY() - deltaY) / scale;

            switch (toolController.getActiveTool()) {
                case "pencil":
                    gc.setStroke(color);
                    gc.setLineWidth(pencilSize);
                    currentLine = new Line(color, pencilSize);
                    lastX = x;
                    lastY = y;
                    gc.beginPath();
                    gc.moveTo(lastX, lastY);
                    isDrawing = true;
                    break;
                case "eraser":
                    gc.setStroke(backgroundColor);
                    gc.setLineWidth(eraserSize);
                    currentLine = new Line(backgroundColor, eraserSize);
                    lastX = x;
                    lastY = y;
                    gc.beginPath();
                    gc.moveTo(lastX, lastY);
                    isDrawing = true;
                    break;
            }
        }
    }

    @FXML
    public void draw(MouseEvent e) {
        if (isPanning) {
            double dx = e.getX() - panStartX;
            double dy = e.getY() - panStartY;

            deltaX += dx;
            deltaY += dy;

            panStartX = e.getX();
            panStartY = e.getY();

            redraw();
            return;
        }
        if (!isDrawing || gc == null || toolController == null) return;

        double x = (e.getX() - deltaX) / scale;
        double y = (e.getY() - deltaY) / scale;


        switch (toolController.getActiveTool()) {
            case "pencil":
                gc.setStroke(color);
                gc.setLineWidth(pencilSize);
                currentLine.addPoint(lastX, lastY);
                currentLine.addPoint(x, y);
                gc.lineTo(x, y);
                gc.stroke();
                lastX = x;
                lastY = y;
                break;
            case "eraser":
                gc.setStroke(backgroundColor);
                gc.setLineWidth(eraserSize);
                currentLine.addPoint(lastX, lastY);
                currentLine.addPoint(x, y);
                gc.lineTo(x, y);
                gc.stroke();
                lastX = x;
                lastY = y;
                break;
        }
    }

    @FXML
    public void endDrawing(MouseEvent e) {
        if (isPanning) {
            isPanning = false;
            return;
        }
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

    private void redraw() {
        if (gc != null && canvas != null) {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            gc.setTransform(1, 0, 0, 1, 0, 0);
            gc.clearRect(0, 0, width, height);

            // 2. Заливаем фон
            gc.setFill(backgroundColor);
            gc.fillRect(0, 0, width, height);


            gc.setTransform(scale, 0, 0, scale, deltaX, deltaY);


            for (Line line : lines) {
                gc.setStroke(line.getColor());
                gc.setLineWidth(line.getThickness());
                List<Point> points = line.getPoints();
                if (!points.isEmpty()) {
                    gc.beginPath();
                    Point firstPoint = points.get(0);
                    gc.moveTo(firstPoint.x, firstPoint.y);
                    for (int i = 1; i < points.size(); i++) {
                        Point p = points.get(i);
                        gc.lineTo(p.x, p.y);
                    }
                    gc.stroke();
                }
            }
        }
    }

    @FXML
    public void setBackgroundColor(String hexColor) {

        this.backgroundColor = Color.web(hexColor);

        redraw();
        System.out.println("Цвет фона установлен: " + hexColor);
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

        if (toolController != null && "pencil".equals(toolController.getActiveTool())) {
            if (gc != null) {
                gc.setStroke(this.color);
            }
        }
        System.out.println("Цвет установлен: " + color);
    }
}