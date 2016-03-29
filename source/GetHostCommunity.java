/**
 *  Agent getting resources information
 *
 *     @author     Edgar Magana
 *     @version    1.2
 *     @see		   jdk 1.5.0_06
 */

package source;

import java.io.*;

public class GetHostCommunity {
	/*************************************************************************************
	  Reads the host and community from a configuration file
	  currently named snmpd.conf
	 ************************************************************************************/
	public String [] initFromFile(String config) {
		String s;
		String[] params;
		int i, offset;
		int lastoffset=0;
		String [] community = new String [3];
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
				if (params[0].equals("rwcommunity")) {
					community [0] = params[1];					
					community [1] = params[2];
					community [2] = params[3];
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println ("Community: "+ community [0]);
		//System.out.println ("Host: "+ community [1]);	
		//System.out.println ("Port: "+ community [2]);
		return (community);
	}

}
