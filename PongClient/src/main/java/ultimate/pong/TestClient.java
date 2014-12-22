package ultimate.pong;

import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ultimate.pong.data.model.Command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class TestClient
{
	protected static transient final Logger logger = LoggerFactory.getLogger(TestClient.class);
	
	public static void main(String[] args) throws Exception
	{
		final int port = 55555;
		int players = 3;

		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter writer = mapper.writer();

		Socket[] sockets = new Socket[players];
		int[] messagesSent = new int[players];

		try
		{

			for(int i = 0; i < players; i++)
			{
				sockets[i] = new Socket("localhost", port);
				logger.info(i + ": connected");
			}

			Thread.sleep(1000);
			
			final double speed = 0.005;
			double pos = 0.5;
			double dir = speed;

			while(true)
			{
				pos += dir;
				if(pos >= 1.0)
					dir = -speed;
				else if(pos <= 0.0)
					dir = +speed;
				
//				logger.debug("pos=" + pos);
				
				for(int i = 0; i < players; i++)
				{
					sockets[i].getOutputStream().write(writer.writeValueAsString(new Command(null, pos, true, true, "player" + i, null)).getBytes());
					sockets[i].getOutputStream().write("\n\n".getBytes());
					sockets[i].getOutputStream().flush();
					messagesSent[i]++;
					if(messagesSent[i] % 100 == 0)
						logger.info(i + ": " + messagesSent[i]);

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
