package source;

import java.io.*;
import java.net.*;
import java.util.*;

class NetworkLevel_Server extends Thread {

	public int PUERTO;
	public Vector networklevelParameters; 
	public ServerSocket listen_socket;
	NetworkLevelAgent  proc_agent;

	public NetworkLevel_Server() {
	}

	public int getting_port ()
	{
		//Default value
		int port = 0;
		GettingPorts ports = new GettingPorts ();
		try {
			//Configuration Files
			String config = "ports.conf";
			String resource = "networklevel";
			port = Integer.parseInt(ports.initFromFile(config, resource));
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception in: " + ex.getMessage());
		}
		return port;
	}

	public void set_NetworkLevelAgent(NetworkLevelAgent pointer)
	{
		proc_agent = pointer;		
	}

	public void refreshing_parameters (Vector parameters) { 
		////System.out.println("New Parameters");
		networklevelParameters = parameters;
	}

	public void closingSocket () { 
		////System.out.println("Closing Socket Memory");
		try {
			listen_socket.close();
			//this.finalize();
			//this.interrupt();
		}
		catch (Throwable e) {
			//System.out.println("Exception Creating Server Socket: " + e.getMessage());
		}
	}

	public void new_parameters () { 
		try {
			PUERTO = getting_port ();
			//System.out.println("Listening in Port: " + PUERTO );
			listen_socket = new ServerSocket(PUERTO); 
			this.start();
		}
		catch (IOException e) {
			System.out.println("Exception Creating Server Socket: " + e.getMessage());
		}
	}


	public void run() {
		try {
			while (true) {
				////System.out.println("Parameters networklevel");
				Socket skCliente = listen_socket.accept(); 
				OutputStream out = skCliente.getOutputStream();
				ObjectOutputStream sendStream = new ObjectOutputStream(out);
				Vector v = proc_agent.networkMetricsParameters;
				sendStream.writeObject(v);							
				skCliente.close();
				out.close();
				sendStream.close();
			}
		} catch( Exception e ) {
			System.out.println( e.getMessage() );
		}
	}

	/*
	   public static void main( String[] arg ) {
	   new Sblomars_Server();
	   }
	   */
}

