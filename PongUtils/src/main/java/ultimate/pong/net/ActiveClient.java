package ultimate.pong.net;

import java.io.IOException;

public abstract class ActiveClient extends Client implements Runnable
{
	public ActiveClient()
	{
		super();
	}

	@Override
	public void listen(Handler messageHandler)
	{
		setMessageHandler(messageHandler);
		new Thread(this).start();
	}
	
	protected abstract String readNextMessage() throws IOException;
	
	@Override
	public void run()
	{
		try
		{
			this.messageHandler.setup();
		}
		catch(IOException e)
		{
			logger.error("could not run setup", e);
			return;
		}

		try
		{
			String message;
			while(isConnected())
			{
				message = readNextMessage();
				if(message != null)
					this.messageHandler.handleMessage(message);
				else
					break; // cancel if message is null
			}
		}
		catch(Exception e)
		{
			logger.error("exception running client", e);
		}

		try
		{
			this.messageHandler.teardown();
		}
		catch(IOException e)
		{
			logger.warn("could not run teardown", e);
		}
	}
}
