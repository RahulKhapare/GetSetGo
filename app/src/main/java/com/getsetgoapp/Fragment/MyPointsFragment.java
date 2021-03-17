package com.getsetgoapp.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;
import com.getsetgoapp.Model.MyPointsModel;
import com.getsetgoapp.R;
import com.getsetgoapp.activity.BaseScreenActivity;
import com.getsetgoapp.adapterview.MyPointsAdapter;
import com.getsetgoapp.databinding.FragmentMyPointsBinding;
import com.getsetgoapp.util.App;
import com.getsetgoapp.util.Click;
import com.getsetgoapp.util.JumpToLogin;
import com.getsetgoapp.util.P;
import com.getsetgoapp.util.RemoveHtml;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MyPointsFragment extends Fragment implements MyPointsAdapter.onClick{

    FragmentMyPointsBinding binding;
    Context context;
    private List<MyPointsModel> myPointsModelList;
    private MyPointsAdapter adapter;
    MyPointsSarchFragment searchTransactionsFragment;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager linearLayoutManager;
    int count;
    int pageCount = 1;
    public static boolean MY_POINTS_FILTER = false;
    public static String START_DATE = "";
    public static String END_DATE = "";
    public static String ACTION_TYPE = "";

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
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.VISIBLE);
        init();
        return rootView;
    }


    private void init(){

        myPointsModelList = new ArrayList<>();
        adapter = new MyPointsAdapter(getActivity(),myPointsModelList,MyPointsFragment.this);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerMyPoints.setLayoutManager(linearLayoutManager);
        binding.recyclerMyPoints.setAdapter(adapter);

        callMyPointsDetailsApi(getActivity(),false,pageCount);
        setPagination();

        BaseScreenActivity.binding.incFragmenttool.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressClick();
            }
        });

        BaseScreenActivity.binding.incFragmenttool.ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Click.preventTwoClick(view);
                loadFragment(view);
            }
        });
    }

    private void loadFragment(View v) {
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        searchTransactionsFragment = new MyPointsSarchFragment();
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, searchTransactionsFragment)
                .addToBackStack(null)
                .commit();
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
                            Log.e("TAG", "callMyPointsDetailsApiROW: " + count  + "");
                            callMyPointsDetailsApi(getActivity(),false,pageCount);
                        }
                    }
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.VISIBLE);
        if (MY_POINTS_FILTER){
            pageCount = 1;
            MY_POINTS_FILTER = false;
            callMyPointsDetailsApi(getActivity(),true,pageCount);
        }
    }

    private void callMyPointsDetailsApi(Context context,boolean isFilter,int pageCount) {

        String api = "course_points?" + getPaginationUrl(pageCount);
        LoadingDialog loadingDialog = new LoadingDialog(context, false);
        Api.newApi(context, P.baseUrl + api )
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
                            checkData(myPointsModelList);
                        }))
                .onSuccess(Json1 -> {
                    if (Json1 != null) {
                        JumpToLogin.call(Json1,context);
                        loadingDialog.dismiss();
                        if (isFilter){
                            myPointsModelList.clear();
                        }
                        if (Json1.getInt(P.status) == 0) {
                            checkData(myPointsModelList);
                            H.showMessage(context, Json1.getString(P.err));
                        } else {
                            Json1 = Json1.getJson(P.data);
                            count = Json1.getInt(P.num_rows);
                            JsonList list = Json1.getJsonList(P.list);
                            if (list!=null && list.size()!=0){
                                for (Json jsonData : list){
                                    MyPointsModel model = new MyPointsModel();
                                    model.setPoints(jsonData.getString(P.points));
                                    model.setCreate_date_text(jsonData.getString(P.create_date_text));
                                    model.setAction_type(jsonData.getString(P.action_type));
                                    model.setDescription(jsonData.getString(P.description));
                                    model.setPoints_type(jsonData.getString(P.points_type));
                                    model.setUsername(jsonData.getString(P.username));
                                    model.setParent_username(jsonData.getString(P.parent_username));
                                    model.setCourses(jsonData.getString(P.courses));
                                    model.setJson(jsonData);
                                    myPointsModelList.add(model);
                                }
                            }
                            adapter.notifyDataSetChanged();
                            checkData(myPointsModelList);
                        }
                    }

                }).run("callMyPointsDetailsApi");

    }

    private void checkData(List<MyPointsModel> myPointsModelList){
        if (myPointsModelList.isEmpty()){
            binding.txtError.setVisibility(View.VISIBLE);
        }else {
            binding.txtError.setVisibility(View.GONE);
        }
    }

    private String getPaginationUrl(int pageCount){
        String url = "";

        if (!TextUtils.isEmpty(START_DATE)){
            url = url + "&create_date_start=" + START_DATE;
        }

        if (!TextUtils.isEmpty(END_DATE)){
            url = url + "&create_date_end=" + END_DATE;
        }

        if (!TextUtils.isEmpty(ACTION_TYPE)){
            url = url + "&action_type=" + ACTION_TYPE;
        }

        url = url + "&page=" + pageCount+"";

//        if (isFilter){
//            url = url + "&page=" + "1";
//            pageCount = 1;
//        }else {
//            url = url + "&page=" + pageCount+"";
//        }

        url = url + "&per_page=" + "10";

        return url;
    }

    private void onBackPressClick() {
        if (binding.nestedScroll.getVisibility()==View.VISIBLE){
            visibleMyPoints();
        }else {
            if (getFragmentManager().getBackStackEntryCount() > 0) {
                clearData();
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                BaseScreenActivity.callBack();
            }
        }

    }


    private void clearData(){
        START_DATE = "";
        END_DATE = "";
        ACTION_TYPE = "";
    }

    @Override
    public void showData(Json json) {
        setData(json);
    }

    private void setData(Json jsonData){
        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Point's History");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);
        binding.recyclerMyPoints.setVisibility(View.GONE);
        binding.nestedScroll.setVisibility(View.VISIBLE);

        Json json = jsonData;
        binding.txtTitleName.setText(checkString(json.getString("username")));
        binding.txtPoints.setText(checkString(json.getString("points")));
        binding.txtDateTime.setText(checkString(json.getString("create_date_text")));
        binding.txtAction.setText(checkString(json.getString("action_type")));
        binding.txtParent.setText(checkString(json.getString("parent_username")));
        binding.txtCourse.setText(RemoveHtml.html2text(checkString(json.getString("courses"))));
        binding.txtDescription.setText(RemoveHtml.html2text(checkString(json.getString("description"))));

        binding.btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visibleMyPoints();
            }
        });
    }

    private String checkString(String string){
        String value = "";
        if (!TextUtils.isEmpty(string) || !string.equals("null")){
            value = string;
        }
        return value;
    }

    private void visibleMyPoints(){
        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("My Points");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.VISIBLE);
        binding.nestedScroll.setVisibility(View.GONE);
        binding.recyclerMyPoints.setVisibility(View.VISIBLE);
    }
}
