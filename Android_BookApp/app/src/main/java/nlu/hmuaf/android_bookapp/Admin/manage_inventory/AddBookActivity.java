package nlu.hmuaf.android_bookapp.admin.manage_inventory;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import nlu.hmuaf.android_bookapp.R;
import nlu.hmuaf.android_bookapp.dto.request.AddBookRequestDTO;
import nlu.hmuaf.android_bookapp.dto.response.BookDetailResponseDTO;
import nlu.hmuaf.android_bookapp.enums.EBookFormat;
import nlu.hmuaf.android_bookapp.networking.BookAppApi;
import nlu.hmuaf.android_bookapp.networking.BookAppService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBookActivity extends AppCompatActivity {

    private static final int PICK_THUMBNAIL_REQUEST = 1;
    private static final int PICK_IMAGES_REQUEST = 2;
    private static final int PERMISSIONS_REQUEST_READ_STORAGE = 101;
    private static final int PERMISSIONS_REQUEST_READ_IMAGES = 102;

    private EditText etBookTitle, etBookID, etBookDescription, etBookPrice, etNumberOfPages, etPublicationDate, etLanguage, etSize, etFormat, etAuthor, etDiscountCode;
    private Button btnUploadThumbnail, btnUploadBookImages, btnSave, btnCancel;
    private ImageView ivThumbnail, ivPublicationDate;
    private RecyclerView rvBookImages;
    private Spinner spinnerPublishers;
    private TextView tvSelectedPublishers;
    private BookImagesAdapter adapter;
    private List<Uri> imageUris = new ArrayList<>();
    private ArrayList<String> selectedPublishers = new ArrayList<>();
    private String[] publisherList = {"Yen On", "A", "B", "C"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_add_book);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Thêm Sách");

        // Initialize all views
        initializeViews();

        // Setup spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, publisherList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPublishers.setAdapter(spinnerAdapter);

        spinnerPublishers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedPublisher = publisherList[position];
                if (!selectedPublishers.contains(selectedPublisher)) {
                    selectedPublishers.add(selectedPublisher);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        btnUploadThumbnail.setOnClickListener(v -> {
            if (checkPermission()) {
                openImagePicker(false, PICK_THUMBNAIL_REQUEST); // Chỉ cho phép chọn một hình
            } else {
                requestStoragePermission();
            }
        });

        btnUploadBookImages.setOnClickListener(v -> {
            if (checkPermission()) {
                openImagePicker(true, PICK_IMAGES_REQUEST); // Cho phép chọn nhiều hình
            } else {
                requestStoragePermission();
            }
        });


        btnSave.setOnClickListener(v -> saveBook());
        btnCancel.setOnClickListener(v -> finish());

        rvBookImages.setLayoutManager(new GridLayoutManager(this, 3)); // Thay '3' bằng số cột bạn muốn
        adapter = new BookImagesAdapter(this, imageUris);
        rvBookImages.setAdapter(adapter);


        // Set up publication date picker dialog
        ivPublicationDate.setOnClickListener(v -> showDatePickerDialog());
    }

    private void initializeViews() {
        etBookTitle = findViewById(R.id.etBookTitle);
        etBookID = findViewById(R.id.etBookID);
        etBookDescription = findViewById(R.id.etBookDescription);
        etBookPrice = findViewById(R.id.etBookPrice);
        etNumberOfPages = findViewById(R.id.etNumberOfPages);
        etPublicationDate = findViewById(R.id.etPublicationDate);
        etLanguage = findViewById(R.id.etLanguage);
        etSize = findViewById(R.id.etSize);
        etFormat = findViewById(R.id.etFormat);
        etAuthor = findViewById(R.id.etAuthor);
        etDiscountCode = findViewById(R.id.etDiscountCode);
        btnUploadThumbnail = findViewById(R.id.btnUploadThumbnail);
        btnUploadBookImages = findViewById(R.id.btnUploadBookImages);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        ivThumbnail = findViewById(R.id.ivThumbnail);
        ivPublicationDate = findViewById(R.id.ivPublicationDate);
        rvBookImages = findViewById(R.id.rvBookImages);
        spinnerPublishers = findViewById(R.id.spinnerPublishers);
        tvSelectedPublishers = findViewById(R.id.tvSelectedPublishers);

        // Setup RecyclerView for book images
        rvBookImages.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new BookImagesAdapter(this, imageUris);
        rvBookImages.setAdapter(adapter);

        // Setup date picker
        ivPublicationDate.setOnClickListener(v -> showDatePickerDialog());
        etPublicationDate.setOnClickListener(v -> showDatePickerDialog());
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, month1, dayOfMonth) -> etPublicationDate.setText(String.format("%04d-%02d-%02d", year1, month1 + 1, dayOfMonth)),
                year, month, day);
        datePickerDialog.show();
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, PERMISSIONS_REQUEST_READ_IMAGES);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_STORAGE || requestCode == PERMISSIONS_REQUEST_READ_IMAGES) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Quyền truy cập đã được cấp!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Quyền truy cập bị từ chối. Không thể truy cập hình ảnh.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openImagePicker(boolean allowMultiple, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (allowMultiple) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        startActivityForResult(Intent.createChooser(intent, "Chọn hình ảnh"), requestCode);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_THUMBNAIL_REQUEST) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    Glide.with(this).load(selectedImageUri).into(ivThumbnail);
                }
            } else if (requestCode == PICK_IMAGES_REQUEST) {
                imageUris.clear();
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        imageUris.add(imageUri);
                    }
                } else if (data.getData() != null) {
                    Uri imageUri = data.getData();
                    imageUris.add(imageUri);
                }
                adapter.notifyDataSetChanged();
            }
        }
    }


    private void saveBook() {
        String title = etBookTitle.getText().toString().trim();
        String id = etBookID.getText().toString().trim();
        String description = etBookDescription.getText().toString().trim();
        String price = etBookPrice.getText().toString().trim();
        String pages = etNumberOfPages.getText().toString().trim();
        String publicationDate = etPublicationDate.getText().toString().trim();
        String language = etLanguage.getText().toString().trim();
        String size = etSize.getText().toString().trim();
        String format = etFormat.getText().toString().trim();
        String author = etAuthor.getText().toString().trim();
        String discountCode = etDiscountCode.getText().toString().trim();

        if (!validateInput(title, id, description, price, pages, publicationDate, language, size, format, author, discountCode)) {
            return;
        }

        // Assuming you are saving the details to a local database or sending them to a server:
        saveBookDetails(title, id, description, price, pages, publicationDate, language, size, format, author, discountCode, selectedPublishers);
        Toast.makeText(this, "Sách đã được lưu cùng với các nhà xuất bản đã chọn.", Toast.LENGTH_SHORT).show();
        finish(); // Close the activity
    }

    private boolean validateInput(String title, String id, String description, String price, String pages, String publicationDate, String language, String size, String format, String author, String discountCode) {
        if (title.isEmpty()) {
            etBookTitle.setError("Tên sách là bắt buộc");
            etBookTitle.requestFocus();
            return false;
        }

        if (id.isEmpty()) {
            etBookID.setError("ID sách là bắt buộc");
            etBookID.requestFocus();
            return false;
        }

        if (description.isEmpty()) {
            etBookDescription.setError("Mô tả là bắt buộc");
            etBookDescription.requestFocus();
            return false;
        }

        if (price.isEmpty() || !Pattern.matches("\\d+(\\.\\d{1,2})?", price)) {
            etBookPrice.setError("Giá không hợp lệ");
            etBookPrice.requestFocus();
            return false;
        }

        if (pages.isEmpty() || !Pattern.matches("\\d+", pages)) {
            etNumberOfPages.setError("Số trang phải là số");
            etNumberOfPages.requestFocus();
            return false;
        }

        if (publicationDate.isEmpty()) {
            etPublicationDate.setError("Ngày xuất bản là bắt buộc");
            etPublicationDate.requestFocus();
            return false;
        }

        if (language.isEmpty()) {
            etLanguage.setError("Ngôn ngữ là bắt buộc");
            etLanguage.requestFocus();
            return false;
        }

        if (size.isEmpty()) {
            etSize.setError("Kích thước là bắt buộc");
            etSize.requestFocus();
            return false;
        }

        if (format.isEmpty()) {
            etFormat.setError("Định dạng là bắt buộc");
            etFormat.requestFocus();
            return false;
        }

        if (author.isEmpty()) {
            etAuthor.setError("Tác giả là bắt buộc");
            etAuthor.requestFocus();
            return false;
        }

        if (discountCode.isEmpty()) {
            etDiscountCode.setError("Mã giảm giá là bắt buộc");
            etDiscountCode.requestFocus();
            return false;
        }

        return true;
    }

    private void saveBookDetails(String title, String id, String description, String price, String pages, String publicationDate, String language, String size, String format, String author, String discountCode, ArrayList<String> publishers) {
        // Tạo AddBookRequestDTO
        AddBookRequestDTO addBookRequestDTO = new AddBookRequestDTO();
        addBookRequestDTO.setCode(id);
        addBookRequestDTO.setTitle(title);
        addBookRequestDTO.setDescription(description);
        addBookRequestDTO.setPrice(Double.parseDouble(price));
        addBookRequestDTO.setThumbnail("https://example.com/thumbnail.jpg"); // Placeholder
        addBookRequestDTO.setAuthor(author);
        addBookRequestDTO.setSize(size);
        addBookRequestDTO.setNumPage(Integer.parseInt(pages));
        addBookRequestDTO.setBookFormat(EBookFormat.fromString(format));
        addBookRequestDTO.setPublishCompanyName(publishers.isEmpty() ? "NXB Kim Đồng" : publishers.get(0));
        
        // Convert image URIs to URLs (placeholder for now)
        List<String> imageUrls = new ArrayList<>();
        for (Uri uri : imageUris) {
            imageUrls.add(uri.toString());
        }
        addBookRequestDTO.setBookImages(imageUrls);

        // Gọi API để thêm sách
        BookAppApi bookAppApi = BookAppService.getClient();
        Call<BookDetailResponseDTO> call = bookAppApi.addBook(addBookRequestDTO);
        call.enqueue(new Callback<BookDetailResponseDTO>() {
            @Override
            public void onResponse(Call<BookDetailResponseDTO> call, Response<BookDetailResponseDTO> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddBookActivity.this, "Thêm sách thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddBookActivity.this, "Lỗi: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookDetailResponseDTO> call, Throwable t) {
                Toast.makeText(AddBookActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
