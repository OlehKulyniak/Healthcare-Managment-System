package mainStage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

// Для запуску програми з використанням JavaFX, потрібно наслідувати клас Application та перевизначити функцію start.
public class ProgramStart extends Application {

    // Обов'язкова функція для запуску програми з використанням JavaFX.
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("Medex");
            Image icon = new Image("/Images/MedicalLogo.png");
            stage.getIcons().add(icon);
            stage.centerOnScreen();
            stage.show();
        }
        catch(Exception error) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Launch error");
            alert.setContentText("The application launch was interrupted. Try restarting the application " +
                                                                          "or checking the application files");
            alert.showAndWait();
        }
    }
    public static void main(String[] args) {
        launch();
    }
}
