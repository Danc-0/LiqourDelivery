package com.danc.winesapi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.danc.winesapi.Interfaces.FragmentCommunication;
import com.danc.winesapi.ItemActivity;
import com.danc.winesapi.Models.Product;
import com.danc.winesapi.R;
import com.danc.winesapi.UIFragments.ItemFragment;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private static final String TAG = "ProductAdapter";
    Context context;
    List<Product> products = new ArrayList<>();
    String itemID;
    FragmentCommunication communication;

    public ProductAdapter(Context context){
        this.context = context;
    }

    public void setData(List<Product> productList){
        this.products = productList;

    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.product_item, parent, false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.bindData(product);
        String imageUrlHead = "http://192.168.0.12:8000";
        Picasso.get().load(imageUrlHead + product.getImageUrl()).into(holder.productImage);

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView productImage;
        TextView productTitle, itemId, productDescription, productPrice, productOriginalPrice, NumRatings;
        RatingBar productRatings;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.image);

            itemId = itemView.findViewById(R.id.itemId);
            productTitle = itemView.findViewById(R.id.title);
            productDescription = itemView.findViewById(R.id.description);
            productPrice = itemView.findViewById(R.id.price1);
            productOriginalPrice = itemView.findViewById(R.id.original_price);
            NumRatings = itemView.findViewById(R.id.num_ratings);

            productRatings = itemView.findViewById(R.id.rating);

            itemView.setOnClickListener(this);

        }

        public void bindData(Product product) {
            itemId.setText(product.getId());
            productTitle.setText(product.getProductName());
            productDescription.setText(product.getProductDescription());
            productPrice.setText(product.getProductPrice());
            productOriginalPrice.setText(product.getInitialPrice());
            NumRatings.setText(product.getRatingsNumbers());

            Float rates = Float.parseFloat(product.getRatingsNumbers());
            productRatings.setRating(rates);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Log.d(TAG, "onClick: Item Position " + position);
            Product product = products.get(position);
            Intent intent = new Intent(view.getContext(), ItemActivity.class);
            intent.putExtra("SelectedProduct", product);
            Log.d(TAG, "onClick: SelectedProduct " + product);
            context.startActivity(intent);


//            AppCompatActivity appCompatActivity = (AppCompatActivity) view.getContext();
//            ItemFragment itemFragment = new ItemFragment();
//            FragmentTransaction transaction = appCompatActivity.getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.main_container, itemFragment);
//            transaction.addToBackStack(null);
//            transaction.commit();

        }
    }
}
