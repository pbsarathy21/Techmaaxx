package com.android.ninos.techmaaxx.retrofit;


import android.content.SharedPreferences;

import com.android.ninos.techmaaxx.Utils.Consts;
import com.android.ninos.techmaaxx.pojo.request_transaction.RequestResponse;
import com.android.ninos.techmaaxx.request.CommonRequest;
import com.android.ninos.techmaaxx.session.Session;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RetrofitInterface {


    // live

   // public String url = "http://192.168.1.11/tracking/public/api/";


     public String url = "http://" + Consts.Domain_url + "/api/";

    // public String url = "http://beginnersparadise.com/tracking/public/api/";


     //public String url = "http://luggage.mmttechmaaxx.com/api/";
     public String payment_url = "https://test.digitaltermination.com/api/";


    @POST("destination/list")
    Call<ListDetails> Destin(@Body CommonRequest commonRequest);

    @POST("login")
    Call<ListDetails> Login(@Body CommonRequest commonRequest);

    @POST("weighable/price")
    Call<ListDetails> weight_price(@Body CommonRequest commonRequest);

    @POST("weighable/store")
    Call<ListDetails> Weight(@Body CommonRequest commonRequest);

    @GET("category/list")
    Call<ListDetails> Cat();

    /*  @GET("origin/list")
      Call<ListDetails> orgin();*/
    @POST("origin/list")
    Call<ListDetails> orgin(@Body CommonRequest commonRequest);

    @POST("product/list")
    Call<ListDetails> product_list(@Body CommonRequest commonRequest);

    @POST("countable/price")
    Call<ListDetails> Count_Prize(@Body CommonRequest commonRequest);

    @POST("countable/store")
    Call<ListDetails> count_store(@Body CommonRequest commonRequest);


    @GET("food/list")
    Call<ListDetails> FoodList();

    @POST("countable/list")
    Call<ListDetails> count_list(@Body CommonRequest commonRequest);


    @POST("countable/delete")
    Call<ListDetails> delete(@Body CommonRequest commonRequest);


    /* @POST("report/list")
     Call<ListDetails> repor(@Body CommonRequest commonRequest);
 */
    @POST("today/report")
    Call<ListDetails> today_report(@Body CommonRequest commonRequest);

    @POST("yesterday/report")
    Call<ListDetails> yesterday_report(@Body CommonRequest commonRequest);

    @Headers("Content-Type: application/json")
    @POST("custom/transactions/tech-maxx/wallets/debit-wallet")
    Call<RequestResponse> sendRequest(@Body String Request);


}