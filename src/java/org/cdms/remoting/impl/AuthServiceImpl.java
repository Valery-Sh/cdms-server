package org.cdms.remoting.impl;

import org.cdms.auth.AuthenticatedUser;
import org.cdms.entities.User;
import org.cdms.remoting.AuthService;
import org.cdms.remoting.UserInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author V. Shyshkin
 */
public class AuthServiceImpl implements AuthService{

    @Override
    public UserInfo authenticate(String name, String password) {
        UserInfo user = null;
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        if ( a .getDetails() instanceof AuthenticatedUser) {
            AuthenticatedUser au = (AuthenticatedUser)a.getDetails();
            user = new UserInfo();
            user.setFirstName(au.getFirstName());
            user.setLastName(au.getLastName());
            user.setUserName(au.getUsername());
            for ( GrantedAuthority gr : au.getAuthorities()) {
                user.getRoles().add(gr.getAuthority());
            }
        }
        
        return user;
    }
    
}
