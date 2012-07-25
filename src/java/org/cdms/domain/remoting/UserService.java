package org.cdms.domain.remoting;

import org.cdms.domain.User;

/**
 *
 * @author V. Shyshkin
 */
public interface UserService {
    User findById(long userId);
    void insert(User user);
    void update(User user);
    void delete(long id);
}
