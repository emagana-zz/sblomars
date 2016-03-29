package source;

import java.io.*;
import java.net.*;

public class Memory_Client {

	public String resource_manager_host = "";
	public int resource_manager_port = 0;
	
	Memory_Client () {
		
	}
	
	public void gettingResourceMaganagerData (String config) {
		String s;
		String[] params;
		int i, offset;
		int lastoffset=0;
		try {
			BufferedReader br = new BufferedReader (new FileReader(config));
			while ( (s = br.readLine()) != null) {
				offset=0;
				lastoffset=0;
				params = new String[10];
				i=0;
				while ((offset = s.indexOf(" ", lastoffset))!=-1) {
					params[i] = s.substring(lastoffset, offset);
					lastoffset = offset+1;
					i++;
				}
				params[i] = s.substring(lastoffset);            			
				if (params[0].equals("resource_manager")) {
					resource_manager_host = params[1];					
				}
				if (params[0].equals("memory")) {
					resource_manager_port = Integer.parseInt(params[1]);					
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println ("Resource Manager Host: "+ resource_manager_host);
		//System.out.println ("Memory Port: "+ resource_manager_port );
	}
	
	public void sendingRequest (String _localhost){
		try{
			//System.out.println( "Init Memory Socket Client Registration" );
			//System.out.println("resource manager host: "+resource_manager_host);
			//System.out.println("resource manager port: "+resource_manager_port);			
			

			Socket skClienteMemory = new Socket( resource_manager_host , resource_manager_port );
							
			OutputStream out = skClienteMemory.getOutputStream();
			ObjectOutputStream sendStream = new ObjectOutputStream(out);
			//We are sending IP address of this resource
			//System.out.println(_localhost);
			sendStream.writeObject(_localhost);							
			skClienteMemory.close();
			out.close();
			sendStream.close();
		} 
		catch( Exception e ) {
					
			System.out.println( e.getMessage() );
		}
	}
	
	public void notifyingAgent(String _localhost ) {
		gettingResourceMaganagerData ("sblomars.conf");
		sendingRequest (_localhost);
	}
	
	public void main( String[] arg ) {
	
		gettingResourceMaganagerData ("sblomars.conf");
	}
}
