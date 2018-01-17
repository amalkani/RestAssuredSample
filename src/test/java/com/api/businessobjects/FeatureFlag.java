package com.api.businessobjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FeatureFlag {
	@JsonProperty("default")
	private String featureflagId;

	public String getParent_featureflagName() {
		return featureflagName;
	}

	public void setParent_featureflagName(String parent_featureflagName) {
		this.featureflagName = parent_featureflagName;
	}
    
	private boolean crossTeamFlag = false;
	public boolean isCrossTeamFlag() {
		return crossTeamFlag;
	}

	public void setCrossTeamFlag(boolean crossTeamFlag) {
		this.crossTeamFlag = crossTeamFlag;
	}

	public boolean isFeatureEnabled() {
		return featureEnabled;
	}

	public void setFeatureEnabled(boolean featureEnabled) {
		this.featureEnabled = featureEnabled;
	}

	private String featureflagName;
	private boolean featureEnabled = false;
	private String parent_featureflagName;

	public String getFeatureflagId() {
		return featureflagId;
	}

	public void setFeatureflagId(String featureflagId) {
		this.featureflagId = featureflagId;
	}

	public String getFeatureflagName() {
		return featureflagName;
	}

	public void setFeatureflagName(String featureflagName) {
		this.featureflagName = featureflagName;
	}

	public boolean getFeatureStatus() {
		return featureEnabled;
	}

	public void setFeatureStatus(boolean featureEnabled) {
		this.featureEnabled = featureEnabled;
	}
	
	public FeatureFlag (String featureflagName, boolean crossTeamFlag, String parent_featureflagName ) {
		this.featureflagName=featureflagName;
		this.crossTeamFlag=crossTeamFlag;
		this.parent_featureflagName=parent_featureflagName;
		}

	public FeatureFlag() {}
}