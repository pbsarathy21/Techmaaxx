package com.android.ninos.techmaaxx.MyApi;

import com.android.ninos.techmaaxx.pojo.request_transaction.RequestResponse;
import com.android.ninos.techmaaxx.pojo.transaction_response.TransactionResponse;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Parthasarathy D on 1/5/2019.
 * Ninos IT Solution
 * parthasarathy.d@ninositsolution.com
 */
public interface Api {

    @GET("/api/transactions/{id}")
    Observable<TransactionResponse> getTransactions(@Path("id") String id);

    @POST("/api/custom/transactions/tech-maxx/wallets/debit-wallet")
    Call<RequestResponse> sendRequest(@Body String Request);
}
