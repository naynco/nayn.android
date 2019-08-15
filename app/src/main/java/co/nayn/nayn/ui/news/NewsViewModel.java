package co.nayn.nayn.ui.news;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;

import javax.inject.Inject;

import co.nayn.nayn.BuildConfig;
import co.nayn.core.data.model.Posts;
import co.nayn.core.data.model.ResponseModel;
import co.nayn.core.data.remote.NaynDataSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsViewModel extends AndroidViewModel {

    @Inject
    NaynDataSource naynDataSource;

    private MutableLiveData<ResponseModel<ArrayList<Posts>>> postMutableLiveData;
    private MutableLiveData<ResponseModel<ArrayList<Posts>>> allPostMutableLiveData;
    private DialogListener dialogListener;

    public NewsViewModel(@NonNull Application application) {
        super(application);
    }


    public void setNaynDataSource(NaynDataSource naynDataSource){
        this.naynDataSource = naynDataSource;
    }

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    public LiveData<ResponseModel<ArrayList<Posts>>> getNewsPosts(String URL, int page){
        if(postMutableLiveData == null)
            postMutableLiveData = new MutableLiveData<>();
        getPosts(URL,page);
        return postMutableLiveData;
    }

    public LiveData<ResponseModel<ArrayList<Posts>>> getAllPosts(int page, int limit){
        if(allPostMutableLiveData == null)
            allPostMutableLiveData = new MutableLiveData<>();
        getPosts(page,limit);
        return allPostMutableLiveData;
    }



    private void getPosts(int page, int limit){
        dialogListener.show();
        naynDataSource.getAllPosts(page,limit).enqueue(new Callback<ResponseModel<ArrayList<Posts>>>() {
            @Override
            public void onResponse(Call<ResponseModel<ArrayList<Posts>>> call, Response<ResponseModel<ArrayList<Posts>>> response) {
                if(response.isSuccessful() && response.body().getData() != null)
                    allPostMutableLiveData.setValue(response.body());
                dialogListener.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseModel<ArrayList<Posts>>> call, Throwable t) {
                if(BuildConfig.DEBUG)
                    Log.d("AllPostsError",t.getMessage());
                dialogListener.dismiss();
            }
        });
    }

    private void getPosts(String URL,int page){
        dialogListener.show();
        naynDataSource.getPostsByCategory(URL,page).enqueue(new Callback<ResponseModel<ArrayList<Posts>>>() {
            @Override
            public void onResponse(Call<ResponseModel<ArrayList<Posts>>> call, Response<ResponseModel<ArrayList<Posts>>> response) {
                    if(response.isSuccessful() && response.body().getData() != null)
                        postMutableLiveData.setValue(response.body());
                    dialogListener.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseModel<ArrayList<Posts>>> call, Throwable t) {
                if(BuildConfig.DEBUG)
                    Log.d("PostsError",t.getMessage());
                dialogListener.dismiss();
            }
        });
    }

    interface DialogListener{
        void dismiss();
        void show();
    }

}
