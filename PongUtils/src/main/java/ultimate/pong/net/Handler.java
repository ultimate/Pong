package ultimate.pong.net;

import java.io.IOException;

public interface Handler
{
	public void setup() throws IOException;
	
	public void handleMessage(String message) throws IOException;
	
	public void teardown() throws IOException;
}
