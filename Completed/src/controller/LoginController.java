package controller;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginController {
@FXML
private TextField email;
@FXML
private PasswordField password;
@FXML
 private Button close_btn;
@FXML
private AnchorPane login_scene;
@FXML
private Button minimize_btn;
private Stage stage;
private Scene scene;
private Parent root;
private Connection conn=null;
private String user_email;
// private PreparedStatement ps=null;
// private ResultSet resultSet = null;

 @FXML
void changeSceneToSignup(ActionEvent event) throws Exception{
   FXMLLoader loader =new FXMLLoader(getClass().getResource("/scenes/SignupScene.fxml"));
 root = loader.load();
   stage=(Stage)((Node)event.getSource()).getScene().getWindow();
   scene=new Scene(root);
   stage.setScene(scene);
   stage.show();
   

}
void changeSceneToMainScene(ActionEvent event) throws IOException{
FXMLLoader loader =new FXMLLoader(getClass().getResource("/scenes/MainScene.fxml"));
root = loader.load();
  stage=(Stage)((Node)event.getSource()).getScene().getWindow();
  scene=new Scene(root);
  stage.setScene(scene);
  stage.centerOnScreen();
  stage.show();
}
private MainController mainController;
public void setMainController(MainController mainController) {
    this.mainController = mainController;
}
@FXML
void login(ActionEvent event) throws IOException{
  if(email.getText().isBlank()||password.getText().isBlank()){
    Alert alert=new Alert(AlertType.ERROR);
                alert.setTitle("Fill all entries");
                // alert.setContentText("fill all entries");
                alert.setHeaderText("fill all entries");
                if(alert.showAndWait().get()==ButtonType.OK){
                    System.out.println("enter fields");
                    return;
                   }
                 }
  else if(!isEmailAlreadyExists(email.getText())){
                  Alert alert=new Alert(AlertType.INFORMATION);
                  alert.setTitle("User not found");
                  alert.setHeaderText("Please provide a valid email adress or register");
                  // alert.setContentText("Please provide a valid email adress or register");
                  if(alert.showAndWait().get()==ButtonType.OK){
                      System.out.println("enter fields");
                      return;
                    }
              }

  else if(login(email.getText(), password.getText())) {
    // set_email(email);
    // mainController.fetch_user_name(user_email);
    user_email=email.getText();
    Alert alert=new Alert(AlertType.INFORMATION);
    alert.setTitle("Login Successfull");
    // alert.setContentText("you have successfuly loged in");
    alert.setHeaderText("you have successfuly loged in");
    //   if(alert.showAndWait().get()==ButtonType.OK){
    
        alert.showAndWait();
        System.out.println("enter fields");
        changeSceneToMainScene(event);
        return;
    //   }
  }
else{
  Alert alert=new Alert(AlertType.INFORMATION);
  alert.setTitle("INCORRECT PASSWORD");
  // alert.setContentText("you have successfuly loged in");
  alert.setHeaderText("YOU HAVE ENTERED AN INCORRECT PASSWORD");
    if(alert.showAndWait().get()==ButtonType.OK){
      System.out.println("enter fields");
      return;
    }

}
}
@FXML
 public static boolean login( String email, String password) {
        
  PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
           String sql = "SELECT password FROM users WHERE email = ?";

            Connection connection=DatabaseConnection.connectDatabase();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,email);
            resultSet = preparedStatement.executeQuery();

            // Check if a user with the given email exists
            if (resultSet.next()) {

                // Retrieve the stored password hash from the database
                String storedPassword = resultSet.getString("password");

               
                if (storedPassword != null && storedPassword.equals(password)) {
                    // Passwords match, login successful
                    
                    return true;
                   
                } else {
                    // Passwords do not match
                    return false;
                }
            } 
            else {
                // No user found with the given email
                return false;
            }

        } catch (SQLException e) {
            // Handle any SQL exceptions
            System.err.println("Error during login attempt: " + e.getMessage());
            return false; // Login failed due to an error
        } finally {
            // Close resources
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    System.err.println("Error closing ResultSet: " + e.getMessage());
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    System.err.println("Error closing PreparedStatement: " + e.getMessage());
                }
            }
           
        }
    }
////////////////////

// void set_email(String mail){
// user_email=mail;
// mainController.fetch_user_name(user_email);
// }


    ///////////////////
    public boolean isEmailAlreadyExists(String email) {
      PreparedStatement preparedStatement = null;
      ResultSet resultSet = null;

      try {
         conn=DatabaseConnection.connectDatabase();
          String sql = "SELECT COUNT(*) FROM users WHERE email = ?";

         
          preparedStatement = conn.prepareStatement(sql);

         
          preparedStatement.setString(1, email);

       
          resultSet = preparedStatement.executeQuery();
        
          if (resultSet.next()) {
             
              int count = resultSet.getInt(1);

              return count > 0;
          }

                      } 
      catch (SQLException e) {
          
          System.err.println("Error checking email existence: " + e.getMessage());
          
      } finally {
          
          if (resultSet != null) {
              try {
                  resultSet.close();
              } catch (SQLException e) {
                  System.err.println("Error closing ResultSet: " + e.getMessage());
              }
          }
          if (preparedStatement != null) {
              try {
                  preparedStatement.close();
              } catch (SQLException e) {
                  System.err.println("Error closing PreparedStatement: " + e.getMessage());
              }
          }
        
      }
      return false;
      }


      public void close() {
        System.exit(0);
    }
    
    public void minimize() {
        Stage stage = (Stage) login_scene.getScene().getWindow();
        stage.setIconified(true);
    }
public void change_to_mainscene(){

}
// public void set_system_user(){
//     mainController.fetch_user_name(user_email);
// }


}