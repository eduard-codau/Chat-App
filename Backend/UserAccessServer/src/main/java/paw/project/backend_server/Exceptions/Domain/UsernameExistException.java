package paw.project.backend_server.Exceptions.Domain;

public class UsernameExistException extends Exception{
    public UsernameExistException(String message) {
        super(message);
    }
}
