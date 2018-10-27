package app.zingo.merabihars.WebApi;

import java.util.ArrayList;

import app.zingo.merabihars.Model.SubCategories;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by ZingoHotels Tech on 05-09-2018.
 */

public interface SubCategoryAPI {

    @GET("SubCategories")
    Call<ArrayList<SubCategories>> getSubCategories();

    @GET("SubCategories/{id}")
    Call<SubCategories> getSubCategoryById(@Path("id") int id);

    @GET("GetSubCategoryByCityId/{CityId}")
    Call<ArrayList<SubCategories>> getSubCategoriesByCityId(@Path("CityId") int id);

}
