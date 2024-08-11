package SetConditionsStage;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;

public class SetCondController {
    @FXML
    private ComboBox<Integer> maxAgeCmbBox;
    @FXML
    private ComboBox<String> bloodTypeCmbBox;
    @FXML
    private Button submitButton;
    private Integer maxAge;
    private String bloodType;

    @FXML
    public void initialize() {
        // Ініціалізація значень ageComboBox можливими значеннями максимально віку пацієнтів.
        for(int i = 1; i < 200; i++) {
            maxAgeCmbBox.getItems().add(i);
        }

        // Ініціалізація значень bloodTypes можливими значеннями групи крові.
        String[] bloodTypes = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        bloodTypeCmbBox.setItems(FXCollections.observableArrayList(bloodTypes));

        // Встановлення кнопки у вимкнений режим, тобто кнопку неможливо натиснути.
        submitButton.setDisable(true);
    }
    /**
     * Функція натискання на кнопку завершення роботи вікна зі збереженням результатів.
     */
    public void submitClick(ActionEvent actionEvent) {
        maxAge = maxAgeCmbBox.getValue();
        bloodType = bloodTypeCmbBox.getValue();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Перевірка чи встановлені значення comboBox.
     */
    public void isValidParameters() {
        submitButton.setDisable(maxAgeCmbBox.getValue() == null || bloodTypeCmbBox.getValue() == null);
    }
    /**
     * Функція для передачі максимального віку між stage-контролерами.
     */
    public Integer getMaxAge() {
        return maxAge;
    }

    /**
     * Функція для передачі групи крові між stage-контролерами.
     */
    public String getBloodType() {
        return bloodType;
    }

}

