package org.example.model;

import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

public class Line {
    private final List<Point> points;
    private  Color color;
    private double thickness;

    public Line(Color color, double thickness) {
        this.color = color;
        this.points = new ArrayList<>();
        this.thickness = thickness;
    }

    public void addPoint(double x, double y) {
        points.add(new Point(x, y));
    }

    public List<Point> getPoints() {
        return points;
    }

    public Color getColor() {
        return color;
    }

    public double getThickness() {
        return thickness;
    }

    public void setThickness(double thickness) {
        this.thickness = thickness;
    }
}
