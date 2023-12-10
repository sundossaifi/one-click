package controllers;

import classes.Main;
import classes.Stuff;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import oracle.jdbc.driver.OracleDriver;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class editUsercontroller implements Initializable
{
    private String imgPath;
    @FXML
    private AnchorPane anc;

    @FXML
    private TextField nameField;

    @FXML
    private TextField ssnField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField countryField;

    @FXML
    private PasswordField oldPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmNewPasswordField;

    @FXML
    private Button savebutton;
    @FXML
    private Button cancelButton;
    @FXML
    private DatePicker dobField;

    @FXML
    private Circle circle;

    @FXML
    private Button camerabutton;

    @FXML
    private Button backbutton;

    @FXML
    private CheckBox changePasswordBox;

    @FXML
    void def()
    {
        Image image = new Image(("/images/prev.png"));
        ImageView view = new ImageView(image);
        this.backbutton.setGraphic(view);
    }

    @FXML
    void gif()
    {
        Image image = new Image(("/images/prev.gif"));
        ImageView view = new ImageView(image);
        this.backbutton.setGraphic(view);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        this.circle.setFill(new ImagePattern(new Image(new File(Stuff.user.getPicPath()).toURI().toString())));
        Image ima = new Image(("/images/camera.png"));
        ImageView v = new ImageView(ima);
        this.camerabutton.setGraphic(v);
        Image ima1 = new Image(("/images/prev.png"));
        ImageView v1 = new ImageView(ima1);
        this.backbutton.setGraphic(v1);
        this.setFieldsData();
    }

    @FXML
    void backToProfileScreenEvent() throws IOException
    {
        Parent root= FXMLLoader.load(getClass().getResource("/fxml/userScreen.fxml"));
        Scene scene=new Scene(root);
        Main.mainStage.setScene(scene);
    }

    @FXML
    void changePictureEvent()
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

        File file = fileChooser.showOpenDialog(Main.mainStage);

        if (file != null)
        {
            this.imgPath=file.toString();
            this.circle.setFill(new ImagePattern(new Image(new File(this.imgPath).toURI().toString())));
        }
        else
        {
            this.imgPath=null;
        }
    }

    @FXML
    void saveAction()
    {
        int SSN;
        String name,country,email,dateOfBirth,password,picPath;
        LocalDate dob;

        if(this.checkIfDataIsValid())
        {
            name=this.nameField.getText();
            country=this.countryField.getText();
            SSN=Integer.parseInt(this.ssnField.getText());
            email=this.emailField.getText();
            password=this.newPasswordField.getText();
            dob=this.dobField.getValue();
            dateOfBirth=""+dob.getYear()+""+"-"+""+dob.getMonthValue()+""+"-"+""+dob.getDayOfMonth()+"";

            if(this.imgPath==null)
            {
                picPath=Stuff.user.getPicPath();
            }
            else
            {
                picPath=this.imgPath;
            }

            if(password.isBlank())
            {
                password=Stuff.user.getPassword();
            }

            try
            {
                DriverManager.registerDriver((new OracleDriver()));
                String url="jdbc:oracle:thin:@localhost:1521:xe";
                Connection con = DriverManager.getConnection(url, "C##sundos_yaqeen", "11223344");
                String query="update users set ssn="+SSN+",name='"+name+"',country='"+country+"',dob=TO_DATE('"+dateOfBirth+"','YYYY-MM-DD'),email='"+email+"',password='"+password+"',pic='"+picPath+"' where ssn="+Stuff.user.getSSN()+"";
                PreparedStatement ps=con.prepareStatement(query);
                ps.executeQuery();

                Alert alert=new Alert(Alert.AlertType.NONE);
                alert.initOwner(Main.mainStage);
                alert.getButtonTypes().add(ButtonType.OK);
                alert.setTitle("Property-Home");
                alert.setHeaderText(null);
                alert.setContentText("Your Information Updated Successfully.");
                alert.showAndWait();

                this.setSharedUserObject(name,SSN,dob,country,email,password,picPath);
                this.reset();

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
                            e.printStackTrace();
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
                con.close();
            }
            catch(SQLIntegrityConstraintViolationException sqlIntegrityConstraintViolationException)
            {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.initOwner(Main.mainStage);
                alert.setHeaderText(null);
                alert.setContentText("Maybe you've entered a pre-registered Email, Or SSN.");
                alert.showAndWait();
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

    @FXML
    void changePasswordBoxEvent()
    {
        if(this.changePasswordBox.isSelected())
        {
            this.oldPasswordField.setEditable(true);
            this.newPasswordField.setEditable(true);
            this.confirmNewPasswordField.setEditable(true);
        }
        else
        {
            this.oldPasswordField.clear();
            this.newPasswordField.clear();
            this.confirmNewPasswordField.clear();

            this.oldPasswordField.setEditable(false);
            this.newPasswordField.setEditable(false);
            this.confirmNewPasswordField.setEditable(false);

            this.oldPasswordField.setStyle("");
            this.newPasswordField.setStyle("");
            this.confirmNewPasswordField.setStyle("");
        }
    }

    private boolean checkIfDataIsValid()
    {
        String errorMessage="";
        boolean error=false;

        if(this.nameField.getText().isBlank())
        {
            this.nameField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Name Field Is Empty.\n";
            error=true;
        }
        else
        {
            this.nameField.setStyle("");
        }

        if(this.ssnField.getText().isBlank())
        {
            this.ssnField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*SSN Field Is Empty.\n";
            error=true;
        }
        else if(!this.isNumeric(this.ssnField.getText()))
        {
            this.ssnField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*SSN IS Invalid.\n";
            error=true;
        }
        else if(this.ssnField.getText().length()>9)
        {
            this.ssnField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*SSN Must Be At Maximum 9-Digits.\n";
            error=true;
        }
        else
        {
            this.ssnField.setStyle("");
        }

        if(this.countryField.getText().isBlank())
        {
            this.countryField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Country Field Is Empty.\n";
            error=true;
        }
        else
        {
            this.countryField.setStyle("");
        }

        if(this.emailField.getText().isBlank())
        {
            this.emailField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Email Field Is Empty.\n";
            error=true;
        }
        else if(!this.isValidEmail(this.emailField.getText()))
        {
            this.emailField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Email Address Is Not Valid.\n";
            error=true;
        }
        else
        {
            this.emailField.setStyle("");
        }

        if(this.dobField.getValue()==null)
        {
            this.dobField.setStyle("-fx-border-color: #FF0000; -fx-focus-color: #FF0000; -fx-border-radius: 30;" +
                    "-fx-border-insets: 0,1,1;");
            errorMessage+="*Date Field Is Empty.\n";
            error=true;
        }
        else
        {
            this.dobField.setStyle("");
        }

        if(this.changePasswordBox.isSelected())
        {
            if(this.oldPasswordField.getText().isBlank())
            {
                this.oldPasswordField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
                errorMessage+="*Old Password Field Is Empty.\n";
                error=true;
            }
            else if(!this.oldPasswordField.getText().equals(Stuff.user.getPassword()))
            {
                this.oldPasswordField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
                errorMessage+="*Old Password Is Not Correct.\n";
                error=true;
            }
            else
            {
                this.oldPasswordField.setStyle("");
            }

            if(this.newPasswordField.getText().isBlank())
            {
                this.newPasswordField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
                errorMessage+="*New Password Field Is Empty.\n";
                error=true;
            }

            else if(!this.isValidPassword(this.newPasswordField.getText()))
            {
                this.newPasswordField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
                errorMessage+="*New Password Is Not Valid:\n" +
                        " -your Password should not contain any space.\n" +
                        " -Password should contain at least one digit(0-9).\n" +
                        " -Password length should be between 8 to 18 characters.\n" +
                        " -Password should contain at least one lowercase letter(a-z).\n" +
                        " -Password should contain at least one uppercase letter(A-Z).\n" +
                        " -Password should contain at least one special character.\n";
                error=true;
            }
            else
            {
                this.newPasswordField.setStyle("");
            }

            if(this.confirmNewPasswordField.getText().isBlank())
            {
                this.confirmNewPasswordField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
                errorMessage+="*Confirm Password Field Is Empty.\n";
                error=true;
            }
            else if(!this.confirmNewPasswordField.getText().equals(this.newPasswordField.getText()))
            {
                this.confirmNewPasswordField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
                errorMessage+="*Passwords Didn't Match.\n";
                error=true;
            }
            else
            {
                this.confirmNewPasswordField.setStyle("");
            }
        }

        if(error)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(Main.mainStage);
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
        for (char c : string.toCharArray())
        {
            if (!Character.isDigit(c))
            {
                return false;
            }
        }
        return true;
    }

    private boolean isValidEmail(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);

        if (email == null)
        {
            return false;
        }

        return pat.matcher(email).matches();
    }

    private boolean isValidPassword(String password)
    {
        if (!((password.length() >= 8) && (password.length() <= 18)))
        {
            return false;
        }

        if (password.contains(" "))
        {
            return false;
        }

        if (true)
        {
            int count = 0;

            for (int i = 0; i <= 9; i++)
            {
                String str1 = Integer.toString(i);

                if (password.contains(str1))
                {
                    count = 1;
                }
            }
            if (count == 0)
            {
                return false;
            }
        }

        if (!(password.contains("@") || password.contains("#")
                || password.contains("!") || password.contains("~")
                || password.contains("$") || password.contains("%")
                || password.contains("^") || password.contains("&")
                || password.contains("*") || password.contains("(")
                || password.contains(")") || password.contains("-")
                || password.contains("+") || password.contains("/")
                || password.contains(":") || password.contains(".")
                || password.contains(", ") || password.contains("<")
                || password.contains(">") || password.contains("?")
                || password.contains("|")))
        {
            return false;
        }

        if (true)
        {
            int count = 0;

            for (int i = 65; i <= 90; i++)
            {
                char c = (char)i;

                String str1 = Character.toString(c);
                if (password.contains(str1))
                {
                    count = 1;
                }
            }
            if (count == 0)
            {
                return false;
            }
        }

        if (true)
        {
            int count = 0;

            for (int i = 90; i <= 122; i++)
            {
                char c = (char)i;
                String str1 = Character.toString(c);

                if (password.contains(str1))
                {
                    count = 1;
                }
            }

            if (count == 0)
            {
                return false;
            }
        }
        return true;
    }

    private void setFieldsData()
    {
        this.emailField.setText(Stuff.user.getEmail());
        this.ssnField.setText(Integer.toString(Stuff.user.getSSN()));
        this.countryField.setText(Stuff.user.getCountry());
        this.nameField.setText(Stuff.user.getName());
        this.dobField.setValue(Stuff.user.getDOB());
    }

    private void setSharedUserObject(String name,int SSN,LocalDate DOB,String country,String email,String password,String picPath)
    {
        Stuff.user.setName(name);
        Stuff.user.setSSN(SSN);
        Stuff.user.setDOB(DOB);
        Stuff.user.setCountry(country);
        Stuff.user.setEmail(email);
        Stuff.user.setPicPath(picPath);
        Stuff.user.setPassword(password);
    }

    @FXML
    void reset()
    {
        this.changePasswordBox.setSelected(false);
        this.oldPasswordField.clear();
        this.newPasswordField.clear();
        this.confirmNewPasswordField.clear();
        this.oldPasswordField.setEditable(false);
        this.newPasswordField.setEditable(false);
        this.confirmNewPasswordField.setEditable(false);

        this.confirmNewPasswordField.setStyle("");
        this.newPasswordField.setStyle("");
        this.oldPasswordField.setStyle("");
        this.emailField.setStyle("");
        this.ssnField.setStyle("");
        this.countryField.setStyle("");
        this.nameField.setStyle("");
        this.dobField.setStyle("");

        this.setFieldsData();
    }
}