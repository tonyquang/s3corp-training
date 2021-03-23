package com.proxyserver.kafka.producers;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.log4j.Logger;

import com.proxyserver.constants.IKafkaConstants;

public class ProducerCreator {

	final static Logger LOGGER = Logger.getLogger(ProducerCreator.class);
	
	/**
	 * 
	 * @return
	 */
	private Producer<Long, String> createProducer() {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaConstants.KAFKA_BROKERS);
		props.put(ProducerConfig.CLIENT_ID_CONFIG, IKafkaConstants.CLIENT_ID);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		// props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,
		// CustomPartitioner.class.getName());
		return new KafkaProducer<>(props);
	}

	/**
	 * 
	 * @param msg
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public void runProducer(final String msg) throws ExecutionException, InterruptedException {
		
		Producer<Long, String> producer = this.createProducer();
		ProducerRecord<Long, String> request = new ProducerRecord<Long, String>(IKafkaConstants.TOPIC_NAME, msg);
		RecordMetadata metadata = producer.send(request).get();
		System.out.println("Record sent to partition " + metadata.partition()
		+ " with offset " + metadata.offset());
	}
}