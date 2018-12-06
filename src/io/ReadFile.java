package io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import resources.Book;
import resources.DVD;
import resources.Laptop;
import user.Librarian;
import user.User;

/**
 * @author Samuel Jankinson
 */

public class ReadFile extends IO {
	private static FileReader file = null;
	private static BufferedReader reader = null;
	private static String currentLine = null;

	public static ArrayList<User> readUsers() {
		JSONParser parser = new JSONParser();
		JSONArray resourceArray = new JSONArray();
		JSONArray transactionArray = new JSONArray();
		JSONArray borrowHistoryArray = new JSONArray();
		ArrayList<User> userList = new ArrayList<>();
		try {
			file = new FileReader(IO.getUsersFilePath());
			reader = new BufferedReader(file);
			while ((currentLine = reader.readLine()) != null) {
				JSONObject object = (JSONObject) parser.parse(currentLine);
				User user = new User((String) object.get("username"), (String) object.get("firstName"),
						(String) object.get("lastName"), (String) object.get("mobileNumber"),
						(String) object.get("firstLineAddress"), (String) object.get("secondLineAddress"),
						(String) object.get("postCode"), (String) object.get("townName"),
						Double.parseDouble((String) object.get("accountBalance")), (String) object.get("imageAddress"));

				resourceArray = (JSONArray) object.get("resourceBorrow");
				if (resourceArray != null) {
					for (Object resource : resourceArray) {
						String stringResource = (String) resource;
						ArrayList<String> data = new ArrayList<String>();
						data.add(stringResource);
						user.setResourceCurrentlyBorrowed(data);
					}
				}

				transactionArray = (JSONArray) object.get("transactionHistory");
				if (transactionArray != null) {
					for (Object transactionInformation : transactionArray) {
						JSONArray transactionInformationArray = (JSONArray) transactionInformation;
						String[] data = new String[2];
						data[1] = (String) transactionInformationArray.get(0);
						data[2] = (String) transactionInformationArray.get(1);
						user.addToTransactionHistory(data);
					}
				}

				borrowHistoryArray = (JSONArray) object.get("borrowHistory");
				if (borrowHistoryArray != null) {
					for (Object borrowInformation : borrowHistoryArray) {
						JSONArray borrowInformationArray = (JSONArray) borrowInformation;
						String[] data = new String[2];
						data[1] = (String) borrowInformationArray.get(0);
						data[0] = (String) borrowInformationArray.get(1);
						user.addToBorrowHistory(data);
					}
				}

				// TODO: Currently Requested

				// TODO: Currently Reserved

				userList.add(user);
			}

			reader.close();
			file.close();
		} catch (FileNotFoundException e) {
			System.out.println("Cannot find " + IO.getUsersFilePath());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("ERROR reading file " + IO.getUsersFilePath());
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("ERROR parsing users JSON");
			e.printStackTrace();
		}
		return userList;
	}

	public static ArrayList<Librarian> readLibrarians() {
		JSONParser parser = new JSONParser();
		ArrayList<Librarian> librarianList = new ArrayList<>();

		try {
			file = new FileReader(IO.getLibrarianFilePath());
			reader = new BufferedReader(file);
			while ((currentLine = reader.readLine()) != null) {
				JSONObject object = (JSONObject) parser.parse(currentLine);
				Librarian librarian = new Librarian((String) object.get("username"), (String) object.get("firstName"),
						(String) object.get("lastName"),
						(String) object.get("mobileNumber"),
						(String) object.get("firstLineAddress"),
						(String) object.get("secondLineAddress"),
						(String) object.get("postCode"),
						(String) object.get("townName"),
						Double.parseDouble((String) object.get("accountBalance")),
						(String) object.get("imageAddress"),
						Integer.parseInt((String) object.get("empDay")),
						Integer.parseInt((String) object.get("empMonth")),
						Integer.parseInt((String) object.get("empYear")), (String) object.get("staffNumber"),
						Integer.parseInt((String) object.get("noOfEmploys")));
				librarianList.add(librarian);
			}

			reader.close();
			file.close();
		} catch (FileNotFoundException e) {
			System.out.println("Cannot find " + IO.getLibrarianFilePath());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("ERROR reading file " + IO.getLibrarianFilePath());
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("ERROR parsing users JSON");
			e.printStackTrace();
		}
		return librarianList;
	}

	// TODO: CHANGE TO ArrayList<Book>
	public static ArrayList<Book> readBooks() {
		JSONParser parser = new JSONParser();
		JSONArray languageArray = new JSONArray();
		JSONArray bookQueueArray = new JSONArray();
		JSONArray listOfLoanDur = new JSONArray();
		ArrayList<Book> bookList = new ArrayList<Book>();

		try {
			file = new FileReader(IO.getBookFilePath());
			reader = new BufferedReader(file);

			while ((currentLine = reader.readLine()) != null) {
				JSONObject object = (JSONObject) parser.parse(currentLine);

				String year = ((String) object.get("year"));
				String title = ((String) object.get("title"));
				String thumbnailImg = ((String) object.get("thumbnailImg"));
				String uniqueID = ((String) object.get("uniqueID"));
				String author = ((String) object.get("author"));
				String genre = ((String) object.get("genre"));
				String isbn = ((String) object.get("isbn"));
				String publisher = ((String) object.get("publisher"));

				int noOfCopies = Integer.parseInt((String) object.get("noOfCopies"));

				ArrayList<String> loanDurs = new ArrayList<String>();

				listOfLoanDur = (JSONArray) object.get("listOfLoanDur");
				if (listOfLoanDur != null) {
					for (Object loanDur : listOfLoanDur) {
						String loanDurString = (String) loanDur;
						loanDurs.add(loanDurString);
					}
				}

				Book bookToAdd = new Book(year, title, thumbnailImg, uniqueID, author, genre, isbn, publisher, null,
						noOfCopies, loanDurs, new ArrayList<>());

				languageArray = (JSONArray) object.get("languages");
				if (languageArray != null) {
					for (Object language : languageArray) {
						String stringLanguage = (String) language;
						bookToAdd.addLanguage(stringLanguage);
					}
				}

				// TODO: Make this work
				bookQueueArray = (JSONArray) object.get("bookQueue");
				String bookQueues = "";
				if (bookQueueArray != null) {
					for (Object bookQueue : bookQueueArray) {
						String stringBookQueue = (String) bookQueue;
						bookQueues = bookQueues + stringBookQueue + ",";
					}
				}

				bookList.add(bookToAdd);
			}

			reader.close();
			file.close();
		} catch (FileNotFoundException e) {
			System.out.println("Cannot find " + IO.getBookFilePath());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("ERROR reading file " + IO.getBookFilePath());
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("ERROR parsing users JSON");
			e.printStackTrace();
		}
		return bookList;
	}

	public static ArrayList<DVD> readDvds() {
		JSONParser parser = new JSONParser();
		JSONArray languageArray = new JSONArray();
		JSONArray dvdQueueArray = new JSONArray();
		JSONArray listOfLoanDur = new JSONArray();

		ArrayList<DVD> dvds = new ArrayList<DVD>();

		try {
			file = new FileReader(IO.getDvdFilePath());
			reader = new BufferedReader(file);

			while ((currentLine = reader.readLine()) != null) {
				JSONObject object = (JSONObject) parser.parse(currentLine);

				String year = ((String) object.get("year"));
				String title = ((String) object.get("title"));
				String thumbnailImageRef = ((String) object.get("thumbnailImg"));
				String uniqueID = ((String) object.get("uniqueID"));
				String director = ((String) object.get("director"));
				String runtime = ((String) object.get("runtime"));
				String language = ((String) object.get("language"));
				int noOfCopies = Integer.parseInt((String) object.get("noOfCopies"));

				languageArray = (JSONArray) object.get("sub-languages");
				ArrayList<String> subLang = new ArrayList<>();
				if (languageArray != null) {
					for (Object lang : languageArray) {
						String stringLanguage = (String) language;
						subLang.add(stringLanguage);
					}
				}

				// TODO: MAKE THIS WORK
				dvdQueueArray = (JSONArray) object.get("bookQueue");
				String dvdQueues = "";
				if (dvdQueueArray != null) {
					for (Object bookQueue : dvdQueueArray) {
						String stringBookQueue = (String) bookQueue;
						dvdQueues += stringBookQueue + ",";
					}
				}

				ArrayList<String> loanDurs = new ArrayList<String>();

				listOfLoanDur = (JSONArray) object.get("listOfLoanDur");
				if (listOfLoanDur != null) {
					for (Object loanDur : listOfLoanDur) {
						String loanDurString = (String) loanDur;
						loanDurs.add(loanDurString);
					}
				}

				dvds.add(new DVD(director, runtime, language, subLang, year, title, thumbnailImageRef, uniqueID,
						noOfCopies, loanDurs, new ArrayList<>()));
			}

			reader.close();
			file.close();
		} catch (FileNotFoundException e) {
			System.out.println("Cannot find " + IO.getDvdFilePath());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("ERROR reading file " + IO.getDvdFilePath());
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("ERROR parsing users JSON");
			e.printStackTrace();
		}

		return dvds;
	}

	public static ArrayList<Laptop> readLaptops() {
		JSONParser parser = new JSONParser();
		// TODO: Implement these:
		JSONArray languageArray = new JSONArray();
		JSONArray dvdQueueArray = new JSONArray();
		////////////////////////
		JSONArray listOfLoanDur = new JSONArray();

		ArrayList<Laptop> laptops = new ArrayList<Laptop>();

		try {
			file = new FileReader(IO.getLaptopFilePath());
			reader = new BufferedReader(file);

			while ((currentLine = reader.readLine()) != null) {
				JSONObject object = (JSONObject) parser.parse(currentLine);

				String uniqueID = ((String) object.get("uniqueID"));
				String manufacturer = ((String) object.get("manufacturer"));
				String model = ((String) object.get("model"));
				String operatingSys = ((String) object.get("operatingSys"));
				String year = ((String) object.get("year"));
				String title = ((String) object.get("title"));
				String thumbnailImg = ((String) object.get("thumbnailImg"));
				int noOfCopies = Integer.parseInt((String) object.get("noOfCopies"));

				ArrayList<String> loanDurs = new ArrayList<String>();

				listOfLoanDur = (JSONArray) object.get("listOfLoanDur");
				if (listOfLoanDur != null) {
					for (Object loanDur : listOfLoanDur) {
						String loanDurString = (String) loanDur;
						loanDurs.add(loanDurString);
					}
				}

				Laptop laptopToAdd = new Laptop(manufacturer, model, operatingSys, year, title, thumbnailImg, uniqueID,
						noOfCopies, loanDurs, new ArrayList<>());

				laptops.add(laptopToAdd);
			}

			reader.close();
			file.close();
		} catch (FileNotFoundException e) {
			System.out.println("Cannot find " + IO.getLaptopFilePath());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("ERROR reading file " + IO.getLaptopFilePath());
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("ERROR parsing users JSON");
			e.printStackTrace();
		}

		return laptops;
	}
}
