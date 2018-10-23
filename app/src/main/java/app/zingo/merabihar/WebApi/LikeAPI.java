package app.zingo.merabihar.WebApi;

import app.zingo.merabihar.Model.Likes;
import app.zingo.merabihar.Model.UserProfile;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by ZingoHotels Tech on 23-10-2018.
 */

public interface LikeAPI {

    @POST("Likes")
    Call<Likes> postLikes(@Body Likes body);
}
