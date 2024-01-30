package MainStage;

import Exceptions.EmptyListError;
import Exceptions.FileNotSelected;
import Exceptions.InvalidFileData;
import Exceptions.PatientAddingError;
import Exceptions.PatientDeletionError;
import Exceptions.StageLaunchError;
import VaccinationsStage.VaccController;
import AddPatientStage.AddController;
import SetConditionsStage.SetCondController;
import AlertMessage.AlertController;

import PatientPack.Patient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableCell;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

import java.io.*;
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
    private List<Patient> patients;

    @FXML
    public void initialize() {
        //Встановлення кожного поля класу Patient у відповідний стовпець таблиці mainTableView.
        fullNameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
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
        //Встановлення назви кнопки для ToggleButtons.
        maxAgeCrtBTypeTButton.setText("Younger with given\nblood type");
        oldstLstVaccTButton.setText("Oldest with least vaccinations");
        maxVaccNoDsblTButton.setText("Max vaccinations and\nnot disabled");

        //Створення списку пацієнтів.
        patients = new ArrayList<>();
    }

    /**
    * Функція пошуку та виведення пацієнтів за повним ім'ям, заданим щепленням,
    * заданим хронічним захворюванням та групою крові.
    */
    public void searchFilterOnKR() {
        Set<Patient> searchingRes = new LinkedHashSet<>();
        if(searchField.getText().isEmpty() || searchField.getText().isBlank() || searchField.getText() == null) {
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
        try {
            maxAgeCrtBTypeTButton.setSelected(false);
            oldstLstVaccTButton.setSelected(false);
            maxVaccNoDsblTButton.setSelected(false);
            searchField.clear();
            mainTableView.getItems().clear();
            mainTableView.setItems(FXCollections.observableArrayList(patients));
        }
        catch(Exception error) {
            AlertController alertController = new AlertController();
            alertController.createMessage("Unknown Error",
                                     "An unknown error has occured." +
                                               "\nTry restarting the program.");
        }
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
            if(patients.size() == 0) {
                maxAgeCrtBTypeTButton.setSelected(false);
                throw new EmptyListError();
            }
            if(maxAgeCrtBTypeTButton.isSelected()) {
                oldstLstVaccTButton.setSelected(false);
                maxVaccNoDsblTButton.setSelected(false);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/SetConditionsStage/SetCondScene.fxml"));
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
            if(patients.size() == 0) {
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
            if(patients.size() == 0) {
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddPatientStage/AddScene.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/VaccinationsStage/VaccScene.fxml"));
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

