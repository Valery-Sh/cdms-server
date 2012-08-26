package org.cdms.remoting.impl;

import org.cdms.auth.AuthenticatedUser;
import org.cdms.shared.remoting.AuthService;
import org.cdms.shared.remoting.UserInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 *
 * @author V. Shyshkin
 */
public class AuthServiceImpl implements AuthService{
    
    @Override
    public UserInfo authenticate(String name, String password) {
        UserInfo user = null;
        WebAuthenticationDetails w;
        
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        Object principal = a.getPrincipal();
        
        if ( principal instanceof AuthenticatedUser) {
            AuthenticatedUser au = (AuthenticatedUser)principal;
            user = new UserInfo();
            user.setId(au.getUserId());
            user.setFirstName(au.getFirstName());
            user.setLastName(au.getLastName());
            user.setUserName(au.getUsername());
            user.setTicket(au.getTicket());
            for ( GrantedAuthority gr : au.getAuthorities()) {
                user.getRoles().add(gr.getAuthority());
            }
        }
        
        return user;
    }
    
}
