package com.getsetgo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.getsetgo.Adapter.MyCourseAdapter;
import com.getsetgo.Adapter.WishlistCourseAdapter;
import com.getsetgo.R;

public class WishlistActivity extends AppCompatActivity {
    Context context;
    public RecyclerView recyclerViewWishlist;
    WishlistCourseAdapter wishlistCourseAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        context = WishlistActivity.this;
        init();
    }
    private void init(){
        recyclerViewWishlist = findViewById(R.id.recyclerViewWishlist);
        setupRecyclerViewForWishlist();
    }
    private void setupRecyclerViewForWishlist() {
        recyclerViewWishlist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        wishlistCourseAdapter = new WishlistCourseAdapter(this,recyclerViewWishlist);
        recyclerViewWishlist.setItemAnimator(new DefaultItemAnimator());
        recyclerViewWishlist.setAdapter(wishlistCourseAdapter);
        wishlistCourseAdapter.notifyDataSetChanged();
    }
}