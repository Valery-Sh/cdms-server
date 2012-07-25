package org.cdms.domain.dao;

import java.util.List;
import org.cdms.domain.User;

/**
 *
 * @author V. Shyshkin
 */
public interface UserDao {
    void insert(User user); 
    void update(User user); 
    void delete(Long id); 
    User findById(Long id); 
    List<User> findAll(); 
}
