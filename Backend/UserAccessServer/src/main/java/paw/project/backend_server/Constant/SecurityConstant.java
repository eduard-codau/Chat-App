package paw.project.backend_server.Constant;

public class SecurityConstant  {
    public static final long EXPIRATION_TIME=432_000_000;//5 days in milliseconds

    public static final String TOKEN_PREFIX="Bearer ";// whoever gives me this token, i don't have to do any further verifications
    //if the Token have Bearer, i don't care how it obtained it, i just take it

    public static final String JWT_TOKEN_HEADER="Jwt-Token";//custom http header

    public static final String TOKEN_CANNOT_BE_VERIFIED="Token cannot be verified";//when the token cannot be verified

    public static final String GET_ARRAYS_LLC="Get Arrays, LLC";//the issuer of the token(ex:this was provided by Google,or Amazon etc

    public static final String GET_ARRAYS_ADMINISTRATION="User Management Portal";//who are grant to use that token

    public static final String AUTHORITIES="authorities";
    public static final String FORBIDDEN_MESSAGE="You need to log in to access this page!";

    public static final String ACCESS_DENIED_MESSAGE="You do not have permission to access this page!";

    public static final String OPTIONS_HTTP_METHOD="OPTIONS";

    //public static final String[] PUBLIC_URLS={"/user/login","/user/register","/user/resetpassword/**","/user/image/**","/api/user/home"};
    public static final String[] PUBLIC_URLS={"**"};


}
