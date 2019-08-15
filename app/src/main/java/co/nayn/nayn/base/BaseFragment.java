package co.nayn.nayn.base;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Objects;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import co.nayn.nayn.R;
import co.nayn.nayn.ui.main.MainActivity;
import dagger.android.support.DaggerFragment;

public abstract class BaseFragment extends DaggerFragment implements BaseFragmentView {

    Unbinder unbinder;

    View root;

    MainNavigation mainNavigation;

    private ProgressDialog progressDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof MainNavigation){
            mainNavigation = (MainNavigation) context;
        }

    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if(root == null){
           root = inflater.inflate(getLayoutRes(),container, false);
        }

        return root;
    }


    @LayoutRes
    public abstract int getLayoutRes();

    public void setUnbinder(Unbinder unbinder) {
        this.unbinder = unbinder;
    }

    @Override
    public void onDestroyView() {
        if(unbinder!=null)
            unbinder.unbind();
        super.onDestroyView();
    }


    @Override
    public void changeStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if(getActivity() !=null)
                getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(),color));
        }

    }


    @Override
    public void pushFragment(Fragment fragment,String tag) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flContainerFragment, fragment, tag)
                .commit();
    }

    @Override
    public void onBackPressed() {
        ((MainActivity)getActivity()).onBackPressed();
    }

    public interface MainNavigation{
        void clearStack();
        void pushFragment(Fragment fragment);
        void returnTopOfRecyclerView(RecyclerView recyclerView, int position);
    }

    @Override
    public void clearStack() {
        mainNavigation.clearStack();
    }

    @Override
    public void pushFragment(Fragment fragment) {
        mainNavigation.pushFragment(fragment);
    }

    @Override
    public void returnTopOfRecyclerView(RecyclerView recyclerView, int position) {
        mainNavigation.returnTopOfRecyclerView(recyclerView,position);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public MaterialDialog buildDialog(String positiveText, String negativeText, String content, String title) {
        return new MaterialDialog.Builder(Objects.requireNonNull(getContext()))
                .title(title)
                .content(content)
                .positiveText(positiveText)
                .negativeText(negativeText)
                .build();
    }

    @Override
    public void showProgressDialog() {
        if(progressDialog != null)
            progressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        if(progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void showConnectionError() {
        new AlertDialog.Builder(getActivity())
                .setCancelable(false)
                .setMessage(R.string.internet_error)
                .setPositiveButton("Yeniden dene", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().recreate();
                    }
                }).setNegativeButton("Çıkış yap", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getActivity().finish();
            }
        }).show();
    }

    @Override
    public void showInternalError() {
        new AlertDialog.Builder(getActivity())
                .setCancelable(false)
                .setMessage(R.string.internal_error)
                .setPositiveButton("Yeniden dene", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().recreate();
                    }
                }).setNegativeButton("Çıkış yap", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().finish();
                    }
                }).show();
    }

    @Override
    public ProgressDialog createProgressDialog(String title, String message) {
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setMessage(message);
        progressDialog.setTitle(title);
        return progressDialog;
    }

}
