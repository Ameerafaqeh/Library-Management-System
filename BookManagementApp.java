import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.*;
import java.util.Properties;

public class BookManagementApp extends Application {
    private ObservableList<Book> bookList = FXCollections.observableArrayList();
    private TableView<Book> bookTableView;

    private String dbURL;
    private String dbUsername = "root";
    private String dbPassword = "0987";
    private String URL = "127.0.0.1";
    private String port = "3306";
    private String dbName = "SchoolLibrary";
    private Connection con;
	private App mainApp;

       public BookManagementApp(App mainApp) {
        this.mainApp = mainApp;
    }
       private void goBackToMainScene() {
           mainApp.showMainScene(); // «” Œœ«„ „—Ã⁄ «·Ê«ÃÂ… «·—∆Ì”Ì… ··⁄Êœ… ≈·ÌÂ«
       }
	public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            connectDB();
            setupDatabase();  // Add this line to initialize the database and tables
            setupUI(stage);
            getBookData();
            stage.setTitle("Book Management App");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setupUI(Stage stage) {
        TableColumn<Book, Integer> bookIdCol = new TableColumn<>("Book ID");
        bookIdCol.setCellValueFactory(new PropertyValueFactory<>("bookId"));

        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Book, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<Book, Integer> categoryIdCol = new TableColumn<>("Category ID");
        categoryIdCol.setCellValueFactory(new PropertyValueFactory<>("categoryId"));

        bookTableView = new TableView<>();
        bookTableView.getColumns().addAll(bookIdCol, titleCol, authorCol, categoryIdCol);

        Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: #8B4513; -fx-text-fill: white;");
        deleteButton.setOnAction((e) -> deleteSelectedBook());

        Button deleteAllButton = new Button("Delete All Data");
        deleteAllButton.setStyle("-fx-background-color: #8B4513; -fx-text-fill: white;");
        deleteAllButton.setOnAction((e) -> deleteAllBookData());

        Button refreshButton = new Button("Refresh");
        refreshButton.setStyle("-fx-background-color: #8B4513; -fx-text-fill: white;");
        refreshButton.setOnAction((e) -> getBookData());

        Button addDataButton = new Button("Add Data");
        addDataButton.setStyle("-fx-background-color: #8B4513; -fx-text-fill: white;");
        addDataButton.setOnAction((e) -> showAddDataDialog());

        Button editButton = new Button("Edit");
        editButton.setStyle("-fx-background-color: #8B4513; -fx-text-fill: white;");
        editButton.setOnAction((e) -> showEditDialog());

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #8B4513; -fx-text-fill: white;");
        backButton.setOnAction(event -> goBackToMainScene(stage));

        VBox buttonVBox = new VBox(10, refreshButton, addDataButton, deleteButton, deleteAllButton, editButton);
        buttonVBox.setAlignment(Pos.TOP_LEFT);
        buttonVBox.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setLeft(buttonVBox);
        root.setCenter(bookTableView);
        root.setBottom(backButton);
        root.setStyle("-fx-background-color: #382D2C;");

        Scene scene = new Scene(root);

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


	private void getBookData() {
        try {
            bookList.clear();
            String SQL = "SELECT * FROM book";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);

            while (rs.next()) {
                bookList.add(new Book(
                        rs.getInt("bookId"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("categoryId")
                ));
            }

            rs.close();
            stmt.close();

            bookTableView.setItems(bookList);

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
        addDataDialog.setTitle("Add Book Data");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 20, 20, 20));

        Label titleLabel = new Label("Title:");
        TextField titleField = new TextField();

        Label authorLabel = new Label("Author:");
        TextField authorField = new TextField();

        Label categoryLabel = new Label("Category ID:");
        TextField categoryIdField = new TextField();

        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-background-color: #8B4513; -fx-text-fill: white;");
        submitButton.setOnAction(event -> {
            String title = titleField.getText();
            String author = authorField.getText();
            String categoryIdStr = categoryIdField.getText();

            try {
                int categoryId = Integer.parseInt(categoryIdStr);
                if (isCategoryExists(categoryId)) {
                    addBookData(title, author, categoryId);
                    getBookData();
                    addDataDialog.close();
                } else {
                    showAlert("Category Not Found", "Please enter a valid Category ID.");
                }
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Please enter a valid Category ID.");
            }
        });

        gridPane.add(titleLabel, 0, 0);
        gridPane.add(titleField, 1, 0);
        gridPane.add(authorLabel, 0, 1);
        gridPane.add(authorField, 1, 1);
        gridPane.add(categoryLabel, 0, 2);
        gridPane.add(categoryIdField, 1, 2);
        gridPane.add(submitButton, 1, 3);

        Scene dialogScene = new Scene(gridPane);
        addDataDialog.setScene(dialogScene);
        addDataDialog.showAndWait();
    }

    private boolean isCategoryExists(int categoryId) {
        try {
            String SQL = "SELECT * FROM book_categories WHERE categoryId = ?";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();
            boolean exists = rs.next();
            rs.close();
            stmt.close();
            return exists;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void addBookData(String title, String author, int categoryId) {
        try {
            String SQL = "INSERT INTO book (title, author, categoryId) VALUES (?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setInt(3, categoryId);
            stmt.executeUpdate();
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

    private void deleteSelectedBook() {
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            int bookId = selectedBook.getBookId();
            deleteBookData(bookId);
            getBookData();
        } else {
            showAlert("No Selection", "Please select a book to delete.");
        }
    }

    private void deleteBookData(int bookId) {
        try {
            String SQL = "DELETE FROM book WHERE bookId = ?";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setInt(1, bookId);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteAllBookData() {
        try {
            String SQL = "DELETE FROM book";
            Statement stmt = con.createStatement();
            stmt.executeUpdate(SQL);
            stmt.close();
            getBookData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showEditDialog() {
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();

        if (selectedBook != null) {
            Stage editDataDialog = new Stage();
            editDataDialog.initModality(Modality.APPLICATION_MODAL);
            editDataDialog.setTitle("Edit Book Data");

            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(20, 20, 20, 20));

            Label titleLabel = new Label("Title:");
            TextField titleField = new TextField(selectedBook.getTitle());

            Label authorLabel = new Label("Author:");
            TextField authorField = new TextField(selectedBook.getAuthor());

            Label categoryLabel = new Label("Category ID:");
            TextField categoryIdField = new TextField(String.valueOf(selectedBook.getCategoryId()));

            Button saveChangesButton = new Button("Save Changes");
            saveChangesButton.setStyle("-fx-background-color: #8B4513; -fx-text-fill: white;");
            saveChangesButton.setOnAction(event -> {
                String title = titleField.getText();
                String author = authorField.getText();
                String categoryIdStr = categoryIdField.getText();

                try {
                    int categoryId = Integer.parseInt(categoryIdStr);
                    if (isCategoryExists(categoryId)) {
                        updateBookData(selectedBook.getBookId(), title, author, categoryId);
                        getBookData();
                        editDataDialog.close();
                    } else {
                        showAlert("Category Not Found", "Please enter a valid Category ID.");
                    }
                } catch (NumberFormatException e) {
                    showAlert("Invalid Input", "Please enter a valid Category ID.");
                }
            });

            gridPane.add(titleLabel, 0, 0);
            gridPane.add(titleField, 1, 0);
            gridPane.add(authorLabel, 0, 1);
            gridPane.add(authorField, 1, 1);
            gridPane.add(categoryLabel, 0, 2);
            gridPane.add(categoryIdField, 1, 2);
            gridPane.add(saveChangesButton, 1, 3);

            Scene dialogScene = new Scene(gridPane);
            editDataDialog.setScene(dialogScene);
            editDataDialog.showAndWait();
        } else {
            showAlert("No Selection", "Please select a book to edit.");
        }
    }

    

    private void updateBookData(int bookId, String title, String author, int categoryId) {
        try {
            String SQL = "UPDATE book SET title=?, author=?, categoryId=? WHERE bookId=?";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setInt(3, categoryId);
            stmt.setInt(4, bookId);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void setupDatabase() {
        try {
            Statement stmt = con.createStatement();

            // Add the SQL statements for creating tables and inserting sample data here
            String createStudentTableSQL = "CREATE TABLE IF NOT EXISTS Student (" +
                    "studentID INT PRIMARY KEY," +
                    "studentName VARCHAR(32)," +
                    "class INT" +
                    ")";
            stmt.executeUpdate(createStudentTableSQL);

   
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
