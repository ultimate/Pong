package ultimate.pong.logic.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ultimate.pong.data.model.Color;
import ultimate.pong.data.model.Command;
import ultimate.pong.data.model.Map;
import ultimate.pong.data.model.MapObject;
import ultimate.pong.data.model.Match;
import ultimate.pong.data.model.Player;
import ultimate.pong.data.model.objects.Ball;
import ultimate.pong.data.model.objects.Slider;
import ultimate.pong.data.model.objects.Wall;
import ultimate.pong.enums.EnumMatchState;
import ultimate.pong.logic.MatchManager;
import ultimate.pong.logic.PongHost;
import ultimate.pong.math.Geometry;
import ultimate.pong.math.Physics;
import ultimate.pong.math.Vector;
import ultimate.pong.net.PongSocketHost;

public class MatchManagerImpl implements MatchManager
{
	protected transient final Logger			logger						= LoggerFactory.getLogger(getClass());

	public static final int						DEFAULT_INTERVAL			= 10;
	public static final int						DEFAULT_MAX_SCORE			= 10;
	public static final double					DEFAULT_SLIDER_SIZE			= 0.1;
	public static final double					DEFAULT_BALL_RELEASE_SPEED	= 0.002;

	protected List<Match>						matches						= new ArrayList<Match>();
	protected HashMap<Integer, List<PongHost>>	hosts						= new HashMap<Integer, List<PongHost>>();
	protected List<Ticker>						tickers						= new ArrayList<Ticker>();

	protected int								objectIdCounter;
	protected int								matchIdCounter;
	protected int								playerIdCounter;
	protected Random							random						= new Random();

	// configurable values
	protected int								interval;
	protected int								maxScore;
	protected double							sliderSize;
	protected double							ballReleaseSpeed;
	
	// calculated values
	protected double							maxSliderPositionChange; 

	public MatchManagerImpl()
	{
		setInterval(DEFAULT_INTERVAL);
		setMaxScore(DEFAULT_MAX_SCORE);
		setSliderSize(DEFAULT_SLIDER_SIZE);
		setBallReleaseSpeed(DEFAULT_BALL_RELEASE_SPEED);
	}

	public int getInterval()
	{
		return interval;
	}

	public void setInterval(int interval)
	{
		if(interval <= 0)
			throw new IllegalArgumentException("interval must be > 0");
		this.interval = interval;
		// minimum of 100ms for full range
		this.maxSliderPositionChange = 1.0 / (100.0 / interval);
	}

	public int getMaxScore()
	{
		return maxScore;
	}

	public void setMaxScore(int maxScore)
	{
		this.maxScore = maxScore;
	}

	public double getSliderSize()
	{
		return sliderSize;
	}

	public void setSliderSize(double sliderSize)
	{
		this.sliderSize = sliderSize;
	}

	public double getBallReleaseSpeed()
	{
		return ballReleaseSpeed;
	}

	public void setBallReleaseSpeed(double ballReleaseSpeed)
	{
		this.ballReleaseSpeed = ballReleaseSpeed;
	}

	@Override
	public List<Match> getMatches()
	{
		return this.matches;
	}

	@Override
	public synchronized void tick(Match match)
	{
		logger.debug("match tick #" + match.getTick());

		// update tick
		match.setTick(match.getTick() + 1);
		
		Command c;
		Player p;

		// check the number of connected players
		int connected = checkPlayer(match);
		

		switch(match.getState())
		{
			case waiting:
				
				// check if match is ready and start it
				if(isReady(match))
					start(match);
				
			case finished:
			case deleted:
				
				// moving slider is always possible
				for(int i = 0; i < match.getPlayers().size(); i++)
				{
					p = match.getPlayers().get(i);
					c = getCommand(p, match.getTick());
					applySliderCommand(match, c, i);
				}
				
				break;
				
			case running:
				
				// move slider and release ball
				for(int i = 0; i < match.getPlayers().size(); i++)
				{
					p = match.getPlayers().get(i);
					c = getCommand(p, match.getTick());
					applySliderCommand(match, c, i);
					applyReleaseCommand(match, c, i);
				}

				// handle map objects
				List<MapObject> objects = match.getMap().getObjects();

				// clear collisions
				for(MapObject obj : objects)
				{
					obj.getCollisions().clear();
				}
				
				List<MapObject> objectsToRemove = new LinkedList<MapObject>();

				for(MapObject obj : objects)
				{
					if(obj instanceof Ball)
					{
						Ball ball = (Ball) obj;

						// consider interaction between objects
						Physics.interact(ball, objects);

						// update color (use color of player with last contact)
						ball.setColor(ball.getLastContact().getColor());

						// check if ball is out of the arena (R > 1)
						if(!checkBall(match, ball))
						{
							objectsToRemove.add(ball);
						}
						
						logger.debug("ball @ " + ball.getPosition());
					}
				}
				
				objects.removeAll(objectsToRemove);
				
				// check if there are at least 2 active players
				if(match.getState() == EnumMatchState.running && connected < 2)
					match.setState(EnumMatchState.finished);
		}

		List<PongHost> matchHosts = this.hosts.get(match.getId());
		for(PongHost host : matchHosts)
		{
			host.broadcast(match);
		}
	}
	
	protected int checkPlayer(Match match)
	{
		int connected = 0;
		// check disconnected players
		List<Player> disconnectedPlayers = new ArrayList<Player>();
		for(Player p : match.getPlayers())
		{
			if(p.isConnected())
			{
				connected++;
			}
			else
			{
				// update slider to act as a wall
				p.setSliderPosition(0.5);
				p.setSliderSize(1.0);
				disconnectedPlayers.add(p);
			}
		}
		if(match.getState() == EnumMatchState.waiting && disconnectedPlayers.size() > 0)
		{
			match.getPlayers().removeAll(disconnectedPlayers);
			rebuild(match);
		}
		return connected;
	}
	
	protected void applySliderCommand(Match match, Command command, int index)
	{
		if(command == null)
			return;
		

		Player p = command.getPlayer();
		// get min/max valid position
		double minValid = p.getSliderPosition() - maxSliderPositionChange;
		double maxValid = p.getSliderPosition() + maxSliderPositionChange;

		double old = p.getSliderPosition();
		
		// move slider
		p.setSliderPosition(command.getSliderPosition());
		// validate new position
		if(p.getSliderPosition() < minValid)
			p.setSliderPosition(minValid);
		else if(p.getSliderPosition() > maxValid)
			p.setSliderPosition(maxValid);
		
		logger.debug(index + " @ " + old + " -> " + p.getSliderPosition());

		// recalculate slider
		updateSlider(p, match.getPlayers().size(), index);
	}
	
	protected void applyReleaseCommand(Match match, Command command, int index)
	{
		if(command == null)
			return;
		
		int corners = match.getPlayers().size() * 2;
		Player p = command.getPlayer();
		// release ball
		if(command.getRelease() == true && p.isBall())
		{
			logger.info("ball released by player " + index + " '" + p.getName() + "'");
			Ball ball = new Ball();
			ball.setId(++objectIdCounter);
			ball.setName("ball");
			ball.setPosition(Geometry.center(p.getSlider().getStart(), p.getSlider().getEnd()));
			// get direction from players slider range center in order to start
			// orthogonal
			Vector playerCenter = Geometry.getCenterPoint(corners, index * 2);
			Vector direction = new Vector(playerCenter).scale(- ballReleaseSpeed);
			logger.debug("center=" + playerCenter);
			logger.debug("dir=" + direction);
			ball.setDirection(direction);
			// last contact is releasing player (use player color)
			ball.setLastContact(p);
			// reset ball state for player
			p.setBall(false);
			
			match.getMap().getObjects().add(ball);
		}
	}
	
	/**
	 * 
	 * @param match
	 * @param ball
	 * @return ball valid
	 */
	protected boolean checkBall(Match match, Ball ball)
	{
		int corners = match.getPlayers().size() * 2;
		
		// check if ball is out of the arena (R > 1)
		double ballRadius = Geometry.distanceFromCenter(ball.getPosition());
		if(ballRadius > 1.1)
		{
			int targetPlayerIndex = Geometry.getPlayerSectorIndex(corners, ball.getPosition());
			Player targetPlayer = match.getPlayers().get(targetPlayerIndex);
			// update score
			if(targetPlayer != ball.getLastContact())
				ball.getLastContact().increaseScore(1);
			else
				ball.getLastContact().decreaseScore(1);
			// assign ball
			targetPlayer.setBall(true);
			
			return false;
		}
		
		return true;
	}
	

	@Override
	public synchronized Match createMatch(String name, int port)
	{
		logger.info("creating match: '" + name + "'");
		Match match = new Match();
		match.setId(++matchIdCounter);
		match.setName(name);
		match.setMap(new Map());
		match.getMap().setColor(Color.BLACK);

		this.matches.add(match);

		this.hosts.put(match.getId(), new ArrayList<PongHost>());

		try
		{
			this.addHost(match, new PongSocketHost(this, match, port));
			match.setPort(port);
		}
		catch(IOException e)
		{
			logger.error("could not create host");
			this.deleteMatch(match);
			match = null;
		}

		// immediately start ticking
		Ticker ticker = new Ticker(match);
		ticker.start();

		this.tickers.add(ticker);

		return match;
	}

	@Override
	public synchronized void deleteMatch(Match match)
	{
		// set deleted
		match.setState(EnumMatchState.running);
		
		// stop accepting clients
		List<PongHost> matchHosts = this.hosts.get(match.getId());
		for(PongHost host : matchHosts)
		{
			host.stopAccepting();
		}
		this.hosts.remove(match.getId());
		
		this.matches.remove(match);
		
		// ticker will stop automatically
	}

	@Override
	public synchronized void addHost(Match match, PongHost host)
	{
		this.hosts.get(match.getId()).add(host);
		host.startAccepting();
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
		logger.debug("players ready: " + ready + " of " + match.getPlayers().size());
		return ready > 1 && ready == match.getPlayers().size();
	}

	@Override
	public synchronized boolean start(Match match)
	{
		logger.debug("attempt to start match: '" + match.getName() + "'");

		if(!isReady(match))
			return false;

		logger.info("starting match: '" + match.getName() + "'");

		// set running
		match.setState(EnumMatchState.running);

		// assign ball
		Player randomPlayer = match.getPlayers().get(random.nextInt(match.getPlayers().size()));
		randomPlayer.setBall(true);

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
		player.setSliderSize(sliderSize);
		player.setConnected(true);

		match.getPlayers().add(player);

		rebuild(match); // for new player count

		return player;
	}

	@Override
	public synchronized void command(Match match, Command command)
	{
		logger.debug("player command: " + command.getPlayer().getId() + " pos=" + command.getSliderPosition());

		// apply changes to player
		if(command.getPlayerColor() != null)
			command.getPlayer().setColor(command.getPlayerColor());
		if(command.getPlayerName() != null)
			command.getPlayer().setName(command.getPlayerName());
		if(command.getPlayerReady() != null)
			command.getPlayer().setReady(command.getPlayerReady());

		int nextTick = match.getTick() + 1;
		List<Command> playerCommands = command.getPlayer().getCommands();
		Command next = getCommand(command.getPlayer(), nextTick);
		if(next != null)
			// command already sent for next tick -> remove for override
			command.getPlayer().getCommands().remove(next);
		command.setTick(nextTick);
		playerCommands.add(command);
	}

	protected Command getCommand(Player player, int tick)
	{
		if(player.getCommands() == null)
			return null;
		if(player.getCommands().size() == 0)
			return null;
		Command last = player.getCommands().get(player.getCommands().size() - 1);
		if(last.getTick() != tick)
			return null;
		return last;
	}

	protected void rebuild(Match match)
	{
		if(match.getState() != EnumMatchState.waiting)
			throw new IllegalStateException("match already started");

		int playerCount = match.getPlayers().size();
		if(playerCount < 2)
			playerCount = 2;

		logger.debug("updating match map");
		// remove all objects
		match.getMap().getObjects().clear();
		// add walls
		Wall w;
		for(int i = 0; i < playerCount; i++)
		{
			w = new Wall();
			w.setId(++objectIdCounter);
			w.setName("wall");
			w.setColor(Color.WHITE);
			w.setStart(Geometry.getStartCornerPoint(playerCount * 2, i * 2 + 1));
			w.setEnd(Geometry.getEndCornerPoint(playerCount * 2, i * 2 + 1));
			match.getMap().getObjects().add(w);
		}
		// add sliders as Map Object
		Player p;
		for(int i = 0; i < match.getPlayers().size(); i++)
		{
			p = match.getPlayers().get(i);
			updateSlider(p, playerCount, i);
			match.getMap().getObjects().add(p.getSlider());
		}
	}

	protected Color randomColor()
	{
		return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
	}

	protected void updateSlider(Player player, int playerCount, int playerIndex)
	{
		Slider slider = player.getSlider();
		double position = player.getSliderPosition();
		double size = player.getSliderSize();

		double startPos = position - size / 2;
		double endPos = position + size / 2;

		Vector startCorner = Geometry.getStartCornerPoint(playerCount * 2, playerIndex * 2);
		Vector endCorner = Geometry.getEndCornerPoint(playerCount * 2, playerIndex * 2);

		Vector edge = new Vector(endCorner).sub(startCorner);

		Vector start = new Vector(startCorner).add(new Vector(edge).scale(startPos));
		Vector end = new Vector(startCorner).add(new Vector(edge).scale(endPos));

		slider.setStart(start);
		slider.setEnd(end);
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
			long start, end, sleep;
			while(match.getState() != EnumMatchState.deleted)
			{
				start = System.currentTimeMillis();
				tick(match);
				end = System.currentTimeMillis();

				sleep = interval - (end - start);
				if(sleep > 0)
				{
					try
					{
						Thread.sleep(sleep);
					}
					catch(InterruptedException e)
					{
						logger.error("could not sleep", e);
					}
				}
			}
			
			tickers.remove(this);
		}
	}
}
