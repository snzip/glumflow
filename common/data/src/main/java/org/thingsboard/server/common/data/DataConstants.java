package org.thingsboard.server.common.data;

/**
 * @author Andrew Shvayka
 */
public class DataConstants {

    public static final String SYSTEM = "SYSTEM";
    public static final String TENANT = "TENANT";
    public static final String CUSTOMER = "CUSTOMER";
    public static final String DEVICE = "DEVICE";

    public static final String CLIENT_SCOPE = "CLIENT_SCOPE";
    public static final String SERVER_SCOPE = "SERVER_SCOPE";
    public static final String SHARED_SCOPE = "SHARED_SCOPE";

    public static final String[] allScopes() {
        return new String[]{CLIENT_SCOPE, SHARED_SCOPE, SERVER_SCOPE};
    }

    public static final String ALARM = "ALARM";
    public static final String ERROR = "ERROR";
    public static final String LC_EVENT = "LC_EVENT";
    public static final String STATS = "STATS";
    public static final String DEBUG_RULE_NODE = "DEBUG_RULE_NODE";

    public static final String ONEWAY = "ONEWAY";
    public static final String TWOWAY = "TWOWAY";

    public static final String IN = "IN";
    public static final String OUT = "OUT";

    public static final String INACTIVITY_EVENT = "INACTIVITY_EVENT";
    public static final String CONNECT_EVENT = "CONNECT_EVENT";
    public static final String DISCONNECT_EVENT = "DISCONNECT_EVENT";
    public static final String ACTIVITY_EVENT = "ACTIVITY_EVENT";

    public static final String ENTITY_CREATED = "ENTITY_CREATED";
    public static final String ENTITY_UPDATED = "ENTITY_UPDATED";
    public static final String ENTITY_DELETED = "ENTITY_DELETED";
    public static final String ENTITY_ASSIGNED = "ENTITY_ASSIGNED";
    public static final String ENTITY_UNASSIGNED = "ENTITY_UNASSIGNED";
    public static final String ATTRIBUTES_UPDATED = "ATTRIBUTES_UPDATED";
    public static final String ATTRIBUTES_DELETED = "ATTRIBUTES_DELETED";
    public static final String ALARM_ACK = "ALARM_ACK";
    public static final String ALARM_CLEAR = "ALARM_CLEAR";

    public static final String RPC_CALL_FROM_SERVER_TO_DEVICE = "RPC_CALL_FROM_SERVER_TO_DEVICE";

}
