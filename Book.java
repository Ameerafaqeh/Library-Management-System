import javafx.beans.property.*;

public class Book {
    private final IntegerProperty bookId;
    private final StringProperty title;
    private final StringProperty author;
    private final IntegerProperty categoryId;
 
    public Book() {
        this.bookId = new SimpleIntegerProperty();
        this.title = new SimpleStringProperty();
        this.author = new SimpleStringProperty();
        this.categoryId = new SimpleIntegerProperty();
    }

    public Book(int bookId, String title, String author, int categoryId) {
        this.bookId = new SimpleIntegerProperty(bookId);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.categoryId = new SimpleIntegerProperty(categoryId);
    }

    public int getBookId() {
        return bookId.get();
    }

    public IntegerProperty bookIdProperty() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId.set(bookId);
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getAuthor() {
        return author.get();
    }

    public StringProperty authorProperty() {
        return author;
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public int getCategoryId() {
        return categoryId.get();
    }

    public IntegerProperty categoryIdProperty() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId.set(categoryId);
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId.get() +
                ", title='" + title.get() + '\'' +
                ", author='" + author.get() + '\'' +
                ", categoryId=" + categoryId.get() +
                '}';
    }
}
