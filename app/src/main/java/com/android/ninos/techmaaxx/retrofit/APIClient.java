package com.android.ninos.techmaaxx.retrofit;


import com.android.ninos.techmaaxx.Utils.Consts;
import com.android.ninos.techmaaxx.session.Session;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by anupamchugh on 05/01/17.
 */

public class APIClient {

    private static Retrofit retrofit = null;



    public static Retrofit getClient() {


      /*  HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
*/
        /*OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS).build();
*/
        retrofit = new Retrofit.Builder()
                .baseUrl(Consts.title)
                .addConverterFactory(GsonConverterFactory.create())
                // .client(client)
                .build();
        return retrofit;
    }


}
