import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Properties;

public class BookCategoryApp extends Application {
    private ObservableList<BookCategory> bookCategories = FXCollections.observableArrayList();
    private TableView<BookCategory> categoryTableView;

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
    public BookCategoryApp(App mainApp) {
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
            getCategoriesData();

            stage.setTitle("Book Categories");
            setupUI(stage);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupUI(Stage stage) {
        TableColumn<BookCategory, Integer> categoryIdCol = new TableColumn<>("Category ID");
        categoryIdCol.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
        categoryIdCol.setEditable(false);

        TableColumn<BookCategory, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setOnEditCommit(event -> {
            BookCategory category = event.getRowValue();
            category.setName(event.getNewValue());
            updateCategory(category);
        });

        TableColumn<BookCategory, String> descriptionCol = new TableColumn<>("Description");
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("categoryDescription"));
        descriptionCol.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionCol.setOnEditCommit(event -> {
            BookCategory category = event.getRowValue();
            category.setCategoryDescription(event.getNewValue());
            updateCategory(category);
        });

        categoryTableView = new TableView<>();
        categoryTableView.setEditable(true); // Enable editing
        categoryTableView.getColumns().addAll(categoryIdCol, nameCol, descriptionCol);
        categoryTableView.setItems(bookCategories);
        
        // Set the background color of the TableView to brown
        categoryTableView.setStyle("-fx-background-color: #D2B48C;");

        String buttonStyle = "-fx-background-color: #8B4513; -fx-text-fill: white; -fx-font-size: 14px;";

        Button addButton = new Button("Add Category");
        addButton.setStyle(buttonStyle);
        addButton.setOnAction(e -> showAddCategoryDialog());

        Button deleteButton = new Button("Delete Selected Category");
        deleteButton.setStyle(buttonStyle);
        deleteButton.setOnAction(e -> deleteSelectedCategory());

        Button refreshButton = new Button("Refresh");
        refreshButton.setStyle(buttonStyle);
        refreshButton.setOnAction(e -> refreshCategories());

        Button deleteAllButton = new Button("Delete All Categories");
        deleteAllButton.setStyle(buttonStyle);
        deleteAllButton.setOnAction(e -> showDeleteAllConfirmation());

        Button backButton = new Button("Back");
        backButton.setStyle(buttonStyle);
        backButton.setOnAction(event -> goBackToMainScene(stage));

        VBox buttonVBox = new VBox(10);
        buttonVBox.setPadding(new Insets(10));
        buttonVBox.getChildren().addAll(addButton, deleteButton, refreshButton, deleteAllButton);

        BorderPane root = new BorderPane();
        root.setLeft(buttonVBox);
        root.setCenter(categoryTableView); // Set the TableView in the center
        root.setBottom(backButton); // Set the "Back" button at the bottom
        root.setStyle("-fx-background-color: #382D2C;");

        Scene scene = new Scene(root, 700, 400);
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
        // Ì„ﬂ‰ﬂ  ”ÃÌ· «·Œÿ√ √Ê ÿ»«⁄ Â ›Ì «·ﬂÊ‰”Ê·
        e.printStackTrace();
        
        // Ì„ﬂ‰ﬂ √Ì÷« ⁄—÷ —”«·… ··„” Œœ„ ⁄‰œ Ê«ÃÂ… «·„” Œœ„
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("An error occurred. Please try again.");
        alert.showAndWait();
    }
	private void showDeleteAllConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete All Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete all categories?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteAllCategories();
        }
    }
    private void deleteAllCategories() {
        try {
            Statement stmt = con.createStatement();
            String deleteAllSQL = "DELETE FROM book_categories";
            stmt.executeUpdate(deleteAllSQL);
            stmt.close();
            refreshCategories();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while deleting all categories.");
        }
    }

    private void showAddCategoryDialog() {
        Stage addCategoryDialog = new Stage();
        addCategoryDialog.initModality(Modality.APPLICATION_MODAL);
        addCategoryDialog.setTitle("Add Category");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 20, 20, 20));

        Label nameLabel = new Label("Name:");
        TextField nameTextField = new TextField();

        Label descriptionLabel = new Label("Description:");
        TextField descriptionTextField = new TextField();

        Button saveButton = new Button("Save");
        saveButton.setStyle("-fx-background-color: #8B4513; -fx-text-fill: white;");
        saveButton.setOnAction(e -> {
            String name = nameTextField.getText().trim();
            String description = descriptionTextField.getText().trim();

            if (!name.isEmpty() && !description.isEmpty()) {
                addCategory(name, description);
                addCategoryDialog.close();
            } else {
                showAlert("Invalid Data", "Please fill in all the required fields.");
            }
        });

        gridPane.add(nameLabel, 0, 0);
        gridPane.add(nameTextField, 1, 0);
        gridPane.add(descriptionLabel, 0, 1);
        gridPane.add(descriptionTextField, 1, 1);
        gridPane.add(saveButton, 1, 2);

        Scene dialogScene = new Scene(gridPane, 300, 200);
        addCategoryDialog.setScene(dialogScene);
        addCategoryDialog.showAndWait();
    }


    private void addCategory(String name, String description) {
        try {
            String SQL = "INSERT INTO book_categories (categoryId, Name, categoryDescription) VALUES (null, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.executeUpdate();
            stmt.close();

            // Refresh the categories data in the TableView
            refreshCategories();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while adding the category.");
        }
    }

    private void deleteSelectedCategory() {
        BookCategory selectedCategory = categoryTableView.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            deleteCategory(selectedCategory.getCategoryId());
            refreshCategories();
        } else {
            showAlert("No Category Selected", "Please select a category to delete.");
        }
    }

    private void deleteCategory(int categoryId) {
        try {
            Statement stmt = con.createStatement();
            String deleteSQL = "DELETE FROM book_categories WHERE categoryId = " + categoryId;
            stmt.executeUpdate(deleteSQL);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while deleting the category.");
        }
    }

    private void refreshCategories() {
        try {
            // Clear existing data in the ObservableList
            bookCategories.clear();

            // Fetch updated data from the database
            String SQL = "SELECT * FROM book_categories";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);

            while (rs.next()) {
                BookCategory category = new BookCategory(
                        rs.getInt("categoryId"),
                        rs.getString("Name"),
                        rs.getString("categoryDescription"),
                        new ArrayList<>()
                );
                bookCategories.add(category);
            }

            rs.close();
            stmt.close();

            // Update the TableView with the new data
            categoryTableView.setItems(bookCategories);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    

    private void updateCategory(BookCategory category) {
        try {
            String SQL = "UPDATE book_categories SET Name = ?, categoryDescription = ? WHERE categoryId = ?";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getCategoryDescription());
            stmt.setInt(3, category.getCategoryId());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while updating the category.");
        }
    }

    private void getCategoriesData() {
        try {
            bookCategories.clear();
            String SQL = "SELECT * FROM book_categories";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);

            while (rs.next()) {
                BookCategory category = new BookCategory(rs.getInt("categoryId"), rs.getString("Name"),
                        rs.getString("categoryDescription"), new ArrayList<>());
                bookCategories.add(category);
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

            // Create the book_categories table if not exists
            String createTableSQL = "CREATE TABLE IF NOT EXISTS book_categories (" + "categoryId INT PRIMARY KEY,"
                    + "Name VARCHAR(255)," + "categoryDescription VARCHAR(255)" + ")";
            stmt.executeUpdate(createTableSQL);

            // Insert sample data if the table is empty
            String checkDataSQL = "SELECT COUNT(*) AS count FROM book_categories";
            ResultSet result = stmt.executeQuery(checkDataSQL);
            result.next();
            int count = result.getInt("count");
            result.close();

            if (count == 0) {
                String insertDataSQL = "INSERT INTO book_categories (categoryId, Name, categoryDescription) VALUES ";
                ;
                stmt.executeUpdate(insertDataSQL);
            }

            stmt.close();
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
}
