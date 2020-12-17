package com.getsetgo.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.getsetgo.Adapter.MyCourseAdapter;
import com.getsetgo.Adapter.WishlistCourseAdapter;
import com.getsetgo.R;
import com.getsetgo.databinding.FragmentFavoritesBinding;
import com.getsetgo.databinding.FragmentYourCourseBinding;

public class FavouritesFragment extends Fragment {

    FragmentFavoritesBinding binding;
    WishlistCourseAdapter wishlistCourseAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_favorites, container, false);
        View rootView = binding.getRoot();

        setupRecyclerViewFavoutites();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void setupRecyclerViewFavoutites() {
        binding.recyclerViewWishlist.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        wishlistCourseAdapter = new WishlistCourseAdapter(getActivity(),binding.recyclerViewWishlist);
        binding.recyclerViewWishlist.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewWishlist.setAdapter(wishlistCourseAdapter);
        wishlistCourseAdapter.notifyDataSetChanged();
    }


}