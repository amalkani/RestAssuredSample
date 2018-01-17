package com.api.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Before;

import com.api.helper.FeatureFlagServiceSession;
import com.api.helper.MicroServiceTestException;
import com.api.helper.MicroserviceEnvironment;

public class ApiTestsBase {

	private static final File microservicePropertiesFile = new File("platform.properties");

	protected MicroserviceEnvironment featureFlagServiceEnvironment;
	protected FeatureFlagServiceSession adminSession;

	@Before
	public void buildEnvironment() throws ConfigurationException {
		Properties properties = new Properties();

		if (microservicePropertiesFile.exists()) {
			try (InputStream in = new FileInputStream(microservicePropertiesFile)) {
				properties.load(in);
			} catch (IOException ex) {
				throw new MicroServiceTestException("Failed to load properties from " + microservicePropertiesFile, ex);
			}
		} else {
			properties.putAll(System.getProperties());
		}

		String featureFlagServiceProtocol = properties.getProperty("featureFlagServiceProtocol");
		String featureFlagServiceUrn = properties.getProperty("featureFlagServiceUrn");
		String logRequests = properties.getProperty("logRequests");

		MicroserviceEnvironment.Builder featureFlagServiceBuilder = MicroserviceEnvironment.builder()
				.withFeatureFlagServiceProtocol(featureFlagServiceProtocol)
				.withFeatureFlagServiceUrn(featureFlagServiceUrn).withRequestLogging();

		if (logRequests.equalsIgnoreCase("true")) {
			featureFlagServiceBuilder.withRequestLogging();
		}

		featureFlagServiceEnvironment = featureFlagServiceBuilder.build();

	}

}
