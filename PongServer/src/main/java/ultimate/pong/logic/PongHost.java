package ultimate.pong.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ultimate.pong.data.model.Command;
import ultimate.pong.data.model.Match;
import ultimate.pong.data.model.Player;
import ultimate.pong.enums.EnumMatchState;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

public abstract class PongHost
{
	protected transient final Logger	logger			= LoggerFactory.getLogger(getClass());

	public static final String			DELIM			= "\n\n";

	protected MatchManager				matchManager;

	protected Match						match;

	protected List<Client>				clients;

	protected ObjectMapper				mapper			= new ObjectMapper();
	protected ObjectWriter				writer			= mapper.writer();
	protected ObjectReader				mapReader		= mapper.reader().withType(new TypeReference<HashMap<String,String>>(){});
	protected ObjectReader				playerReader	= mapper.reader().withType(Player.class);
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
		try
		{
			String message = writer.writeValueAsString(match);
			for(Client c : this.getClients())
			{
				try
				{
					c.sendMessage(message);
				}
				catch(IOException e)
				{
					logger.error("error sending match to client", e);
				}
			}
		}
		catch(IOException e)
		{
			logger.error("error serializing match", e);
		}
	}

	public abstract void startAccepting();

	public abstract void stopAccepting();

	public abstract class Client implements Runnable
	{
		protected Player	player;
		
		protected int messagesHandled = 0;

		public Client()
		{
			super();
			clients.add(this);
		}

		public void listen(Player player)
		{
			this.player = player;
			new Thread() {
				@Override
				public void run()
				{
					
					try
					{
						Client.this.run();
					}
					catch(Exception e)
					{
						logger.error("exception running client", e);
					}

					Client.this.player.setConnected(false);
					clients.remove(Client.this);
					matchManager.tick(match);
				}
			}.start();
		}

		protected void handleMessage(String message) throws IOException
		{
			logger.debug("match state: " + match.getState() + " -> incoming message:\n" + message);
			if(match.getState() == EnumMatchState.waiting)
			{
				// copy update values only if present
				// therefore use a map to check wether given keys are present.
				Map<String, Object> playerUpdateMap = mapReader.readValue(message);
				Player playerUpdate = playerReader.readValue(message);
				boolean update = false;
				if(playerUpdateMap.containsKey("name"))
				{
					this.player.setName(playerUpdate.getName());
					update = true;
				}
				if(playerUpdateMap.containsKey("color"))
				{
					this.player.setColor(playerUpdate.getColor());
					update = true;
				}
				if(playerUpdateMap.containsKey("ready"))
				{
					this.player.setReady(playerUpdate.isReady());
					update = true;
				}
				if(update)
				{
					matchManager.tick(match);
				}
			}
			else if(match.getState() == EnumMatchState.running)
			{
				Command command = commandReader.readValue(message);
				command.setPlayer(this.player);
				matchManager.command(match, command);
			}
			else
			{
				logger.warn("illegal state?!");
			}
			
			messagesHandled++;
			
//			if(messagesHandled % 100 == 0)
//				logger.info(player.getId() + ": " + messagesHandled);
		}

		protected abstract void sendMessage(String message) throws IOException;
	}
}
