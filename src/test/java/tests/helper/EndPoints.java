package tests.helper;

// Defining all the end points for the POST, GET, PUT, PATCH, DELETE endpoints
public class EndPoints {

    public static final String USERS = "/public/v2/users";
    public static String USERS_CREATED = "/public/v2/users/{{userId}}";

    public static void setUsersCreatedEndpoint(String userId) {
        USERS_CREATED = USERS_CREATED.replace("{{userId}}", userId);
    }

    public static final String CREATE_USER = "/public/v2/users" ;

    public static final String INVALID_ENDPOINT = "/public/v2/use";
}
