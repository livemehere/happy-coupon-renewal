package kr.co.pointmobile.msrdemo.retrofit;

import kr.co.pointmobile.msrdemo.models.CardAuthResult;
import kr.co.pointmobile.msrdemo.models.Coupon;
import kr.co.pointmobile.msrdemo.models.Post;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {


    @POST("/libs/client/card.auth.php")
    Call<CardAuthResult> authCard(@Query("serial") String serial,
                                  @Query("card_no") String card_no,
                                  @Query("expire_date") String expire_date,
                                  @Query("install_period") String install_period,
                                  @Query("tot_amt") String tot_amt
                        );

    @FormUrlEncoded
    @POST("/posts")
    Call<Coupon> useCoupon(@Field("serial") String serial,@Field("serial") String coupon_no);

    @FormUrlEncoded
    @POST ("/posts")
    Call<Coupon> cancelCoupon(@Field("serial") String serial,@Field("serial") String coupon_no);//@Body String serial, String coupon_no
}
