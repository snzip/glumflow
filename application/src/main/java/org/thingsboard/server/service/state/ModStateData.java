package org.thingsboard.server.service.state;

import lombok.Builder;
import lombok.Data;
import org.thingsboard.server.common.data.id.ModId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.msg.TbMsgMetaData;

/**
 * Created by ashvayka on 01.05.18.
 */
@Data
@Builder
class ModStateData {

    private final TenantId tenantId;
    private final ModId modId;

    private TbMsgMetaData metaData;
    private final ModState state;

}
