package com.api.helper;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.cookie.CookieFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.filter.session.SessionFilter;

import io.restassured.specification.RequestSpecification;

/**
 * Representation and entry point for the Feature Flag -microservice Can be
 * extended to capture specific authentication that might be required to use
 * this microservice
 */
public class MicroserviceEnvironment {

	public static final String SESSION_ID_COOKIE_NAME = "SessionId";

	public static Builder builder() {
		return new Builder();
	}

	private static RequestSpecification createRequestSpec(String protocol, String host, boolean logRequests) {
		RequestSpecBuilder result = new RequestSpecBuilder().setBaseUri(String.format("%s://%s", protocol, host))
				.addFilter(new CookieFilter()).addFilter(new SessionFilter());

		if (logRequests) {
			result.addFilter(new RequestLoggingFilter()).addFilter(new ResponseLoggingFilter());
		}

		return result.build();
	}

	private final String featureFlagServiceProtocol;
	private final String featureFlagServiceURN;
	private final boolean logRequests;

	protected MicroserviceEnvironment(Builder builder) {
		this.featureFlagServiceProtocol = builder.featureFlagServiceProtocol;
		this.featureFlagServiceURN = builder.featureFlagServiceURN;
		this.logRequests = builder.logRequests;
	}

	public static class Builder {
		private String featureFlagServiceProtocol;
		private String featureFlagServiceURN;
		private boolean logRequests;

		public Builder withFeatureFlagServiceProtocol(String featureFlagServiceProtocol) {
			this.featureFlagServiceProtocol = featureFlagServiceProtocol;
			return this;
		}

		public Builder withFeatureFlagServiceUrn(String featureFlagServiceURN) {
			this.featureFlagServiceURN = featureFlagServiceURN;
			return this;
		}

		public Builder withRequestLogging() {
			this.logRequests = true;
			return this;
		}

		public MicroserviceEnvironment build() {
			return new MicroserviceEnvironment(this);
		}
	}
}
