package nlu.hmuaf.android_bookapp.admin.user.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import nlu.hmuaf.android_bookapp.R;
import nlu.hmuaf.android_bookapp.dto.response.UserAdminResponseDTO;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<UserAdminResponseDTO> userList;
    public UserAdapter(List<UserAdminResponseDTO> userList) {
        this.userList = userList;
    }
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_admin, parent, false);
        return new UserViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserAdminResponseDTO user = userList.get(position);
        holder.userId.setText(String.valueOf(user.getUserId()));
        holder.username.setText(user.getUsername());
        holder.email.setText(user.getEmail());
        holder.role.setText(user.getRole());
        holder.createdDate.setText(user.getCreatedDate());
    }
    @Override
    public int getItemCount() {
        return userList.size();
    }
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userId, username, email, role, createdDate;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userId = itemView.findViewById(R.id.user_id);
            username = itemView.findViewById(R.id.username);
            email = itemView.findViewById(R.id.email);
            role = itemView.findViewById(R.id.role);
            createdDate = itemView.findViewById(R.id.created_date);
        }
    }
} 