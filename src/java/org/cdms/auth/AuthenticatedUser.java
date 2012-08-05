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
    
    private String firstName;
    private String lastName;
    private String ticket;
    
    public AuthenticatedUser(String username,
            String password,
            java.util.Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }
    public AuthenticatedUser(String firstName,String lastName,
            String username,
            String password,
            java.util.Collection<? extends GrantedAuthority> authorities) {
        this(username, password, authorities);
        this.firstName = firstName;
        this.lastName = lastName;
        this.ticket = password; //TODO: in production 
                
        
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
