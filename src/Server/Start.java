package Server;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.InitialContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

public class Start implements javax.jms.MessageListener {
    public static final String TOPIC = "topic/RealTimeChatTopic";

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            throw new Exception("Favor informar o seu nome como argumento.");
        }

        String username = args[0];
        Start server = new Start();
        Context initialContext = Start.getInitialContext();
        Topic topic = (Topic) initialContext.lookup(TOPIC);
        TopicConnectionFactory connectionFactory = (TopicConnectionFactory) initialContext.lookup("ConnectionFactory");
        TopicConnection connection = (TopicConnection) connectionFactory.createConnection();
        server.subscribe(connection, topic, server);
        server.publish(connection, topic, username);
    }

    public static Context getInitialContext() throws JMSException, NamingException {
        Properties props = new Properties();
        props.setProperty("java.naming.factory.initual", "org.jnp.interfaces.NamingContextFactory");
        props.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming");
        props.setProperty("java.naming.provider.url", "localhost:1099");
        Context context = new InitialContext(props);
        return context;
    }

    public void publish(TopicConnection connection, Topic topic, String username) throws JMSException, IOException {
        TopicSession session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        TopicPublisher publisher = session.createPublisher(topic);
        connection.start();

        BufferedReader reader = new java.io.BufferedReader(new InputStreamReader(System.in));

        while (true) {
            String messageToSend = reader.readLine();
            // connection.close();
            // System.exit(0);

            TextMessage message = session.createTextMessage();
            message.setText(username + ": " + messageToSend);
            publisher.publish(message);
        }
    }

    public void subscribe(TopicConnection connection, Topic topic, Start server) throws JMSException {
        TopicSession session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        TopicSubscriber subscriber = session.createSubscriber(topic);
        subscriber.setMessageListener(server);
    }

    public void onMessage(Message message) {
        try {
            String messageText = ((TextMessage) message).getText();
            System.out.println(messageText);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
