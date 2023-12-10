package controllers;

import classes.Main;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import oracle.jdbc.driver.OracleDriver;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class addTourController
{
    static Stage stage;
    private ArrayList<String> touristicPictures=new ArrayList<>();

    @FXML
    private TextField nameField;

    @FXML
    private TextField locationField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField cidField;

    @FXML
    private Button savebutton;

    @FXML
    private Button uploadButton;

    @FXML
    void saveEvent(ActionEvent event)
    {
         int id=-1;
         String name;
         String location;
         String description;
         int cid;

        if(this.checkIfDataIsValid())
        {
            name=this.nameField.getText();
            location=this.locationField.getText();
            description=this.descriptionField.getText();
            cid=Integer.parseInt(this.cidField.getText());

            try
            {
                DriverManager.registerDriver((new OracleDriver()));
                String url="jdbc:oracle:thin:@localhost:1521:xe";
                Connection con = DriverManager.getConnection(url, "c##sundos_yaqeen", "11223344");

                String query="insert into Touristic_place values(id_seq.nextVAL,'"+name+"','"+location+"','"+description+"',"+cid+")";
                PreparedStatement ps=con.prepareStatement(query);
                ps.executeQuery();

                query="select id_seq.currval from DUAL";
                ps=con.prepareStatement(query);
                ResultSet resultSet=ps.executeQuery();
                resultSet.next();
                id=resultSet.getInt(1);

                for(int i=0;i<this.touristicPictures.size();i++)
                {
                    query="insert into touristic_places_pictures values(id_seq.nextVAL,'"+this.touristicPictures.get(i)+"',"+id+")";
                    ps=con.prepareStatement(query);
                    ps.executeQuery();
                }

                con.close();
                savebutton.setText("saved");
                Image image = new Image(("/images/check.png"));
                ImageView view = new ImageView(image);
                savebutton.setGraphic(view);

                Task<Void> sleeper = new Task<Void>()
                {
                    @Override
                    protected Void call()
                    {
                        try
                        {
                            Thread.sleep(1500);
                        }
                        catch (InterruptedException e)
                        {
                        }
                        return null;
                    }
                };

                sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>()
                {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        savebutton.setGraphic(null);
                        savebutton.setText("save");
                    }
                });
                new Thread(sleeper).start();

                Alert alert=new Alert(Alert.AlertType.NONE);
                alert.initOwner(Main.mainStage);
                alert.getButtonTypes().add(ButtonType.OK);
                alert.setHeaderText(null);
                alert.setContentText("Touristic Place Added Successfully.");
                alert.showAndWait();

                FXMLLoader fxmlLoader1 = new FXMLLoader();
                fxmlLoader1.setLocation(getClass().getResource("/fxml/adminScreen.fxml"));
                Parent root1= fxmlLoader1.load();
                adminscreencontroller Adminscreencontroller = fxmlLoader1.getController();

                FXMLLoader fxmlLoader2 = new FXMLLoader();
                fxmlLoader2.setLocation(getClass().getResource("/fxml/TourTableScreen.fxml"));
                Parent root2= fxmlLoader2.load();
                TourTableScreenController tourTableScreenController = fxmlLoader2.getController();
                tourTableScreenController.fillTableData("select *from touristic_place");

                Adminscreencontroller.anch.getChildren().clear();
                Adminscreencontroller.anch.getChildren().add(root2);

                Scene scene=new Scene(root1);
                Main.mainStage.setScene(scene);
                stage.close();
            }
            catch(SQLIntegrityConstraintViolationException sqlIntegrityConstraintViolationException)
            {
                this.reverseOnError(id);
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.initOwner(Main.mainStage);
                alert.setHeaderText(null);
                alert.setContentText("Maybe You've Entered An Existing Image Or The Entered City ID Not Found");
                alert.showAndWait();
            }
            catch (SQLException sqlException)
            {
                this.reverseOnError(id);
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.initOwner(Main.mainStage);
                alert.setHeaderText("Data-Base Connection Error!");
                alert.setContentText(sqlException.getMessage());
                alert.showAndWait();
            }
            catch(NullPointerException | IOException exception)
            {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.initOwner(Main.mainStage);
                alert.setHeaderText("Loading Screen Error!");
                alert.setContentText(exception.getMessage());
                alert.show();
            }
        }
    }

    @FXML
    void uploadEvent()
    {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilterJPG
                = new FileChooser.ExtensionFilter("JPG files (*.JPG)", "*.JPG");
        FileChooser.ExtensionFilter extFilterjpg
                = new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter extFilterPNG
                = new FileChooser.ExtensionFilter("PNG files (*.PNG)", "*.PNG");
        FileChooser.ExtensionFilter extFilterpng
                = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        FileChooser.ExtensionFilter extFilterJPEG
                = new FileChooser.ExtensionFilter("JPEG files (*.JPEG)", "*.JPEG");
        FileChooser.ExtensionFilter extFilterjpeg
                = new FileChooser.ExtensionFilter("jpeg files (*.jpeg)", "*.jpeg");
        fileChooser.getExtensionFilters()
                .addAll(extFilterJPG, extFilterjpg, extFilterPNG, extFilterpng,extFilterJPEG,extFilterjpeg);

        File file = fileChooser.showOpenDialog(stage);

        if (file != null)
        {
            String imgPath=file.toString();
            this.touristicPictures.add(imgPath);
        }
    }

    private boolean checkIfDataIsValid()
    {
        String errorMessage="";
        boolean error=false;

        if(this.nameField.getText().isBlank())
        {
            this.nameField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Touristic Place Name Field Is Empty.\n";
            error=true;
        }
        else
        {
            this.nameField.setStyle("");
        }

        if(this.locationField.getText().isBlank())
        {
            this.locationField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Location Field Is Empty.\n";
            error=true;
        }
        else
        {
            this.locationField.setStyle("");
        }

        if(this.descriptionField.getText().isBlank())
        {
            this.descriptionField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Description Field Is Empty.\n";
            error=true;
        }
        else
        {
            this.descriptionField.setStyle("");
        }

        if(this.cidField.getText().isBlank())
        {
            this.cidField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*City ID Field Is Empty.\n";
            error=true;
        }
        else if(!this.isNumeric(this.cidField.getText()))
        {
            this.cidField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*City ID Is Not Valid.\n";
            error=true;
        }
        else
        {
            this.cidField.setStyle("");
        }

        if(this.touristicPictures.size()==0)
        {
            this.uploadButton.setStyle("-fx-border-color: #FF0000; -fx-focus-color: #FF0000; -fx-border-radius: 30;" +
                    "-fx-border-insets: 0,1,1;");
            errorMessage+="*Please Upload A Touristic Place Picture.\n";
            error=true;
        }
        else
        {
            this.uploadButton.setStyle("");
        }

        if(error)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(this.savebutton.getScene().getWindow());
            alert.setHeaderText("Data Is Invalid!");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
        else
        {
            return true;
        }
    }

    private boolean isNumeric(String string)
    {
        for(char c:string.toCharArray())
        {
            if(!Character.isDigit(c))
            {
                return false;
            }
        }
        return true;
    }

    private void reverseOnError(int tid)
    {
        try
        {
            DriverManager.registerDriver((new OracleDriver()));
            String url="jdbc:oracle:thin:@localhost:1521:xe";
            Connection con = DriverManager.getConnection(url, "c##sundos_yaqeen", "11223344");
            String query="delete from touristic_places_pictures where tid="+tid+"";
            PreparedStatement ps=con.prepareStatement(query);
            ps.executeQuery();

            query="delete from touristic_place where touristic_id="+tid+"";
            ps=con.prepareStatement(query);
            ps.executeQuery();

            con.close();
        }
        catch (SQLException sqlException)
        {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.initOwner(Main.mainStage);
            alert.setHeaderText("Data-Base Connection Error!");
            alert.setContentText(sqlException.getMessage());
            alert.showAndWait();
        }
    }
}