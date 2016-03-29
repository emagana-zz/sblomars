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

public class NetworkGenerator {

	private PrintWriter bufwrite;
	private String fileName;
	private Vector networkParameters;
	private int network_elements;
	static int counter [];
	private int counter_cicle = 0;
	static int file_counter = 0;
	static int last_num_dev = 0;
	public long percentage_amount_in [];
	public long percentage_amount_out [];
	static int timer [];
	public int duration;
	public long InOctets_initial [];
	public long OutOctets_initial [];
	public int network_cicles;
	NetworkRDDTool databaseRDD;

	public void generate (int _duration, Vector networkParameters, int _counter_cicle, int _network_cicles, NetworkRDDTool _databaseRDD) {
		this.databaseRDD = _databaseRDD;
		this.duration = _duration;
		this.fileName = "data/network/Network_Report_"+duration+"_Seconds.xml";
		this.networkParameters = networkParameters;
		this.counter_cicle = _counter_cicle;
		this.network_cicles = _network_cicles;
		network_elements = networkParameters.size();
		
		if (file_counter==0)
		{
			counter = new int [network_elements];
			timer = new int [network_elements];
			percentage_amount_in = new long [network_elements];
			percentage_amount_out = new long [network_elements];
			last_num_dev = network_elements;
		}
		
		else if (network_elements != last_num_dev )
		{
			
			int [] t_counter = new int [network_elements];
			float [] t_percentage_amount = new float [network_elements];
			int t_last_num_dev = network_elements;
			
			counter = new int [network_elements];
			timer = new int [network_elements];
			percentage_amount_in = new long [network_elements];
			percentage_amount_out = new long [network_elements];
			
			last_num_dev = network_elements;
			//this.file_counter = 0;
					}
		file_counter++;
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
			System.out.println("Memory Generator: " + e.getMessage());
		}
		System.gc();
	}

	private void print() {
		bufwrite.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		bufwrite.println("<!-- Edited with Agent BLOMERSXML v1.0 (http://nmg.upc.edu/~emagana) by TSC (UPC)>");
		bufwrite.println("<Monitoring Resources Service xmlns:xsi=http://www.w3.org/2001/XMLSchema-instance-->");
		bufwrite.println("	<Monitoring_Network_Available_Information>");
		bufwrite.println("		<Device_Type>Network_Interfaces</Device_Type>");
		bufwrite.println("			<Number_of_Elements>"+network_elements+"</Number_of_Elements>");
		for (int j = 0; j < network_elements; j++)
			network_device (j);
		bufwrite.println("	</Monitoring_Network_Available_Information>");
	}

	public void network_device (int k)
	{
		
		String [] datos = (String [])networkParameters.get(k);
		if (timer [k] == 0){
			InOctets_initial  = new long [networkParameters.size()];
			OutOctets_initial = new long [networkParameters.size()];
			InOctets_initial [k] = Long.parseLong(datos [2]);
			OutOctets_initial [k] = Long.parseLong(datos [3]);
			percentage_amount_in  = new long [networkParameters.size()];
			percentage_amount_out = new long [networkParameters.size()];
			percentage_amount_in [k] = 0; 
			percentage_amount_out [k] = 0;
		}
		//percentage_amount_in [k] = percentage_amount_in [k] + Long.parseLong(datos [2]); 
		//percentage_amount_out [k] = percentage_amount_out [k] + Long.parseLong(datos [3]);
		
		
		
		timer [k]++;
		counter [k]++;
		////System.out.println("Counter: " +timer [k]);

		
		//** After each network_cicles the graphic is generated **//
		//int number_of_cicles = 3;
		if (timer [k] == network_cicles)
		{
			percentage_amount_in [k] = Long.parseLong(datos [2]); 
			percentage_amount_out [k] = Long.parseLong(datos [3]);
			//double average_in = percentage_amount_in[k]*1.0d/counter [k]*1.0d;
			//double average_out = percentage_amount_out[k]*1.0d/counter [k]*1.0d;
			////System.out.println("Octets Initial_IN: " + InOctets_initial);
			////System.out.println("Octets Initial_OUT: " + OutOctets_initial);
			////System.out.println("Octets Final_IN: " + percentage_amount_in[k]);
			////System.out.println("Octets Final_OUT: " + percentage_amount_out[k]);
			////System.out.println("Average Network Used: " + duration);
			////System.out.println("Average Network Used: " + Long.parseLong(datos [1]));
			////System.out.println("Cicle: " + counter_cicle);
			
			float half_duplex_bw = (float)(((percentage_amount_in [k] - InOctets_initial [k]) + 
						(percentage_amount_out [k] - OutOctets_initial [k]))*8*100)/((network_cicles)*duration*Long.parseLong(datos [1]));
			
			
			//Here the Update of the RDDTool DB
			databaseRDD.UpdateRDDTool(half_duplex_bw);
			
			
			////System.out.println("Average Network Used: " + half_duplex_bw);
	
			NetworkAverage creating_average = new NetworkAverage ();
			creating_average.generate (counter_cicle, half_duplex_bw, k, network_cicles, percentage_amount_in[k], percentage_amount_out[k]);
			//average = 0.0;
			timer [k] = 0; 
			counter [k] = 0;
			percentage_amount_in [k] = 0;
			percentage_amount_out [k] = 0;
			System.gc();
		}
		bufwrite.println("				<Network_Device>");
		bufwrite.println("					<Label>"+datos [0]+"</Label>");
		bufwrite.println("					<Speed>"+datos [1]+"</Speed>");
		bufwrite.println("					<In_Octets>"+datos [2]+"</In_Octets>");
		bufwrite.println("					<Out_Octets>"+datos [3]+"</Out_Octets>");
		//bufwrite.println("					<BW_Percentage_Used>"+datos [4]+"</BW_Percentage_Used>");
		bufwrite.println("				</Network_Device>"); 
	}
}
