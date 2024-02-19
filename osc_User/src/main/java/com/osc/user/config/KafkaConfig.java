package com.osc.user.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.TopicBuilder;

import com.osc.user.constants.AppConstants;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@EnableKafkaStreams
public class KafkaConfig {


	@Bean
	public KTable<String, String> otpTable(StreamsBuilder builder) {
		return builder.table(AppConstants.OTP_TOPIC);
	}

		@Bean
	public NewTopic newTopic() {
		return TopicBuilder
				.name(AppConstants.EMAIL_TOPIC_NAME)
//					.partitions(0)
//					.replicas(0)
				.build();
	}

	@Bean
	public NewTopic cacheTopic() {
		return TopicBuilder
				.name(AppConstants.CACHE_STORE_TOPIC)
				//	.partitions(0)
				//	.replicas(0);
				.build();
	}

	@Bean
	public NewTopic sessionTopic() {
		return TopicBuilder
				.name(AppConstants.SESSION_STREAM_TOPIC)
//					.partitions(0)
//					.replicas(0)
				.build();
	}

}
