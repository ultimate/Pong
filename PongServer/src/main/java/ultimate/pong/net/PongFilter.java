package ultimate.pong.net;

import java.io.IOException;
import java.net.BindException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ultimate.pong.data.model.Match;
import ultimate.pong.logic.MatchManager;
import ultimate.pong.logic.PongHost;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class PongFilter implements Filter
{
	protected transient final Logger	logger				= LoggerFactory.getLogger(getClass());

	public static final String			PARAM_NAME			= "name";
	public static final String			PARAM_PORT			= "port";

	protected MatchManager				matchManager;

	protected ObjectMapper				mapper				= new ObjectMapper();
	protected ObjectWriter				writer				= mapper.writer();

	public PongFilter(MatchManager matchManager)
	{
		super();
		this.matchManager = matchManager;
	}

	public MatchManager getMatchManager()
	{
		return matchManager;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		httpResponse.setContentType("text/plain");

		if(httpRequest.getMethod().equalsIgnoreCase("post"))
		{
			String name = httpRequest.getParameter(PARAM_NAME);
			int port = Integer.parseInt(httpRequest.getParameter(PARAM_PORT));

			logger.info("incoming post: name='" + name + "', port=" + port);

			if(name == null || port <= 0 || port > 65535)
			{
				httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "illegal name or port");
				return;
			}

			this.matchManager.createMatch(name, port);
		}
		else if(httpRequest.getMethod().equalsIgnoreCase("get"))
		{
			logger.info("incoming get");
		}
		else
		{
			httpResponse.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "illegal method");
			return;
		}
		
		httpResponse.getOutputStream().write(writer.writeValueAsString(matchManager.getMatches()).getBytes());
		httpResponse.setStatus(HttpServletResponse.SC_OK);
	}

	@Override
	public void destroy()
	{
	}
}
