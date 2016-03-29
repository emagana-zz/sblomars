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

public class SblomarsProcessor extends Thread {

	String [] systemID = null;
	static String comm;
	static String host;
	static int port;
	String cpuModel;
	public int cpuUsed;
	public int errorFlag = 0;

	Vector cpuDetails = new Vector();
	
	static SnmpTarget target;
	
	
	//variables for saving the valid values
	String old_value_b,old_value_c,old_value_d;
	String old_value_e,old_value_f,old_value_g,old_value_h,old_value_k;
	
	
	SblomarsProcessor () {
		
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
	
	//old_value_b
	public String getCpuModel() {
		String oid = ".1.3.6.1.2.1.1.1.0";
		try {
			cpuModel = this.snmpget(oid);
			//This is for Windows
			//cpuModel = cpuModel.substring(10, 47);
			////System.out.println("Model CPU: "+cpuModel);
			
			//in case we obtain a null value we maintain the old value generated in the last iteration
			old_value_b=(cpuModel==null) ? old_value_b : cpuModel;  
			cpuModel=(cpuModel==null) ? old_value_b : cpuModel;  
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting CPU Model: " + ex.getMessage());
			cpuModel = "0";
		}
		return cpuModel;
	}
	
	
	//old_value_c
	public String getCpuSystem() {
		String oid = ".1.3.6.1.2.1.25.5.1.1.1.1";
		String temporal_value = "";
		try {
			temporal_value = this.snmpget(oid);
			
			//in case we obtain a null value we maintain the old value generated in the last iteration
			old_value_c=(temporal_value==null) ? old_value_c : temporal_value;  
			temporal_value=(temporal_value==null) ? old_value_c : temporal_value;  
		
			
			////System.out.println("CPU Used: "+ssCpuSystem);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting CPU Raw System: " + ex.getMessage());
		}
			
		return temporal_value;
	}
	
	//old_value_d
	public String getCpuUsedPercentage() {
		String oid = ".1.3.6.1.4.1.9600.1.1.5.1.5.1.48";
		String temporal_value = null;
		try {
			temporal_value = this.snmpget(oid);
			
			//in case we obtain a null value we maintain the old value generated in the last iteration
			old_value_d=(temporal_value==null) ? old_value_d : temporal_value;  
			temporal_value=(temporal_value==null) ? old_value_d : temporal_value;  

			cpuUsed = Integer.parseInt(temporal_value);
			////System.out.println("Percentage CPU Used: "+cpuUsed);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting CPU Used: " + ex.getMessage());
		}
		if (temporal_value == null)
			temporal_value = "0";
		return temporal_value;
	}

	//old_value_e
	public long getCpuRawUser() {
		String oid = ".1.3.6.1.4.1.2021.11.50.0";
		long ssCpuSystem = 0;
		try {
			String temporal_value = this.snmpget(oid);
			
			//in case we obtain a null value we maintain the old value generated in the last iteration
			old_value_e=(temporal_value==null) ? old_value_e : temporal_value;  
			temporal_value=(temporal_value==null) ? old_value_e : temporal_value;  
			
			ssCpuSystem = Long.parseLong(temporal_value);
			////System.out.println("CPU Used: "+ssCpuSystem);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting CPU Raw System: " + ex.getMessage());
			ssCpuSystem = 0;
		}
			
		return ssCpuSystem;
	}
	
	//old_value_f
	public long getCpuRawNice() {
		String oid = ".1.3.6.1.4.1.2021.11.51.0";
		long ssCpuSystem = 0;
		try {
			String temporal_value = this.snmpget(oid);
			
			//in case we obtain a null value we maintain the old value generated in the last iteration
			old_value_f=(temporal_value==null) ?  old_value_f : temporal_value;  
			temporal_value=(temporal_value==null) ? old_value_f : temporal_value;  

			ssCpuSystem = Long.parseLong(temporal_value);
			////System.out.println("CPU Used: "+ssCpuSystem);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting CPU Raw System: " + ex.getMessage());
			ssCpuSystem = 0;
		}
			
		return ssCpuSystem;
	}
	
	//old_value_g
	public long getCpuRawSystem() {
		String oid = ".1.3.6.1.4.1.2021.11.52.0";
		long ssCpuSystem = 0;
		try {
			String temporal_value = this.snmpget(oid);
			
			//in case we obtain a null value we maintain the old value generated in the last iteration
			old_value_g=(temporal_value==null) ? old_value_g : temporal_value;  
			temporal_value=(temporal_value==null) ? old_value_g : temporal_value;  

			ssCpuSystem = Long.parseLong(temporal_value);
			////System.out.println("CPU Used: "+ssCpuSystem);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting CPU Raw System: " + ex.getMessage());
			ssCpuSystem = 0;
		}
			
		return ssCpuSystem;
	}
	
	
	//old_value_h
	public long getCpuRawIdle() {
		String oid = ".1.3.6.1.4.1.2021.11.53.0";
		long ssCpuSystem = 0;
		try {
			String temporal_value = this.snmpget(oid);
			
				//in case we obtain a null value we maintain the old value generated in the last iteration
			old_value_h=(temporal_value==null) ? old_value_h : temporal_value;  
			temporal_value=(temporal_value==null) ? old_value_h : temporal_value;  
			
			ssCpuSystem = Long.parseLong(temporal_value);
			
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting CPU Raw System: " + ex.getMessage());
			ssCpuSystem = 0;
		}
			
		return ssCpuSystem;
	}
	
	public String getCpuUsedUnixPercentage() {
		double ssCpuUsed =0;
		try {
			long temporal1_getCpuRawUser = getCpuRawUser ();
			long temporal1_getCpuRawNice = getCpuRawNice ();
			long temporal1_getCpuRawSystem = getCpuRawSystem ();
			long temporal1_getCpuRawIdle = getCpuRawIdle ();
			sleep (10000); 
			long temporal2_getCpuRawUser = getCpuRawUser ();
			long temporal2_getCpuRawNice = getCpuRawNice ();
			long temporal2_getCpuRawSystem = getCpuRawSystem ();
			long temporal2_getCpuRawIdle = getCpuRawIdle ();
			ssCpuUsed = (temporal2_getCpuRawUser - temporal1_getCpuRawUser)*100 /
				((temporal2_getCpuRawUser - temporal1_getCpuRawUser)+(temporal2_getCpuRawNice - temporal1_getCpuRawNice)
						+(temporal2_getCpuRawSystem - temporal1_getCpuRawSystem)+(temporal2_getCpuRawIdle - temporal1_getCpuRawIdle));
			////System.out.println("CPU Percentage Used: "+ssCpuUsed);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting CPU Used Unix: " + ex.getMessage());
			ssCpuUsed = 0;
		}
		return Double.toString(ssCpuUsed);
	}

	public String [] ciclo_cpu () {
		String valoresTotales [] = new String [3];
		try {
		valoresTotales [0] =  getCpuModel();
		valoresTotales [1] =  getCpuSystem();
		valoresTotales [2] =  getCpuUsedPercentage();
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Cicle CPU Used Unix: " + ex.getMessage());
		}
		return valoresTotales;
	}
	
	
	public String [] ciclo_cpu_unix () {
		String valoresTotales [] = new String [3];
		try {
		valoresTotales [0] =  getCpuModel();
		valoresTotales [1] =  Long.toString(getCpuRawUser());
		valoresTotales [2] =  getCpuUsedUnixPercentage();
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Cicle CPU Used Unix: " + ex.getMessage());
		}
		return valoresTotales;
	}

	/**
	 * @author Edgar Magana
	 * Method to get information regarding Memory
	 * Creates the Vector with all the information for each memory on the system
	 */
	public Vector getCpuParameters() {
		try { 
			cpuDetails.addElement(ciclo_cpu());	
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting CPU Parameters: " + ex.getMessage());
		}
		return cpuDetails;
	}

	public Vector getCpuParametersUnix() {
		try { 
			cpuDetails.add(0, ciclo_cpu_unix());	
			if (cpuDetails.elementAt(1) != null)
				cpuDetails.remove(1);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting CPU Unix Parameters: " + ex.getMessage());
		}
		return cpuDetails;
		
	}

	//old_value_k
	public int getOS ()
	{
		String oid = ".1.3.6.1.2.1.1.1.0";
		int getOS = 1;
		int idx = 0; 
		int tokenCount;
		String temporal_string = null;
		try { 
			temporal_string = this.snmpget(oid);  // I need to add a default value
			
				//in case we obtain a null value we maintain the old value generated in the last iteration
			old_value_k=(temporal_string==null) ? old_value_k : temporal_string;  
			temporal_string=(temporal_string==null) ? old_value_k : temporal_string;  
			

		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting OS: " + ex.getMessage());
		}
		//System.out.println("OS: " + temporal_string);
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
			//errorFlag = 1;
			//System.err.println("Request failed or timed out. \n"+
			//target.getErrorString());
		} 
		else { // print the values
			//errorFlag = 0;
			////System.out.println("Response received.  Values:");		
			////System.out.println(target.getObjectID() + ": " + sOut);
		}
		System.gc();
		return sOut;
	}
}//Closing Class
