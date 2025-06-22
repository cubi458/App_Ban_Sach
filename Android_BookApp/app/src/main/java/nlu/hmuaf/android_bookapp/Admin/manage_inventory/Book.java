package nlu.hmuaf.android_bookapp.admin.manage_inventory;

import java.io.Serializable;

public class Book implements Serializable {
    private long bookId;
    private String title;
    private String bookID;
    private String description;
    private double price;
    private int numberOfPages;
    private String publicationDate;
    private String language;
    private String genre;
    private String publisherName;
    private String author;
    private String discountCode;
    private int quantity;
    private String imageUrl;
    private String status;
    private String storageLocation;
    private boolean preOrder;
    private String thumbnail;
    private Double averageRating;
    private double originalPrice;
    private double discountedPrice;
    private Double discount;

    // Constructor mặc định
    public Book() {
    }

    // Constructor từ ListBookResponseDTO
    public Book(nlu.hmuaf.android_bookapp.dto.response.ListBookResponseDTO dto) {
        this.bookId = dto.getBookId();
        this.title = dto.getTitle();
        this.author = dto.getAuthor();
        this.thumbnail = dto.getThumbnail();
        this.averageRating = dto.getAverageRating();
        this.originalPrice = dto.getOriginalPrice();
        this.discountedPrice = dto.getDiscountedPrice();
        this.quantity = dto.getQuantity() != null ? dto.getQuantity().intValue() : 0;
        this.discount = dto.getDiscount();
        
        // Set status dựa trên quantity
        if (this.quantity > 10) {
            this.status = "In Stock";
        } else if (this.quantity > 0) {
            this.status = "Low Stock";
        } else {
            this.status = "Out of Stock";
        }
        
        // Set imageUrl từ thumbnail
        this.imageUrl = dto.getThumbnail();
    }

    // Getters và Setters
    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public boolean isPreOrder() {
        return preOrder;
    }

    public void setPreOrder(boolean preOrder) {
        this.preOrder = preOrder;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }
}