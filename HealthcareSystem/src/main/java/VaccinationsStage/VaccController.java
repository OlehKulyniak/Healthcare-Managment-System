package VaccinationsStage;

import PatientPack.Patient;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class VaccController {
    @FXML
    private Label whoseVaccLabel;
    @FXML
    private TextField newVaccField;
    @FXML
    private Button addVaccButton;
    @FXML
    private TableView<String> vaccTableView;
    @FXML
    private TableColumn<String, String> vaccinationColumn;
    @FXML
    private TableColumn<String, String> actionColumn;
    @FXML
    private Label newVaccMsgLabel;
    private Patient currPatient;

    @FXML
    public void initialize() {
        // Встановлення значення назви вакцинації в стовпець vaccinationColumn.
        vaccinationColumn.setCellValueFactory(elem -> new SimpleStringProperty(elem.getValue()));
        actionColumn.setSortable(false);
        actionColumn.setEditable(false);
        // Встановлення кнопки видалення щеплення з масиву щеплень у стовпець actionColumn.
        actionColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        actionColumn.setCellFactory(columnCellData -> new TableCell<>() {
            private final Button deleteButton = new Button();
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item,empty);

                if(item == null) {
                    setGraphic(null);
                } else {
                    deleteButton.setText("Delete");
                    deleteButton.getStyleClass().add("deleteButton");
                    setGraphic(deleteButton);
                    deleteButton.setOnAction(event -> { getTableView().getItems().remove(item);
                                                        currPatient.delVaccination(item);});
                }
            }

        });
        // Встановлення кнопки у вимкнений режим, тобто кнопка неможливо натиснути.
        addVaccButton.setDisable(true);
    }

    /**
     * Функція додавання щеплення до масиву пацієнта та виведення в таблицю vaccTableView.
     */
    public void addVaccClick() {
        currPatient.addVaccination(newVaccField.getText());
        vaccTableView.getItems().add(newVaccField.getText());
        newVaccField.clear();
    }

    /**
     * Функція завершення роботи вікна Vaccination Manager зі збереженням результатів.
     */
    public void okClick(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Функція для передачі даних на інший stage-контролер.
     */
    public Patient getCurrPatient() {
        return currPatient;
    }

    /**
     * Функція для отрмання даних з stage-контролера іншого вікна.
     */
    public void setCurrPatient(Patient patient) {
        currPatient = patient;
        vaccTableView.setItems(FXCollections.observableArrayList(currPatient.getVaccinations()));
        whoseVaccLabel.setText(patient.getFullName() + "'s vaccinations");
    }

    /**
     * Функція перевірки поля нового щеплення newVaccField.
     * */
    public void isNewVaccValidOnKR() {
        if(newVaccField.getText().isEmpty() || newVaccField.getText().isBlank()) {
            newVaccField.setStyle("-fx-border-color: #3366FF");
            newVaccMsgLabel.setText("");
            addVaccButton.setDisable(true);
            return;
        }
        if(!isValidString(newVaccField.getText())) {
            newVaccField.setStyle("-fx-border-color: red");
            if(newVaccField.getText().length() > 30) {
                newVaccMsgLabel.setText("The length must be less than or equal to 30 characters");
            } else {
                newVaccMsgLabel.setText("Cannot contain @ ! # $ % ^ & * ~ ? > < \" ' / \\");
            }
            addVaccButton.setDisable(true);
        } else {
            newVaccField.setStyle("-fx-border-color: #3366FF");
            newVaccMsgLabel.setText("");
            addVaccButton.setDisable(false);
        }
    }

    /**
     * Перевірка на коректність вміст поля назви нового щеплення newVaccField.
     */
    boolean isValidString(String str) {
        return str.length() <= 30 && !str.matches("(.*)[@!#$%^&*~><?\"'/\\\\](,*)");
    }
}
