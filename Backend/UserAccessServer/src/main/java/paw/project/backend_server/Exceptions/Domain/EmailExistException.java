package paw.project.backend_server.Exceptions.Domain;

public class EmailExistException extends Exception{
    public EmailExistException(String message) {
        super(message);
    }
}
