package io.metadew.iesi.gcp.connection.pubsub;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;

public class PubsubService {

    private static PubsubService INSTANCE;

    public synchronized static PubsubService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PubsubService();
        }
        return INSTANCE;
    }

    private PubsubService () {

    }

    public void createTopic(String projectName, String topicName) {
        Topic topic = new Topic(projectName,topicName);

        try {
            if (!topic.exists()) {
                topic.create();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteTopic(String projectName, String topicName){
        Topic topic = new Topic(projectName,topicName);

        try {
            if (topic.exists()) {
                topic.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
