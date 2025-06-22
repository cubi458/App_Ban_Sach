package nlu.hmuaf.android_bookapp.user.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import nlu.hmuaf.android_bookapp.R;
import nlu.hmuaf.android_bookapp.dto.response.TokenResponseDTO;
import nlu.hmuaf.android_bookapp.utils.MyUtils;
import nlu.hmuaf.android_bookapp.utils.NavigationUtils;
import nlu.hmuaf.android_bookapp.utils.TokenTestUtils;

public class RoleBasedHomeActivity extends AppCompatActivity {
    
    private TextView welcomeText;
    private Handler handler = new Handler();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_based_home);
        
        welcomeText = findViewById(R.id.welcome_text);
        
        // Lấy thông tin user từ TokenResponse
        TokenResponseDTO tokenResponse = MyUtils.getTokenResponse(this);
        
        // Test token
        TokenTestUtils.testToken(this);
        TokenTestUtils.testRoleNavigation(this);
        
        if (tokenResponse != null) {
            String username = tokenResponse.getUsername();
            String role = tokenResponse.getRole();
            
            // Hiển thị thông tin chào mừng
            String welcomeMessage = "Xin chào, " + username + "!\nVai trò: " + role;
            welcomeText.setText(welcomeMessage);
            
            // Chuyển hướng sau 2 giây
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    NavigationUtils.navigateBasedOnRole(RoleBasedHomeActivity.this);
                    finish();
                }
            }, 2000);
        } else {
            // Nếu không có thông tin user, chuyển về login
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
} 