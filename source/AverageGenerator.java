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

public class AverageGenerator {

	private PrintWriter bufwrite;
	private String fileName;
	private int cpuAverage;
	private int timeAverage;
	private int samples;
	private String cpuDescription;

	public void generate (String _fileName, int _cpuAverage, String _cpuDescription, int _samples, int _timeAverage) {
		this.fileName = _fileName;
		this.cpuAverage = _cpuAverage;
		this.cpuDescription = _cpuDescription;
		this.samples = _samples;
		this.timeAverage = _timeAverage;
		write();
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

	private void print() {
		bufwrite.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		bufwrite.println("<!-- Edited with Agent BLOMERSXML v1.0 (http://nmg.upc.edu/~emagana) by TSC (UPC)>");
		bufwrite.println("< Monitoring Resources Service xmlns:xsi=http://www.w3.org/2001/XMLSchema-instance-->");
		bufwrite.println("	<Monitoring_CPU_Average_Information>");
		bufwrite.println("		<Device_Type>Processor</Device_Type>");	
		bufwrite.println("				<Processor_Device>");
		bufwrite.println("					<Kind>"+cpuDescription+"</Kind>");
		bufwrite.println("					<Percentage_Average_Used>"+cpuAverage+"</Percentage_Average_Used>");
		bufwrite.println("					<Percentage_Average_Time>"+samples*timeAverage+"</Percentage_Average_Time>");
		bufwrite.println("				</Processor_Device>"); 
		bufwrite.println("	</Monitoring_CPU_Average_Information>");
	}
}
