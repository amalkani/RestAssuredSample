package com.api.helper;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.api.businessobjects.Environment;
import com.api.businessobjects.FeatureFlag;

import io.restassured.specification.RequestSpecification;

public class FeatureFlagHelper {

	FeatureFlag featurFlag = new FeatureFlag();
	Environment environment = new Environment();
	final RequestSpecification spec;

	public FeatureFlagHelper(RequestSpecification spec) {
		this.spec = spec;
	}

	/**
	 * Create a new feature flag. This flag will be passed on in Disabled state to
	 * all environments that are created
	 * 
	 * @param defaultFeature
	 * @return
	 * @throws NullPointerException
	 */
	public FeatureFlag createDefaultFeature(FeatureFlag defaultFeature) throws NullPointerException {
		Objects.requireNonNull(defaultFeature);
		FeatureFlag featureFlag = given().spec(spec).contentType("application/json").body(defaultFeature).when()
				.post("/api/flags").then().statusCode(200).extract().body().as(FeatureFlag.class);
		return featureFlag;
	}

	/**
	 * This method returns the Environment Specific Feature Flag
	 * 
	 * @param env
	 * @param feature
	 * @return
	 * @throws NullPointerException
	 */
	public FeatureFlag getFeatureFlag(Environment env, FeatureFlag feature) throws NullPointerException {
		Objects.requireNonNull(env);
		Objects.requireNonNull(feature);
		FeatureFlag featureFlag = given().spec(spec).when()
				.get("/api/flags/" + env.getName() + "/" + feature.getFeatureflagName()).then().statusCode(200).log()
				.all().and().extract().body().as(FeatureFlag.class);
		return featureFlag;
	}

	/**
	 * Returns the list of all Flags in the specific Environment
	 * 
	 * @param env
	 * @return
	 * @throws NullPointerException
	 */
	public FeatureFlag[] listAllFeatureFlags(Environment env) throws NullPointerException {
		Objects.requireNonNull(env);

		FeatureFlag[] flagList = given().spec(spec).when().get("/api/flags/" + env.getName()).then().statusCode(200)
				.extract().body().as(FeatureFlag[].class);
		return flagList;
	}

	/**
	 * Enables toggling the status of the specific Flag
	 * 
	 * @param env
	 * @param feature
	 * @return
	 * @throws NullPointerException
	 */

	public FeatureFlag update(Environment env, FeatureFlag feature) throws NullPointerException {
		Objects.requireNonNull(env);
		Objects.requireNonNull(feature);

		FeatureFlag flagList = given().spec(spec).contentType("application/json").body(feature.getFeatureStatus())
				.when().post("/api/flags/" + env.getName() + "/" + feature.getFeatureflagName()).then().statusCode(200)
				.extract().body().as(FeatureFlag.class);
		return flagList;
	}

	public  ArrayList<FeatureFlag> getListOfFeaturesInHeirarchy(Environment env, String parentHeirarchy) throws NullPointerException {
		FeatureFlag[] flags=listAllFeatureFlags(env);
		List<FeatureFlag> flagList = Arrays.asList(flags);
		 ArrayList<FeatureFlag> featureFlags = new ArrayList<FeatureFlag>();
		for (FeatureFlag flag : flagList) {
	        if (flag.getParent_featureflagName().equalsIgnoreCase(parentHeirarchy))
	        		 {featureFlags.add(flag);}
	        }
       return featureFlags;
			
	}
}
