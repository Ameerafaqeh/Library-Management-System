import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.*;
import java.util.Properties;

public class LoadsUI extends Application {
    private ObservableList<Loads> loadsList = FXCollections.observableArrayList();
    private TableView<Loads> loadsTableView;

    private String dbURL;
    private String dbUsername = "root";
    private String dbPassword = "0987";
    private String URL = "127.0.0.1";
    private String port = "3306";
    private String dbName = "SchoolLibrary";
    private Connection con;
	private App mainApp;

    public static void main(String[] args) {
        Application.launch(args);
    }

    public LoadsUI(App mainApp) {
        this.mainApp = mainApp;
    }

    private void goBackToMainScene() {
        mainApp.showMainScene();
    }
    @Override
    public void start(Stage stage) {
        try {
            connectDB();
            setupDatabase(); // Add this line to initialize the database and tables
            setupUI(stage);
            getLoadsData();
            stage.setTitle("Loads Management App");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupUI(Stage stage) {
        TableColumn<Loads, Integer> loadIdCol = new TableColumn<>("Load ID");
        loadIdCol.setCellValueFactory(new PropertyValueFactory<>("loadId"));

        TableColumn<Loads, Date> loadDateCol = new TableColumn<>("Load Date");
        loadDateCol.setCellValueFactory(new PropertyValueFactory<>("loadDate"));

        TableColumn<Loads, Integer> userIdCol = new TableColumn<>("User ID");
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));

        TableColumn<Loads, Date> dueDateCol = new TableColumn<>("Due Date");
        dueDateCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        TableColumn<Loads, Date> returnDateCol = new TableColumn<>("Return Date");
        returnDateCol.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        TableColumn<Loads, String> loanStatusCol = new TableColumn<>("Loan Status");
        loanStatusCol.setCellValueFactory(new PropertyValueFactory<>("loanStatus"));

        TableColumn<Loads, Integer> bookBorrowedCol = new TableColumn<>("Book Borrowed");
        bookBorrowedCol.setCellValueFactory(new PropertyValueFactory<>("bookBorrowed"));

        TableColumn<Loads, Integer> staffIdCol = new TableColumn<>("Staff ID");
        staffIdCol.setCellValueFactory(new PropertyValueFactory<>("staffId"));

        loadsTableView = new TableView<>();
        loadsTableView.getColumns().addAll(loadIdCol, loadDateCol, userIdCol, dueDateCol, returnDateCol, loanStatusCol, bookBorrowedCol, staffIdCol);

        Button refreshButton = new Button("Refresh");
        refreshButton.setStyle("-fx-background-color: #8B4513; -fx-text-fill: white;");
        refreshButton.setOnAction((e) -> getLoadsData());

        Button addDataButton = new Button("Add Data");
        addDataButton.setStyle("-fx-background-color: #8B4513; -fx-text-fill: white;");
        addDataButton.setOnAction((e) -> showAddDataDialog());

        Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: #8B4513; -fx-text-fill: white;");
        deleteButton.setOnAction((e) -> deleteSelectedLoad());

        Button deleteAllButton = new Button("Delete All Data");
        deleteAllButton.setStyle("-fx-background-color: #8B4513; -fx-text-fill: white;");
        deleteAllButton.setOnAction((e) -> deleteAllLoadsData());

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #8B4513; -fx-text-fill: white;");
        backButton.setOnAction(event -> goBackToMainScene(stage));

        VBox buttonVBox = new VBox(10, refreshButton, addDataButton, deleteButton, deleteAllButton);
        buttonVBox.setAlignment(Pos.TOP_LEFT);
        buttonVBox.setPadding(new Insets(10));

        HBox bottomButtonsBox = new HBox(10);
        bottomButtonsBox.getChildren().addAll(backButton);
        bottomButtonsBox.setAlignment(Pos.BOTTOM_LEFT);

        BorderPane root = new BorderPane();
        root.setLeft(buttonVBox);
        root.setCenter(loadsTableView);
        root.setBottom(bottomButtonsBox);
        root.setStyle("-fx-background-color: #382D2C;");

        Scene scene = new Scene(root, 750, 400);
        stage.setScene(scene);
    }






    private void goBackToMainScene(Stage stage) {
        App mainApp = new App();
        try {
            mainApp.start(stage);
        } catch (Exception e) {
            handleException(e);
        }
    }
    private void handleException(Exception e) {
        // íãßäß ÊÓÌíá ÇáÎØÃ Ãæ ØÈÇÚÊå Ýí ÇáßæäÓæá
        e.printStackTrace();
        
        // íãßäß ÃíÖðÇ ÚÑÖ ÑÓÇáÉ ááãÓÊÎÏã ÚäÏ æÇÌåÉ ÇáãÓÊÎÏã
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("An error occurred. Please try again.");
        alert.showAndWait();
    }
    private void getLoadsData() {
        try {
            loadsList.clear();
            String SQL = "SELECT * FROM loads";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);

            while (rs.next()) {
                loadsList.add(new Loads(
                        rs.getInt("loadId"),
                        rs.getDate("loadDate"),
                        rs.getInt("userId"),
                        rs.getDate("dueDate"),
                        rs.getDate("returnDate"),
                        rs.getString("loanStatus"),
                        rs.getInt("bookBorrowed"),
                        rs.getInt("staffId")
                ));
            }

            rs.close();
            stmt.close();

            loadsTableView.setItems(loadsList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void connectDB() {
        try {
            dbURL = "jdbc:mysql://" + URL + ":" + port + "/" + dbName + "?verifyServerCertificate=false";
            Properties p = new Properties();
            p.setProperty("user", dbUsername);
            p.setProperty("password", dbPassword);
            p.setProperty("useSSL", "false");
            p.setProperty("autoReconnect", "true");
            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection(dbURL, p);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showAddDataDialog() {
        Stage addDataDialog = new Stage();
        addDataDialog.initModality(Modality.APPLICATION_MODAL);
        addDataDialog.setTitle("Add Load Data");

        // Replace the following lines with the UI elements for entering load data
        Label loadIdLabel = new Label("Load ID:");
        TextField loadIdTextField = new TextField();

        Label loadDateLabel = new Label("Load Date:");
        DatePicker loadDatePicker = new DatePicker();

        // ... add more UI elements as needed

        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-background-color: #8B4513; -fx-text-fill: white;");
        submitButton.setOnAction(event -> {
            // Handle submission of load data
            // Retrieve data from UI elements and process as needed
            addDataDialog.close();
        });

        // Replace the following lines with the actual arrangement of UI elements
        VBox dialogVBox = new VBox(20);
        dialogVBox.getChildren().addAll(loadIdLabel, loadIdTextField, loadDateLabel, loadDatePicker, submitButton);
        dialogVBox.setAlignment(Pos.CENTER);

        Scene dialogScene = new Scene(dialogVBox, 300, 200);
        addDataDialog.setScene(dialogScene);
        addDataDialog.showAndWait();
    }

    private void deleteSelectedLoad() {
        Loads selectedLoad = loadsTableView.getSelectionModel().getSelectedItem();
        if (selectedLoad != null) {
            int loadId = selectedLoad.getLoadId();
            deleteLoadData(loadId);
            getLoadsData();
        } else {
            showAlert("No Selection", "Please select a load to delete.");
        }
    }

    private void deleteLoadData(int loadId) {
        try {
            String SQL = "DELETE FROM loads WHERE loadId = ?";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setInt(1, loadId);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteAllLoadsData() {
        try {
            String SQL = "DELETE FROM loads";
            Statement stmt = con.createStatement();
            stmt.executeUpdate(SQL);
            stmt.close();
            getLoadsData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void setupDatabase() {
        try {
            Statement stmt = con.createStatement();

            // Add the SQL statements for creating tables and inserting sample data here
            String createLoadsTableSQL = "CREATE TABLE IF NOT EXISTS loads (" +
                    "loadId INT PRIMARY KEY," +
                    "loadDate DATE," +
                    "userId INT," +
                    "dueDate DATE," +
                    "returnDate DATE," +
                    "loanStatus VARCHAR(255)," +
                    "bookBorrowed INT," +
                    "staffId INT" +
                    ")";
            stmt.executeUpdate(createLoadsTableSQL);

            // Insert sample data into the loads table (you've already provided this)

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
