package tests;



import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.*;
import io.restassured.http.ContentType;


import tests.fileReader.ConfigFileReader;
import tests.helper.RequestBodyBuilder;
import tests.helper.EndPoints;


import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;


public class TestMain {
    private static final Logger logger = Logger.getLogger(TestMain.class.getName());
    ConfigFileReader configFileReader = new ConfigFileReader();

    // Retrieving  the base URL and bearer token from the config file
    String baseUrl = configFileReader.getApplicationUrl();
    String bearerToken = configFileReader.getToken();

    private String userId;

    @BeforeTest
    void setup() {
        baseURI = baseUrl;
        RestAssured.baseURI = baseURI;

        // By Printing Verified the Base URL & BEARER TOKEN
        logger.info("Base URL: " + baseUrl);
        logger.info("Bearer Token: " + bearerToken);
    }

    @Test(priority = 0)
    @Description("POST - Creating a new user")
    void createUserTest() {
        // Calling Dynamic Variables from the RequestBodyBuilder.java class
        String name = RequestBodyBuilder.generateRandomName();
        String email = RequestBodyBuilder.generateRandomEmail();
        RequestBodyBuilder.Gender gender = RequestBodyBuilder.Gender.getRandomGender();
        String status = RequestBodyBuilder.Status.getRandomStatus().toString();

        // Define the endpoint for creating a user
        String endpoint = EndPoints.CREATE_USER;

        // Generated the request body using the provided parameter that used in RequestBodyBuilder.java file
        String requestBody = RequestBodyBuilder.createUserRequestBody(name, email, gender.toString(), status);

        // Performing a POST request to create a user
        Response response = RestAssured
                .given()
                .header("Authorization", "Bearer " + bearerToken)
                .contentType("application/json")
                .body(requestBody)
                .post(endpoint);

        // Assert the response status code to ensure successful creation
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 201);

        // Log the response details
        logger.info("Response Body: " + response.getBody().asString());
        logger.info("Response Status Code: " + statusCode);
        logger.info("Response Time: " + response.getTime());
        logger.info("Response Status Line: " + response.getStatusLine());
        logger.info("Response Content Type: " + response.getHeader("content-type"));

        // Assign userId regardless of whether a new user is created or not
        userId = response.jsonPath().getString("id");

        // Update the USERS_CREATED endpoint with userId
        EndPoints.setUsersCreatedEndpoint(userId);

        logger.info(userId);
    }


    @Test(priority = 1)
    @Description("GET - Retrieve the data after creating from the JSON")
    void getUserData() {
        //end - poitns
        String endpoint = EndPoints.USERS;
        // Perform a GET request to the specified endpoint
        Response response = given()
                // Set the bearer token call here
                .header("Authorization", "Bearer " + bearerToken )
                .contentType(ContentType.JSON)
                .get(endpoint);

        // verify by printing the all data
        logger.info("Base URI: " + baseURI);
        logger.info("Response Status Code: " + response.getStatusCode());
        logger.info("Response Time: " + response.getTime());
        logger.info("Response Status Line: " + response.getStatusLine());
        logger.info("Response Content Type: " + response.getHeader("content-type"));

        // Verify by print each user by using map
        String responseBody = response.getBody().asString();
        JsonPath jsonPath = new JsonPath(responseBody);
        List<Map<String, Object>> users = jsonPath.getList("$");
        for (Map<String, Object> user : users) {
            logger.info("User ID: " + user.get("id"));
            logger.info("Name: " + user.get("name"));
            logger.info("Email: " + user.get("email"));
            logger.info("Gender: " + user.get("gender"));
            logger.info("Status: " + user.get("status"));
        }

        // Print the response body by using logger
        logger.info("Response Body: " + responseBody);

        // Assert that the response status code is 200 (OK)
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 2)
    void fetchUserData() {
        Assert.assertNotNull(userId, "User ID is null. Make sure createUserTest() test case is executed successfully.");

        // Set the endpoint to fetch user data using the userId
        String endpoint = EndPoints.USERS_CREATED;
        // Perform a GET request to fetch user data
        Response response = given()
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(ContentType.JSON)
                .get(endpoint);

        // Print each user's details
        String responseBody = response.getBody().asString();
        logger.info("Response Body: " + responseBody);


        // Extract user details directly from JSON object
        JsonPath jsonPath = response.jsonPath();
        logger.info("User ID: " + jsonPath.get("data.id"));
        logger.info("Name: " + jsonPath.get("data.name"));
        logger.info("Email: " + jsonPath.get("data.email"));
        logger.info("Gender: " + jsonPath.get("data.gender"));
        logger.info("Status: " + jsonPath.get("data.status"));

        // Assert that the response status code is 200 (OK)
        Assert.assertEquals(response.getStatusCode(), 200);
    }



    @Test(priority = 3)
    @Description("PUT - UPDATE THE USER ALL DATA")
    void updateUser() {
        String name = RequestBodyBuilder.generateRandomName();
        String email = RequestBodyBuilder.generateRandomEmail();
        RequestBodyBuilder.Gender gender = RequestBodyBuilder.Gender.getRandomGender();
        String status = RequestBodyBuilder.Status.getRandomStatus().toString();

        // Defining the endpoint for updating user data
        String endpoint = EndPoints.USERS_CREATED;

        // Defined the updated data
        String updatedUserData = RequestBodyBuilder.updateUserRequestBody(name, email,gender.toString(), status);
        // Performing a PUT request to update
        Response response = given()
                .header("Authorization", "Bearer " + bearerToken) // setting the bearer token
                .contentType(ContentType.JSON)
                .body(updatedUserData)
                .when()
                .put(endpoint); // Specified the endpoints

        // Log response details
        logger.info("Response Status Code: " + response.getStatusCode());
        logger.info("Response Time: " + response.getTime());
        logger.info("Response Status Line: " + response.getStatusLine());
        logger.info("Response Content Type: " + response.getHeader("content-type"));

        // Verify the response body
        String responseBody = response.getBody().asString();
        logger.info("Response Body: " + responseBody);

        // Verify by asserting
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 4)
    @Description("PATCH - user data on the website")
    void patchUser() {

        String name = RequestBodyBuilder.generateRandomName();
        String email = RequestBodyBuilder.generateRandomEmail();
        String status = RequestBodyBuilder.Status.getRandomStatus().toString();
        // Define the endpoint for patching user data
        String endpoint = EndPoints.USERS_CREATED;

        // Defined the updated data
        String patchedUserData = RequestBodyBuilder.updateUserPartialData(name, email, status);

        // Performing a PATCH request to update the user data
        Response response = given()
                .header("Authorization", "Bearer " + bearerToken)
                .contentType(ContentType.JSON)
                .body(patchedUserData)
                .when()
                .put(endpoint);

        logger.info("Response Status Code: " + response.getStatusCode());
        logger.info("Response Time: " + response.getTime());
        logger.info("Response Status Line: " + response.getStatusLine());
        logger.info("Response Content Type: " + response.getHeader("content-type"));

        // printed the response body
        String responseBody = response.getBody().asString();
        logger.info("Response Body: " + responseBody);

        Assert.assertEquals(response.getStatusCode(), 200);
    }
    @Test(priority = 5)
    @Description("DELETE - removed user data from the website")
    void deleteUser() {
        String endpoint = EndPoints.USERS_CREATED;
        Response response = given()
                .header("Authorization", "Bearer " + bearerToken)
                .when()
                .delete(endpoint);

        logger.info("Response Status Code: " + response.getStatusCode());
        logger.info("Response Time: " + response.getTime());
        logger.info("Response Status Line: " + response.getStatusLine());
        logger.info("Response Content Type: " + response.getHeader("content-type"));

        // Printing the response body here
        String responseBody = response.getBody().asString();
        logger.info("Response Body: " + responseBody);

        try {
            Assert.assertEquals(response.getStatusCode(), 204);
        } catch (AssertionError e) {
        // If the assertion fails log it and continue
        logger.warning("Deletion operation did not return 204 status code. Expected 204, but got: " + response.getStatusCode());
        Assert.assertEquals(response.getStatusCode(), 404);
        }
    }
    @Test(priority = 6)
    @Description("POST - Negative test case: Attempting to create a user with invalid data")
    void createInvalidUser() {
        // Defining the end points
        String endpoint = EndPoints.USERS;

        // Missing required fields: email, gender, status
        String invalidRequestBody = "{ \"name\": \"bdjsbj\" }";

        // Perform a POST request to create a user with invalid data
        Response response = RestAssured
                .given()
                .header("Authorization", "Bearer " + bearerToken) // Set the authorization header with the bearer token
                .contentType(ContentType.JSON)
                .body(invalidRequestBody)
                .post(endpoint);

        // Assert the response status code to ensure the request failed
        int statusCode = response.getStatusCode();
        String responseBody = response.getBody().asString();
        logger.info("Response Body: " + responseBody);

        Assert.assertEquals(response.getStatusCode(), 422);
        Assert.assertEquals(statusCode, 422);
        // Log the response details
        logger.info("Response Body: " + response.getBody().asString());
        logger.info("Response Status Code: " + statusCode);
        logger.info("Response Time: " + response.getTime());
        logger.info("Response Status Line: " + response.getStatusLine());
        logger.info("Response Content Type: " + response.getHeader("content-type"));
    }

    @Test(priority = 7)
    @Description("DELETE - Negative test case: Attempting to delete a non-existent user")
    void deleteNonExistentUser() {
        // Define the endpoint for deleting a user
        String endpoint = EndPoints.USERS_CREATED;

        // Perform a DELETE request to delete a non-existent END POINT
        Response response = given()
                .header("Authorization", "Bearer " + bearerToken)
                .when()
                .delete(endpoint);

        // Assert the response status code to ensure the request failed
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 404);

        // Log the response details
        logger.info("Response Status Code: " + response.getStatusCode());
        logger.info("Response Time: " + response.getTime());
        logger.info("Response Status Line: " + response.getStatusLine());
        logger.info("Response Content Type: " + response.getHeader("content-type"));
    }

    @Test(priority = 8)
    @Description("PUT - Negative test case: Attempting to perform operation on Invalid End Points")
    void invalidEndPoints() {
        // Define the endpoint for deleting a user
        String endpoint = EndPoints.INVALID_ENDPOINT;

        // Perform a DELETE request to delete a non-existent END POINT
        Response response = given()
                .header("Authorization", "Bearer " + bearerToken)
                .when()
                .put(endpoint);

        //Asserting the response status code
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 404);

        // Log the response details
        logger.info("Response Status Code: " + response.getStatusCode());
        logger.info("Response Time: " + response.getTime());
        logger.info("Response Status Line: " + response.getStatusLine());
        logger.info("Response Content Type: " + response.getHeader("content-type"));
    }

    @Test(priority = 9)
    @Description("PATCH - Negative test case: Attempting to perform operation on Invalid Token")
    void invalidToken() {
        // Define the endpoint for deleting a user
        String endpoint = EndPoints.USERS_CREATED;

        // PerformING a DELETE request to delete a non-existent END POINTS
        Response response = given()
                .header("Authorization", "Bearer SDAGHDSDUHUHAW") //Invalid end point here
                .when()
                .patch(endpoint);

        //Asserting the response status code
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 401);

        // Log the response details
        logger.info("Response Status Code: " + response.getStatusCode());
        logger.info("Response Time: " + response.getTime());
        logger.info("Response Status Line: " + response.getStatusLine());
        logger.info("Response Content Type: " + response.getHeader("content-type"));
    }
}





