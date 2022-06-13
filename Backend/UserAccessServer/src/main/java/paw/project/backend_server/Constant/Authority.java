package paw.project.backend_server.Constant;

public class Authority {
    public static final String[] USER_AUTHORITIES={"user:read"};
    public static final String[] ADMIN_AUTHORITIES={"user:read","user:update","user:create","user:delete"};
    public static final String[] GROUP_MANAGER_AUTHORITIES={"user:read","user:update"};



}
