Testing REST Services using REST-assured
========================================================


####Objective####

Extensible Framework to support multiple Microservices (for instance , ability to support authentication)
* Contract Tests which would fail when contract changes
* Functional Tests which checks for the "feature" of the microservice under tests
* Fail fast tests that can be run from Jenkins 
* Externalized property file to help run these tests against any environment

####System Under Test####

Testing an environment with Feature Flags:

1) The feature flags is expected to be Available in all env (testing and production) when created

2) Flags are not enabled by default. You have to be in your env and specifically enabled it.

For example,
I create a feature flag enabletable.dimension.It will show up in both env (testing and production) enabletable.dimension  off. As a user I have to go to specific env in the UI console and enable the flag to see its impact.

A cross team feature will have collection of feature flags under same namespace to enable a feature. For example:

*enableIngest

*enableIngest.platform

*enableIngest.dataloader

*enableIngest.hierarchies

In the example above 3 teams have defined feature flags  (enableIngest.platform,enableIngest.dataloader, enableIngest.hierarchies)

The three flags are interdependent because they fall under the same namespace (enableIngest)

Each team has the option to enable disable the flag for testing in any env but production.
In production environment all three feature flag belonging to a namespace needs to be enabled for the feature to be enabled.

####Assumptions####
There is a hypothetical service that can create multiple environments
There is no API to tear down Environments (marked as TBD in tests) 
There is no  Authentication for these API


####Stack####
This Java (Maven) project has been created using Eclipse Java EE IDE for Web Developers.Version: Oxygen.1a Release (4.7.1a) community edition..
 Tests have been written in JUnit.


####Framework Overview####
The framework consists of three main parts

##Business Objects##
     Based on the this example, the two objects in our example are Environment and Feature Flags . 
     RestAssured Framework works seamlessly serializing and deserializing data to these POJO.  
     The whole Business Objects package can be replaced if we can pull these objects from a maven repo.
##Helpers## 
      These contain the RestAssured Logic. The basic RESTAssured Calls are contained in these classes.
      Note there is an entry point to the service to handle authentication (placeholder now).
      Environment properties that are externalized are handled in Helper classes
##Tests##
      The tests are pure JUNIT tests. The RestAssured magic is abstracted from the test classes. It is pure translation of the "test case" into "Java"
      
         
