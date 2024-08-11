package mainStage;

import exceptions.EmptyListError;
import exceptions.FileNotSelected;
import exceptions.InvalidFileData;
import exceptions.PatientAddingError;
import exceptions.PatientDeletionError;
import exceptions.StageLaunchError;
import vaccinationsStage.VaccController;
import addPatientStage.AddController;
import setConditionsStage.SetCondController;
import alertMessage.AlertController;
import model.Patient;

import javafx.geometry.Insets;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.TableCell;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.util.converter.CharacterStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.*;
import java.time.Year;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.LinkedHashSet;
public class MainController {
    @FXML
    private TableView<Patient> mainTableView;
    @FXML
    private TableColumn<Patient, String> fullNameCol;
    @FXML
    private TableColumn<Patient, Integer> birthYearCol;
    @FXML
    private TableColumn<Patient, Character> genderCol;
    @FXML
    private TableColumn<Patient, String> bloodTypeCol;
    @FXML
    private TableColumn<Patient, String> isDisabledCol;
    @FXML
    private TableColumn<Patient, String> chrDiseaseCol;
    @FXML
    private TableColumn<Patient, Patient> vaccinationsCol;
    @FXML
    private TextField searchField;
    @FXML
    private ToggleButton maxAgeCrtBTypeTButton;
    @FXML
    private ToggleButton oldstLstVaccTButton;
    @FXML
    private ToggleButton maxVaccNoDsblTButton;
    @FXML
    private VBox actionButtonsVBox;
    private List<Patient> patients;

    @FXML
    public void initialize() {
        mainTableView.setEditable(true);
        //Встановлення кожного поля класу Patient у відповідний стовпець таблиці mainTableView.
        fullNameCol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getFullName()));
        birthYearCol.setCellValueFactory(new PropertyValueFactory<>("birthYear"));
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        bloodTypeCol.setCellValueFactory(new PropertyValueFactory<>("bloodType"));
        isDisabledCol.setCellValueFactory(columnCellData ->
                      ((columnCellData.getValue().getIsDisabled()) ? new SimpleStringProperty("Yes") :
                                                                     new SimpleStringProperty("No" ))
        );
        chrDiseaseCol.setCellValueFactory(new PropertyValueFactory<>("chrDisease"));


        //Встановлення в стовпець таблиці Vaccinations кнопки.
        //При натисканні створює новий Stage для виконнання дій зі щепленнями певного пацієнта.
        vaccinationsCol.setCellValueFactory(columnCellData ->
                        new ReadOnlyObjectWrapper<>(columnCellData.getValue()));
        vaccinationsCol.setCellFactory(columnCellData -> new TableCell<>() {
            private final Button vaccinationButton = new Button();
            @Override
            protected void updateItem(Patient item, boolean empty) {
                super.updateItem(item, empty);

                if(item == null) {
                    setGraphic(null);
                } else {
                    vaccinationButton.setText(getItem().getVaccinations()
                                                       .toString().replaceAll("[\\[\\]]", ""));
                    vaccinationButton.setPrefWidth(columnCellData.getPrefWidth());
                    vaccinationButton.getStyleClass().add("vaccinationButton");
                    setGraphic(vaccinationButton);
                    vaccinationButton.setOnAction(event -> getVaccinationsClick(item, getIndex()));

                }
            }

        });

        fullNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        fullNameCol.setOnEditCommit(event -> {
            if(!event.getNewValue().isBlank() && event.getNewValue().length() <= 30) {
                Patient patient = event.getRowValue();
                patient.setFullName(event.getNewValue());
                patients.set(event.getTablePosition().getRow(), patient);
            } else {
                mainTableView.getItems().set(event.getTablePosition().getRow(), event.getRowValue());
            }
        });

        birthYearCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        birthYearCol.setOnEditCommit(event -> {
            if(event.getNewValue() > Year.now().getValue() - 180 && event.getNewValue() < Year.now().getValue()) {
                Patient patient = event.getRowValue();
                patient.setBirthYear(event.getNewValue());
                patients.set(event.getTablePosition().getRow(), patient);
            } else {
                mainTableView.getItems().set(event.getTablePosition().getRow(), event.getRowValue());
            }
        });

        genderCol.setCellFactory(TextFieldTableCell.forTableColumn(new CharacterStringConverter()));
        genderCol.setOnEditCommit(event -> {
            if(event.getNewValue() == 'M' || event.getNewValue() == 'F' || event.getNewValue() == 'O') {
                Patient patient = event.getRowValue();
                patient.setGender(event.getNewValue());
                patients.set(event.getTablePosition().getRow(), patient);
            } else {
                mainTableView.getItems().set(event.getTablePosition().getRow(), event.getRowValue());
            }
        });

        bloodTypeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        bloodTypeCol.setOnEditCommit(event -> {
            String newBloodType = event.getNewValue();
            if(newBloodType.equals("A+") || newBloodType.equals("A-") ||
               newBloodType.equals("B+") || newBloodType.equals("B-") ||
               newBloodType.equals("AB+") || newBloodType.equals("AB-") ||
               newBloodType.equals("O+") || newBloodType.equals("O-")) {
                Patient patient = event.getRowValue();
                patient.setBloodType(event.getNewValue());
                patients.set(event.getTablePosition().getRow(), patient);
            } else {
                mainTableView.getItems().set(event.getTablePosition().getRow(), event.getRowValue());
            }
        });

        isDisabledCol.setCellFactory(TextFieldTableCell.forTableColumn());
        isDisabledCol.setOnEditCommit(event -> {
            Patient patient = event.getRowValue();
            if(event.getNewValue().equals("Yes")) {
                patient.setIsDisabled(true);
                patients.set(event.getTablePosition().getRow(), patient);
            } else if(event.getNewValue().equals("No")) {
                patient.setIsDisabled(false);
                patients.set(event.getTablePosition().getRow(), patient);
            } else {
                mainTableView.getItems().set(event.getTablePosition().getRow(), patient);
            }
        });

        chrDiseaseCol.setCellFactory(TextFieldTableCell.forTableColumn());
        chrDiseaseCol.setOnEditCommit(event -> {
            if(!event.getNewValue().isBlank() && event.getNewValue().length() <= 30) {
                Patient patient = event.getRowValue();
                patient.setChrDisease(event.getNewValue());
                patients.set(event.getTablePosition().getRow(), patient);
            } else {
                mainTableView.getItems().set(event.getTablePosition().getRow(), event.getRowValue());
            }
        });

        //Встановлення назви кнопки для ToggleButtons.
        maxAgeCrtBTypeTButton.setText("Younger with given\nblood type");
        oldstLstVaccTButton.setText("Oldest with least vaccinations");
        maxVaccNoDsblTButton.setText("Max vaccinations and\nnot disabled");
        patients = new ArrayList<>();
        actionButtonsVBox.heightProperty().addListener((observable, oldValue, newValue) -> {
           actionButtonsVBox.setPadding(new Insets(newValue.doubleValue() / 10, 0, 0,0));
           actionButtonsVBox.setSpacing(actionButtonsVBox.getHeight() / 25);
        });
    }

    /**
    * Функція пошуку та виведення пацієнтів за повним ім'ям, заданим щепленням,
    * заданим хронічним захворюванням та групою крові.
    */
    public void searchFilterOnKR() {
        Set<Patient> searchingRes = new LinkedHashSet<>();
        if(searchField.getText().isBlank()) {
            mainTableView.setItems(FXCollections.observableArrayList(patients));
            return;
        }
        searchingRes.addAll(Patient.findByFullName(patients, searchField.getText()));
        searchingRes.addAll(Patient.findByBloodType(patients, searchField.getText()));
        searchingRes.addAll(Patient.findByChrDisease(patients, searchField.getText()));
        searchingRes.addAll(Patient.findByVaccination(patients, searchField.getText()));
        mainTableView.getItems().clear();
        mainTableView.setItems(FXCollections.observableArrayList(searchingRes));
    }

    /**
     * Функція скасування пошуку за певними параметром та встановлення початкового списку patients у mainTableView.
     */
    public void allPatientsClick() {
        maxAgeCrtBTypeTButton.setSelected(false);
        oldstLstVaccTButton.setSelected(false);
        maxVaccNoDsblTButton.setSelected(false);
        searchField.clear();
        mainTableView.getItems().clear();
        mainTableView.setItems(FXCollections.observableArrayList(patients));
    }

    /**
     * Функція отримання та виведення списку пацієнтів з .dat або .txt файлу.
     */
    public void fGetListClick() {
        try {
            Stage fileStage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                               new FileChooser.ExtensionFilter("Dat files (*.dat)", "*.dat"),
                               new FileChooser.ExtensionFilter("Bin files (*.bin)", "*.bin"));

            fileChooser.setInitialDirectory(new File("Patients Lists"));
            File readFile = fileChooser.showOpenDialog(fileStage);
            if (readFile == null) {
                throw new FileNotSelected();
            }
            patients.clear();
            try {
                FileInputStream fileInputStream = new FileInputStream(readFile);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                int size = objectInputStream.readInt();
                for (int i = 0; i < size; i++) {
                    Patient patient = new Patient();
                    patient.readExternal(objectInputStream);
                    patients.add(patient);
                }
            }
            catch(Exception error) {
                throw new InvalidFileData();
            }
            mainTableView.setItems(FXCollections.observableArrayList(patients));
        }
        catch(InvalidFileData error) {
            AlertController alertController = new AlertController();
            alertController.createMessage("Incorrect Parameters", error.getMessage());
        }
        catch(FileNotSelected error) {
            AlertController alertController = new AlertController();
            alertController.createMessage("Select File Error",  error.getMessage());
        }
        catch(Exception error) {
            AlertController alertController = new AlertController();
            alertController.createMessage("Unknown Error", "An unknown error has occured." +
                                                                          "\nTry restarting the program.");
        }
    }

    /**
     * Функція збереження списку пацієнтів у файл.
     */
    public void fSetListClick() {
        try {
            Stage fileStage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                               new FileChooser.ExtensionFilter("Dat files (*.dat)", "*.dat"),
                               new FileChooser.ExtensionFilter("Bin files (*.bin)", "*.bin"));
            fileChooser.setInitialDirectory(new File("Patients Lists"));
            File writeFile = fileChooser.showSaveDialog(fileStage);
            if(writeFile == null) {
                throw new FileNotSelected("The patient list was not saved");
            }
            FileOutputStream fileOutputStream =  new FileOutputStream(writeFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeInt(patients.size());
            for(Patient patient : patients) {
                patient.writeExternal(objectOutputStream);
            }
            objectOutputStream.close();
        }
        catch(FileNotSelected error) {
            AlertController alertController = new AlertController();
            alertController.createMessage("Save file Error", error.getMessage());
        }
        catch(Exception error) {
            AlertController alertController = new AlertController();
            alertController.createMessage("Unknown Error.",
                                     "An unknown error has occured." +
                                               "\nTry restarting the program.");
        }
    }

    /**
     * Функція пошуку та виведення пацієнтів молодших заданого віку та з певною групою крові. <br>
     * Функція створює Stage - SetConditionStage.
     */
    public void findMaxAgeCrtBTypeClick() {
        try {
            if(patients.isEmpty()) {
                maxAgeCrtBTypeTButton.setSelected(false);
                throw new EmptyListError();
            }
            if(maxAgeCrtBTypeTButton.isSelected()) {
                oldstLstVaccTButton.setSelected(false);
                maxVaccNoDsblTButton.setSelected(false);
                FXMLLoader loader = new FXMLLoader(getClass()
                                                   .getResource("/setConditionsStage/SetCondScene.fxml"));
                Stage findStage = new Stage();
                Scene scene;
                try {
                    scene = new Scene(loader.load());

                } catch(Exception error) {
                    throw new StageLaunchError();
                }
                findStage.setScene(scene);
                findStage.setTitle("Search Conditions Manager");
                Image icon = new Image("Images/MedicalLogo.png");
                findStage.getIcons().add(icon);
                findStage.initModality(Modality.APPLICATION_MODAL);
                findStage.centerOnScreen();
                findStage.setResizable(false);
                findStage.showAndWait();
                SetCondController setCondController = loader.getController();
                if(setCondController.getMaxAge() == null || setCondController.getBloodType() == null) {
                    maxAgeCrtBTypeTButton.setSelected(false);
                    return;
                }
                mainTableView.getItems().clear();
                mainTableView.setItems(FXCollections.observableArrayList(Patient.findMaxAgeCrtBType(patients,
                                                                          setCondController.getMaxAge(),
                                                                          setCondController.getBloodType())));
            } else {
                mainTableView.getItems().clear();
                mainTableView.setItems(FXCollections.observableArrayList(patients));
            }
        }
        catch(EmptyListError error) {
            AlertController alertController = new AlertController();
            alertController.createMessage("Empty List Error", error.getMessage());
        }
        catch(StageLaunchError error) {
            AlertController alertController = new AlertController();
            alertController.createMessage("Stage Launch Error",
                                     "The Search Conditions Manager stage is not open." +
                                               "Try restarting the program.");
        }
        catch(Exception error) {
            AlertController alertController = new AlertController();
            alertController.createMessage("Unknown Error",
                                     "An unknown error has occured." +
                                               "\nTry restarting the program.");
        }
    }

    /**
     * Функція пошуку та виведення найстарших пацієнтів з найменшою кількістю щеплень.
     */
    public void findMaxAgeMinVaccClick() {
        try {
            if(patients.isEmpty()) {
                oldstLstVaccTButton.setSelected(false);
                throw new EmptyListError();
            }
            if(oldstLstVaccTButton.isSelected()) {
                maxAgeCrtBTypeTButton.setSelected(false);
                maxVaccNoDsblTButton.setSelected(false);
                mainTableView.getItems().clear();
                mainTableView.setItems(FXCollections.observableArrayList(Patient.findMaxAgeMinVacc(patients)));
            } else {
                mainTableView.getItems().clear();
                mainTableView.setItems(FXCollections.observableArrayList(patients));
            }
        }
        catch(EmptyListError error) {
            AlertController alertController = new AlertController();
            alertController.createMessage("Empty List Error", error.getMessage());
        }
        catch(Exception error) {
            AlertController alertController = new AlertController();
            alertController.createMessage("Unknown Error",
                                     "An unknown error has occured." +
                                               "\nTry restarting the program.");
        }
    }

    /**
     * Функція пошуку та виведення пацієнтів з найбільшою кількістю вакцинацій та без інвалідності.
     */
    public void findMaxVaccNoDsblClick() {
        try {
            if(patients.isEmpty()) {
                maxVaccNoDsblTButton.setSelected(false);
                throw new EmptyListError();
            }
            if (maxVaccNoDsblTButton.isSelected()) {
                maxAgeCrtBTypeTButton.setSelected(false);
                oldstLstVaccTButton.setSelected(false);
                mainTableView.getItems().clear();
                mainTableView.setItems(FXCollections.observableArrayList(Patient.findMaxVaccNoDsbl(patients)));
            } else {
                mainTableView.getItems().clear();
                mainTableView.setItems(FXCollections.observableArrayList(patients));
            }
        }
        catch(EmptyListError error) {
            AlertController alertController = new AlertController();
            alertController.createMessage("Empty List Error", error.getMessage());
        }
        catch(Exception error) {
            AlertController alertController = new AlertController();
            alertController.createMessage("Unknown Error",
                                     "An unknown error has occured." +
                                               "\nTry restarting the program.");
        }
    }

    /**
     * Функція додавання нового елемента до списку patients та його виведення в mainTableView. <br>
     * Функція створює Stage - AddPatientStage.
     */
    public void addPatientClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addPatientStage/AddScene.fxml"));
            Stage addPatientStage = new Stage();
            Scene scene;
            try {
                scene = new Scene(loader.load());
            }
            catch(Exception error) {
                throw new StageLaunchError();
            }
            addPatientStage.setScene(scene);
            addPatientStage.setTitle("Patient Adding Manager");
            Image icon = new Image("/Images/MedicalLogo.png");
            addPatientStage.getIcons().add(icon);
            addPatientStage.centerOnScreen();
            addPatientStage.setResizable(false);
            addPatientStage.showAndWait();
            AddController addController = loader.getController();
            if(addController.getNewPatient() == null) {
                throw new PatientAddingError();
            }
            patients.add(addController.getNewPatient());
            mainTableView.getItems().add(addController.getNewPatient());
        }
        catch(PatientAddingError error) {
            AlertController alertController = new AlertController();
            alertController.createMessage("Patient Adding Error", error.getMessage());
        }
        catch(StageLaunchError error) {
            AlertController alertController = new AlertController();
            alertController.createMessage("Stage Launch Error",
                                     "The Patient Adding Manager stage is not open." +
                                               "Try restarting the program.");
        }
        catch(Exception error) {
            AlertController alertController = new AlertController();
            alertController.createMessage("Unknown Error",
                                     "An unknown error has occured." +
                                               "\nTry restarting the program.");
        }
    }

    /**
     * Функція видалення пацієнта зі списку patient та таблиці mainTableView.
     */
    public void deletePatientClick() {
        try {
            if (mainTableView.getSelectionModel().isEmpty()) {
                throw new PatientDeletionError();
            }
            mainTableView.getItems().remove(mainTableView.getSelectionModel().getSelectedIndex());
            patients.remove(mainTableView.getSelectionModel().getSelectedIndex());
        }
        catch(PatientDeletionError error) {
            AlertController alertController = new AlertController();
            alertController.createMessage("Deletion Error", error.getMessage());
        }
        catch(Exception error) {
            AlertController alertController = new AlertController();
            alertController.createMessage("Unknown Error",
                                     "An unknown error has occured." +
                                               "\nTry restarting the program.");
        }
    }

    /**
     * Функція створення вікна Vaccinations Manager для виконання операцій над масивом щеплень. <br>
     * Функія створює Stage - VaccinationsStage.
     */
    public void getVaccinationsClick(Patient patient, int index) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vaccinationsStage/VaccScene.fxml"));
            Stage vaccinationsStage = new Stage();
            Scene scene;
            try {
                scene = new Scene(loader.load());
            }
            catch(Exception error) {
                throw new StageLaunchError();
            }
            VaccController vaccController = loader.getController();
            vaccController.setCurrPatient(patient);
            vaccinationsStage.setScene(scene);
            vaccinationsStage.setTitle("Vaccinations Manager");
            Image icon = new Image("/Images/MedicalLogo.png");
            vaccinationsStage.getIcons().add(icon);
            vaccinationsStage.centerOnScreen();
            vaccinationsStage.setResizable(false);
            vaccinationsStage.initModality(Modality.APPLICATION_MODAL);
            vaccinationsStage.showAndWait();
            if(vaccController.getCurrPatient() != null) {
                patients.set(index, new Patient(vaccController.getCurrPatient()));
                mainTableView.getItems().set(index, patients.get(index));
            }
        }
        catch(StageLaunchError error) {
            AlertController alertController = new AlertController();
            alertController.createMessage("Stage Launch Error",
                                     "The Vaccinations Manager stage was not opened." +
                                               "Try restarting the program.");
        }
        catch(Exception error) {
            AlertController alertController = new AlertController();
            alertController.createMessage("Unknown Error",
                                     "An unknown error has occured." +
                                               "\nTry restarting the program.");
        }

    }
}

