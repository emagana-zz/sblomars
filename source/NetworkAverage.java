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

public class NetworkAverage {

	private PrintWriter bufwrite;
	private String fileName;
	public int counter_cicles;
	private double average;
	
	public String octets_in;
	public String octets_out;

	public void generate (int _counter, float average, int k, int number_of_cicles, long in, long out)
	{
		this.counter_cicles = _counter/number_of_cicles;
		this.fileName = "data/network/Network_ID_"+k+"_Average_Report_"+counter_cicles+".xml";
		this.average = average;
		this.octets_in = Long.toString(in);
		this.octets_out = Long.toString(out);
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
			System.out.println("Network Generator: " + e.getMessage());
		}
	}

	private void print() {
		bufwrite.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		bufwrite.println("<!-- Edited with Agent BLOMERSXML v1.0 (http://nmg.upc.edu/~emagana) by TSC (UPC)>");
		bufwrite.println("< Monitoring Resources Service xmlns:xsi=http://www.w3.org/2001/XMLSchema-instance -->");
		bufwrite.println("	<Monitoring_Network_Available_Information>");
		bufwrite.println("		<Device_Type>Network</Device_Type>");
		bufwrite.println("					<Counter>"+counter_cicles+"</Counter>");
		bufwrite.println("					<Octets_In>"+octets_in+"</Octets_In>");
		bufwrite.println("					<Octets_Out>"+octets_out+"</Octets_Out>");
		bufwrite.println("					<Average_BW>"+average+"</Average_BW>");
		bufwrite.println("	</Monitoring_Network_Available_Information>");
	}
}
