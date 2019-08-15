package co.nayn.nayn.ui.navigation;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import co.nayn.core.data.model.Category;
import co.nayn.core.data.remote.NaynDataSource;
import co.nayn.nayn.R;
import co.nayn.nayn.adapter.NavigationAdapter;
import co.nayn.nayn.base.BaseFragment;
import co.nayn.nayn.constants.NaynConstants;
import co.nayn.nayn.ui.main.MainActivity;
import co.nayn.nayn.utils.Network;
import co.nayn.nayn.widgets.NavigationCardView;


public class NavigationFragment extends BaseFragment implements NavigationCardView.NavigationViewClickListener,NavigationContract.View {

    @BindView(R.id.navigation_recycler_view)
    RecyclerView navigationRecyclerView;

    @Inject
    NaynDataSource naynDataSource;

    private NavigationAdapter adapter;
    private NavigationViewModel navigationViewModel;
    private NavigationPresenterImpl navigationPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public static NavigationFragment newInstance() {
        NavigationFragment navigationFragment = new NavigationFragment();
        Bundle args = new Bundle();
        navigationFragment.setArguments(args);
        return navigationFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUnbinder(ButterKnife.bind(this,view));
        setAdapter();
        adapter.setSelected(NaynConstants.INDEX_ALL);
    }

    @Override
    public void init() {
        changeStatusBarColor(R.color.toolbar_black_color);
    }

    @Override
    public void initPresenter() {
        navigationViewModel = ViewModelProviders.of(this).get(NavigationViewModel.class);
        navigationViewModel.setNaynDataSource(naynDataSource);
        navigationPresenter = new NavigationPresenterImpl(this,navigationViewModel);
        navigationPresenter.setLifeCycleOwner(this);
        fetch();
    }

    private void fetch(){
        if(Network.isConnected(getContext())){
            navigationPresenter.fetchNavigationCategories();
        }else{
            showConnectionError();
        }
    }

    @Override
    public void setAdapter() {
        if(navigationRecyclerView.getAdapter() == null){
            initPresenter();
            adapter = new NavigationAdapter();
            adapter.setOnNavigationViewClickListener(this);
            navigationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            navigationRecyclerView.setAdapter(adapter);
        }else{
            fetch();
            adapter.notifyDataSetChanged();
        }

    }


    @Override
    public void addNavigationCategories(ArrayList<Category> categories) {
        Category category = new Category();
        category.setId(NaynConstants.CATEGORY_ID_ALL_NEWS);
        category.setPosition(0);
        category.setName("Tümü");
        category.setColorCodeEnd("1F1F1F");
        category.setColorCodeStart("1F1F1F");
        category.setSelected(true);
        categories.add(NaynConstants.INDEX_ALL,category);

        Category saved = new Category();
        saved.setId(NaynConstants.CATEGORY_ID_SAVED);
        saved.setName("Sonra Oku");
        saved.setColorCodeEnd("ffe6ff");
        saved.setColorCodeStart("ff4dff");
        saved.setSelected(false);
        categories.add(saved);

        adapter.addCategories(categories);
        pushFragment(categories.get(0));
    }


    private void pushFragment(Category category) {
        ((MainActivity)getActivity()).initFragment(category);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_navigation;
    }

    @Override
    public void onNavigationClick(Category category) {
        for(int i=0; i<navigationRecyclerView.getAdapter().getItemCount();i++){
            RecyclerView.ViewHolder viewHolder = navigationRecyclerView.findViewHolderForAdapterPosition(i);
            if(category.getPosition() == i){
                ((NavigationCardView)viewHolder.itemView).setIconButtonVisibility(View.VISIBLE);
            }else{
                ((NavigationCardView)viewHolder.itemView).setIconButtonVisibility(View.GONE);
            }
        }
        pushFragment(category);
    }


}
