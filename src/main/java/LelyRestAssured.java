
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.User;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.List;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class LelyRestAssured {


    //Create singleUse data with randomize email
    private static User user = new User("test222","male","active");
    String token = "1db9c9b6c959682be7c96f74ca532c3cb0bd331f46b86a92602f8d319481b6f5";
    final static Logger logger = Logger.getLogger(LelyRestAssured.class);

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "https://gorest.co.in";
        RestAssured.basePath ="/public/v1/users";

    }

    @DisplayName("Check ID's 4 digits")
    @Test
    public void checkIds4digitsAndNotNull() {
        List<Integer> ids = given().
                when().
                get("").jsonPath().getList("data.id");

        for(Integer id : ids)
        {
            assertAll("Id must be 4 digits and not null",
                    () -> assertEquals(4, String.valueOf(id).length()),
                    () -> assertNotNull(String.valueOf(id).length())
            );
        }
    }


    @Order(1)
    @DisplayName("Create an user and check if Successfull")
    @Test
    public void createUserNewEmailAdressCheckSuccessful() {

        Response response = given()
                .log().all()
                .header("authorization", "Bearer " + token)
                .formParam("email", user.getEmail())
                .formParam("name", user.getName())
                .formParam("status", user.getStatus())
                .formParam("gender",user.getGender())
                .when().post("");

        JsonPath jsonPathEvaluator = response.jsonPath();

        assertAll("Response validation : Check Location Value and Created user must be same as Requested",
                () -> assertNotNull(response.getHeader("Location")),
                () -> assertEquals(jsonPathEvaluator.get("data.email"), user.getEmail()),
                () -> assertEquals(jsonPathEvaluator.get("data.name"), user.getName()),
                () -> assertEquals(jsonPathEvaluator.get("data.gender"), user.getGender()),
                () -> assertEquals(jsonPathEvaluator.get("data.status"), user.getStatus())
        );

        System.out.println("Created user URL : " + response.getHeader("Location"));
        logger.info("Created user URL : " + response.getHeader("Location"));

    }

    @Order(2)
    @DisplayName("Create user with used email adress and check if Unsuccessful")
    @Test
    public void createUserUsedEmailAdressCheckUnsuccessful() {

        Response response = given()
                .log().all()
                .header("authorization", "Bearer " + token)
                .formParam("email", user.getEmail())
                .formParam("name", user.getName())
                .formParam("status", user.getStatus())
                .formParam("gender",user.getGender())
                .when().post("");

        //Check if user not created by controlling Header-Location Parameter
        Assert.assertNull(response.getHeader("Location"));
        logger.info("Error Message in Response : " + getJsonPath(response,"data[0].message"));
        System.out.println("Error Message in Response : " + getJsonPath(response,"data[0].message"));
    }

    public static String getJsonPath(Response response, String key) {
        String complete = response.asString();
        JsonPath js = new JsonPath(complete);
        return js.get(key).toString();
    }




}