package source;

import java.io.*;
import java.net.*;
import java.util.*;

class Storage_Server extends Thread {
	public int PUERTO;
	public Vector hddParameters; 
	protected ServerSocket listen_socket;
	StorageAgent  proc_agent;

	public Storage_Server () {
	}

	public int getting_port ()
	{
		//Default value
		int port = 0;
		GettingPorts ports = new GettingPorts ();
		try {
			//Configuration Files
			String config = "ports.conf";
			String resource = "storage";
			port = Integer.parseInt(ports.initFromFile(config, resource));
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception in: " + ex.getMessage());
		}
		return port;
	}

	public void set_storageAgent(StorageAgent pointer)
	{
		proc_agent = pointer;		
	}

	public void refreshing_parameters (Vector parameters) { 
		////System.out.println("New Parameters");
		hddParameters = parameters;
	}

	public void closingSocket () { 
		////System.out.println("Closing Socket Network");
		try {
			listen_socket.close();
			//this.finalize();
			//this.interrupt();
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
				////System.out.println("Parameters Storage");
				Socket skCliente = listen_socket.accept(); 
				OutputStream out = skCliente.getOutputStream();
				ObjectOutputStream sendStream = new ObjectOutputStream(out);
				Vector v = proc_agent.hddParameters;
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
