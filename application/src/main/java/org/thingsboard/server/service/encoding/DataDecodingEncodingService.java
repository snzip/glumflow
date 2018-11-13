package org.thingsboard.server.service.encoding;

import org.thingsboard.server.common.msg.TbActorMsg;
import org.thingsboard.server.common.msg.cluster.ServerAddress;
import org.thingsboard.server.gen.cluster.ClusterAPIProtos;

import java.util.Optional;

public interface DataDecodingEncodingService {

    Optional<TbActorMsg> decode(byte[] byteArray);

    byte[] encode(TbActorMsg msq);

    ClusterAPIProtos.ClusterMessage convertToProtoDataMessage(ServerAddress serverAddress,
                                                              TbActorMsg msg);

}

