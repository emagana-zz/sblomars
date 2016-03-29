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

public class ProcessorGenerator {
	private PrintWriter bufwrite;
	private String fileName;
	private Vector processorParameters;
	private int processor_elements;
	static int counter [];
	private int counter_cicle = 0;
	public int file_counter = 0;
	static int last_num_dev = 0;
	static double percentage_amount [];
	// static float percentage_amount_general [] = new float [720]; 
	static int timer [];
	int processor_cicles;
	int alarmThresholdPositive = 0;
	int alarmThresholdNegative = 0;
	int breakingFlagReconfigurationPositive = 0;
	int breakingFlagReconfigurationNegative = 0;
	ProcessorRDDTool databaseRDD;
	long tempsmilisaux=0;

	public void generate (int _duration, Vector processorParameters, int _counter_cicle, int _processor_cicles, ProcessorRDDTool _databaseRDD, long tempsmilis) {
		this.databaseRDD = _databaseRDD;
		
		this.fileName = "data/processor/Processor_Report_"+_duration+"_Seconds.xml";
		this.processorParameters = processorParameters;
		this.counter_cicle = _counter_cicle;
		this.processor_cicles = _processor_cicles;
		processor_elements = processorParameters.size();
		tempsmilisaux=tempsmilis;
		if (file_counter==0)
		{
			counter = new int [processor_elements];
			timer = new int [processor_elements];
			percentage_amount = new double [processor_elements];
			last_num_dev = processor_elements;
		}
		else if (processor_elements != last_num_dev )
		{
			int [] t_counter = new int [processor_elements];
			float [] t_percentage_amount = new float [processor_elements];
			int t_last_num_dev = processor_elements;
			
			counter = new int [processor_elements];
			timer = new int [processor_elements];
			percentage_amount = new double [processor_elements];
			last_num_dev = processor_elements;
			//this.file_counter = 0;
		}
		file_counter++;
		write(_duration);
	}

	//Made file
	private void write(int _duration) {
		try {
			bufwrite = new PrintWriter(new FileWriter(fileName));
			print(_duration);
			bufwrite.flush();
			bufwrite.close();
		} catch (IOException e) {
			System.out.println("Processor Generator: " + e.getMessage());
		}
		System.gc();
	}

	private void print(int _duration) {
		bufwrite.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		bufwrite.println("<!-- Edited with Agent BLOMERSXML v1.0 (http://nmg.upc.edu/~emagana) by TSC (UPC)>");
		bufwrite.println("< Monitoring Resources Service xmlns:xsi=http://www.w3.org/2001/XMLSchema-instance -->");
		bufwrite.println("	<Monitoring_Processor_Available_Information>");
		bufwrite.println("		<Device_Type>Processor_Elements</Device_Type>");
		bufwrite.println("			<Number_of_Elements>"+processor_elements+"</Number_of_Elements>");
		for (int j = 0; j < processor_elements; j++)
			processor_device (j, _duration);
		bufwrite.println("	</Monitoring_Processor_Available_Information>");
		//System.out.println("////////////____Report___/////////////////\n "+(System.currentTimeMillis()-tempsmilisaux)+"\n///////___Fi Report___/////////////");
	}

	public void processor_device (int k, int _duration)
	{
		String [] datos = (String [])processorParameters.get(k);
		percentage_amount [k] = percentage_amount [k]  + Double.parseDouble(datos [2]); 
		//Comentado por que es para la parte de crear el .txt
		//percentage_amount_general [counter [k]] = Long.parseLong(datos [1]); 
		counter [k]++;
		timer [k]++;
		//System.out.println("Counter: " +counter [k]);

		//Cambiar la manera de no usar numero fijo..
		//int number_of_cicles = processor_cicles;
		
		if (timer[k] == processor_cicles)
		{
			long entrada=0;			
			System.out.println("Creating Report");
			//System.out.println("***********__Entrada__******************\n "+((System.currentTimeMillis()-entrada)%500)+"\n*********__Fi Entrada__******");
			double averagePercentage = percentage_amount[k]*1.0d/counter [k]*1.0d;
				System.out.println("Average = "+averagePercentage);
			
			//Here the Update of the RDDTool DB
			databaseRDD.UpdateRDDTool(averagePercentage,System.currentTimeMillis());
			
			/* ////////////////
			//Creating Policy for Re-Configuration case
			
		
			if (averagePercentage > 40) {
				alarmThresholdPositive++;
			}
			
			else if (averagePercentage < 20) {
				alarmThresholdNegative++;
			}
			
			if (alarmThresholdPositive >= 1) {
				alarmThresholdNegative = 0;
				alarmThresholdPositive = 0;
				//Calling Re-Configuration Method To modify the values from File
				// if ( breakingFlagReconfigurationNegative != 0)
					//breakingFlagReconfigurationNegative--;
				//breakingFlagReconfigurationPositive++;
				//if (breakingFlagReconfigurationPositive <= 2){ // Only two time to reconfigure
				if (_duration >= 20) {
				System.out.println("Sending new values for Re-Configuration NEGATIVE"); //REDUCTION
				reconfiguringFile creatingFile = new reconfiguringFile ();
				int duration = (_duration*60)/100;  //not dividing because a division operation is expensive				
				
				String newValues [] = {"timingValuesProcessor.conf", Integer.toString(duration), Integer.toString(processor_cicles)};
								
				creatingFile.write(newValues);
				
				}
			}
			
			if (alarmThresholdNegative >= 1) {
				alarmThresholdPositive = 0;
				alarmThresholdNegative = 0;
				//Calling Re-Configuration Method To modify the values from File
				//if (breakingFlagReconfigurationPositive != 0)
					//breakingFlagReconfigurationPositive--;
				//breakingFlagReconfigurationNegative++;
				//if (breakingFlagReconfigurationNegative <= 2){ // Only two time to reconfigure
					if (_duration < 60) {
				System.out.println("Sending new values for Re-Configuration POSITIVE");
				reconfiguringFile creatingFile = new reconfiguringFile ();
				int duration = _duration*2;
				String newValues [] = {"timingValuesProcessor.conf", Integer.toString(duration), Integer.toString(processor_cicles)};
				creatingFile.write(newValues);
				
				}
			}
			*/
			
			//System.out.println("Average Processor Used: " + average);
			//	 		Llamar una funcion para imprimir en un archivo el resultado del promedio usado
			ProcessorAverage creating_average = new ProcessorAverage ();
			creating_average.generate (counter_cicle, averagePercentage, datos [1], k, processor_cicles);
			// Se crea el archivo .txt
			//ProcessorGeneral average_values = new ProcessorGeneral ();
			//average_values.generate (percentage_amount_general);
			//average = 0.0;
				
			timer [k] = 0; 
			counter [k] = 0;
			percentage_amount [k] = 0;
			this.file_counter = 0;			
			System.gc();
			entrada=System.currentTimeMillis();
		}
		// if ( counter [k] > 100 ){
		//Liberar memoria y reiniciar todo
		//System.out.println("Aqui se ha completado un ciclo..: "); 
		// }
		bufwrite.println("				<Processor_Device>");
		bufwrite.println("					<Kind>"+datos [0]+"</Kind>");
		//bufwrite.println("					<Speed>"+datos [0]+"</Speed>");
		bufwrite.println("					<Percentage_Used>"+datos [2]+"</Percentage_Used>");
		bufwrite.println("					<Raw_Used>"+datos [1]+"</Raw_Used>");
		bufwrite.println("				</Processor_Device>"); 
	}
}
