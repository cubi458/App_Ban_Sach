package nlu.hmuaf.android_bookapp.dto.response;

// DTO dùng để nhận dữ liệu người dùng từ backend cho admin
public class UserAdminResponseDTO {
    private long userId;
    private String username;
    private String email;
    private String role;
    private String createdDate;

    public UserAdminResponseDTO() {}

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }
} 