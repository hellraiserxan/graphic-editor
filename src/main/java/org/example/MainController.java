package org.example;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

import javafx.scene.input.MouseEvent;

public class MainController{
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private Node menuBar;

    @FXML
    public void initialize() {
        // Сделать панель меню перетаскиваемой
        if (menuBar != null) {
            menuBar.setOnMousePressed(this::handleMousePressed);
            menuBar.setOnMouseDragged(this::handleMouseDragged);
        }
    }
    @FXML
    public void closeWindow(javafx.scene.input.MouseEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML
    public void handleMaximizeButton(javafx.scene.input.MouseEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        javafx.geometry.Rectangle2D bounds = javafx.stage.Screen.getPrimary().getVisualBounds();

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
    private void handleMousePressed(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    private void handleMouseDragged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }
 }
