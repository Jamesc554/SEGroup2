package screen;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import library.Library;
import resources.Resource;

/**
 * 
 * @author Samuel Jankinson
 * @version 1.0
 */

public class AccountScreen extends Screen implements Initializable{
		@FXML
		private TextField searchBar;

		@FXML
		private Button searchBtn;

		@FXML
		private ImageView userIcon;

		@FXML
		private Text usernameText;

		@FXML
		private Button logoutBtn;

		@FXML
		private Button homeBtn;

		@FXML
		private Button accountBtn;

		@FXML
		private Button issueDeskBtn;

		@FXML
		private Button drawAppBtn;
		
		@FXML
		private Label usernameField;
		
		@FXML
		private Label nameField;
		
		@FXML
		private Label mobileNumberField;
		
		@FXML
		private Label balanceField;
		
		@FXML
		private TextArea addressField;
		
		@FXML
		private ImageView profileImageField;
		
		@FXML
		private Text fineText;
		
		@Override
		public void start() {
			Pane root;
			try {
				root = FXMLLoader.load(getClass().getResource("fxml/Account.fxml"));
				ScreenManager.setCurrentScene(new Scene(root, 1280, 720));
		        //setupEvents();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@FXML
		private void drawAppButton(Event event) {
			ScreenManager.changeScreen(new DrawApp());
		}
		
		@FXML
		private void searchButton(Event event) {
			ScreenManager.changeScreen(new SearchResultScreen());
		}
		
		@FXML
		private void logoutButton(Event event) {
			logout();
		}

		@FXML
		private void issueDeskButton(Event event) {
			ScreenManager.changeScreen(new IssueDeskScreen());
		}

		@Override
		public void initialize(URL location, ResourceBundle resources) {
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
}
