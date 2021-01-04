package com.getsetgo.Fragment;

import android.content.Context;
import android.os.Bundle;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;
import com.getsetgo.Adapter.TotalUserAdapter;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.databinding.FragmentTotalUsersBinding;
import com.getsetgo.util.App;
import com.getsetgo.util.Click;
import com.getsetgo.util.P;

public class TotalDirectUsersFragment extends Fragment {

    FragmentTotalUsersBinding binding;
    static TotalUserAdapter totalUserAdapter;
    SearchUserFragment searchUserFragment;
    LinearLayoutManager layoutManager;
    boolean isScrolling = false;
    int currentItem, totalItems, scrollOutItems;
    Context context;
    public static boolean isSearch = false;

    static int page = 1;
    static JsonList directUserJsonList = new JsonList();
    static Json directUserJson = new Json();
    static boolean NextPage = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_total_users, container, false);
        View rootView = binding.getRoot();


        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Total Direct Users");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.VISIBLE);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event

                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStackImmediate();
                    directUserJsonList.clear();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        if (!isSearch) {
            if(directUserJsonList.size() <= 0) {
                initVariable();
                callTotalDirectUserApi(context);
            }else{
                setupRecyclerViewForTotalUser();
            }
        }
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.recyclerViewTotalUser.setLayoutManager(layoutManager);
        binding.recyclerViewTotalUser.setItemAnimator(new DefaultItemAnimator());
        totalUserAdapter = new TotalUserAdapter(context, directUserJsonList);
        binding.recyclerViewTotalUser.setAdapter(totalUserAdapter);

        binding.recyclerViewTotalUser.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItem = layoutManager.getChildCount();
                totalItems = layoutManager.getItemCount();
                scrollOutItems = layoutManager.findFirstVisibleItemPosition();
                if (isScrolling && (currentItem + scrollOutItems) >= totalItems) {
                    if (NextPage) {
                        isScrolling = false;
                        callTotalDirectUserApi(getContext());
                    }
                }
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

    private void initVariable() {
        page = 1;
        directUserJsonList.clear();
    }

    public static void setupRecyclerViewForTotalUser() {
        totalUserAdapter.notifyDataSetChanged();
    }

    private void loadFragment(View v) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isTotalUser",false);
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        searchUserFragment = new SearchUserFragment();
        searchUserFragment.setArguments(bundle);
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, searchUserFragment)
                .addToBackStack(null)
                .commit();


    }

    public static void callTotalDirectUserApi(Context context) {

        int Page = 1;
        String apiParam;
        LoadingDialog loadingDialog = new LoadingDialog(context);
        if (isSearch) {
            String name = SearchUserFragment.name;
            String email = SearchUserFragment.email;
            String contact = SearchUserFragment.contact;
            String has_purchased = SearchUserFragment.has_purchased;
            String courseId = SearchUserFragment.courseId;
            String crashId = SearchUserFragment.crashId;
            String start_date = SearchUserFragment.start_date;
            String end_date = SearchUserFragment.end_date;
            String is_affiliate = SearchUserFragment.is_affiliate;
            String parent_name = SearchUserFragment.parent_name;
            String program_service_id = SearchUserFragment.program_service_id;
            String regdPurposeId = SearchUserFragment.regdPurposeId;
            String purchase_start_date = SearchUserFragment.purchase_start_date;
            String purchase_end_date = SearchUserFragment.purchase_end_date;

            Page = SearchUserFragment.page;
            apiParam = "?name="+name + "&email="+email + "&contact="+contact + "&has_purchased="+has_purchased +
                    "&is_affiliate="+is_affiliate + "&start_date="+start_date + "&end_date="+end_date +
                    "&program_service_id="+ program_service_id + "&course_id="+courseId +
                    "&purchase_start_date="+purchase_start_date + "&purchase_end_date="+purchase_end_date +
                    "&parent_name="+parent_name + "&registration_purpose_id="+regdPurposeId + "&page=" + Page + "&per_page=10";
        }else{
            Page = page;
            apiParam = "?name=" + "&email=" + "&contact=" + "&has_purchased=" + "&is_affiliate=" + "&start_date=" + "&end_date=" + "&program_service_id=" + "&course_id=" + "&purchase_start_date=" + "&purchase_end_date=" +
                    "&parent_name=" + "&registration_purpose_id=" + "&page=" + Page + "&per_page=10";

        }

        Api.newApi(context, P.baseUrl + "regular_users/direct" + apiParam)
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
                        }))
                .onSuccess(Json1 -> {
                    if (Json1 != null) {
                        loadingDialog.dismiss();
                        if (Json1.getInt(P.status) == 0) {
                            H.showMessage(context, Json1.getString(P.err));
                        } else {
                            Json1 = Json1.getJson(P.data);
                            int numRows = Json1.getInt(P.num_rows);
                            JsonList jsonList = Json1.getJsonList(P.list);
                            if (jsonList != null && !jsonList.isEmpty()) {
                                directUserJsonList.addAll(jsonList);
                                directUserJson = Json1;
                                setupRecyclerViewForTotalUser();
                                if (directUserJsonList.size() < numRows) {
                                    if (isSearch) {
                                        SearchUserFragment.page++;
                                    }else{
                                        page++;
                                    }
                                    NextPage = true;
                                } else {
                                    NextPage = false;
                                    page = 1;
                                }
                            }

                        }
                    }

                }).run("regular_users/direct");
    }


}