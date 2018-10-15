package app.zingo.merabihar.WebApi;

import java.util.ArrayList;

import app.zingo.merabihar.Model.ActivityImages;
import app.zingo.merabihar.Model.ActivityModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by ZingoHotels Tech on 29-08-2018.
 */

public interface ActivityApi {

    @GET("Activities")
    Call<ArrayList<ActivityModel>> getActivities();

    @GET("ActivityImages")
    Call<ArrayList<ActivityImages>> getActivityImages();

    @GET("Activities/{id}")
    Call<ActivityModel> getActById(@Path("id") int id);

    @GET("GetActivitiesByCategoryId/{CategoryId}")
    Call<ArrayList<ActivityModel>> getActivityByCategoryId(@Path("CategoryId") int id);

    @GET("GetActivitiesBySubCategoryId/{SubCategoryId}")
    Call<ArrayList<ActivityModel>> getActivityBySubCategoryId(@Path("SubCategoryId") int id);

    @GET("GetActivitiesByCityId/{CityId}")
    Call<ArrayList<ActivityModel>> getActivityByCityId(@Path("CityId") int id);
}
