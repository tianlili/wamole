package com.baidu.wamole.server;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;

import com.baidu.wamole.model.Project;
import com.baidu.wamole.model.Wamole;
import com.caucho.quercus.servlet.QuercusServlet;

public class JettyServer {
	private static MainServer server;

	public static void setPort(int port) throws Exception {
		if(server != null)
			server.stop();
		server = new MainServer(port);
	}

	public static void start() throws Exception {
		initHandler();
		
		for(SubServer ss : server.vector)
			ss.start();
		
		server.start();
		server.join();
	}
	
	private static void initHandler(){
		//common resource support
		HandlerCollection collection = new HandlerCollection();
		collection.addHandler(new CommonResouceHandlerWrapper().getHandler());
		
		//project resource support
		for(Project<?,?> project : Wamole.getInstance().getModels(Project.class)){//getProjectList().getView()){
			addPath(project);
		}
		
		//restful support
		collection.addHandler(new RestfulHandlerWrapper().getHandler());
		
		server.setHandler(collection);
	}
	
	public static void addPath(Project<?,?> project){
		try {
			server.addPath(project.getPath(), "/project/"+project.getName()+"/view").start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static class MainServer extends Server {
		public MainServer(int port) {
			super(port);
		}

		Vector<JettyServer.SubServer> vector = new Vector<JettyServer.SubServer>();

		@Override
		public void handle(String target, Request baseRequest,
				HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException {
//			System.out.println(target);
			SubServer subServer = null;
			for (JettyServer.SubServer ss : vector) {				
				if (target.startsWith(ss.getURIPrefix())) {
					subServer = ss;
					break;
				}
			}
			if (subServer != null)
				subServer.handle(target, baseRequest, request, response);
			else
				super.handle(target, baseRequest, request, response);
		}

		public SubServer addPath(String path, String uri) {
			JettyServer.SubServer ss = new JettyServer.SubServer(path, uri, getUsablePort());
			ss.enablePHPSupport();
			vector.add(ss);
			return ss;
		}

		public int getUsablePort() {
			int port = 20120;
			for (SubServer ss : vector) {
				if (port == ss.getPort())
					port++;
			}
			return port;
		}
	}

	private static class SubServer extends Server {
		private final WebAppContext context;
		public SubServer(String path, String uri, int port) {
			super(port);
			this.port = port;
			this.uriPrefix= uri;
			context = new WebAppContext(path, uri);
			context.setDefaultsDescriptor(Thread.currentThread().getClass()
					.getResource("/resource/jetty/webdefault.xml").toString());
			this.setHandler(context);
		}

		final private int port;

		final private String uriPrefix;
		
		public void enablePHPSupport(){
			context.addServlet(QuercusServlet.class, "*.php");
		}

		public int getPort() {
			return port;
		}

		public String getURIPrefix() {
			return uriPrefix;
		}
	}
}
