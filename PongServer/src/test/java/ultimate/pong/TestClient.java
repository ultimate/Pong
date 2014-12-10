package ultimate.pong;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import ultimate.pong.data.model.Command;
import ultimate.pong.logic.PongHost;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class TestClient
{
	public static void main(String[] args) throws IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter writer = mapper.writer();

		int port = 5555;

		String name = "player" + System.currentTimeMillis();

		Map<String, Object> playerInfo = new HashMap<String, Object>();
		playerInfo.put("name", name);
		playerInfo.put("ready", true);
		
		Socket so = null;
		try
		{
			so = new Socket("localhost", port);
			
			System.out.println("connected");
			
			try
			{
				Thread.sleep(10000);
			}
			catch(InterruptedException e1)
			{
				e1.printStackTrace();
			}
			
			so.getOutputStream().write(writer.writeValueAsString(playerInfo).getBytes());
			so.getOutputStream().write(PongHost.DELIM.getBytes());
			so.getOutputStream().flush();
			
			System.out.println("ready");
			
			double speed = 0.05;
			double pos = 0.5;
			double dir = speed;
			
			while(true)
			{
				pos += dir;
				if(pos >= 1.0)
					dir = -speed;
				else if(pos <= 0.0)
					dir = +speed;
				
				so.getOutputStream().write(writer.writeValueAsString(new Command(null, pos, true)).getBytes());
				so.getOutputStream().write(PongHost.DELIM.getBytes());
				so.getOutputStream().flush();
				
				while(so.getInputStream().available() > 0)
				{
					so.getInputStream().read();
				}
				
				try
				{
					Thread.sleep(500);
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
		finally
		{
			if(so != null)
				so.close();
		}
	}
}
