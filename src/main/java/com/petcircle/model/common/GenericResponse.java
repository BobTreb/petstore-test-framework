package com.petcircle.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class GenericResponse {

	@JsonProperty("code")
	private int code;

	@JsonProperty("type")
	private String type;

	@JsonProperty("message")
	private String message;

	// Getters
	public int getCode() {
		return code;
	}

	public String getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}

	// Setters
	public void setCode(int code) {
		this.code = code;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	// Constructors
	public GenericResponse() {
	}

	public GenericResponse(int code, String type, String message) {
		this.code = code;
		this.type = type;
		this.message = message;
	}

	// equals and hashCode
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof GenericResponse)) return false;
		GenericResponse that = (GenericResponse) o;
		return code == that.code &&
				Objects.equals(type, that.type) &&
				Objects.equals(message, that.message);
	}

	@Override
	public int hashCode() {
		return Objects.hash(code, type, message);
	}
}
