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

public class SblomarsStorage extends Thread {

	String [] systemID = null;
	static String comm;
	static String host;
	static int port;
	public int errorFlag = 0;

	Vector storageDetails = new Vector();
	
	static SnmpTarget target;
	
	static String old_value_a,old_value_b,old_value_c,old_value_d,old_value_f;
	
	SblomarsStorage () {
		
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
	public String getHddUsed(int i) {
		String oid = ".1.3.6.1.2.1.25.2.3.1.6."+i;
		String HddUsed = null;
		try { 
			String temporal_string = this.snmpget(oid);
			
			//in case we obtain a null value we maintain the old value generated in the last iteration
			old_value_a=(temporal_string==null) ? old_value_a : temporal_string;  
			temporal_string=(temporal_string==null) ? old_value_a : temporal_string;  
			
			long temporal_int = Integer.parseInt(temporal_string);
			temporal_string = getStorageAllocationUnits(i);
			long temporal_int2 = Integer.parseInt(temporal_string);
			temporal_int = temporal_int*temporal_int2;
			HddUsed = Long.toString(temporal_int);
			////System.out.println("getHddUsed: "+HddUsed);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting HDD Used: " + ex.getMessage());
			HddUsed = "0";
		}
		return HddUsed;
	}

	//old_value_b
	public String getHddSize(int i) {
		String oid = ".1.3.6.1.2.1.25.2.3.1.5."+i;
		String HddSize = null;
		try { 
			String temporal_string = this.snmpget(oid);
			
			//in case we obtain a null value we maintain the old value generated in the last iteration
			old_value_b=(temporal_string==null) ? old_value_b : temporal_string;  
			temporal_string=(temporal_string==null) ? old_value_b : temporal_string;  
			
			long temporal_int = Integer.parseInt(temporal_string);
			temporal_string = getStorageAllocationUnits(i);
			long temporal_int2 = Integer.parseInt(temporal_string);
			temporal_int = temporal_int*temporal_int2;
			HddSize = Long.toString(temporal_int);
			////System.out.println("getHddSize: "+HddSize);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting HDD Size: " + ex.getMessage());
			HddSize = "0";
		}
		return HddSize;
	}

	//old_value_c
	public String getStorageAllocationUnits(int i) {
		String oid = ".1.3.6.1.2.1.25.2.3.1.4."+i;
		String StorageAllocationUnits = null;
		try { 
			StorageAllocationUnits = this.snmpget(oid);
			
			//in case we obtain a null value we maintain the old value generated in the last iteration
			old_value_c=(StorageAllocationUnits==null) ? old_value_c : StorageAllocationUnits;  
			StorageAllocationUnits=(StorageAllocationUnits==null) ? old_value_c : StorageAllocationUnits;  
			
			////System.out.println("StorageAllocationUnits: "+StorageAllocationUnits);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting Storage Allocation Units: " + ex.getMessage());
			StorageAllocationUnits = "0";
		}
		return StorageAllocationUnits;
	}

	//old_value_d
	public String getHddLabel(int i) {
		String oid = ".1.3.6.1.2.1.25.2.3.1.3."+i;
		String HddLabel = null;
		try { 
			//String temporal_string = this.snmpget(2,comm,host,oid);
			//HddLabel = temporal_string.substring(0, 2);
			// This is for Linux
			HddLabel = this.snmpget(oid);
			
				//in case we obtain a null value we maintain the old value generated in the last iteration
			old_value_d=(HddLabel==null) ? old_value_d : HddLabel;  
			HddLabel=(HddLabel==null) ? old_value_d : HddLabel;  
						
			////System.out.println("HddLabel: "+HddLabel);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting HDD Label: " + ex.getMessage());
			HddLabel = "No label";
		}
		return HddLabel;
	}
	
	//old_value_f
	public int filteringHddDevices(int i) {
		String oid = ".1.3.6.1.2.1.25.2.3.1.2."+i;
		String HddDeviceType = null;
		int flag = 1;
		try { 
			HddDeviceType = this.snmpget(oid);
			
			//in case we obtain a null value we maintain the old value generated in the last iteration
			old_value_f=(HddDeviceType==null) ? old_value_f : HddDeviceType;  
			HddDeviceType=(HddDeviceType==null) ? old_value_f : HddDeviceType;  
					
			int index_typeofStorageElement = Integer.parseInt(HddDeviceType.substring(20));
			switch (index_typeofStorageElement) { 
		    case 4: 
		    	flag = 0;
		    case 5: 
		    	flag = 0;
		    break;
		}
			
			//if (index_typeofStorageElement == 4)
				//flag = 0;
			/*int coso = HddDeviceType.compareTo(".1.3.6.1.2.1.25.2.1.4");
			
			
			if (coso == 0) {
				flag = 0;
				coso = HddDeviceType.compareTo(".1.3.6.1.2.1.25.2.1.5"); 
				}
			else if (coso == 0)
				flag = 0;
			else
				flag = 1;*/
			//flag = HddDeviceType.compareTo(".1.3.6.1.2.1.25.2.1.4");
			////System.out.println("Device Type: "+HddDeviceType);
			////System.out.println("Flag: "+flag);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Filtering HDD Devices: " + ex.getMessage());
		}
		return flag;
	}
	
	
	public String [] cicle_hdd (int i) {
		String valoresTotales [] = new String [5];
		try { 
		valoresTotales [0] =  getHddLabel(i);
		valoresTotales [1] =  getHddSize(i);
		valoresTotales [2] =  getHddUsed(i);
		long temporal_value = Long.parseLong(valoresTotales [1]) - Long.parseLong(valoresTotales [2]);
		valoresTotales [3] =   Long.toString(temporal_value);
		valoresTotales [4] = Long.toString((Long.parseLong(valoresTotales [2])*100)/(Long.parseLong(valoresTotales [1])));
		////System.out.println("Hdd Used Percent: "+valoresTotales [4]);
		
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Cicle HDD: " + ex.getMessage());
			valoresTotales [0] = "No label";
			valoresTotales [1] = "0";
			valoresTotales [2] = "0";
			valoresTotales [3] = "0";
			valoresTotales [4] = "0";
		}
		return valoresTotales;
	}
	
	/**
	 * @author Edgar Magana
	 * Method to get information regarding Storage 
	 */
	public Vector getHddParameters() {
		try { 
			int i = 1;
			checkHddOIDvality (i); //Check if the OID is valid or not
			while (errorFlag != 1) {
				if (filteringHddDevices(i) == 0)  //This method is for Hard Disk filter
					if (getHddSize(i).compareTo("0") != 0)
						storageDetails.addElement(cicle_hdd(i));	
				i++;
				checkHddOIDvality (i);
			}	
		}	
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting HDD Parameters: " + ex.getMessage());
		}
		return storageDetails;
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

	public void checkHddOIDvality (int i) {
		String oid = ".1.3.6.1.2.1.25.2.3.1.4."+i;
		String Hddvality = null;
		
		
		try { 
			String temporal_string = this.snmpget(oid);
			
			if(temporal_string!=null){
				int temporal_int = Integer.parseInt(temporal_string);
			}
			//System.out.println("getHddSize: ");
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting HDD Size: " + ex.getMessage());
			
		}
		
		
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
