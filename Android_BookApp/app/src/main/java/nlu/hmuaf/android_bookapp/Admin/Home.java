package nlu.hmuaf.android_bookapp.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import nlu.hmuaf.android_bookapp.admin.manage_inventory.AddBookActivity;
import nlu.hmuaf.android_bookapp.admin.manage_inventory.ManageInventoryActivity;
import nlu.hmuaf.android_bookapp.admin.manage_inventory.ManageInventorDetailActivity;
import nlu.hmuaf.android_bookapp.admin.order.activity.OrderList;
import nlu.hmuaf.android_bookapp.R;
import nlu.hmuaf.android_bookapp.dto.response.TokenResponseDTO;
import nlu.hmuaf.android_bookapp.user.login.Login;
import nlu.hmuaf.android_bookapp.utils.MyUtils;
import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private LinearLayout orderManagementSubmenu, productManagementSubmenu, userManagementSubmenu;
    private TextView navSalesManagement , listOrderSubMenu, listAddOrderSubMenu, listInStockSubMenu, listProductSubMenu,
            listAddProductSubMenu, listUserSubMenu, listAddUserSubMenu, adminWelcomeText, logoutText;
    private ImageView navOrderManagement,navProductManagement,navUserManagement;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home);
        
        System.out.println("=== ADMIN HOME DEBUG: onCreate started ===");

        // Khai báo
        drawerLayout = findViewById(R.id.drawer_layout);
        productManagementSubmenu = findViewById(R.id.product_management_submenu);
        userManagementSubmenu = findViewById(R.id.user_management_submenu);

        // nút hiển thị các nav
        orderManagementSubmenu = findViewById(R.id.order_management_submenu);

        // Hiển thị thông tin admin
        adminWelcomeText = findViewById(R.id.user_name);
        logoutText = findViewById(R.id.user_email);
        
        // Lấy thông tin user
        TokenResponseDTO tokenResponse = MyUtils.getTokenResponse(this);
        System.out.println("=== ADMIN HOME DEBUG: TokenResponse = " + tokenResponse + " ===");
        
        if (tokenResponse != null) {
            String welcomeMessage = "Admin: " + tokenResponse.getUsername();
            adminWelcomeText.setText(welcomeMessage);
            logoutText.setText(tokenResponse.getEmail());
            System.out.println("=== ADMIN HOME DEBUG: Welcome message set = " + welcomeMessage + " ===");
        } else {
            System.out.println("=== ADMIN HOME DEBUG: TokenResponse is null ===");
        }
        
        // Xử lý đăng xuất - thay đổi TextView thành clickable
        logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.deleteTokenResponse(Home.this);
                Toast.makeText(Home.this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Home.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        // Chọn Quản lý đơn hàng => Danh sách đơn hàng, Thêm đơn hàng và Danh sách tồn kho
        navOrderManagement = findViewById(R.id.nav_order_management);
        navOrderManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSubMenu(orderManagementSubmenu);
            }
        });

        // Chọn Danh sách đơn hàng
        listOrderSubMenu = findViewById(R.id.list_order_submenu);
        listOrderSubMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(Home.this, OrderList.class);
                startActivity(intent);
            }
        });

        // Chọn Thêm đơn hàng
        listAddOrderSubMenu = findViewById(R.id.list_addOrder_submenu);
        listAddOrderSubMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View view) {
//                Intent intent = new Intent(AdminHome.this, ManageOrder.class);
//                startActivity(intent);
            }
        });

        // Chọn Quản lý tồn kho
        listInStockSubMenu = findViewById(R.id.list_inStock_submenu);
        listInStockSubMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(Home.this, ManageInventoryActivity.class);
                startActivity(intent);
            }
        });


        // Chọn quản lý sản phẩm -> Danh sách sản phẩm và Thêm đơn hàng
        navProductManagement = findViewById(R.id.nav_product_management);
        navProductManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSubMenu(productManagementSubmenu);
            }
        });

        // Chọn Danh sách sản phẩm
        listProductSubMenu = findViewById(R.id.list_product_submenu);
        listProductSubMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(Home.this, ManageInventorDetailActivity.class);
                intent.putExtra("tabIndex", 0); // Hiển thị tất cả sản phẩm
                startActivity(intent);
            }
        });

        // Chọn Thêm sản phẩm
        listAddProductSubMenu = findViewById(R.id.list_addProduct_submenu);
        listAddProductSubMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(Home.this, AddBookActivity.class);
                startActivity(intent);
            }
        });

        // Chọn doanh thu
        navSalesManagement = findViewById(R.id.nav_sales_mangagement);
        navSalesManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(AdminHome.this, HomeActivity.class);
//                startActivity(intent);
            }
        });

        // Chọn quản lý người dùng => Danh sách người dùng và Thêm người dùng
        navUserManagement = findViewById(R.id.nav_user_management);
        navUserManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSubMenu(userManagementSubmenu);
            }
        });

        // Chọn Danh sách người dùng
        listUserSubMenu = findViewById(R.id.list_user_submenu);
        listUserSubMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(Home.this, nlu.hmuaf.android_bookapp.admin.user.UserListActivity.class);
                startActivity(intent);
            }
        });

        // Chọn Thêm người dùng
        listAddUserSubMenu = findViewById(R.id.list_addUser_submenu);
        listAddUserSubMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View view) {
//                Intent intent = new Intent(AdminHome.this, ManageOrder.class);
//                startActivity(intent);
            }
        });

    }

    // hàm hiện ra thanh menu
    private void toggleSubMenu(LinearLayout submenu) {
        if (submenu.getVisibility() == View.GONE) {
            submenu.setVisibility(View.VISIBLE);
        } else {
            submenu.setVisibility(View.GONE);
        }
    }
}
