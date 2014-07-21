package servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.atmosphere.config.service.MeteorService;
import org.atmosphere.cpr.AtmosphereResource.TRANSPORT;
import org.atmosphere.cpr.Meteor;
import org.atmosphere.interceptor.AtmosphereResourceLifecycleInterceptor;

/**
 * 
 * 
 * @author Adriano Brito
 *
 */
@MeteorService(path = "/*", interceptors = { AtmosphereResourceLifecycleInterceptor.class })
public class AsyncServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		
		// Getting meteor service
		Meteor meteor = (Meteor)req.getSession().getAttribute("meteor"); 
		if(meteor == null)	meteor = Meteor.build(req);
		
		// If it is Long Polling mode, sets the Persistent Connection
		meteor.resumeOnBroadcast(meteor.transport() == TRANSPORT.LONG_POLLING ? true : false).suspend(-1);
		
		// Broadcasts a message with random number in a 2 seconds interval
		meteor.schedule("Random Number " + Math.random(), 2);
		
	}
		
}