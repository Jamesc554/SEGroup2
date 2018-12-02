package screen;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import library.Library;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class represents the Issue Desk, a screen only available to Librarians to authorise payments and loans.
 * @author Etienne Badoche
 * @version 1.0
 */
public class IssueDeskScreen extends Screen implements Initializable {

    @FXML
    private TextField loanUsername;
    @FXML
    private TextField loanResourceId;
    @FXML
    private Button loanBtn;
    @FXML
    private Label loanUserError;
    @FXML
    private Label loanResourceError;
    @FXML
    private Label loanSuccess;
    @FXML
    private TextField paymentUsername;
    @FXML
    private TextField paymentAmount;
    @FXML
    private Button paymentBtn;
    @FXML
    private Label paymentUserError;
    @FXML
    private Label paymentAmountError;
    @FXML
    private Label paymentSuccess;
    @FXML
    private TextField userUsername;
    @FXML
    private TextField userFirstName;
    @FXML
    private TextField userLastName;
    @FXML
    private TextField userMobile;
    @FXML
    private TextField userAddr1;
    @FXML
    private TextField userAddr2;
    @FXML
    private TextField userPstCd;
    @FXML
    private TextField userTown;
    @FXML
    private Button createUserBtn;
    @FXML
    private Label userUsernameError;
    @FXML
    private Label userError;
    @FXML
    private Label userSuccess;

    @Override
    public void start() {
        Pane root;
        try {
            root = FXMLLoader.load(getClass().getResource("fxml/IssueDesk.fxml"));
            ScreenManager.setCurrentScene(new Scene(root, 1280, 720));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(Library.getCurrentLoggedInUser().getProfImage()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //TODO: Change to only librarians once librarian is added
        if (!Library.currentUserIsLibrarian()) {
            issueDeskBtn.setVisible(true);
        }

        userIcon.setImage(SwingFXUtils.toFXImage(img, null));
        usernameText.setText(Library.getCurrentLoggedInUser().getUserName());
    }

    /**
     * Event handling to process payments.
     * @param e the JavaFX event event
     */
    @FXML
    private void paymentButton(Event e) {
        String user = paymentUsername.getText();

        //Reset all error/success labels
        paymentAmountError.setVisible(false);
        paymentUserError.setVisible(false);
        paymentSuccess.setVisible(false);

        if (Library.checkForUser(user)) {
            try {
                int balance = Integer.parseInt(paymentAmount.getText()) * 100;  //Convert pounds to pence
                Library.subtractBalance(balance, paymentUsername.getText());
                paymentSuccess.setVisible(true);

            } catch (IllegalArgumentException ex) {
                paymentAmountError.setVisible(true);
            }
        } else {
            paymentUserError.setVisible(true);
        }
    }

    /**
     * Event handling to process loans.
     * @param e the JavaFX event
     */
    @FXML
    private void loanButton(Event e) {
        String user = loanUsername.getText();
        String rID = loanResourceId.getText();

        //Reset all error/success labels
        loanUserError.setVisible(false);
        loanResourceError.setVisible(false);
        loanSuccess.setVisible(false);

        if (Library.checkForUser(user)) {
            if (Library.getResource(rID) != null) {
                Library.loanResource(user, rID);
                loanSuccess.setVisible(true);
            } else {
                loanResourceError.setVisible(true);
            }
        } else {
            loanUserError.setVisible(true);
        }
    }

    /**
     * Event handling to create a new User
     * @param e the JavaFX event
     */
    @FXML
    private void createUserButton(Event e) {
        String username = userUsername.getText();
        String firstName = userFirstName.getText();
        String lastName = userLastName.getText();
        String mobileNum = userMobile.getText();
        String address1 = userAddr1.getText();
        String address2 = userAddr2.getText();
        String postCode = userPstCd.getText();
        String town = userTown.getText();

        //Reset all error/success labels
        userUsernameError.setVisible(false);
        userError.setVisible(false);
        userSuccess.setVisible(false);

        //Check if username not already used
        if (!Library.checkForUser(username)) {
            //Check all required fields have inputs
            if (username.equals("") || firstName.equals("") || lastName.equals("") || mobileNum.equals("")
                || address1.equals("") || address2.equals("") || postCode.equals("") || town.equals("")) {
                userError.setVisible(true);
            } else {
                Library.addUser(username, firstName, lastName, mobileNum, address1, address2, postCode, town,
                        0, "./data/images/testUser/testImg32.png");
                userSuccess.setVisible(true);
            }
        } else {
            userUsernameError.setVisible(true);
        }
    }

    /**
     * Event handling for Resource creation.
     */
    public void createResource() {

    }
}
