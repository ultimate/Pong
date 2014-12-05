package ultimate.pong.logic.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ultimate.pong.data.model.Color;
import ultimate.pong.data.model.Command;
import ultimate.pong.data.model.Match;
import ultimate.pong.data.model.Player;
import ultimate.pong.enums.EnumMatchState;
import ultimate.pong.logic.MatchManager;
import ultimate.pong.logic.PongHost;

public class MatchManagerImpl implements MatchManager
{
	protected transient final Logger	logger	= LoggerFactory.getLogger(getClass());
	
	protected List<Match>						matches			= new ArrayList<Match>();
	protected HashMap<Match, List<PongHost>>	hosts			= new HashMap<Match, List<PongHost>>();

	protected int								matchIdCounter;
	protected int								playerIdCounter;
	protected Random							random = new Random();

	protected int								interval	= 100;

	public int getInterval()
	{
		return interval;
	}

	public void setInterval(int interval)
	{
		if(interval <= 0)
			throw new IllegalArgumentException("interval must be > 0");
		this.interval = interval;
	}

	@Override
	public List<Match> getMatches()
	{
		return this.matches;
	}

	@Override
	public synchronized void tick(Match match)
	{
		logger.info("match tick #" + match.getTick());
		switch(match.getState())
		{
			case waiting:
				// TODO waiting
				break;

			case running:
				// TODO running
				break;

			case finished:

				break;

			default:
				break;
		}
	}

	@Override
	public synchronized Match createMatch(String name)
	{
		logger.info("creating match: '" + name + "'");
		Match match = new Match();
		match.setId(++matchIdCounter);
		match.setName(name);
		this.matches.add(match);
		
		// debug
//		addPlayer(match, "foo");
//		addPlayer(match, "bar");		
		
		return match;
	}

	@Override
	public void deleteMatch(Match match)
	{
		this.matches.remove(match);
		this.hosts.remove(match);
	}

	@Override
	public synchronized void addHost(Match match, PongHost host)
	{
		if(!this.hosts.containsKey(match))
			this.hosts.put(match, new ArrayList<PongHost>());
		this.hosts.get(match).add(host);
	}

	@Override
	public boolean isReady(Match match)
	{
		int ready = 0;
		for(Player player : match.getPlayers())
		{
			if(player.isReady())
				ready++;
		}
		return ready > 1 && ready == match.getPlayers().size();
	}

	@Override
	public boolean start(Match match)
	{
		if(!isReady(match))
			return false;
		
		logger.info("starting match: '" + match.getName() + "'");

		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public synchronized Player addPlayer(Match match, String name)
	{
		if(match.getState() != EnumMatchState.waiting)
			throw new IllegalStateException("match already started");
		
		logger.info("adding player to match '" + match.getName() + "': '" + name + "'");

		Player player = new Player();
		player.setId(++playerIdCounter);
		player.setName(name);
		player.setColor(randomColor());
		player.setSliderPosition(0.5);
		
		match.getPlayers().add(player);

		rebuild(match); // for new player count

		return player;
	}

	@Override
	public synchronized void command(Match match, Command command)
	{
		int nextTick = match.getTick() + 1;
		List<Command> playerCommands = command.getPlayer().getCommands();
		if(playerCommands.get(playerCommands.size() - 1).getTick() == nextTick)
			// command already sent for this tick -> remove for override
			command.getPlayer().getCommands().remove(playerCommands.size() - 1);
		command.setTick(nextTick);
		playerCommands.add(command);
	}

	protected void rebuild(Match match)
	{
		logger.debug("updating match map");
		// TODO updated map

		logger.debug("updating player sliders");
		// TODO update sliders
	}

	protected Color randomColor()
	{
		return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
	}

	protected class Ticker extends Thread
	{
		protected Match	match;

		public Ticker(Match match)
		{
			super();
			this.match = match;
		}

		public void run()
		{
			long start, end;
			while(match.getState() != EnumMatchState.finished)
			{
				start = System.currentTimeMillis();
				tick(match);
				end = System.currentTimeMillis();

				try
				{
					Thread.sleep(interval - (end-start));
				}
				catch(InterruptedException e)
				{
					logger.error("could not sleep", e);
				}
			}
		}
	}
}
