package com.api.tests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.api.businessobjects.Environment;
import com.api.businessobjects.FeatureFlag;
import com.api.helper.EnvironmentHelper;
import com.api.helper.FeatureFlagHelper;

public class FeatureFlagTests extends ApiTestsBase {

	EnvironmentHelper environmentHelper;
	FeatureFlagHelper featureFlagHelper;

	@BeforeClass
	public void setUpOrganizations() {
		environmentHelper = adminSession.environments();
		featureFlagHelper = adminSession.Features();
	}

	@After
	public void tearDownAfterEachTest() {
		// NEED API TO CLEAN UP ENVIRONMENTS CREATED BY EACH TEST
	}

	/**
	 * Assumption here is that there are no pre-existing Environments
	 * 
	 * @throws Exception
	 */
	@Test
	public void newDefaultFeatureTest() throws Exception {
		// Create a new Feature
		FeatureFlag defaultFeature = new FeatureFlag("StandandFeature1", false, null);
		ArrayList<FeatureFlag> featureFlags = new ArrayList<FeatureFlag>();
		featureFlags.add(defaultFeature);

		// Create a new Environment
		Environment env = new Environment("newDefaultFeature", "newDefaultFeature", featureFlags);
		Environment new_env = environmentHelper.createEnvironment(env);
		assertThat(new_env).isNotNull();

		// Retrive List of Environments and compare the environment with the original
		// request
		Environment[] environments = environmentHelper.listAllEnvironments();

		assertThat(environments).describedAs("Verify that one environment has been created").hasSize(1);
		assertThat(environments[0].getName()).describedAs("Verify the environment created matches request")
				.isEqualTo("newDefaultFeature");

		assertThat(environments[0].getFeatureFlags()).describedAs("Verify there are default flags").hasSize(1);

		assertThat(environments[0].getFeatureFlags().get(0).getFeatureflagName())
				.describedAs("Verify Feature Flag name matches request").isEqualTo("StandandFeature1");

		assertThat(environments[0].getFeatureFlags().get(0).getFeatureStatus())
				.describedAs("Verify Standard OOB Feature appears disabled").isFalse();
	}

	@Test
	// Creating a feature flag that is to be shared by sub-teams
	public void newSharedFeatureTest() throws Exception {
		// Create a new Feature
		FeatureFlag parentFeature = new FeatureFlag("SharedFeature1", true, null);
		FeatureFlag teamFeature1 = new FeatureFlag("Team1", false, "SharedFeature1");
		FeatureFlag teamFeature2 = new FeatureFlag("Team2", false, "SharedFeature1");
		ArrayList<FeatureFlag> featureFlags = new ArrayList<FeatureFlag>();
		featureFlags.add(parentFeature);
		featureFlags.add(teamFeature1);
		featureFlags.add(teamFeature2);

		// Create a new Environment
		Environment env = new Environment("newSharedFeatureTest", "newDefaultFeature", featureFlags);
		Environment new_env = environmentHelper.createEnvironment(env);
		assertThat(new_env).isNotNull();

		// Retrieve the environment and compare the flags
		Environment environment = environmentHelper.describeEnvironment(new_env);
		assertThat(environment.getName()).describedAs("Verify the environment created matches request")
				.isEqualTo("newSharedFeatureTest");
		ArrayList<FeatureFlag> features = featureFlagHelper.getListOfFeaturesInHeirarchy(environment, "SharedFeature1");

		assertThat(features).describedAs("Number of features in the heirarchy matches the request").hasSize(2);
		ArrayList<FeatureFlag> deltaFlags = new ArrayList<FeatureFlag>();
		for (FeatureFlag featureflag1 : featureFlags) {
			for (FeatureFlag featureflag2 : features) {
				if (!featureflag1.getFeatureflagName().equals(featureflag2.getFeatureflagName())) {
					deltaFlags.add(featureflag1);
				}
				if (featureflag1.isFeatureEnabled()) {
					deltaFlags.add(featureflag1);
				}
			}
		}
		assertThat(features)
				.describedAs("Number of Shared Sub Team Features do not match the request or A sub feature is enabled")
				.hasSize(0);
	}

	@Test
	public void newFeatureTest_toggleState() throws Exception {
		FeatureFlag defaultFeature = new FeatureFlag("DefaultFeature1", false, null);
		ArrayList<FeatureFlag> featureFlags = new ArrayList<FeatureFlag>();
		featureFlags.add(defaultFeature);

		// Create a new Environment
		Environment env = new Environment("newFeatureTest_false", "newFeatureTest_false", featureFlags);
		Environment new_env = environmentHelper.createEnvironment(env);
		assertThat(new_env).isNotNull();

		FeatureFlag envFeature = featureFlagHelper.getFeatureFlag(new_env, defaultFeature);
		assertThat(envFeature).isNotNull();
		assertThat(envFeature.getFeatureStatus()).describedAs("Verifying the Default status is False").isFalse();

		envFeature.setFeatureEnabled(true);
		featureFlagHelper.updateFlag(new_env, envFeature);

		envFeature = featureFlagHelper.getFeatureFlag(new_env, defaultFeature);
		assertThat(envFeature).isNotNull();
		assertThat(envFeature.getFeatureStatus()).describedAs("Updated Status is True").isTrue();

		envFeature.setFeatureEnabled(false);
		featureFlagHelper.updateFlag(new_env, envFeature);

		envFeature = featureFlagHelper.getFeatureFlag(new_env, defaultFeature);
		assertThat(envFeature).isNotNull();
		assertThat(envFeature.getFeatureStatus()).describedAs("Verifying the Default status is False").isFalse();

	}

	@Test
	public void newCrossTeamFeatureTest() throws Exception {
		FeatureFlag parentFeature = new FeatureFlag("SharedFeature1", true, null);
		FeatureFlag teamFeature1 = new FeatureFlag("Team1", false, "SharedFeature1");
		FeatureFlag teamFeature2 = new FeatureFlag("Team2", false, "SharedFeature1");
		ArrayList<FeatureFlag> featureFlags = new ArrayList<FeatureFlag>();
		featureFlags.add(parentFeature);
		featureFlags.add(teamFeature1);
		featureFlags.add(teamFeature2);

		// Create a new Environment
		Environment env = new Environment("newSharedFeatureTest", "newDefaultFeature", featureFlags);
		Environment new_env = environmentHelper.createEnvironment(env);
		assertThat(new_env).isNotNull();

		// Retrieve the environment and compare the flags
		Environment environment = environmentHelper.describeEnvironment(new_env);
		assertThat(environment.getName()).describedAs("Verify the environment created matches request")
				.isEqualTo("newSharedFeatureTest");
		ArrayList<FeatureFlag> features = featureFlagHelper.getListOfFeaturesInHeirarchy(environment, "SharedFeature1");
		
		FeatureFlag envFeature = featureFlagHelper.getFeatureFlag(new_env, teamFeature1);
		assertThat(envFeature.getFeatureStatus()).describedAs("Default Status is False").isFalse();
		
	
		FeatureFlag envFeature2 = featureFlagHelper.getFeatureFlag(new_env, teamFeature2);
		assertThat(envFeature.getFeatureStatus()).describedAs("Default Status is False").isFalse();
         
		envFeature.setFeatureEnabled(false);
		featureFlagHelper.updateFlag(new_env, envFeature);
		 envFeature = featureFlagHelper.getFeatureFlag(new_env, teamFeature1);
		assertThat(envFeature.getFeatureStatus()).describedAs("Updated Status is True").isTrue();
		envFeature2 = featureFlagHelper.getFeatureFlag(new_env, teamFeature2);
		assertThat(envFeature2.getFeatureStatus()).describedAs("Default Status is False").isFalse();
		envFeature2.setFeatureEnabled(true);
		featureFlagHelper.updateFlag(new_env, envFeature2);
		 envFeature = featureFlagHelper.getFeatureFlag(new_env, teamFeature1);
		assertThat(envFeature.getFeatureStatus()).describedAs("Updated Status is True").isTrue();
		
		envFeature2 = featureFlagHelper.getFeatureFlag(new_env, teamFeature2);
		assertThat(envFeature2.getFeatureStatus()).describedAs("Updated Status is True").isFalse();
		
		
	}

	@Test
	public void multipleHierarchyTest() throws Exception {
	}

	@Test
	public void getAllEnvironmentTest() throws Exception {
	}

	@Test
	public void getAllFlagsInEnvironmentTest() throws Exception {
	}

	@Test
	public void getAllFlags_HeirarchyTest() throws Exception {
	}

	@Test
	public void updateFlagFalse() throws Exception {
	}

	@Test
	public void updateFlagTrue() throws Exception {
	}

	@Test
	public void updateSharedFeatureFalse() throws Exception {
	}

	@Test
	public void updateSharedFeatureTrue() throws Exception {
	}

	@Test
	public void getAllFlags_MultipleHeirarchyTest() throws Exception {
	}

}
