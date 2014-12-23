package ultimate.pong.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ultimate.pong.data.model.Command;
import ultimate.pong.data.model.Match;
import ultimate.pong.data.model.Player;
import ultimate.pong.enums.EnumMatchState;
import ultimate.pong.net.Client;
import ultimate.pong.net.Handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

public abstract class PongHost extends Thread
{
	protected transient final Logger	logger			= LoggerFactory.getLogger(getClass());

	public static final String			DELIM			= "\n\n";

	protected MatchManager				matchManager;

	protected Match						match;

	protected List<ClientAdapter>		clients;

	protected boolean					running;

	protected ObjectMapper				mapper			= new ObjectMapper();
	protected ObjectWriter				writer			= mapper.writer();
	protected ObjectReader				commandReader	= mapper.reader().withType(Command.class);

	public PongHost(MatchManager matchManager, Match match) throws IOException
	{
		super();
		this.matchManager = matchManager;
		this.match = match;
		this.clients = new ArrayList<ClientAdapter>();
	}

	public List<ClientAdapter> getClients()
	{
		return clients;
	}
	
	public void broadcast(final Match match, boolean waitForWriters)
	{
		List<Thread> writers = new LinkedList<Thread>();
		Thread writer;
		synchronized(this.clients)
		{
			for(ClientAdapter cl : this.clients)
			{
				if(cl.client.isConnected())
				{
					final ClientAdapter clFinal = cl;
					writer = new Thread() {
						public void run()
						{
							try
							{
								clFinal.sendMatch(match);
							}
							catch(IOException e)
							{
								logger.error("error sending match to client: " + clFinal.toString(), e);
								try
								{
									clFinal.client.disconnect();
								}
								catch(IOException e1)
								{
									// ignore
								}
							}
						}
					};
					writer.start();
					writers.add(writer);
				}
			}
		}
		
		if(waitForWriters)
		{
			for(Thread w : writers)
			{
				try
				{
					w.join();
				}
				catch(InterruptedException e)
				{
					logger.error("could not join writer", e);
				}
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
					client.listen(new ClientAdapter(client));
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

	public class ClientAdapter implements Handler
	{
		protected Player	player;

		protected Client	client;

		protected int		messagesHandled	= 0;

		public ClientAdapter(Client client)
		{
			super();
			this.client = client;
		}

		public void handleMessage(String message) throws IOException
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

		public void sendMatch(Match match) throws IOException
		{
//			long start, end;
//			start = System.currentTimeMillis();
			for(Player p : match.getPlayers())
			{
				if(this.player == null)
					p.setYou(false);
				else
					p.setYou(p.getId() == this.player.getId());
			}
//			end = System.currentTimeMillis();
//			logger.info("" + (end-start));
			
//			start = System.currentTimeMillis();
			String msg = writer.writeValueAsString(match);
//			end = System.currentTimeMillis();
//			logger.info("" + (end-start));

//			System.out.println(msg);
			
			try
			{
				this.client.sendMessage(msg);
			}
			catch(IOException e)
			{
				
			}
		}

		@Override
		public void setup() throws IOException
		{
			synchronized(PongHost.this.clients)
			{
				PongHost.this.clients.add(this);
			}
		}

		@Override
		public void teardown() throws IOException
		{
			if(this.player != null)
				this.player.setConnected(false);
			
			synchronized(PongHost.this.clients)
			{
				PongHost.this.clients.remove(this);
			}
		}
	}
}
