package com.android.ninos.techmaaxx.MyApi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Parthasarathy D on 1/5/2019.
 * Ninos IT Solution
 * parthasarathy.d@ninositsolution.com
 */
public class RetrofitClient {

    //Test URL
    private static final String BASE_URL = " https://test.digitaltermination.com";

    //access token

    //Live URL
    //public static final String BASE_URL = " https://test.digitaltermination.com";

    private static Retrofit retrofit;

    public static Retrofit getRetrofit()
    {

        String access_token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImU1ZDU1YzM" +
                "2OWZiNjAxYzgxMmMzMDhiMzRjODMwNmQ0Y2Q5ODk3YmQzNWU0YjY5OGY2ZmE3ZTgyYjJlOTQxYTYyYzdmMTA5MDE0ZDIzZDhkIn0." +
                "eyJhdWQiOiIyMiIsImp0aSI6ImU1ZDU1YzM2OWZiNjAxYzgxMmMzMDhiMzRjODMwNmQ0Y2Q5ODk3YmQzNWU0YjY5OGY2ZmE3ZTgyYjJlOTQx" +
                "YTYyYzdmMTA5MDE0ZDIzZDhkIiwiaWF0IjoxNTQ2NjY0NDY2LCJuYmYiOjE1NDY2NjQ0NjYsImV4cCI6MTU3ODIwMDQ2Niwic3ViIjoiNTkiLCJz" +
                "Y29wZXMiOltdfQ.bDxO_o4lCUjDrz3ts6IiZJQP1XhbzKqLwndMTzdRfL-PBhbixnDidaj-BS6n_R2ZW3ulp5qltheNKQoKCfU_T-1XeIyXk34v3Zo07s" +
                "uX5kLlaogzNV_Swz3l-Kp0wcItNgOORTy8eAcs1dJkMvXT33LzU5Rncs8a9ZASxL_IAvyHSbq6I_aJVi06I0Shf92aCtguUEwMZK-J6_RZ9hAleqFuMn1yZSwEbv" +
                "VElBI3cgNBNjKlnQVpdaTBQeGSGNTppyhHPwIpltoyWgCMLF12gFVRudQeJvicQoCUOW06g6-ohhVrSBe-hEp9KNOBzCGY3ApLeC6_1BpYQeg6EXzesh6nO5UoKvp8eTKL" +
                "vD5TjN9CcxXU49ceP9U-enUJcsjyvspIxK4dMgAMpr5T1YJVbtviu9zIhS11VDTfR73JjN7HBHjeQsqP3kVi-XvlZRAxdqMDhexgDXoE19m2Abu0fRjvh2fd5SSt-33WqP00dJCj" +
                "4TMCSkGrj_6q19OESdVm0X08Zvw8QYXQ-on9P1Q2z897Yy4BrgA8Fks1DaPu9t_WTlQNN6G7lJNQAFsEyVpMqJk-piejoOgbf5wwKRsrhmAIrz25EJdr1wq2JtQbpVX6EUhLOXn6qqndjV" +
                "lt9lKEOwWjoGo4QkcT9dPBuywu1xWRwDqmwueZFtDQV0iBY2U";

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer "+access_token)
                            .build();
                    return chain.proceed(request);
                }).build();

        if (retrofit == null)
            retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        return retrofit;
    }

    public RetrofitClient() {
    }
}
