package com.getsetgoapp.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
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
import com.adoisstudio.helper.Session;
import com.getsetgoapp.Adapter.CommentAdapter;
import com.getsetgoapp.Model.CommentModel;
import com.getsetgoapp.Model.CourseDocumentModel;
import com.getsetgoapp.Model.CourseLinkModel;
import com.getsetgoapp.Model.CourseModuleModel;
import com.getsetgoapp.Model.VideoUrlModel;
import com.getsetgoapp.R;
import com.getsetgoapp.activity.BaseScreenActivity;
import com.getsetgoapp.activity.VideoPlayActivity;
import com.getsetgoapp.adapterview.CourseChildAdapter;
import com.getsetgoapp.adapterview.CourseChildModel;
import com.getsetgoapp.adapterview.CourseDocumentAdapter;
import com.getsetgoapp.adapterview.CourseLinkAdapter;
import com.getsetgoapp.adapterview.CourseModuleAdapter;
import com.getsetgoapp.adapterview.VideoQualityAdapter;
import com.getsetgoapp.databinding.FragmentMyCourseDetailsBinding;
import com.getsetgoapp.util.App;
import com.getsetgoapp.util.Click;
import com.getsetgoapp.util.Config;
import com.getsetgoapp.util.JumpToLogin;
import com.getsetgoapp.util.P;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MyCourseDetailFragment extends Fragment implements Player.EventListener, CourseChildAdapter.childAction, CourseDocumentAdapter.onClick, VideoQualityAdapter.onClick,
        CourseModuleAdapter.click ,CourseLinkAdapter.onClick{

    private FragmentMyCourseDetailsBinding binding;
    private Context context;
    private SimpleExoPlayer exoPlayer;
    private PlayerView playerView;

    private ImageView imgFullScreen;
    private ImageView imgQuality;

    private String courseSlug;

    public static String videoPlayPath = "";
    public static final int REQUEST_CODE = 101;
    public static final int RESULT_CODE = 12;
    public static long lastVideoPosition = 0;
    public static int lastSelectedPosition = 0;
    private CourseModuleAdapter courseModuleAdapter;
    private CourseDocumentAdapter courseDocumentAdapter;
    private CourseLinkAdapter courseLinkAdapter;

    private List<CourseModuleModel> courseModuleModelList;
    private List<CourseDocumentModel> courseDocumentModelList;
    private List<CourseLinkModel> courseLinkModelList;
    public static List<VideoUrlModel> videoUrlModelList;
    private JsonList previewJsonList = new JsonList();
    private List<CommentModel> commentModelList;
    private CommentAdapter commentAdapter;

    private int childVideoPosition;
    private int actualSize;

    private Dialog dialog;
    private LoadingDialog loadingDialog;
    private String courseId = "";

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager linearLayoutManager;
    int count;
    int pageCount = 1;
    boolean isPagination =  true;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_course_details, container, false);
            context = inflater.getContext();
            courseSlug = Config.myCourseSlug;
            BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Course Details");
            BaseScreenActivity.binding.incFragmenttool.llSubCategory.setVisibility(View.GONE);
            BaseScreenActivity.binding.bottomNavigation.setVisibility(View.GONE);
            initView();
        }
        return binding.getRoot();
    }

    private void initView() {

        BaseScreenActivity.binding.incFragmenttool.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackClick();
            }
        });

        loadingDialog = new LoadingDialog(context);
        imgFullScreen = binding.playerView.findViewById(R.id.exo_fullscreen_icon);
        imgQuality = binding.playerView.findViewById(R.id.ivVideoQuality);

        videoPlayPath = "";
        lastVideoPosition = 0;
        lastSelectedPosition = 0;
        videoUrlModelList = new ArrayList<>();

        courseModuleModelList = new ArrayList<>();
        courseModuleAdapter = new CourseModuleAdapter(context, courseModuleModelList, MyCourseDetailFragment.this, true);
        binding.recyclerModule.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerModule.setNestedScrollingEnabled(false);
        binding.recyclerModule.setAdapter(courseModuleAdapter);

        courseDocumentModelList = new ArrayList<>();
        courseDocumentAdapter = new CourseDocumentAdapter(context, courseDocumentModelList, MyCourseDetailFragment.this);
        binding.recyclerDocument.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerDocument.setNestedScrollingEnabled(false);
        binding.recyclerDocument.setAdapter(courseDocumentAdapter);

        courseLinkModelList = new ArrayList<>();
        courseLinkAdapter = new CourseLinkAdapter(context, courseLinkModelList,MyCourseDetailFragment.this);
        binding.recyclerLink.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerLink.setNestedScrollingEnabled(false);
        binding.recyclerLink.setAdapter(courseLinkAdapter);

        commentModelList = new ArrayList<>();
        commentAdapter = new CommentAdapter(context,commentModelList);
        linearLayoutManager = new LinearLayoutManager(context);
        binding.recyclerComment.setLayoutManager(linearLayoutManager);
        binding.recyclerComment.setHasFixedSize(true);
        binding.recyclerComment.setNestedScrollingEnabled(false);
        binding.recyclerComment.setAdapter(commentAdapter);

        isPagination = true;
        callCourseDetailsApi(context, courseSlug);
        onClick();
        callGetCommentAPI(context,pageCount);

    }

    private void setPagination(){

        if (binding.nestedScroll != null) {
            binding.nestedScroll.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                if (scrollY == ( v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight() )) {
                    if (commentModelList!=null && !commentModelList.isEmpty()){
                        if (commentModelList.size()<count){
                            pageCount++;
                            callGetCommentAPI(context,pageCount);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Config.isHalfScreen) {
            Config.isHalfScreen = false;
            playVideo(videoPlayPath, true);
        }

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                onBackClick();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    private void onClick() {

        binding.btnSubmitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (TextUtils.isEmpty(binding.etxComments.getText().toString().trim())){
                    H.showMessage(context,"Please enter comment");
                }else if (binding.etxComments.getText().toString().trim().length()<5){
                    H.showMessage(context,"Please enter valid comment");
                }else{
                    callPostCommentAPI(context,courseId,binding.etxComments.getText().toString().trim());
                }
            }
        });

        imgFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                exoPlayer.pause();
                lastVideoPosition = exoPlayer.getCurrentPosition();
                Intent intent = new Intent(getContext(), VideoPlayActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        imgQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (videoUrlModelList != null && !videoUrlModelList.isEmpty()) {
                    qualityUrlDialog(videoUrlModelList);
                }
            }
        });

        binding.btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (childVideoPosition > 0) {
                    childVideoPosition--;
                    updateVideoData(childVideoPosition);
                }
            }
        });

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (childVideoPosition < actualSize) {
                    childVideoPosition++;
                    updateVideoData(childVideoPosition);
                }
            }

        });

    }

    private void updateVideoData(int position) {
        lastSelectedPosition= 0;
        lastVideoPosition = 0;
        try {
            Json json = previewJsonList.get(position);
            JsonList video_urls = json.getJsonList(P.video_urls);
            if (video_urls != null && video_urls.size() != 0) {
                updateButton(position);
                setVideoData(video_urls);
            }
        }catch (Exception e)
        {
        }

    }

    private void updateButton(int position) {

        if (position == 0) {
            binding.btnPrevious.setBackground(getResources().getDrawable(R.drawable.bg_previous));
            binding.btnPrevious.setTextColor(getResources().getColor(R.color.colorPrimary));
            binding.btnNext.setBackground(getResources().getDrawable(R.drawable.bg_next));
            binding.btnNext.setTextColor(getResources().getColor(R.color.colorWhite));
        } else if (position == actualSize) {
            binding.btnPrevious.setBackground(getResources().getDrawable(R.drawable.bg_next));
            binding.btnPrevious.setTextColor(getResources().getColor(R.color.colorWhite));
            binding.btnNext.setBackground(getResources().getDrawable(R.drawable.bg_previous));
            binding.btnNext.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else if (position < actualSize) {
            binding.btnPrevious.setBackground(getResources().getDrawable(R.drawable.bg_next));
            binding.btnPrevious.setTextColor(getResources().getColor(R.color.colorWhite));
            binding.btnNext.setBackground(getResources().getDrawable(R.drawable.bg_next));
            binding.btnNext.setTextColor(getResources().getColor(R.color.colorWhite));
        }

    }


    private void playVideo(String videoPath, boolean replay) {
        Log.e("TAG", "playVideo: " + videoPath);
        videoPlayPath = videoPath;
        exoPlayer = new SimpleExoPlayer.Builder(context).build();
        playerView = binding.playerView.findViewById(R.id.playerView);
        binding.playerView.setPlayer(exoPlayer);
        binding.playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.addListener(this);

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), Util.getUserAgent(getApplicationContext(), getApplicationContext().getString(R.string.app_name)));

        MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(videoPath));

        exoPlayer.prepare(videoSource);
        exoPlayer.setMediaItem(MediaItem.fromUri(videoPath));

        if (replay) {
            if (lastVideoPosition != C.TIME_UNSET) {
                exoPlayer.seekTo(lastVideoPosition);
            }
            exoPlayer.play();
        } else {
            exoPlayer.play();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null) {
            exoPlayer.stop(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (exoPlayer != null) {
            exoPlayer.pause();
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if (playbackState == Player.STATE_BUFFERING) {
            binding.pbVideoPlayer.setVisibility(View.VISIBLE);

        } else if (playbackState == Player.STATE_READY || playbackState == Player.STATE_ENDED) {
            binding.pbVideoPlayer.setVisibility(View.INVISIBLE);
        }
    }


    private void callCourseDetailsApi(Context context, String slug) {

        LoadingDialog loadingDialog = new LoadingDialog(context, false);
        Api.newApi(context, P.baseUrl + "active_course" + "/" + slug)
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
                        } else {
                            Json1 = Json1.getJson(P.data);
                            setData(Json1);
                        }
                    }

                }).run("callCourseDetailsApi");

    }


    private void setData(Json jsonData) {

        Json courseObject = jsonData.getJson(P.course);
        JsonList module_list = jsonData.getJsonList(P.module_list);

        courseId = courseObject.getString(P.id);
        String courseName = courseObject.getString(P.course_name);
        String progress = courseObject.getString(P.complete_percentage);

        if (checkString(progress, binding.txtCourseName)) {
            binding.txtCourseName.setText(courseName);
        }

        if (checkString(progress, binding.txtPercentage)) {
            binding.txtPercentage.setText(progress + "%" + " " + "Completed");
        }

        if (checkString(progress)) {
            try {
                binding.progressBar.setProgress(Integer.parseInt(progress));
            }catch (Exception e){
                binding.progressBar.setProgress(0);
            }
        }

        setModuleData(module_list);

    }


    private void setModuleData(JsonList jsonList) {

        courseModuleModelList.clear();

        for (Json json : jsonList) {
            CourseModuleModel moduleModel = new CourseModuleModel();
            moduleModel.setId(json.getString(P.module_id));
            moduleModel.setModule_name(json.getString(P.module_name));
            moduleModel.setVideos(json.getJsonList(P.videos));
            moduleModel.setClickFlag(false);
            courseModuleModelList.add(moduleModel);
        }

        if (courseModuleModelList.isEmpty()) {
            binding.txtModule.setVisibility(View.VISIBLE);
        } else {
            binding.txtModule.setVisibility(View.VISIBLE);
        }

        courseModuleAdapter.notifyDataSetChanged();
    }

    private void updateButtonView(JsonList jsonList){
        if (jsonList==null || jsonList.size()<=1){
            binding.lnrButtonView.setVisibility(View.GONE);
        }else {
            binding.lnrButtonView.setVisibility(View.VISIBLE);
        }
    }

    private void updateVideoPREV_NEXTListData(JsonList videoData){
        previewJsonList.clear();
        if (videoData != null && videoData.size() != 0) {
            for (Json json : videoData) {
                previewJsonList.add(json);
            }
        }

        actualSize = previewJsonList.size();
        actualSize--;
    }

    @Override
    public void moduleSelection(CourseModuleModel moduleModel) {

//        NOT USING NOW
//        updateVideoPREV_NEXTListData(moduleModel.getVideos());

    }

    private void updateVideoStatus(String course_id, String video_id) {
//        ProgressView.show(context,loadingDialog);
        Json j = new Json();
        j.addString(P.course_id, course_id);
        j.addString(P.video_id, video_id);
        Api.newApi(context, P.baseUrl + "add_video_status").addJson(j)
                .setMethod(Api.POST)
                .onHeaderRequest(App::getHeaders)
                .onError(() -> {
//                    ProgressView.dismiss(loadingDialog);
                })
                .onSuccess(json ->
                {
                    JumpToLogin.call(json, context);
                    if (json.getInt(P.status) == 1) {
                    } else {
                    }
//                    ProgressView.dismiss(loadingDialog);
                })
                .run("updateVideoStatus");
    }

    @Override
    public void calledChild(CourseChildModel model, int position) {
        binding.txtVideoTitle.setText(model.getVideo_title());
        updateVideoPREV_NEXTListData(model.getMainVideoList());
        updateVideoStatus(courseId, model.getVideo_id());
        lastVideoPosition = 0;
        lastSelectedPosition = 0;
        JsonList videoUrlList = model.getVideo_urls();
        JsonList additionFileList = model.getAdditional_files();
        JsonList additionLinkList = model.getAdditional_links();

        if (videoUrlList != null && videoUrlList.size() != 0) {
            updateButtonView(previewJsonList);
            childVideoPosition = position;
            updateButton(childVideoPosition);
            binding.playerView.findViewById(R.id.ivVideoQuality).setVisibility(View.VISIBLE);
            setVideoData(videoUrlList);
        } else {
            binding.playerView.findViewById(R.id.ivVideoQuality).setVisibility(View.GONE);
        }

        if (additionFileList != null && additionFileList.size() != 0) {
            binding.txtAdditionalFiles.setVisibility(View.VISIBLE);
            setAdditionalFilesData(additionFileList);
        } else {
            binding.txtAdditionalFiles.setVisibility(View.GONE);
            courseDocumentModelList.clear();
            courseDocumentAdapter.notifyDataSetChanged();
        }

        if (additionLinkList != null && additionLinkList.size() != 0) {
            binding.txtAdditionalLinked.setVisibility(View.VISIBLE);
            setAdditionalLinkedData(additionLinkList);
        } else {
            binding.txtAdditionalLinked.setVisibility(View.GONE);
            courseLinkModelList.clear();
            courseLinkAdapter.notifyDataSetChanged();
        }

        binding.nestedScroll.scrollTo(0, 0);
    }

    private void setVideoData(JsonList jsonList) {

        videoUrlModelList.clear();

        for (Json json : jsonList) {
            VideoUrlModel model = new VideoUrlModel();
            model.setType(json.getString(P.type));
            model.setLink(json.getString(P.link));
            model.setQuality(json.getString(P.quality));
            videoUrlModelList.add(model);
        }

        String path = videoUrlModelList.get(0).getLink();
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
        playVideo(path, false);
    }

    private void qualityUrlDialog(List<VideoUrlModel> videoUrlModelList) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_video_url_dialog);

        RecyclerView recyclerQuality = dialog.findViewById(R.id.recyclerQuality);
        recyclerQuality.setLayoutManager(new LinearLayoutManager(context));
        VideoQualityAdapter adapter = new VideoQualityAdapter(context, videoUrlModelList, MyCourseDetailFragment.this, lastSelectedPosition, 1);
        recyclerQuality.setAdapter(adapter);

        dialog.setCancelable(true);
        dialog.show();
//        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void qualityClick(VideoUrlModel model, int position) {
        if (dialog != null) {
            dialog.dismiss();
        }
        if (exoPlayer != null) {
            lastSelectedPosition = position;
            lastVideoPosition = exoPlayer.getCurrentPosition();
            exoPlayer.release();
            exoPlayer = null;
        }
        playVideo(model.getLink(), true);
    }

    private void setAdditionalFilesData(JsonList jsonList) {

        courseDocumentModelList.clear();

        for (Json json : jsonList) {
            CourseDocumentModel model = new CourseDocumentModel();
            model.setFile(json.getString(P.file));
            model.setFile_name(json.getString(P.file_name));
            courseDocumentModelList.add(model);
        }

        courseDocumentAdapter.notifyDataSetChanged();
    }

    private void setAdditionalLinkedData(JsonList jsonList) {

        courseLinkModelList.clear();

        for (Json json : jsonList) {
            CourseLinkModel model = new CourseLinkModel();
            model.setLink(json.getString(P.link));
            model.setLink_name(json.getString(P.link_name));
            courseLinkModelList.add(model);
        }

        courseLinkAdapter.notifyDataSetChanged();
    }


    @Override
    public void downloadPDF(CourseDocumentModel model) {
        ((BaseScreenActivity) context).checkPDFPath(model);
    }

    @Override
    public void onDestroyView() {
        if (binding.getRoot() != null) {
            ViewGroup parentViewGroup = (ViewGroup) binding.getRoot().getParent();

            if (parentViewGroup != null) {
                parentViewGroup.removeAllViews();
            }
        }

        super.onDestroyView();
    }

    public static MyCourseDetailFragment newInstance() {
        MyCourseDetailFragment fragment = new MyCourseDetailFragment();
        return fragment;
    }

    private boolean checkString(String string, TextView textView) {
        boolean value = true;

        if (TextUtils.isEmpty(string) || string.equals("null")) {
            value = false;
            textView.setVisibility(View.GONE);
        }
        return value;
    }

    private boolean checkString(String string) {
        boolean value = true;

        if (TextUtils.isEmpty(string) || string.equals("null")) {
            value = false;
        }
        return value;
    }

    private void onBackClick() {
        if (Config.POP_HOME) {
            Config.POP_HOME = false;
            getFragmentManager().popBackStackImmediate();
            BaseScreenActivity.binding.bottomNavigation.setVisibility(View.VISIBLE);
        } else {
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            BaseScreenActivity.callBack();
        }
    }

    @Override
    public void onLinkClick(String link) {
        ((BaseScreenActivity)getActivity()).openLink(link);
    }


    private void callPostCommentAPI(Context context,String courseId,String comment) {
        LoadingDialog loadingDialog = new LoadingDialog(context, false);

        Json json = new Json();
        json.addString(P.course_id, courseId);
        json.addString(P.comment, comment);

        Api.newApi(context, P.baseUrl + "add_course_comment")
                .addJson(json)
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
                        JumpToLogin.call(Json1, context);
                        loadingDialog.dismiss();
                        if (Json1.getInt(P.status) == 1) {
                            binding.etxComments.setText("");
                            H.showMessage(context, Json1.getString(P.msg));
                            commentModelList.clear();
                            pageCount = 1;
                            callGetCommentAPI(context,pageCount);
                        } else {
                            H.showMessage(context, Json1.getString(P.err));
                        }
                    }

                }).run("callPostCommentAPI");
    }

    private void callGetCommentAPI(Context context,int pageCount) {
        LoadingDialog loadingDialog = new LoadingDialog(context, false);
        Api.newApi(context, P.baseUrl + "active_course_comments/"+courseSlug+"?"+"page="+pageCount+"&per_page=10")
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
                        if (Json1.getInt(P.status) == 1) {
                            loading = false;
                            Json json = Json1.getJson(P.data);
                            count = json.getInt(P.num_rows);
                            JsonList commentList = json.getJsonList(P.list);

                            if (commentList!=null && commentList.size()!=0){
                                for (Json jsonData : commentList){
                                    CommentModel model = new CommentModel();
                                    model.setInitials(jsonData.getString(P.initials));
                                    model.setComment(jsonData.getString(P.comment));
                                    model.setName(jsonData.getString(P.name));
                                    model.setLastname(jsonData.getString(P.lastname));
                                    model.setAdd_date(jsonData.getString(P.add_date));
                                    commentModelList.add(model);
                                }
                            }
                            commentAdapter.notifyDataSetChanged();
                            if (commentModelList.isEmpty()){
                                binding.txtUserComment.setVisibility(View.GONE);
                            }

                            if (isPagination){
                                isPagination = false;
                                setPagination();
                            }
                        }
                    }

                }).run("callGetCommentAPI");
    }



}
