package org.cdms.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    
    private long id;
    private long version;

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
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CDMS_USERS_SEQ")
    @SequenceGenerator(allocationSize=1,initialValue=10, name = "CDMS_USERS_SEQ", sequenceName = "CDMS_USERS_SEQ")    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    @Version
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
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
    
//    @OneToMany(mappedBy = "userid")
//    private List<CdmsPermissions> cdmsPermissionsList;

}
