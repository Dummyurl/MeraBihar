package app.zingo.merabihars.WebApi;

import java.util.ArrayList;

import app.zingo.merabihars.Model.UserProfile;
import app.zingo.merabihars.Model.VideoTrackers;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by ZingoHotels Tech on 26-10-2018.
 */

public interface VideoTrackersAPI {

    @POST("VideoTrackers")
    Call<VideoTrackers> postVideoTrackers(@Body VideoTrackers body);

    @PUT("VideoTrackers/{id}")
    Call<VideoTrackers> updateVideoTrackers(@Path("id") int id, @Body VideoTrackers body);

    @GET("VideoTrackers/GetProfileIdByVideoReferralCodeUsedToWatch/{VideoReferralCodeUsedToWatch}")
    Call<ArrayList<UserProfile>> getLiveWatchers(@Path("VideoReferralCodeUsedToWatch") String id);

}
