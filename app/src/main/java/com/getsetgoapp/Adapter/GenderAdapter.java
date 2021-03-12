package com.getsetgoapp.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Session;
import com.getsetgoapp.Fragment.EditProfileFragment;
import com.getsetgoapp.Model.GenderModel;
import com.getsetgoapp.R;
import com.getsetgoapp.databinding.ActivityGenderBgBinding;
import com.getsetgoapp.util.P;

import java.util.List;

public class GenderAdapter extends RecyclerView.Adapter<GenderAdapter.ViewHolder> {

    Context context;
    List<GenderModel> genderModelList;
    private int lastCheckPosition = -1;
    EditProfileFragment fragment;
    boolean value = false;

    public GenderAdapter(Context context, List<GenderModel> genderModelList, EditProfileFragment fragment, boolean value) {
        this.context = context;
        this.genderModelList = genderModelList;
        this.fragment = fragment;
        this.value = value;
    }

    public interface click{
        void genderClick(String id);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityGenderBgBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_gender_bg, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GenderModel model = genderModelList.get(position);

        if (value){
            String id = new Session(context).getString(P.gender);
            if (!TextUtils.isEmpty(id) && id.equals(model.getValue())){
                lastCheckPosition = position;
                ((EditProfileFragment)fragment).genderClick(model.getValue());
                value = false;
            }
        }

        holder.binding.radioGender.setChecked(position == lastCheckPosition);
        holder.binding.radioGender.setText(model.getName());

        holder.binding.radioGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastCheckPosition = holder.getAdapterPosition();
                notifyItemRangeChanged(0,genderModelList.size());
                ((EditProfileFragment)fragment).genderClick(model.getValue());
            }
        });

    }

    @Override
    public int getItemCount() {
        return genderModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ActivityGenderBgBinding binding;
        public ViewHolder(@NonNull ActivityGenderBgBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}