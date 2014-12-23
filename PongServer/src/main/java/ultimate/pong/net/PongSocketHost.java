package ultimate.pong.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ultimate.pong.data.model.Match;
import ultimate.pong.logic.MatchManager;
import ultimate.pong.logic.PongHost;

public class PongSocketHost extends PongHost
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
	
	public Client accept() throws IOException
	{
		Socket clientSocket = this.server.accept();
		return new SocketClient(DELIM, clientSocket);
	}

	@Override
	public void close() throws IOException
	{
		this.server.close();
	}
}
