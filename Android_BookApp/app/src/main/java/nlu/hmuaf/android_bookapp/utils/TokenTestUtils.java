package nlu.hmuaf.android_bookapp.utils;

import android.content.Context;
import android.util.Log;

import com.auth0.android.jwt.JWT;

import java.util.Date;

import nlu.hmuaf.android_bookapp.dto.response.TokenResponseDTO;

public class TokenTestUtils {
    
    public static void testToken(Context context) {
        TokenResponseDTO tokenResponse = MyUtils.getTokenResponse(context);
        
        Log.d("TOKEN_TEST", "=== TOKEN TEST START ===");
        
        if (tokenResponse == null) {
            Log.d("TOKEN_TEST", "TokenResponse is null");
            return;
        }
        
        Log.d("TOKEN_TEST", "Token: " + tokenResponse.getToken());
        Log.d("TOKEN_TEST", "Role: " + tokenResponse.getRole());
        Log.d("TOKEN_TEST", "Username: " + tokenResponse.getUsername());
        Log.d("TOKEN_TEST", "Email: " + tokenResponse.getEmail());
        
        try {
            JWT jwt = new JWT(tokenResponse.getToken());
            
            Log.d("TOKEN_TEST", "JWT Subject: " + jwt.getSubject());
            Log.d("TOKEN_TEST", "JWT Issued At: " + jwt.getIssuedAt());
            Log.d("TOKEN_TEST", "JWT Expires At: " + jwt.getExpiresAt());
            Log.d("TOKEN_TEST", "Current Time: " + new Date());
            
            if (jwt.getExpiresAt() != null) {
                boolean isExpired = jwt.getExpiresAt().before(new Date());
                Log.d("TOKEN_TEST", "Token is expired: " + isExpired);
            } else {
                Log.d("TOKEN_TEST", "Token has no expiration date");
            }
            
            // Test all claims
            Log.d("TOKEN_TEST", "All claims: " + jwt.getClaims());
            
        } catch (Exception e) {
            Log.e("TOKEN_TEST", "Error parsing JWT: " + e.getMessage());
            e.printStackTrace();
        }
        
        Log.d("TOKEN_TEST", "=== TOKEN TEST END ===");
    }
    
    public static void testRoleNavigation(Context context) {
        Log.d("ROLE_TEST", "=== ROLE NAVIGATION TEST ===");
        
        TokenResponseDTO tokenResponse = MyUtils.getTokenResponse(context);
        if (tokenResponse != null) {
            String role = tokenResponse.getRole();
            Log.d("ROLE_TEST", "Role: " + role);
            Log.d("ROLE_TEST", "Is ADMIN: " + "ADMIN".equals(role));
            Log.d("ROLE_TEST", "Is MANAGER: " + "MANAGER".equals(role));
            Log.d("ROLE_TEST", "Is USER: " + "USER".equals(role));
        } else {
            Log.d("ROLE_TEST", "TokenResponse is null");
        }
        
        Log.d("ROLE_TEST", "=== ROLE NAVIGATION TEST END ===");
    }
} 