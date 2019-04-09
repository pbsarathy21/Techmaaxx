package com.android.ninos.techmaaxx.pojo.request_transaction;

import com.google.gson.annotations.SerializedName;

public class RequestResponse{

	@SerializedName("amount")
	private Object amount;

	@SerializedName("code")
	private int code;

	@SerializedName("zeepay_id")
	private Object zeepayId;

	@SerializedName("message")
	private String message;

	public void setAmount(Object amount){
		this.amount = amount;
	}

	public Object getAmount(){
		return amount;
	}

	public void setCode(int code){
		this.code = code;
	}

	public int getCode(){
		return code;
	}

	public void setZeepayId(Object zeepayId){
		this.zeepayId = zeepayId;
	}

	public Object getZeepayId(){
		return zeepayId;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}