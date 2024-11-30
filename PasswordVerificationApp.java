import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class PasswordVerificationApp extends Application {

    private String correctDatabasePassword = "0987";
    private App mainApp;
    private boolean passwordCorrect;

    public PasswordVerificationApp(App mainApp) {
        this.mainApp = mainApp;
    }

    public boolean isPasswordCorrect() {
        return passwordCorrect;
    }

    private void goBackToMainScene() {
        mainApp.showMainScene();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Database Password Verification");

        // Create the GridPane layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Add controls to the GridPane
        Label passwordLabel = new Label("Enter Database Password:");
        PasswordField passwordField = new PasswordField();
        Button verifyButton = new Button("Verify");

        // Add controls to the GridPane
        grid.add(passwordLabel, 0, 0);
        grid.add(passwordField, 1, 0);
        grid.add(verifyButton, 1, 1);

        // Set actions for the Verify button
        verifyButton.setOnAction(event -> verifyPassword(passwordField.getText()));

        // Create the scene and set it on the stage
        Scene scene = new Scene(grid, 300, 150);
        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();
    }

    private void verifyPassword(String enteredPassword) {
        if (enteredPassword.equals(correctDatabasePassword)) {
            // Password is correct, proceed to the main scene
            System.out.println("Password is correct. Proceed to the main scene.");
            passwordCorrect = true;
            goBackToMainScene();
        } else {
            // Password is incorrect, show an error message
            System.out.println("Incorrect password. Please try again.");
            passwordCorrect = false;

            // Show an alert for incorrect password
            showAlert("Incorrect Password", "The entered password is incorrect. Please try again.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
