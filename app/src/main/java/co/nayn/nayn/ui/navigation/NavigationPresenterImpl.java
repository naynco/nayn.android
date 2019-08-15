package co.nayn.nayn.ui.navigation;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

public class NavigationPresenterImpl extends NavigationContract.Presenter implements LifecycleObserver{

    private NavigationViewModel navigationViewModel;
    private LifecycleOwner lifecycleOwner;

    public NavigationPresenterImpl(NavigationContract.View mainView, NavigationViewModel navigationViewModel) {
        super(mainView);
        this.navigationViewModel = navigationViewModel;

    }

    public void setLifeCycleOwner(LifecycleOwner lifecycleOwner){
       this.lifecycleOwner = lifecycleOwner;
    }

    @Override
    public void fetchNavigationCategories() {
        navigationViewModel.getNavigationCategories().observe(lifecycleOwner, categories -> {
            if(categories == null){
                mainView.showInternalError();
            }else{
                mainView.addNavigationCategories(categories.getData());
            }
        });
    }

}
