package paw.project.backend_server.Enumeration;

import static paw.project.backend_server.Constant.Authority.*;

public enum Role {
    ROLE_USER(USER_AUTHORITIES),
    ROLE_ADMIN(ADMIN_AUTHORITIES),
    ROLE_GROUP_MANAGER(GROUP_MANAGER_AUTHORITIES);

    private String[] authorities;

    Role(String ... authorities){
        this.authorities=authorities;
    }

    public String[] getAuthorities() {
        return authorities;
    }
    //ROLE_USER.getAuthorities()=>USER_AUTHORITIES
}
