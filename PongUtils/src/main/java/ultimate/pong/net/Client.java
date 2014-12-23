package ultimate.pong.net;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Client
{
	protected transient final Logger	logger	= LoggerFactory.getLogger(getClass());
	
	protected Handler messageHandler;

	public Client()
	{
		super();
	}

	protected Handler getMessageHandler()
	{
		return messageHandler;
	}

	public void setMessageHandler(Handler messageHandler)
	{
		this.messageHandler = messageHandler;
	}

	public abstract void listen(Handler messageHandler);
	
	public abstract void disconnect() throws IOException;

	public abstract boolean isConnected();

	public abstract void sendMessage(String message) throws IOException;
}
