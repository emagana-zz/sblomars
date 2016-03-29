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

 // Many things TODO
public class NetworkLevelGenerator {
	private PrintWriter bufwrite;
	private String fileName;
	private String probeID;
	private Vector networkLevelParameters;
	private int networklevel_elements;
	static int counter [];
	private int counter_cicle = 0;
	public int file_counter = 0;
	static int last_num_dev = 0;
	static long percentage_amount [];
	// static float percentage_amount_general [] = new float [720]; 
	static int timer [];
	int networklevel_cicles;
	 

	public void generate (int _duration, Vector networkLevelParameters, int _counter_cicle, int _networklevel_cicles, String _probeID) {
		this.probeID = _probeID;
		this.fileName = "data/networklevel/Network_Level_Report_"+"ID-"+probeID+"_"+_duration+"_Seconds_"+_counter_cicle+".xml";
		this.networkLevelParameters = networkLevelParameters;
		this.counter_cicle = _counter_cicle;
		
		this.networklevel_cicles = _networklevel_cicles;
		networklevel_elements = networkLevelParameters.size();
		if (file_counter==0)
		{
			counter = new int [networklevel_elements];
			timer = new int [networklevel_elements];
			percentage_amount = new long [networklevel_elements];
			last_num_dev = networklevel_elements;
		}
		else if (networklevel_elements != last_num_dev )
		{
			int [] t_counter = new int [networklevel_elements];
			float [] t_percentage_amount = new float [networklevel_elements];
			int t_last_num_dev = networklevel_elements;
			
			counter = new int [networklevel_elements];
			timer = new int [networklevel_elements];
			percentage_amount = new long [networklevel_elements];
			last_num_dev = networklevel_elements;
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
			System.out.println("Network Level Generator: " + e.getMessage());
		}
		System.gc();
	}

	private void print() {
		bufwrite.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		bufwrite.println("<!-- Edited with Agent BLOMERSXML v1.0 (http://nmg.upc.edu/~emagana) by TSC (UPC)>");
		bufwrite.println("< Monitoring Resources Service xmlns:xsi=http://www.w3.org/2001/XMLSchema-instance -->");
		bufwrite.println("	<Monitoring_Network_Level_Available_Information>");
		bufwrite.println("		<Device_Type>Network_Level_Elements</Device_Type>");
		bufwrite.println("			<Number_of_Elements>"+networklevel_elements+"</Number_of_Elements>");
		bufwrite.println("			<Probe_ID>"+probeID+"</Probe_ID>");
		for (int j = 0; j < networklevel_elements; j++)
			processor_device (j);
		bufwrite.println("	</Monitoring_Network_Level_Available_Information>");
	}

	public void processor_device (int k)
	{
		String [] datos = (String [])networkLevelParameters.get(k);
		//percentage_amount [k] = percentage_amount [k]  + Long.parseLong(datos [2]); 
		//Comentado por que es para la parte de crear el .txt
		//percentage_amount_general [counter [k]] = Long.parseLong(datos [1]); 
		//counter [k]++;
		//timer [k]++;
		////System.out.println("Counter: " +counter [k]);

		//Cambiar la manera de no usar numero fijo..
		//int number_of_cicles = processor_cicles;
		/*if (timer[k] == networklevel_cicles)
		{
			//System.out.println("Creating Report");
			double averagePercentage = percentage_amount[k]*1.0d/counter [k]*1.0d;
			////System.out.println("Average Processor Used: " + average);
			//	 		Llamar una funcion para imprimir en un archivo el resultado del promedio usado
			NetworkLevelAverage creating_average = new NetworkLevelAverage ();
			creating_average.generate (counter_cicle, averagePercentage, datos [], k, networklevel_cicles);
			// Se crea el archivo .txt
			//ProcessorGeneral average_values = new ProcessorGeneral ();
			//average_values.generate (percentage_amount_general);
			//average = 0.0;
			timer [k] = 0; 
			counter [k] = 0;
			percentage_amount [k] = 0;
			this.file_counter = 0;
			}
			*/
	
			System.gc();
		
		// if ( counter [k] > 100 ){
		//Liberar memoria y reiniciar todo
		////System.out.println("Aqui se ha completado un ciclo..: "); 
		// }
		bufwrite.println("				<NetworkLevel_Device>");
		bufwrite.println("					<TOS_Value>"+datos [4]+"</TOS_Value>");
		bufwrite.println("					<Bandwidth>"+datos [0]+"</Bandwidth>");
		bufwrite.println("					<PacketLossRatio>"+datos [1]+"</PacketLossRatio>");
		bufwrite.println("					<Jitter_Average>"+datos [2]+"</Jitter_Average>");
		bufwrite.println("					<Delay_Average>"+datos [3]+"</Delay_Average>");
		bufwrite.println("				</NetworkLevel_Device>"); 
	}
}
