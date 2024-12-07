import java.util.*;
class Book {
    private int id;
    private String title;
    private String author;

    public Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return "Book ID: " + id + ", Title: " + title + ", Author: " + author;
    }
}

class BookNotAvailableException extends Exception {
    public BookNotAvailableException(String message) {
        super(message);
    }
}

class BookNotFoundException extends Exception {
    public BookNotFoundException(String message) {
        super(message);
    }
}

class Library {
    private final List<Book> books = new ArrayList<>();
    private final Set<Integer> borrowedBooks = new HashSet<>();
    
    public void addBook(Book book) {
        books.add(book);
    }

    public void listAvailableBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available in the library.");
            return;
        }
        System.out.println("\nList of Available Books:");
        books.stream()
                .filter(book -> !borrowedBooks.contains(book.getId()))
                .forEach(System.out::println);
    }

    public void borrowBook(int bookId) throws BookNotFoundException, BookNotAvailableException {
        Optional<Book> bookOptional = books.stream().filter(book -> book.getId() == bookId).findFirst();

        if (bookOptional.isEmpty()) {
            throw new BookNotFoundException("Error: Book ID " + bookId + " not found in the library.");
        }
        if (borrowedBooks.contains(bookId)) {
            throw new BookNotAvailableException("Error: Book ID " + bookId + " is already borrowed.");
        }

        borrowedBooks.add(bookId);
        System.out.println("You successfully borrowed the book: " + bookOptional.get().getTitle());
    }

    public void returnBook(int bookId) throws BookNotFoundException {
        Optional<Book> bookOptional = books.stream().filter(book -> book.getId() == bookId).findFirst();

        if (bookOptional.isEmpty()) {
            throw new BookNotFoundException("Error: Book ID " + bookId + " not found in the library.");
        }
        if (borrowedBooks.remove(bookId)) {
            System.out.println("You successfully returned the book: " + bookOptional.get().getTitle());
        } else {
            System.out.println("Error: Book ID " + bookId + " was not borrowed.");
        }
    }
}

public class LibraryManagementSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();

        while (true) {
            System.out.println("\nEnter book details to add to the library:");
            System.out.print("Enter Book ID: ");
            int bookId = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter Book Title: ");
            String bookTitle = scanner.nextLine();
            System.out.print("Enter Book Author: ");
            String bookAuthor = scanner.nextLine();

            Book book = new Book(bookId, bookTitle, bookAuthor);
            library.addBook(book);
            System.out.println("Book added successfully!");

            System.out.print("\nWould you like to add another book? (yes/no): ");
            String choice = scanner.nextLine();
            if (choice.equalsIgnoreCase("no")) {
                break;
            }
        }

        while (true) {
            System.out.println("\nLibrary Management System:");
            System.out.println("1. List Available Books");
            System.out.println("2. Borrow a Book");
            System.out.println("3. Return a Book");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    library.listAvailableBooks();
                    break;
                case 2:
                    System.out.print("\nEnter the ID of the book to borrow: ");
                    int borrowId = scanner.nextInt();
                    try {
                        library.borrowBook(borrowId);
                    } catch (BookNotFoundException | BookNotAvailableException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    System.out.print("\nEnter the ID of the book to return: ");
                    int returnId = scanner.nextInt();
                    try {
                        library.returnBook(returnId);
                    } catch (BookNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    System.out.println("Exiting the system...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }
}

