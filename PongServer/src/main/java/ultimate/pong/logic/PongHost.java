package ultimate.pong.logic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ultimate.pong.data.model.Command;
import ultimate.pong.data.model.Match;
import ultimate.pong.data.model.Player;
import ultimate.pong.enums.EnumMatchState;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

public abstract class PongHost extends Thread
{
	protected transient final Logger	logger			= LoggerFactory.getLogger(getClass());

	public static final String			DELIM			= "\n\n";

	protected MatchManager				matchManager;

	protected Match						match;

	protected List<Client>				clients;

	protected boolean					running;

	protected ObjectMapper				mapper			= new ObjectMapper();
	protected ObjectWriter				writer			= mapper.writer();
	protected ObjectReader				commandReader	= mapper.reader().withType(Command.class);

	public PongHost(MatchManager matchManager, Match match) throws IOException
	{
		super();
		this.matchManager = matchManager;
		this.match = match;
		this.clients = new ArrayList<Client>();
	}

	public List<Client> getClients()
	{
		return clients;
	}

	public void broadcast(Match match)
	{
		for(Client c : this.getClients())
		{
			try
			{
				c.sendMatch(match);
			}
			catch(IOException e)
			{
				logger.error("error sending match to client", e);
			}
		}
	}

	public synchronized void startAccepting()
	{
		this.running = true;
		super.start();
	}
	
	public synchronized void stopAccepting()
	{
		this.running = false;
		try
		{
			this.close();
		}
		catch(IOException e)
		{
			logger.error("could not stop host", e);
		}
	}

	public void run()
	{
		Client client;
		try
		{
			while(this.running)
			{
				try
				{
					client = accept();
					client.listen();
				}
				catch(IOException e)
				{
					logger.error("error accepting client", e);
				}
			}
		}
		catch(Exception e)
		{
			logger.error("error accepting clients", e);
		}
		finally
		{
			try
			{
				this.close();
			}
			catch(IOException e)
			{
				logger.error("error closing host", e);
			}
		}
	}

	public abstract Client accept() throws IOException;

	public abstract void close() throws IOException;

	public abstract class Client
	{
		protected Player	player;

		protected int		messagesHandled	= 0;

		public Client()
		{
			super();
		}

		protected abstract void listen();

		protected abstract boolean isConnected();

		protected abstract String readNextMessage() throws IOException;

		protected void handleMessage(String message) throws IOException
		{
			logger.debug("match state: " + match.getState() + " -> incoming message:\n" + message);

			if(this.player == null)
			{
				if(match.getState() == EnumMatchState.waiting)
				{
					// add player
					this.player = matchManager.addPlayer(match, "unnamed");
				}
				else
				{
					// match already started -> ignore message
					return;
				}
			}

			Command command = commandReader.readValue(message);
			command.setPlayer(this.player);
			matchManager.command(match, command);

			messagesHandled++;

			// if(messagesHandled % 100 == 0)
			// logger.info(player.getId() + ": " + messagesHandled);
		}

		protected abstract void sendMessage(String message) throws IOException;

		protected void sendMatch(Match match) throws IOException
		{
			for(Player p : match.getPlayers())
			{
				if(this.player == null)
					p.setYou(false);
				else
					p.setYou(p.getId() == this.player.getId());
			}
		}
	}

	public abstract class ActiveClient extends Client implements Runnable
	{
		@Override
		protected void listen()
		{
			new Thread(this).start();
		}

		@Override
		public void run()
		{
			clients.add(this);

			try
			{
				String message;
				while(isConnected())
				{
					message = readNextMessage();
					if(message != null)
						handleMessage(message);
					else
						break; // cancel if message is null
				}
			}
			catch(Exception e)
			{
				logger.error("exception running client", e);
			}

			if(this.player != null)
				this.player.setConnected(false);

			clients.remove(this);
		}
	}

	public abstract class StreamClient extends ActiveClient
	{
		protected abstract InputStream getInputStream() throws IOException;

		protected abstract OutputStream getOutputStream() throws IOException;

		@Override
		protected String readNextMessage() throws IOException
		{
			try
			{
				StringBuffer sb = new StringBuffer();
				int c;
				while(true)
				{
					c = getInputStream().read();
					if(c == -1)
						break;

					sb.append((char) c);

					if(sb.length() > DELIM.length())
					{
						boolean delimFound = true;
						for(int di = 0; di < DELIM.length(); di++)
						{

							if(sb.charAt(sb.length() - DELIM.length() + di) != DELIM.charAt(di))
							{
								delimFound = false;
								break;
							}
						}
						if(delimFound)
							return sb.toString();
					}
				}
			}
			catch(Exception e)
			{
				logger.error("error reading message (connected? "+ isConnected() + ")", e);
			}
			return null;
		}

		@Override
		protected void sendMessage(String message) throws IOException
		{
			this.getOutputStream().write(message.getBytes());
			this.getOutputStream().write(DELIM.getBytes());
			this.getOutputStream().flush();
		}
	}
}
