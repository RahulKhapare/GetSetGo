package com.getsetgo.Fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;
import com.getsetgo.Adapter.SearchAdapter;
import com.getsetgo.Model.SearchModel;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.databinding.FragmentComposeBinding;
import com.getsetgo.databinding.FragmentSearchBinding;
import com.getsetgo.util.App;
import com.getsetgo.util.Click;
import com.getsetgo.util.JumpToLogin;
import com.getsetgo.util.P;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SearchFragment extends Fragment {

    FragmentSearchBinding binding;
    TextView[] t;
    TextView[] tC;

    private List<SearchModel> topSearchModelList;
    private List<SearchModel> yourSearchModelList;
    private SearchAdapter topAdapter;
    private SearchAdapter yourAdapter;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        View rootView = binding.getRoot();
        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Search");
        init();
        return rootView;
    }

    private void init() {
        BaseScreenActivity.binding.incFragmenttool.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    BaseScreenActivity.callBack();
                }
            }
        });

        topSearchModelList = new ArrayList<>();
        yourSearchModelList = new ArrayList<>();

        topAdapter = new SearchAdapter(getActivity(),topSearchModelList);
        binding.recyclerTopSearch.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerTopSearch.setAdapter(topAdapter);

        yourAdapter = new SearchAdapter(getActivity(),yourSearchModelList);
        binding.recyclerYourSearch.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerYourSearch.setAdapter(yourAdapter);

        setTopSearchData(HomeFragment.top_searches);

        binding.etxSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString();
                if (!TextUtils.isEmpty(newText) && newText.length()>2) {
                    hitSearchListApi(getActivity(),binding.etxSearch.getText().toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });



    }

    private void hitSearchListApi(Context context, String searchValue) {

//        LoadingDialog loadingDialog = new LoadingDialog(context, false);
        Api.newApi(context, P.baseUrl + "search" + "?q=" + searchValue)
                .setMethod(Api.GET)
                .onHeaderRequest(App::getHeaders)
                .onLoading(isLoading -> {
//                    if (isLoading)
//                        loadingDialog.show("loading...");
//                    else
//                        loadingDialog.hide();
                })
                .onError(() ->
                        MessageBox.showOkMessage(context, "Message", "Failed to login. Please try again", () -> {
//                            loadingDialog.dismiss();
                        }))
                .onSuccess(Json1 -> {
                    if (Json1 != null) {
                        JumpToLogin.call(Json1,context);
//                        loadingDialog.dismiss();
                        if (Json1.getInt(P.status) == 0) {
                            checkSearch(yourSearchModelList);
                        } else {
                            Json1 = Json1.getJson(P.data);
                            JsonList jsonList = Json1.getJsonList(P.course_list);
                            setYourSearchData(jsonList);
                        }
                    }

                }).run("hitSearchListApi");

    }

    private void setTopSearchData(JsonList jsonList){
        topSearchModelList.clear();
        if (jsonList==null || jsonList.size()==0){
            binding.txtTopSearch.setVisibility(View.GONE);
            topAdapter.notifyDataSetChanged();
        }else {
            binding.txtTopSearch.setVisibility(View.VISIBLE);
            for (Json json : jsonList){
                SearchModel model = new SearchModel();
                model.setId(json.getString(P.id));
                model.setSlug(json.getString(P.slug));
                model.setCourse_name(json.getString(P.course_name));
                topSearchModelList.add(model);
            }
            topAdapter.notifyDataSetChanged();
        }
    }

    private void setYourSearchData(JsonList jsonList){
        if (jsonList==null || jsonList.size()==0){
            checkSearch(yourSearchModelList);
        }else {
            for (Json json : jsonList){
                SearchModel model = new SearchModel();
                model.setId(json.getString(P.id));
                model.setSlug(json.getString(P.slug));
                model.setCourse_name(json.getString(P.course_name));
                if (!isContainsLocation(yourSearchModelList,model.getId())){
                    yourSearchModelList.add(model);
                }
            }
            yourAdapter.notifyDataSetChanged();
            checkSearch(yourSearchModelList);
        }
    }

    private void checkSearch(List<SearchModel> list){
        if (list.isEmpty()){
            binding.txtError.setVisibility(View.VISIBLE);
            binding.txtYourSearch.setVisibility(View.VISIBLE);
        }else {
            binding.txtError.setVisibility(View.GONE);
            binding.txtYourSearch.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public boolean isContainsLocation(Collection<SearchModel> c, String location) {
        for(SearchModel o : c) {
            if(o != null && o.getId().equals(location)) {
                return true;
            }
        }
        return false;
    }

}