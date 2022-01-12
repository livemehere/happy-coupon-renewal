package kr.co.pointmobile.msrdemo.retrofit;

import kr.co.pointmobile.msrdemo.models.CardAuthResult;
import kr.co.pointmobile.msrdemo.models.Coupon;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitService {

    //카드결제 승인
    @POST("/libs/client/card.auth.php")
    Call<CardAuthResult> authCard(@Query("serial") String serial,
                                  @Query("card_no") String card_no,
                                  @Query("expire_date") String expire_date,
                                  @Query("install_period") String install_period,
                                  @Query("tot_amt") String tot_amt);
    //카드결제 취소
    @POST("/libs/client/card.cancel.php")
    Call<CardAuthResult> cancelCard(@Query("serial") String serial,
                                    @Query("coupon_no") String coupon_no);
    //쿠폰 사용 승인
    @POST("/libs/client/coupon.use.php")
    Call<Coupon> useCoupon(@Query("serial") String serial,
                           @Query("coupon_no") String coupon_no);
    //쿠폰 사용 취소
    @POST("/libs/client/coupon.cancel.php")
    Call<Coupon> cancelCoupon(@Query("serial") String serial,
                           @Query("coupon_no") String coupon_no);




}
