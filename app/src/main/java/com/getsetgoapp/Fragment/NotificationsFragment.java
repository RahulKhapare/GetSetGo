package com.getsetgoapp.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import com.getsetgoapp.Adapter.NotificationAdapter;
import com.getsetgoapp.Model.NotificationModel;
import com.getsetgoapp.R;
import com.getsetgoapp.activity.BaseScreenActivity;
import com.getsetgoapp.databinding.FragmentNotificationsBinding;
import com.getsetgoapp.util.App;
import com.getsetgoapp.util.JumpToLogin;
import com.getsetgoapp.util.P;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    FragmentNotificationsBinding binding;
    NotificationAdapter notificationAdapter;
    List<NotificationModel> notificationModelList;
    boolean isFromBottom;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager linearLayoutManager;
    int count;
    int pageCount = 1;

    public NotificationsFragment() {
    }

    public static NotificationsFragment newInstance() {
        NotificationsFragment fragment = new NotificationsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notifications, container, false);
        View rootView = binding.getRoot();

        isFromBottom = getArguments().getBoolean("isFromBottom");
        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Notifications");
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

        notificationModelList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(getContext(),notificationModelList);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerViewNotifications.setLayoutManager(linearLayoutManager);
        binding.recyclerViewNotifications.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewNotifications.setAdapter(notificationAdapter);

        onClick();
        hitNotificationApi(getActivity(),pageCount);
        setPagination();

    }


    private void setPagination(){
        binding.recyclerViewNotifications.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    if (notificationModelList!=null && !notificationModelList.isEmpty()){
                        if (notificationModelList.size()<count){
                            pageCount++;
                            hitNotificationApi(getActivity(),pageCount);
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

    private void hitNotificationApi(Context context,int pageCount) {
        String page = "";
        if (pageCount!=0){
            page = "page="+ pageCount + "";
        }
        LoadingDialog loadingDialog = new LoadingDialog(context, false);
        Api.newApi(context, P.baseUrl + "user_notifications?"+page)
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
                            count = Json1.getInt(P.num_rows);
                            JsonList list = Json1.getJsonList(P.list);
                            if (list!=null&&list.size()!=0){
                                for (Json jsonData : list){
                                    String title = jsonData.getString(P.title);
                                    String description = jsonData.getString(P.description);
                                    String action = jsonData.getString(P.action);
                                    String action_data = jsonData.getString(P.action_data);
                                    String image = jsonData.getString(P.image);

                                    NotificationModel model = new NotificationModel();
                                    model.setTitle(title);
                                    model.setDescription(description);
                                    model.setAction(action);
                                    model.setAction_data(action_data);
                                    model.setImage(image);

                                    notificationModelList.add(model);

                                }
                            }
                            notificationAdapter.notifyDataSetChanged();
                            checkError();
                        }
                    }
                }).run("hitNotificationApi");
    }


    private void checkError(){
        if (notificationModelList.isEmpty()){
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