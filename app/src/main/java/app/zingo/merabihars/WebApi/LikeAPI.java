package app.zingo.merabihars.WebApi;

import app.zingo.merabihars.Model.Contents;
import app.zingo.merabihars.Model.Likes;
import app.zingo.merabihars.Model.UserProfile;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by ZingoHotels Tech on 23-10-2018.
 */

public interface LikeAPI {

    @POST("Likes")
    Call<Likes> postLikes(@Body Likes body);

    @DELETE("Likes/{id}")
    Call<Likes> deleteLikes(@Path("id") int id);
}
