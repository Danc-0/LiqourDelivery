package com.danc.winesapi.UIFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danc.winesapi.Adapter.ProductAdapter;
import com.danc.winesapi.Interfaces.ApiClient;
import com.danc.winesapi.Interfaces.FragmentCommunication;
import com.danc.winesapi.Models.Product;
import com.danc.winesapi.R;
import com.danc.winesapi.SQLite.CartItemOpenHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    private static final String TAG = "MainFragment";
    Retrofit retrofit;
    ApiClient apiClient;
    RecyclerView recyclerView;
    ProductAdapter adapter;
    List<Product> products;

    CartItemOpenHelper mDb;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.12:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiClient = retrofit.create(ApiClient.class);

        adapter = new ProductAdapter(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        setData();
        return view;
    }

    public void setData() {
        Call<List<Product>> product = apiClient.getProducts();

        product.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                Log.d(TAG, "onResponse: Response is: " + response.body());

                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: Response not successful " + response.code());
                }
                products = response.body();
                adapter.setData(products);
                Log.d(TAG, "onResponse: Code " + response.code());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.d(TAG, "onFailure: Failure is: " + t.getMessage());
            }
        });
    }

};