import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class Login {

    String inputUsername;
    String inputPassword;
    User user;
    DatabaseMock database = new DatabaseMock();
    boolean loginSuccessful = false;
    boolean exit = false;
    Scanner scanner = new Scanner(System.in);

    public void appStart() {
        while (!exit) {
//            debugUsers();
//            login:
        while (!loginSuccessful) {
            try {
                displayLogin();
            } catch (IncorrectPasswordException | UsernameDoesNotExistException e) {
                System.out.println(e.getMessage());
            }
        }
            System.out.println("What do you want to do today? Choose a number:");
            System.out.println("1. View my books");
            System.out.println("2. Add a brand new book");
            System.out.println("3. Edit a book entry");
            System.out.println("4. Add a review to a book");
            System.out.println("5. View all book borrowers");
            System.out.println("6. Add a book borrower");
            System.out.println("7. That's it for this time, farewell");
            int userChoice = 0;
            try {
                userChoice = scanner.nextInt();
                displayMainMenu(userChoice);
                scanner.reset();
            } catch (InputMismatchException e) {
                System.out.println("Well I think there was some kind of misunderstanding here.");
                exit = true;
            }
            scanner.reset();
        }
    }

    private void displayMainMenu(int userChoice) {
        switch (userChoice) {
            case 0:
                exit = false;
                break;
            case 1:
                viewAllBooks();
                break;
            case 2:
                addANewBook();
                break;
            case 3:
                editABook();
                break;
            case 4:
                addAReview();
                break;
            case 5:
                viewAllBorrowers();
                break;
            case 6:
                addABorrower();
                break;
            case 7:
                exit = true;
                break;
        }
    }

    private void addABorrower() {
        //todo
    }

    private void viewAllBorrowers() {
        //todo
    }

    private void addAReview() {
        //todo
    }

    private void editABook() {
        System.out.println("Which book are you willing to change?");
        viewAllBooks();
        int bookChoice = scanner.nextInt();
        Book bookToEdit = database.getBookById(bookChoice);
        System.out.println("You have selected this book to edit: \n" + bookToEdit.toString() + "\n");
        Book newBookRecord = askTheUserForBookDetails(bookToEdit);
        database.bookMap.put(newBookRecord.getId(),newBookRecord);
        database.saveAllBooks();
        scanner.reset();
    }

    private void addANewBook() {
        Book book = askTheUserForBookDetails();
        database.addABook(book);
    }

    private Book askTheUserForBookDetails() {
        return askTheUserForBookDetails(null);
    }

    private Book askTheUserForBookDetails(Book editedBook) {
        Scanner scanner = new Scanner(System.in);
        Book book = new Book();
        book.setId(database.getNumberOfBooks());
        if (editedBook != null) {
            book.setId(editedBook.getId());
        }
        System.out.println("Type the author of the book");
        if (editedBook != null) {
            System.out.println("So far, your book was written by: " + editedBook.getAuthor());
        }
        String author = "";
        author = scanner.nextLine();
        book.setAuthor(author);
        String title = "";
        while (title.length() < 1) {
            System.out.println("Type the title of the book you want to add");
            if (editedBook != null) {
                System.out.println("The book was titled: " + editedBook.getTitle());
            }
            title = scanner.nextLine();
            if (title.length() < 1) {
                System.out.println("This Book Needs A Title -- Theodore Ficklestein");
            }
        }
        book.setTitle(title);
        System.out.println("Enter an ISBN number, if you want");
        if (editedBook != null) {
            System.out.println("The ISBN code was: " + editedBook.getIsbn());
        }
        String isbn = scanner.nextLine();
        book.setIsbn(isbn);
        System.out.println("Enter a publisher name");
        if (editedBook != null) {
            System.out.println("This book was published by: " + editedBook.getPublisher());
        }
        String publisher = scanner.nextLine();
        book.setPublisher(publisher);
        System.out.println("if you fancy, add the number of pages as well");
        if (editedBook != null) {
            System.out.println("Previously, the book was that thick: " + editedBook.getNumberOfPages());
        }
        int pages = scanner.nextInt();
        book.setNumberOfPages(pages);
        return book;
    }

    private void viewAllBooks() {
        for (Book book : database.getAllBooks()) {
            System.out.println(book.toString());
        }
    }

    private void displayLogin() throws IncorrectPasswordException, UsernameDoesNotExistException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to BookKeeper - a place to guard all your books.");
        System.out.println("Please log in with a username:");
        System.out.println("(if you want to create a new user, write CREATIO)");
        inputUsername = scanner.nextLine();
        if (inputUsername.equalsIgnoreCase("creatio")) {
            createANewUser(scanner);
        } else {
            System.out.println("Please enter password:");
            inputPassword = scanner.nextLine();
            user = loginWithUsernameAndPassword(inputUsername, inputPassword);
            loginSuccessful = true;
        }
    }

    private void createANewUser(Scanner scanner) {
        boolean usernameIsNew = false;
        String newUsername = "";
        System.out.println("So you want a new username. Type the one you fancy, and we'll see what's possible.");
        while (!usernameIsNew) {
            newUsername = scanner.nextLine();
            usernameIsNew = true;
            for (String name : database.getAllUserNames()) {
                if (name.equalsIgnoreCase(newUsername)) {
                    System.out.println("Darn. That one's taken. Try again.");
                    usernameIsNew = false;
                    break;
                }
            }

        }
        System.out.println("Enter the pass phrase that will unlock the BookKeeper's Library");
        String newPassword = scanner.nextLine();
        System.out.println("By what name are you known?");
        String newName = scanner.nextLine();
        System.out.println(newName + " of which house? - please, enter your surname");
        String newSurname = scanner.nextLine();
        String newEmail = validateEmail(scanner);
        database.addUser(newUsername, newName, newSurname, newEmail, newPassword);
        database.loadTheDatabase();
    }

    private String validateEmail(Scanner scanner) {
        String newEmail;
        while (true) {
            System.out.println("How can we reach you? Enter an email address");
            newEmail = scanner.nextLine();
            if (newEmail.contains("@")) {
                String dotFinder = newEmail.substring(newEmail.indexOf('@'));
                if (dotFinder.contains(".")) {
                    break;
                }
            }
            System.out.println("Well that does not seem to be an email address.");
        }
        return newEmail;
    }

    private void debugUsers() {
        Map<String, User> tempMap = database.userMap;
        for (User user : tempMap.values()) {
            String debug = user.toString();
            System.out.println(debug);
        }
    }


    private User loginWithUsernameAndPassword(String inputUsername, String inputPassword) throws IncorrectPasswordException, UsernameDoesNotExistException {
        boolean loginExists = false;
        for (String username : database.userMap.keySet()
        ) {
            if (inputUsername.equals(username)) {
                loginExists = true;
                break;
            }
        }
        if (!loginExists) {
            throw new UsernameDoesNotExistException();
        }
        if (database.getUserByUsername(inputUsername).getPassword().equals(inputPassword)) {
            return database.getUserByUsername(inputUsername);
        } else {
            throw new IncorrectPasswordException();
        }
    }

}
