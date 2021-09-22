package com.getsetgoapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.getsetgoapp.Model.RegisterForModel;
import com.getsetgoapp.R;
import com.getsetgoapp.activity.SignUpActivity;

import java.util.List;

public class RegisterForAdapter extends RecyclerView.Adapter<RegisterForAdapter.ViewHolder> {

    Context context;
    List<RegisterForModel> registerForModelList;

    public RegisterForAdapter(Context context, List<RegisterForModel> registerForModelList) {
        this.context = context;
        this.registerForModelList = registerForModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_register_for_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RegisterForModel model = registerForModelList.get(position);
        model.setValue(0);
        holder.checkFor.setText(model.getPurpose_name());
        holder.checkFor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    model.setValue(1);
                } else {
                    model.setValue(0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return registerForModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkFor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkFor = itemView.findViewById(R.id.checkFor);
        }
    }

}

