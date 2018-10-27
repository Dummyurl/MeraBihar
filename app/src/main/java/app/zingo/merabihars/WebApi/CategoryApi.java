package app.zingo.merabihars.WebApi;

import java.util.ArrayList;

import app.zingo.merabihars.Model.Category;
import app.zingo.merabihars.Model.CategoryAndContentList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by ZingoHotels Tech on 29-08-2018.
 */

public interface CategoryApi {

    @GET("Categories")
    Call<ArrayList<Category>> getCategories();

    @GET("Categories/{id}")
    Call<Category> getCategoryById(@Path("id") int id);

    @GET("GetCategoriesByCityId/{CityId}")
    Call<ArrayList<Category>> getCategoriesByCityId(@Path("CityId") int id);

    @GET("ProfileCategoryMappings/GetCategoryByProfileId/{ProfileId}")
    Call<ArrayList<Category>> getCategoriesByProfileId(@Path("ProfileId") int id);

    @GET("Categories/GetContentAndCategoriesByCityId/{CityId}")
    Call<ArrayList<CategoryAndContentList>> getContentAndCategoryByCityId(@Path("CityId") int id);
}
