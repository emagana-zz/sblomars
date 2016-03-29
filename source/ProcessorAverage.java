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

public class ProcessorAverage {

	private PrintWriter bufwrite;
	private String fileName;
	public int counter_cicles;
	private double averagePercentage;
	private String averageRaw;

	public void generate (int _counter, double averagePercentage, String averageRaw,  int k, int number_of_cicles)
	{
		this.counter_cicles = _counter/number_of_cicles;
		this.fileName = "data/processor/Processor_ID_"+k+"_Average_Report_"+counter_cicles+".xml";
		this.averagePercentage = averagePercentage;
		this.averageRaw = averageRaw;
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
			System.out.println("Processor Generator: " + e.getMessage());
		}
	}

	private void print() {
		long time_general = System.currentTimeMillis();
		bufwrite.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		bufwrite.println("<!-- Edited with Agent BLOMERSXML v1.0 (http://nmg.upc.edu/~emagana) by TSC (UPC)>");
		bufwrite.println("<Monitoring Resources Service xmlns:xsi=http://www.w3.org/2001/XMLSchema-instance -->");
		bufwrite.println("	<Monitoring_Processor_Available_Information>");
		bufwrite.println("		<Device_Type>Processor</Device_Type>");
		bufwrite.println("					<Counter>"+counter_cicles+"</Counter>");
		bufwrite.println("					<AverageRaw>"+averageRaw+"</AverageRaw>");
		bufwrite.println("					<AveragePercentage>"+averagePercentage+"</AveragePercentage>");
		bufwrite.println("					<Timing>"+time_general+"</Timing>");
		bufwrite.println("	</Monitoring_Processor_Available_Information>");
	}
}
