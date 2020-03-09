import java.io.*;
import java.util.*;

public class DatabaseMock {

    File users = new File("src/Users");
    File books = new File("src/Books");
    Map<String, User> userMap = new HashMap<>();
    Map<Integer, Book> bookMap = new HashMap<>();

    public DatabaseMock() {
        loadTheDatabase();
    }

    public void loadTheDatabase() {
        try {
            Scanner usersScanner = new Scanner(users);
            Scanner booksScanner = new Scanner(books);
            while (usersScanner.hasNextLine()) {
                String nextRecord = usersScanner.nextLine();
                //System.out.println(nextRecord);
                if (nextRecord.length() < 4) {
                    continue;
                }
                loadASingleUser(nextRecord);
            }
            while (booksScanner.hasNextLine()) {
                String nextRecord = booksScanner.nextLine();
                //System.out.println(nextRecord);
                //System.out.println(bookMap.values());
                if (nextRecord.length() < 4) {
                    continue;
                }
                loadASingleBook(nextRecord);
            }
            usersScanner.close();
            booksScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private void loadASingleUser(String nextRecord) {
        User user = new User();
        user.setUsername(nextRecord.substring(0, nextRecord.indexOf('|')));
        nextRecord = nextRecord.substring(nextRecord.indexOf('|') + 1);
        user.setFirstName(nextRecord.substring(0, nextRecord.indexOf('|')));
        nextRecord = nextRecord.substring(nextRecord.indexOf('|') + 1);
        user.setSurname(nextRecord.substring(0, nextRecord.indexOf('|')));
        nextRecord = nextRecord.substring(nextRecord.indexOf('|') + 1);
        user.setEmail(nextRecord.substring(0, nextRecord.indexOf('|')));
        nextRecord = nextRecord.substring(nextRecord.indexOf('|') + 1);
        user.setPassword(nextRecord);
        userMap.put(user.getUsername(), user);
    }

    private void loadASingleBook(String nextRecord) {
        Book book = new Book();
        book.setId(Integer.parseInt(nextRecord.substring(0, nextRecord.indexOf('|'))));
        nextRecord = nextRecord.substring(nextRecord.indexOf('|') + 1);
        book.setAuthor(nextRecord.substring(0, nextRecord.indexOf('|')));
        nextRecord = nextRecord.substring(nextRecord.indexOf('|') + 1);
        book.setTitle(nextRecord.substring(0, nextRecord.indexOf('|')));
        nextRecord = nextRecord.substring(nextRecord.indexOf('|') + 1);
        book.setPublisher(nextRecord.substring(0, nextRecord.indexOf('|')));
        nextRecord = nextRecord.substring(nextRecord.indexOf('|') + 1);
        book.setIsbn(nextRecord.substring(0, nextRecord.indexOf('|')));
        nextRecord = nextRecord.substring(nextRecord.indexOf('|') + 1);
        book.setNumberOfPages(Integer.parseInt(nextRecord));
        bookMap.put(book.getId(), book);
    }

    public void addABook(Book book) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(books, true));
            String bookToFile = book.getId() + "|" + book.getAuthor() + "|" + book.getTitle() + "|" + book.getPublisher() + "|" + book.getIsbn() + "|" + book.getNumberOfPages();
            writer.newLine();
            writer.append(bookToFile);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Book getBookById(int id) {
        try {
            Scanner booksScanner = new Scanner(books);
            while (booksScanner.hasNextLine()) {
                String nextRecord = booksScanner.nextLine();
                if (nextRecord.length()<3){
                    continue;
                }
                if (Integer.parseInt(nextRecord.substring(0, nextRecord.indexOf('|'))) == id) {
                    return parseStringForBook(nextRecord);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Could not find the \"Books\" file");
        }
        return null;
    }

    private Book parseStringForBook(String nextRecord) {
        Book book = new Book();
        book.setId(Integer.parseInt(nextRecord.substring(0, nextRecord.indexOf('|'))));
        nextRecord = nextRecord.substring(nextRecord.indexOf('|') + 1);
        book.setAuthor(nextRecord.substring(0, nextRecord.indexOf('|')));
        nextRecord = nextRecord.substring(nextRecord.indexOf('|') + 1);
        book.setTitle(nextRecord.substring(0, nextRecord.indexOf('|')));
        nextRecord = nextRecord.substring(nextRecord.indexOf('|') + 1);
        book.setPublisher(nextRecord.substring(0, nextRecord.indexOf('|')));
        nextRecord = nextRecord.substring(nextRecord.indexOf('|') + 1);
        book.setIsbn(nextRecord.substring(0, nextRecord.indexOf('|')));
        nextRecord = nextRecord.substring(nextRecord.indexOf('|') + 1);
        book.setNumberOfPages(Integer.parseInt(nextRecord));
        return book;
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(bookMap.values());
    }

    public void saveAllBooks() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(books));
            writer.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Book book : bookMap.values()) {
            addABook(book);
        }
    }

    public int getNumberOfBooks() {
        return bookMap.size() + 1;
    }

    public List<String> getAllUserNames() {
        return new ArrayList<>(userMap.keySet());
    }

    public User getUserByUsername(String inputUsername) {
        try {
            return userMap.get(inputUsername);
        } catch (NullPointerException e) {
            System.out.println("ERROR: No user with this username found.");
        }
        return null;
    }

    public void addUser(String newUsername, String newName, String newSurname, String newEmail, String newPassword) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(users, true));
            String bookToFile = newUsername + "|" + newName + "|" + newSurname + "|" + newEmail + "|" + newPassword;
            writer.newLine();
            writer.append(bookToFile);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
