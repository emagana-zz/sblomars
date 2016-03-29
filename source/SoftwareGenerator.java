/**
 *  Agent getting resources information
 *
 *     @author     Edgar Magana
 *     @version    1.2
 *     @see		   jdk 1.5.0_06
 */

package source;

import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class SoftwareGenerator {
	private PrintWriter bufwrite;	//JM: For printing representation of objects in to textformat in a file
	private String fileName;
	private Vector softwareParameters;
	private int software_elements;

	public void generate (int _duration, Vector softwareParameters, int file_counter) {
		this.fileName = "data/software/Software_Report_"+_duration+"_Seconds_"+file_counter+".xml";
		this.softwareParameters = softwareParameters;
		software_elements = softwareParameters.size();
		write();
	}

	//Made file
	private void write() {
		try {
			bufwrite = new PrintWriter(new FileWriter(fileName));	
			print();
			bufwrite.flush();
			bufwrite.close();
		} catch (IOException e) {
			System.out.println("Software Generator: " + e.getMessage());
		}
		System.gc();
	}

	private void print() {
		bufwrite.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		bufwrite.println("<!-- edited with Agent BLOMERSXML v1.0 (http://nmg.upc.edu/~emagana) by TSC (UPC) >");
		bufwrite.println("<Monitoring Resources Service xmlns:xsi=http://www.w3.org/2001/XMLSchema-instance -->");
		bufwrite.println("	<Monitoring_Software_Available_Information>");
		bufwrite.println("		<Device_Type>Software_Elements</Device_Type>");
		bufwrite.println("			<Number_of_Elements>"+software_elements+"</Number_of_Elements>");
		for (int j = 0; j < software_elements; j++)
			software_device (j);
		bufwrite.println("	</Monitoring_Software_Available_Information>");
	}

	public void software_device (int k)
	{
		String [] datos = (String [])softwareParameters.get(k);
		bufwrite.println("				<Software_Device>");
		bufwrite.println("					<Software_Name>"+datos [0]+"</Software_Name>");
		bufwrite.println("					<Installed_Date>"+datos [1]+"</Installed_Date>");
		bufwrite.println("				</Software_Device>"); 
	}
}
