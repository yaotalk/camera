package com.minivision.cameraplat.rest.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.annotations.ApiModel;

import java.util.UUID;

@ApiModel(description = "通用返回结构")
@JsonInclude(Include.NON_EMPTY)
public class RestResult<T> {

	private String requestId;
	private int timeUsed;
	private String errorMessage;
	@JsonUnwrapped
	private T data;

	public RestResult() {
		this.requestId = UUID.randomUUID().toString();
	}
	
	public RestResult(T data){
		this.data = data;
		this.requestId = UUID.randomUUID().toString();
	}
	
	public RestResult(Throwable t){
		this.errorMessage = t.getMessage();
		this.requestId = UUID.randomUUID().toString();
	}
	public RestResult(String msg){
		this.errorMessage = msg;
		this.requestId = UUID.randomUUID().toString();
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public int getTimeUsed() {
		return timeUsed;
	}

	public void setTimeUsed(int timeUsed) {
		this.timeUsed = timeUsed;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "RestResult [requestId=" + requestId + ", timeUsed=" + timeUsed + ", errorMessage=" + errorMessage
				+ ", data=" + data + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((requestId == null) ? 0 : requestId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RestResult<?> other = (RestResult<?>) obj;
		if (requestId == null) {
			if (other.requestId != null)
				return false;
		} else if (!requestId.equals(other.requestId))
			return false;
		return true;
	}

}
