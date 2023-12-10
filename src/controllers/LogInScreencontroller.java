package controllers;

import classes.Main;
import classes.Stuff;
import classes.User;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import oracle.jdbc.driver.OracleDriver;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class LogInScreencontroller implements Initializable
{
    private String imgPath=null;
    private SimpleBooleanProperty showPassword ;
    private  Label message;
    private Label label;
    static Stage loadingStage;

    @FXML
    private AnchorPane parentContainer;
    @FXML
    private AnchorPane anchorpaneleft;
    @FXML
    private Label welcomelabel;
    @FXML
    private Label hellofriendlabel;
    @FXML
    private Label missedlabel;
    @FXML
    private Label enjoylabel;
    @FXML
    private Button signupbutton;
    @FXML
    private Label personaldetails;
    @FXML
    private Label startj;
    @FXML
    private Button signinbutton;
    @FXML
    private AnchorPane anchorpaneright;
    @FXML
    private Label creataccountlabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label signupEmailLabel;
    @FXML
    private Label signupPasswordLabel;
    @FXML
    private Label oneclick;
    @FXML
    private Label confirmPasswordLabel;
    @FXML
    private TextField nameField;
    @FXML
    private TextField ssnField;
    @FXML
    private TextField countryField;
    @FXML
    private TextField signUpEmailField;
    @FXML
    private PasswordField signUpPasswordField;
    @FXML
    private PasswordField signUpConfirmPasswordField;
    @FXML
    private DatePicker dobField;
    @FXML
    private Label uploadPhotoLabel;
    @FXML
    private Button uploadButton;
    @FXML
    private Label ssnLabel;
    @FXML
    private Label countryLabel;
    @FXML
    private Label dobLabel;
    @FXML
    private Button signinbuttonenter;
    @FXML
    private Button signupbuttonenter;
    @FXML
    private Label chooseImgLabel;
    @FXML
    private Label signinpass;
    @FXML
    private Label signinemail;
    @FXML
    private TextField signInEmailField;
    @FXML
    private PasswordField signInPasswordField;
    @FXML
    private Button forgetpassword;
    @FXML
    private Rectangle rectangle;
    @FXML
    private Tooltip tooltip;
    @FXML
    private CheckBox checkBox;
    @FXML
    private ImageView imgView;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.parentContainer.setOpacity(0);
        FadeTransition fadeTransition=new FadeTransition();
        fadeTransition.setNode(this.parentContainer);
        fadeTransition.setDuration(Duration.millis(1200));
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();

        this.setup();

        this.showPassword = new SimpleBooleanProperty();
        this.tooltip = new Tooltip();
        this.message = new Label("");
        this.label = new Label("Password");

        this.tooltip.setShowDelay(Duration.ZERO);
        this.tooltip.setAutoHide(false);
        this.tooltip.setMinWidth(50);

        this.showPassword.addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) ->
        {
            if(newValue)
            {
                showPassword();
            }
            else
            {
                hidePassword();
            }
        });

        this.signInPasswordField.setOnKeyTyped(e ->
        {
            if ( showPassword.get() )
            {
                showPassword();
            }
        });

        this.showPassword.bind(this.checkBox.selectedProperty());
    }

    @FXML
    private void btn()
    {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.7));
        slide.setNode(this.anchorpaneleft);
        slide.setToX(400);
        slide.play();
        this.anchorpaneright.setTranslateX(-400);

        this.signinbutton.setVisible(false);
        this.signupbutton.setVisible(true);

        this.signinbuttonenter.setVisible(true);
        this.signupbuttonenter.setVisible(false);

        this.signinemail.setVisible(true);
        this.signinpass.setVisible(true);
        this.signInEmailField.setVisible(true);
        this.signInPasswordField.setVisible(true);
        this.forgetpassword.setVisible(true);
        this.imgView.setVisible(true);
        this.checkBox.setVisible(true);

        this.oneclick.setVisible(true);
        this.hellofriendlabel.setVisible(true);
        this.missedlabel.setVisible(true);
        this.enjoylabel.setVisible(true);

        this.welcomelabel.setVisible(false);
        this.personaldetails.setVisible(false);
        this.startj.setVisible(false);
        this.creataccountlabel.setVisible(false);

        this.nameLabel.setVisible(false);
        this.nameField.setVisible(false);

        this.ssnLabel.setVisible(false);
        this.ssnField.setVisible(false);

        this.countryLabel.setVisible(false);
        this.countryField.setVisible(false);

        this.dobLabel.setVisible(false);
        this.dobField.setVisible(false);

        this.signupEmailLabel.setVisible(false);
        this.signUpEmailField.setVisible(false);

        this.signupPasswordLabel.setVisible(false);
        this.signUpPasswordField.setVisible(false);

        this.confirmPasswordLabel.setVisible(false);
        this.signUpConfirmPasswordField.setVisible(false);

        this.uploadPhotoLabel.setVisible(false);
        this.chooseImgLabel.setVisible(false);
        this.uploadButton.setVisible(false);
    }

    @FXML
    private void btn2()
    {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.7));
        slide.setNode(anchorpaneleft);
        slide.setToX(0);
        slide.play();
        anchorpaneright.setTranslateX(0);

        this.signinbutton.setVisible(true);
        this.signupbutton.setVisible(false);

        this.signinbuttonenter.setVisible(false);
        this.signupbuttonenter.setVisible(true);

        this.signinemail.setVisible(false);
        this.signinpass.setVisible(false);
        this.signInEmailField.setVisible(false);
        this.signInPasswordField.setVisible(false);
        this.forgetpassword.setVisible(false);
        this.imgView.setVisible(false);
        this.checkBox.setVisible(false);

        this.oneclick.setVisible(false);
        this.hellofriendlabel.setVisible(false);
        this.missedlabel.setVisible(false);
        this.enjoylabel.setVisible(false);

        this.welcomelabel.setVisible(true);
        this.personaldetails.setVisible(true);
        this.startj.setVisible(true);
        this.creataccountlabel.setVisible(true);

        this.nameLabel.setVisible(true);
        this.nameField.setVisible(true);

        this.ssnLabel.setVisible(true);
        this.ssnField.setVisible(true);

        this.countryLabel.setVisible(true);
        this.countryField.setVisible(true);

        this.dobLabel.setVisible(true);
        this.dobField.setVisible(true);

        this.signupEmailLabel.setVisible(true);
        this.signUpEmailField.setVisible(true);

        this.signupPasswordLabel.setVisible(true);
        this.signUpPasswordField.setVisible(true);

        this.confirmPasswordLabel.setVisible(true);
        this.signUpConfirmPasswordField.setVisible(true);

        this.uploadPhotoLabel.setVisible(true);
        this.chooseImgLabel.setVisible(true);
        this.uploadButton.setVisible(true);
    }

    @FXML
    void signInEvent() throws SQLException
    {
        int SSN;
        LocalDate DOB;
        String name,country,email,password,picPath,tempDOB,recoveryCode,type;
        String errorMessage="";
        boolean error=false;

        if(this.signInEmailField.getText().isBlank())
        {
            error=true;
            errorMessage+="Email Field Is Empty\n";
            this.signInEmailField.setStyle("-fx-border-color:red; -fx-border-radius: 30;" +
                                            "-fx-border-insets: 0,1,1;");
        }
        else
        {
            this.signInEmailField.setStyle("");
        }

        if(this.signInPasswordField.getText().isBlank())
        {
            error=true;
            errorMessage+="Password Field Is Empty\n";
            this.signInPasswordField.setStyle("-fx-border-color:red; -fx-border-radius: 30;" +
                    "-fx-border-insets: 0,1,1;");
        }
        else
        {
            this.signInPasswordField.setStyle("");
        }

        if(error)
        {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Can't Log In");
            alert.setContentText(errorMessage);
            alert.initOwner(Main.mainStage);
            alert.show();
        }
        else
        {
            String screenURL;
            DriverManager.registerDriver((new OracleDriver()));
            String url="jdbc:oracle:thin:@localhost:1521:xe";
            Connection con = DriverManager.getConnection(url, "C##sundos_yaqeen", "11223344");
            String query="select *from users where email='"+this.signInEmailField.getText()+"'and password='"+this.signInPasswordField.getText()+"'";
            PreparedStatement ps=con.prepareStatement(query);
            ResultSet resultSet=ps.executeQuery();

            if(resultSet.next())
            {
                name=resultSet.getString(1);
                SSN=resultSet.getInt(2);
                email=resultSet.getString(3);
                password=resultSet.getString(4);
                picPath=resultSet.getString(5);
                type=resultSet.getString(6);
                country=resultSet.getString(7);
                tempDOB=resultSet.getString(8);
                DOB=LocalDate.of(Integer.parseInt(tempDOB.split(" ")[0].split("-")[0]),Integer.parseInt(tempDOB.split(" ")[0].split("-")[1]),Integer.parseInt(tempDOB.split(" ")[0].split("-")[2]));

                User user=new User(name,SSN,DOB,country,email,password,picPath,type);
                Stuff.user=user;

                screenURL=(Stuff.user.getType().equals("U")?"/fxml/CityScreen.fxml":"/fxml/adminScreen.fxml");
                this.clearFields();
                this.switchToMainScreen(screenURL);
            }
            else
            {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Can't Log In");
                alert.setContentText("Wrong Email Or Password");
                alert.initOwner(Main.mainStage);
                alert.show();
            }
            con.close();
        }
    }

    @FXML
    void signUpEvent()
    {
        int ssn;
        String name,country,email,dateOfBirth,password,type;
        LocalDate localDate;

        if(this.checkIfDataIsValid())
        {
            name=this.nameField.getText();
            ssn=Integer.parseInt(this.ssnField.getText());
            country=this.countryField.getText();
            email=this.signUpEmailField.getText();
            password=this.signUpPasswordField.getText();
            localDate=this.dobField.getValue();
            dateOfBirth=""+localDate.getYear()+""+"-"+""+localDate.getMonthValue()+""+"-"+""+localDate.getDayOfMonth()+"";
            type=(email.equals("yaqeenyaseen6@gmail.com")||email.equals("sundossaifi99@gmail.com"))?"A":"U";

            try
            {
                DriverManager.registerDriver((new OracleDriver()));
                String url="jdbc:oracle:thin:@localhost:1521:xe";
                Connection con = DriverManager.getConnection(url, "C##sundos_yaqeen", "11223344");
                String query="insert into users values('"+name+"',"+ssn+",'"+email+"','"+password+"','"+this.imgPath+"','"+type+"','"+country+"',TO_DATE('"+dateOfBirth+"','YYYY-MM-DD'))";
                PreparedStatement ps=con.prepareStatement(query);
                ps.executeQuery();

                Alert alert=new Alert(Alert.AlertType.INFORMATION);
                alert.initOwner(Main.mainStage);
                alert.setHeaderText("Done!");
                alert.setContentText("Your Account Created Successfully.");
                alert.show();

                this.clearFields();
                this.btn();

                con.close();
            }
            catch(SQLIntegrityConstraintViolationException sqlIntegrityConstraintViolationException)
            {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.initOwner(Main.mainStage);
                alert.setHeaderText("This User Already Exist!");
                alert.setContentText("Maybe you've entered a pre-registered SSN, Email.");
                alert.showAndWait();
                this.clearFields();
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
    void forgetPasswordEvent()
    {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Password Recovery");
        dialog.setHeaderText("Enter Your Email To Retrieve Your Password.");
        dialog.initOwner(Main.mainStage);
        Optional<String> result = dialog.showAndWait();
        String entered =null;
        String password=null;

        if (result.isPresent())
        {
            entered = result.get();
            if(this.isValidEmail(entered))
            {
                try
                {
                    DriverManager.registerDriver((new OracleDriver()));
                    String url="jdbc:oracle:thin:@localhost:1521:xe";
                    Connection con = DriverManager.getConnection(url, "C##sundos_yaqeen", "11223344");
                    String query="select *from users where email='"+entered+"'";
                    PreparedStatement ps=con.prepareStatement(query);
                    ResultSet resultSet=ps.executeQuery();

                    if(resultSet.next())
                    {
                        this.sendMail(resultSet.getString(3),resultSet.getString(4));
                        Alert alert=new Alert(Alert.AlertType.INFORMATION);
                        alert.initOwner(Main.mainStage);
                        alert.setHeaderText("Done!");
                        alert.setContentText("Please Check Your Email");
                        alert.show();
                    }
                    else
                    {
                        Alert alert=new Alert(Alert.AlertType.ERROR);
                        alert.initOwner(Main.mainStage);
                        alert.setHeaderText("Not Found!");
                        alert.setContentText("Theres No Registered Email Matches The Entered Email!");
                        alert.show();
                    }

                    con.close();
                }
                catch (SQLException sqlException)
                {
                    Alert alert=new Alert(Alert.AlertType.ERROR);
                    alert.initOwner(Main.mainStage);
                    alert.setHeaderText("Data-Base Connection Error!");
                    alert.setContentText(sqlException.getMessage());
                    alert.show();
                }
                catch (Exception exception)
                {
                    exception.printStackTrace();
                }
            }
            else
            {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.initOwner(Main.mainStage);
                alert.setHeaderText("Invalid Email");
                alert.setContentText("Enter A Valid Email");
                alert.show();
            }
        }
        else
        {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.initOwner(Main.mainStage);
            alert.setHeaderText("Email Field Is Empty");
            alert.setContentText("Enter A Valid Email");
            alert.showAndWait();
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

        File file = fileChooser.showOpenDialog(Main.mainStage);

        if (file != null)
        {
            this.imgPath=file.toString();
            this.chooseImgLabel.setText(file.getName());
        }
    }

    private void setup()
    {
        this.checkBox.setVisible(false);
        this.imgView.setVisible(false);
        this.hellofriendlabel.setVisible(false);
        this.missedlabel.setVisible(false);
        this.enjoylabel.setVisible(false);
        this.signupbutton.setVisible(false);
        this.oneclick.setVisible(false);
        this.signinbuttonenter.setVisible(false);
        this.signinemail.setVisible(false);
        this.signInEmailField.setVisible(false);
        this.signInPasswordField.setVisible(false);
        this.signinpass.setVisible(false);
        this.forgetpassword.setVisible(false);
        this.signinemail.setVisible(false);
        this.signinpass.setVisible(false);
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

        if(this.signUpEmailField.getText().isBlank())
        {
            this.signUpEmailField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Email Field Is Empty.\n";
            error=true;
        }
        else if(!this.isValidEmail(this.signUpEmailField.getText()))
        {
            this.signUpEmailField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Email Address Is Not Valid.\n";
            error=true;
        }
        else
        {
            this.signUpEmailField.setStyle("");
        }

        if(this.signUpPasswordField.getText().isBlank())
        {
            this.signUpPasswordField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Password Field Is Empty.\n";
            error=true;
        }
        else if(!this.isValidPassword(this.signUpPasswordField.getText()))
        {
            this.signUpPasswordField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Password Is Not Valid:\n" +
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
            this.signUpPasswordField.setStyle("");
        }

        if(this.signUpConfirmPasswordField.getText().isBlank())
        {
            this.signUpConfirmPasswordField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Confirm Password Field Is Empty.\n";
            error=true;
        }
        else if(!this.signUpConfirmPasswordField.getText().equals(this.signUpPasswordField.getText()))
        {
            this.signUpConfirmPasswordField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Passwords Didn't Match.\n";
            error=true;
        }
        else
        {
            this.signUpConfirmPasswordField.setStyle("");
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

        if(this.imgPath==null)
        {
            this.uploadButton.setStyle("-fx-border-color: #FF0000; -fx-focus-color: #FF0000; -fx-border-radius: 30;" +
                        "-fx-border-insets: 0,1,1;");
            errorMessage+="*Please Upload Your Profile Picture.\n";
            error=true;
        }
        else
        {
            this.uploadButton.setStyle("");
        }

        if(error)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(this.parentContainer.getScene().getWindow());
            alert.setContentText(null);
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

    private boolean isValidEmail(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+"[a-zA-Z0-9_+&*-]+)*@"+"(?:[a-zA-Z0-9-]+\\.)+[a-z"+"A-Z]{2,7}$";

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

    private void clearFields()
    {
        this.signInEmailField.setText("");
        this.signInPasswordField.setText("");
        this.nameField.setText("");
        this.ssnField.setText("");
        this.countryField.setText("");
        this.signUpEmailField.setText("");
        this.signUpPasswordField.setText("");
        this.signUpConfirmPasswordField.setText("");
        this.dobField.setValue(null);
        this.chooseImgLabel.setText("Choose Img");
        this.hidePassword();
    }

    public void sendMail(String recipient,String userPassword) throws Exception
    {
        Properties properties = new Properties();

        //Enable authentication
        properties.put("mail.smtp.auth", "true");
        //Set TLS encryption enabled
        properties.put("mail.smtp.starttls.enable", "true");
        //Set SMTP host
        properties.put("mail.smtp.host", "smtp.gmail.com");
        //Set smtp port
        properties.put("mail.smtp.port", "587");

        //Your gmail address
        String myAccountEmail = "oneclick.pal@gmail.com";
        //Your gmail password
        String password = "oneclick@2021";

        //Create a session with account credentials
        Session session = Session.getInstance(properties, new Authenticator()
        {
            @Override
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(myAccountEmail, password);
            }
        });

        //Prepare email message
        Message message = prepareMessage(session, myAccountEmail, recipient,userPassword);

        //Send mail
        Transport.send(message);
    }

    private Message prepareMessage(Session session, String myAccountEmail, String recipient,String userPassword)
    {
        try
        {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("Password Recovery");
            String htmlCode = "<h1> One Click </h1> <br/> <p>Your Password Is: "+userPassword+"</p> <br/> <h2>Best Regards</h2>";
            message.setContent(htmlCode, "text/html");
            return message;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    private void switchToMainScreen(String screenURL)
    {
        try
        {
            Parent root= FXMLLoader.load(getClass().getResource("/fxml/LoadingAnimation.fxml"));
            Scene scene=new Scene(root);
            loadingStage=new Stage();
            scene.setFill(Color.TRANSPARENT);
            Image image=new Image("/images/icon.png");
            loadingStage.getIcons().add(image);
            loadingStage.initStyle(StageStyle.TRANSPARENT);
            loadingStage.setScene(scene);
            loadingStage.setWidth(413);
            loadingStage.setHeight(287);
            loadingStage.setResizable(false);
            this.anchorpaneleft.setOpacity(0.5);
            this.anchorpaneright.setOpacity(0.5);
            double centerXPosition = this.parentContainer.getScene().getWindow().getX() + this.parentContainer.getScene().getWindow().getWidth()/2d;
            double centerYPosition = this.parentContainer.getScene().getWindow().getY() + this.parentContainer.getScene().getWindow().getHeight()/2d;
            loadingStage.setOnShowing(ev -> loadingStage.hide());
            loadingStage.setOnShown(ev ->
            {
                loadingStage.setX(centerXPosition - loadingStage.getWidth()/2d);
                loadingStage.setY(centerYPosition - loadingStage.getHeight()/2d);
                loadingStage.show();
            });
            loadingStage.showAndWait();

            root= FXMLLoader.load(getClass().getResource(screenURL));
            scene=new Scene(root);
            Main.mainStage.setScene(scene);
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
    }

    private void showPassword()
    {
        Point2D p = this.signInPasswordField.localToScene(this.signInPasswordField.getBoundsInLocal().getMaxX(), this.signInPasswordField.getBoundsInLocal().getMaxY());
        tooltip.setText(this.signInPasswordField.getText());
        tooltip.show(this.signInPasswordField,
                p.getX() + Main.mainStage.getScene().getX() +  Main.mainStage.getX()-50,
                p.getY() + Main.mainStage.getScene().getY() +  Main.mainStage.getY()+5);
    }

    private void hidePassword()
    {
        tooltip.setText("");
        tooltip.hide();
    }
}