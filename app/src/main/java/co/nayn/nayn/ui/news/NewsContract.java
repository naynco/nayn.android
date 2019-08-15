package co.nayn.nayn.ui.news;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import co.nayn.core.data.model.Posts;
import co.nayn.nayn.ui.main.MainPresenterImpl;
import co.nayn.nayn.ui.main.MainView;

public interface NewsContract {

    interface View extends MainView, SwipeRefreshLayout.OnRefreshListener, NewsViewModel.DialogListener {
        void init();
        void initPresenter();
        void setAdapter(ArrayList<Posts> posts);
        void addPosts(ArrayList<Posts> posts);
        void fetchPostByCategories(String URL , int page);
        void fetchAllPosts(int page, int limit);
        void addAllPosts(ArrayList<Posts> allPosts);
        void initAdapter();
        void hidePodFab();
    }

    abstract class Presenter extends MainPresenterImpl<NewsContract.View> {


        public Presenter(View mainView) {
            super(mainView);
        }

        public abstract void fetchPostByCategories(String URL , int page);

        public abstract void fetchAllPosts(int page, int limit);
    }
}
