package com.android.ninos.techmaaxx.pojo.transaction_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Parthasarathy D on 1/5/2019.
 * Ninos IT Solution
 * parthasarathy.d@ninositsolution.com
 */
public class TransactionResponse {

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
