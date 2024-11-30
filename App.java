import java.util.Optional;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
public class App extends Application {

    private Stage primaryStage;
    private boolean isAuthenticated = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        authenticateUser();
    }

    private void authenticateUser() {
        if (!isAuthenticated) {
            // Create a password dialog
            Dialog<String> passwordDialog = new Dialog<>();
            passwordDialog.setTitle("Password Verification");

            // Set the button types (OK and CANCEL)
            ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            passwordDialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

            // Create a grid pane and add labels and a password field
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            PasswordField passwordField = new PasswordField();
            passwordField.setPromptText("Password");

            grid.add(new Label("Password:"), 0, 0);
            grid.add(passwordField, 1, 0);

            // Enable/Disable OK button depending on whether a password was entered
            Node loginButton = passwordDialog.getDialogPane().lookupButton(loginButtonType);
            loginButton.setDisable(true);

            // Listen for changes in the password field and enable/disable the OK button accordingly
            passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
                loginButton.setDisable(newValue.trim().isEmpty());
            });

            passwordDialog.getDialogPane().setContent(grid);

            // Request focus on the password field by default
            Platform.runLater(() -> passwordField.requestFocus());

            // Convert the result to a password when the login button is clicked
            passwordDialog.setResultConverter(dialogButton -> {
                if (dialogButton == loginButtonType) {
                    return passwordField.getText();
                }
                return null;
            });

            // Show the password dialog and get the result
            Optional<String> result = passwordDialog.showAndWait();

            // Check if the correct password is entered
            if (result.isPresent() && result.get().equals("0987")) {
                isAuthenticated = true;
            } else {
                // Incorrect password, you may choose to handle this case (e.g., show an error message)
                showAlert("Incorrect Password", "Please enter the correct password.");
                primaryStage.close(); // Close the application if the password is incorrect
            }
        }

        showMainScene();
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
	void showMainScene() {
	    // Create a StackPane as the root layout
	    StackPane root = new StackPane();
	    root.setAlignment(Pos.CENTER);

	    // Load the image
	    Image backgroundImage = new Image("file:C:/Users/karakoon_net/Downloads/background_image.jpg");
	    ImageView backgroundImageView = new ImageView(backgroundImage);

	    // Set the size of the ImageView to match the scene size
	    backgroundImageView.setFitWidth(400);
	    backgroundImageView.setFitHeight(300); // Adjust height as needed

	    // Add the ImageView to the StackPane
	    root.getChildren().add(backgroundImageView);

	    VBox mainVBox = new VBox(20);
	    mainVBox.setAlignment(Pos.TOP_LEFT); // Set alignment to TOP_LEFT

	    Button backButton = new Button("Back");
	    Button bookButton = new Button("Books");
	    Button loadsButton = new Button("Loads");
	    Button bookCategoryButton = new Button("Book Category");
	    Button logSystemButton = new Button("Log System");
	    Button helpButton = new Button("Help"); // Added Help button

	    // Set the background color, text color, and font size for buttons
	    String buttonStyle = "-fx-background-color: #8B4513; -fx-text-fill: white; -fx-font-size: 14px;";
	    backButton.setStyle(buttonStyle);
	    bookButton.setStyle(buttonStyle);
	    loadsButton.setStyle(buttonStyle);
	    bookCategoryButton.setStyle(buttonStyle);
	    logSystemButton.setStyle(buttonStyle);
	    helpButton.setStyle(buttonStyle); // Style for Help button

	    // Set the graphic (icon) for the Help button
	    ImageView helpIcon = new ImageView(new Image("file:C:/Users/karakoon_net/Downloads/help_icon.png"));
	    helpIcon.setFitWidth(16);
	    helpIcon.setFitHeight(16);
	    helpButton.setGraphic(helpIcon);

	    // Set actions for buttons
	    bookCategoryButton.setOnAction(event -> showBookCategory());
	    bookButton.setOnAction(event -> showBookManagementApp());
	    backButton.setOnAction(event -> authenticateUser());
	    loadsButton.setOnAction(event -> loasUl());
	    logSystemButton.setOnAction(event -> showlogSystem());
	    helpButton.setOnAction(event -> showHelpDialog()); // Added action for Help button

	    // Add buttons to the VBox
	    mainVBox.getChildren().addAll(bookButton, loadsButton, bookCategoryButton, logSystemButton, helpButton);

	    // Add the VBox to the StackPane, ensuring it appears above the background image
	    root.getChildren().add(mainVBox);

	    // Create the scene with the StackPane as the root
	    Scene mainScene = new Scene(root, 400, 300);

	    // Set background color for the VBox (adjust as needed)
	    mainVBox.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));

	    primaryStage.setScene(mainScene);
	    primaryStage.setTitle("Main Scene");
	    primaryStage.show();
	}

	private void loasUl() {
		LoadsUI loads = new LoadsUI(this);
		try {
			loads.start(primaryStage);
		} catch (Exception e) {
			handleException(e);
		}
	}

	private void showBookManagementApp() {
		BookManagementApp bookManagementApp = new BookManagementApp(this);
		try {
			bookManagementApp.start(primaryStage);
		} catch (Exception e) {
			handleException(e);
		}

	}
    private void password(Stage stage) {
    	PasswordVerificationApp password = new PasswordVerificationApp(this);
    	try {
    		password.start(primaryStage);
  
    	}catch (Exception e) {
    		handleException(e);
		}
    }
	private void showBookCategory() {
		BookCategoryApp bookCategoryApp = new BookCategoryApp(this);

		try {
			bookCategoryApp.start(primaryStage);

		} catch (Exception e) {
			handleException(e);
		}

	}
 private void showlogSystem() {
	 LogSystemApp logSystem =new LogSystemApp(this);
	 try {
		 logSystem.start(primaryStage);
	 }catch(Exception e) {
		 handleException(e);
	 }
 }
 
	private void handleException(Exception e) {
		e.printStackTrace();
		showAlert("Error", "An error occurred. Please try again.");
	}
	private void showHelpDialog() {
	    // You can use JavaFX Dialog to display a help dialog window
	    Dialog<String> helpDialog = new Dialog<>();
	    helpDialog.setTitle("Help");

	    // Add labels for email and phone to the dialog
	    VBox dialogContent = new VBox();
	    dialogContent.setSpacing(10);

	    // Labels for email and phone
	    Label emailLabel = new Label("Email: ameerafaqeh@gaiml.com");
	    Label phoneLabel = new Label("Phone: 064829324");


	    dialogContent.getChildren().addAll(emailLabel, phoneLabel);

	    helpDialog.getDialogPane().setContent(dialogContent);

	    // Add a close button to the dialog
	    helpDialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

	    // Show the dialog window
	    helpDialog.showAndWait();
	}

}
