package co.nayn.nayn.ui.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import co.nayn.nayn.R;
import co.nayn.nayn.base.BaseFragment;
import co.nayn.nayn.constants.NaynConstants;
import co.nayn.core.data.model.Category;
import co.nayn.core.data.model.Posts;
import co.nayn.nayn.data.local.db.AppDatabase;
import co.nayn.nayn.data.local.db.SavedNew;
import co.nayn.nayn.ui.main.MainActivity;
import co.nayn.nayn.widgets.CustomFontTextView;
import co.nayn.nayn.widgets.NaynGradientButton;

public class NewsDetailFragment extends BaseFragment implements NewsDetailContract.View {

    @Inject
    AppDatabase database;
    @BindView(R.id.back)
    View back;
    @BindView(R.id.toolbar_tvTitle)
    View save_later_title;
    @BindView(R.id.save_later)
    ImageView save_later;
    @BindView(R.id.news_detail_scroll_view)
    ScrollView newsDetailScrollView;
    @BindView(R.id.news_detail_root_layout)
    LinearLayout rootLayout;
    @BindView(R.id.news_detail_image)
    AppCompatImageView newsDetailImage;
    @BindView(R.id.news_category_detail_button)
    NaynGradientButton categoryDetailButton;
    @BindView(R.id.news_detail_sum_text)
    CustomFontTextView newsDetailSumText;
    @BindView(R.id.news_detail_time_text)
    CustomFontTextView newsDetailTimeText;
    @BindView(R.id.share_whatsapp_button)
    AppCompatButton shareWhatsappButton;
    @BindView(R.id.share_facebook_button)
    AppCompatButton shareFacebookButton;
    @BindView(R.id.share_messenger_button)
    AppCompatButton shareMessengerButton;
    @BindView(R.id.share_twitter_button)
    AppCompatButton shareTwitterButton;
    @BindView(R.id.share_fab)
    FloatingActionButton shareFab;
    @BindView(R.id.news_detail_webview)
    WebView newsDetailWebView;

    private Posts post;
    private NewsDetailPresenter newsDetailPresenter;

    public static NewsDetailFragment newInstance(Posts post){
        Bundle args = new Bundle();
        args.putSerializable(NaynConstants.POST_KEY, post);
        NewsDetailFragment newsDetailFragment = new NewsDetailFragment();
        newsDetailFragment.setArguments(args);
        return newsDetailFragment;
    }

    @Optional
    @OnClick({R.id.share_facebook_button, R.id.share_messenger_button, R.id.share_twitter_button, R.id.share_whatsapp_button, R.id.share_fab})
    void onShareClicked(View view){
        if(post != null){
            sharePost(view.getId(),post.getTitle(), post.getUrl());
        }
    }

    @OnClick(R.id.news_category_detail_button)
    void onCategoryDetailClicked(){
        if(post.getCategories().size() < 2 && !post.getCategories().isEmpty()){
            ((MainActivity) getActivity()).initFragment(post.getCategories().get(0));
        }else{
            ((MainActivity) getActivity()).initFragment(post.getCategories().get(1));

        }
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_news_detail;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUnbinder(ButterKnife.bind(this,view));
        init();
        if(this.getArguments() != null){
            post = (Posts) this.getArguments().getSerializable(NaynConstants.POST_KEY);
        }
        back.setOnClickListener(view1 -> {
            onBackPressed();
        });
        setSaveLaterIcon();
        fillPost(post);
    }

    private void setSaveLaterIcon(){
        AsyncTask.execute(() -> {
            int count  = database.newsDao().count(post.getId());
            getActivity().runOnUiThread(() -> {
                if(count>0){
                    save_later.setImageResource(R.drawable.ic_clear_black_24dp);
                    save_later.setOnClickListener(view -> AsyncTask.execute(() -> {
                        database.newsDao().delete(new SavedNew(post));
                        getActivity().runOnUiThread(() -> setSaveLaterIcon());
                    }));
                    save_later_title.setOnClickListener(view -> AsyncTask.execute(() -> {
                        database.newsDao().delete(new SavedNew(post));
                        getActivity().runOnUiThread(() -> setSaveLaterIcon());
                    }));
                }else{
                    save_later.setImageResource(R.drawable.ic_add_black_24dp);
                    save_later.setOnClickListener(view -> AsyncTask.execute(() -> {
                        database.newsDao().insertAll(new SavedNew(post));
                        getActivity().runOnUiThread(() -> setSaveLaterIcon());
                    }));
                    save_later_title.setOnClickListener(view -> AsyncTask.execute(() -> {
                        database.newsDao().insertAll(new SavedNew(post));
                        getActivity().runOnUiThread(() -> setSaveLaterIcon());
                    }));

                }
            });
        });
    }


    @Override
    public void init() {
        initPresenter();
        initViews();
    }

    @Override
    public void initViews() {
         changeStatusBarColor(R.color.toolbar_black_color);
         newsDetailWebView.getSettings().setJavaScriptEnabled(true);
         newsDetailWebView.getSettings().setSupportZoom(true);
         newsDetailWebView.getSettings().setBuiltInZoomControls(true);
         newsDetailWebView.getSettings().setLoadWithOverviewMode(true);
        ((MainActivity) getActivity()).hideToolbar();
    }

    @Override
    public void openInsideUrl() {
        this.newsDetailWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Intent openURLIntent = new Intent(Intent.ACTION_VIEW);
                openURLIntent.setData(Uri.parse(url));
                startActivity(openURLIntent);
                return true;
            }
        });


    }


    @Override
    public void fillPost(Posts post) {
        if(post.getImages() != null && !post.getImages().getLarge().isEmpty()){
              Picasso.get()
                      .load(post.getImages().getLarge())
                      .fit()
                      .into(newsDetailImage);
                if(post.getCategories() != null)
                setGradientButton(post.getCategories());
                newsDetailTimeText.setText(post.getCreatedAt());
                newsDetailSumText.setText(post.getTitle());
                String contentHTML = "<style>img{display: inline;height: auto;max-width: 100%;}</style>" + post.getContent();
            this.newsDetailWebView.loadDataWithBaseURL(null,contentHTML, "text/html", "UTF-8",null);
            openInsideUrl();
          }
    }

    @Override
    public void sharePost(int buttonId, String title, String URL) {
        newsDetailPresenter.sharePost(buttonId,title,URL);
    }

    @Override
    public void initPresenter() {
        newsDetailPresenter = new NewsDetailPresenter(this,getActivity());
    }

    private void setGradientButton(ArrayList<Category> categories){
        Category category;
        if(categories.size() < 2 && !categories.isEmpty()){
           category = categories.get(0);
        }else{
           category = categories.get(1);
        }

        float cornerRadius = this.getResources().getDimensionPixelSize(R.dimen.button_radius);
        String startColor = category.getColorCodeStart();
        String endColor = category.getColorCodeEnd();
        String categoryName = category.getName();
        categoryDetailButton.setGradientColorsWithRadii(cornerRadius,startColor,endColor,categoryName);
    }

}
