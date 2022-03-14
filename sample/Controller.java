package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Controller {
    @FXML private TextField login;
    @FXML private PasswordField passwordField;
    @FXML private Button btnenter;
    @FXML private Button btnregistration;
    public static int userID;
    @FXML
    private void initialize(){
        FXMLLoader loader = new FXMLLoader();
        btnregistration.setOnAction(event -> {
            btnregistration.getScene().getWindow().hide();
            loader.setLocation(getClass().getResource("registration.fxml"));

            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e){
                e.printStackTrace();
            }
            Stage stage = new Stage();
            assert root != null;
            stage.setScene(new Scene(root));
            stage.show();
        });
        btnenter.setOnAction(event -> {
            try{
                String username = login.getText();
                String password = passwordField.getText();
                Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/yarosh_schema",
                        "root", "root")){
                    Statement statement = conn.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
                    while(resultSet.next()){
                        if (resultSet.getString("login").equals(username) &&
                                resultSet.getString("password").equals(password)){
                            userID = resultSet.getInt("id");
                            btnenter.getScene().getWindow().hide();
                            loader.setLocation(getClass().getResource("listofmedicines.fxml"));
                            Parent root = null;
                            try {
                                root = loader.load();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Stage stage = new Stage();
                            assert root != null;
                            stage.setScene(new Scene(root));
                            stage.show();
                            break;
                        }
                    }
                }
            }
            catch(Exception ex){
                System.out.println("Ошибка доступа к БД");
            }
        });
    }
}
