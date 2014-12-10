package ultimate.pong;

import java.io.IOException;

import ultimate.pong.data.model.Color;
import ultimate.pong.data.model.Command;
import ultimate.pong.data.model.Map;
import ultimate.pong.data.model.MapObject;
import ultimate.pong.data.model.Match;
import ultimate.pong.data.model.Player;
import ultimate.pong.data.model.objects.Ball;
import ultimate.pong.data.model.objects.Slider;
import ultimate.pong.data.model.objects.Wall;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class TestSomething
{
	public static void main(String[] args) throws IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter writer = mapper.writer();

		System.out.println(writer.writeValueAsString(new Color()));
		System.out.println(writer.writeValueAsString(new Command()));
		System.out.println(writer.writeValueAsString(new Map()));
		System.out.println(writer.writeValueAsString(new MapObject()));
		System.out.println(writer.writeValueAsString(new Match()));
		System.out.println(writer.writeValueAsString(new Player()));
		System.out.println(writer.writeValueAsString(new Ball()));
		System.out.println(writer.writeValueAsString(new Slider()));
		System.out.println(writer.writeValueAsString(new Wall()));
	}
}
