package ultimate.pong.data.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ultimate.pong.enums.EnumMatchState;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Match
{
	protected Integer			id;
	protected String			name;
	protected List<Player>		players;
	protected Map				map;
	protected EnumMatchState	state;
	protected int				tick;
	protected Date				lastTickDate;
	protected int				port;

	public Match()
	{
		super();
		this.players = new ArrayList<Player>();
		this.tick = 0;
		this.state = EnumMatchState.waiting;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public List<Player> getPlayers()
	{
		return players;
	}

	public void setPlayers(List<Player> players)
	{
		this.players = players;
	}

	public Map getMap()
	{
		return map;
	}

	public void setMap(Map map)
	{
		this.map = map;
	}

	public EnumMatchState getState()
	{
		return state;
	}

	public void setState(EnumMatchState state)
	{
		this.state = state;
	}

	public int getTick()
	{
		return tick;
	}

	public void setTick(int tick)
	{
		this.tick = tick;
	}

	public Date getLastTickDate()
	{
		return lastTickDate;
	}

	public void setLastTickDate(Date lastTickDate)
	{
		this.lastTickDate = lastTickDate;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastTickDate == null) ? 0 : lastTickDate.hashCode());
		result = prime * result + ((map == null) ? 0 : map.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((players == null) ? 0 : players.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + tick;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		Match other = (Match) obj;
		if(id == null)
		{
			if(other.id != null)
				return false;
		}
		else if(!id.equals(other.id))
			return false;
		if(lastTickDate == null)
		{
			if(other.lastTickDate != null)
				return false;
		}
		else if(!lastTickDate.equals(other.lastTickDate))
			return false;
		if(map == null)
		{
			if(other.map != null)
				return false;
		}
		else if(!map.equals(other.map))
			return false;
		if(name == null)
		{
			if(other.name != null)
				return false;
		}
		else if(!name.equals(other.name))
			return false;
		if(players == null)
		{
			if(other.players != null)
				return false;
		}
		else if(!players.equals(other.players))
			return false;
		if(state != other.state)
			return false;
		if(tick != other.tick)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "Match [id=" + id + ", name=" + name + ", players=" + players + ", map=" + map + ", state=" + state + ", tick=" + tick
				+ ", lastTickDate=" + lastTickDate + "]";
	}
}
