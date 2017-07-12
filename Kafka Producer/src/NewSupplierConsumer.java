import java.util.*;
import java.io.*;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class NewSupplierConsumer{

	public static void main(String[] args) throws Exception{

		String topicName = "SimpleProducerTopic";
		String groupName = "SupplierTopicGroup";
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092,localhost:9093");
		props.put("group.id", groupName);
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

		InputStream input = null;
		KafkaConsumer<String, String> consumer = null;

		try {
			consumer = new KafkaConsumer<String,String>(props);
			consumer.subscribe(Arrays.asList(topicName));

			while (true){
				ConsumerRecords<String, String> records = consumer.poll(100);
				for (ConsumerRecord<String, String> record : records){
					System.out.println("Supplier id= " + String.valueOf(record.value()));
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			consumer.close();
		}
	}
}	