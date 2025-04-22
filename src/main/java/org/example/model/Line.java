package org.example.model;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Line {
    private final List<Point> points;
    private final Color color;

    public Line(Color color) {
        this.color = color;
        this.points = new ArrayList<>();
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
}
