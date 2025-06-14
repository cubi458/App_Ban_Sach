package nlu.hmuaf.android_bookapp.admin.order.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import nlu.hmuaf.android_bookapp.admin.order.bean.OrderItem;
import nlu.hmuaf.android_bookapp.R;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderItemViewHolder> {

    private final List<OrderItem> orderItemList;

    public OrderDetailAdapter(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_item_detail, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        holder.bind(orderItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

     class OrderItemViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productQuantity, productPrice;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            productPrice = itemView.findViewById(R.id.product_price);
        }
         public void bind(OrderItem orderItem) {
             // Load hình ảnh sản phẩm
             Picasso.get().load(orderItem.getResourceid()).into(productImage);

             productName.setText(orderItem.getProductName());
             productQuantity.setText("Số lượng: "+orderItem.getQuantity());
             productPrice.setText("Thành tiền:" +orderItem.getProductPrice());
         }

     }
}