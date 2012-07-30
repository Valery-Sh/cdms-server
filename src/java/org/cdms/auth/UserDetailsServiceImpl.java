package org.cdms.auth;

import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 *
 * @author V. Shyshkin
 */
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        List<GrantedAuthority> permissions = new ArrayList<GrantedAuthority>();

        if (username.equals("u")) {

            permissions.add(new SimpleGrantedAuthority("supervisor"));
            permissions.add(new SimpleGrantedAuthority("user"));
            permissions.add(new SimpleGrantedAuthority("teller"));
            User u;

            return new User("u", "up", permissions);
        } else {
            throw new UsernameNotFoundException("User not found: " + username);
        }

    }
}
