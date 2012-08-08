/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cdms.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 *
 * @author Valery
 */
public class AuthenticatedUser extends User{
    private Long userId;
    private String firstName;
    private String lastName;
    private String ticket;
    
    public AuthenticatedUser(Long userId,String username,
            String password,
            java.util.Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = userId;
    }
    public AuthenticatedUser(Long userId,String firstName,String lastName,
            String username,
            String password,
            java.util.Collection<? extends GrantedAuthority> authorities) {
        this(userId,username, password, authorities);
        this.firstName = firstName;
        this.lastName = lastName;
        this.ticket = password; //TODO: in production 
                
        
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getFirstName() {
        GrantedAuthority a;
        
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
}
