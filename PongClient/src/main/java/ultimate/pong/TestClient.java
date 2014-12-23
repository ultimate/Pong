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
		int players = 5;

		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter writer = mapper.writer();

		final Socket[] sockets = new Socket[players];
		int[] messagesSent = new int[players];

		try
		{

			for(int i = 0; i < players; i++)
			{
				sockets[i] = new Socket("localhost", port);
				logger.info(i + ": connected");
				
				final int j = i;
				new Thread() {
					public void run() {
						try
						{
							while(sockets[j].getInputStream().read() != -1);
						}
						catch(IOException e)
						{
							e.printStackTrace();
						}
					}
				}.start();
				
				Thread.sleep(100);
			}

			Thread.sleep(1000);
			
			final double speed = 0.05;
			double pos = 0.5;
			double dir = speed;
			
			int cycle = 0;
			
			while(true)
			{
				cycle++;
				pos += dir;
				if(pos >= 1.0)
					dir = -speed;
				else if(pos <= 0.0)
					dir = +speed;
				
//				logger.debug("pos=" + pos);
				
				for(int i = 0; i < players; i++)
				{
					sockets[i].getOutputStream().write(writer.writeValueAsString(new Command(null, pos, true, cycle > 10, "player" + i, null)).getBytes());
					sockets[i].getOutputStream().write("\n\n".getBytes());
					sockets[i].getOutputStream().flush();
					messagesSent[i]++;
					if(messagesSent[i] % 100 == 0)
						logger.info(i + ": " + messagesSent[i]);
				}

				Thread.sleep(20);
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
