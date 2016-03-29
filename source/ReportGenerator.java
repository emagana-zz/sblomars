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

public class ReportGenerator {
	private PrintWriter bufwrite;
	private String fileName;
	private int storage_elements = 0;
	private int processor_elements = 0;
	private int memory_elements = 0;
	private int network_elements = 0;
	private int software_elements = 0;
	private int duration;
	private Vector hddParameters;
	private Vector cpuParameters;
	private Vector memoryParameters;
	private Vector networkParameters;
	private Vector softwareParameters;

	public void generate (int duration, Vector hddParameters, Vector cpuParameters, Vector memoryParameters, Vector networkParameters, Vector softwareParameters) {
		this.fileName = "Ablomers_"+duration+"seconds.xml";
		this.duration = duration;
		this.hddParameters = hddParameters;
		this.cpuParameters = cpuParameters;
		this.memoryParameters = memoryParameters;
		this.networkParameters = networkParameters;
		this.softwareParameters = softwareParameters;
		getting_elements ();
		write();
	}

	private void getting_elements() {
		try {
			storage_elements = hddParameters.size();
			processor_elements = cpuParameters.size();
			memory_elements = memoryParameters.size();
			network_elements = networkParameters.size();
			software_elements = softwareParameters.size();
		} catch (Exception ex ) {
			System.out.println("Report Generator: " + ex.getMessage());
		}
	}

	//Made file
	private void write() {
		try {
			bufwrite = new PrintWriter(new FileWriter(fileName));
			print();
			bufwrite.flush();
		} catch (IOException e) {
			System.out.println("Report Generator: " + e.getMessage());
		}
	}

	//General structure to create EL policy
	private void print() {
		bufwrite.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		bufwrite.println("<!-- Edited with Agent BLOMERSXML v1.0 (http://nmg.upc.edu/~emagana) by TSC (UPC)>");
		bufwrite.println("<Monitoring Resources Service xmlns:xsi=http://www.w3.org/2001/XMLSchema-instance -->");
		bufwrite.println("	<Report_Period_Time>"+duration+"</Report_Period_Time>");
		//bufwrite.println("	<Operative_System>WindowsXP</Operative_System>");
		bufwrite.println("	<Monitoring_Information>");
		bufwrite.println("		<Device_Type>Storage</Device_Type>");
		bufwrite.println("			<Number_of_Elements>"+storage_elements+"</Number_of_Elements>");
		for (int j = 0; j < storage_elements; j++)
			storage_device (j);
		bufwrite.println("		<Device_Type>Processor</Device_Type>");
		bufwrite.println("			<Number_of_Elements>"+processor_elements+"</Number_of_Elements>");
		for (int j = 0; j < processor_elements; j++)
			processor_device (j);
		bufwrite.println("		<Device_Type>Memory</Device_Type>");
		bufwrite.println("			<Number_of_Elements>"+memory_elements+"</Number_of_Elements>");
		for (int j = 0; j < memory_elements; j++)
			memory_device (j);
		bufwrite.println("		<Device_Type>Network_Interfaces</Device_Type>");
		bufwrite.println("			<Number_of_Elements>"+network_elements+"</Number_of_Elements>");
		for (int j = 0; j < network_elements; j++)
			network_device (j);
		bufwrite.println("		<Device_Type>Software_Elements</Device_Type>");
		bufwrite.println("			<Number_of_Elements>"+software_elements+"</Number_of_Elements>");
		for (int j = 0; j < software_elements; j++)
			software_device (j);
		bufwrite.println("	</Monitoring_Information>");
	}

	public void storage_device (int k)
	{
		String [] datos = (String [])hddParameters.get(k);
		bufwrite.println("				<Storage_Device>");
		bufwrite.println("					<Label>"+datos [0]+"</Label>");
		bufwrite.println("					<Space_Total>"+datos [1]+"</Space_Total>");
		bufwrite.println("					<Space_Available>"+datos [3]+"</Space_Available>");
		bufwrite.println("					<Space_Used>"+datos [2]+"</Space_Used>");
		bufwrite.println("				</Storage_Device>"); 
	}

	public void processor_device (int k)
	{
		String [] datos = (String [])cpuParameters.get(k);
		bufwrite.println("				<Processor_Device>");
		bufwrite.println("					<Kind>"+datos [0]+"</Kind>");
		bufwrite.println("					<Percentage_Used>"+datos [1]+"</Percentage>");
		bufwrite.println("				</Processor_Device>"); 
	}

	public void memory_device (int k)
	{
		String [] datos = (String [])memoryParameters.get(k);
		bufwrite.println("				<Memory_Device>");
		bufwrite.println("					<Kind>RAM</Label>");
		bufwrite.println("					<Memory_Total>"+datos [0]+"</Memory_Total>");
		bufwrite.println("					<Memory_Available>"+datos [1]+"</Memory_Available>");
		bufwrite.println("					<Memory_Used>"+datos [2]+"</Memory_Used>");
		bufwrite.println("				</Memory_Device>"); 
	}

	public void network_device (int k)
	{
		String [] datos = (String [])networkParameters.get(k);
		bufwrite.println("				<Network_Device>");
		bufwrite.println("					<Label>"+datos [0]+"</Label>");
		bufwrite.println("					<Speed>"+datos [1]+"</Speed>");
		bufwrite.println("					<In_Octets>"+datos [2]+"</In_Octets>");
		bufwrite.println("					<Out_Octets>"+datos [3]+"</Out_Octets>");
		bufwrite.println("				</Network_Device>"); 
	}

	public void software_device (int k)
	{
		String [] datos = (String [])softwareParameters.get(k);
		bufwrite.println("				<Software_Device>");
		bufwrite.println("					<Software_Name>"+datos [0]+"</Software_Named>");
		bufwrite.println("					<Installed_Date>"+datos [1]+"</Installed_Date>");
		bufwrite.println("				</Software_Device>"); 
	}
}
