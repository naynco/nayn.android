package co.nayn.nayn.ui.news;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;




public class NewsPresenterImpl extends NewsContract.Presenter implements LifecycleObserver {

    private LifecycleOwner lifecycleOwner;
    private NewsViewModel newsViewModel;

    public NewsPresenterImpl(NewsContract.View mainView, NewsViewModel newsViewModel) {
        super(mainView);
        this.newsViewModel = newsViewModel;
    }


    public void setLifeCycleOwner(LifecycleOwner lifecycleOwner){
        this.lifecycleOwner = lifecycleOwner;
    }

    @Override
    public void fetchPostByCategories(String URL, int page) {
            newsViewModel.getNewsPosts(URL, page).observe(lifecycleOwner,arrayListResponseModel -> {
                mainView.addPosts(arrayListResponseModel.getData());
            });
    }

    @Override
    public void fetchAllPosts(int page, int limit) {
        newsViewModel.getAllPosts(page,limit).observe(lifecycleOwner,arrayListResponseModel -> {
            mainView.addAllPosts(arrayListResponseModel.getData());
        });
    }


}
