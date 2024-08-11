package alertMessage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AlertController {
    @FXML
    private Label titleLabel;
    @FXML
    private Label infoLabel;
    @FXML
    private Button okButton;
    @FXML
    private Stage stage;
    private double x = 0, y = 0;

    public void setTitle(String title) {
        titleLabel.setText(title);
    }
    public void setInformation(String information) {
        infoLabel.setText(information);
    }
    public void closeClick(ActionEvent event) {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    public void onMouseDragged(MouseEvent event) {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }
    public void onMousePressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }
    public AlertController createMessage(String title, String information) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/alertMessage/AlertScene.fxml"));
            Scene scene = new Scene(loader.load());
            scene.setFill(Color.TRANSPARENT);
            stage = new Stage();
            stage.setScene(scene);
            AlertController alertController = loader.getController();
            alertController.setTitle(title);
            alertController.setInformation(information);
            Image icon = new Image("/Images/MedicalLogo.png");
            stage.getIcons().add(icon);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();
            return alertController;
        }
        catch(Exception error) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Alert Message Error");
            alert.setContentText("Unable to create alert message, try to restart the program");
            alert.showAndWait();
        }
        return null;
    }
    public void setOKButtonStyle(String staticStyle, String hoverStyle, String pressedStyle) {
        okButton.setStyle(staticStyle);
        okButton.setOnMouseEntered(event -> okButton.setStyle(hoverStyle));
        okButton.setOnMouseExited(event -> okButton.setStyle(staticStyle));
        okButton.setOnMousePressed(event -> okButton.setStyle(pressedStyle));

    }
}
