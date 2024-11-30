import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

public class LogSystemApp extends Application {
    private ObservableList<SystemLog> systemLogs = FXCollections.observableArrayList();
    private TableView<SystemLog> logTableView;
    private String dbURL;
    private String dbUsername = "root";
    private String dbPassword = "0987";
    private String URL = "127.0.0.1";
    private String port = "3306";
    private String dbName = "SchoolLibrary";
    private Connection con;
	private App mainApp;

	public static void main(String[] args) {
        launch(args);
    }

  
  
    public  LogSystemApp(App mainApp) {
        this.mainApp = mainApp;
    }
    private void goBackToMainScene() {
        mainApp.showMainScene();
    }
    @Override
    public void start(Stage stage) {
        try {
            connectDB();
            setupDatabase();
            getSystemLogsData();

            stage.setTitle("System Logs");
            setupUI(stage);
            stage.show();
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void setupUI(Stage stage) {
        TableColumn<SystemLog, Integer> logIdCol = new TableColumn<>("Log ID");
        logIdCol.setCellValueFactory(new PropertyValueFactory<>("logID"));

        TableColumn<SystemLog, String> timestampCol = new TableColumn<>("Timestamp");
        timestampCol.setCellValueFactory(cellData -> {
            Date timestamp = cellData.getValue().getTimestamp();
            String formattedTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(timestamp);
            return new javafx.beans.property.SimpleStringProperty(formattedTimestamp);
        });

        TableColumn<SystemLog, String> eventTypeCol = new TableColumn<>("Event Type");
        eventTypeCol.setCellValueFactory(new PropertyValueFactory<>("eventType"));
        eventTypeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        eventTypeCol.setOnEditCommit(event -> {
            SystemLog log = event.getRowValue();
            log.setEventType(event.getNewValue());
            updateLog(log);
        });

        logTableView = new TableView<>();
        logTableView.setEditable(true);
        logTableView.getColumns().addAll(logIdCol, timestampCol, eventTypeCol);

        logTableView.setItems(systemLogs);

        String buttonStyle = "-fx-background-color: #8B4513; -fx-text-fill: white;";

        Button addButton = new Button("Add Log");
        addButton.setStyle(buttonStyle);
        addButton.setOnAction(e -> showAddLogDialog());

        Button refreshButton = new Button("Refresh");
        refreshButton.setStyle(buttonStyle);
        refreshButton.setOnAction(e -> refreshLogs());

        Button deleteButton = new Button("Delete Selected Log");
        deleteButton.setStyle(buttonStyle);
        deleteButton.setOnAction(e -> deleteSelectedLog());

        Button deleteAllButton = new Button("Delete All Logs");
        deleteAllButton.setStyle(buttonStyle);
        deleteAllButton.setOnAction(e -> deleteAllLogs());

        Button editButton = new Button("Edit Selected Log");
        editButton.setStyle(buttonStyle);
        editButton.setOnAction(e -> editSelectedLog());

        Button backButton = new Button("Back");
        backButton.setStyle(buttonStyle);
        backButton.setOnAction(event -> goBackToMainScene(stage));

        VBox buttonVBox = new VBox(10, refreshButton, addButton, deleteButton, deleteAllButton, editButton);
        buttonVBox.setAlignment(Pos.TOP_LEFT);
        buttonVBox.setPadding(new Insets(10));

        HBox hbox = new HBox();
        hbox.getChildren().addAll(buttonVBox, logTableView);

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(hbox, backButton);
        root.setStyle("-fx-background-color: #382D2C;");

        Scene scene = new Scene(root, 500, 400);
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
        e.printStackTrace();

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("An error occurred. Please try again.");
        alert.showAndWait();
    }

    private void refreshLogs() {
        getSystemLogsData();
    }

    private void showAddLogDialog() {
        Stage addLogDialog = new Stage();
        addLogDialog.initModality(Modality.APPLICATION_MODAL);
        addLogDialog.setTitle("Add Log");

        Label timestampLabel = new Label("Timestamp:");
        DatePicker timestampPicker = new DatePicker();

        Label eventTypeLabel = new Label("Event Type:");
        TextField eventTypeTextField = new TextField();

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            try {
                LocalDate date = timestampPicker.getValue();
                LocalTime time = LocalTime.now();

                LocalDateTime dateTime = LocalDateTime.of(date, time);
                Date timestamp = java.sql.Timestamp.valueOf(dateTime);

                String eventType = eventTypeTextField.getText().trim();

                if (date != null && !eventType.isEmpty()) {
                    addLog(timestamp, eventType);
                    addLogDialog.close();
                } else {
                    showAlert("Invalid Data", "Please fill in all the required fields.");
                }
            } catch (Exception ex) {
                showAlert("Error", "Invalid timestamp format.");
            }
        });

        VBox dialogVBox = new VBox(10);
        dialogVBox.getChildren().addAll(timestampLabel, timestampPicker, eventTypeLabel, eventTypeTextField, saveButton);
        dialogVBox.setPadding(new Insets(10));
        dialogVBox.setSpacing(10);

        Scene dialogScene = new Scene(dialogVBox, 300, 200);
        addLogDialog.setScene(dialogScene);
        addLogDialog.showAndWait();
    }

    private void addLog(Date timestamp, String eventType) {
        try {
            String SQL = "INSERT INTO system_logs (timestamp, eventType) VALUES (?, ?)";
            PreparedStatement stmt = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            stmt.setTimestamp(1, new java.sql.Timestamp(timestamp.getTime()));
            stmt.setString(2, eventType);
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int logID = generatedKeys.getInt(1);
                SystemLog newLog = new SystemLog(logID, timestamp, eventType);
                systemLogs.add(newLog);
            }

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while adding the log.");
        }
    }


    private void editSelectedLog() {
        SystemLog selectedLog = logTableView.getSelectionModel().getSelectedItem();
        if (selectedLog != null) {
            logTableView.edit(systemLogs.indexOf(selectedLog), logTableView.getColumns().get(2));
        } else {
            showAlert("No Log Selected", "Please select a log to edit.");
        }
    }

    private void deleteSelectedLog() {
        SystemLog selectedLog = logTableView.getSelectionModel().getSelectedItem();
        if (selectedLog != null) {
            deleteLog(selectedLog.getLogID());
            refreshLogs();
        } else {
            showAlert("No Log Selected", "Please select a log to delete.");
        }
    }

    private void deleteAllLogs() {
        try {
            Statement stmt = con.createStatement();
            String deleteAllSQL = "DELETE FROM system_logs";
            stmt.executeUpdate(deleteAllSQL);
            stmt.close();
            refreshLogs();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while deleting all logs.");
        }
    }

    private void deleteLog(int logID) {
        try {
            Statement stmt = con.createStatement();
            String deleteSQL = "DELETE FROM system_logs WHERE logID = " + logID;
            stmt.executeUpdate(deleteSQL);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while deleting the log.");
        }
    }

    private void updateLog(SystemLog log) {
        try {
            String updateSQL = "UPDATE system_logs SET eventType = ? WHERE logID = ?";
            PreparedStatement stmt = con.prepareStatement(updateSQL);
            stmt.setString(1, log.getEventType());
            stmt.setInt(2, log.getLogID());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while updating the log.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void getSystemLogsData() {
        try {
            systemLogs.clear();
            String SQL = "SELECT * FROM system_logs";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);

            while (rs.next()) {
                SystemLog log = new SystemLog(
                        rs.getInt("logID"),
                        rs.getTimestamp("timestamp"),
                        rs.getString("eventType")
                );
                systemLogs.add(log);
            }

            rs.close();
            stmt.close();
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

    private void setupDatabase() {
        try {
            Statement stmt = con.createStatement();

            String createTableSQL = "CREATE TABLE IF NOT EXISTS system_logs (" +
                    "logID INT AUTO_INCREMENT PRIMARY KEY," +
                    "timestamp TIMESTAMP," +
                    "eventType VARCHAR(255)" +
                    ")";
            stmt.executeUpdate(createTableSQL);

            String checkDataSQL = "SELECT COUNT(*) AS count FROM system_logs";
            ResultSet result = stmt.executeQuery(checkDataSQL);
            result.next();
            int count = result.getInt("count");
            result.close();

            if (count == 0) {
                String insertDataSQL = "INSERT INTO system_logs (timestamp, eventType) VALUES " +
                        "('2024-01-25 10:30:00', 'INFO')," +
                        "('2024-01-25 11:45:00', 'ERROR')," +
                        "('2024-01-25 12:15:00', 'WARNING')";
                stmt.executeUpdate(insertDataSQL);
            }

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


