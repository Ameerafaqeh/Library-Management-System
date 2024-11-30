import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PasswordDialog {

    public static boolean authenticate() {
        Stage passwordDialog = new Stage();
        passwordDialog.initModality(Modality.APPLICATION_MODAL);
        passwordDialog.setTitle("Enter Password");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> handleAuthentication(passwordDialog, passwordField.getText()));

        VBox dialogVBox = new VBox(20);
        dialogVBox.getChildren().addAll(passwordField, submitButton);
        dialogVBox.setAlignment(Pos.CENTER);

        Scene dialogScene = new Scene(dialogVBox, 300, 150);
        passwordDialog.setScene(dialogScene);

        passwordDialog.showAndWait();

        // Ì„ﬂ‰ﬂ  Œ’Ì’ Â–« «” ‰«œ« ≈·Ï „‰ÿﬁ «·„’«œﬁ… «·Œ«’ »ﬂ
        return true;
    }

    private static void handleAuthentication(Stage passwordDialog, String enteredPassword) {
        if (enteredPassword.equals("0987")) {
            passwordDialog.close();
        } else {
            showAlert("Incorrect Password", "Please enter the correct password.");
        }
    }

    private static void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

