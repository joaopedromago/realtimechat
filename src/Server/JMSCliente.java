package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JMSCliente {

	private static final int NUMREQS = 5;
	private static final int MAXTAM = 10;
	private static final int MAXELM = 100;

	private Connection connection = null;
	private Session session = null;
	private MessageProducer producer = null;
	private MessageConsumer consumer = null;

	private String currentMessage = null;

	public void setCurrentMessage(final String string) {
		currentMessage = string;
	}

	private String myCode = null;

	public void setMyCode(final String string) {
		myCode = string;
	}

	private String currentChat = null;

	public void setCurrentChat(final String string) {
		currentChat = string;
	}

	private final ArrayList receives;

	public ArrayList getReceives() {
		return receives;
	}

	// Obtém referências ao TopicConnectionFactory e à fila via JNDI
	TopicConnectionFactory connectionFactory = null;
	Topic topic = null;

	public JMSCliente() {
		receives = new ArrayList();

		// Faz a inicialização das estruturas de mensagens
		System.out.println("JMSCliente: inicialização");

		// Obtenção do contexto JNDI
		Context jndiContext = null;
		try {
			jndiContext = new InitialContext();
		} catch (final NamingException e) {
			System.out.println("JMSCliente: obtenção do contexto inicial falhou: " + e);
			System.exit(-1);
		}

		// Obtenção da fábrica de conexões e das filas de mensagens
		ConnectionFactory connectionFactory = null;
		Destination request = null;
		Destination response = null;
		final String requestName = "RequestQueue";
		final String responseName = "ResponseQueue";
		try {
			connectionFactory = (ConnectionFactory) jndiContext.lookup("queueConnectionFactory");
			request = (Destination) jndiContext.lookup(requestName);
			response = (Destination) jndiContext.lookup(responseName);
		} catch (final NamingException e) {
			System.out.println("JMSCliente: falha no lookup JNDI: " + e);
			System.exit(-1);
		}

		try {
			// Criação da conexão e da sessão (falso indica que não utiliza transação)
			connection = connectionFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// Criação do produtor de mensagens
			producer = session.createProducer(request);

			// Criação do consumidor e início da recepção de mensagens.
			consumer = session.createConsumer(response);

			System.out.println("JMSCliente: inicialização concluída");
		} catch (final JMSException e) {
			System.out.println("JMSCliente: exceção capturada: " + e.toString());
		}

		// startMessages();
	}

	public void startMessages() {
		try {
			// recebe mensagens
			final TopicConnection connection = connectionFactory.createTopicConnection();
			final TopicSession session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			final TopicSubscriber subscriber = session.createSubscriber(topic);
			connection.start();

			while (true) {
				if (currentMessage != null) {
					sendMessage(currentMessage, myCode);
					currentMessage = null;
				}

				final TextMessage message = (TextMessage) subscriber.receive();
				if (message != null) {
					final String[] vet = message.getText().split("#@#");

					// Valida se é pra mim mesmo a mensagem
					if (vet[1] == currentChat || currentChat == null) {
						receives.add(vet[1]);
					}
				}
			}
		} catch (final JMSException e) {
			e.printStackTrace();
			System.out.println("JMSCliente: exceção capturada: " + e.toString());
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (final JMSException e) {
				}
			}
		}
	}

	private void sendMessage(final String messageText, final String code) throws JMSException {
		ObjectMessage message;

		// Montando a mensagem
		final String[] vet = new String[2];
		vet[0] = messageText;
		vet[1] = code;

		// Envio da mensagem
		message = session.createObjectMessage();
		message.setObject(vet);
		producer.send(message);
	}
}
