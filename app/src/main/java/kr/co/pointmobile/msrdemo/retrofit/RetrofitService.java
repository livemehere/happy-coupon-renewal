package kr.co.pointmobile.msrdemo.retrofit;

import java.util.List;

import kr.co.pointmobile.msrdemo.models.Post;
import kr.co.pointmobile.msrdemo.models.PostList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {
    @GET("/posts/{q}")
    Call<Post>getList(@Path("q") String q);
}
