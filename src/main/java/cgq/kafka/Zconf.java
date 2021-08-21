package cgq.kafka;

import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * @author zhangzhenxin03 <zhangzhenxin03@kuaishou.com>
 * Created on 2021-08-21
 */
public class Zconf {
    static String name = "张小心";
    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            KafkaConsumer<String, String> consumer = Consumer.getConsumer();
            while (true) {
                ConsumerRecords<String, String> poll = consumer.poll(1);
                for (ConsumerRecord<String, String> record : poll) {
                    name = record.value();
                }
            }
        }).start();
        while (true) {
            TimeUnit.SECONDS.sleep(2);
            System.out.println(name);
        }
    }
}
