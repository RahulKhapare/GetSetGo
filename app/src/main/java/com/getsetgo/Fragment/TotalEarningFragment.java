package com.getsetgo.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;
import com.adoisstudio.helper.Session;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.databinding.FragmentTotalearningBinding;
import com.getsetgo.util.P;

public class TotalEarningFragment extends Fragment {


    FragmentTotalearningBinding binding;
    private LoadingDialog loadingDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_totalearning, container, false);
        View rootView = binding.getRoot();
        init(rootView);
        return rootView;

    }
    private void init(View view){
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);
        callTotalEarningApi();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    private void callTotalEarningApi() {
        loadingDialog = new LoadingDialog(getActivity());

        String apiParam = "?create_date_start=" + "&create_date_end=" + "&page=" + "&per_page=" ;

        Api.newApi(getActivity(), P.baseUrl + "total_earning" + apiParam ).setMethod(Api.GET)
                .onLoading(isLoading -> {
                    if (!getActivity().isDestroyed()) {
                        if (isLoading)
                            loadingDialog.show("loading...");
                        else
                            loadingDialog.hide();
                    }
                })
                .onError(() ->
                        MessageBox.showOkMessage(getActivity(), "Message", "Failed to login. Please try again", () -> {
                        }))
                .onSuccess(Json1 -> {
                    if (Json1 != null) {
                        loadingDialog.dismiss();
                        if (Json1.getInt(P.status) == 0) {
                            H.showMessage(getActivity(), Json1.getString(P.err));
                        } else {
                            Json1 = Json1.getJson(P.data);
                            Session session = new Session(getActivity());

                        }
                    }

                }).run("total_earning");
    }
}