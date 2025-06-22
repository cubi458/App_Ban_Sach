package nlu.hmuaf.android_bookapp.admin.manage_inventory;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import nlu.hmuaf.android_bookapp.R;
import nlu.hmuaf.android_bookapp.dto.response.ListBookResponseDTO;
import nlu.hmuaf.android_bookapp.dto.response.TokenResponseDTO;
import nlu.hmuaf.android_bookapp.networking.BookAppApi;
import nlu.hmuaf.android_bookapp.networking.BookAppService;
import nlu.hmuaf.android_bookapp.utils.MyUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageInventorDetailActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private List<Book> allBooks;
    private int tabIndex;
    private BookAppApi bookAppApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_manage_inventory_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Quản lý sản phẩm");

        tabLayout = findViewById(R.id.tab_layout);
        recyclerView = findViewById(R.id.rvProductList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tabIndex = getIntent().getIntExtra("tabIndex", 0);
        
        // Khởi tạo danh sách rỗng
        allBooks = new ArrayList<>();
        bookAdapter = new BookAdapter(allBooks, ManageInventorDetailActivity.this);
        recyclerView.setAdapter(bookAdapter);

        setupTabs();
        loadBooksFromBackend();
    }

    private void loadBooksFromBackend() {
        TokenResponseDTO tokenResponse = MyUtils.getTokenResponse(this);
        if (tokenResponse != null) {
            bookAppApi = BookAppService.getClient(tokenResponse.getToken());
            Call<List<ListBookResponseDTO>> call = bookAppApi.getAllBooksForAdmin();
            
            call.enqueue(new Callback<List<ListBookResponseDTO>>() {
                @Override
                public void onResponse(Call<List<ListBookResponseDTO>> call, Response<List<ListBookResponseDTO>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // Convert ListBookResponseDTO to Book objects
                        allBooks.clear();
                        for (ListBookResponseDTO dto : response.body()) {
                            allBooks.add(new Book(dto));
                        }
                        
                        bookAdapter.updateBooks(allBooks);
                        updateTabTitles();
                        selectInitialTab();
                    } else {
                        Toast.makeText(ManageInventorDetailActivity.this, 
                            "Không thể tải dữ liệu sách", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<ListBookResponseDTO>> call, Throwable t) {
                    Toast.makeText(ManageInventorDetailActivity.this, 
                        "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupTabs() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                List<Book> newBookList = getBooksForTab(tab.getPosition());
                bookAdapter.updateBooks(newBookList);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Add tabs
        tabLayout.addTab(tabLayout.newTab().setText("Tất cả (0)"));
        tabLayout.addTab(tabLayout.newTab().setText("Còn hàng (0)"));
        tabLayout.addTab(tabLayout.newTab().setText("Đã ẩn (0)"));
        tabLayout.addTab(tabLayout.newTab().setText("Hết hàng (0)"));
        tabLayout.addTab(tabLayout.newTab().setText("Sắp hết hàng (0)"));
        tabLayout.addTab(tabLayout.newTab().setText("SKU đặt hàng (0)"));

        updateTabTitles();
    }

    private void selectInitialTab() {
        TabLayout.Tab tab = tabLayout.getTabAt(tabIndex);
        if (tab != null) {
            tab.select();
        }
    }

    private void updateTabTitles() {
        tabLayout.getTabAt(0).setText("Tất cả (" + getBooksForTab(0).size() + ")");
        tabLayout.getTabAt(1).setText("Còn hàng (" + getBooksForTab(1).size() + ")");
        tabLayout.getTabAt(2).setText("Đã ẩn (" + getBooksForTab(2).size() + ")");
        tabLayout.getTabAt(3).setText("Hết hàng (" + getBooksForTab(3).size() + ")");
        tabLayout.getTabAt(4).setText("Sắp hết hàng (" + getBooksForTab(4).size() + ")");
        tabLayout.getTabAt(5).setText("SKU đặt hàng (" + getBooksForTab(5).size() + ")");
    }

    private List<Book> getBooksForStatus(String status) {
        List<Book> filteredBooks = new ArrayList<>();
        for (Book book : allBooks) {
            if (status.equals(book.getStatus())) {
                filteredBooks.add(book);
            }
        }
        return filteredBooks;
    }

    private List<Book> getBooksForPreOrder() {
        List<Book> preOrderBooks = new ArrayList<>();
        for (Book book : allBooks) {
            if (book.isPreOrder()) {
                preOrderBooks.add(book);
            }
        }
        return preOrderBooks;
    }


    private List<Book> getBooksForTab(int tabPosition) {
        List<Book> books = new ArrayList<>();
        switch (tabPosition) {
            case 0:
                books.addAll(allBooks);
                break;
            case 1:
                for (Book book : allBooks) {
                    if ("In Stock".equals(book.getStatus())) {
                        books.add(book);
                    }
                }
                break;
            case 2:
                for (Book book : allBooks) {
                    if ("Hidden".equals(book.getStatus())) {
                        books.add(book);
                    }
                }
                break;
            case 3:
                for (Book book : allBooks) {
                    if ("Out of Stock".equals(book.getStatus())) {
                        books.add(book);
                    }
                }
                break;
            case 4:
                for (Book book : allBooks) {
                    if ("Low Stock".equals(book.getStatus())) {
                        books.add(book);
                    }
                }
                break;
            case 5:
                for (Book book : allBooks) {
                    if (book.isPreOrder()) {
                        books.add(book);
                    }
                }
                break;
        }
        return books;
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
