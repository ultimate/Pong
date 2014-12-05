package ultimate.pong.logic;

import java.util.List;

import ultimate.pong.data.model.Command;
import ultimate.pong.data.model.Match;
import ultimate.pong.data.model.Player;

public interface MatchManager
{
	public void tick(Match match);

	public Match createMatch(String name);

	public void deleteMatch(Match match);
	
	public void addHost(Match match, PongHost host);
	
	public boolean isReady(Match match);
	
	public boolean start(Match match);
	
	public List<Match> getMatches();

	public Player addPlayer(Match match, String name);

	public void command(Match match, Command command);
}
