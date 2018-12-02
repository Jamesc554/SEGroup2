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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import library.Library;
import resources.Book;
import resources.DVD;
import resources.Laptop;
import resources.Resource;

/**
 * This class represents the Resource screen, which shows detailed information on a particular Resource.
 * @author Etienne Badoche, Peter Daish
 * @version 1.0
 */
public class ResourceScreen extends Screen implements Initializable{
		
	// TOP TOOL BAR - COMMON BETWEEN SCREENS - COPY FROM HERE - MAKE SURE THE IDs IN
	// SCENEBUILDER ARE OF THE SAME NAME AS THE VARIBLES HERE!!!!!

		@FXML
		private HBox bookHBox;
		
		@FXML
		private HBox dvdHBox;
		
		@FXML
		private HBox laptopHBox;
		
		//This classes specific attributes.
		
		@FXML
		private Button addCopyButton;
		
		@FXML
		private Button deleteCopyButton;
		
		@FXML
		private Button editResourceButton; //button which allows user to edit a resource's details.
		
		@FXML
		private TextField titleTextField;
		
		@FXML
		private TextField yearTextField;
		
		@FXML
		private TextField thumbnailTextField;
		
		@FXML
		private TextField authorTextField;
		
		@FXML
		private TextField genreTextField;
		
		
		
		private Object res = null; // this resource must be passed in to this class.
		
		@Override
		public void start() {
			Pane root;
			try {
				root = FXMLLoader.load(getClass().getResource("fxml/Resource.fxml"));
				ScreenManager.setCurrentScene(new Scene(root, 1280, 720));
				// setupEvents();
			} catch (IOException e) {
				// TODO Auto-generated catch block
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

			userIcon.setImage(SwingFXUtils.toFXImage(img, null));
			usernameText.setText(Library.getCurrentLoggedInUser().getUserName());
		}
    
    /**
     * Event handling for editing a Resource.
     */
    public void editResourceButton(Event e) {
    	//Edit the specific details of a resource i.e. Author, genre etc.
    	if(res instanceof Book) {
    		((Book) res).setTitle(titleTextField.getText());
    		((Book) res).setYear(yearTextField.getText());
    		((Book) res).setThumbnailImageRef(thumbnailTextField.getText());
    		((Book) res).setAuthor((authorTextField.getText()));
    		((Book) res).setGenre(genreTextField.getText());
    	} else if (res instanceof DVD) {
    		((DVD) res).setTitle(titleTextField.getText());
    		((DVD) res).setYear(yearTextField.getText());
    		((DVD) res).setThumbnailImageRef(thumbnailTextField.getText());
    	} else if (res instanceof Laptop) {
    		((Laptop) res).setTitle(titleTextField.getText());
    		((Laptop) res).setYear(yearTextField.getText());
    		((Laptop) res).setThumbnailImageRef(thumbnailTextField.getText());
    	}
    }
    
    /**
     * Adds a copy to current resource
     */
    public void addCopyButton(Event e) {
    	//if object is a "Book" then add copy to book's copy arraylist. Same for if DVD or Laptop.
    	if(res instanceof Book) {
    		((Book) res).addToCopies();
    	} else if (res instanceof DVD) {
    		((DVD) res).addToCopies();
    	} else if (res instanceof Laptop) {
    		((Laptop) res).addToCopies();
    	}	
    }
    
    /**
     * Deletes a copy of current resource
     */
    public void deleteCopyButton(Event e) {
    	//if object is a "Book" then add copy to book's copy arraylist. Same for if DVD or Laptop.
    	if(res instanceof Book) {
    		((Book) res).removeCopy();
    	} else if (res instanceof DVD) {
    		((DVD) res).removeCopy();
    	} else if (res instanceof Laptop) {
    		((Laptop) res).removeCopy();
    	}	
    }
}
