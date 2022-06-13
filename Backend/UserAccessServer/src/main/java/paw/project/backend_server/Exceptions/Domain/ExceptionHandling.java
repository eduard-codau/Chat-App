package paw.project.backend_server.Exceptions.Domain;

import com.auth0.jwt.exceptions.TokenExpiredException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import paw.project.backend_server.Domain.HttpResponse;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Objects;

@RestControllerAdvice
public class ExceptionHandling implements ErrorController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    public static final String ACCOUNT_LOCKED="Your account has been locked. Please contact administration!";
    public static final String METHOD_IS_NOT_ALLOWED="This request method is not allowed on this endpoint. Please send a '%s' request";
    public static final String INTERNAL_SERVER_ERROR_MSG="An error occurred while processing the request.";
    public static final String INCORRECT_CREDENTIALS="Username / password incorrect. Please try again";
    public static final String ACCOUNT_DISABLED="Your account has been disabled. If this is an error, please contact administration.";
    public static final String ERROR_PROCESSING_FILE="Error occurred while processing file.";
    public static final String NOT_ENOUGH_PERMISSION="You do not have enough permission.";
    public static final String USERNAME_ALREADY_EXISTS="This username already exists!";
    public static final String EMAIL_ALREADY_EXISTS="This email already exists!";

    //public static final String ERROR_PATH="/error";

    /*//replace Whitelabel Page Error
    public String getErrorPath(){
        return ERROR_PATH;
    }
    @RequestMapping(ERROR_PATH)
    public ResponseEntity<HttpResponse> notFound404(){
        return createHttpResponse(HttpStatus.NOT_FOUND, "There is no mapping for this URL!");
    }*/



    //if the user is trying to log in, but his account is disabled, then this function is gonna be fired
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<HttpResponse> accountDisabledException(){
        return createHttpResponse(HttpStatus.BAD_REQUEST,ACCOUNT_DISABLED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpResponse> badCredentialsException(){
        return createHttpResponse(HttpStatus.BAD_REQUEST,INCORRECT_CREDENTIALS);//should be UNAUTHORIZED
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HttpResponse> accessDeniedException(){
        return createHttpResponse(HttpStatus.FORBIDDEN,NOT_ENOUGH_PERMISSION);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<HttpResponse> lockedException(){
        return createHttpResponse(HttpStatus.UNAUTHORIZED,ACCOUNT_LOCKED);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<HttpResponse> tokenExpiredException(TokenExpiredException exception){
        return createHttpResponse(HttpStatus.UNAUTHORIZED,exception.getMessage());
    }

    @ExceptionHandler(EmailExistException.class)
    public ResponseEntity<HttpResponse> emailExistException(EmailExistException exception){
        return createHttpResponse(HttpStatus.BAD_REQUEST, EMAIL_ALREADY_EXISTS);
    }

    @ExceptionHandler(UsernameExistException.class)
    public ResponseEntity<HttpResponse> usernameExistException(UsernameExistException exception){
        return createHttpResponse(HttpStatus.BAD_REQUEST, USERNAME_ALREADY_EXISTS);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<HttpResponse> emailNotFoundException(EmailNotFoundException exception){
        return createHttpResponse(HttpStatus.BAD_REQUEST,exception.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<HttpResponse> userNotFoundException(UserNotFoundException exception){
        return createHttpResponse(HttpStatus.BAD_REQUEST,exception.getMessage());
    }

    //it's gonna be thrown if the user tries to send a request and use the wrong request method
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<HttpResponse> methodNotSupportedException(HttpRequestMethodNotSupportedException exception){
        HttpMethod supportedMethod= Objects.requireNonNull(exception.getSupportedHttpMethods()).iterator().next();
        return createHttpResponse(HttpStatus.METHOD_NOT_ALLOWED,String.format(METHOD_IS_NOT_ALLOWED,supportedMethod));
    }

    //this is the implicit exception if the others exceptions didn't occurred
    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse> internalServerErrorException(Exception exception){
        LOGGER.error(exception.getMessage());//for debugging purposes
        return createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR,INTERNAL_SERVER_ERROR_MSG);
    }

    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<HttpResponse> noFoundException(NoResultException exception){
        LOGGER.error(exception.getMessage());
        return createHttpResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<HttpResponse> iOException(IOException exception){
        LOGGER.error(exception.getMessage());
        return createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_PROCESSING_FILE);
    }

    //tells spring just throw this exception, not handling it by yourself(the page generated with Whitelabel error)->impl in ErrorMvcAutoConfiguration.class
    //for the WhiteLabel
    //@ExceptionHandler(NoHandlerFoundException.class)
    //public ResponseEntity<HttpResponse> noHandlerFoundException(NoHandlerFoundException exception){
        //return createHttpResponse(HttpStatus.BAD_REQUEST, "This page was not found!");
    //}





    //we will use this anytime we need to return an HttP Response to the user
    private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message){
        HttpResponse httpResponse=new HttpResponse(httpStatus.value(),httpStatus,httpStatus.getReasonPhrase().toUpperCase(),
                message.toUpperCase());//this is for the body
        return new ResponseEntity<>(httpResponse,httpStatus);

    }
}
