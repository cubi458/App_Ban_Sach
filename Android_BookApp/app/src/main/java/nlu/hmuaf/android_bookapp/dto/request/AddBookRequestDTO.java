package nlu.hmuaf.android_bookapp.dto.request;

import java.util.List;

import nlu.hmuaf.android_bookapp.enums.EBookFormat;

public class AddBookRequestDTO {
    private String code;
    private String title;
    private String description;
    private double price;
    private String thumbnail;
    private List<String> bookImages;
    private String author;
    private String size;
    private int numPage;
    private EBookFormat bookFormat;
    private String publishCompanyName;
    private Long discountId;

    public AddBookRequestDTO() {
    }

    public AddBookRequestDTO(String code, String title, String description, double price, 
                           String thumbnail, List<String> bookImages, String author, 
                           String size, int numPage, EBookFormat bookFormat, 
                           String publishCompanyName, Long discountId) {
        this.code = code;
        this.title = title;
        this.description = description;
        this.price = price;
        this.thumbnail = thumbnail;
        this.bookImages = bookImages;
        this.author = author;
        this.size = size;
        this.numPage = numPage;
        this.bookFormat = bookFormat;
        this.publishCompanyName = publishCompanyName;
        this.discountId = discountId;
    }

    // Getters and Setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<String> getBookImages() {
        return bookImages;
    }

    public void setBookImages(List<String> bookImages) {
        this.bookImages = bookImages;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getNumPage() {
        return numPage;
    }

    public void setNumPage(int numPage) {
        this.numPage = numPage;
    }

    public EBookFormat getBookFormat() {
        return bookFormat;
    }

    public void setBookFormat(EBookFormat bookFormat) {
        this.bookFormat = bookFormat;
    }

    public String getPublishCompanyName() {
        return publishCompanyName;
    }

    public void setPublishCompanyName(String publishCompanyName) {
        this.publishCompanyName = publishCompanyName;
    }

    public Long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }
} 