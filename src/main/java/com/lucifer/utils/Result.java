package com.lucifer.utils;

public class Result {
	
	private Integer oper_code = 0;
	
	private boolean ok;
	
	private Object message;
	
	private Object data;
	
	private String desc;

	public boolean _isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	public static Result result(boolean ok, Object message, Object data) {
	    Result result = new Result();
	    result.setOk(ok);
	    result.setMessage(message);
	    result.setData(data);
	    return result;
	}
	
	public static Result ok() {
	    return Result.result(true, null, null);
	}
	
	public static Result ok(Object data) {
	    return Result.result(true, null, data);
	}
	
	public static Result ok(String msg,Object data) {
	    return Result.result(true, msg, data);
	}
	
	public static Result fail() {
	    return Result.result(false, null, null);
	}
	
	public static Result fail(String msg) {
	    return Result.result(false, msg, null);
	}
	
	public static Result fail(String msg,Object data) {
	    return Result.result(false, msg, data);
	}
	
	public Integer getOper_code(){
		if (oper_code<0) {
			return oper_code;
		}
		if (ok) {
			return 1;
		}
		return 0;
	}
	
	public static Result exception(String msg){
		Result rusult= Result.result(false, msg, null);
		rusult.oper_code = -1;
		return rusult;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
}