package com.getsetgoapp.Fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;
import com.adoisstudio.helper.Session;
import com.getsetgoapp.R;
import com.getsetgoapp.activity.BaseScreenActivity;
import com.getsetgoapp.databinding.FragmentDashboardBinding;
import com.getsetgoapp.util.App;
import com.getsetgoapp.util.Click;
import com.getsetgoapp.util.JumpToLogin;
import com.getsetgoapp.util.P;
import com.squareup.picasso.Picasso;

public class DashBoardFragment extends Fragment {

    FragmentDashboardBinding binding;


    public DashBoardFragment() {
        // Required empty public constructor
    }

    public static DashBoardFragment newInstance() {
        DashBoardFragment fragment = new DashBoardFragment();

        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);
        View rootView = binding.getRoot();

        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Dashboard");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);
        // The callback can be enabled or disabled here or in handleOnBackPressed()
        hitBusinessDashboardApi(getActivity());
        onClick();
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event

                if(getFragmentManager().getBackStackEntryCount() > 0){
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    BaseScreenActivity.callBack();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void onClick() {

        binding.imvCopyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = binding.txtAffCode.getText().toString();
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", text);
                clipboard.setPrimaryClip(clip);
                H.showMessage(getActivity(),"Copy "+ clipboard.getText().toString());
            }
        });

        binding.imvCopyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = binding.txtAffLink.getText().toString();
                ClipboardManager clipboard = (ClipboardManager)getActivity(). getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label link", text);
                clipboard.setPrimaryClip(clip);
                H.showMessage(getActivity(), "Copy "+ clipboard.getText().toString());
            }
        });

        binding.rlTotalUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                TotalUsersFragment totalUsersFragment = new TotalUsersFragment();
                loadFragment(totalUsersFragment,v);
            }
        });

        binding.rlTotalDirectUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                TotalDirectUsersFragment totalDirectUsersFragment = new TotalDirectUsersFragment();
                loadFragment(totalDirectUsersFragment,v);
            }
        });

        binding.rlShareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                shareApp(getActivity(),new Session(getActivity()).getString(P.app_link));
            }
        });

        BaseScreenActivity.binding.incFragmenttool.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    BaseScreenActivity.callBack();
                }
            }
        });
    }

    public void shareApp(Context context,String link)
    {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, link);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    private void loadFragment(Fragment fragment,View v){
        TotalUsersFragment.isSearch = false;
        TotalDirectUsersFragment.isSearch = false;
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void hitBusinessDashboardApi(Context context) {
        LoadingDialog loadingDialog = new LoadingDialog(context, false);
        Api.newApi(context, P.baseUrl + "business_dashboard")
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
                        JumpToLogin.call(Json1, context);
                        loadingDialog.dismiss();
                        if (Json1.getInt(P.status) == 0) {
                            H.showMessage(context, Json1.getString(P.err));
                        } else if (Json1.getInt(P.status) == 1) {
                            Json1 = Json1.getJson(P.data);

                            String affiliate_link = Json1.getString(P.affiliate_link);
                            String affiliate_code = Json1.getString(P.affiliate_code);
                            String application_link = Json1.getString(P.application_link);
                            String direct_users = Json1.getString(P.direct_users);
                            String total_users = Json1.getString(P.total_users);


                            binding.txtAffLink.setText(checkString(affiliate_link,binding.txtAffLink));
                            binding.txtAffCode.setText(checkString(affiliate_code,binding.txtAffCode));
                            binding.txtTotalDirectUser.setText(checkString(direct_users,binding.txtTotalDirectUser));
                            binding.txtTotalUser.setText(checkString(total_users,binding.txtTotalUser));

                            Json user_details = Json1.getJson(P.user_details);
                            String user_name = user_details.getString(P.user_name);
                            String email = user_details.getString(P.email);
                            String user_id = user_details.getString(P.user_id);
                            String profile_completion_percentage = user_details.getString(P.profile_completion_percentage);
                            String sponsor_id = user_details.getString(P.sponsor_id);
                            String d_direct_users = user_details.getString(P.direct_users);
                            String d_total_users = user_details.getString(P.total_users);
                            String hierarchy = user_details.getString(P.hierarchy);

                            Session session = new Session(getActivity());
                            String profile_picture = session.getString(P.profile_picture);
                            if (!TextUtils.isEmpty(profile_picture)){
                                Picasso.get().load(profile_picture).placeholder(R.drawable.ic_profile_imag).error(R.drawable.ic_profile_imag).into(binding.imvViewProfile);
                            }
                            binding.txtName.setText(checkString(user_name,binding.txtName));
                            binding.txtEmail.setText(checkString(email,binding.txtEmail));
                            binding.txtUserId.setText(checkString("User Id - "+user_id,binding.txtUserId));
                            if (!TextUtils.isEmpty(profile_completion_percentage) && !profile_completion_percentage.equals("null")){
                                binding.txtStatus.setText(profile_completion_percentage + "% Completed");
                                int persent = Integer.parseInt(profile_completion_percentage);
                                binding.progressBarDash.setProgress(persent);
                            }


                            Json franchise_json = Json1.getJson(P.franchise);
                            String f_user_name = franchise_json.getString(P.user_name);
                            String f_contact = franchise_json.getString(P.contact);

                            binding.txtFranchiseName.setText(checkString(f_user_name,binding.txtFranchiseName));
                            binding.txtFranchiseConatct.setText(checkString(f_contact,binding.txtFranchiseConatct));

                            Json master_franchise = Json1.getJson(P.master_franchise);
                            String m_user_name = master_franchise.getString(P.user_name);
                            String m_contact = master_franchise.getString(P.contact);

                            binding.txtMasterName.setText(checkString(m_user_name,binding.txtMasterName));
                            binding.txtMasterContact.setText(checkString(m_contact,binding.txtMasterContact));

                            Json advisor = Json1.getJson(P.advisor);
                            String a_user_name = advisor.getString(P.user_name);
                            String a_contact = advisor.getString(P.contact);

                            binding.txtAdviserName.setText(checkString(a_user_name,binding.txtAdviserName));
                            binding.txtAvisorContact.setText(checkString(a_contact,binding.txtAvisorContact));
                        }
                    }

                }).run("hitBusinessDashboardApi");
    }

    private String checkString(String string, TextView textView){
        String value = "";
        if (!TextUtils.isEmpty(string) && !string.equals("null")){
            value =  string;
        }else {
            textView.setVisibility(View.GONE);
        }
        return value;
    }

}