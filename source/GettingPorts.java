/**
 *  Agent getting resources information
 *
 *     @author     Edgar Magana
 *     @version    1.2
 *     @see		   jdk 1.5.0_06
 */

package source;

import java.io.*;

public class GettingPorts {
	/*************************************************************************************
	  Reads ports from a configuration file
	  currently named ports.conf
	 ************************************************************************************/
	public String initFromFile(String config, String resource) {
		String s;
		String[] params;
		int i, offset;
		int lastoffset=0;
		String [] ports = new String [3];
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
				if (params[0].equals(resource)) {
					ports [0] = params[1];					
					ports [1] = params[2];
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println ("Port: "+ ports [1]);	
		return (ports [1]);
	}
}
