package ultimate.pong.logic;

import java.io.IOException;
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

public abstract class PongHost
{
	protected transient final Logger	logger	= LoggerFactory.getLogger(getClass());

	public static final String			DELIM	= "\n\n";

	protected MatchManager				matchManager;

	protected Match						match;

	protected List<Client>				clients;

	protected ObjectMapper				mapper	= new ObjectMapper();
	protected ObjectReader				playerReader	= mapper.reader().withType(Player.class);
	protected ObjectReader				commandReader	= mapper.reader().withType(Command.class);

	public PongHost(MatchManager matchManager, Match match) throws IOException
	{
		super();
		this.matchManager = matchManager;
		this.matchManager.addHost(match, this);
		this.match = match;
		this.clients = new ArrayList<Client>();
	}

	public List<Client> getClients()
	{
		return clients;
	}

	public abstract void startAccepting();

	public abstract void stopAccepting();

	public abstract class Client extends Thread
	{
		protected Player	player;

		public Client(Player player)
		{
			super();
			this.player = player;
			clients.add(this);
		}

		protected void handleMessage(String message) throws IOException
		{
			logger.debug("match state: " + match.getState() + " -> incoming message:\n" + message);
			if(match.getState() == EnumMatchState.waiting)
			{
				Player playerUpdate = playerReader.readValue(message);
				this.player.setName(playerUpdate.getName());
				this.player.setColor(playerUpdate.getColor());
				this.player.setReady(playerUpdate.isReady());
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
		}

		protected abstract void sendMessage(String message) throws IOException;
	}
}
