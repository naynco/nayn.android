package co.nayn.nayn.ui.navigation;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import co.nayn.core.data.model.Category;
import co.nayn.core.data.model.ResponseModel;
import co.nayn.core.data.remote.NaynDataSource;
import co.nayn.nayn.BuildConfig;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NavigationViewModel extends AndroidViewModel {

    @Inject
    NaynDataSource naynDataSource;

    private MutableLiveData<ResponseModel<ArrayList<Category>>> getNavigationCategoriesList;

    public NavigationViewModel(@NonNull Application application) {
        super(application);
    }


    public void setNaynDataSource(NaynDataSource naynDataSource){
        this.naynDataSource = naynDataSource;
    }

    public LiveData<ResponseModel<ArrayList<Category>>> getNavigationCategories(){
        if(getNavigationCategoriesList == null)
            getNavigationCategoriesList = new MutableLiveData<>();
        getCategories();
        return getNavigationCategoriesList;
    }

    private void getCategories(){
        naynDataSource.getNewsCategories().enqueue(new Callback<ResponseModel<ArrayList<Category>>>() {
            @Override
            public void onResponse(Call<ResponseModel<ArrayList<Category>>> call, Response<ResponseModel<ArrayList<Category>>> response) {
                if(response.isSuccessful() && response.body() != null)
                    getNavigationCategoriesList.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ResponseModel<ArrayList<Category>>> call, Throwable t) {
                if(BuildConfig.DEBUG)
                    Log.d("CategoriesError",t.getMessage());
                getNavigationCategoriesList.setValue(null);
            }
        });
    }
}


