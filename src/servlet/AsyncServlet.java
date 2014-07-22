package servlet;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.atmosphere.config.service.MeteorService;
import org.atmosphere.cpr.AtmosphereResource.TRANSPORT;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
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
	private static int i = 0;
	
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		
		Broadcaster broadcaster = BroadcasterFactory.getDefault().lookup("name");
		if(broadcaster == null) broadcaster = BroadcasterFactory.getDefault().get("name");
		
		// Getting meteor service
		Meteor meteor = Meteor.build(req);
		meteor.setBroadcaster(broadcaster);
		
		// If it is Long Polling mode, sets the Persistent Connection
		meteor.resumeOnBroadcast(meteor.transport() == TRANSPORT.LONG_POLLING ? true : false).suspend(-1);
		
		// Broadcasts a message with random number in a 2 seconds interval;
		meteor.schedule(new Callable<String>() {
			@Override
			public String call() throws Exception {
				return "Random Number " + (i++);
			}
		}, 2);
		
	}
		
}