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

public class SblomarsMemory extends Thread {

	String [] systemID = null;
	static String comm;
	static String host;
	static int port;
	public long memCommitted;
	public long memCached;
	public long memAvailable;
	public long memTotal;
	public long memUsed;
	public int errorFlag = 0;
	
	

	Vector memoryDetails = new Vector();
	String old_value_a,old_value_b,old_value_h,old_value_i;
	String old_value_e,old_value_f,old_value_g,old_value_k;
	
	
	static SnmpTarget target;
	
	
	SblomarsMemory () {
		
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
	public String getMemoryAvailable() {
		String oid = ".1.3.6.1.4.1.9600.1.1.2.1.0";
		String temporal_value = null;
		try {
			temporal_value = this.snmpget(oid);
			
			//in case we obtain a null value we maintain 
			//the old value generated in the last iteration
			old_value_a=(temporal_value==null) ?old_value_a :temporal_value;  
			temporal_value=(temporal_value==null)? old_value_a :temporal_value;  
			
			memAvailable = Integer.parseInt(temporal_value);
			////System.out.println("Memory Available: " + memAvailable);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting Memory Available: " + ex.getMessage());
			temporal_value = "0";
		}
		return temporal_value;
	}
	
	//old_value_b
	public String getMemoryUsed() {
		String oid = ".1.3.6.1.4.1.9600.1.1.2.4.0";
		String temporal_value = null;
		try {
			temporal_value = this.snmpget(oid);
			
			//in case we obtain a null value we maintain the old value generated in the last iteration
			old_value_b=(temporal_value==null) ? old_value_b : temporal_value;  
			temporal_value=(temporal_value==null) ? old_value_b : temporal_value;  
			
			memCommitted = Long.parseLong(temporal_value);
			////System.out.println("Memory Used: " + memCommitted);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting Memory Used: " + ex.getMessage());
			temporal_value = "0";
		}
		return temporal_value;
	}

	
	public String getMemoryUsedUnix() {
		try {
			//memTotal = Integer.parseInt(getMemoryTotal());
			memUsed = Long.parseLong(getMemoryTotal()) - (getMemoryRealUnix()*1024 + getMemoryBufferUnix()*1024 + getMemoryCachedUnix()*1024);
			//memUsed = (getMemoryRealUnix() + getMemoryBufferUnix() + getMemoryCachedUnix());
			////System.out.println("Memory Used: "+memUsed);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting Memory Used Unix: " + ex.getMessage());
			memUsed = 0;
		}
		return Long.toString(memUsed);
	}

	
	public String getMemoryAvailableUnix() {
		try {
			memTotal = Integer.parseInt(getMemoryTotal());
			memAvailable = memTotal - memUsed;
			////System.out.println("Memory Available: "+memAvailable);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting Memory Available Unix: " + ex.getMessage());
			memAvailable = 0;
		}
		return Long.toString(memAvailable);
	}

	//old_value_e
	public long getMemoryRealUnix() {
		String oid = ".1.3.6.1.4.1.2021.4.6.0";
		try {
			String temporal_value = this.snmpget(oid);
			
			//in case we obtain a null value we maintain the old value generated in the last iteration
			old_value_e=(temporal_value==null) ? old_value_e : temporal_value;  
			temporal_value=(temporal_value==null) ? old_value_e : temporal_value;  
		
			memAvailable = Integer.parseInt(temporal_value);
			////System.out.println("Memory Avail Real: "+memAvailReal);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting Memory Real Unix: " + ex.getMessage());
			memAvailable = 0;
		}
		return memAvailable;
	}
	
	//old_value_f
	public long getMemoryBufferUnix() {
		String oid = ".1.3.6.1.4.1.2021.4.14.0";
		long memBuffer = 0;
		try {
			String temporal_value = this.snmpget(oid);
			
			//in case we obtain a null value we maintain the old value generated in the last iteration
			old_value_f=(temporal_value==null) ? old_value_f : temporal_value;  
			temporal_value=(temporal_value==null) ? old_value_f : temporal_value;  
			
			memBuffer = Long.parseLong(temporal_value);
			////System.out.println("Memory Buffer: "+memBuffer);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting Memory Buffer Unix: " + ex.getMessage());
			memBuffer = 0;
		}
		return memBuffer;
	}

	//old_value_g
	public long getMemoryCachedUnix() {
		String oid = ".1.3.6.1.4.1.2021.4.15.0";
		try {
			String temporal_value = this.snmpget(oid);
			
			//in case we obtain a null value we maintain the old value generated in the last iteration
			old_value_g=(temporal_value==null) ? old_value_g : temporal_value;  
			temporal_value=(temporal_value==null) ? old_value_g : temporal_value;  
			
			memCached = Integer.parseInt(temporal_value);
			////System.out.println("Memory Cached: "+memCached);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting Memory Cached Unix: " + ex.getMessage());
			memCached = 0;
		}
		return memCached;
	}
	
	//old_value_h
	public String getMemoryCached() {
		String oid = ".1.3.6.1.4.1.9600.1.1.2.5.0";
		String temporal_value = null;
		try {
			temporal_value = this.snmpget(oid);
			
			//in case we obtain a null value we maintain the old value generated in the last iteration
			old_value_h=(temporal_value==null) ?old_value_h : temporal_value;  
			temporal_value=(temporal_value==null) ? old_value_h : temporal_value;  
			
			memCached = Integer.parseInt(temporal_value);
			////System.out.println("Memory Available: " + memCached);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting Memory Cached: " + ex.getMessage());
			temporal_value = "0";
		}
		return temporal_value;
	}

	//old_value_i
	public String getMemoryTotal() {
		String oid = ".1.3.6.1.2.1.25.2.2.0";
		String temporal_value = null;
		try {
			temporal_value = this.snmpget(oid);
			
			//in case we obtain a null value we maintain the old value generated in the last iteration
			old_value_i=(temporal_value==null) ?old_value_i : temporal_value;  
			temporal_value=(temporal_value==null) ? old_value_i : temporal_value;  
			
			memTotal = Long.parseLong(temporal_value)*1024;
			temporal_value = Long.toString(memTotal);
			////System.out.println("Memory Total: " + memTotal);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting Memory Total: " + ex.getMessage());
			temporal_value = "0";
		}
		return temporal_value;
	}
	
	public String [] cicle_memory () {
		String valoresTotales [] = new String [4];
		try {
		valoresTotales [0] =  getMemoryTotal();
		valoresTotales [1] =  getMemoryAvailable();
		valoresTotales [2] =  getMemoryUsed();
		valoresTotales [3] = Long.toString((Long.parseLong(valoresTotales [2])*100)/(Long.parseLong(valoresTotales [0])));
		////System.out.println("Memory Used Percent: "+valoresTotales [3]);
		}
		catch ( Exception ex ) {
			System.out.println("Exception Cicle Memory Total: " + ex.getMessage());
		}
		return valoresTotales;
	}

	
	public String [] cicle_memory_unix () {
		String valoresTotales [] = new String [4];
		try {
		valoresTotales [0] =  getMemoryTotal();
		valoresTotales [1] =  getMemoryAvailableUnix();
		valoresTotales [2] =  getMemoryUsedUnix();
		valoresTotales [3] = Long.toString((Long.parseLong(valoresTotales [2])*100)/(Long.parseLong(valoresTotales [0])));
		////System.out.println("Memory Used Percent: "+valoresTotales [3]);
		}
		catch ( Exception ex ) {
			System.out.println("Exception Cicle Memory Total: " + ex.getMessage());
		}
		return valoresTotales;
	}

	public Vector getMemoryParameters() {
		try { 
			memoryDetails.addElement(cicle_memory());	
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting Memory Parameters: " + ex.getMessage());
		}
		return memoryDetails;
	}

	public Vector getMemoryParametersUnix() {
		try { 
			memoryDetails.add(0, cicle_memory_unix());	
			if (memoryDetails.elementAt(1) != null)
				memoryDetails.remove(1);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting Memory Unix Parameters: " + ex.getMessage());
		}
		return memoryDetails;
	}

	//old_value_k
	public int getOS ()
	{
			
		String oid = ".1.3.6.1.2.1.1.1.0";
		int getOS = 1;
		int idx = 0; 
		int tokenCount;
		String temporal_string = null;
		temporal_string = this.snmpget(oid);
		
		//in case we obtain a null value we maintain the old value generated in the last iteration
		old_value_k=(temporal_string==null) ? old_value_k : temporal_string;  
		temporal_string=(temporal_string==null) ? old_value_k : temporal_string;  
			
		String words[] = new String [100];
		StringTokenizer st = new StringTokenizer(temporal_string);
		tokenCount = st.countTokens();
		while (st.hasMoreTokens())
		{ words[idx] = st.nextToken(); idx++; }
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
