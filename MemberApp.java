import static javafx.stage.Modality.NONE;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

public class MemberApp extends Application {
	private ArrayList<Member> dataMember;

	private ObservableList<Member> dataListMember;
	   private String dbURL;
	    private String dbUsername = "root";
	    private String dbPassword = "0987";
	    private String URL = "127.0.0.1";
	    private String port = "3306";
	    private String dbName = "Schoollibrary";
	    private Connection con;
	@Override
	public void start(Stage arg0) throws Exception {
		dataMember = new ArrayList<>();
		Stage stage2= new Stage();

		Stage stageMember = new Stage();
		try {
			Parent root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
		    Scene scene2 = new Scene(root);

			getDataMember();

			// convert data from arraylist to observable arraylist
			dataListMember = FXCollections.observableArrayList(dataMember);
			stage2.setScene(scene2);
			
			// really bad method
			tableViewMember(stageMember);
			stageMember.show();
			stage2.show();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
	}
	}
	@SuppressWarnings("unchecked")
	private void tableViewMember(Stage stage) {

		TableView<Member> myDataTable = new TableView<Member>();

		Scene scene = new Scene(new Group());
		stage.setTitle("Member Table");
		stage.setWidth(550);
		stage.setHeight(500);

		Label label = new Label("Member Table");
		label.setFont(new Font("Arial", 20));
		label.setTextFill(Color.BLUE);

		myDataTable.setEditable(true);
		myDataTable.setMaxHeight(200);
		myDataTable.setMaxWidth(408);

		// name of column for display
		TableColumn<Member, Integer> MemberIDCol = new TableColumn<Member, Integer>("MemberID");
		MemberIDCol.setMinWidth(50);

		// to get the data from a specific column
		MemberIDCol.setCellValueFactory(new PropertyValueFactory<Member, Integer>("MemberID"));

		TableColumn<Member, String> nameCol = new TableColumn<Member, String>("MemberName");
		nameCol.setMinWidth(100);
		nameCol.setCellValueFactory(new PropertyValueFactory<Member, String>("MemberName"));

		nameCol.setCellFactory(TextFieldTableCell.<Member>forTableColumn());

		nameCol.setOnEditCommit((CellEditEvent<Member, String> t) -> {
			((Member) t.getTableView().getItems().get(t.getTablePosition().getRow())).setMemberName(t.getNewValue());
			updateNameMember(t.getRowValue().getMemberID(), t.getNewValue());
		});

		TableColumn<Member, String> TypeCol = new TableColumn<Member, String>("membertype");
		TypeCol.setMinWidth(100);
		TypeCol.setCellValueFactory(new PropertyValueFactory<Member, String>("membertype"));

		TypeCol.setCellFactory(TextFieldTableCell.<Member>forTableColumn());

		TypeCol.setOnEditCommit((CellEditEvent<Member, String> t) -> {
			((Member) t.getTableView().getItems().get(t.getTablePosition().getRow())).setMembertype(t.getNewValue());
			updateType(t.getRowValue().getMemberID(), t.getNewValue());
		});

		

		myDataTable.setItems(dataListMember);

		myDataTable.getColumns().addAll(MemberIDCol, nameCol, TypeCol);

		final TextField addMemberID = new TextField();
		addMemberID.setPromptText("MemberID");
		addMemberID.setMaxWidth(MemberIDCol.getPrefWidth());

		final TextField addName = new TextField();
		addName.setMaxWidth(nameCol.getPrefWidth());
		addName.setPromptText("MemberName");

		final TextField addSubject = new TextField();
		addSubject.setMaxWidth(TypeCol.getPrefWidth());
		addSubject.setPromptText("memberType");

		

		final Button addButton = new Button("Add");
		addButton.setOnAction((ActionEvent e) -> {
			Member newMember = new Member(Integer.valueOf(addMemberID.getText()), addName.getText(), addSubject.getText());

			dataListMember.add(newMember);
			insertData(newMember);
			addMemberID.clear();
			
			addName.clear();
			addSubject.clear();
		});

		final HBox hb = new HBox();

		final Button deleteButton = new Button("Delete");
		deleteButton.setOnAction((ActionEvent e) -> {
			ObservableList<Member> selectedRows = myDataTable.getSelectionModel().getSelectedItems();
			ArrayList<Member> rows = new ArrayList<>(selectedRows);
			rows.forEach(row -> {
				myDataTable.getItems().remove(row);
				deleteRow(row);
				myDataTable.refresh();
			});
		});

		hb.getChildren().addAll(addMemberID, addName, addSubject, addButton, deleteButton);
		hb.setSpacing(3);

		final Button refreshButton = new Button("Refresh");
		refreshButton.setOnAction((ActionEvent e) -> {
			myDataTable.refresh();
		});

		final Button clearButton = new Button("Clear All");
		clearButton.setOnAction((ActionEvent e) -> {
			showDialogMember(stage, NONE, myDataTable);
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
		((Group) scene.getRoot()).getChildren().addAll(vbox);
		stage.setScene(scene);
	}

	private void insertData(Member newMember) {
		try {
			System.out.println("Insert into member (memberID, memberName, membertype) values("
					+ newMember.getMemberID() + ",'" + newMember.getMemberName() + "','" + newMember.getMembertype()
					+ "');");
			connectDB();
			ExecuteStatement("Insert into member (memberID, memberName, membertype) values("
					+ newMember.getMemberID() + ",'" + newMember.getMemberName() + "','" + newMember.getMembertype()
					+ "');");
			con.close();
			System.out.println("Connection closed" + dataMember.size());
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void updateNameMember(int MemberID, String name) {
		try {
			System.out.println("update Member set MemberName = '" + name + "' where MemberID = " + MemberID);
			connectDB();
			ExecuteStatement("update Member set MemberName = '" + name + "' where MemberID = " + MemberID + ";");
			con.close();
			System.out.println("Connection closed");
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void updateType(int MemberID, String Type) {
		try {
			System.out.println("update Member set memberType = '" + Type + "' where MemberID = " + MemberID);
			connectDB();
			ExecuteStatement("update Member set memberType = '" + Type + "' where MemberID = " + MemberID + ";");
			con.close();
			System.out.println("Connection closed");
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	

	private void deleteRow(Member row) {
		try {
			System.out.println("delete from Member where MemberID=" + row.getMemberID() + ";");
			connectDB();
			ExecuteStatement("delete from Member where MemberID=" + row.getMemberID() + ";");
			con.close();
			System.out.println("Connection closed");
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void showDialogMember(Window owner, Modality modality, TableView<Member> table) {
		Stage stage = new Stage();
		stage.initOwner(owner);
		stage.initModality(modality);

		Button yesButton = new Button("Confirm");
		yesButton.setOnAction(e -> {
			for (Member row : dataListMember) {
				deleteRow(row);
				table.refresh();
			}
			table.getItems().removeAll(dataListMember);
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

	private void getDataMember() throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub

		String SQL;

		connectDB();
		System.out.println("Connection established");

		// Modify the SQL query to select data from the Member table
		SQL = "SELECT MemberID,MemberName, memberType FROM Member";

		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		while (rs.next())
			dataMember.add(new Member(Integer.parseInt(rs.getString(1)), rs.getString(2), rs.getString(3)));

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + dataListMember.size());
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
	

}
