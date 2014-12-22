package ultimate.pong.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Command
{
	@JsonIgnore
	protected Player	player;
	protected Double	sliderPosition;
	protected Boolean	release;
	protected Boolean	playerReady;
	protected String	playerName;
	protected Color		playerColor;
	@JsonIgnore
	protected int		tick;

	public Command()
	{
		super();
	}

	public Command(Player player, Double sliderPosition, Boolean release, Boolean playerReady, String playerName, Color playerColor)
	{
		super();
		this.player = player;
		this.sliderPosition = sliderPosition;
		this.release = release;
		this.playerReady = playerReady;
		this.playerName = playerName;
		this.playerColor = playerColor;
	}

	public Player getPlayer()
	{
		return player;
	}

	public void setPlayer(Player player)
	{
		this.player = player;
	}

	public Double getSliderPosition()
	{
		return sliderPosition;
	}

	public void setSliderPosition(Double sliderPosition)
	{
		this.sliderPosition = sliderPosition;
	}

	public Boolean getRelease()
	{
		return release;
	}

	public void setRelease(Boolean release)
	{
		this.release = release;
	}

	public Boolean getPlayerReady()
	{
		return playerReady;
	}

	public void setPlayerReady(Boolean playerReady)
	{
		this.playerReady = playerReady;
	}

	public String getPlayerName()
	{
		return playerName;
	}

	public void setPlayerName(String playerName)
	{
		this.playerName = playerName;
	}

	public Color getPlayerColor()
	{
		return playerColor;
	}

	public void setPlayerColor(Color playerColor)
	{
		this.playerColor = playerColor;
	}

	public int getTick()
	{
		return tick;
	}

	public void setTick(int tick)
	{
		this.tick = tick;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((playerColor == null) ? 0 : playerColor.hashCode());
		result = prime * result + ((playerName == null) ? 0 : playerName.hashCode());
		result = prime * result + ((playerReady == null) ? 0 : playerReady.hashCode());
		result = prime * result + ((release == null) ? 0 : release.hashCode());
		result = prime * result + ((sliderPosition == null) ? 0 : sliderPosition.hashCode());
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
		Command other = (Command) obj;
		if(playerColor == null)
		{
			if(other.playerColor != null)
				return false;
		}
		else if(!playerColor.equals(other.playerColor))
			return false;
		if(playerName == null)
		{
			if(other.playerName != null)
				return false;
		}
		else if(!playerName.equals(other.playerName))
			return false;
		if(playerReady == null)
		{
			if(other.playerReady != null)
				return false;
		}
		else if(!playerReady.equals(other.playerReady))
			return false;
		if(release == null)
		{
			if(other.release != null)
				return false;
		}
		else if(!release.equals(other.release))
			return false;
		if(sliderPosition == null)
		{
			if(other.sliderPosition != null)
				return false;
		}
		else if(!sliderPosition.equals(other.sliderPosition))
			return false;
		if(tick != other.tick)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "Command [player=" + player + ", sliderPosition=" + sliderPosition + ", release=" + release + ", tick=" + tick + "]";
	}
}
