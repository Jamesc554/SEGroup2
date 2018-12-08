package library;
import java.awt.image.SampleModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import resources.*;
import user.Librarian;
import user.User;
@SuppressWarnings("Duplicates")

/**
 * This class implements all methods required for library operations.
 * @author Dominik R Wojtasiewicz
 * @since 18/11/2018
 */

public class Library {
	private static User currentUser; //Stores the currently logged-in user.
	private static String searchScreenText = "";

	/**
	 * This methods starts the library. Library is a static method so we can only
	 * have one at a time.
	 */
	public static void start() {
		LibraryResources.start();
	}

	public static void onLogin(String username) {
		Library.setLoggedInUser(Library.getUser(username));
	}
	private static void addFine(String username, int fineAmount) {
		Library.getUser(username).addAccountBalance(fineAmount);
		Library.getUser(username).addTransaction("Library", fineAmount);
	}
	/**
	 * This methods adds a new book to the library.
	 * @param year	Year published.
	 * @param title Book title.
	 * @param thumbnailImg Path to the image of the book.
	 * @param uniqueID ID of the book.
	 * @param author The author of the book.
	 * @param genre The genre of the book.
	 * @param isbn ISBN of the book.
	 * @param publisher The name of the published.
	 * @param lang Language of the book.
	 */
	public static void addBook(String year,String title, String thumbnailImg, String uniqueID,
						   String author, String genre, String isbn, String publisher, ArrayList<String> lang,
							   Integer noOfCopies, ArrayList<String> loanDuration, List<List<BorrowHistoryData>> borrowHistory,
							   List<BorrowHistoryData> currentBorrowData) {
		LibraryResources.addBook(new Book(year, title, thumbnailImg, uniqueID, author, genre, isbn, publisher,
				lang, noOfCopies, loanDuration, borrowHistory, currentBorrowData));
	}

	/**
	 * This methods adds a new DVD to the library.
	 * @param year	Year released .
	 * @param title Book title.
	 * @param thumbnailImg Path to the image of the DVD.
	 * @param uniqueID ID of the DVD.
	 * @param director The name of the director.
	 * @param runtime The run time of the movie.
	 * @param language The language of the movie.
	 * @param subLang The subtitles language of the DVD.
	 */
	public static void addDVD(String year, String title, String thumbnailImg, String uniqueID,
						 String director, String runtime, String language, ArrayList<String> subLang, Integer noOfCopies,
							  ArrayList<String> loanDuration, List<List<BorrowHistoryData>> borrowHistory,
							  List<BorrowHistoryData> currentBorrowData) {
	    LibraryResources.addDVD(new DVD(year, title, thumbnailImg, subLang, director, runtime, language, uniqueID,
				noOfCopies, loanDuration, borrowHistory, currentBorrowData));
	}

	/**
	 * This methods adds a laptop to the library.
	 * @param year The year of release.
	 * @param title The name of the laptop.
	 * @param thumbnailImageRef Img of the laptop.
	 * @param uniqueID The id of the laptop.
	 * @param manufacturer The manufacture of the laptop.
	 * @param model The model of the laptop.
	 * @param operatingSys The operating system of the laptop.
	 */
	public static void addLaptop(String year, String title, String thumbnailImageRef, String uniqueID,
							  String manufacturer, String model,  String operatingSys, Integer noOfCopies,
								 ArrayList<String> loanDuration, List<List<BorrowHistoryData>> borrowHistory,
								 List<BorrowHistoryData> currentBorrowData) {
	    LibraryResources.addLaptop(new Laptop(year, title, thumbnailImageRef, uniqueID, manufacturer, model, operatingSys,
				noOfCopies, loanDuration, borrowHistory, currentBorrowData));
	}

	/**
	 * This methods adds a user to the library.
	 * @param userName the username.
	 * @param firstName first name.
	 * @param lastName last name.
	 * @param mobileNumber mobile number.
	 * @param firstLineAddress first line of address.
	 * @param secondLineAddress second line of address.
	 * @param postCode post code.
	 * @param townName name of the town.
	 * @param accountBalance account balance
	 * @param profImage path to the profile image of the user.
	 */
	public static void addUser(String userName, String firstName, String lastName, String mobileNumber, String firstLineAddress,
						  String secondLineAddress, String postCode, String townName, int accountBalance, String profImage) {
		LibraryResources.addUser(new User(userName, firstName, lastName, mobileNumber, firstLineAddress,
                secondLineAddress, postCode, townName, accountBalance, profImage));
	}

	/**
	 * This methods adds a librarian to the library.
	 * @param userName the username.
	 * @param firstName first name.
	 * @param lastName last name.
	 * @param mobileNumber mobile number.
	 * @param firstLineAddress first line of address.
	 * @param secondLineAddress second line of address.
	 * @param postCode post code.
	 * @param townName name of the town.
	 * @param accountBalance account balance
	 * @param profImage path to the profile image of the librarian.
	 * @param empDay the day of employment start.
	 * @param empMonth the month of employment start.
	 * @param empYear the year of employment start.
	 * @param staffNumber staff number.
	 * @param noOfEmploys number of employs.
	 */
	public static void addLibrarian(String userName, String firstName, String lastName, String mobileNumber, String firstLineAddress,
								  String secondLineAddress, String postCode, String townName, int accountBalance, String profImage,
								  int empDay, int empMonth, int empYear, String staffNumber, int noOfEmploys) {
		LibraryResources.addUser(new Librarian(userName, firstName, lastName, mobileNumber, firstLineAddress,
                secondLineAddress, postCode, townName, accountBalance, profImage, empDay, empMonth, empYear, staffNumber,
                noOfEmploys));
	}

	/**
	 * Get's resource when the id of sed resource is known. Used for all resources.
	 * @param id String of ID of the resource.
	 * @return Resource object.
	 */
	public static Resource getResource(String id) {
		id = id.split("-")[0];
		String resourceType = id.substring(0, 1);
		switch (resourceType.toLowerCase()) {
			case "l":
				return LibraryResources.getLaptop(id);
			case "d":
				return LibraryResources.getDVD(id);
			case "b":
				return LibraryResources.getBook(id);
			default:
				return null;
		}
	}
	/**
	 * Gets the user based on username.
	 * @param username String username.
	 * @return user object.
	 */
	public static User getUser(String username) {
		return LibraryResources.getUser(username);
	}
	/**
	 * Adds balance to the user account.
	 * @param amount amount to increase the balance.
	 * @param username the username to modify.
	 */
	public static void addBalance(int amount, String username) {
		getUser(username).addAccountBalance(amount);
	}

	/**
	 * Removes balance from account.
	 * @param amount amount to be removed.
	 * @param username username of the user.
	 */
	public static void subtractBalance (int amount, String username) {
		if (amount <= 0) {
			throw new IllegalArgumentException("Cannot subtract negative or null amount");
		} else if (amount > getUser(username).getAccountBalanceDouble()) {
			throw new IllegalArgumentException("Amount superior to account balance");
		}
		getUser(username).subtractAccountBalance(amount);
		getUser(username).addTransaction(currentUser.getUserName(), amount);
	}

	/**
	 * Returns current date and time in the following format DD-MM-YYYY HH-MM-SS
	 * @return String.
	 */
	public static String getCurrentDateTime() {
		SimpleDateFormat dataFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		return dataFormat.format(new Date());
	}

	public static void setSearchStringText(String s) {
		searchScreenText = s;
	}

	public static String getSearchScreenText() {
		return searchScreenText;
	}

	/**
	 * Loans resource to the user.
	 * @param username Username of the person borrowing a resource.
	 * @param resourceID The id of the resource.
	 */
	public static void loanResource(String username, String resourceID) {
		getUser(username).loanResource(resourceID);
		String[] resInfo = resourceID.split("-");
		Resource r = Library.getResource(resInfo[0]);
		r.loanResource(resInfo[1], username);
	}

	/**
	 * Returns resource to the library.
	 * @param username Username
	 * @param resourceID ID of the resource.
	 */
	public static void returnResource(String username, String resourceID) {
		if(chekcCopyOverdue(resourceID)){
			Library.addFine(username, Library.calculateFine(resourceID));
		}
		getUser(username).returnResource(resourceID);
	}

	/**
	 * This removes a resource from library.
	 * @param id Unique ID of the resource.
	 */
	public static void removeResource(String id) {
        LibraryResources.removeResource(id);
    }

	/**
	 * Remove user from the library.
	 * @param username String
	 */
	public static void removeUser(String username) {
	    LibraryResources.removeUser(username);
	}

	/**
	 * Checks if user exists.
	 * @param username Username of the user
	 * @return Boolean. True if exists False if not.
	 */
	public static boolean checkForUser(String username) {
		return LibraryResources.checkIfValidUsername(username);
	}

	/**
	 * Returns all books in the library.
	 * @return ArrayList<Book> of all books.
	 */
	public static ArrayList<Book> getAllBooks() {
		return LibraryResources.getListOfBooks();
	}
	/**
	 * Returns all Laptops in the library.
	 * @return ArrayList<Laptop> of all books.
	 */
	public static ArrayList<Laptop> getAllLaptops() {
		return LibraryResources.getListOfLaptops();
	}
	/**
	 * Returns all DVD in the library.
	 * @return ArrayList<DVD> of all books.
	 */
	public static ArrayList<DVD> getAllDVD() {
		return LibraryResources.getListOfDVD();
	}

	/**
	 * Returns all users in the library.
	 * @return ArrayList of all books.
	 */
	public static ArrayList<User> getAllUsers() {
		return LibraryResources.getAllUsers();
	}

	/**
	 * Returns all librarians in the library.
	 * @return ArrayList
	 */
	public static ArrayList<Librarian> getAllLibrarians() {
		return LibraryResources.getAllLibrarians();
	}

	/**
	 * Sets the current logged in user.
	 * @param user the user object.
	 */
	public static void setLoggedInUser(User user) {
		currentUser = user;
	}

	/**
	 * Gets the currently logged in user.
	 * @return User object.
	 */
	public static User getCurrentLoggedInUser() {
		return currentUser;
	}

	/**
	 * Checks if the logged in user is of class Librarian
	 * @return True if current user is a Librarian
	 */
	public static boolean currentUserIsLibrarian() {
		return currentUser instanceof Librarian;
	}

	/**
	 * Changes address of the user.
	 * @param username The username of the user for which the address should be changed.
	 * @param firstLine The first line of address.
	 * @param secondLine Second line of address.
	 */
	public static void changeAddress(String username, String firstLine, String secondLine) {
	    User u = getUser(username);
	    u.setFirstLineAddress(firstLine);
	    u.setSecondLineAddress(secondLine);
    }

	/**
	 * Changes the post code of the user.
	 * @param username The username of the user for which the address should be changed.
	 * @param postCode
	 */
	public static void changePoctCode(String username, String postCode) {
	    getUser(username).setPostCode(postCode);
    }

	/**
	 * Changes the post code of the user.
	 * @param username The username of the user for which the address should be changed.
	 * @param townName The new town name.
	 */
	public static void changeTownName(String username, String townName) {
	    getUser(username).setTownName(townName);
    }

	/**
	 * Changes the post code of the user.
	 * @param username The username of the user for which the address should be changed.
	 * @param phoneNumber The new phone number.
	 */
	public static void changePhoneNumber(String username, String phoneNumber) {
	    getUser(username).setMobileNumber(phoneNumber);
    }

	/**
	 * Changes the last name of the user.
	 * @param username The username of which data will be updated.
	 * @param lastname The new last name.
	 */
	public static void changeLastName(String username, String lastname) {
	    getUser(username).setLastName(lastname);
    }

	/**
	 * Changes the profile img of the user.
	 * @param username The username of the user for which the address should be changed.
	 * @param path the new path of the profile image.
	 */
	public static void changeImage(String username, String path ) {
	    getUser(username).setProfImage(path);
    }

	/**
	 * Allows users to request a book that is not available.
	 * @param id of resource to be requested
	 */
	public static void requestResource(String id) {
		currentUser.requestResource(id); // Add it to the user
		Resource requestedResource = getResource(id); // Get the resource
		requestedResource.addUserToRequestQueue(currentUser);
		requestedResource.requestReturn(requestedResource.getCopyWithEarlestReturn());
	}

	/**
	 * Returns all requested books of the user currently logged in.
	 * @return ArrayList<String>
	 */
	public static ArrayList<String> getAllrequestedResource() {
		return currentUser.getAllRequested();
	}

	/**
	 * Returns all reserved items of the user currently logged in.
	 * @return ArrayList<String>
	 */
	public static ArrayList<String> getAllReservedResources() {
		return currentUser.getAllReserved();
	}

	public static Boolean chekcCopyOverdue(String id) {
		return Library.getResource(id).checkIfOverdue(Integer.valueOf(id.split("-")[1]));
	}

	/**
	 * Returns a list of all overdue resource of a user.
	 * @param username String of the user
	 * @return ArrayList
	 */
	public static ArrayList<String> checkForOverDue(String username) {
		ArrayList<String> overDue = new ArrayList<>();
		ArrayList<String> list = Library.getUser(username).getCurrentlyBorrowedResources();
		for (String s : list) {
			if (Library.getResource(s).checkIfOverdue(Integer.valueOf(s.split("-")[1]))) {
				overDue.add(s);
			}
		}
		return overDue;
	}

	/**
	 * Calculates a fine for a copy of resource.
	 * @param copyID String ID of resource to be checked.
	 * @return int amount fine.
	 */
	public static int calculateFine(String copyID) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date currentDate = new Date();
		Date dateToBeReturned = new Date();

		double fineAmount;

		try {
			currentDate = sdf.parse(Library.getCurrentDateTime());
		} catch (ParseException e) {
			System.out.println("Library403 calculate fine parse 1 ");
		}

		BorrowHistoryData r = Library.getResource(copyID.split("-")[0]).getCopyInfo(
				Integer.valueOf(copyID.split("-")[1])).getCurrentInfo();

		try {
			dateToBeReturned = sdf.parse(r.getDateRequestedReturn());
		} catch (ParseException e) {
			System.out.println("Library411 calculate fine parse 2 ");
		}
		long noOfDays = (currentDate.getTime() - dateToBeReturned.getTime()) / (1000 * 60 * 60 * 24);
		if (noOfDays == 0) {
			noOfDays = 1;
		}
		fineAmount = noOfDays * Library.getResource(copyID.split("-")[0]).getFineAmount();
		if (fineAmount > Library.getResource(copyID.split("-")[0]).getMaxFine()) {
			fineAmount = Library.getResource(copyID.split("-")[0]).getMaxFine();
		}
		return (int) Math.round(fineAmount);
	}

	/**
	 * Find's all overdue resources.
	 * @return ArrayList.
	 */
	public static ArrayList<String> findAllOverdue() {
		ArrayList<User> users = Library.getAllUsers();
		ArrayList<String> allOverDue = new ArrayList<>();

		for(User u : users) {
			allOverDue.addAll(checkForOverDue(u.getUserName()));
		}
		return allOverDue;
	}

	/**
	 * Updates a book's properties.
	 * @param id the book resource unique ID
	 * @param title the updated book title
	 * @param year the updated book year
	 * @param author the updated book author
	 * @param publisher the updated book publisher
	 * @param genre the updated book genre
	 * @param isbn the updated book isbn
	 * @param languages the updated book available languages
	 */
	public static void editBook(String id, String title, String year, String author, String publisher, String genre,
								String isbn, ArrayList<String> languages) {
		LibraryResources.getBook(id).setTitle(title);
		LibraryResources.getBook(id).setYear(year);
		LibraryResources.getBook(id).setAuthor(author);
		LibraryResources.getBook(id).setPublisher(publisher);
		LibraryResources.getBook(id).setGenre(genre);
		LibraryResources.getBook(id).setIsbn(isbn);
		LibraryResources.getBook(id).setLanguages(languages);
	}

	/**
	 * Updates a DVD's properties
	 * @param id the DVD resource unique ID
	 * @param title the updated DVD title
	 * @param year the updated DVD year
	 * @param director the updated DVD director
	 * @param runtime the updated DVD runtime
	 * @param language the updated DVD language
	 * @param subs the updated DVD available subtitle languages
	 */
	public static void editDVD(String id, String title, String year, String director, String runtime, String language,
							   ArrayList<String> subs) {
		LibraryResources.getDVD(id).setTitle(title);
		LibraryResources.getDVD(id).setYear(year);
		LibraryResources.getDVD(id).setDirector(director);
		LibraryResources.getDVD(id).setRuntime(runtime);
		LibraryResources.getDVD(id).setLanguage(language);
		LibraryResources.getDVD(id).setSubLang(subs);
	}

	/**
	 * Updates a Laptop's properties
	 * @param id the Laptop resource unique ID
	 * @param title the updated Laptop title
	 * @param year the updated Laptop year
	 * @param manufacturer the updated Laptop manufacturer
	 * @param model the updated Laptop model
	 * @param os the updated Laptop operating system
	 */
	public static void editLaptop(String id, String title, String year, String manufacturer, String model, String os) {
		LibraryResources.getLaptop(id).setTitle(title);
		LibraryResources.getLaptop(id).setYear(year);
		LibraryResources.getLaptop(id).setManufacturer(manufacturer);
		LibraryResources.getLaptop(id).setModel(model);
		LibraryResources.getLaptop(id).setOperatingSys(os);
	}
}
