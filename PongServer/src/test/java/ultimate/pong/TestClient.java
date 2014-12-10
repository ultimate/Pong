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
	public static void main(String[] args) throws Exception
	{
		final int port = 5555;
		int players = 2;

		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter writer = mapper.writer();

		Socket[] sockets = new Socket[players];
		int[] messagesSent = new int[players];

		try
		{

			for(int i = 0; i < players; i++)
			{
				sockets[i] = new Socket("localhost", port);
				System.out.println(i + ": connected");
			}

			Thread.sleep(1000);

			for(int i = 0; i < players; i++)
			{
				Map<String, Object> playerInfo = new HashMap<String, Object>();
				playerInfo.put("name", "player" + i);
				playerInfo.put("ready", true);

				sockets[i].getOutputStream().write(writer.writeValueAsString(playerInfo).getBytes());
				sockets[i].getOutputStream().write(PongHost.DELIM.getBytes());
				sockets[i].getOutputStream().flush();

				System.out.println(i + ": ready");
			}

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

				for(int i = 0; i < players; i++)
				{
					sockets[i].getOutputStream().write(writer.writeValueAsString(new Command(null, pos, true)).getBytes());
					sockets[i].getOutputStream().write(PongHost.DELIM.getBytes());
					sockets[i].getOutputStream().flush();
					messagesSent[i]++;
					if(messagesSent[i] % 100 == 0)
						System.out.println(i + ": " + messagesSent[i]);

					while(sockets[i].getInputStream().available() > 0)
						sockets[i].getInputStream().read();
				}

				Thread.sleep(10);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			for(int i = 0; i < players; i++)
			{
				if(sockets[i] == null)
					continue;
				try
				{
					sockets[i].close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
