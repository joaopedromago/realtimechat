package Server;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Produtor simples de mensagens para filas
 * 
 * @author fvlaper
 */
public class Produtor {

	public static void main(String[] args) {
		System.out.println("--------> Exemplo JMS: produtor de mensagens de filas");
		// Obtenção do contexto JNDI

		Context jndiContext = null;
		try {
			jndiContext = new InitialContext();
		} catch (NamingException e) {
			System.out.println("--------> Obtenção do contexto inicial falhou: " + e);
			System.exit(-1);
		}

		// Obtenção da fábrica de conexões e da fila de destino
		ConnectionFactory connectionFactory = null;
		Destination destination = null;
		Destination response = null;
		String requestName = "RequestQueue";
		String responseName = "ResponseQueue";
		try {
			connectionFactory = (ConnectionFactory) jndiContext.lookup("queueConnectionFactory");
			destination = (Destination) jndiContext.lookup(requestName);
			response = (Destination) jndiContext.lookup(responseName);
		} catch (NamingException e) {
			System.out.println("--------> Falha no lookup JNDI: " + e);
			System.exit(-1);
		}

		Connection connection = null;
		try {
			// Criação da conexão e da sessão (falso indica que não utiliza transação)
			connection = connectionFactory.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// Criação do produtor de mensagens
			MessageProducer producer = session.createProducer(destination);

			// Criação do consumidor e início da recepção de mensagens.
			MessageConsumer consumer = session.createConsumer(response);

			// Criação e envio de uma mensagem
			int[] vet = { 1, 2, 3, 4, 5 };
			ObjectMessage message = session.createObjectMessage();
			// TextMessage message = session.createTextMessage();
			message.setStringProperty("KEY", "teste");
			message.setObject(vet);
			// message.setText("Mensagem de teste");
			// message.setText("fim");

			// Envio da mensagem
			producer.send(message);

			System.out.println("--------> Mensagem enviada: " + message);

			// Recepção da resposta
			connection.start();
			ObjectMessage resultado = (ObjectMessage) consumer.receive();
			int r = (Integer) resultado.getObject();
			System.out.println("Resultado = " + r);
			connection.stop();
		} catch (JMSException e) {
			System.out.println("Exception occurred: " + e.toString());
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (JMSException e) {
				}
			}
		}
	}

}
