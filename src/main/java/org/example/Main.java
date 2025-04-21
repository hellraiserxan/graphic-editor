package org.example;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Stage splashStage = new Stage();
        Parent splashRoot = FXMLLoader.load(getClass().getResource("/splash_screen.fxml"));
        Scene splashScene = new Scene(splashRoot);
        splashStage.initStyle(StageStyle.UNDECORATED);
        splashStage.setScene(splashScene);

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        double centerX = screenBounds.getMinX() + screenBounds.getWidth() / 2;
        double centerY = screenBounds.getMinY() + screenBounds.getHeight() / 2;
        double splashWidth = splashRoot.getBoundsInLocal().getWidth();
        double splashHeight = splashRoot.getBoundsInLocal().getHeight();


        double offsetY = 50;
        splashStage.setX(centerX - splashWidth / 2);
        splashStage.setY(centerY - splashHeight / 2 + offsetY);

        splashStage.show();


        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main-view.fxml"));
                Parent mainRoot = loader.load();

                Stage mainStage = new Stage();
                Scene mainScene = new Scene(mainRoot, 660, 300);
                mainScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
                mainStage.initStyle(StageStyle.UNDECORATED);
                mainStage.setWidth(1200.0);
                mainStage.setHeight(800.0);
                mainStage.setScene(mainScene);
                mainStage.centerOnScreen();
                FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), splashRoot);
                fadeOut.setToValue(0);
                fadeOut.setOnFinished(e -> {
                    splashStage.close();
                    mainStage.show();
                });
                fadeOut.play();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        delay.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}