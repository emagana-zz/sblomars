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

public class StorageGenerator {
	private PrintWriter bufwrite;
	private String fileName;
	private Vector storageParameters;
	private int storage_elements;
	static int counter [];
	private int counter_cicle;
	public int file_counter = 0;
	static int last_num_dev = 0;
	static long percentage_amount [];
	// static float percentage_amount_general [] = new float [720]; 
	public int timer [];
	public int storage_cicles;

	public void generate (int _duration, Vector storageParameters, int _counter_cicle, int _storage_cicles) {
		this.fileName = "data/storage/Storage_Report_"+_duration+"_Seconds.xml";
		
		this.storageParameters = storageParameters;
		this.counter_cicle = _counter_cicle;
		this.storage_cicles = _storage_cicles;
		storage_elements = storageParameters.size();
		
		if (file_counter==0)
		{
			counter = new int [storage_elements];
			timer = new int [storage_elements];
			//timer = 0;
			percentage_amount = new long [storage_elements];
			last_num_dev = storage_elements;
		}
		
		else if (storage_elements != last_num_dev )
		{
			int [] t_counter = new int [storage_elements];
			float [] t_percentage_amount = new float [storage_elements];
			int t_last_num_dev = storage_elements;
			
			counter = new int [storage_elements];
			timer = new int [storage_elements];
			percentage_amount = new long [storage_elements];
			
			last_num_dev = storage_elements;
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
			System.out.println("Storage Report Generator: " + e.getMessage());
		}
		System.gc();
	}

	private void print() {
		bufwrite.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		bufwrite.println("<!-- Edited with Agent BLOMERSXML v1.0 (http://nmg.upc.edu/~emagana) by TSC (UPC)>");
		bufwrite.println("<Monitoring Resources Service xmlns:xsi=http://www.w3.org/2001/XMLSchema-instance -->");
		bufwrite.println("	<Monitoring_Storage_Available_Information>");
		bufwrite.println("		<Device_Type>Storage</Device_Type>");
		bufwrite.println("			<Number_of_Elements>"+storage_elements+"</Number_of_Elements>");
		for (int j = 0; j < storage_elements; j++)
			storage_device (j);
		bufwrite.println("	</Monitoring_Storage_Available_Information>");
	}

	public void storage_device (int k)
	{
		try {
		String [] datos = (String [])storageParameters.get(k);
		percentage_amount [k] = percentage_amount [k]  + Long.parseLong(datos [4]); 
		counter [k]++;
		timer [k]++;
		
		////System.out.println("Counter: " +counter [k]);
		
		//int number_of_cicles = 4;
		if (timer [k] == storage_cicles)
		{
			double average = percentage_amount[k]*1.0d/counter [k]*1.0d;
			////System.out.println("Generating Statistical Report. Average Storage Used: " + average);
			StorageAverage creating_average = new StorageAverage ();
			creating_average.generate (counter_cicle, average, k, storage_cicles);
			//average = 0.0;
			timer [k] = 0; 
			counter [k] = 0;
			percentage_amount [k] = 0;
			this.file_counter = 0;
			System.gc();
		}
		bufwrite.println("				<Storage_Device>");
		bufwrite.println("					<Label>"+datos [0]+"</Label>");
		bufwrite.println("					<Space_Total>"+datos [1]+"</Space_Total>");
		bufwrite.println("					<Space_Available>"+datos [3]+"</Space_Available>");
		bufwrite.println("					<Space_Used>"+datos [2]+"</Space_Used>");
		bufwrite.println("					<Space_Used_Percent>"+datos [4]+"</Space_Used_Percent>");
		bufwrite.println("				</Storage_Device>"); 
		}
		catch (Exception e) {
			System.out.println("Storage Report Generator Error in Generating Statistical Report: " + e.getMessage());
		}
	}
}
