package com.getsetgoapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Json;
import com.getsetgoapp.Model.AllCrashCourseModel;
import com.getsetgoapp.Model.ChildCrashCourseModel;
import com.getsetgoapp.R;

import java.util.ArrayList;
import java.util.List;

public class AllCrashCourseAdapter extends RecyclerView.Adapter<AllCrashCourseAdapter.ViewHolder> {

    Context context;
    List<AllCrashCourseModel> allCrashCourseModelList;

    public AllCrashCourseAdapter(Context context, List<AllCrashCourseModel> allCrashCourseModelList) {
        this.context = context;
        this.allCrashCourseModelList = allCrashCourseModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_all_crash_course_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AllCrashCourseModel model = allCrashCourseModelList.get(position);

        holder.txtCourseName.setText(model.getText());

        List<ChildCrashCourseModel> childCrashCourseModelList = new ArrayList<>();

        if (model.getCrash_courses()!=null && !model.getCrash_courses().isEmpty()){
            for (Json json : model.getCrash_courses()){
                ChildCrashCourseModel courseModel = new ChildCrashCourseModel();
                courseModel.setId(json.getString("id"));
                courseModel.setName(json.getString("name"));
                courseModel.setSlug(json.getString("slug"));
                courseModel.setProgram_date(json.getString("program_date"));
                courseModel.setProgram_end_date(json.getString("program_end_date"));
                courseModel.setImage(json.getString("image"));
                courseModel.setCategory_name(json.getString("category_name"));
                courseModel.setPrice(json.getString("price"));
                courseModel.setSale_price(json.getString("sale_price"));
                childCrashCourseModelList.add(courseModel);
            }
        }

        ChildCrashCourseAdapter adapter = new ChildCrashCourseAdapter(context,childCrashCourseModelList);
        holder.recyclerViewChild.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
        holder.recyclerViewChild.setHasFixedSize(true);
        holder.recyclerViewChild.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        return allCrashCourseModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCourseName;
        RecyclerView recyclerViewChild;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCourseName = itemView.findViewById(R.id.txtCourseName);
            recyclerViewChild = itemView.findViewById(R.id.recyclerViewChild);
        }
    }
}
