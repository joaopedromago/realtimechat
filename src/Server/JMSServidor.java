package Server;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JMSServidor {

	// Classe de tratamento das mensagens
	private class RecebeMensagem implements MessageListener {

		private MessageProducer producer = null;
		private QueueSession session = null;
		TopicConnectionFactory connectionFactory = null;
		Topic topic = null;

		public RecebeMensagem(MessageProducer producer, QueueSession session) {
			this.producer = producer;
			this.session = session;
		}

		public void onMessage(Message message) {

			try {
				// Nova mensagem recebida
				if (message == null)
					System.out.println("JMSServidor: recebida mensagem nula");
				else if (message instanceof ObjectMessage) {
					// Obtém o conteúdo da mensagem (vetor de inteiros)
					ObjectMessage m = (ObjectMessage) message;
					String[] vet = (String[]) m.getObject();
					exibe(vet);

					try {
						TopicConnection connection = connectionFactory.createTopicConnection();
						TopicSession session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

						System.out.println("Enviando a mensagem de: " + vet[1]);
						// Publica uma única mensagem
						TopicPublisher publisher = session.createPublisher(topic);
						TextMessage messageTopic = session.createTextMessage();
						messageTopic.setText(vet[0] + "#@#" + vet[1]);
						publisher.publish(messageTopic);
						System.out.println("Mensagem publicada: " + messageTopic);

						// libera recursos
						publisher.close();
						session.close();
						connection.close();
					} catch (JMSException e) {
						e.printStackTrace();
					}

				} else
					System.out.println("JMSServidor: recebida mensagem de tipo inesperado: " + message);
			} catch (JMSException e) {
				System.out.println("JMSServidor: recebida exceção: " + e);
			}
		}

		private void exibe(String[] v) {
			System.out.print("JMSServidor: recebido: ");
			for (String e : v)
				System.out.print(e + " ");
			System.out.println();
		}
	}

	public JMSServidor() {
		System.out.println("JMSServidor: inicialização");

		// Obtenção do contexto JNDI
		Context jndiContext = null;
		try {
			jndiContext = new InitialContext();
		} catch (NamingException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		// Obtenção da fábrica de conexões e das filas de mensagens
		String requestName = "RequestQueue";
		String responseName = "ResponseQueue";
		QueueConnectionFactory connectionFactory = null;
		Queue request = null;
		Queue response = null;
		try {
			connectionFactory = (QueueConnectionFactory) jndiContext.lookup("queueConnectionFactory");
			request = (Queue) jndiContext.lookup(requestName);
			response = (Queue) jndiContext.lookup(responseName);
		} catch (NamingException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		// Criação da conexão e da sessão (falso indica que não usa transaçao)
		QueueConnection connection = null;
		try {
			connection = connectionFactory.createQueueConnection();
			QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

			// Criação do consumidor, do produtor de respostas e início da recepção de
			// mensagens.
			QueueReceiver receiver = session.createReceiver(request);
			MessageProducer producer = session.createProducer(response);
			receiver.setMessageListener(new RecebeMensagem(producer, session));
			connection.start();
		} catch (JMSException e) {
			e.printStackTrace();
		}

		System.out.println("JMSServidor: inicialização concluída");
	}

	public static void main(String[] args) {
		new JMSServidor();

		int i = 0;
		while (true) {
			System.out.println("JMSServidor: servidor executando " + ++i);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}

}
