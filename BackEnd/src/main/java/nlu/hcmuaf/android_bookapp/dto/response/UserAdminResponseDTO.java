package nlu.hcmuaf.android_bookapp.dto.response;

// DTO dùng để trả về thông tin người dùng cho admin (FE)
public class UserAdminResponseDTO {
    private long userId;
    private String username;
    private String email;
    private String role;
    private String createdDate;

    public UserAdminResponseDTO() {}

    public UserAdminResponseDTO(long userId, String username, String email, String role, String createdDate) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
        this.createdDate = createdDate;
    }

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