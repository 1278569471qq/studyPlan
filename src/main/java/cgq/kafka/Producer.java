package cgq.kafka;

import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.log4j.Logger;

/******************************************************
 ****** @ClassName   : Producer.java
 ****** @author      : Mr.Chen
 ****** @date        : 2021.7.24
 ****** @version     : v1.0.x
 *******************************************************/
public class Producer {

    static Logger log = Logger.getLogger(Producer.class);

    private static final String TOPIC = cgq.kafka.KafKaConstant.TOPIC;
    private static final String BROKER_LIST = cgq.kafka.KafKaConstant.BROKER_LIST;
    private static KafkaProducer<String,String> producer = null;

    /*
    初始化生产者
     */
    static {
        Properties configs = initConfig();
        producer = new KafkaProducer<String, String>(configs);
    }

    /*
    初始化配置
     */
    private static Properties initConfig(){
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,BROKER_LIST);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        return properties;
    }

    public static void main(String[] args) throws InterruptedException {
        //消息实体
        ProducerRecord<String , String> record = null;

        while (true) {
            Scanner sc = new Scanner(System.in);
            String next = sc.nextLine();
            if (next.equalsIgnoreCase("exit")) {
                break;
            }
            record = new ProducerRecord<String, String>(TOPIC, next);
            //发送消息
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (null != e){
                        System.out.println(("send error" + e.getMessage()));
                    }else {
                        System.out.println(String.format("offset:%s,partition:%s",recordMetadata.offset(),recordMetadata.partition()));
                    }
                }
            });
        }
        producer.close();
    }
}
