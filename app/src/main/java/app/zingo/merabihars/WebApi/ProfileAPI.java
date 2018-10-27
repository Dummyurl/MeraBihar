package app.zingo.merabihars.WebApi;

import java.util.ArrayList;

import app.zingo.merabihars.Model.FollowerNonFollowers;
import app.zingo.merabihars.Model.UserProfile;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by ZingoHotels Tech on 12-09-2018.
 */

public interface ProfileAPI {

    @POST("Profiles/GetProfileByEmailAndPassword")
    Call<ArrayList<UserProfile>> getProfileforLogin(@Body UserProfile body);

    @GET("Profiles")
    Call<ArrayList<UserProfile>> getProfiles();

    @DELETE("Profiles/{id}")
    Call<UserProfile> deletProfile(@Path("id") int id);

    @GET("Profiles/{id}")
    Call<UserProfile> getProfileById(@Path("id") int id);

    @POST("Profiles")
    Call<UserProfile> postProfile(@Body UserProfile body);

    @PUT("Profiles/{id}")
    Call<UserProfile> updateProfile(@Path("id") int id, @Body UserProfile body);

    @POST("Profiles/GetProfileByPhone")
    Call<ArrayList<UserProfile>> getUserByPhone(@Body UserProfile userProfile);

    @POST("Profiles/GetProfileByEmail")
    Call<ArrayList<UserProfile>> getUserByEmail(@Body UserProfile userProfile);

    @GET("Profiles/GetProfileByAuthId/{AuthId}")
    Call<ArrayList<UserProfile>> getUserByAuthId(@Path("AuthId") String id);

    @GET("Profiles/GetProfileByUserRoleId/{UserRoleId}")
    Call<ArrayList<UserProfile>> getUserByUserRoleId(@Path("UserRoleId") int id);

    @GET("Follow/GetFollowersAndNonFollowersByProfileId/{ProfileId}")
    Call<FollowerNonFollowers> getOtherProfiles(@Path("ProfileId") int id);
}
