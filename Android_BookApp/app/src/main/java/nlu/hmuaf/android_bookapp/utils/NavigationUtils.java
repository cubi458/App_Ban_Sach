package nlu.hmuaf.android_bookapp.utils;

import android.content.Context;
import android.content.Intent;

import nlu.hmuaf.android_bookapp.admin.Home;
import nlu.hmuaf.android_bookapp.dto.response.TokenResponseDTO;
import nlu.hmuaf.android_bookapp.user.home.activity.HomeActivity;
import nlu.hmuaf.android_bookapp.user.login.Login;

public class NavigationUtils {
    
    /**
     * Chuyển hướng user đến trang phù hợp dựa trên role
     */
    public static void navigateBasedOnRole(Context context) {
        TokenResponseDTO tokenResponse = MyUtils.getTokenResponse(context);
        
        System.out.println("=== NAVIGATION DEBUG ===");
        System.out.println("TokenResponse: " + tokenResponse);
        
        if (tokenResponse != null && MyUtils.isTokenExpiration(context)) {
            // User đã đăng nhập và token còn hạn
            String role = tokenResponse.getRole();
            System.out.println("Role: " + role);
            
            Intent intent;
            switch (role) {
                case "ADMIN":
                    System.out.println("Navigating to Admin Home");
                    intent = new Intent(context, Home.class);
                    break;
                case "MANAGER":
                case "USER":
                default:
                    System.out.println("Navigating to User Home");
                    intent = new Intent(context, HomeActivity.class);
                    break;
            }
            
            context.startActivity(intent);
        } else {
            // User chưa đăng nhập hoặc token hết hạn
            System.out.println("Token is null or expired, navigating to Login");
            Intent intent = new Intent(context, Login.class);
            context.startActivity(intent);
        }
    }
    
    /**
     * Chuyển hướng đến trang đăng nhập
     */
    public static void navigateToLogin(Context context) {
        Intent intent = new Intent(context, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
    
    /**
     * Chuyển hướng đến Admin Home
     */
    public static void navigateToAdminHome(Context context) {
        Intent intent = new Intent(context, Home.class);
        context.startActivity(intent);
    }
    
    /**
     * Chuyển hướng đến User Home
     */
    public static void navigateToUserHome(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }
    
    /**
     * Kiểm tra xem user có quyền truy cập admin không
     */
    public static boolean hasAdminAccess(Context context) {
        return RoleUtils.isAdmin(context);
    }
    
    /**
     * Kiểm tra xem user có quyền truy cập user features không
     */
    public static boolean hasUserAccess(Context context) {
        return RoleUtils.isLoggedIn(context);
    }
} 