package co.nayn.nayn.ui.navigation;

import java.util.ArrayList;

import co.nayn.core.data.model.Category;
import co.nayn.nayn.ui.main.MainPresenterImpl;
import co.nayn.nayn.ui.main.MainView;

public interface NavigationContract{

    interface View extends MainView {
        void init();
        void initPresenter();
        void setAdapter();
        void addNavigationCategories(ArrayList<Category> categories);
        void showInternalError();
    }

    abstract class Presenter extends MainPresenterImpl<NavigationContract.View> {

        public Presenter(NavigationContract.View mainView) {
            super(mainView);
        }

        public abstract void fetchNavigationCategories();
    }

}
