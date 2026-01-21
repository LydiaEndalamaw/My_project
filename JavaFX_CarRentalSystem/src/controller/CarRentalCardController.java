package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import model.CarRentalCard;


public class CarRentalCardController {
     @FXML
    private AnchorPane add_btn;

    @FXML
    private ImageView car_image;

    @FXML
    private Label car_make;

    @FXML
    private Label car_model;

    @FXML
    private Label car_price;
    @FXML
    private Button car_btn;
    
    @FXML
    private Label car_id;
  @FXML
    public void add_rental(ActionEvent event){
        System.out.println("hallo");
    }
public void setData(CarRentalCard carRental){
Image image=new Image(getClass().getResourceAsStream(carRental.getCar_image()));
car_image.setImage(image);
car_id.setText(carRental.getCar_id());
car_make.setText(carRental.getCar_make());
car_model.setText(carRental.getCar_model());
car_price.setText(carRental.getCar_price());


}
private MainController mainController;

public void setMainController(MainController mainController) {
    this.mainController = mainController;
}
@FXML
public void add_car_rental(ActionEvent event){


mainController.set_car_rental_fields(car_id.getText(),car_make.getText(),car_price.getText());


}




}
