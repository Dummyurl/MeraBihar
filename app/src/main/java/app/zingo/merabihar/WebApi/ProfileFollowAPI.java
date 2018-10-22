package app.zingo.merabihar.WebApi;

import java.util.ArrayList;

import app.zingo.merabihar.Model.Interest;
import app.zingo.merabihar.Model.ProfileFollowMapping;
import app.zingo.merabihar.Model.UserProfile;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by ZingoHotels Tech on 03-10-2018.
 */

public interface ProfileFollowAPI {

    @POST("Follows")
    Call<ProfileFollowMapping> postInterest(@Body ProfileFollowMapping body);

    @DELETE("Follows/{id}")
    Call<ProfileFollowMapping> deleteIntrs(@Path("id") int id);

    @GET("Follows")
    Call<ArrayList<ProfileFollowMapping>> getInterest();

    @GET("Follow/GetFollowingByProfileId/{ProfileId}")
    Call<ArrayList<UserProfile>> getFollowersByProfileId(@Path("ProfileId") int id);

    @GET("Follow/GetFollowersByProfileId/{ProfileId}")
    Call<ArrayList<UserProfile>> getFollowingByProfileId(@Path("ProfileId") int id);

    @GET("Follow/GetFollowersContentByProfileId/{ProfileId}")
    Call<ArrayList<UserProfile>> getFollowersContentByProfileId(@Path("ProfileId") int id);

    @GET("Follow/GetFollowingContentByProfileId/{ProfileId}")
    Call<ArrayList<UserProfile>> getFollowingContentByProfileId(@Path("ProfileId") int id);

    @PUT("Follows/{id}")
    Call<ProfileFollowMapping> updateProfile(@Path("id") int id, @Body ProfileFollowMapping body);

    @GET("Follows/{id}")
    Call<ProfileFollowMapping> getInterestById(@Path("id") int id);
}
