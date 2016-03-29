package source;

import java.io.*;
import java.net.*;

class Sblomars_Server extends Thread {

	public int port;
	public String host;
	protected ServerSocket listen_socket;

	public Sblomars_Server() {
	}

	public void getting_host ()
	{
		GetHostCommunity hosting = new GetHostCommunity ();
		try {
			String [] systemID = hosting.initFromFile("FALTA CAMBIOS");
			host = systemID [1]; 
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting Host Information: " + ex.getMessage());
		}
		//target = new SnmpTarget ();
	}
	
	public void new_parameters () { 
		try {
			port = 6705;
			//System.out.println("Listening in Port: " + port );
			listen_socket = new ServerSocket(port); 
			getting_host();
			this.start();
		}
		catch (IOException e) {
			System.out.println("Exception Creating Server Socket: " + e.getMessage());
		}
	}
		
	public void run() {
		try {
			while (true) {
				////System.out.println("Parametros Software");
				Socket skCliente = listen_socket.accept(); 
				OutputStream out = skCliente.getOutputStream();
				ObjectOutputStream sendStream = new ObjectOutputStream(out);
				sendStream.writeUTF(host);							
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
