package org.thingsboard.server.dao.sql.device;

import org.springframework.data.repository.CrudRepository;
import org.thingsboard.server.dao.model.sql.DeviceCredentialsEntity;
import org.thingsboard.server.dao.util.SqlDao;

/**
 * Created by Valerii Sosliuk on 5/6/2017.
 */
@SqlDao
public interface DeviceCredentialsRepository extends CrudRepository<DeviceCredentialsEntity, String> {

    DeviceCredentialsEntity findByDeviceId(String deviceId);

    DeviceCredentialsEntity findByCredentialsId(String credentialsId);
}
