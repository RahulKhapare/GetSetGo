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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;
import com.getsetgoapp.Adapter.MyOrderAdapter;
import com.getsetgoapp.Model.MyOrderModel;
import com.getsetgoapp.R;
import com.getsetgoapp.activity.BaseScreenActivity;
import com.getsetgoapp.databinding.FragmentMyOrderBinding;
import com.getsetgoapp.util.App;
import com.getsetgoapp.util.JumpToLogin;
import com.getsetgoapp.util.P;

import java.util.ArrayList;
import java.util.List;

public class MyOrderFragment extends Fragment {

    FragmentMyOrderBinding binding;
    MyOrderAdapter myOrderAdapter;
    List<MyOrderModel> myOrderModelList;
    boolean isFromBottom;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager linearLayoutManager;
    int count;
    int pageCount = 1;

    public MyOrderFragment() {

    }

    public static MyOrderFragment newInstance() {
        MyOrderFragment fragment = new MyOrderFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_order, container, false);
        View rootView = binding.getRoot();

        isFromBottom = getArguments().getBoolean("isFromBottom");
        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("My Order");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);

        init();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                onBackClick();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
               onBackClick();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        super.onCreate(savedInstanceState);
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void init(){

        myOrderModelList = new ArrayList<>();
        myOrderAdapter = new MyOrderAdapter(getContext(),myOrderModelList);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerMyOrder.setLayoutManager(linearLayoutManager);
        binding.recyclerMyOrder.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerMyOrder.setAdapter(myOrderAdapter);

        onClick();
        hitMyOrderApi(getActivity(),pageCount);
//        setPagination();

    }


    private void setPagination(){
        binding.recyclerMyOrder.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    if (myOrderModelList!=null && !myOrderModelList.isEmpty()){
                        if (myOrderModelList.size()<count){
                            pageCount++;
                            hitMyOrderApi(getActivity(),pageCount);
                        }
                    }
                }
            }
        });
    }

    private void onClick(){

        BaseScreenActivity.binding.incFragmenttool.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackClick();
            }
        });

    }

    private void hitMyOrderApi(Context context,int pageCount) {
//        String page = "";
//        if (pageCount!=0){
//            page = "page="+ pageCount + "";
//        }
        LoadingDialog loadingDialog = new LoadingDialog(context, false);
        Api.newApi(context, P.baseUrl + "course_order_history")
                .setMethod(Api.GET)
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
                            checkError();
                        }))
                .onSuccess(Json1 -> {
                    if (Json1 != null) {
                        JumpToLogin.call(Json1, context);
                        loadingDialog.dismiss();
                        if (Json1.getInt(P.status) == 0) {
                            checkError();
                            H.showMessage(context, Json1.getString(P.err));
                        } else if (Json1.getInt(P.status) == 1) {
                            Json1 = Json1.getJson(P.data);
//                            count = Json1.getInt(P.num_rows);
                            JsonList list = Json1.getJsonList(P.list);
                            if (list!=null&&list.size()!=0){
                                for (Json jsonData : list){

                                    String purchase_date = jsonData.getString(P.purchase_date);
                                    String amount = jsonData.getString(P.amount);
                                    String course_name = jsonData.getString(P.course_name);
                                    String slug = jsonData.getString(P.slug);

                                    MyOrderModel model = new MyOrderModel();
                                    model.setPurchase_date(purchase_date);
                                    model.setAmount(amount);
                                    model.setCourse_name(course_name);
                                    model.setSlug(slug);
                                    myOrderModelList.add(model);

                                }
                            }
                            myOrderAdapter.notifyDataSetChanged();
                            checkError();
                        }
                    }
                }).run("hitMyOrderApi");
    }


    private void checkError(){
        if (myOrderModelList.isEmpty()){
            binding.txtError.setVisibility(View.VISIBLE);
        }else {
            binding.txtError.setVisibility(View.GONE);
        }
    }

    private void onBackClick(){
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            if (isFromBottom) {
                getFragmentManager().popBackStackImmediate();
                BaseScreenActivity.binding.bottomNavigation.setVisibility(View.VISIBLE);
                BaseScreenActivity.binding.bottomNavigation.setSelectedItemId(R.id.menu_Account);
            } else {
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                BaseScreenActivity.callBack();
            }
        }
    }

}