package ultimate.pong.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ultimate.pong.data.model.Match;
import ultimate.pong.data.model.Player;
import ultimate.pong.enums.EnumMatchState;
import ultimate.pong.logic.MatchManager;
import ultimate.pong.logic.PongHost;

public class PongSocketHost extends PongHost implements Runnable
{
	protected transient final Logger	logger	= LoggerFactory.getLogger(getClass());

	protected int						port;

	protected ServerSocket				server;

	protected Thread					thread;

	public PongSocketHost(MatchManager matchManager, Match match, int port) throws IOException
	{
		super(matchManager, match);
		this.port = port;
		this.server = new ServerSocket(port);
	}

	@Override
	public void startAccepting()
	{
		if(this.thread != null)
			throw new IllegalStateException("already accepting");

		this.thread = new Thread(this);
		this.thread.start();
	}

	@Override
	public void stopAccepting()
	{
		if(this.thread == null)
			throw new IllegalStateException("not accepting");

		try
		{
			this.server.close();
			this.thread.join();
		}
		catch(IOException e)
		{
			logger.error("error closing socket server", e);
		}
		catch(InterruptedException e)
		{
			logger.error("error joining thread", e);
		}
	}

	@Override
	public void run()
	{
		Socket clientSocket;
		Player player;
		Client client;
		logger.info("listening: " + this.server.getLocalPort());
		while(match.getState() == EnumMatchState.waiting)
		{
			try
			{
				clientSocket = this.server.accept();
				logger.info("client connected");
				client = new Client(clientSocket);
				player = this.matchManager.addPlayer(match, "unnamed");
				player.setConnected(true);
				client.listen(player);
				this.matchManager.tick(match);
			}
			catch(SocketException e)
			{
				logger.info("socket closed");
				if(this.server.isClosed())
					break;
			}
			catch(IOException e)
			{
				logger.error("error accepting client connections", e);
			}
		}
	}

	private class Client extends PongHost.Client
	{
		private Socket	socket;

		public Client(Socket socket)
		{
			super();
			this.socket = socket;
		}

		public void run()
		{
			try
			{
				StringBuffer sb = new StringBuffer();
				int c;
				while(true)
				{
					c = socket.getInputStream().read();
					if(c == -1)
						break;

					sb.append((char) c);
					
					if(sb.length() > DELIM.length())
					{
						boolean delimFound = true;
						for(int di = 0; di < DELIM.length(); di++)
						{

							if(sb.charAt(sb.length() - DELIM.length() + di) != DELIM.charAt(di))
							{
								delimFound = false;
								break;
							}
						}
						if(delimFound)
						{
							try
							{
								// complete
								handleMessage(sb.toString());
							}
							catch(IOException e)
							{
								logger.error("could not handle client message:\n" + sb.toString(), e);
							}
							finally
							{
								// clear buffer
//								sb.delete(0, sb.length());
//								Thread.sleep(1);
								sb = new StringBuffer();
							}
						}
					}
				}
			}
			catch(Exception e)
			{
				logger.error("error with client '" + this.player.getName() + "' -> disconnecting", e);
			}
			finally
			{
				try
				{
					this.socket.close();
				}
				catch(IOException e)
				{
					// ignore
				}
			}
		}

		@Override
		protected void sendMessage(String message) throws IOException
		{
			this.socket.getOutputStream().write(message.getBytes());
			this.socket.getOutputStream().write(DELIM.getBytes());
			this.socket.getOutputStream().flush();
		}
	}
}
