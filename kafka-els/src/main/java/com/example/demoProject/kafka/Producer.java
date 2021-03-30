package com.example.demoProject.kafka;

import com.example.demoProject.configuration.ProducerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

/**
 * A Kafka producer that sends numEvents (# of messages) to a given topicName
 */
public class Producer {
    private static final Logger logger = LogManager.getLogger(Producer.class);

    /**
     * Applications entry point
     *
     */
    public void inputKafka(String mess){
        String topicName;
        topicName = "network_packet_topic";
        logger.info("Starting HelloProducer...");

        logger.trace("Creating Kafka Producer...");
        Properties props = new ProducerConfig().config();
        KafkaProducer<Integer, String> producer = new KafkaProducer<>(props);

        logger.trace("Start sending messages...");
        try {
            producer.send(new ProducerRecord<>(topicName, 0, mess));
        } catch (KafkaException e) {
            logger.error("Exception occurred - Check log for more details.\n" + e.getMessage());
            System.exit(-1);
        } finally {
            logger.info("Finished HelloProducer - Closing Kafka Producer.");
            producer.close();
        }
    }
}
