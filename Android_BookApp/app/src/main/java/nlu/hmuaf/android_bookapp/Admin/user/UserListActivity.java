package nlu.hmuaf.android_bookapp.admin.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import nlu.hmuaf.android_bookapp.R;
import nlu.hmuaf.android_bookapp.admin.user.adapter.UserAdapter;
import nlu.hmuaf.android_bookapp.user.profile.classess.User;
import nlu.hmuaf.android_bookapp.networking.BookAppApi;
import nlu.hmuaf.android_bookapp.networking.BookAppService;
import nlu.hmuaf.android_bookapp.dto.response.TokenResponseDTO;
import nlu.hmuaf.android_bookapp.utils.MyUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import nlu.hmuaf.android_bookapp.dto.response.UserAdminResponseDTO;

public class UserListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<UserAdminResponseDTO> userList = new ArrayList<>();
    private BookAppApi bookAppApi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        recyclerView = findViewById(R.id.recycler_view_user);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(userList);
        recyclerView.setAdapter(userAdapter);
        fetchUsers();
    }

    private void fetchUsers() {
        TokenResponseDTO tokenResponseDTO = MyUtils.getTokenResponse(getApplicationContext());
        bookAppApi = BookAppService.getClient(tokenResponseDTO.getToken());
        Call<List<UserAdminResponseDTO>> call = bookAppApi.getAllUsers();
        call.enqueue(new Callback<List<UserAdminResponseDTO>>() {
            @Override
            public void onResponse(Call<List<UserAdminResponseDTO>> call, Response<List<UserAdminResponseDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userList.clear();
                    userList.addAll(response.body());
                    userAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(UserListActivity.this, "Không lấy được danh sách người dùng", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<UserAdminResponseDTO>> call, Throwable t) {
                Toast.makeText(UserListActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
            }
        });
    }
} 