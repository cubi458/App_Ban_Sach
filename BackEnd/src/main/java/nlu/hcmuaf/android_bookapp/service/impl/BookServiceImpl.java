package nlu.hcmuaf.android_bookapp.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import nlu.hcmuaf.android_bookapp.dto.json.BooksWrapper;
import nlu.hcmuaf.android_bookapp.dto.request.AddBookRequestDTO;
import nlu.hcmuaf.android_bookapp.dto.response.BookDetailResponseDTO;
import nlu.hcmuaf.android_bookapp.dto.response.ListBookResponseDTO;
import nlu.hcmuaf.android_bookapp.dto.response.PageBookResponseDTO;
import nlu.hcmuaf.android_bookapp.entities.Addresses;
import nlu.hcmuaf.android_bookapp.entities.BookDetails;
import nlu.hcmuaf.android_bookapp.entities.BookImages;
import nlu.hcmuaf.android_bookapp.entities.BookRating;
import nlu.hcmuaf.android_bookapp.entities.Books;
import nlu.hcmuaf.android_bookapp.entities.PublishCompany;
import nlu.hcmuaf.android_bookapp.entities.Ratings;
import nlu.hcmuaf.android_bookapp.entities.ShipmentDetails;
import nlu.hcmuaf.android_bookapp.entities.Shipments;
import nlu.hcmuaf.android_bookapp.entities.Users;
import nlu.hcmuaf.android_bookapp.enums.EBookFormat;
import nlu.hcmuaf.android_bookapp.exception.handler.ResourceNotFoundException;
import nlu.hcmuaf.android_bookapp.repositories.AddressRepository;
import nlu.hcmuaf.android_bookapp.repositories.BookDetailsRepository;
import nlu.hcmuaf.android_bookapp.repositories.BookImageRepository;
import nlu.hcmuaf.android_bookapp.repositories.BookRepository;
import nlu.hcmuaf.android_bookapp.repositories.RatingRepository;
import nlu.hcmuaf.android_bookapp.repositories.ShipmentRepository;
import nlu.hcmuaf.android_bookapp.repositories.UserRepository;
import nlu.hcmuaf.android_bookapp.service.templates.IBookService;
import nlu.hcmuaf.android_bookapp.service.templates.IPublishCompanyService;
import nlu.hcmuaf.android_bookapp.specifications.SearchCriteria;
import nlu.hcmuaf.android_bookapp.specifications.entity_specification.BooksSpecifications;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class BookServiceImpl implements IBookService {

  private final String[] companyDefault = {"NXB Kim Đồng", "NXB Hà Nội", "NXB Văn Học", "Yen On",
      "NXB Thế Giới"};
  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private BookRepository bookRepository;
  @Autowired
  private ModelMapper modelMapper;
  @Autowired
  private IPublishCompanyService publishCompanyService;
  @Autowired
  private RatingRepository ratingRepository;
  @Autowired
  private BookImageRepository bookImageRepository;
  @Autowired
  private BookDetailsRepository bookDetailsRepository;
  @Autowired
  private ShipmentRepository shipmentRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private AddressRepository addressRepository;

  @Override
  @Transactional(rollbackOn = Exception.class)
  public void loadDefaultData() {
    try {
      if (bookRepository.getAllBy().isEmpty()) {
        ClassPathResource resource = new ClassPathResource("data.json");
        List<BooksWrapper> listBookWrapper = objectMapper.readValue(resource.getInputStream(),
            new TypeReference<List<BooksWrapper>>() {
            });

        // get publish company
        PublishCompany kimDongCompany = publishCompanyService.getPublishCompanyByCompanyName(
            companyDefault[0]);
        PublishCompany haNoiCompany = publishCompanyService.getPublishCompanyByCompanyName(
            companyDefault[1]);
        PublishCompany vanHocCompany = publishCompanyService.getPublishCompanyByCompanyName(
            companyDefault[2]);
        PublishCompany yenOnCompany = publishCompanyService.getPublishCompanyByCompanyName(
            companyDefault[3]);
        PublishCompany theGioiCompany = publishCompanyService.getPublishCompanyByCompanyName(
            companyDefault[4]);
        for (BooksWrapper booksWrapper : listBookWrapper) {
          // set book details for each book
          BookDetails bookDetails = new BookDetails();
          bookDetails.setEBookFormat(
              EBookFormat.valueOfLabel(booksWrapper.getBooks().getBookDetails().getBookFormat()));
          bookDetails.setSize(booksWrapper.getBooks().getBookDetails().getSize());
          bookDetails.setNumPage(
              Integer.valueOf(booksWrapper.getBooks().getBookDetails().getNumPage()));
          String formattedAuthors = booksWrapper.getBooks().getBookDetails().getAuthor()
              .replace("\n", ", ");
          bookDetails.setAuthor(formattedAuthors);

          // set publish company for each book
          String bookWrapperCompany = booksWrapper.getBooks().getBookDetails().getPublishCompany()
              .getCompanyName();
          if (bookWrapperCompany.equals(companyDefault[0])) {
            bookDetails.setPublishCompany(kimDongCompany);
          } else if (bookWrapperCompany.equals(companyDefault[1])) {
            bookDetails.setPublishCompany(haNoiCompany);
          } else if (bookWrapperCompany.equals(companyDefault[2])) {
            bookDetails.setPublishCompany(vanHocCompany);
          } else if (bookWrapperCompany.equals(companyDefault[3])) {
            bookDetails.setPublishCompany(yenOnCompany);
          } else if (bookWrapperCompany.equals(companyDefault[4])) {
            bookDetails.setPublishCompany(theGioiCompany);
          }

          // set book wrapper to book entity
          Books book = new Books();
          book.setCode(booksWrapper.getBooks().getCode());
          book.setTitle(booksWrapper.getBooks().getTitle());
          book.setPrice(Double.valueOf(booksWrapper.getBooks().getPrice()));
          book.setThumbnail(booksWrapper.getBooks().getThumbnail());
          book.setDescription(booksWrapper.getBooks().getDescription());

          // set book to book details
          bookDetails.setBook(book);

          // set book sup images
          String[] listBookImages = booksWrapper.getBooks().getBookImages();
          Set<BookImages> bookImagesSet = new HashSet<>();
          for (String url : listBookImages) {
            BookImages bookImage = new BookImages();
            bookImage.setBook(book);
            bookImage.setUrl(url);
            bookImagesSet.add(bookImage);
          }
          book.setBookImages(bookImagesSet);
          book.setBookDetails(bookDetails);

          // setting rating
          Ratings ratings = new Ratings();
          ratings.setStar((float) (4.1 + (5.0 - 4.1) * Random.class.newInstance().nextDouble()));
          // save rating to database
          ratingRepository.save(ratings);

          // generate book rating
          BookRating bookRating = new BookRating();
          bookRating.setBook(book);
          bookRating.setRating(ratings);

          // generate set Book rating
          Set<BookRating> bookRatingSet = new HashSet<>();
          bookRatingSet.add(bookRating);

          // setting book ratings
          ratings.setBookRatings(bookRatingSet);
          book.setBookRatings(bookRatingSet);

          // save book to database
          bookRepository.save(book);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public Page<ListBookResponseDTO> getNewBookList(Pageable pageable) {
    return bookRepository.getNewBookList(pageable);
  }

  @Override
  public Page<ListBookResponseDTO> getDiscountBookList(Pageable pageable) {
    return bookRepository.getDiscountBookList(pageable);
  }

  @Override
  public BookDetailResponseDTO getBookDetailsByBookId(long bookId) {
    Optional<BookDetailResponseDTO> optional = Optional.ofNullable(
        bookRepository.getBooksDetailsByBookId(bookId).orElseThrow(
            () -> new ResourceNotFoundException("Book details NOT FOUND WITH " + bookId)));
    if (optional.isPresent()) {
      var data = optional.get();
      List<String> bookImages = bookImageRepository.getAllByBookImageByBookId(bookId);
      data.setImg(bookImages.toArray(new String[0]));
      Optional<BookDetails> bookDetails = bookDetailsRepository.getAllByBookId(bookId);
      data.setDetails(bookDetails.get().toString());
      return data;
    }
    return null;
  }

  @Override
  public PageBookResponseDTO findBooks(String title, String bookType, String coverType,
      String publisher, Pageable pageable) {
    List<Specification<Books>> specs = new ArrayList<>();
    if (title != null && !title.isEmpty()) {
      specs.add(new BooksSpecifications(new SearchCriteria("title", ":", title)));
    }
    if (bookType != null && !bookType.isEmpty()) {
      if (bookType.equalsIgnoreCase("newBook")) {
        specs.add(new BooksSpecifications(new SearchCriteria("newBook")));
      } else if (bookType.equalsIgnoreCase("discountBook")) {
        specs.add(new BooksSpecifications(new SearchCriteria("discountBook")));
      }
    }
    if (coverType != null && !coverType.isEmpty()) {
      specs.add(new BooksSpecifications(new SearchCriteria("coverType", ":", coverType)));
    }
    if (publisher != null && !publisher.isEmpty()) {
      specs.add(new BooksSpecifications(new SearchCriteria("publisher", ":", publisher)));
    }
    Specification<Books> finalSpec = specs.stream().reduce(Specification::and).orElse(null);

    // Tìm kiếm theo các tiêu chí và phân trang
    Page<Books> booksPage;
    if (finalSpec != null) {
      booksPage = bookRepository.findAll(finalSpec, pageable);
    } else {
      booksPage = bookRepository.findAll(pageable);
    }

    // Chuyển đổi từ Page<Books> sang PageBookResponseDTO
    List<ListBookResponseDTO> listBookResponseDTOs = booksPage.getContent().stream()
        .map(book -> {
          // Tính trung bình rating
          Double averageRating = book.getBookRatings().stream()
              .mapToDouble(r -> r.getRating().getStar())
              .average()
              .orElse(0.0);

          // Tổng số lượng shipment details
          Long totalQuantity = book.getShipmentDetails().stream()
              .mapToLong(sd -> sd.getQuantity())
              .sum();

          // Lấy percent từ Discounts, nếu null thì mặc định là 0.0
          Double discountPercent = Optional.ofNullable(book.getDiscounts())
              .map(discounts -> discounts.getPercent())
              .orElse(0.0);

          return new ListBookResponseDTO(
              book.getBookId(),
              book.getThumbnail(),
              book.getTitle(),
              book.getBookDetails().getAuthor(),
              averageRating,
              book.getPrice(),
              totalQuantity,
              discountPercent
          );
        })
        .collect(Collectors.toList());

    // Tạo đối tượng PageBookResponseDTO và đặt giá trị
    PageBookResponseDTO pageBookResponseDTO = new PageBookResponseDTO();
    pageBookResponseDTO.setBookResponseDTOList(listBookResponseDTOs);
    pageBookResponseDTO.setTotalPages(booksPage.getTotalPages());

    return pageBookResponseDTO;
  }

  @Override
  public List<ListBookResponseDTO> getAllBooksForAdmin() {
    return bookRepository.getAllBooksForAdmin();
  }

  @Override
  @Transactional(rollbackOn = Exception.class)
  public BookDetailResponseDTO addBook(AddBookRequestDTO addBookRequestDTO) {
    // Tạo entity Books
    Books book = new Books();
    book.setCode(addBookRequestDTO.getCode());
    book.setTitle(addBookRequestDTO.getTitle());
    book.setDescription(addBookRequestDTO.getDescription());
    book.setPrice(addBookRequestDTO.getPrice());
    book.setThumbnail(addBookRequestDTO.getThumbnail());

    // Tạo BookDetails
    BookDetails bookDetails = new BookDetails();
    bookDetails.setAuthor(addBookRequestDTO.getAuthor());
    bookDetails.setSize(addBookRequestDTO.getSize());
    bookDetails.setNumPage(addBookRequestDTO.getNumPage());
    bookDetails.setEBookFormat(addBookRequestDTO.getBookFormat());

    // Tìm PublishCompany theo tên
    PublishCompany publishCompany = publishCompanyService.getPublishCompanyByCompanyName(
        addBookRequestDTO.getPublishCompanyName());
    if (publishCompany == null) {
      throw new ResourceNotFoundException("Publish company not found: " + addBookRequestDTO.getPublishCompanyName());
    }
    bookDetails.setPublishCompany(publishCompany);

    // Liên kết Book và BookDetails
    bookDetails.setBook(book);
    book.setBookDetails(bookDetails);

    // Tạo BookImages
    if (addBookRequestDTO.getBookImages() != null && !addBookRequestDTO.getBookImages().isEmpty()) {
      Set<BookImages> bookImagesSet = new HashSet<>();
      for (String imageUrl : addBookRequestDTO.getBookImages()) {
        BookImages bookImage = new BookImages();
        bookImage.setBook(book);
        bookImage.setUrl(imageUrl);
        bookImagesSet.add(bookImage);
      }
      book.setBookImages(bookImagesSet);
    }

    // Lưu book vào database
    Books savedBook = bookRepository.save(book);

    // Tạo shipment details cho sách mới để có quantity
    createShipmentDetailsForNewBook(savedBook);

    // Tạo BookDetailResponseDTO để trả về
    BookDetailResponseDTO responseDTO = new BookDetailResponseDTO();
    responseDTO.setBookId(savedBook.getBookId());
    responseDTO.setTitle(savedBook.getTitle());
    responseDTO.setAuthor(bookDetails.getAuthor());
    responseDTO.setOriginalPrice(savedBook.getPrice());
    responseDTO.setDescription(savedBook.getDescription());
    responseDTO.setDetails(bookDetails.toString());
    responseDTO.setPublicCompany(publishCompany.getCompanyName());

    // Lấy danh sách hình ảnh
    if (savedBook.getBookImages() != null) {
      List<String> imageUrls = savedBook.getBookImages().stream()
          .map(BookImages::getUrl)
          .collect(Collectors.toList());
      responseDTO.setImg(imageUrls.toArray(new String[0]));
    }

    return responseDTO;
  }

  private void createShipmentDetailsForNewBook(Books book) {
    try {
      // Lấy shipment đầu tiên hoặc tạo mới nếu chưa có
      List<Shipments> existingShipments = shipmentRepository.getAllBy();
      Shipments shipment;
      
      if (existingShipments.isEmpty()) {
        // Tạo shipment mới nếu chưa có
        shipment = new Shipments();
        shipment.setDateAdded(LocalDate.now());
        shipment.setAvailable(true);
        shipment.setTotalQuantity(50);
        
        // Lấy user admin đầu tiên
        Optional<Users> adminUser = userRepository.findUsersByUsername("vuluu");
        if (adminUser.isPresent()) {
          shipment.setUser(adminUser.get());
        }
        
        // Lấy address đầu tiên
        Optional<Addresses> firstAddress = addressRepository.findById(1L);
        if (firstAddress.isPresent()) {
          shipment.setAddress(firstAddress.get());
        }
        
        shipment = shipmentRepository.save(shipment);
      } else {
        // Sử dụng shipment đầu tiên
        shipment = existingShipments.get(0);
      }
      
      // Tạo shipment details cho sách mới
      ShipmentDetails shipmentDetails = new ShipmentDetails();
      shipmentDetails.setBook(book);
      shipmentDetails.setShipment(shipment);
      shipmentDetails.setQuantity(50); // Số lượng mặc định
      shipmentDetails.setAvailable(true);
      
      // Lưu shipment details
      // Note: ShipmentDetails có composite key nên cần lưu thông qua Shipments
      Set<ShipmentDetails> currentDetails = shipment.getShipmentDetails();
      if (currentDetails == null) {
        currentDetails = new HashSet<>();
      }
      currentDetails.add(shipmentDetails);
      shipment.setShipmentDetails(currentDetails);
      
      // Cập nhật total quantity của shipment
      int currentTotal = shipment.getTotalQuantity();
      shipment.setTotalQuantity(currentTotal + 50);
      
      shipmentRepository.save(shipment);
      
    } catch (Exception e) {
      // Log lỗi nhưng không throw exception để không ảnh hưởng đến việc thêm sách
      System.err.println("Error creating shipment details for book: " + e.getMessage());
    }
  }
}
