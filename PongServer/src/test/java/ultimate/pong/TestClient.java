package ultimate.pong;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

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
			
			so.getOutputStream().write(writer.writeValueAsString(playerInfo).getBytes());
			so.getOutputStream().write(PongHost.DELIM.getBytes());
			so.getOutputStream().flush();
			
			while(true)
			{
				so.getOutputStream().write(writer.writeValueAsString(new Command(null, Math.random(), true)).getBytes());
				so.getOutputStream().write(PongHost.DELIM.getBytes());
				so.getOutputStream().flush();
				
				try
				{
					Thread.sleep(1000);
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
