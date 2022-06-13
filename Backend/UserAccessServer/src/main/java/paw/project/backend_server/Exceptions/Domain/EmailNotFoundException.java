package paw.project.backend_server.Exceptions.Domain;

public class EmailNotFoundException extends Exception{
    public EmailNotFoundException(String message) {
        super(message);
    }
}
