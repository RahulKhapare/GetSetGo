package com.getsetgoapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.getsetgoapp.Adapter.WishlistCourseAdapter;
import com.getsetgoapp.R;
import com.getsetgoapp.activity.BaseScreenActivity;
import com.getsetgoapp.databinding.FragmentFavoritesBinding;

public class FavouritesFragment extends Fragment {

    FragmentFavoritesBinding binding;
    WishlistCourseAdapter wishlistCourseAdapter;


    public FavouritesFragment() {
        // Required empty public constructor
    }

    public static FavouritesFragment newInstance() {
        FavouritesFragment fragment = new FavouritesFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_favorites, container, false);
        View rootView = binding.getRoot();
init();
        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Favourites");
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void init(){
setupRecyclerViewFavoutites();
        BaseScreenActivity.binding.incFragmenttool.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    BaseScreenActivity.callBack();
                }
            }
        });
    }

    private void setupRecyclerViewFavoutites() {
        binding.recyclerViewWishlist.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        wishlistCourseAdapter = new WishlistCourseAdapter(getActivity(),binding.recyclerViewWishlist);
        binding.recyclerViewWishlist.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewWishlist.setAdapter(wishlistCourseAdapter);
        wishlistCourseAdapter.notifyDataSetChanged();
    }


}