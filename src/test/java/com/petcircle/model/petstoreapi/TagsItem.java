package com.petcircle.model.petstoreapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class TagsItem {

	@JsonProperty("name")
	private String name;

	@JsonProperty("id")
	private int id;

	// Getters and Setters
	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	// Override equals() to compare based on field values
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TagsItem tagsItem = (TagsItem) o;
		return id == tagsItem.id &&
				Objects.equals(name, tagsItem.name);
	}

	// Override hashCode() to generate consistent hash codes
	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}
}
