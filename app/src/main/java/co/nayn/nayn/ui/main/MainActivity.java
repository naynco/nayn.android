package co.nayn.nayn.ui.main;

import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;

import com.ncapdevi.fragnav.FragNavController;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import co.nayn.core.data.model.Category;
import co.nayn.core.data.model.Posts;
import co.nayn.core.data.model.ResponseModel;
import co.nayn.core.data.remote.NaynDataSource;
import co.nayn.nayn.R;
import co.nayn.nayn.base.BaseActivity;
import co.nayn.nayn.base.BaseFragment;
import co.nayn.nayn.constants.NaynConstants;
import co.nayn.nayn.data.local.db.AppDatabase;
import co.nayn.nayn.ui.detail.NewsDetailFragment;
import co.nayn.nayn.ui.navigation.NavigationFragment;
import co.nayn.nayn.ui.news.NewsFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.Gravity.RIGHT;

public class MainActivity extends BaseActivity implements MainActivityView, BaseFragment.MainNavigation, FragNavController.RootFragmentListener, FragNavController.TransactionListener {
    public final static String EXTRA_POST_ID = "post_id";
    @Inject
    AppDatabase database;

    @Inject
    NaynDataSource dataSource;

    @BindView(R.id.drawerLayout)
    DrawerLayout drawer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_ivNavigation)
    AppCompatImageView toolbarIcon;
    @BindView(R.id.nayn_main_image)
    AppCompatImageView naynMainImage;
    @BindView(R.id.toolbar_tvTitle)
    TextView tvTitle;

    @OnClick({R.id.toolbar_ivNavigation, R.id.toolbar_tvTitle})
    void onClickNavigation() {
        openCloseDrawer();
    }

    @OnClick(R.id.settings)
    void onClickSettings(){
        int toolbarColor = ((ColorDrawable) toolbar.getBackground()).getColor();
        if(toolbarColor == R.color.nayn_black_color)
            toolbar.setBackgroundColor((getResources().getColor(R.color.nayn_white_color)));
        else
            toolbar.setBackgroundColor(getResources().getColor(R.color.nayn_black_color));

    }

    private Unbinder unbinder;
    private FragNavController fragNavController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        toolbar.setNavigationIcon(null);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        fragNavController = FragNavController.newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.flContainerFragment)
                .transactionListener(this)
                .rootFragmentListener(this, 1)
                .build();
        replaceMainFragment();
        if(getIntent().hasExtra(EXTRA_POST_ID)){
            pushDetailFragmentViaPostId(getIntent().getStringExtra(EXTRA_POST_ID));
        }
    }

    private void pushDetailFragmentViaPostId(String post_id) {
        dataSource.getPost(post_id).enqueue(new Callback<ResponseModel<ArrayList<Posts>>>() {
            @Override
            public void onResponse(Call<ResponseModel<ArrayList<Posts>>> call, Response<ResponseModel<ArrayList<Posts>>> response) {
                if(response.isSuccessful()){
                    runOnUiThread(() -> pushFragment(NewsDetailFragment.newInstance(response.body().getData().get(0))));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<ArrayList<Posts>>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (!fragNavController.isRootFragment()) {
            fragNavController.popFragment();
            showToolbar();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState);
        if (fragNavController != null)
            fragNavController.onSaveInstanceState(outState);
    }

    @Override
    public void replaceMainFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flContainerNavigationMenu, NavigationFragment.newInstance(), "Navigation").commit();
    }

    @Override
    public void closeDrawer() {
        if (drawer.isDrawerOpen(RIGHT))
            drawer.closeDrawer(RIGHT);
    }


    @Override
    public void openCloseDrawer() {
        if (drawer.isDrawerOpen(RIGHT))
            drawer.closeDrawer(RIGHT);
        else
            drawer.openDrawer(RIGHT);
    }


    @Override
    public void setToolbarTitle(String toolbarTitle) {
        tvTitle.setText(toolbarTitle);
    }

    @Override
    public void pushFragment(Fragment fragment, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flContainerFragment, fragment, tag)
                .commit();
        closeDrawer();
    }

    @Override
    public void initFragment(Category category) {
        Bundle args = new Bundle();
        args.putInt(NaynConstants.CATEGORY_ID, category.getId());
        showToolbar();
        switch (category.getName()) {
            case "Tümü":
                Bundle bundle = new Bundle();
                bundle.putInt(NaynConstants.CATEGORY_ID, 10);
                pushFragment(NewsFragment.newInstance(bundle));
            case "DÜNYA":
                pushFragment(NewsFragment.newInstance(args));
                break;
            case "GÜNDEM":
                pushFragment(NewsFragment.newInstance(args));
                break;
            case "SANAT":
                pushFragment(NewsFragment.newInstance(args));
                break;
            case "SICAK":
                pushFragment(NewsFragment.newInstance(args));
                break;
            case "SOSYAL":
                pushFragment(NewsFragment.newInstance(args));
                break;
            case "SPOR":
                pushFragment(NewsFragment.newInstance(args));
                break;
            case "Sonra Oku":
                AsyncTask.execute(() -> {
                    List list = database.newsDao().getAll();
                    runOnUiThread(() -> pushFragment(NewsFragment.newInstance(list)));
                });

                break;
        }
        closeDrawer();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void hideToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    @Override
    public void showToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().show();
        }
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return super.getLifecycle();
    }


    @Override
    public void clearStack() {
        fragNavController.clearStack();
    }

    @Override
    public void pushFragment(Fragment fragment) {
        fragNavController.pushFragment(fragment);
    }

    @Override
    public void returnTopOfRecyclerView(RecyclerView recyclerView, int position) {
        naynMainImage.setOnClickListener(view -> {
            recyclerView.scrollToPosition(position);
        });
    }

    @Override
    public Fragment getRootFragment(int i) {
        Bundle args = new Bundle();
        return NewsFragment.newInstance(args);
    }

    @Override
    public void onTabTransaction(Fragment fragment, int i) {

    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    @Override
    public void onFragmentTransaction(Fragment fragment, FragNavController.TransactionType transactionType) {

    }

}
