package com.danc.winesapi.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.danc.winesapi.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartViewHolder> {

    private static final String TAG = "CartItemAdapter";
    private Context context;
    private ArrayList title, imageUrl, price, originalPrice, quantity;

    public CartItemAdapter(Context context,
                           ArrayList title,
                           ArrayList imageUrl1,
                           ArrayList price,
                           ArrayList initialPrice,
                           ArrayList quantity) {
        this.context = context;
        this.title = title;
        this.imageUrl = imageUrl1;
        this.price = price;
        this.originalPrice = initialPrice;
        this.quantity = quantity;

    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.cart_item, parent, false);

        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.title.setText(String.valueOf(title.get(position)));
        holder.price.setText(String.valueOf(price.get(position)));
        holder.initialPrice.setText(String.valueOf(originalPrice.get(position)));
        holder.quantity.setText(String.valueOf(quantity.get(position)));
        Picasso.get().load(String.valueOf(imageUrl.get(position))).into(holder.image);

//        getTotalCost(Integer.parseInt(String.valueOf(quantity.get(position))), Integer.parseInt(String.valueOf(price.get(position))));

    }

    @Override
    public int getItemCount() {
        return title.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, price, initialPrice, quantity, totalPrice;
        ImageView image;
        RelativeLayout increaseQuantity, decreaseQuantity;

        int currentQty;
        int pricePerPiece;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            initialPrice = itemView.findViewById(R.id.original_price);
            quantity = itemView.findViewById(R.id.quantity);
//            totalPrice = itemView.findViewById(R.id.total_amount);

            increaseQuantity = itemView.findViewById(R.id.increase_quantity);
            decreaseQuantity = itemView.findViewById(R.id.decrease_quantity);

            increaseQuantity.setOnClickListener(this);
            decreaseQuantity.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.increase_quantity:
                    currentQty = Integer.parseInt(quantity.getText().toString());
                    currentQty++;
                    quantity.setText(String.valueOf(currentQty));
                    break;

                case R.id.decrease_quantity:
                    currentQty = Integer.parseInt(quantity.getText().toString());

                    if (currentQty <= 1) {
                        quantity.setText(String.valueOf(currentQty));
                    } else {
                        currentQty--;
                        quantity.setText(String.valueOf(currentQty));
                        break;
                    }
            }
        }
    }

//    public void getTotalCost(int s1, int s2){
//        int calculated = s1 * s2;
//        totalPrice.setText(String.valueOf(calculated));
//    }
}
