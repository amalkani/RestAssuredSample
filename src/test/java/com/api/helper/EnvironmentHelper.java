package com.api.helper;

import static io.restassured.RestAssured.given;

import java.util.Objects;

import com.api.businessobjects.Environment;

import io.restassured.specification.RequestSpecification;

public class EnvironmentHelper {

	Environment env = new Environment();
	final RequestSpecification spec;

	public EnvironmentHelper(RequestSpecification spec) {
		this.spec = spec;
	}

	/**
	 * Enables creating an Environment with Out of Box default Features (set to
	 * Boolean=False)
	 * 
	 * @param env
	 * @return
	 * @throws NullPointerException
	 */
	public Environment createEnvironment(Environment env) throws NullPointerException {
		Objects.requireNonNull(env);
		this.env = env;
		Environment environment = given().spec(spec).contentType("application/json").body(env).when()
				.post("/api/environment").then().statusCode(200).extract().body().as(Environment.class);
		return environment;
	}

	/**
	 * Describes the Environment
	 * 
	 * @param env
	 * @return
	 * @throws NullPointerException
	 */
	public Environment describeEnvironment(Environment env) throws NullPointerException {
		Objects.requireNonNull(env);
		this.env = env;
		Environment environment = given().spec(spec).when().get("/api/environment/" + env.getName()).then()
				.statusCode(200).log().all().and().extract().body().as(Environment.class);
		return environment;
	}

	/**
	 * Lists all Environments that have been created.
	 * 
	 * @param env
	 * @return
	 * @throws NullPointerException
	 */
	public Environment[] listAllEnvironments() throws NullPointerException {
		Environment[] environments = given().spec(spec).when().get("/api/environment").then().statusCode(200).extract()
				.body().as(Environment[].class);
		return environments;
	}

}
