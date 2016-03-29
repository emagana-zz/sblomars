/**
 *  Agent getting resources information
 *
 *     @author     Edgar Magana
 *     @version    1.2
 *     @see		   jdk 1.5.0_06
 */

package source;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import com.adventnet.snmp.beans.*;

public class SblomarsNetworkLevel extends Thread {

	String [] systemID = null;
	static String comm;
	static String host;
	static int port;
	static int probeID;
	public int errorFlag = 0;
	public int flagID = 0;
	public String sOutID = "";
	public String subOID = "";

	Vector networklevelDetails = new Vector();
	
	static SnmpTarget target;
	
	
	static String old_value_a,old_value_b,old_value_c,old_value_d,old_value_e,old_value_f,old_value_g,old_value_h;
	static String old_value_i,old_value_j,old_value_k,old_value_l,old_value_m,old_value_n,old_value_o,old_value_p,old_value_q,old_value_r,old_value_s,old_value_t,old_value_u,old_value_v,old_value_w;	
		
	SblomarsNetworkLevel () {
		
		target = new SnmpTarget ();
		
	}

	public String [] startingHosts (String configuration_file) {
		GetHostCommunity thread = new GetHostCommunity ();
		try {
			systemID = thread.initFromFile(configuration_file);
			comm = systemID [0];
			host = systemID [1]; 
			port = Integer.parseInt(systemID [2]); 
			probeID = Integer.parseInt(systemID [3]);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting Host Information: " + ex.getMessage());
		}
		return systemID;
		//target = new SnmpTarget ();
	}
	
	//old_value_a
	public String getTOSValue() {
		String oid = ".1.3.6.1.4.1.9.9.42.1.2.2.1.9." + probeID;
		String temporal_value = "";
		try {
			temporal_value = this.snmpget(oid);
			
			//in case we obtain a null value we maintain 
			//the old value generated in the last iteration
			old_value_a=(temporal_value==null) ?old_value_a :temporal_value;  
			temporal_value=(temporal_value==null)? old_value_a :temporal_value;  

			//System.out.println("TOS Value: " + temporal_value);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting TOS Value: " + ex.getMessage());
		
		}
			
		return temporal_value;
	}
	
	//old_value_b	
	public String getNumPackets() {
		String oid = ".1.3.6.1.4.1.9.9.42.1.2.2.1.18." + probeID;
		String temporal_value = "";
		try {
			temporal_value = this.snmpget(oid);
	
			//in case we obtain a null value we maintain 
			//the old value generated in the last iteration
			old_value_b=(temporal_value==null) ?old_value_b :temporal_value;  
			temporal_value=(temporal_value==null)? old_value_b :temporal_value;  
			
			//System.out.println("NumPacket: " + temporal_value);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting NumPackets: " + ex.getMessage());
		
		}
			
		return temporal_value;
	}
	
	//old_value_c
	public String getFrequency() {
		String oid = ".1.3.6.1.4.1.9.9.42.1.2.1.1.6." + probeID;
		String temporal_value = "";
		try {
			temporal_value = this.snmpget(oid);
			
			//in case we obtain a null value we maintain 
			//the old value generated in the last iteration
			old_value_c=(temporal_value==null) ?old_value_c :temporal_value;  
			temporal_value=(temporal_value==null)? old_value_c :temporal_value;  
		
			//System.out.println("Frequency: "+ temporal_value);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting Frequency: " + ex.getMessage());
		
		}
			
		return temporal_value;
	}
	
	//old_value_d
	public String getPktDataRequestSize() {
		String oid = ".1.3.6.1.4.1.9.9.42.1.2.2.1.3." + probeID;
		String temporal_value = "";
		try {
			temporal_value = this.snmpget(oid);
			
			//in case we obtain a null value we maintain 
			//the old value generated in the last iteration
			old_value_d=(temporal_value==null) ?old_value_d :temporal_value;  
			temporal_value=(temporal_value==null)? old_value_d :temporal_value;  
	
			//System.out.println("PktDataRequestSize: " +temporal_value);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting PktDataRequestSize: " + ex.getMessage());
		
		}
			
		return temporal_value;
	}
	
	//old_value_e
	public String getPacketLossDS() {
		String oid = ".1.3.6.1.4.1.9.9.42.1.3.5.1.35." + probeID + "." + subOID;
		String temporal_value = "";
		try {
			temporal_value = this.snmpget(oid);

			//in case we obtain a null value we maintain 
			//the old value generated in the last iteration
			old_value_e=(temporal_value==null) ?old_value_e :temporal_value;  
			temporal_value=(temporal_value==null)? old_value_e :temporal_value;  

			//System.out.println("PacketLossDS: " +temporal_value);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting PacketLossDS: " + ex.getMessage());
		}
		return temporal_value;
	}
	
	//old_value_f	
	public String getPacketLossSD() {
		String oid = ".1.3.6.1.4.1.9.9.42.1.3.5.1.34." + probeID + "." + subOID;
		String temporal_value = "";
		try {
			temporal_value = this.snmpget(oid);

			//in case we obtain a null value we maintain 
			//the old value generated in the last iteration
			old_value_f=(temporal_value==null) ?old_value_f :temporal_value;  
			temporal_value=(temporal_value==null)? old_value_f :temporal_value;  

			//System.out.println("PacketLossSD: " +temporal_value);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting PacketLossSD: " + ex.getMessage());
		}
		return temporal_value;
	}
	
	//old_value_g
	public String getPacketMIA() {
		String oid = ".1.3.6.1.4.1.9.9.42.1.3.5.1.37." + probeID + "." + subOID;
		String temporal_value = "";
		try {
			temporal_value = this.snmpget(oid);

			//in case we obtain a null value we maintain 
			//the old value generated in the last iteration
			old_value_g=(temporal_value==null) ?old_value_g :temporal_value;  
			temporal_value=(temporal_value==null)? old_value_g :temporal_value;  
		
			//System.out.println("Packet MIA: " +temporal_value);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting Packet MIA: " + ex.getMessage());
		}
		return temporal_value;
	}
	
	//old_value_h
	public String getPacketLateArrival() {
		String oid = ".1.3.6.1.4.1.9.9.42.1.3.5.1.38." + probeID + "." + subOID;
		String temporal_value = "";
		try {
			temporal_value = this.snmpget(oid);

			//in case we obtain a null value we maintain 
			//the old value generated in the last iteration
			old_value_h=(temporal_value==null) ?old_value_h :temporal_value;  
			temporal_value=(temporal_value==null)? old_value_h :temporal_value;  

			//System.out.println("PacketLateArrival: " +temporal_value);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting PacketLateArrival: " + ex.getMessage());
		}
		return temporal_value;
	}
	
	//old_value_i
	public String getPacketOutOfSequence() {
		String oid = ".1.3.6.1.4.1.9.9.42.1.3.5.1.36." + probeID + "." + subOID;
		String temporal_value = "";
		try {
			temporal_value = this.snmpget(oid);
	
			//in case we obtain a null value we maintain 
			//the old value generated in the last iteration
			old_value_i=(temporal_value==null) ?old_value_i :temporal_value;  
			temporal_value=(temporal_value==null)? old_value_i :temporal_value;  
				
			//System.out.println("PacketOutOfSequence: " +temporal_value);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting PacketOutOfSequence: " + ex.getMessage());
		}
		return temporal_value;
	}
	
	//old_value_j
	public String getRTTSum() {
		String oid = ".1.3.6.1.4.1.9.9.42.1.3.5.1.5." + probeID + "." + subOID;
		String temporal_value = "";
		try {
			temporal_value = this.snmpget(oid);

			//in case we obtain a null value we maintain 
			//the old value generated in the last iteration
			old_value_j=(temporal_value==null) ?old_value_j :temporal_value;  
			temporal_value=(temporal_value==null)? old_value_j :temporal_value;  

			//System.out.println("getRTTSum: " +temporal_value);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting getRTTSum: " + ex.getMessage());
		}
		return temporal_value;
	}
	
	//old_value_k
	public String getNumOfRTT() {
		String oid = ".1.3.6.1.4.1.9.9.42.1.3.5.1.4." + probeID + "." + subOID;
		String temporal_value = "";
		try {
			temporal_value = this.snmpget(oid);

			//in case we obtain a null value we maintain 
			//the old value generated in the last iteration
			old_value_k=(temporal_value==null) ?old_value_k :temporal_value;  
			temporal_value=(temporal_value==null)?old_value_k :temporal_value;  

			//System.out.println("getNumOfRTT: " +temporal_value);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting getNumOfRTT: " + ex.getMessage());
		}
		return temporal_value;
	}
	
	//old_value_l
	public String getOWSumSD() {
		String oid = ".1.3.6.1.4.1.9.9.42.1.3.5.1.41." + probeID + "." + subOID;
		String temporal_value = "";
		try {
			temporal_value = this.snmpget(oid);

			//in case we obtain a null value we maintain 
			//the old value generated in the last iteration
			old_value_l=(temporal_value==null) ?old_value_l :temporal_value;  
			temporal_value=(temporal_value==null)? old_value_l :temporal_value;  
		
			//System.out.println("getOWSumSD: " +temporal_value);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting getOWSumSD: " + ex.getMessage());
		}
		return temporal_value;
	}
	
	//old_value_m
	public String getNumOfOW() {
		String oid = ".1.3.6.1.4.1.9.9.42.1.3.5.1.51." + probeID + "." + subOID;
		String temporal_value = "";
		try {
			temporal_value = this.snmpget(oid);

			//in case we obtain a null value we maintain 
			//the old value generated in the last iteration
			old_value_m=(temporal_value==null) ?old_value_m :temporal_value;  
			temporal_value=(temporal_value==null)? old_value_m :temporal_value;  
			//DEbugging end add test- 05-05-2009

			//System.out.println("getNumOfOW: " +temporal_value);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting getNumOfOW: " + ex.getMessage());
		}
		return temporal_value;
	}
	
	//old_value_n	
	public String getOWSumDS() {
		String oid = ".1.3.6.1.4.1.9.9.42.1.3.5.1.46." + probeID + "." + subOID;
		String temporal_value = "";
		try {
			temporal_value = this.snmpget(oid);

			//in case we obtain a null value we maintain 
			//the old value generated in the last iteration
			old_value_n=(temporal_value==null) ?old_value_n :temporal_value;  
			temporal_value=(temporal_value==null)? old_value_n :temporal_value;  

			//System.out.println("getOWSumDS: " +temporal_value);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting getOWSumDS: " + ex.getMessage());
		}
		return temporal_value;
	}
	
	//old_value_o
	public String getSumOfPositiveDS() {
		String oid = ".1.3.6.1.4.1.9.9.42.1.3.5.1.25." + probeID + "." + subOID;
		String temporal_value = "";
		try {
			temporal_value = this.snmpget(oid);

			//in case we obtain a null value we maintain 
			//the old value generated in the last iteration
			
			//DEbugging add test- 05-05-2009
			old_value_o=(temporal_value==null) ?old_value_o :temporal_value;  
			temporal_value=(temporal_value==null)? old_value_o :temporal_value;  
			//DEbugging end add test- 05-05-2009		

			//System.out.println("getSumOfPositiveDS: " +temporal_value);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting getSumOfPositiveDS: " + ex.getMessage());
		}
		return temporal_value;
	}
	
	//old_value_p
	public String getSumOfPositiveSD() {
		String oid = ".1.3.6.1.4.1.9.9.42.1.3.5.1.13." + probeID + "." + subOID;
		String temporal_value = "";
		try {
			temporal_value = this.snmpget(oid);

			//in case we obtain a null value we maintain 
			//the old value generated in the last iteration
			old_value_p=(temporal_value==null) ?old_value_p :temporal_value;  
			temporal_value=(temporal_value==null)? old_value_p :temporal_value;  

			//System.out.println("getSumOfPositiveSD: " +temporal_value);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting getSumOfPositiveSD: " + ex.getMessage());
		}
		return temporal_value;
	}
	
	//old_value_q
	public String getSumOfNegativeSD() {
		String oid = ".1.3.6.1.4.1.9.9.42.1.3.5.1.19." + probeID + "." + subOID;
		String temporal_value = "";
		try {
			temporal_value = this.snmpget(oid);

			//in case we obtain a null value we maintain 
			//the old value generated in the last iteration
			old_value_q=(temporal_value==null) ?old_value_q :temporal_value;  
			temporal_value=(temporal_value==null)? old_value_q :temporal_value;  
		
			//System.out.println("getSumOfNegativeSD: " +temporal_value);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting getSumOfNegativeSD: " + ex.getMessage());
		}
		return temporal_value;
	}
	
	//old_value_r
	public String getSumOfNegativeDS() {
		String oid = ".1.3.6.1.4.1.9.9.42.1.3.5.1.31." + probeID + "." + subOID;
		String temporal_value = "";
		try {
			temporal_value = this.snmpget(oid);
	
			//in case we obtain a null value we maintain 
			//the old value generated in the last iteration
			old_value_r=(temporal_value==null) ?old_value_r :temporal_value;  
			temporal_value=(temporal_value==null)? old_value_r :temporal_value;  
		
			//System.out.println("getSumOfNegativeDS: " +temporal_value);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting getSumOfNegativeDS: " + ex.getMessage());
		}
		return temporal_value;
	}
	
	//old_value_s
	public String getNumOfPositiveDS() {
		String oid = ".1.3.6.1.4.1.9.9.42.1.3.5.1.24." + probeID + "." + subOID;
		String temporal_value = "";
		try {
			temporal_value = this.snmpget(oid);

			//in case we obtain a null value we maintain 
			//the old value generated in the last iteration
			old_value_s=(temporal_value==null) ?old_value_s :temporal_value;  
			temporal_value=(temporal_value==null)? old_value_s :temporal_value;  
				
			//System.out.println("getSumOfPositiveDS: " +temporal_value);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting getSumOfPositiveDS: " + ex.getMessage());
		}
		return temporal_value;
	}
	
	//old_value_t
	public String getNumOfPositiveSD() {
		String oid = ".1.3.6.1.4.1.9.9.42.1.3.5.1.12." + probeID + "." + subOID;
		String temporal_value = "";
		try {
			temporal_value = this.snmpget(oid);

			//in case we obtain a null value we maintain 
			//the old value generated in the last iteration
			
			old_value_t=(temporal_value==null) ?old_value_t :temporal_value;  
			temporal_value=(temporal_value==null)? old_value_t :temporal_value;  
		

			//System.out.println("getSumOfPositiveSD: " +temporal_value);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting getSumOfPositiveSD: " + ex.getMessage());
		}
		return temporal_value;
	}
	
	//old_value_u
	public String getNumOfNegativeSD() {
		String oid = ".1.3.6.1.4.1.9.9.42.1.3.5.1.18." + probeID + "." + subOID;
		String temporal_value = "";
		try {
			temporal_value = this.snmpget(oid);

			//in case we obtain a null value we maintain 
			//the old value generated in the last iteration
			old_value_u=(temporal_value==null) ?old_value_u :temporal_value;  
			temporal_value=(temporal_value==null)? old_value_u :temporal_value;  
			
			//System.out.println("getSumOfNegativeSD: " +temporal_value);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting getSumOfNegativeSD: " + ex.getMessage());
		}
		return temporal_value;
	}
	
	//old_value_v
	public String getNumOfNegativeDS() {
		String oid = ".1.3.6.1.4.1.9.9.42.1.3.5.1.30." + probeID + "." + subOID;
		String temporal_value = "";
		try {
			temporal_value = this.snmpget(oid);
	
			//in case we obtain a null value we maintain 
			//the old value generated in the last iteration
			old_value_v=(temporal_value==null) ?old_value_v :temporal_value;  
			temporal_value=(temporal_value==null)? old_value_v :temporal_value;  
			
			//System.out.println("getSumOfNegativeDS: " +temporal_value);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting getSumOfNegativeDS: " + ex.getMessage());
		}
		return temporal_value;
	}
	
public float getJitterAverage() {
		
		float temporal_value = 0;
		float temporal_value1 = 0;
		try {
			temporal_value = Integer.parseInt(getSumOfNegativeDS())+Integer.parseInt(getSumOfNegativeSD())
								+Integer.parseInt(getSumOfPositiveDS())+Integer.parseInt(getSumOfPositiveDS());
			temporal_value1 = Integer.parseInt(getNumOfNegativeDS())+Integer.parseInt(getNumOfNegativeSD())
								+Integer.parseInt(getNumOfPositiveDS())+Integer.parseInt(getNumOfPositiveDS());
			temporal_value = temporal_value/temporal_value1;
			//System.out.println("Jitter Average: "+ temporal_value);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting Jitter Average: " + ex.getMessage());
		
		}
		return temporal_value;
	}

public float getDelayAverage () {
	
	float temporal_value = 0;
	
	try {
		float rTTSum = Float.parseFloat(getRTTSum());
		float numOfRTT = Float.parseFloat(getNumOfRTT());

			temporal_value = rTTSum/numOfRTT;
		
		//System.out.println("Delay Average: "+ temporal_value);
	}
	catch ( Exception ex ) 
	{
		System.out.println("Exception Getting Delay Average: " + ex.getMessage());
	
	}
	return temporal_value;
}

	
public float getDelayAverage_old() {
	
	float temporal_value = 0;
	float temporal_value1 = 0;
	try {
		int numOFOW = Integer.parseInt(getNumOfOW());
		int oWSumSD = Integer.parseInt(getOWSumSD());
		int oWSumDS = Integer.parseInt(getOWSumDS());
		if (numOFOW != 0){
			temporal_value = (oWSumSD)/(numOFOW);
			temporal_value1 = (oWSumDS)/(numOFOW);
			temporal_value = temporal_value+temporal_value1;
		}
		//System.out.println("Delay Average: "+ temporal_value);
	}
	catch ( Exception ex ) 
	{
		System.out.println("Exception Getting Delay Average: " + ex.getMessage());
	
	}
	return temporal_value;
}
	
	public float getPacketLossRatio() {
		
		float temporal_value = 0;
		float temporal_value1 = 0;
		try {
			temporal_value = (Integer.parseInt(getPacketLossDS())+Integer.parseInt(getPacketLossSD())+Integer.parseInt(getPacketMIA()))*100;
			temporal_value1 = (Integer.parseInt(getPacketLossDS())+Integer.parseInt(getPacketLossSD())+Integer.parseInt(getPacketMIA())
					+Integer.parseInt(getPacketLateArrival())+Integer.parseInt(getPacketOutOfSequence())+Integer.parseInt(getNumOfRTT()))*100;
			temporal_value = (temporal_value/temporal_value1)*1000;
			//System.out.println("PacketLossRatio: "+temporal_value);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting PacketLossRatio: " + ex.getMessage());
		
		}
		return temporal_value;
	}
	
public float getBandwidth() {
		
		float temporal_value = 0;
		float temporal_value1 = 0;
		try {
			temporal_value = (Integer.parseInt(getPktDataRequestSize())+12)*8*Integer.parseInt(getNumPackets());
			temporal_value1 = Integer.parseInt(getFrequency())*1000;
			temporal_value = temporal_value/temporal_value1;
			//System.out.println("Bandwidth: "+temporal_value);
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting Bandwidth: " + ex.getMessage());
		
		}
			
		return temporal_value;
	}
	
	public String [] ciclo_networklevel () {
		String valoresTotales [] = new String [5];
		try {
//			 Here we do only once the searching for the SubOID
			if (subOID == "") 
				getSubOID ();
			valoresTotales [0] = Float.toString(getBandwidth());
			valoresTotales [1] = Float.toString(getPacketLossRatio());
			valoresTotales [2] = Float.toString(getJitterAverage());
			valoresTotales [3] = Float.toString(getDelayAverage());
			valoresTotales [4] = getTOSValue();
			
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Cicle Network Level: " + ex.getMessage());
		}
		return valoresTotales;
	}

	/**
	 * @author Edgar Magana
	 * Method to get information regarding Network Level
	 * Creates the Vector with all the information for each memory on the system
	 */
	public Vector getNetworkLevelParameters() {
		try { 
			networklevelDetails.addElement(ciclo_networklevel());	
		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting Network Level Parameters: " + ex.getMessage());
		}
		return networklevelDetails;
	}
	
	
	//old_value_w
	
	public void getSubOID () {
				
		String oid = ".1.3.6.1.4.1.9.9.42.1.2.10.1.6." + probeID;
		flagID = 1;
		String temporal_value = "";
		
		try {
			temporal_value = this.snmpget(oid);

			//in case we obtain a null value we maintain 
			//the old value generated in the last iteration
			old_value_w=(temporal_value==null) ?old_value_w :temporal_value;  
			temporal_value=(temporal_value==null)? old_value_w :temporal_value;  

		}
		catch ( Exception ex ) 
		{
			System.out.println("Exception Getting Sub OID: " + ex.getMessage());
		}
		//Counting the number of values in the string
		temporal_value = sOutID;
		int long_oid = oid.length();
		int long_oid2 = temporal_value.length();
		subOID = temporal_value.substring(long_oid,long_oid2);
		//System.out.println("ObjectID: " +temporal_value);
		
		flagID = 0;
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
		
		if (flagID == 1) {
			//<>
				while (target.snmpGetNext() != null) {
					sOutID = target.getObjectID();
					oid = ".1.3.6.1.4.1.9.9.42.1.3.5.1.2." + probeID;
					if (sOutID.length()>(oid.length()+3)){
						sOutID = target.getObjectID();
						//System.out.println("Next Value OID: " + sOutID);
						int len_oid = (sOutID.length())-oid.length();
						String oid_k = sOutID.substring(0, (sOutID.length()-len_oid));
						////System.out.println("Next Value OID: " + oid_k);
						//oid = ".1.3.6.1.4.1.9.9.42.1.3.5.1.2." + probeID;
						if (oid_k.compareTo(oid) == 0)
							break ;
					}
				}
		}
	
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
