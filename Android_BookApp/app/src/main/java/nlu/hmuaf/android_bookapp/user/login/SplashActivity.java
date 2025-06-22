package nlu.hmuaf.android_bookapp.user.login;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import nlu.hmuaf.android_bookapp.R;
import nlu.hmuaf.android_bookapp.utils.NavigationUtils;

public class SplashActivity extends AppCompatActivity {
    
    private static final int SPLASH_DELAY = 2000; // 2 giây
    private Handler handler = new Handler();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        // Chờ 2 giây rồi kiểm tra và chuyển hướng
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                NavigationUtils.navigateBasedOnRole(SplashActivity.this);
                finish();
            }
        }, SPLASH_DELAY);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
} 