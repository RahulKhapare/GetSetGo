package com.getsetgo.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.getsetgo.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.sasank.roundedhorizontalprogress.RoundedHorizontalProgressBar;

public class WishlistCourseAdapter extends RecyclerView.Adapter<WishlistCourseAdapter.WishlistCourseViewHolder> {

    Context context;
    int lastSwipedPosition = -1;
    RecyclerView recyclerView;

    public WishlistCourseAdapter(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;

        this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                try {
                    if (lastSwipedPosition != -1) {
                        SwipeLayout swipeLayout = (SwipeLayout) recyclerView.findViewHolderForAdapterPosition(lastSwipedPosition).itemView.findViewById(R.id.swMain);
                        swipeLayout.close(true);
                    }
                    lastSwipedPosition = -1;
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (Error e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @NonNull
    @Override
    public WishlistCourseAdapter.WishlistCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_wishlist, parent, false);
        return new WishlistCourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistCourseAdapter.WishlistCourseViewHolder holder, int position) {



        holder.swMain.addDrag(SwipeLayout.DragEdge.Left, holder.itemView.findViewById(R.id.bottom_wrapper));
        holder.swMain.addDrag(SwipeLayout.DragEdge.Right, holder.itemView.findViewById(R.id.bottom_wrapper));
        holder.swMain.setLeftSwipeEnabled(false);
        holder.swMain.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {
                try {
                    if (lastSwipedPosition != -1 && lastSwipedPosition != position
                            && recyclerView != null &&
                            recyclerView.findViewHolderForAdapterPosition(lastSwipedPosition) != null &&
                            recyclerView.findViewHolderForAdapterPosition(lastSwipedPosition).itemView != null &&
                            recyclerView.findViewHolderForAdapterPosition(lastSwipedPosition).itemView.findViewById(R.id.swMain) != null) {
                        SwipeLayout swipeLayout = (SwipeLayout)recyclerView.findViewHolderForAdapterPosition(lastSwipedPosition).itemView.findViewById(R.id.swMain);
                        swipeLayout.close(true);
                    }
                    lastSwipedPosition = position;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onOpen(SwipeLayout layout) {

            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onClose(SwipeLayout layout) {

            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class WishlistCourseViewHolder extends RecyclerView.ViewHolder {

        TextView txtWishCourse, txtWishProfName, txtWishReview,
                txtWishNewPrice, txtWishOldPrice, txtWishBuyNow;
        RoundedImageView imvWishCourse;
        SwipeLayout swMain;


        public WishlistCourseViewHolder(@NonNull View itemView) {
            super(itemView);

            txtWishCourse = itemView.findViewById(R.id.txtWishCourse);
            txtWishProfName = itemView.findViewById(R.id.txtWishProfName);
            txtWishReview = itemView.findViewById(R.id.txtWishReview);
            txtWishNewPrice = itemView.findViewById(R.id.txtWishNewPrice);
            txtWishOldPrice = itemView.findViewById(R.id.txtWishOldPrice);
            txtWishBuyNow = itemView.findViewById(R.id.txtWishBuyNow);
            imvWishCourse = itemView.findViewById(R.id.imvWishCourse);
            swMain = itemView.findViewById(R.id.swMain);

        }
    }
}

