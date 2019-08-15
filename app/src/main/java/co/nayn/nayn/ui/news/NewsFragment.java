package co.nayn.nayn.ui.news;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.paginate.Paginate;
import com.thefinestartist.finestwebview.FinestWebView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.nayn.core.data.model.Category;
import co.nayn.core.data.model.Posts;
import co.nayn.core.data.model.ResponseModel;
import co.nayn.core.data.remote.NaynDataSource;
import co.nayn.nayn.BuildConfig;
import co.nayn.nayn.R;
import co.nayn.nayn.adapter.NewsAdapter;
import co.nayn.nayn.base.BaseFragment;
import co.nayn.nayn.constants.NaynConstants;
import co.nayn.nayn.data.local.db.AppDatabase;
import co.nayn.nayn.data.local.db.SavedNew;
import co.nayn.nayn.ui.detail.NewsDetailFragment;
import co.nayn.nayn.ui.main.MainActivity;
import co.nayn.nayn.widgets.MainNewsView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFragment extends BaseFragment implements NewsContract.View, MainNewsView.MainNewsViewClickListener, Paginate.Callbacks {
    @Inject
    AppDatabase database;
    @Inject
    NaynDataSource naynDataSource;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.news_recycler_view)
    RecyclerView newsRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.pod_fab)
    FloatingActionButton podFab;
    @BindView(R.id.info_text)
    TextView info_text;

    boolean isSavedNews;

    private NewsAdapter newsAdapter;
    private NewsViewModel newsViewModel;
    private NewsPresenterImpl newsPresenter;

    private static String URL;

    private int categoryID;
    private int pageNumber = 1;
    private int limit = 10;

    private boolean isPostLoading;
    private boolean isAllDataLoaded;

    ArrayList<Posts> savedNews;

    public static NewsFragment newInstance(Bundle args) {
        NewsFragment newsFragment = new NewsFragment();
        newsFragment.setArguments(args);
        return newsFragment;
    }

    public static NewsFragment newInstance(List<SavedNew> posts) {
        NewsFragment newsFragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("saved_posts", new ArrayList<>(posts));
        newsFragment.setArguments(args);
        return newsFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        this.categoryID = this.getArguments().getInt(NaynConstants.CATEGORY_ID);
        URL = NaynConstants.POSTS_BY_CATEGORY + String.valueOf(this.categoryID);
        if (getArguments().containsKey("saved_posts")) {
            isSavedNews = true;
            ArrayList<SavedNew> savedNews = getArguments().getParcelableArrayList("saved_posts");
            this.savedNews = new ArrayList<>();
            for (SavedNew savedNew : savedNews) {
                this.savedNews.add(savedNew.toPost());
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUnbinder(ButterKnife.bind(this, view));
        swipeRefreshLayout.setOnRefreshListener(this);
        initPresenter();
        initAdapter();
    }

    @OnClick(R.id.pod_fab)
    void onPodFabClick() {
        new FinestWebView.Builder(getActivity())
                .show(NaynConstants.POD_URL);
    }

    @Override
    public void init() {
        changeStatusBarColor(R.color.toolbar_black_color);
    }

    @Override
    public void initAdapter() {
        if (newsRecyclerView.getAdapter() == null) {
            newsAdapter = new NewsAdapter();
            newsAdapter.setMainNewsViewClickListener(this);
            newsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            hidePodFab();
            if (savedNews != null) {
                setAdapter(savedNews);
            } else {
                if (this.categoryID == 10) {
                    fetchAllPosts(pageNumber, limit);
                } else {
                    fetchPostByCategories(URL, pageNumber);
                }
            }
            ((MainActivity) getActivity()).returnTopOfRecyclerView(newsRecyclerView, 0);
        }

    }

    @Override
    public void initPresenter() {
        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        newsViewModel.setNaynDataSource(naynDataSource);
        newsViewModel.setDialogListener(this);
        newsPresenter = new NewsPresenterImpl(this, newsViewModel);
        newsPresenter.setLifeCycleOwner(this);
    }

    @Override
    public void setAdapter(ArrayList<Posts> posts) {
        if (isSavedNews) {
            if(posts.isEmpty()){
                info_text.setText(R.string.empty_saved_news);
                info_text.setVisibility(View.VISIBLE);
            }else{
                info_text.setVisibility(View.GONE);
            }
            isAllDataLoaded = true;
            newsAdapter.addAllPosts(posts);
            newsRecyclerView.setAdapter(newsAdapter);
            progressBar.setVisibility(View.GONE);
            return;
        }
        isAllDataLoaded = posts.isEmpty();
        if (newsRecyclerView.getAdapter() == null) {
            newsRecyclerView.setAdapter(newsAdapter);
            Paginate.with(newsRecyclerView, this).build();

        } else {
            newsAdapter.addAllPosts(posts);
            for (int i = 0; i < posts.size(); i++) {
                newsAdapter.addAllCategories(posts.get(i).getCategories());
            }
        }

        isPostLoading = false;
    }

    @Override
    public void addPosts(ArrayList<Posts> posts) {
        setAdapter(posts);
    }


    @Override
    public void fetchPostByCategories(String URL, int page) {
        newsPresenter.fetchPostByCategories(URL, page);
    }

    @Override
    public void fetchAllPosts(int page, int limit) {
        newsPresenter.fetchAllPosts(page, limit);
    }

    @Override
    public void addAllPosts(ArrayList<Posts> allPosts) {
        setAdapter(allPosts);
    }

    @Override
    public void onLoadMore() {
        isPostLoading = true;
        pageNumber++;
        if (this.categoryID == 10) {
            newsPresenter.fetchAllPosts(pageNumber, limit);
        } else {
            newsPresenter.fetchPostByCategories(URL, pageNumber);
        }
    }

    @Override
    public boolean isLoading() {
        return isPostLoading;
    }

    @Override
    public boolean hasLoadedAllItems() {
        return isAllDataLoaded;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_news;
    }

    @Override
    public void onCategoryButtonClicked(ArrayList<Category> categories) {
        ((MainActivity) getActivity()).initFragment(categories.get(0));
    }

    @Override
    public void hidePodFab() {
        newsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && podFab.isShown()) {
                    podFab.hide();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    podFab.show();
                }

                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    @Override
    public void onMainNewsClick(Posts post) {
        pushFragment(NewsDetailFragment.newInstance(post));
    }

    @Override
    public void onRefresh() {
        if (isSavedNews) {
            savedNews = new ArrayList<>();
            newsRecyclerView.setAdapter(null);
            AsyncTask.execute(() -> {
                for (SavedNew n : database.newsDao().getAll()) {
                    savedNews.add(n.toPost());
                }
                getActivity().runOnUiThread(() -> {
                    initAdapter();
                    setAdapter(savedNews);
                    swipeRefreshLayout.setRefreshing(false);
                });
            });
            return;
        }
        refreshNews();
    }


    private void refreshNews() {
        newsRecyclerView.setAdapter(null);
        swipeRefreshLayout.setRefreshing(true);
        if (this.categoryID == 10) {
            progressBar.setVisibility(View.VISIBLE);
            naynDataSource.getAllPosts(pageNumber, limit).enqueue(new Callback<ResponseModel<ArrayList<Posts>>>() {
                @Override
                public void onResponse(Call<ResponseModel<ArrayList<Posts>>> call, Response<ResponseModel<ArrayList<Posts>>> response) {
                    if (response.isSuccessful() && response.body().getData() != null)
                        setAdapter(response.body().getData());
                    progressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(Call<ResponseModel<ArrayList<Posts>>> call, Throwable t) {
                    if (BuildConfig.DEBUG) {
                        Log.d("AllPostsError", t.getMessage());
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            });
        } else {
            progressBar.setVisibility(View.VISIBLE);
            naynDataSource.getPostsByCategory(URL, pageNumber).enqueue(new Callback<ResponseModel<ArrayList<Posts>>>() {
                @Override
                public void onResponse(Call<ResponseModel<ArrayList<Posts>>> call, Response<ResponseModel<ArrayList<Posts>>> response) {
                    if (response.isSuccessful() && response.body().getData() != null)
                        setAdapter(response.body().getData());
                    progressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(Call<ResponseModel<ArrayList<Posts>>> call, Throwable t) {
                    if (BuildConfig.DEBUG)
                        Log.d("PostsError", t.getMessage());
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void dismiss() {
        if (progressBar != null)
            progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void show() {
        if (progressBar != null)
            progressBar.setVisibility(View.VISIBLE);
    }
}
