/**
 *  Agent getting resources information
 *
 *     @author     Edgar Magana
 *     @version    1.2
 *     @see		   jdk 1.5.0_06
 */

package source;

import java.util.*;

import com.adventnet.snmp.beans.*;

public class SblomarsNetwork extends Thread {

	String [] systemID = null;
	static String comm;
	static String host;
	static int port;
	public String InOctets;
	public String OutOctets;
	public String Speed;
	public int errorFlag = 0;

	static String old_value_c,old_value_d,old_value_e,old_value_f,old_value_g,old_value_h;
	static String [] old_value_a = new String[327685-327682];
	static String [] old_value_b = new String[1040-1025];
	
	Vector networkDetails = new Vector();
	
	static SnmpTarget target;
	
	
	SblomarsNetwork () {
		
		target = new SnmpTarget ();
		
	}

	public String startingHosts (String configuration_file) {
		GetHostCommunity thread = new GetHostCommunity ();
		try {
			systemID = thread.initFromFile(configuration_file);
			comm = systemID [0];
			host = systemID [1]; 
			port = Integer.parseInt(systemID [2]);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting Host Information: " + ex.getMessage());
		}
		return host;
		//target = new SnmpTarget ();
	}
	
	//old_value_a
	public String [] getNetworkIfIndex() {
		//This is for a maximum of 10 network devices
		String Indexs [] = new String [10]; 
		try {
			int j = 0;
			//for (int i = 5; i <= 15; i++) {  //CAMBIO TEMPORAL PARA ESTA MAQUINA
			for (int i = 327682; i <= 327685; i++) {
				//String oid = ".1.3.6.1.2.1.25.3.4.1.1."+i;
				String oid = ".1.3.6.1.2.1.2.2.1.1."+i;
				String Index = this.snmpget(oid);
				
				//in case we obtain a null value we maintain the old value generated in the last iteration
				old_value_a[j]=(Index==null) ?old_value_a[j] :Index;  
				Index=(Index==null)? old_value_a[j] :Index;

				if (Index != null) {
					Indexs [j] = Index;
					j++;
				}
			}
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting Networks Index: " + ex.getMessage());
		}
		return Indexs;
	}
	
	//old_value_b
	public String [] getNetworkIfIndexUnix() {
		//This is for a maximum of 10 network devices
		String Indexs [] = new String [10]; 
		try {
			int j = 0;
			for (int i = 1025; i <= 1040; i++) {
				String oid = ".1.3.6.1.2.1.25.3.4.1.1."+i;
				String Index = this.snmpget(oid);
				
				//in case we obtain a null value we maintain the old value generated in the last iteration
				old_value_b[j]=(Index==null) ?old_value_b[j] :Index;  
				Index=(Index==null)? old_value_b[j] :Index;

				if (Index != null) {
					Indexs [j] = Index;
					j++;
				}
			}
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting Networks Index: " + ex.getMessage());
		}
		return Indexs;
	}
	
	//old_value_c
	public String getIfInOctets(int i) {
		String oid = ".1.3.6.1.2.1.2.2.1.10."+i;
		InOctets = null;
		try {
			InOctets = this.snmpget(oid);
			
			//in case we obtain a null value we maintain the old value generated in the last iteration
			old_value_c=(InOctets==null) ?old_value_c :InOctets;  
			InOctets=(InOctets==null)? old_value_c :InOctets;  
			
			////System.out.println("In Octects: " + InOctets);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting If In Octets: " + ex.getMessage());
			InOctets = "0";
		}
		return InOctets;
	}

	//old_value_d
	public String getIfOutOctets(int i) {
		String oid = ".1.3.6.1.2.1.2.2.1.16."+i;
		OutOctets = null;
		try {
			OutOctets = this.snmpget(oid);
			
			//in case we obtain a null value we maintain the old value generated in the last iteration
			old_value_d=(OutOctets==null) ?old_value_d :OutOctets;  
			OutOctets=(OutOctets==null)? old_value_d :OutOctets;  
			
			////System.out.println("Out Octects: " + OutOctets);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting If Out Octets: " + ex.getMessage());
			OutOctets = "0";
		}
		return OutOctets;
	}
	
	//old_value_e
	public String getIfSpeed (int i) {
		String oid = ".1.3.6.1.2.1.2.2.1.5."+i;
		Speed = null;
		try {
			Speed = this.snmpget(oid);
			
			//in case we obtain a null value we maintain the old value generated in the last iteration
			old_value_e=(Speed==null) ?old_value_e :Speed;  
			Speed=(Speed==null)? old_value_e :Speed;  
		
			if (Speed.compareTo("0") == 0)	
				Speed = "10000000";
			////System.out.println("Interface Speed: " + Speed);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting If Speed: " + ex.getMessage());
			Speed = "0";
		}
		return Speed;
	}
	
	//old_value_f
	public String getIfLabel (int i) {
		String oid = ".1.3.6.1.2.1.2.2.1.2."+i;
		String Label = "";
		try {
			String temporal_string = null;
			temporal_string = this.snmpget(oid);
			
			//in case we obtain a null value we maintain the old value generated in the last iteration
			old_value_f=(temporal_string==null) ?old_value_f :temporal_string;  
			temporal_string=(temporal_string==null)? old_value_f :temporal_string;  
			
			StringTokenizer st = new StringTokenizer(temporal_string);
			if (st.countTokens() > 1) 
				//Label = temporal_string;
				//break;
			{
				while (st.hasMoreTokens())
				{ 
					int temp = Integer.parseInt(st.nextToken(), 16);
					String tempo = new Character((char)temp).toString();
					if (st.hasMoreTokens())		
						if (tempo.compareTo("-") == 0)
							break;
					Label = Label + tempo;
				}
			}
			else if (st.countTokens() == 1)
				Label = temporal_string;
			////System.out.println("Interface Label: " + Label);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting If Label: " + ex.getMessage());
			Label = "0";
		}
		return Label;
	}
	

	//old_value_g
	public int filteringNetworkDevices(int i) {
		String oid = ".1.3.6.1.2.1.2.2.1.3."+i;
		String HddDeviceType = null;
		int flag = 0;
		try { 
			HddDeviceType = this.snmpget(oid);

			
			//in case we obtain a null value we maintain the old value generated in the last iteration
			old_value_g=(HddDeviceType==null) ?old_value_g :HddDeviceType;  
			HddDeviceType=(HddDeviceType==null)? old_value_g :HddDeviceType;  

			flag = HddDeviceType.compareTo("6");
			////System.out.println("Device Type: "+HddDeviceType);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting Filtering Network Devices: " + ex.getMessage());
		}
		return flag;
	}
	
	
	public long network_bw_old (int j)
	{
		long half_duplex_bw = 0;
		try {
			////System.out.println("Calculating  BW... ");
			long InOctets_initial = Long.parseLong(InOctets);
			long OutOctets_initial = Long.parseLong(OutOctets);
			int delta_interval = 60000;
			int interval_segundos = delta_interval/1000;
			sleep (delta_interval); 
			long InOctets_final = Long.parseLong(getIfInOctets(j));
			long OutOctets_final = Long.parseLong(getIfOutOctets(j));
			//long InOctets_delta = Long.parseLong(getIfInOctets(j)) - InOctets_initial;
			////System.out.println(InOctets_final);
			//long OutOctets_delta = Long.parseLong(getIfOutOctets(j)) - OutOctets_initial;
			////System.out.println(OutOctets_final);
			half_duplex_bw = (((InOctets_final -InOctets_initial)   + (OutOctets_final - OutOctets_initial))*8*100)/(interval_segundos*Long.parseLong(Speed));
			////System.out.println("Total BW for Interface is: " + half_duplex_bw);
			} catch (Exception e) {
				System.out.println("Interrupted out of wait in Network BW Calculating" + e);
				half_duplex_bw = 0;
			}
		return half_duplex_bw;
	}
	
	
	public long network_bw (int j)
	{
		long half_duplex_bw = 0;
		try {
			////System.out.println("Calculating  BW... ");
			//long InOctets_initial = Long.parseLong(InOctets);
			//long OutOctets_initial = Long.parseLong(OutOctets);
			//int delta_interval = 60000;
			//int interval_segundos = delta_interval/1000;
			//sleep (delta_interval); 
			//long InOctets_final = Long.parseLong(getIfInOctets(j));
			//long OutOctets_final = Long.parseLong(getIfOutOctets(j));
			//long InOctets_delta = Long.parseLong(getIfInOctets(j)) - InOctets_initial;
			////System.out.println(InOctets_final);
			//long OutOctets_delta = Long.parseLong(getIfOutOctets(j)) - OutOctets_initial;
			////System.out.println(OutOctets_final);
			//half_duplex_bw = (((InOctets_final -InOctets_initial)   + (OutOctets_final - OutOctets_initial))*8*100)/(interval_segundos*Long.parseLong(Speed));
			////System.out.println("Total BW for Interface is: " + half_duplex_bw);
			} catch (Exception e) {
				System.out.println("Interrupted out of wait in Network BW Calculating" + e);
				half_duplex_bw = 0;
			}
		return half_duplex_bw;
	}

	public String [] cicle_network (int i) {
		String valoresTotales [] = new String [4];
		try {
		//valoresTotales [4] =  Long.toString(network_bw (i));
		valoresTotales [0] =  getIfLabel(i);
		valoresTotales [1] =  getIfSpeed(i);
		valoresTotales [2] =  getIfInOctets(i);
		valoresTotales [3] =  getIfOutOctets(i);
		} catch (Exception e) {
			System.out.println("Exception Cicle Network" + e);
		}
		return valoresTotales;
	}

	public Vector getNetworkParameters() {
		try { 
			String [] Indexs = getNetworkIfIndex ();
			int i = 0;
			errorFlag = 0;
			checkIfOctetsIDvality (Integer.parseInt(Indexs [i]));
			while (errorFlag != 1) {
				if (filteringNetworkDevices(Integer.parseInt(Indexs [i])) == 0)  //This method is for Hard Disk filter
					networkDetails.addElement(cicle_network(Integer.parseInt(Indexs [i])));	
				i ++;
				if (Indexs [i] == null) {
					errorFlag = 1;
				} 
				else { // print the values
					errorFlag = 0;
				}
			}
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting Network Parameters: " + ex.getMessage());
		}
		return networkDetails;
	}
	
	public Vector getNetworkParametersUnix() {
		try { 
			String [] Indexs = getNetworkIfIndexUnix ();
			int i = 0;
			errorFlag = 0;
			checkIfOctetsIDvality (Integer.parseInt(Indexs [i]));
			while (errorFlag != 1) {
				if (filteringNetworkDevices(Integer.parseInt(Indexs [i])) == 0)  //This method is for Hard Disk filter
					networkDetails.addElement(cicle_network(Integer.parseInt(Indexs [i])));	
				i ++;
				if (Indexs [i] == null) {
					errorFlag = 1;
				} 
				else { // print the values
					errorFlag = 0;
				}
			}
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting Network Parameters: " + ex.getMessage());
		}
		return networkDetails;
	}


	//old_value_h
	public int getOS ()
	{
		String oid = ".1.3.6.1.2.1.1.1.0";
		int getOS = 1;
		int idx = 0; 
		int tokenCount;
		int flag = 0;
		String temporal_string = null;
		String Label = "";
			
		temporal_string = this.snmpget(oid);

		
			//in case we obtain a null value we maintain the old value generated in the last iteration
			old_value_h=(temporal_string==null) ?old_value_h :temporal_string;  
			temporal_string=(temporal_string==null)? old_value_h :temporal_string;  

		// I need a method to check if this OID is valid and to know in which format it is.
		
		try
		 {
			Integer.parseInt(temporal_string.substring(0,1), 16);
			flag = 1;
		}
		catch ( Exception ex ) 
		{
			Label = temporal_string;
			System.out.println("Exception Getting Network Parameters: " + ex.getMessage());
		}
		
			
		if (flag == 1){	
		StringTokenizer st = new StringTokenizer(temporal_string);
		while (st.hasMoreTokens())
		{ 
			int token_int = Integer.parseInt(st.nextToken(), 16);
			String token_string = new Character((char)token_int).toString();
			//if (st.hasMoreTokens())		
			Label = Label + token_string;
		}
		}
		
		String words[] = new String [100];
		StringTokenizer st1 = new StringTokenizer(Label);
		tokenCount = st1.countTokens();
		while (st1.hasMoreTokens())
		{ words[idx] = st1.nextToken(); idx++; }
		for (idx=0;idx<tokenCount; idx++)
		{
			if (words [idx].equals("Linux"))
			{
				getOS = 0; //It means Linux
				////System.out.println("Operative System Linux: "+getOS);
				break;
			}
			else if (words [idx].equals("Windows")){
				getOS = 1; //It means Windows
				////System.out.println("Operative System Windows: " +getOS);
				break;
			}
			else if (words [idx].equals("Solaris")){
				getOS = 2; //It means Solaris
				////System.out.println("Operative System Solaris: " +getOS);
				break;
			}
			else if (words [idx].equals("Cisco")){
				getOS = 1; //It means Windows
				////System.out.println("Operative System Windows: " +getOS);
				break;
			}
		}
		return getOS;
	}

	/**
	 * This method makes a get of SNMP from the specific host and the provided oids. The parameters are:
	 * <PRE>
	 *   - String community: Community of SNMP. It can be 'public'
	 *   - String host: IP Address of the AP from which we want to retrive information.
	 *	 - String [] oids: an array of strings containing all the oids that we want to consult.
	 * </PRE>
	 * This method returns a String[ ] with the answers to the <i>SNMP Get</i>
	 */

	public void checkIfOctetsIDvality (int i) {
		String oid = ".1.3.6.1.2.1.2.2.1.10."+i;
		this.snmpget(oid);
	}

	//Private method
	private String snmpget(String oid) {
		
		//SnmpTarget target = new SnmpTarget ();
		String sOut; //Resulting Values
		int version = 2;
		// Use an SNMP target bean to perform SNMP operations
		if (host != null)
			target.setTargetHost( host );  // set the agent hostname
		if (comm != null) // set the community if specified
			target.setCommunity( comm );
		if (port != 0) // set the community if specified
			target.setTargetPort( port );
		
		if(version != 0) {  // if SNMP version is specified, set it
			if(version == 2){
				target.setSnmpVersion( SnmpTarget.VERSION2C ) ;}
			else if(version == 1)
				target.setSnmpVersion( SnmpTarget.VERSION1 );
			else if(version == 3)
				target.setSnmpVersion( SnmpTarget.VERSION3 );
			else {
				////System.out.println("Invalid Version Number");
			}
		}
		// Set the OID List on the SnmpTarget instance
		target.setObjectID(oid);
		//Calling SNMP Deamon
		sOut = target.snmpGet();
		if (sOut == null) {
			errorFlag = 1;
			//System.err.println("Request failed or timed out. \n"+
			//target.getErrorString());
		} 
		else { // print the values
			errorFlag = 0;
			////System.out.println("Response received.  Values:");		
			////System.out.println(target.getObjectID() + ": " + sOut);
		}
		System.gc();
		return sOut;
	}
}//Closing Class
