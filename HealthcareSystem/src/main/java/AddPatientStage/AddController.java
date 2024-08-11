package addPatientStage;

import model.Patient;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.Node;

import java.time.Year;
import java.util.Arrays;

public class AddController {
    @FXML
    private TextField fullNameField;
    @FXML
    private ComboBox<Integer> birthYearCmbBox;
    @FXML
    private ComboBox<String> genderCmbBox;
    @FXML
    private ComboBox<String> bloodTypeCmbBox;
    @FXML
    private RadioButton yesRadioButton;
    @FXML
    private ToggleGroup answerToggleGroup;
    @FXML
    private TextField chrDiseaseField;
    @FXML
    private TextField vaccinationsField;
    @FXML
    private Button submitButton;
    @FXML
    private Label fullNameMsgLabel;
    @FXML
    private Label chrDiseaseMsgLabel;
    @FXML
    private Label vaccMsgLabel;
    private Patient newPatient;

    private final String primaryColor = "#3366FF";

    @FXML
    public void initialize() {
        //Ініціалізація значень birthYearComboBox можливими значеннями року народження пацієнта.
        int currYear = Year.now().getValue();
        Integer[] years = new Integer[currYear + 1 - (currYear - 180)];
        for(int i = currYear; i >= currYear - 180; i--) {
           years[Year.now().getValue() - i] = i;
        }
        birthYearCmbBox.getItems().addAll(years);

        //Ініціалізація значень genderComboBox можливими значеннями статі пацієнта.
        String[] genders = {"Male", "Female", "Other"};
        genderCmbBox.getItems().addAll(genders);

        //Ініціалізація значень bloodTypeComboBox можливими значеннями групи крові пацієнта.
        String[] bloodTypes = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        bloodTypeCmbBox.getItems().addAll(bloodTypes);

        submitButton.setDisable(true);
    }
    /**
     * Функція завершення роботи вікна зі збереженням результатів.
     */
    public void submitClick(ActionEvent actionEvent) {
        newPatient = new Patient(fullNameField.getText(),
                                 birthYearCmbBox.getValue(),
                                 genderCmbBox.getValue().charAt(0),
                                 bloodTypeCmbBox.getSelectionModel().getSelectedItem(),
                                 yesRadioButton.isSelected(),
                                 chrDiseaseField.getText(),
                                 Arrays.stream(vaccinationsField.getText().split("\\|")).toList());
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Функція передачі пацієнта між stage-контролерами.
     */
    public Patient getNewPatient() {
        return newPatient;
    }

    /**
     * Функція перевірки поля повного ім'я пацієнта при введені тексту в поле fullNameField.
     */
    public void isFullNameValidOnKR() {
        submitButton.setDisable(!isValidParameters());
        if (fullNameField.getText().isBlank()) {
            fullNameField.setStyle("-fx-border-color: " + primaryColor);
            fullNameMsgLabel.setText("");
            return;
        }
        if (!isValidFullName(fullNameField.getText())) {
            fullNameField.setStyle("-fx-border-color: red");
             if(fullNameField.getText().length() < 3) {
                 fullNameMsgLabel.setText("The length must be greater than 2");
             } else if(fullNameField.getText().length() > 30) {
                 fullNameMsgLabel.setText("The length must be less than or equal to 30 characters");
             } else {
                 fullNameMsgLabel.setText("Incorrect symbol founded");
             }
        } else {
            fullNameField.setStyle("-fx-border-color: " + primaryColor);
            fullNameMsgLabel.setText("");
        }
    }

    /**
     * Функція перевірки поля хронічного захворювання при введені тексту в поле chrDiseaseField.
     */
    public void isChrDiseaseValidOnKR() {
        submitButton.setDisable(!isValidParameters());
        if(chrDiseaseField.getText().isBlank()) {
            chrDiseaseField.setStyle("-fx-border-color: " + primaryColor);
            chrDiseaseMsgLabel.setText("");
            return;
        }
        if(!isValidVaccinations(chrDiseaseField.getText())) {
            chrDiseaseField.setStyle("-fx-border-color: red");
            if(chrDiseaseField.getText().length() > 30) {
                chrDiseaseField.setText("The length must be less than or equal to 30 characters");
            } else {
                chrDiseaseMsgLabel.setText("Cannot contain @ ! # $ % ^ & * ~ ? > < \" ' / \\ ");
            }
        } else {
            chrDiseaseField.setStyle("-fx-border-color: " + primaryColor);
            chrDiseaseMsgLabel.setText("");
        }
    }

    /**
     * Функція перевірки поля масиву вакцинацій при введені тексту в поле vaccinationsField.
     */
    public void isVaccValidOnKR() {
        submitButton.setDisable(!isValidParameters());
        if(vaccinationsField.getText().isBlank()) {
            vaccinationsField.setStyle("-fx-border-color: " + primaryColor);
            vaccMsgLabel.setText("");
            return;
        }
        if(!isValidVaccinations(vaccinationsField.getText())) {
            vaccinationsField.setStyle("-fx-border-color: red");
            if(vaccinationsField.getText().length() > 100) {
                vaccMsgLabel.setText("The length must be less than or equal to 100 characters");
            } else {
                vaccMsgLabel.setText("Cannot contain @ ! # $ % ^ & * ~ ? > < \" ' / \\");
            }
        } else {
            vaccinationsField.setStyle("-fx-border-color: " + primaryColor);
            vaccMsgLabel.setText("");
        }

    }
    /**
     * Перевірка чи всі параметри введені коректно для ввімкнення кнопки submit.
     */
    public boolean isValidParameters() {
        return (isValidFullName(fullNameField.getText()) &&
           !birthYearCmbBox.getSelectionModel().isEmpty() &&
           !genderCmbBox.getSelectionModel().isEmpty() &&
           !bloodTypeCmbBox.getSelectionModel().isEmpty() &&
           !(answerToggleGroup.getSelectedToggle() == null) &&
           isValidDisease(chrDiseaseField.getText()) &&
           isValidVaccinations(vaccinationsField.getText()));
    }

    /**
     * Функція, що перевіряє вміст поля fullNameField на коректні дані.
     */
    public boolean isValidFullName(String str) {
        return str.length() > 2 && str.length() <= 30 && !str.matches("(.*)[@!#$%^&*~?><\"'/\\\\](.*)");
    }

    /**
     * Функція, що перевіряє вміст поля chrDiseaseField на коректні дані.
     */
    public boolean isValidDisease(String str) {
        return !str.isEmpty() && str.length() <= 30 && (!str.matches("(.*)[@!#$%^&*~?><\"'/\\\\](.*)"));
    }

    /**
     * Функція, що перевіряє вміст поля vaccinationsField на коректні дані.
     */
    public boolean isValidVaccinations(String str) {
        return !str.isBlank() && str.length() <= 100 && (!str.matches("(.*)[@!#$%^&*~?><\"'/\\\\](.*)"));
    }




}
