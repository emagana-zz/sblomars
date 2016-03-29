package source;

import java.io.*;
import java.net.*;

public class Sblomars_Client {
	static final String HOST = "localhost";
	static final int PUERTO=6705;

	public Sblomars_Client( ) {
		try{
			//System.out.println( "Inicia Socket Client" );
			Socket skCliente = new Socket( HOST , PUERTO );
			//System.out.println("Con objetos");
			InputStream in = skCliente.getInputStream();
			ObjectInputStream receive = new ObjectInputStream(in);
			Object received = receive.readObject();
			skCliente.close();
			in.close();
			receive.close();
		} 
		catch( Exception e ) {
			System.out.println( e.getMessage() );
		}
	}

	public static void main( String[] arg ) {
		NetworkLevel_Client activation = new NetworkLevel_Client ();
		activation.gettingResourceMaganagerData ("sblomars.conf");
		new Sblomars_Client();
	}
}
