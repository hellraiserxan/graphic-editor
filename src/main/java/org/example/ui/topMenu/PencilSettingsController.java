package org.example.ui.topMenu;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.scene.control.ColorPicker;
import org.example.ui.CanvasController;

public class PencilSettingsController {
    @FXML private ImageView pencilIcon;
    private CanvasController canvasController;
    @FXML private Slider slider;
    @FXML private Button showSliderButton;
    @FXML private VBox sliderPane;
    @FXML private TextField sizeTextField;
    @FXML private HBox rootHBox;
    private boolean sliderVisible = false;
    private boolean isInteractingWithSlider = false;

    public PencilSettingsController() {}

    public void setCanvasController(CanvasController canvasController) {
        this.canvasController = canvasController;
        try {
            int initialSize = Integer.parseInt(sizeTextField.getText());
            this.canvasController.setPencilSize(initialSize);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        // Инициализация значения TextField из Slider
        sizeTextField.setText(String.valueOf((int) slider.getValue()));

        // Синхронизация Slider при изменении текста в TextField
        sizeTextField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty()) {
                try {
                    int value = Integer.parseInt(newVal);
                    if (value >= slider.getMin() && value <= slider.getMax()) {
                        slider.setValue(value);
                        if (canvasController != null) {
                            canvasController.setPencilSize(value);
                        }
                    }
                } catch (NumberFormatException e) {
                    sizeTextField.setText(oldVal); // Возвращаем старое значение в случае ошибки
                }
            }
        });

        // Слушатель для изменения значения ползунка
        slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            sizeTextField.setText(String.valueOf(newVal.intValue())); // Обновляем TextField
            if (canvasController != null) {
                canvasController.setPencilSize(newVal.intValue()); // Обновляем размер кисти в CanvasController
            }
        });

        // Показ ползунка при клике на TextField
        sizeTextField.setOnMouseClicked(this::handleShowSlider);

        // Обработка потери фокуса с TextField
        sizeTextField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && !isInteractingWithSlider && sliderVisible) {
                applyTextFieldValue();
                hideSliderAndClearFocus();
            } else if (newVal) {
                showSlider();
            }
        });

        // Обработка нажатия Enter в TextField
        sizeTextField.setOnKeyPressed(this::handleTextFieldKeyPressed);

        // Отслеживание взаимодействия с ползунком
        slider.setOnMousePressed(event -> {
            isInteractingWithSlider = true;
            if (!sliderVisible) { // Убедимся, что ползунок виден при начале взаимодействия
                showSlider();
            }
        });

        slider.setOnMouseReleased(event -> {
            isInteractingWithSlider = false;

        });

        slider.setOnMouseDragged(event -> {
            isInteractingWithSlider = true; // Убеждаемся, что флаг установлен во время перетаскивания
            if (!sliderVisible) {
                showSlider();
            }
        });

        // Логика для отображения/скрытия слайдера при клике вне его
        slider.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                    javafx.scene.Node target = (javafx.scene.Node) event.getTarget();
                    boolean isSliderOrTextField = false;
                    javafx.scene.Node current = target;
                    while (current != null) {
                        if (current == slider || current == sliderPane || current == sizeTextField) {
                            isSliderOrTextField = true;
                            break;
                        }
                        current = current.getParent();
                    }

                    // Только если ползунок виден и клик произошел вне ползунка, скрываем его
                    if (sliderVisible && !isSliderOrTextField && !isInteractingWithSlider) {
                        hideSliderAndClearFocus();
                    }
                });
            }
        });
    }

    // Метод для скрытия слайдера
    public void hideSlider() {
        sliderPane.setVisible(false);
        sliderPane.setManaged(false);
        sliderVisible = false;
    }

    // Метод для показа слайдера
    private void showSlider() {
        sliderPane.setVisible(true);
        sliderPane.setManaged(true);
        sliderVisible = true;
    }

    // Метод для обработки клика на TextField
    @FXML
    private void handleShowSlider(MouseEvent event) {
        event.consume();
        if (!sliderVisible) {
            showSlider();
        }
    }

    // Метод для скрытия слайдера и потери фокуса
    private void hideSliderAndClearFocus() {
        hideSlider();
    }

    private void applyTextFieldValue() {
        try {
            int value = Integer.parseInt(sizeTextField.getText());
            if (value >= slider.getMin() && value <= slider.getMax()) {
                slider.setValue(value);
                if (canvasController != null) {
                    canvasController.setPencilSize(value);
                }
            } else {
                sizeTextField.setText(String.valueOf((int) slider.getValue()));
            }
        } catch (NumberFormatException e) {
            sizeTextField.setText(String.valueOf((int) slider.getValue()));
        }
    }

    private void handleTextFieldKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            applyTextFieldValue();
            hideSliderAndClearFocus();
            event.consume(); // Предотвращаем дальнейшую обработку события
        }
    }
}
