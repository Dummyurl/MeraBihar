package app.zingo.merabihar.WebApi;

import java.util.ArrayList;

import app.zingo.merabihar.Model.Blogs;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by ZingoHotels Tech on 04-09-2018.
 */

public interface BlogApi {

    @GET("Blogs")
    Call<ArrayList<Blogs>> getBlogs();

    @GET("Blogs/{id}")
    Call<Blogs> getBlogById(@Path("id") int id);

    @GET("Blogs/GetBlogsByActivityId/{ActivityId}")
    Call<ArrayList<Blogs>> getBlogsByActivityId(@Path("ActivityId") int id);

    @GET("Blogs/GetBlogsByProfileId/{ProfileId}")
    Call<ArrayList<Blogs>> getBlogsByProfileId(@Path("ProfileId") int id);

    @GET("Blogs/GetBlogsByStatusAndProfileId/{Status}/{ProfileId}")
    Call<ArrayList<Blogs>> getBlogsByStatusProfileId(@Path("Status") String sid,@Path("ProfileId") int id);

    @GET("Blogs/GetBlogsByCategoryId/{CategoryId}")
    Call<ArrayList<Blogs>> getBlogsByCategoryId(@Path("CategoryId") int id);

    @GET("Blogs/GetBlogsBySubCategoryId/{SubCategoryId}")
    Call<ArrayList<Blogs>> getBlogsBySubCategoryId(@Path("SubCategoryId") int id);

    @GET("Blogs/GetBlogsByCityId/{CityId}")
    Call<ArrayList<Blogs>> getBlogsByCityId(@Path("CityId") int id);

}
