/**
 *  Agent getting resources information
 *
 *     @author     Edgar Magana
 *     @version    1.2
 *     @see		   jdk 1.5.0_06
 */

package source;

import java.io.*;

public class GetSblomarsTimingValues {
	/*************************************************************************************
	  Reads the host and community from a configuration file
	  currently named snmpd.conf
	 ************************************************************************************/
	public String [] initFromFile(String config, String kindDevice) {
		String s;
		String[] params;
		int i, offset;
		int lastoffset=0;
		String [] timingValues = new String [2];
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
				if (params[0].equals(kindDevice)) {
					timingValues [0] = params[1];					
					timingValues [1] = params[2];
					
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return (timingValues);
	}

}
