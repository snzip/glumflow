package org.thingsboard.server.http;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Arrays;

@SpringBootApplication
@EnableAsync
@ComponentScan({"org.thingsboard.server.http", "org.thingsboard.server.common", "org.thingsboard.server.transport.http", "org.thingsboard.server.kafka"})
public class ThingsboardHttpTransportApplication {

    private static final String SPRING_CONFIG_NAME_KEY = "--spring.config.name";
    private static final String DEFAULT_SPRING_CONFIG_PARAM = SPRING_CONFIG_NAME_KEY + "=" + "tb-http-transport";

    public static void main(String[] args) {
        SpringApplication.run(ThingsboardHttpTransportApplication.class, updateArguments(args));
    }

    private static String[] updateArguments(String[] args) {
        if (Arrays.stream(args).noneMatch(arg -> arg.startsWith(SPRING_CONFIG_NAME_KEY))) {
            String[] modifiedArgs = new String[args.length + 1];
            System.arraycopy(args, 0, modifiedArgs, 0, args.length);
            modifiedArgs[args.length] = DEFAULT_SPRING_CONFIG_PARAM;
            return modifiedArgs;
        }
        return args;
    }
}
