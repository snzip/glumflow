package org.thingsboard.server.dao.model.nosql;

import com.datastax.driver.core.utils.UUIDs;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import lombok.EqualsAndHashCode;
import org.thingsboard.server.common.data.id.UserCredentialsId;
import org.thingsboard.server.common.data.id.UserId;
import org.thingsboard.server.common.data.security.UserCredentials;
import org.thingsboard.server.dao.model.BaseEntity;

import java.util.UUID;

import static org.thingsboard.server.dao.model.ModelConstants.ID_PROPERTY;
import static org.thingsboard.server.dao.model.ModelConstants.USER_CREDENTIALS_ACTIVATE_TOKEN_PROPERTY;
import static org.thingsboard.server.dao.model.ModelConstants.USER_CREDENTIALS_COLUMN_FAMILY_NAME;
import static org.thingsboard.server.dao.model.ModelConstants.USER_CREDENTIALS_ENABLED_PROPERTY;
import static org.thingsboard.server.dao.model.ModelConstants.USER_CREDENTIALS_PASSWORD_PROPERTY;
import static org.thingsboard.server.dao.model.ModelConstants.USER_CREDENTIALS_RESET_TOKEN_PROPERTY;
import static org.thingsboard.server.dao.model.ModelConstants.USER_CREDENTIALS_USER_ID_PROPERTY;

@Table(name = USER_CREDENTIALS_COLUMN_FAMILY_NAME)
@EqualsAndHashCode
public final class UserCredentialsEntity implements BaseEntity<UserCredentials> {

    @PartitionKey(value = 0)
    @Column(name = ID_PROPERTY)
    private UUID id;
    
    @Column(name = USER_CREDENTIALS_USER_ID_PROPERTY)
    private UUID userId;

    @Column(name = USER_CREDENTIALS_ENABLED_PROPERTY)
    private boolean enabled;

    @Column(name = USER_CREDENTIALS_PASSWORD_PROPERTY)
    private String password;

    @Column(name = USER_CREDENTIALS_ACTIVATE_TOKEN_PROPERTY)
    private String activateToken;

    @Column(name = USER_CREDENTIALS_RESET_TOKEN_PROPERTY)
    private String resetToken;

    public UserCredentialsEntity() {
        super();
    }

    public UserCredentialsEntity(UserCredentials userCredentials) {
        if (userCredentials.getId() != null) {
            this.id = userCredentials.getId().getId();
        }
        if (userCredentials.getUserId() != null) {
            this.userId = userCredentials.getUserId().getId();
        }
        this.enabled = userCredentials.isEnabled();
        this.password = userCredentials.getPassword();
        this.activateToken = userCredentials.getActivateToken();
        this.resetToken = userCredentials.getResetToken();
    }
    
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
    
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getActivateToken() {
        return activateToken;
    }

    public void setActivateToken(String activateToken) {
        this.activateToken = activateToken;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    @Override
    public UserCredentials toData() {
        UserCredentials userCredentials = new UserCredentials(new UserCredentialsId(id));
        userCredentials.setCreatedTime(UUIDs.unixTimestamp(id));
        if (userId != null) {
            userCredentials.setUserId(new UserId(userId));
        }
        userCredentials.setEnabled(enabled);
        userCredentials.setPassword(password);
        userCredentials.setActivateToken(activateToken);
        userCredentials.setResetToken(resetToken);
        return userCredentials;
    }

}