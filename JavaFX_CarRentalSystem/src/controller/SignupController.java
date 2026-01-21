package controller;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
// import scenes.*;
public class SignupController implements Initializable{

    @FXML
    private TextField email;

    @FXML
    private TextField firstName;

    @FXML
    private TextField middleName;

    @FXML
    private PasswordField password1;

    @FXML
    private PasswordField password2;

    @FXML
    private Button signup;
    
    @FXML
    private Button close_btn;
    @FXML
    private Button minimize_btn;
        @FXML
    private AnchorPane signup_scene;
   

    @FXML
    private ComboBox<String> userPrivelage;
    private String[] privelage={"Admin","User"};

private Stage stage;
private Scene scene;
private Parent root;
private Connection conn=null;
private PreparedStatement ps=null;
// private ResultSet rs=null;
// private Statement statement=null;






@FXML
void success(ActionEvent event) {

        System.out.println("hello");
       

                 }
                

              
@FXML
void changeSceneToLogin(ActionEvent event) throws Exception{
   FXMLLoader loader =new FXMLLoader(getClass().getResource("/scenes/LoginScene.fxml"));
   root=loader.load();
   stage=(Stage)((Node)event.getSource()).getScene().getWindow();
   scene=new Scene(root);
   stage.setScene(scene);
   stage.show();
   

}

@FXML
public void signup(ActionEvent event){

        
        try {

            conn=DatabaseConnection.connectDatabase();
            String sql = "INSERT INTO users (email,first_name,middle_name,password,privileges) VALUES (?, ?, ?, ?, ?);";
            

            if((userPrivelage.getValue()==null)||firstName.getText().isBlank()||middleName.getText().isBlank()||email.getText().isBlank()||password1.getText().isBlank()||password2.getText().isBlank())
            {
           {
            Alert alert=new Alert(AlertType.ERROR);
                alert.setTitle("Fill all entries");
                alert.setContentText("fill all entries");
                if(alert.showAndWait().get()==ButtonType.OK){
                    System.out.println("enter fields");
                    return;
                   }
                

           }}
           else {
                if(!(password1.getText().equals(password2.getText()))){
                            Alert alert=new Alert(AlertType.INFORMATION);
                            alert.setTitle("Password Doesn't Match");
                            alert.setContentText("You entered a password that doesnot match");
                            if(alert.showAndWait().get()==ButtonType.OK){
                                System.out.println("enter fields");
                                return;
                   }
                                }
                else if((password1.getText().length()<6)){
                                    Alert alert=new Alert(AlertType.INFORMATION);
                                    alert.setTitle("Short Password");
                                    alert.setHeaderText("You entered a password that is weak please enter a minimum of 6 charachter");
                                    // alert.setContentText("You entered a password that is weak please enter a minimum of 6 charachter");
                                    if(alert.showAndWait().get()==ButtonType.OK){
                                        System.out.println("enter fields");
                                        return;
                                       }
                                   }
            else if(!isValidEmail(email.getText())){
                                Alert alert=new Alert(AlertType.INFORMATION);
                                alert.setTitle("Invalid Email");
                                alert.setContentText("Please enter a valid email");
                                if(alert.showAndWait().get()==ButtonType.OK){
                                    System.out.println("enter fields");
                                    return;
                                }}

               else if(isEmailAlreadyExists(email.getText())){
                                Alert alert=new Alert(AlertType.INFORMATION);
                                alert.setTitle("Email is alerady taken");
                                alert.setContentText("Please enter a new email adress");
                                if(alert.showAndWait().get()==ButtonType.OK){
                                    System.out.println("enter fields");
                                    return;
                                }}

                            
                else{

                    System.out.println("success");
                    // statement=conn.createStatement();
                //   rs= statement.executeQuery("Select * from customers");
                    // System.out.println(rs);
                ps = conn.prepareStatement(sql);
                ps.setString(1, email.getText());
                ps.setString(2, firstName.getText());
                ps.setString(3, middleName.getText());
                ps.setString(4, password1.getText());
                ps.setString(5, userPrivelage.getValue());
                System.out.println("success 2");
                int rowsaffeted=ps.executeUpdate();
                System.out.println("success 3");
                if(rowsaffeted==1){
           System.out.println("successfull insert");
           changeSceneToLogin(event);
           Alert alert=new Alert(AlertType.INFORMATION);
            alert.setTitle("Registration Successfull");
            alert.setContentText("You Have Succesfully Registered Login To continue");
             if(alert.showAndWait().get()==ButtonType.OK){
   return;
   }

                }
                else{
                    System.out.println("error");
                }
            }

             
           
        }
    }
    
    catch (Exception e) {
           
        }
}






@Override
public void initialize(URL location, ResourceBundle resources) {
    
userPrivelage.getItems().addAll(privelage);

}

public static boolean isValidEmail(String email) {
       
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile(regex);

       
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

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
            // Handle any SQL exceptions
            System.err.println("Error checking email existence: " + e.getMessage());
            // Consider logging the error for debugging purposes
        } finally {
            // Close the resources in a finally block to ensure they are always closed
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



        ///////////////////////////////////////////////
public void check(ActionEvent e){
    if(userPrivelage.getValue() != null){
   System.out.println(userPrivelage.getValue());
    }
    else System.out.println(userPrivelage.getValue());
}

public void close() {
    System.exit(0);
}

public void minimize() {
    Stage stage = (Stage) signup_scene.getScene().getWindow();
    stage.setIconified(true);
}


}