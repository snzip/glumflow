package org.thingsboard.rule.engine.util;

import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.thingsboard.rule.engine.api.TbContext;
import org.thingsboard.rule.engine.api.TbNodeException;
import org.thingsboard.server.common.data.HasTenantId;
import org.thingsboard.server.common.data.alarm.AlarmId;
import org.thingsboard.server.common.data.id.*;

public class EntitiesTenantIdAsyncLoader {

    public static ListenableFuture<TenantId> findEntityIdAsync(TbContext ctx, EntityId original) {

        switch (original.getEntityType()) {
            case TENANT:
                return Futures.immediateFuture((TenantId) original);
            case CUSTOMER:
                return getTenantAsync(ctx.getCustomerService().findCustomerByIdAsync((CustomerId) original));
            case USER:
                return getTenantAsync(ctx.getUserService().findUserByIdAsync((UserId) original));
            case ASSET:
                return getTenantAsync(ctx.getAssetService().findAssetByIdAsync((AssetId) original));
            case DEVICE:
                return getTenantAsync(ctx.getDeviceService().findDeviceByIdAsync((DeviceId) original));
            case ALARM:
                return getTenantAsync(ctx.getAlarmService().findAlarmByIdAsync((AlarmId) original));
            case RULE_CHAIN:
                return getTenantAsync(ctx.getRuleChainService().findRuleChainByIdAsync((RuleChainId) original));
            default:
                return Futures.immediateFailedFuture(new TbNodeException("Unexpected original EntityType " + original));
        }
    }

    private static <T extends HasTenantId> ListenableFuture<TenantId> getTenantAsync(ListenableFuture<T> future) {
        return Futures.transformAsync(future, in -> {
            return in != null ? Futures.immediateFuture(in.getTenantId())
                    : Futures.immediateFuture(null);});
    }
}
