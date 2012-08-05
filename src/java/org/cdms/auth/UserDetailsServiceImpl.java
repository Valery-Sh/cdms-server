package org.cdms.auth;

import java.util.ArrayList;
import java.util.List;
import org.cdms.entities.Permission;
import org.cdms.remoting.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 *
 * @author V. Shyshkin
 */
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        
        AuthenticatedUser au = findByUsername(username);
        
        if ( au != null ) {
            return au;
        } else {
            throw new UsernameNotFoundException("User not found: " + username);
        }

    }

    protected AuthenticatedUser findByUsername(String userName) {
        
        org.cdms.entities.User u = userService.findByUsername(userName);
        
        if (u == null) {
            return null;
        }
        
        List<GrantedAuthority> permissions = new ArrayList<GrantedAuthority>();
        for ( Permission p : u.getPermissions() ) {
            permissions.add(new SimpleGrantedAuthority(p.getPermission()));
        }
    
        AuthenticatedUser au = new AuthenticatedUser(userName, u.getPassword(), permissions);
        
        au.setFirstName(u.getFirstName());
        au.setLastName(u.getLastName());
        return au;
    }
}
