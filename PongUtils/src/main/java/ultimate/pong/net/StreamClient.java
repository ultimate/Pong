package ultimate.pong.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class StreamClient extends ActiveClient
{
	protected String	delim;

	public StreamClient(String delim)
	{
		super();
		this.delim = delim;
	}

	protected abstract InputStream getInputStream() throws IOException;

	protected abstract OutputStream getOutputStream() throws IOException;

	@Override
	protected String readNextMessage() throws IOException
	{
		try
		{
			StringBuffer sb = new StringBuffer();
			int c;
			while(true)
			{
				c = getInputStream().read();
				if(c == -1)
					break;

				sb.append((char) c);

				if(sb.length() > delim.length())
				{
					boolean delimFound = true;
					for(int di = 0; di < delim.length(); di++)
					{

						if(sb.charAt(sb.length() - delim.length() + di) != delim.charAt(di))
						{
							delimFound = false;
							break;
						}
					}
					if(delimFound)
						return sb.toString();
				}
			}
		}
		catch(Exception e)
		{
			logger.error("error reading message (connected? " + isConnected() + ")", e);
		}
		return null;
	}

	@Override
	public void sendMessage(String message) throws IOException
	{
		this.getOutputStream().write(message.getBytes());
		this.getOutputStream().write(delim.getBytes());
		this.getOutputStream().flush();
	}
}