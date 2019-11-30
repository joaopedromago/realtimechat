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
 * Consumidor síncrono de mensagens de filas
 * 
 * @author fvlaper
 */
public class SincConsumidor {

	public static void main(String[] args) {
		System.out.println("Exemplo JMS: consumidor síncrono");

		// Obtenção do contexto JNDI
		Context jndiContext = null;
		try {
			jndiContext = new InitialContext();
		} catch (NamingException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		// Obtenção da fábrica de conexões e das filas de mensagens
		String sourceName = "RequestQueue";
		String destName = "ResponseQueue";
		ConnectionFactory connectionFactory = null;
		Destination request = null;
		Destination response = null;
		try {
			connectionFactory = (ConnectionFactory) jndiContext.lookup("queueConnectionFactory");
			request = (Destination) jndiContext.lookup(sourceName);
			response = (Destination) jndiContext.lookup(destName);
		} catch (NamingException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		// Criação da conexão e da sessão (falso indica que não usa transação)
		Connection connection = null;
		try {
			connection = connectionFactory.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// String selector = "KEY = 'teste'";

			// Criação do produtor de mensagens
			MessageProducer producer = session.createProducer(response);

			// Criação do consumidor e início da recepção de mensagens.
			MessageConsumer consumer = session.createConsumer(request);
			// MessageConsumer consumer = session.createConsumer(destination,selector);

			connection.start();
			while (true) {
				System.out.println("Aguardando mensagens");
				ObjectMessage message = (ObjectMessage) consumer.receive();
				// TextMessage message = (TextMessage)consumer.receive();
				int[] vet = (int[]) message.getObject();
				// System.out.println("Recebido: " + message.getText());
				exibe(vet);
				// Envio da resposta
				int resp = soma(vet);
				ObjectMessage resposta = session.createObjectMessage();
				resposta.setObject(resp);
				producer.send(resposta);
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	private static void exibe(int[] v) {
		System.out.print("Recebido: ");
		for (int e : v)
			System.out.print(e + " ");
		System.out.println();
	}

	private static int soma(int[] vet) {
		int resultado = 0;
		for (int e : vet)
			resultado += e;
		return resultado;
	}
}
