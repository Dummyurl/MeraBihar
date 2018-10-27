package app.zingo.merabihars.WebApi;

import java.util.ArrayList;

import app.zingo.merabihars.Model.InterestProfileMapping;
import app.zingo.merabihars.Model.ProfileSubCategoryMapping;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by ZingoHotels Tech on 04-10-2018.
 */

public interface ProfileSubCategoryAPI {

    @POST("ProfileSubCategoryMappings")
    Call<ProfileSubCategoryMapping> postSubInterest(@Body ProfileSubCategoryMapping body);

    @DELETE("ProfileSubCategoryMappings/{id}")
    Call<ProfileSubCategoryMapping> deleteSubIntrs(@Path("id") int id);

    @GET("ProfileSubCategoryMappings")
    Call<ArrayList<ProfileSubCategoryMapping>> getSubInterest();

    /*@GET("ProfileInterestMappings/GetInterestByProfileId/{ProfileId}")
    Call<ArrayList<InterestProfileMapping>> getInterestByProfileId(@Path("ProfileId") int id);

    @GET("ProfileInterestMappings/GetProfileByInterestId/{InterestId}")
    Call<ArrayList<InterestProfileMapping>> getProfileByInterestId(@Path("InterestId") int id);*/

    @PUT("ProfileSubCategoryMappings/{id}")
    Call<ProfileSubCategoryMapping> updateSubProfile(@Path("id") int id, @Body ProfileSubCategoryMapping body);

    @GET("ProfileSubCategoryMappings/{id}")
    Call<ProfileSubCategoryMapping> getInterestSubById(@Path("id") int id);
}
