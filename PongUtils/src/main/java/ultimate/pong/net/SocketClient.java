package ultimate.pong.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketClient extends StreamClient
{
	protected Socket	socket;

	public SocketClient(String delim, Socket socket)
	{
		super(delim);
		this.socket = socket;
	}

	@Override
	public boolean isConnected()
	{
		return this.socket.isConnected() && !this.socket.isClosed();
	}

	@Override
	public InputStream getInputStream() throws IOException
	{
		return this.socket.getInputStream();
	}

	@Override
	public OutputStream getOutputStream() throws IOException
	{
		return this.socket.getOutputStream();
	}

	@Override
	public void disconnect() throws IOException
	{
		this.socket.close();
	}
}
