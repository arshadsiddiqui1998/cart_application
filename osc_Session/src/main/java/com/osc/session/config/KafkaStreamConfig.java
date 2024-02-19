package com.osc.session.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class
KafkaStreamConfig {
	/*

	@Value(value = "${spring.kafka.bootstrap-server}")
	private String bootstrapAddress;

	@Value(value = "${spring.kafka.topic.demo}")
	private String sessionTopic;


	@Autowired
	private KTableProcessor ktableProcessor;

	@Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
	 public KafkaStreamsConfiguration kStreamsConfig() {
	         Map<String, Object> props = new HashMap<>();
	         props.put(APPLICATION_ID_CONFIG, "streams-app");
	         props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
	         props.put(DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
	         props.put(DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
	         props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); 

	         return new KafkaStreamsConfiguration(props);
	  }
	 
	  @Bean
	  public ConsumerFactory<String, String> consumerFactory() {
	         Map<String, Object> props = new HashMap<>();
	         props.put(
	           ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, 
	           bootstrapAddress);
	         props.put(
	           ConsumerConfig.GROUP_ID_CONFIG, 
	           "demo-1");
	         props.put(
	           ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, 
	           StringDeserializer.class);
	         props.put(
	           ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, 
	           StringDeserializer.class);
	         return new DefaultKafkaConsumerFactory<>(props);
	     }

	     @Bean
	     public ConcurrentKafkaListenerContainerFactory<String, String> 
	       kafkaListenerContainerFactory() {
	    
	         ConcurrentKafkaListenerContainerFactory<String, String> factory =
	           new ConcurrentKafkaListenerContainerFactory<>();
	         factory.setConsumerFactory(consumerFactory());
	         return factory;
	     }
	     
	     @Bean
	     public KStream<String, String> kStream(StreamsBuilder kStreamBuilder) {
	      
	         KStream<String, String> stream = kStreamBuilder.stream(sessionTopic, Consumed.with(Serdes.String(), Serdes.String()));
	         //Process KStream
	         
	         stream.foreach((k, v) -> System.out.println("Key = " + k + " value = " + v));
	         
	         //Process KTable
	         this.ktableProcessor.process(stream);
	         
	         return stream;
	     }

*/


}

