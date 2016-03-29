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

public class MemoryGenerator {

	private PrintWriter bufwrite;
	private String fileName;
	private Vector memoryParameters;
	private int memory_elements;
	static int counter [];
	private int counter_cicle = 0;
	static int file_counter = 0;
	static int last_num_dev = 0;
	static long percentage_amount []; 
	// static float percentage_amount_general [] = new float [720]; 
	static int timer [];
	public int memory_cicles;
	MemoryRDDTool databaseRDD;
	
	public void generate (int _duration, Vector memoryParameters, int _counter_cicle, int _memory_cicles, MemoryRDDTool _databaseRDD) {
		this.databaseRDD = _databaseRDD;
		this.fileName = "data/memory/Memory_Report_"+_duration+"_Seconds.xml";
		this.memoryParameters = memoryParameters;
		this.counter_cicle = _counter_cicle;
		this.memory_cicles = _memory_cicles;
		memory_elements = memoryParameters.size();
		
		if (file_counter==0)
		{
			counter = new int [memory_elements];
			timer = new int [memory_elements];
			//timer = 0;
			percentage_amount = new long [memory_elements];
			last_num_dev = memory_elements;
		}
		
		else if (memory_elements != last_num_dev )
		{
			int [] t_counter = new int [memory_elements];
			float [] t_percentage_amount = new float [memory_elements];
			int t_last_num_dev = memory_elements;
			
			counter = new int [memory_elements];
			timer = new int [memory_elements];
			percentage_amount = new long [memory_elements];
			
			last_num_dev = memory_elements;
			//this.file_counter = 0;
			//Hacer un ciclo donde los valores anteriores a las nuevas direcciones de memoria y despues
			// Hacer que los valores originales apuntes a las nuevas direcciones de memoria
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
		bufwrite.println("< Monitoring Resources Service xmlns:xsi=http://www.w3.org/2001/XMLSchema-instance -->");
		bufwrite.println("	<Monitoring_Memory_Available_Information>");
		bufwrite.println("		<Device_Type>Memory</Device_Type>");
		bufwrite.println("			<Number_of_Elements>"+memory_elements+"</Number_of_Elements>");
		for (int j = 0; j < memory_elements; j++)
			memory_device (j);
		bufwrite.println("	</Monitoring_Memory_Available_Information>");
	}

	public void memory_device (int k)
	{
		String [] datos = (String [])memoryParameters.get(k);
		// Aqui obtengo los valores porcentuales del contador desde que inicia hasta que lo paramos
		percentage_amount [k] = percentage_amount [k]  + Long.parseLong(datos [3]);
		//Comentado por que es para la parte de crear el .txt
		//percentage_amount_general [counter [k]] = Long.parseLong(datos [3]); 
		counter [k]++;
		timer [k]++;
		//System.out.println("Counter: " +counter [k]);
		// This value is to specify how often is mesure the resource usability average
		//int number_of_cicles = 6;
		if (timer [k] == memory_cicles)
		{
			double average = percentage_amount[k]*1.0d/counter [k]*1.0d;
			//System.out.println("Average Memory Used: " + average);
			
			//Here the Update of the RDDTool DB
			databaseRDD.UpdateRDDTool(average);
			
			MemoryAverage creating_average = new MemoryAverage ();
			creating_average.generate (counter_cicle, average, datos [2], k, memory_cicles);
			//Se crea archivo .txt
			//MemoryGeneral average_values = new MemoryGeneral ();
			//average_values.generate (percentage_amount_general);
			//average = 0.0;
			timer [k] = 0; 
			counter [k] = 0;
			percentage_amount [k] = 0;
			System.gc();
		}
		bufwrite.println("				<Memory_Device>");
		bufwrite.println("					<Kind>RAM</Kind>");
		bufwrite.println("					<Memory_Total>"+datos [0]+"</Memory_Total>");
		bufwrite.println("					<Memory_Available>"+datos [1]+"</Memory_Available>");
		bufwrite.println("					<Memory_Used>"+datos [2]+"</Memory_Used>");
		bufwrite.println("					<Memory_Used_Percent>"+datos [3]+"</Memory_Used_Percent>");
		bufwrite.println("				</Memory_Device>"); 
	}
}
