package paw.project.backend_server.Domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.util.Date;

public class HttpResponse {
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="dd-MM-yyyy HH:mm:ss",timezone ="Europe/Bucharest")
    private Date timeStamp;
    private int httpStatusCode;//200, 404 etc
    private HttpStatus httpStatus;
    private String reason;
    private String message;
/*    {
        code:200,
        httpStatus:"OK",
        reason:"ok",
        message: "Your request was succesfull!"
    }*/
    public HttpResponse(){}//remove it i think

    public HttpResponse(int httpStatusCode, HttpStatus httpStatus, String reason, String message) {
        this.timeStamp=new Date();//to know when a httpResponse was given
        this.httpStatusCode = httpStatusCode;
        this.httpStatus = httpStatus;
        this.reason = reason;
        this.message = message;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
