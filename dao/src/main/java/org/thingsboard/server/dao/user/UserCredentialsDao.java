package org.thingsboard.server.dao.user;

import org.thingsboard.server.common.data.security.UserCredentials;
import org.thingsboard.server.dao.Dao;

import java.util.UUID;

/**
 * The Interface UserCredentialsDao.
 */
public interface UserCredentialsDao extends Dao<UserCredentials> {

    /**
     * Save or update user credentials object
     *
     * @param userCredentials the user credentials object
     * @return saved user credentials object
     */
    UserCredentials save(UserCredentials userCredentials);

    /**
     * Find user credentials by user id.
     *
     * @param userId the user id
     * @return the user credentials object
     */
    UserCredentials findByUserId(UUID userId);

    /**
     * Find user credentials by activate token.
     *
     * @param activateToken the activate token
     * @return the user credentials object
     */
    UserCredentials findByActivateToken(String activateToken);

    /**
     * Find user credentials by reset token.
     *
     * @param resetToken the reset token
     * @return the user credentials object
     */
    UserCredentials findByResetToken(String resetToken);

}
