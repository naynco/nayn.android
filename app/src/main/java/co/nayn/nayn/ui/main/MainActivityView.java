package co.nayn.nayn.ui.main;


import androidx.lifecycle.Lifecycle;
import androidx.fragment.app.Fragment;

import co.nayn.core.data.model.Category;


public interface MainActivityView extends MainView {


    Lifecycle getLifecycle();

    void replaceMainFragment();

    void closeDrawer();

    void openCloseDrawer();

    void setToolbarTitle(String toolbarTitle);

    void pushFragment(Fragment fragment, String tag);

    void initFragment(Category category);

    void hideToolbar();

    void showToolbar();
}
