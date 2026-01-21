package controller;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.event.ActionEvent;
import model.Car;

public class CardController {

 

    @FXML
    private Label carid;

    @FXML
    private ImageView carimage;

    @FXML
    private Label carmake;

    @FXML
    private Label carmodel;

    @FXML
    private Label carprice;

    @FXML
    private Button edit_btn;

public void setData(Car car){
Image image=new Image(getClass().getResourceAsStream(car.getCarimage()));
carimage.setImage(image);
carid.setText(car.getCarid());
carmake.setText(car.getCarmake());
carmodel.setText(car.getCarmodel());
carprice.setText(car.getCarprice());


}
//////////////////////////////////////
private MainController mainController;

public void setMainController(MainController mainController) {
    this.mainController = mainController;
}


@FXML
private void set_fields(ActionEvent event) throws IOException {
    Connection connection=DatabaseConnection.connectDatabase();
    Statement statement = null;
     ResultSet resultSet = null;
     String image_path = null;
     String car_id = null;
     String car_model = null;
     String car_brand = null;
     Double car_price = 0.0;
     int car_category = 0;
     String car_status = null;
 System.out.println("the fal");
    try {
        System.out.println("the fal 2");
                    statement=connection.createStatement();
                            resultSet=statement.executeQuery("SELECT car_id,car_image,car_brand,car_model,car_price,category_id,car_status FROM cars WHERE car_id= '"+carid.getText()+"'");
                                                        if(resultSet.next()){
                                                            car_category=resultSet.getInt("category_id");
                                                            car_status=resultSet.getString("car_status");
                                                            car_id=resultSet.getString("car_id");
                                                            car_price=resultSet.getDouble("car_price");
                                                            car_model=resultSet.getString("car_model");
                                                            car_brand=resultSet.getString("car_brand");
                                                       image_path =resultSet.getString("car_image");
                                                        }
                                                                       
                                                 } catch (SQLException e) {
                    
                    e.printStackTrace();
                    System.err.println("error");
                }

System.out.println("***************************************");
FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/scenes/MainScene.fxml"));
                        fxmlLoader.load();
           


mainController.setData(car_id,image_path,car_brand,car_model,String.valueOf(car_price),car_category);




}









}