import java.util.List;

public class BookCategory {
    private int categoryId;
    private String name;
    private String categoryDescription;
    private List<Book> books;

    public BookCategory() {
    }

    public BookCategory(int categoryId, String name, String categoryDescription, List<Book> books) {
        this.categoryId = categoryId;
        this.name = name;
        this.categoryDescription = categoryDescription;
        this.books = books;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "BookCategory [categoryId=" + categoryId + ", name=" + name + ", categoryDescription=" + categoryDescription
                + ", books=" + books + "]";
    }
}
