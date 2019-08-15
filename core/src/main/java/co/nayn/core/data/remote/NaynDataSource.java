package co.nayn.core.data.remote;

import java.util.ArrayList;

import co.nayn.core.data.model.Category;
import co.nayn.core.data.model.Posts;
import co.nayn.core.data.model.ResponseModel;
import co.nayn.core.data.model.Tag;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface NaynDataSource {

    @GET("categories")
    Call<ResponseModel<ArrayList<Category>>> getNewsCategories();

    @GET("tags")
    Call<ResponseModel<ArrayList<Tag>>> getNewsTags();

    @GET("posts-by-tag")
    Call<ResponseModel<ArrayList<Posts>>> getPostsByTag(@Query("p") String slug, @Query("page") int page, @Query("limit") int limit);

    @GET("post")
    Call<ResponseModel<Posts>> getPost();

    @GET("post/{id}")
    Call<ResponseModel<ArrayList<Posts>>> getPost(@Path("id") String id);

    @GET
    Call<ResponseModel<ArrayList<Posts>>> getPostsByCategory(@Url String url, @Query("page") int page);

    @GET("posts")
    Call<ResponseModel<ArrayList<Posts>>> getAllPosts(@Query("page") int page, @Query("limit") int limit);


}

