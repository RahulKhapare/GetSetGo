package com.getsetgoapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.H;
import com.getsetgoapp.Fragment.MyCourseDetailFragment;
import com.getsetgoapp.Model.MyOrderModel;
import com.getsetgoapp.R;
import com.getsetgoapp.activity.BaseScreenActivity;
import com.getsetgoapp.util.CheckConnection;
import com.getsetgoapp.util.Config;

import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {

    Context context;
    List<MyOrderModel> myOrderModelList;
    MyCourseDetailFragment courseDetailFragment;
    Bundle bundle = new Bundle();

    public MyOrderAdapter(Context context, List<MyOrderModel> myOrderModelList) {
        this.context = context;
        this.myOrderModelList = myOrderModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_my_order_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyOrderModel model = myOrderModelList.get(position);

        holder.txtCurseName.setText(model.getCourse_name());
        holder.txtAmount.setText(model.getAmount());
        holder.txtDate.setText(model.getPurchase_date());

        holder.rlyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(model.getSlug()) && !model.getSlug().equals("null")) {
                    if (CheckConnection.isVailable(context)) {
                        loadFragment(v, model.getSlug(),model.getCourse_name());
                    } else {
                        H.showMessage(context, "No internet connection available");
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return myOrderModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtCurseName,txtAmount,txtDate;
        RelativeLayout rlyView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCurseName = itemView.findViewById(R.id.txtCurseName);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtDate = itemView.findViewById(R.id.txtDate);
            rlyView = itemView.findViewById(R.id.rlyView);
        }
    }

    private void loadFragment(View v, String courseSlug,String courseName) {
        Config.POP_HOME = true;
        BaseScreenActivity.binding.bottomNavigation.setVisibility(View.GONE);
        BaseScreenActivity.binding.incBasetool.content.setVisibility(View.GONE);
        BaseScreenActivity.binding.incFragmenttool.content.setVisibility(View.VISIBLE);
        BaseScreenActivity.binding.incFragmenttool.llSubCategory.setVisibility(View.GONE);
        Config.myCourseSlug = courseSlug;
        Config.myCourseTitle = courseName;
        bundle.putString("slug",courseSlug);
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        if (courseDetailFragment == null)
            courseDetailFragment = new MyCourseDetailFragment();
        courseDetailFragment.setArguments(bundle);
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, courseDetailFragment)
                .addToBackStack(null)
                .commit();
    }
}

