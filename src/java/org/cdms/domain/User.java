package org.cdms.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 
 * @author V. Shyshkin
 */
@Entity 
@Table(name = "cdms_Users") 
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CDMS_USERS_SEQ")
    @SequenceGenerator(allocationSize=1,initialValue=10, name = "CDMS_USERS_SEQ", sequenceName = "CDMS_USERS_SEQ")    
    private Long id;
    @Version
    private Long version;

    @NotNull 
    @Size(min=1,max=16) 
    private String firstName;
    @NotNull 
    @Size(min=2,max=16) 
    private String lastName;
    @NotNull 
    @Size(min=1,max=16) 
    private String userName;
    @NotNull 
    @Size(min=1,max=16) 
    private String password;
    
    @OneToMany(mappedBy = "user")
    private List<Permission> permissions = new ArrayList<Permission>();
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getFirstName() {
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    //@OneToMany(mappedBy = "user")
    //@OneToMany

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    

}
