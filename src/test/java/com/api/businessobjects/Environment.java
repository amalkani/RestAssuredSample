package com.api.businessobjects;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Environment {

	
	@JsonProperty("default")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEnvironment_id() {
		return environment_id;
	}

	public void setEnvironment_id(String environment_id) {
		this.environment_id = environment_id;
	}

	private String environment_id;

	private ArrayList<FeatureFlag> featureFlags = new ArrayList<FeatureFlag>();

	public ArrayList<FeatureFlag> getFeatureFlags() {
		return featureFlags;
	}

	public Environment (String name, String environment_id, ArrayList<FeatureFlag> list_featureFlags) {
	    this.name = name;
	    this.environment_id = environment_id;
	    this.featureFlags = list_featureFlags;
	  }
	public Environment ()
	{}
}