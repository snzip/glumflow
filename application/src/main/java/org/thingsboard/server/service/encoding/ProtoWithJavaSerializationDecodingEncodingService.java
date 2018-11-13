package org.thingsboard.server.service.encoding;

import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;
import org.thingsboard.server.common.msg.TbActorMsg;
import org.thingsboard.server.common.msg.cluster.ServerAddress;
import org.thingsboard.server.gen.cluster.ClusterAPIProtos;

import java.util.Optional;

import static org.thingsboard.server.gen.cluster.ClusterAPIProtos.MessageType.CLUSTER_ACTOR_MESSAGE;


@Slf4j
@Service
public class ProtoWithJavaSerializationDecodingEncodingService implements DataDecodingEncodingService {


    @Override
    public Optional<TbActorMsg> decode(byte[] byteArray) {
        try {
            TbActorMsg msg = (TbActorMsg) SerializationUtils.deserialize(byteArray);
            return Optional.of(msg);

        } catch (IllegalArgumentException e) {
            log.error("Error during deserialization message, [{}]", e.getMessage());
           return Optional.empty();
        }
    }

    @Override
    public byte[] encode(TbActorMsg msq) {
        return SerializationUtils.serialize(msq);
    }

    @Override
    public ClusterAPIProtos.ClusterMessage convertToProtoDataMessage(ServerAddress serverAddress,
                                                                     TbActorMsg msg) {
        return ClusterAPIProtos.ClusterMessage
                .newBuilder()
                .setServerAddress(ClusterAPIProtos.ServerAddress
                        .newBuilder()
                        .setHost(serverAddress.getHost())
                        .setPort(serverAddress.getPort())
                        .build())
                .setMessageType(CLUSTER_ACTOR_MESSAGE)
                .setPayload(ByteString.copyFrom(encode(msg))).build();

    }
}
