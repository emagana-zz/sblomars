package source;

import java.io.*;
import java.net.*;
import java.util.*;

class Memory_Server extends Thread {

	public int PUERTO;
	public Vector memoryParameters; 
	protected ServerSocket listen_socket;
	MemoryAgent  proc_agent;

	public Memory_Server() {
	}

	public int getting_port ()
	{
		//Default value
		int port = 0;
		GettingPorts ports = new GettingPorts ();
		try {
			//Configuration Files
			String config = "ports.conf";
			String resource = "memory";
			port = Integer.parseInt(ports.initFromFile(config, resource));
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception in: " + ex.getMessage());
		}
		return port;
	}

	public void set_memoryAgent(MemoryAgent pointer)
	{
		proc_agent = pointer;		
	}

	public void refreshing_parameters (Vector parameters) { 
		////System.out.println("New Parameters");
		memoryParameters = parameters;
	}

	public void closingSocket () { 
		////System.out.println("Closing Socket Memory");
		try {
			listen_socket.close();
		
		}
		catch (Throwable e) {
			System.out.println("Exception Creating Server Socket: " + e.getMessage());
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
				////System.out.println("Memory Parameters");
				Socket skCliente = listen_socket.accept(); 
				OutputStream out = skCliente.getOutputStream();
				ObjectOutputStream sendStream = new ObjectOutputStream(out);
				Vector v = proc_agent.memoryParameters;
				sendStream.writeObject(v);							
				skCliente.close();
				out.close();
				sendStream.close();
			}
		} catch( Exception e ) {
			System.out.println( e.getMessage() );
		}
	}
	
}
