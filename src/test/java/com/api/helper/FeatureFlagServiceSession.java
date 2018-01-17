package com.api.helper;

import io.restassured.specification.RequestSpecification;

/**
 * Representation of a user's ability to act on the microservice under test
 */
public class FeatureFlagServiceSession {

	private final RequestSpecification featureFlagSpec;

	public FeatureFlagServiceSession(RequestSpecification featureFlagSpec, String sessionId) {
		this.featureFlagSpec = featureFlagSpec;
	}

	/**
	 * Get an Environments client for this admin session
	 * 
	 */
	public EnvironmentHelper environments() {
		return new EnvironmentHelper(featureFlagSpec);
	}


	/**
	 * Get an Feature Flag client for this admin session
	 * 
	 */
	public FeatureFlagHelper Features() {
		return new FeatureFlagHelper(featureFlagSpec);
	}

}
