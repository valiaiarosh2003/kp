package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.*;

public class Listofmedicines {
    @FXML private TableView<Medicine> medicineTable;
    @FXML private TableColumn<Medicine, String> nameCol;
    @FXML private TableColumn<Medicine, String> lifeCol;
    @FXML private TableColumn<Medicine, String> countCol;
    @FXML private TableColumn<Medicine, String> descCol;
    @FXML private Button addbtn;
    @FXML private Button delbtn;
    @FXML private TextField medName;
    @FXML private TextField medLife;
    @FXML private TextField medCount;
    @FXML private TextField medDescription;
    private final ObservableList<Medicine> medicineData = FXCollections.observableArrayList();

    @FXML
    private void initialize(){
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        lifeCol.setCellValueFactory(new PropertyValueFactory<>("life"));
        countCol.setCellValueFactory(new PropertyValueFactory<>("count"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("desc"));
        tablerefresh();
        addbtn.setOnAction(event -> {
            if (!(medName.getText().isEmpty() || medLife.getText().isEmpty()
                    || medCount.getText().isEmpty() || medDescription.getText().isEmpty())) {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
                    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/yarosh_schema",
                            "root", "root")){
                        PreparedStatement statement = conn.prepareStatement
                                ("INSERT medicines(medicineName, medicineLife, medicineCount, medicineDescription, userId) VALUES(?, ?, ?, ?, ?)");
                        ResultSet resultSet = statement.executeQuery("SELECT * FROM medicines");
                        while(resultSet.next()){
                            if (resultSet.getString("medicineName").equals(medName.getText()) &&
                                    resultSet.getInt("userId") == Controller.userID) {
                                statement = conn.prepareStatement
                                        ("UPDATE medicines SET medicineName = ?, medicineLife = ?, " +
                                                "medicineCount = ?, medicineDescription = ? WHERE userId = ?");
                                break;
                            }
                        }
                        statement.setString(1, medName.getText());
                        statement.setString(2, medLife.getText());
                        statement.setString(3, medCount.getText());
                        statement.setString(4, medDescription.getText());
                        statement.setInt(5, Controller.userID);
                        statement.executeUpdate();
                    }
                }
                 catch (Exception e) {
                    System.out.println("Ошибка в БД");
                }
            }
            tablerefresh();
        });
        delbtn.setOnAction(event -> {
            if (!(medName.getText().isEmpty())){
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
                    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/yarosh_schema",
                            "root", "root")){
                        PreparedStatement statement = conn.prepareStatement
                                ("DELETE FROM medicines WHERE userId = ? and medicineName = ?");
                        statement.setInt(1, Controller.userID);
                        statement.setString(2, medName.getText());
                        statement.executeUpdate();
                    }
                } catch (Exception e){
                    System.out.println("Ошибка в БД");
                }
            }
            tablerefresh();
        });
    }

    private void tablerefresh(){
        medicineData.clear();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/yarosh_schema",
                    "root", "root")){
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM medicines");
                while(resultSet.next()){
                    if(resultSet.getInt("userId") == Controller.userID){
                        medicineData.add(new Medicine(resultSet.getString("medicineName"),
                                resultSet.getString("medicineLife"),
                                resultSet.getString("medicineCount"),
                                resultSet.getString("medicineDescription")));
                    }
                }
                if (!medicineData.isEmpty()){
                    medicineTable.setItems(medicineData);
                }
            }
        }catch (Exception e){
            System.out.println("Ошибка в БД");
        }
    }
}
