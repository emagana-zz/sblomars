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

public class SblomarsSoftware extends Thread {

	String [] systemID = null;
	static String comm;
	static String host;
	static int port;
	public String softwareName;
	public String softwareInstalledDate;
	public int errorFlag = 0;

	Vector softwareDetails = new Vector();
	
	static SnmpTarget target;
	static String old_value_a,old_value_b;
	
	SblomarsSoftware () {
		
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
	public String getSoftwareName(int i) {
		String oid = ".1.3.6.1.2.1.25.6.3.1.2."+i;
		String Label = "";
		String temporal_string = "";
		try {
			softwareName = this.snmpget(oid); 
			
			//in case we obtain a null value we maintain the old value generated in the last iteration
			old_value_a=(softwareName==null) ? old_value_a : softwareName;  
			softwareName=(softwareName==null) ? old_value_a :softwareName;  
			
			temporal_string = softwareName;
			StringTokenizer st = new StringTokenizer(temporal_string);
			while (st.hasMoreTokens())
			{ 
				int token_int = Integer.parseInt(st.nextToken(), 16);
				String token_string = new Character((char)token_int).toString();
				//if (st.hasMoreTokens())		
				Label = Label + token_string;
			}
			softwareName = Label;
		}
		catch ( Exception ex ) 
		{
			System.out.println("Problems Getting Software Name: " + ex.getMessage());
			//softwareName = "";
		}
		////System.out.println("Software Name: " + softwareName);
		return softwareName;
	}
	
	//old_value_b
	public String getSoftwareInstalledDate(int i) {
		String oid = ".1.3.6.1.2.1.25.6.3.1.5."+i;
		String Label = "";
		String temporal_string = "";
		int counter = 0;
		try {
			softwareInstalledDate = this.snmpget(oid);
			
			//in case we obtain a null value we maintain the old value generated in the last iteration
			old_value_b=(softwareInstalledDate==null) ? old_value_b : softwareInstalledDate;  
			softwareInstalledDate=(softwareInstalledDate==null) ? old_value_b : softwareInstalledDate;  
	
			temporal_string = softwareInstalledDate;
			StringTokenizer st = new StringTokenizer(temporal_string);
			while (st.hasMoreTokens())
			{ 
				String token_string = "";
				if (Label == "") {
					counter = 2;
					while (counter != 0) 
					{
						token_string = token_string + st.nextToken();
						counter --;
					}
					token_string = Integer.toString(Integer.parseInt(token_string, 16));
				}
				else {
					token_string = Integer.toString(Integer.parseInt(st.nextToken(), 16));
				}
				if (st.hasMoreTokens())		
					Label = Label + token_string;
			}
			softwareInstalledDate = Label;
		}
		catch ( Exception ex ) 
		{
			System.out.println("Problems Getting Software Date: " + ex.getMessage());
			softwareInstalledDate = "";
		}
		////System.out.println("Software Installed Date: " + softwareInstalledDate);
		return softwareInstalledDate;
	}

	public String [] cicle_software (int i) {
		String valoresTotales [] = new String [2];
		try {
		valoresTotales [0] =  getSoftwareName(i);
		valoresTotales [1] =  getSoftwareInstalledDate(i);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Problems Cicle Software Date: " + ex.getMessage());
		}
		return valoresTotales;
	}

	/**
	 * @author Edgar Magana
	 * Method to get information regarding Software
	 * Creates the Vector with all the information for each memory on the system
	 */

	public Vector getSoftwareParameters() {
		try { 
			int i = 1;
			checkSoftwareOIDvality (i); //Check if the OID is valid or not
			while (errorFlag != 1) {
				softwareDetails.addElement(cicle_software(i));	//JM:softwareDetails is a publicvector
				i++;
				checkSoftwareOIDvality (i);
			}	
		}	
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting Software Parameters: " + ex.getMessage());
		}
		return softwareDetails;
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

	public void checkSoftwareOIDvality (int i) {
		String oid = ".1.3.6.1.2.1.25.6.3.1.2."+i;
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
