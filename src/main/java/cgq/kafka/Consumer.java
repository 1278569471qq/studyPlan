package cgq.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.Logger;

import java.util.Properties;

/******************************************************
 ****** @ClassName   : Producer.java
 ****** @author      : Mr.Chen
 ****** @date        : 2021.7.24
 ****** @version     : v1.0.x
 *******************************************************/
public class Consumer {

    static Logger log = Logger.getLogger(Producer.class);

    private static final String TOPIC = "milo2";
    private static final String BROKER_LIST = "118.212.149.51:9092";
    private static KafkaConsumer<String,String> consumer = null;

    static {
        Properties configs = initConfig();
        consumer = new KafkaConsumer<String, String>(configs);
    }

    private static Properties initConfig(){
        Properties properties = new Properties();
        properties.put("bootstrap.servers",BROKER_LIST);
        properties.put("group.id","0");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("enable.auto.commit", "true");
        properties.setProperty("auto.offset.reset", "earliest");
        return properties;
    }


    public static void main(String[] args) {
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(10);
            for (ConsumerRecord<String, String> record : records) {
                log.info(record);
            }
        }
    }
}