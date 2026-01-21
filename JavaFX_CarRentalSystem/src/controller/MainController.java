package controller;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Car;
import model.CarRentalCard;


public class MainController implements Initializable{

 
    private List<Car> ls;
    private List<CarRentalCard> cl;
    private int column;
    private int row;
    private String[] status={"Available","Rented","Maintenance"};
    private String[] category={"Mini van","couple","suv","sedan","wagon","truck"};
    private String[] gender={"male","female"};
    private String[] cust_gender={"male","female"};
    private String id=null;
    private String imagePath=null;
    private String brand=null;
    private String model=null;
    private String price=null;
    private int category_number;
    /*********************cars button*************************/
//card
   @FXML
    private AnchorPane box;

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

    /***************************************8 */
    @FXML
    private ComboBox<String> car_category;
    @FXML
    private TextField car_brand;
    @FXML
    private Button car_clear;
    @FXML
    private Button rent_btn;

    @FXML
    private Button car_delete;

    @FXML
    private TextField car_id;

    @FXML
    private ImageView car_image;

    @FXML
    private Button car_import_image;

    @FXML
    private Button car_insert;

    @FXML
    private TextField car_model;

    @FXML
    private TextField car_price;

    @FXML
    private TextField car_search;

    @FXML
    private ComboBox<String> car_status;

    @FXML
    private Button car_update;
    /****************************** */
    @FXML
    private Button carrental_btn;

    @FXML
    private HBox cars;

    @FXML
    private Button cars_btn;

    @FXML
    private AnchorPane cars_panel;

    @FXML
    private HBox customers;

    @FXML
    private Button customers_btn;

    @FXML
    private AnchorPane cutomers_panel;

    @FXML
    private GridPane gridScrollable;

    @FXML
    private HBox home;

    @FXML
    private Button home_btn;

    @FXML
    private AnchorPane home_panel;

    @FXML
    private Button logout_btn;

    @FXML
    private AnchorPane rental_panel;

    @FXML
    private HBox rentals;

    @FXML
    private VBox scrollbody;
    @FXML
    private Button minimize_btn;
    @FXML
    private Button close_btn;
    @FXML
    private Button report_btn;

/***************rental btn and  */
@FXML
    private DatePicker date_rented;

    @FXML
    private DatePicker date_returned;

    @FXML
    private TextField first_name;
    @FXML
    private TextField rental_email;

    @FXML
    private TextField last_name;
    @FXML
    private TextField rental_brand;
    
    @FXML
    private ComboBox<String> rental_gender;
    @FXML
    private TextField rentalCar_id;
    @FXML
    private Label rental_price;

    /*********** */
    @FXML
    private Label system_user;
    @FXML
    private Label dashboard_car;
    @FXML
    private Label dashboard_income;
    @FXML
    private Label dashboard_customers;

  
   
   /**************customer panel */
   @FXML
   private TextField cust_first_name;

   @FXML
   private TextField cust_id;

   @FXML
   private TextField cust_last_name;

   @FXML
   private Button customer_delete_btn;

   @FXML
   private Button customer_update_btn;

   @FXML
   private ComboBox<String> customer_gender;
   @FXML
   private TableView<CustomerData> customers_tableView;
   @FXML
   private TableColumn<CustomerData,String> customer_col_carid;

   @FXML
   private TableColumn<CustomerData,String> customer_col_customerid;

   @FXML
   private TableColumn<CustomerData,Date> customer_col_daterented;

   @FXML
   private TableColumn<CustomerData,Date> customer_col_datereturned;

   @FXML
   private TableColumn<CustomerData,String> customer_col_firstname;

   @FXML
   private TableColumn<CustomerData,String> customer_col_gender;

   @FXML
   private TableColumn<CustomerData,String> customer_col_lastname;



   /******* */

    // private String currentImagePath;
    private String imageDirectory;
    private String targetPath;
   private String fileName;
   private String image_db;
/*********************************************************************************8 */
      @Override
      public void initialize(URL location, ResourceBundle resources) {
    
        setDashboard_fields();
        display_all_image(addedCars());
        displayAvailableCars();
        car_status.getItems().addAll(status);
        car_category.getItems().addAll(category);
        rental_gender.getItems().addAll(gender);  
        customer_gender.getItems().addAll(cust_gender);  
              
        // Initialize the customers table
        rentCarShowListData();
            }
          



/************************************************************/
    
private List<Car> addedCars(){

List<Car> list=new ArrayList<>();
/*************************************************** */
Connection connection = null;
    Statement stmt = null;
    ResultSet rs = null;
    
    try {
        connection = DatabaseConnection.connectDatabase();
        stmt = connection.createStatement();
        
        // SQL query to get all cars
        String sql = "SELECT car_id, car_image, car_brand, car_model, car_price FROM cars";
        
        // Execute query
        rs = stmt.executeQuery(sql);
        
        // Process results
        while (rs.next()) {
          // System.out.println(rs.getString("car_image"));
            Car car = new Car();
            car.setCarid(rs.getString("car_id"));
            car.setCarimage(rs.getString("car_image"));
            car.setCarmake(rs.getString("car_brand"));
            car.setCarmodel(rs.getString("car_model"));
            car.setCarprice(String.valueOf(rs.getDouble("car_price")));
            list.add(car);

        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        // Close resources
    }
    return list;
}
/**************************************************** */
private List<CarRentalCard> getAvailableCars() {
  List<CarRentalCard> availableCars = new ArrayList<>();
  Connection connection = null;
  PreparedStatement preparedStatement = null;
  ResultSet resultSet = null;

  try {
      connection = DatabaseConnection.connectDatabase();
      String query = "SELECT c.car_id, c.car_image, c.car_brand, c.car_model, c.car_price, c.category_id, " +
                   "cat.category_name " +
                   "FROM cars c " +
                   "JOIN car_categories cat ON c.category_id = cat.category_id " +
                   "WHERE c.car_status = 'Available'";

      preparedStatement = connection.prepareStatement(query);
      resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
          CarRentalCard car = new CarRentalCard();
          car.setCar_id(resultSet.getString("car_id"));
          car.setCar_image(resultSet.getString("car_image"));
          car.setCar_make(resultSet.getString("car_brand"));
          car.setCar_model(resultSet.getString("car_model"));
          car.setCar_price(String.valueOf(resultSet.getDouble("car_price")));
          availableCars.add(car);
      }

  } catch (SQLException e) {
      Alert errorAlert = new Alert(AlertType.ERROR);
      errorAlert.setTitle("Error");
      errorAlert.setContentText("Error fetching available cars: " + e.getMessage());
      errorAlert.showAndWait();
      e.printStackTrace();
  } finally {
      try {
          if (resultSet != null) resultSet.close();
          if (preparedStatement != null) preparedStatement.close();
          if (connection != null) connection.close();
      } catch (SQLException e) {
          e.printStackTrace();
      }
  }
  return availableCars;
}
/********************************************** */
void displayAvailableCars() {
  List<CarRentalCard> availableCars = getAvailableCars();
  gridScrollable.getChildren().clear();
  column = 0;
  row = 0;
  
  try {
      for (CarRentalCard car : availableCars) {
          if (column == 2) {
              column = 0;
              row++;
          }
          FXMLLoader fxmlLoader = new FXMLLoader();
          fxmlLoader.setLocation(getClass().getResource("/scenes/CarRental.fxml"));
          
          AnchorPane pane = fxmlLoader.load();
          CarRentalCardController carRentalCardController = fxmlLoader.getController();
          carRentalCardController.setData(car);
          carRentalCardController.setMainController(this);
          gridScrollable.add(pane, column, row);
          column++;
      }
  } catch (Exception e) {
      e.printStackTrace();
  }
}


/************** */
void display_all_image(List<Car> point){
  ls=new ArrayList<>(point);
        try {
    for(int p=0;p<ls.size();p++){
        FXMLLoader fxmlLoader=new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/scenes/Card.fxml"));
       
        AnchorPane box= fxmlLoader.load();
         CardController cardController=fxmlLoader.getController();
         cardController.setMainController(this);
        cardController.setData(ls.get(p));
       scrollbody.getChildren().add(box);
                    }
        
                } 
                catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
}


/**************************************************** */
void display_searched_image(List<Car> point){
  ls=point;
        try {
    for(int p=0;p<ls.size();p++){
        FXMLLoader fxmlLoader=new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/scenes/Card.fxml"));
       
        AnchorPane box= fxmlLoader.load();
         CardController cardController=fxmlLoader.getController();
         cardController.setData(ls.get(p));
         cardController.setMainController(this);
       scrollbody.getChildren().add(box);
                    }
        
                } 
                catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }



}

/********************************************************************* */


/**************************************************** */
 @FXML
public void switchPanels(ActionEvent event){
 

    if (event.getSource() == home_btn) {
        home_panel.setVisible(true);
        cars_panel.setVisible(false);
        rental_panel.setVisible(false);
        cutomers_panel.setVisible(false);
        home.getStyleClass().add("activebtn");
        cars.getStyleClass().remove("activebtn");
        rentals.getStyleClass().remove("activebtn");
        customers.getStyleClass().remove("activebtn");
       
        
      
        
        
    } else if (event.getSource() == cars_btn) {
      home_panel.setVisible(false);
      cars_panel.setVisible(true);
      rental_panel.setVisible(false);
      cutomers_panel.setVisible(false);
      // cars.setStyle("-fx-border-color: black;-fx-border-width: 0 0 0 10;-fx-background-color: whitesmoke;");
      // home.setStyle("-fx-background-color:transparent");
      home.getStyleClass().remove("activebtn");
     cars.getStyleClass().add("activebtn");
      rentals.getStyleClass().remove("activebtn");
      customers.getStyleClass().remove("activebtn");
      //   rentals.setStyle("-fx-background-color:transparent");
      //  customers.setStyle("-fx-background-color:transparent");
}
else if (event.getSource() == carrental_btn) {
  home_panel.setVisible(false);
  cars_panel.setVisible(false);
  rental_panel.setVisible(true);
  cutomers_panel.setVisible(false);

  home.getStyleClass().remove("activebtn");
  cars.getStyleClass().remove("activebtn");
  rentals.getStyleClass().add("activebtn");
  customers.getStyleClass().remove("activebtn");
}
else if (event.getSource() == customers_btn) {
  home_panel.setVisible(false);
  cars_panel.setVisible(false);
  rental_panel.setVisible(false);
  cutomers_panel.setVisible(true);
 
  home.getStyleClass().remove("activebtn");
  cars.getStyleClass().remove("activebtn");
  rentals.getStyleClass().remove("activebtn");
  customers.getStyleClass().add("activebtn");
}


  }
  public void close() {
    System.exit(0);
}

public void minimize() {
    Stage stage = (Stage) home_panel.getScene().getWindow();
    stage.setIconified(true);
}
@FXML
  private void clearCarFields() {
        car_id.clear();
        car_brand.clear();
        car_model.clear();
        car_price.clear();
        car_image.setImage(null);
        car_category.setValue(null);
         imageDirectory = null;
         car_status.setValue(null);
    }
@FXML
    private void handleUpload() {
      imageDirectory="src/images";
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
;        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            try {
                fileName = selectedFile.getName();
                targetPath = imageDirectory + "/"+ fileName;
                // targetPath =  fileName;
                image_db="/images/"+fileName;
                // Copy file to images directory
                File destinationFile=new File(targetPath);
                if(destinationFile.exists()){
                  Alert alert=new Alert(AlertType.INFORMATION);
                alert.setTitle("Image name already exists");
                alert.setHeaderText("the specified image name already exists try to rename");
                if(alert.showAndWait().get()==ButtonType.OK){
                                       return;
                   }

                }
                Files.copy(selectedFile.toPath(), Paths.get(targetPath));
                
                // Save to database
                // dbHelper.saveImage(fileName, targetPath);
                
                // Display the newly uploaded image
                displayImage(targetPath);
            } catch (IOException e) {
                 e.getMessage();
            }
        }
    }


      private void displayImage(String imagePath) {
        try {
            File file = new File(imagePath);
            if (file.exists()) {
                Image image = new Image(new FileInputStream(file));
                car_image.setImage(image);
               
            } else {
              return;
            }
        } catch (IOException e) {
             e.getMessage();
        }
    }

@FXML
private void insert_cars(){
PreparedStatement preparedStatement = null;
Statement statement = null;
int category_num = 0;
        ResultSet resultSet = null;

        try {
          final String INSERT_CAR_SQL = 
        "INSERT INTO cars (car_id, car_image, car_brand, car_model, car_price, category_id, car_status) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";





        final String CATEGORY_SQL="INSERT INTO car_categories (category_name,description) VALUES (' "+ car_category.getValue() +"' ,'falcon car rental') ";
       
        
        
        
        
        Connection connection=DatabaseConnection.connectDatabase();
           System.out.println("step 1");
          preparedStatement=connection.prepareStatement(INSERT_CAR_SQL);
        
          statement=connection.createStatement();
                                          try {
                                            resultSet=statement.executeQuery("SELECT category_id FROM car_categories ORDER BY category_id DESC LIMIT 1");
                                            if(resultSet.next()){
                                              category_num=resultSet.getInt("category_id");
                                            }

                                          } catch (Exception e) {
                                            // TODO: handle exception
                                          }
                                          finally{
                                            statement.close();
                                          }

           if((car_status.getValue()==null)||car_image.getImage()==null||car_category.getValue()==null||car_id.getText().isBlank()||car_brand.getText().isBlank()||car_model.getText().isBlank()||car_price.getText().isBlank())
        {
           Alert alert=new Alert(AlertType.ERROR);
                alert.setTitle("Fill all entries");
                alert.setHeaderText("fill all entries");
                if(alert.showAndWait().get()==ButtonType.OK){
                                       return;
                   }
        }
        else if (!check_price(car_price.getText())) {
          Alert alert=new Alert(AlertType.ERROR);
                alert.setTitle("Invalid price");
                alert.setHeaderText("Please provide a valid price");
                if(alert.showAndWait().get()==ButtonType.OK){
                                       return;
                   }
        } 


        else{
          statement=connection.createStatement();
          System.out.println("step 2");
          System.out.println(category_num);
          /*********************** */
          int rowaff=statement.executeUpdate(CATEGORY_SQL);
          try {
            resultSet=statement.executeQuery("SELECT category_id FROM car_categories ORDER BY category_id DESC LIMIT 1");
            if(resultSet.next()){
              category_num=resultSet.getInt("category_id");
            }

          } catch (Exception e) {
            // TODO: handle exception
          }
          finally{
            statement.close();
          }
          /************************** */
          preparedStatement.setString(1, car_id.getText());
          preparedStatement.setString(2, image_db);  // Store image path as string
          preparedStatement.setString(3, car_brand.getText());
          preparedStatement.setString(4, car_model.getText());
          preparedStatement.setDouble(5, Double.parseDouble(car_price.getText()));
        preparedStatement.setInt(6,category_num);
          preparedStatement.setString(7, car_status.getValue());
     
         
          int rowsAffected = preparedStatement.executeUpdate();
          if(rowsAffected==1){
           
         


/****************************************************************** */
          scrollbody.getChildren().clear();
          display_all_image(addedCars());
          gridScrollable.getChildren().clear();
          displayAvailableCars();

System.out.println("congrats");
Alert alert=new Alert(AlertType.INFORMATION);
                // alert.setTitle("Fill all entries");
               alert.setHeaderText("you Inserted successfully");
                if(alert.showAndWait().get()==ButtonType.OK){
                  clearCarFields();
                                      return;
                   }

        }

      }
    }
      catch (Exception e) {
                // e.printStackTrace();
                Alert alert=new Alert(AlertType.ERROR);
                // alert.setTitle("Fill all entries");
               alert.setHeaderText("Enter a unique car id(plate number recomended)");
                if(alert.showAndWait().get()==ButtonType.OK){

                }

      }
          
finally {
            // Close resources
            try {
               preparedStatement.close();
               
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }



      }

@FXML
private void delete_cars() {
    if (car_id.getText().isBlank()) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Please enter a car ID to delete");
                alert.showAndWait();
        return;
          }

    Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
    confirmAlert.setTitle("Confirm Deletion");
    confirmAlert.setHeaderText("Are you sure you want to delete this car and its category information?");
    
    if (confirmAlert.showAndWait().get() == ButtonType.OK) {
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        try {
            connection = DatabaseConnection.connectDatabase();
            connection.setAutoCommit(false); // Start transaction
            
            // First get the category_id for the car
            String getCategoryQuery = "SELECT category_id FROM cars WHERE car_id = ?";
            preparedStatement = connection.prepareStatement(getCategoryQuery);
            preparedStatement.setString(1, car_id.getText());
            ResultSet rs = preparedStatement.executeQuery();
            
            if (rs.next()) {
                int categoryId = rs.getInt("category_id");
                
                // Delete the car first
                String deleteCarQuery = "DELETE FROM cars WHERE car_id = ?";
                preparedStatement = connection.prepareStatement(deleteCarQuery);
                preparedStatement.setString(1, car_id.getText());
                int carRowsAffected = preparedStatement.executeUpdate();
                
                if (carRowsAffected > 0) {
                    // Check if this category is used by other cars
                    String checkCategoryQuery = "SELECT COUNT(*) FROM cars WHERE category_id = ?";
                    preparedStatement = connection.prepareStatement(checkCategoryQuery);
                    preparedStatement.setInt(1, categoryId);
                    rs = preparedStatement.executeQuery();
                    
                    if (rs.next() && rs.getInt(1) == 0) {
                        // If no other cars use this category, delete it
                        String deleteCategoryQuery = "DELETE FROM car_categories WHERE category_id = ?";
                        preparedStatement = connection.prepareStatement(deleteCategoryQuery);
                        preparedStatement.setInt(1, categoryId);
                        preparedStatement.executeUpdate();
                    }
                    
                    connection.commit(); // Commit transaction
                    setDashboard_fields();
                Alert successAlert = new Alert(AlertType.INFORMATION);
                successAlert.setTitle("Success");
                    successAlert.setHeaderText("Car and associated category information deleted successfully");
                successAlert.showAndWait();
                    
                clearCarFields(); 
                scrollbody.getChildren().clear();
          display_all_image(addedCars());
          gridScrollable.getChildren().clear();
          displayAvailableCars();
            } else {
                    connection.rollback(); // Rollback if car deletion failed
                    Alert errorAlert = new Alert(AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setContentText("No car found with the specified ID");
                    errorAlert.showAndWait();
                }
            } else {
                connection.rollback(); // Rollback if car not found
                Alert errorAlert = new Alert(AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setContentText("No car found with the specified ID");
                errorAlert.showAndWait();
            }
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback(); // Rollback on error
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setContentText("An error occurred while deleting: " + e.getMessage());
            errorAlert.showAndWait();
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) {
                    connection.setAutoCommit(true); // Reset auto-commit
                    connection.close();
                }
               
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}


@FXML
public void setData(String id,String image_car,String brand,String model,String price,int category){
 
 

 try {
  
   
   Image image=new Image(getClass().getResourceAsStream(image_car));
   car_image.setImage(image);
   car_id.setText(id);
   car_brand.setText(brand);
   car_model.setText(model);
   car_price.setText(price);
   this.category_number = category;
  } catch (Exception e) {
   // TODO: handle exception
  }
  
  
  }
 public boolean check_price(String st){

  try {
    Double.parseDouble(st);
    return true;
  } catch (Exception e) {
    e.printStackTrace();
    return false;
  }
 }

    @FXML
    void carSearch(ActionEvent event) {
        String searchText = car_search.getText().toLowerCase().trim();
        
        System.out.println(car_search.getText());
        if (searchText.isEmpty()) {
            // If search is empty, show all cars
            scrollbody.getChildren().clear();
            display_all_image(addedCars());
            return;
        }

        try {
            Connection connection = DatabaseConnection.connectDatabase();
            List<Car> searchResults = new ArrayList<>();
            
            // First check if search text matches any category
            String categoryQuery = "SELECT category_id FROM car_categories WHERE LOWER(category_name) LIKE ?";
            PreparedStatement categoryStmt = connection.prepareStatement(categoryQuery);
            categoryStmt.setString(1, "%" + searchText + "%");
            ResultSet categoryRs = categoryStmt.executeQuery();
            
            // If category match found, search cars in that category
            if (categoryRs.next()) {
                int categoryId = categoryRs.getInt("category_id");
                String carQuery = "SELECT car_id, car_image, car_brand, car_model, car_price, category_id " +
                                "FROM cars WHERE category_id = ?";
                PreparedStatement carStmt = connection.prepareStatement(carQuery);
                carStmt.setInt(1, categoryId);
                ResultSet carRs = carStmt.executeQuery();
                
                while (carRs.next()) {
                    Car car = new Car();
                    car.setCarid(carRs.getString("car_id"));
                    car.setCarimage(carRs.getString("car_image"));
                    car.setCarmake(carRs.getString("car_brand"));
                    car.setCarmodel(carRs.getString("car_model"));
                    car.setCarprice(String.valueOf(carRs.getDouble("car_price")));
                    searchResults.add(car);
                }
                carRs.close();
                carStmt.close();
            } else {
                // If no category match, search in car fields
                String carQuery = "SELECT car_id, car_image, car_brand, car_model, car_price, category_id " +
                                "FROM cars " +
                                "WHERE LOWER(car_id) LIKE ? " +
                                "OR LOWER(car_brand) LIKE ? " +
                                "OR LOWER(car_model) LIKE ?";
                
                PreparedStatement carStmt = connection.prepareStatement(carQuery);
                String searchPattern = "%" + searchText + "%";
                carStmt.setString(1, searchPattern);
                carStmt.setString(2, searchPattern);
                carStmt.setString(3, searchPattern);
                
                ResultSet carRs = carStmt.executeQuery();
                
                while (carRs.next()) {
                    Car car = new Car();
                    car.setCarid(carRs.getString("car_id"));
                    car.setCarimage(carRs.getString("car_image"));
                    car.setCarmake(carRs.getString("car_brand"));
                    car.setCarmodel(carRs.getString("car_model"));
                    car.setCarprice(String.valueOf(carRs.getDouble("car_price")));
                    searchResults.add(car);
                }
                carRs.close();
                carStmt.close();
            }
            
            // Clear and update the display with search results
            scrollbody.getChildren().clear();
            if (!searchResults.isEmpty()) {
                System.out.println(searchResults.getFirst());
                display_searched_image(searchResults);
            } else {
                Alert noResultsAlert = new Alert(AlertType.INFORMATION);
                noResultsAlert.setTitle("No Results");
                noResultsAlert.setHeaderText("No cars found matching your search criteria");
                noResultsAlert.showAndWait();
            }

            // Close resources
            categoryRs.close();
            categoryStmt.close();
            connection.close();

        } catch (SQLException e) {
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setTitle("Search Error");
            errorAlert.setContentText("An error occurred while searching: " + e.getMessage());
            errorAlert.showAndWait();
            e.printStackTrace();
        }
        System.out.println("search completed");
    }

    @FXML
    void carUpdate(ActionEvent event) {
        if (car_id.getText().isBlank()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please enter a car ID to update");
            alert.showAndWait();
            return;
        }

        if (car_brand.getText().isBlank() || car_model.getText().isBlank() || 
            car_price.getText().isBlank() || car_status.getValue() == null||car_category.getValue()==null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please fill all required fields");
            alert.showAndWait();
            return;
        }

        if (!check_price(car_price.getText())) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid price");
            alert.setHeaderText("Please provide a valid price");
            alert.showAndWait();
            return;
        }

        Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Update");
        confirmAlert.setHeaderText("Are you sure you want to update this car's information?");
        
        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            PreparedStatement preparedStatement = null;
            try {
                Connection connection = DatabaseConnection.connectDatabase();
                
                // Simple update for car_categories
                if (car_category.getValue() != null) {
                    String updateCategory = "UPDATE car_categories SET category_name = ? WHERE category_id = ?";
                    preparedStatement = connection.prepareStatement(updateCategory);
                    preparedStatement.setString(1, car_category.getValue());
                    preparedStatement.setInt(2, category_number);
                    preparedStatement.executeUpdate();
                    System.out.println(category_number);
                }

                // Simple update for cars table
                String updateCar = "UPDATE cars SET " +
                                 "car_brand = ?, " +
                                 "car_model = ?, " +
                                 "car_price = ?, " +
                                 "car_status = ?, " +
                                 "category_id = ? " +
                                 "WHERE car_id = ?";

                preparedStatement = connection.prepareStatement(updateCar);
                preparedStatement.setString(1, car_brand.getText());
                preparedStatement.setString(2, car_model.getText());
                preparedStatement.setDouble(3, Double.parseDouble(car_price.getText()));
                preparedStatement.setString(4, car_status.getValue());
                preparedStatement.setInt(5, category_number);
                preparedStatement.setString(6, car_id.getText());
                
                int rowsAffected = preparedStatement.executeUpdate();
                
                if (rowsAffected > 0) {
                    setDashboard_fields();
                    Alert successAlert = new Alert(AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText("Car information updated successfully");
                    successAlert.showAndWait();
                    
                    // Refresh the car display
                    scrollbody.getChildren().clear();
                    display_all_image(addedCars());
                    gridScrollable.getChildren().clear();
                    displayAvailableCars();
                    clearCarFields();
                } else {
                    Alert errorAlert = new Alert(AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setContentText("No car found with the specified ID");
                    errorAlert.showAndWait();
                }
            } catch (SQLException e) {
                Alert errorAlert = new Alert(AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setContentText("An error occurred while updating the car: " + e.getMessage());
                errorAlert.showAndWait();
                e.printStackTrace();
            } finally {
                try {
                    if (preparedStatement != null) preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    
    @FXML
    void rent_cars(ActionEvent event) {
        // Validate required fields
        if (rentalCar_id.getText().isBlank() || first_name.getText().isBlank() || 
            last_name.getText().isBlank() || rental_email.getText().isBlank() || 
            rental_gender.getValue() == null || date_rented.getValue() == null || 
            date_returned.getValue() == null) {
            
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please fill all required fields");
            alert.showAndWait();
            return;
        }

        // Validate dates
        if (date_rented.getValue().isAfter(date_returned.getValue())) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Return date must be after rent date");
            alert.showAndWait();
            return;
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.connectDatabase();

            // Check if customer exists
            String checkCustomerQuery = "SELECT customer_id FROM customers WHERE email = ?";
            preparedStatement = connection.prepareStatement(checkCustomerQuery);
            preparedStatement.setString(1, rental_email.getText());
            resultSet = preparedStatement.executeQuery();

            int customerId;
            if (resultSet.next()) {
                // Customer exists, get their ID
                customerId = resultSet.getInt("customer_id");
            } else {
                // Create new customer
                String insertCustomerQuery = "INSERT INTO customers (first_name, last_name, gender, email) VALUES (?, ?, ?, ?)";
                preparedStatement = connection.prepareStatement(insertCustomerQuery, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, first_name.getText());
                preparedStatement.setString(2, last_name.getText());
                preparedStatement.setString(3, rental_gender.getValue());
                preparedStatement.setString(4, rental_email.getText());
                preparedStatement.executeUpdate();

                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    customerId = resultSet.getInt(1);
                } else {
                    throw new SQLException("Failed to get generated customer ID");
                }
            }

            // Create rental record
            String insertRentalQuery = "INSERT INTO rentals (car_id, customer_id, date_rented, date_returned) VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(insertRentalQuery);
            preparedStatement.setString(1, rentalCar_id.getText());
            preparedStatement.setInt(2, customerId);
            preparedStatement.setDate(3, java.sql.Date.valueOf(date_rented.getValue()));
            preparedStatement.setDate(4, java.sql.Date.valueOf(date_returned.getValue()));
            preparedStatement.executeUpdate();

            // Update car status to Rented
            String updateCarQuery = "UPDATE cars SET car_status = 'Rented' WHERE car_id = ?";
            preparedStatement = connection.prepareStatement(updateCarQuery);
            preparedStatement.setString(1, rentalCar_id.getText());
            preparedStatement.executeUpdate();

            Alert successAlert = new Alert(AlertType.INFORMATION);
            successAlert.setTitle("Success");
            successAlert.setHeaderText("Car rented successfully");
            rentCarShowListData();
            successAlert.showAndWait();

            // Clear fields and refresh displays
            rent_clear(event);
            scrollbody.getChildren().clear();
            display_all_image(addedCars());
            gridScrollable.getChildren().clear();
            displayAvailableCars();
            setDashboard_fields();

        } catch (SQLException e) {
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setContentText("An error occurred while renting the car: " + e.getMessage());
            errorAlert.showAndWait();
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // @FXML
    // void customer_update(ActionEvent event) {
    //  System.out.println("updated");
    // }
    @FXML
    void customer_delete(ActionEvent event) {
        // Get customer ID from text field
        String customerId = cust_id.getText();
        
        if (customerId == null || customerId.trim().isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No customer ID entered");
            alert.setContentText("Please enter a customer ID to delete");
            alert.showAndWait();
            return;
        }

        // Confirm deletion
        Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Are you sure you want to delete this customer and all their rental records?");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            Connection connection = null;
            PreparedStatement preparedStatement = null;

            try {
                connection = DatabaseConnection.connectDatabase();
                connection.setAutoCommit(false); // Start transaction

                // First delete rental records
                String deleteRentalsQuery = "DELETE FROM rentals WHERE customer_id = ?";
                preparedStatement = connection.prepareStatement(deleteRentalsQuery);
                preparedStatement.setInt(1, Integer.parseInt(customerId));
                preparedStatement.executeUpdate();

                // Then delete the customer
                String deleteCustomerQuery = "DELETE FROM customers WHERE customer_id = ?";
                preparedStatement = connection.prepareStatement(deleteCustomerQuery);
                preparedStatement.setInt(1, Integer.parseInt(customerId));
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    connection.commit(); // Commit transaction
                    Alert successAlert = new Alert(AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText("Customer and associated rental records deleted successfully");
                    successAlert.showAndWait();

                    // Clear the text field
                    cust_id.clear();
                    cust_first_name.clear();
                    cust_last_name.clear();
                    customer_gender.getSelectionModel().clearSelection();

                    // Refresh the table
                    rentCarShowListData();
                } else {
                    connection.rollback(); // Rollback transaction
                    Alert errorAlert = new Alert(AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText("Failed to delete customer");
                    errorAlert.setContentText("No customer found with ID: " + customerId);
                    errorAlert.showAndWait();
                }

        } catch (SQLException e) {
            try {
                if (connection != null) {
                        connection.rollback(); // Rollback transaction on error
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setTitle("Error");
                errorAlert.setContentText("An error occurred while deleting the customer: " + e.getMessage());
            errorAlert.showAndWait();
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) {
                    connection.setAutoCommit(true); // Reset auto-commit
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    }

    @FXML
    void handle_logout(ActionEvent event) {
        Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
        confirmAlert.setTitle("Logout Confirmation");
        confirmAlert.setHeaderText("Are you sure you want to logout?");
        confirmAlert.setContentText("You will be redirected to the login page.");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            try {
                // Get the current stage
                Stage currentStage = (Stage) logout_btn.getScene().getWindow();
                
                // Load the login scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/LoginScene.fxml"));
                Parent root = loader.load();
                
                // Create new scene
                Scene scene = new Scene(root);
                
                // Set the new scene
                currentStage.setScene(scene);
                currentStage.centerOnScreen();
                
            } catch (IOException e) {
                Alert errorAlert = new Alert(AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setContentText("Error loading login scene: " + e.getMessage());
                errorAlert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    @FXML
    void generate_report(ActionEvent event) {
        Connection connection = DatabaseConnection.connectDatabase();
        try {
            // Get all customers with their rental information
            String query = """
                SELECT c.customer_id, c.first_name, c.last_name, c.gender,
                       r.car_id, r.date_rented, r.date_returned,
                       car.car_brand, car.car_model, car.car_price
                FROM customers c
                LEFT JOIN rentals r ON c.customer_id = r.customer_id
                LEFT JOIN cars car ON r.car_id = car.car_id
                ORDER BY c.customer_id
                """;

            PreparedStatement pst = connection.prepareStatement(query);
            ResultSet resultSet = pst.executeQuery();

            // Create file chooser
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Report");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
            );

            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                try (FileWriter writer = new FileWriter(file);
                     BufferedWriter bw = new BufferedWriter(writer)) {

                    // Write header
                    bw.write("Customer ID,First Name,Last Name,Gender,Car ID,Date Rented,Date Returned,Car Brand,Car Model,Car Price");
                    bw.newLine();

                    // Write data
                    while (resultSet.next()) {
                        String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                            resultSet.getString("customer_id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getString("gender"),
                            resultSet.getString("car_id"),
                            resultSet.getString("date_rented"),
                            resultSet.getString("date_returned"),
                            resultSet.getString("car_brand"),
                            resultSet.getString("car_model"),
                            resultSet.getString("car_price")
                        );
                        bw.write(line);
                        bw.newLine();
                    }
                }

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Report generated successfully!");
                alert.showAndWait();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error generating report: " + e.getMessage());
            alert.showAndWait();
        }
    }

void setDashboard_fields() {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    try {
        connection = DatabaseConnection.connectDatabase();
        
        // Get user's first name
     
        // Get count of available cars
        String carQuery = "SELECT COUNT(*) as available_cars FROM cars WHERE car_status = 'Available'";
        preparedStatement = connection.prepareStatement(carQuery);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            dashboard_car.setText(String.valueOf(resultSet.getInt("available_cars")));
        }

        // Get total income from rentals
        String incomeQuery = "SELECT SUM(c.car_price) as total_income " +
                           "FROM rentals r " +
                           "JOIN cars c ON r.car_id = c.car_id";
        preparedStatement = connection.prepareStatement(incomeQuery);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            double totalIncome = resultSet.getDouble("total_income");
            dashboard_income.setText(String.format("$%.2f", totalIncome));
        }

        // Get total number of customers
        String customerQuery = "SELECT COUNT(*) as total_customers FROM customers";
        preparedStatement = connection.prepareStatement(customerQuery);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            dashboard_customers.setText(String.valueOf(resultSet.getInt("total_customers")));
        }

    } catch (SQLException e) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("Error fetching dashboard information: " + e.getMessage());
        alert.showAndWait();
        e.printStackTrace();
    } finally {
        try {
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

void set_car_rental_fields(String carid,String carbrand,String carprice){
  rentalCar_id.setText(carid);
  rental_brand.setText(carbrand);
  rental_price.setText(carprice);

}

@FXML
void rent_clear(ActionEvent event) {
   rentalCar_id.clear();
   rental_brand.clear();
   rental_price.setText("");
   rental_email.clear();
   first_name.clear();
   last_name.clear();
   rental_gender.getSelectionModel().clearSelection();
   date_rented.setValue(null);
   date_returned.setValue(null);
}

public void set_system_user(String user){
  system_user.setText(user);
  System.out.println(user);
}



 public ObservableList<CustomerData> rentCarListData(){
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    // List<CustomerData> customerList = new ArrayList<>();
    ObservableList<CustomerData> customerList = FXCollections.observableArrayList();
    try {
        connection = DatabaseConnection.connectDatabase();
        
        // Query to get customer information with their rental details
        String query = "SELECT c.customer_id, c.first_name, c.last_name, c.gender, " +
                     "r.car_id, r.date_rented, r.date_returned " +
                     "FROM customers c " +
                     "LEFT JOIN rentals r ON c.customer_id = r.customer_id " +
                     "ORDER BY c.customer_id";

        preparedStatement = connection.prepareStatement(query);
        resultSet = preparedStatement.executeQuery();

        // Clear existing items
        // customers_tableView.getItems().clear();

        // Create a list to hold customer data
        // List<CustomerData> customerList = new ArrayList<>();

        while (resultSet.next()) {
            CustomerData customer = new CustomerData(
                resultSet.getString("customer_id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getString("gender"),
                resultSet.getString("car_id"),
                resultSet.getDate("date_rented"),
                resultSet.getDate("date_returned")
            );
            customerList.add(customer);
        }
// return customerList;
        // Set the data to the table
        // customers_tableView.getItems().addAll(customerList);

    } catch (SQLException e) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("Error fetching customer information: " + e.getMessage());
        alert.showAndWait();
        e.printStackTrace();
    } finally {
        try {
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return customerList;
}


  private ObservableList<CustomerData> rentCustomerList;

public void rentCarShowListData() {
    rentCustomerList = rentCarListData();

    // Map table columns to CustomerData properties
    customer_col_customerid.setCellValueFactory(new PropertyValueFactory<>("customerId"));
    customer_col_firstname.setCellValueFactory(new PropertyValueFactory<>("firstName"));
    customer_col_lastname.setCellValueFactory(new PropertyValueFactory<>("lastName"));
    customer_col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
    customer_col_carid.setCellValueFactory(new PropertyValueFactory<>("carId"));
    customer_col_daterented.setCellValueFactory(new PropertyValueFactory<>("dateRented"));
    customer_col_datereturned.setCellValueFactory(new PropertyValueFactory<>("dateReturned"));

    customers_tableView.setItems(rentCustomerList);
}


// Customer data class to hold table information
public static class CustomerData {
    private final String customerId;
    private final String firstName;
    private final String lastName;
    private final String gender;
    private final String carId;
    private final Date dateRented;
    private final Date dateReturned;

    public CustomerData(String customerId, String firstName, String lastName, 
                      String gender, String carId, Date dateRented, Date dateReturned) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.carId = carId;
        this.dateRented = dateRented;
        this.dateReturned = dateReturned;
    }

    // Getters
    public String getCustomerId() { return customerId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getGender() { return gender; }
    public String getCarId() { return carId; }
    public Date getDateRented() { return dateRented; }
    public Date getDateReturned() { return dateReturned; }
}

@FXML
void customer_update(ActionEvent event) {
    String customerId = cust_id.getText();
    String firstName = cust_first_name.getText();
    String lastName = cust_last_name.getText();
    Object genderObj = customer_gender.getValue();
    String gender = (genderObj != null) ? genderObj.toString() : null;

    // Validation
    if (customerId == null || customerId.trim().isEmpty()) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("No customer ID entered");
        alert.setContentText("Please enter a customer ID to update");
        alert.showAndWait();
        return;
    }
    if (firstName == null || firstName.trim().isEmpty() ||
        lastName == null || lastName.trim().isEmpty() ||
        gender == null || gender.trim().isEmpty()) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Missing information");
        alert.setContentText("Please fill in all customer fields.");
        alert.showAndWait();
        return;
    }

    Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
    confirmAlert.setTitle("Confirm Update");
    confirmAlert.setHeaderText("Are you sure you want to update this customer's information?");
    if (confirmAlert.showAndWait().get() == ButtonType.OK) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DatabaseConnection.connectDatabase();
            String updateQuery = "UPDATE customers SET first_name = ?, last_name = ?, gender = ? WHERE customer_id = ?";
            preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, gender);
            preparedStatement.setInt(4, Integer.parseInt(customerId));
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                Alert successAlert = new Alert(AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText("Customer information updated successfully");
                successAlert.showAndWait();
                rentCarShowListData(); // Refresh table
            } else {
                Alert errorAlert = new Alert(AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Failed to update customer");
                errorAlert.setContentText("No customer found with ID: " + customerId);
                errorAlert.showAndWait();
            }
        } catch (Exception e) {
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setContentText("An error occurred while updating the customer: " + e.getMessage());
            errorAlert.showAndWait();
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

}