package nlu.hmuaf.android_bookapp.utils;

import android.content.Context;

import nlu.hmuaf.android_bookapp.dto.response.TokenResponseDTO;

public class RoleUtils {
    
    /**
     * Kiểm tra xem user có phải là ADMIN không
     */
    public static boolean isAdmin(Context context) {
        TokenResponseDTO tokenResponse = MyUtils.getTokenResponse(context);
        return tokenResponse != null && "ADMIN".equals(tokenResponse.getRole());
    }
    
    /**
     * Kiểm tra xem user có phải là MANAGER không
     */
    public static boolean isManager(Context context) {
        TokenResponseDTO tokenResponse = MyUtils.getTokenResponse(context);
        return tokenResponse != null && "MANAGER".equals(tokenResponse.getRole());
    }
    
    /**
     * Kiểm tra xem user có phải là USER không
     */
    public static boolean isUser(Context context) {
        TokenResponseDTO tokenResponse = MyUtils.getTokenResponse(context);
        return tokenResponse != null && "USER".equals(tokenResponse.getRole());
    }
    
    /**
     * Lấy role hiện tại của user
     */
    public static String getCurrentRole(Context context) {
        TokenResponseDTO tokenResponse = MyUtils.getTokenResponse(context);
        return tokenResponse != null ? tokenResponse.getRole() : null;
    }
    
    /**
     * Kiểm tra xem user có quyền admin hoặc manager không
     */
    public static boolean isAdminOrManager(Context context) {
        TokenResponseDTO tokenResponse = MyUtils.getTokenResponse(context);
        if (tokenResponse != null) {
            String role = tokenResponse.getRole();
            return "ADMIN".equals(role) || "MANAGER".equals(role);
        }
        return false;
    }
    
    /**
     * Kiểm tra xem user đã đăng nhập chưa
     */
    public static boolean isLoggedIn(Context context) {
        TokenResponseDTO tokenResponse = MyUtils.getTokenResponse(context);
        return tokenResponse != null && MyUtils.isTokenExpiration(context);
    }
} 