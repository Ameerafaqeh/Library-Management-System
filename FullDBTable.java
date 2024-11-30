
import static javafx.stage.Modality.NONE;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.converter.IntegerStringConverter;

public class FullDBTable extends Application {
	
    private ArrayList<Student> data;
    private ObservableList<Student> dataList;


	private String dbURL;
	private String dbUsername = "root";
	private String dbPassword = "0987";
	private String URL = "127.0.0.1";
	private String port = "3306";
	private String dbName = "SchoolLibrary";
	private Connection con;

	public static void main(String[] args) {

		Application.launch(args);
	}

	@Override
	 public void start(Stage primaryStage) {
        try {
            // Get the database password from user input
             {
                data = new ArrayList<>();
                getData();
                dataList = FXCollections.observableArrayList(data);
                tableView(primaryStage);
                primaryStage.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

	@SuppressWarnings("unchecked")

	private void tableView(Stage stage) {

		TableView<Student> myDataTable = new TableView<Student>();

		Scene scene = new Scene(new Group());
		stage.setTitle("Student Table");
		stage.setWidth(550);
		stage.setHeight(500);

		Label label = new Label("Student Table");
		label.setFont(new Font("Arial", 20));
		label.setTextFill(Color.BLUE);

		myDataTable.setEditable(true);
		myDataTable.setMaxHeight(200);
		myDataTable.setMaxWidth(408);

		// name of column for display
		TableColumn<Student, Integer> snumCol = new TableColumn<Student, Integer>("studentID");
		snumCol.setMinWidth(50);

		// to get the data from specific column
		snumCol.setCellValueFactory(new PropertyValueFactory<Student, Integer>("studentID"));

		TableColumn<Student, String> nameCol = new TableColumn<Student, String>("studentName");
		nameCol.setMinWidth(100);
		nameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("studentName"));

		nameCol.setCellFactory(TextFieldTableCell.<Student>forTableColumn());

		nameCol.setOnEditCommit((CellEditEvent<Student, String> t) -> {
			((Student) t.getTableView().getItems().get(t.getTablePosition().getRow())).setStudentName(t.getNewValue()); // display
																														// only
			updateName(t.getRowValue().getStudentID(), t.getNewValue());
		});

		TableColumn<Student, Integer> ageCol = new TableColumn<Student, Integer>("Classvalue");
		ageCol.setMinWidth(50);
		ageCol.setCellValueFactory(new PropertyValueFactory<Student, Integer>("Classvalue"));

		ageCol.setCellFactory(TextFieldTableCell.<Student, Integer>forTableColumn(new IntegerStringConverter()));

		ageCol.setOnEditCommit((CellEditEvent<Student, Integer> t) -> {
			((Student) t.getTableView().getItems().get(t.getTablePosition().getRow())).setClass(t.getNewValue());
			updateClass(t.getRowValue().getStudentID(), t.getNewValue());
		});

		myDataTable.setItems(dataList);

		myDataTable.getColumns().addAll(snumCol, nameCol, ageCol);

		final TextField addSnum = new TextField();
		addSnum.setPromptText("studentID");
		addSnum.setMaxWidth(snumCol.getPrefWidth());

		final TextField addName = new TextField();
		addName.setMaxWidth(nameCol.getPrefWidth());
		addName.setPromptText("studentName");

		final TextField addClass = new TextField();
		addClass.setMaxWidth(ageCol.getPrefWidth());
		addClass.setPromptText("Classvalue");

		final Button addButton = new Button("Add");
		addButton.setOnAction((ActionEvent e) -> {
			Student rc;
			rc = new Student(Integer.valueOf(addSnum.getText()), addName.getText(),
					Integer.valueOf(addClass.getText()));

			dataList.add(rc);
			insertData(rc);
			addSnum.clear();
			addName.clear();
			addClass.clear();

		});

		final HBox hb = new HBox();

		final Button deleteButton = new Button("Delete");
		deleteButton.setOnAction((ActionEvent e) -> {
			ObservableList<Student> selectedRows = myDataTable.getSelectionModel().getSelectedItems();
			ArrayList<Student> rows = new ArrayList<>(selectedRows);
			rows.forEach(row -> {
				myDataTable.getItems().remove(row);
				deleteRow(row);
				myDataTable.refresh();
			});
		});

		hb.getChildren().addAll(addSnum, addName, addClass, addButton, deleteButton);
		hb.setSpacing(3);

		final Button refreshButton = new Button("Refresh");
		refreshButton.setOnAction((ActionEvent e) -> {
			myDataTable.refresh();
		});

//		Button ownedNoneButton = new Button("Owned None");
//		ownedNoneButton.setOnAction(c -> );

		final Button clearButton = new Button("Clear All");
		clearButton.setOnAction((ActionEvent e) -> {
			showDialog(stage, NONE, myDataTable);

		});

		final HBox hb2 = new HBox();
		hb2.getChildren().addAll(clearButton, refreshButton);
		hb2.setAlignment(Pos.CENTER_RIGHT);
		hb2.setSpacing(3);

		final VBox vbox = new VBox();
		vbox.setSpacing(5);
		vbox.setPadding(new Insets(10, 0, 0, 10));
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().addAll(label, myDataTable, hb, hb2);
		// vbox.getChildren().addAll(label, myDataTable);
		((Group) scene.getRoot()).getChildren().addAll(vbox);
		stage.setScene(scene);
	}

	private void insertData(Student rc) {

		try {
			System.out.println("Insert into Student (StudentID, StudentName,class) values(" + rc.getStudentID() + ",'"
					+ rc.getStudentName() + "','" + rc.getClassvalue() + "')");

			connectDB();
			ExecuteStatement("Insert into Student (StudentID, StudentName,class) values(" + rc.getStudentID() + ",'"
					+ rc.getStudentName() + "','" + rc.getClassvalue() + "')");
			con.close();
			System.out.println("Connection closed" + data.size());

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void getData() throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub

		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = "select studentID, studentName, class from student";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		while (rs.next())
			data.add(
					new Student(Integer.parseInt(rs.getString(1)), rs.getString(2), Integer.parseInt(rs.getString(3))));

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + data.size());

	}

	private void connectDB() throws ClassNotFoundException, SQLException {

		dbURL = "jdbc:mysql://" + URL + ":" + port + "/" + dbName + "?verifyServerCertificate=false";
		Properties p = new Properties();
		p.setProperty("user", dbUsername);
		p.setProperty("password", dbPassword);
		p.setProperty("useSSL", "false");
		p.setProperty("autoReconnect", "true");
		Class.forName("com.mysql.jdbc.Driver");

		con = DriverManager.getConnection(dbURL, p);

	}

	public void ExecuteStatement(String SQL) throws SQLException {

		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(SQL);
			stmt.close();

		} catch (SQLException s) {
			s.printStackTrace();
			System.out.println("SQL statement is not executed!");

		}

	}

	public void updateName(int snum, String name) {

		try {
			System.out.println("update  Student set sname = '" + name + "' where snum = " + snum);
			connectDB();
			ExecuteStatement("update  Student set sname = '" + name + "' where snum = " + snum + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateClass(int snum, int Classvalue) {

		try {
			System.out.println("update  Student set class = " + Classvalue + " where studentID = " + snum);
			connectDB();
			ExecuteStatement("update  Student set class = " + Classvalue + " where studentID = " + snum + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void deleteRow(Student row) {
		// TODO Auto-generated method stub

		try {
			System.out.println("delete from  Student where studentID=" + row.getStudentID() + ";");
			connectDB();
			ExecuteStatement("delete from  Student where studentID=" + row.getStudentID() + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void showDialog(Window owner, Modality modality, TableView<Student> table) {
		// Create a Stage with specified owner and modality
		Stage stage = new Stage();
		stage.initOwner(owner);
		stage.initModality(modality);
		// Label modalityLabel = new Label(modality.toString());

		Button yesButton = new Button("Confirm");
		yesButton.setOnAction(e -> {
			for (Student row : dataList) {
				deleteRow(row);
				table.refresh();
			}
			table.getItems().removeAll(dataList);
			stage.close();

		});

		Button noButton = new Button("Cancel");
		noButton.setOnAction(e -> stage.close());

		HBox root = new HBox();
		root.setPadding(new Insets(10, 10, 10, 10));
		root.setAlignment(Pos.CENTER);
		root.setSpacing(10);

		root.getChildren().addAll(yesButton, noButton);
		Scene scene = new Scene(root, 200, 100);
		stage.setScene(scene);
		stage.setTitle("Confirm Delete?");
		stage.show();
	}

	
}
