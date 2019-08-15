package co.nayn.nayn.base;

import android.app.ProgressDialog;

import com.afollestad.materialdialogs.MaterialDialog;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import co.nayn.nayn.ui.main.MainView;

public interface BaseFragmentView extends MainView {
    void changeStatusBarColor(int color);
    void pushFragment(Fragment fragment, String tag);
    void onBackPressed();
    void pushFragment(Fragment fragment);
    void clearStack();
    void returnTopOfRecyclerView(RecyclerView recyclerView, int position);
    void showProgressDialog();
    void hideProgressDialog();
    void showConnectionError();
    void showInternalError();
    MaterialDialog buildDialog(String positiveText, String negativeText, String content, String title);
    ProgressDialog createProgressDialog(String message, String title);

}
