package com.loyalty.analytics.kafka;

import com.loyalty.analytics.kafka.consumers.UsersConsumer;
import io.micronaut.configuration.kafka.serde.SerdeRegistry;
import io.micronaut.configuration.kafka.streams.ConfiguredStreamBuilder;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;

@Factory
public class UserStreams {

    @Inject
    private SerdeRegistry serdeRegistry;

    @Singleton
    public KStream<Long, Void> createdUsers(ConfiguredStreamBuilder builder) {
        Properties props = builder.getConfiguration();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "user-created-streams");
        props.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE);

        KStream<Long, Void> createdUsersStream = builder.stream(UsersConsumer.USER_CREATED_TOPIC, Consumed.with(Serdes.Long(), Serdes.Void()));

        return createdUsersStream;
    }
}
