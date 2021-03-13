package com.getsetgoapp.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;
import com.getsetgoapp.Model.MyPointsModel;
import com.getsetgoapp.R;
import com.getsetgoapp.activity.BaseScreenActivity;
import com.getsetgoapp.adapterview.MyPointsAdapter;
import com.getsetgoapp.databinding.FragmentMyPointsBinding;
import com.getsetgoapp.util.App;
import com.getsetgoapp.util.JumpToLogin;
import com.getsetgoapp.util.P;

import java.util.ArrayList;
import java.util.List;

public class MyPointsFragment extends Fragment {

    FragmentMyPointsBinding binding;
    Context context;
    private List<MyPointsModel> myPointsModelList;
    private MyPointsAdapter adapter;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager linearLayoutManager;
    int count;
    int pageCount = 1;


    public MyPointsFragment() {
    }

    public static MyPointsFragment newInstance() {
        MyPointsFragment fragment = new MyPointsFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event

                onBackPressClick();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_points, container, false);
        View rootView = binding.getRoot();
        context = inflater.getContext();
        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("My Points");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);

        init();
        return rootView;
    }


    private void init(){

        myPointsModelList = new ArrayList<>();
        adapter = new MyPointsAdapter(getActivity(),myPointsModelList);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerMyPoints.setLayoutManager(linearLayoutManager);
        binding.recyclerMyPoints.setAdapter(adapter);

        setData();
//        setPagination();

        BaseScreenActivity.binding.incFragmenttool.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressClick();
            }
        });
    }

    private void setPagination(){
        binding.recyclerMyPoints.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    loading = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                visibleItemCount = linearLayoutManager.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                if (loading && (visibleItemCount + pastVisiblesItems == totalItemCount)){
                    loading = false;
                    if (myPointsModelList!=null && !myPointsModelList.isEmpty()){
                        if (myPointsModelList.size()<count){
                            pageCount++;
                            callMyPointsDetailsApi(getActivity(),pageCount);
                        }
                    }
                }
            }
        });
    }



    private void setData(){
        myPointsModelList.clear();

        Json json = new Json();
        json.addString("username","Rahul");
        json.addString("amount","553.003");
        json.addString("action_type","Action");
        json.addString("parent_username","Something");
        json.addString("courses","Brain Booster");
        json.addString("income_type","T");
        json.addString("description","Something Description");
        json.addString("create_date_text","883/3883.003");

        MyPointsModel model = new MyPointsModel();
        model.setCreate_date_text("");
        model.setIncome_type("T");
        model.setAmount("9292.00");
        model.setJson(json);


        myPointsModelList.add(model);
        myPointsModelList.add(model);
        myPointsModelList.add(model);
        myPointsModelList.add(model);
        myPointsModelList.add(model);
        myPointsModelList.add(model);
        myPointsModelList.add(model);
        myPointsModelList.add(model);
        myPointsModelList.add(model);
        myPointsModelList.add(model);
        myPointsModelList.add(model);
        myPointsModelList.add(model);
        myPointsModelList.add(model);
        myPointsModelList.add(model);
        myPointsModelList.add(model);
        myPointsModelList.add(model);
        myPointsModelList.add(model);
        myPointsModelList.add(model);
        myPointsModelList.add(model);
        myPointsModelList.add(model);
        adapter.notifyDataSetChanged();

    }

    private void callMyPointsDetailsApi(Context context,int pageCount) {

        LoadingDialog loadingDialog = new LoadingDialog(context, false);
        Api.newApi(context, P.baseUrl + "" )
                .setMethod(Api.POST)
                .onHeaderRequest(App::getHeaders)
                .onLoading(isLoading -> {
                    if (isLoading)
                        loadingDialog.show("loading...");
                    else
                        loadingDialog.hide();
                })
                .onError(() ->
                        MessageBox.showOkMessage(context, "Message", "Failed to login. Please try again", () -> {
                            loadingDialog.dismiss();
                        }))
                .onSuccess(Json1 -> {
                    if (Json1 != null) {
                        JumpToLogin.call(Json1,context);
                        loadingDialog.dismiss();
                        if (Json1.getInt(P.status) == 0) {
                            H.showMessage(context, Json1.getString(P.err));
                        } else {
                            Json1 = Json1.getJson(P.data);
                        }
                    }

                }).run("callMyPointsDetailsApi");

    }

    private void onBackPressClick() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            BaseScreenActivity.callBack();
        }
    }

}
