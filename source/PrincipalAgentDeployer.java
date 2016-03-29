package source;

import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;
public class PrincipalAgentDeployer extends Thread {

	static String configuration_file;
	static int software_interval_time;
	static int processor_interval_time;
	static int memory_interval_time;
	static int network_interval_time;
	static int storage_interval_time;
	static int networklevel_interval_time;
	static int amount_of_days;
	private static PrintWriter bufwrite;

	
	
//	create a file with arguments
	// Made file
	public static void write(String args []) {
		try {
			bufwrite = new PrintWriter(new FileWriter("timingValuesProcessor.conf"));
			print(args);
			bufwrite.flush();
		} catch (IOException e) {
			System.out.println("Report Generator: " + e.getMessage());
		}
	}
	
//	General structure to create EL policy
	/*
	private static void print(String args []) {
		bufwrite.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		bufwrite.println("<!-- Edited with Agent BLOMERSXML v1.0 (http://nmg.upc.edu/~emagana) by TSC (UPC)>");
		bufwrite.println("<Monitoring Resources Service xmlns:xsi=http://www.w3.org/2001/XMLSchema-instance -->");
		bufwrite.println("	<Report_Period_Time>"+args [12]+"</Report_Period_Time>");
		bufwrite.println("	<Processor_Traps>"+args [1]+"</Processor_Traps>");
		bufwrite.println("	<Processor_Traps_Number>"+args [2]+"</Processor_Traps>");
		bufwrite.println("	<Memory_Traps>"+args [3]+"</Memory_Traps>");
		bufwrite.println("	<Memory_Traps_Number>"+args [4]+"</Memory_Traps>");
		bufwrite.println("	<Processor_Traps>"+args [5]+"</Processor_Traps>");
		bufwrite.println("	<Processor_Traps_Number>"+args [6]+"</Processor_Traps>");
		bufwrite.println("	<Processor_Traps>"+args [7]+"</Processor_Traps>");
		bufwrite.println("	<Processor_Traps_Number>"+args [8]+"</Processor_Traps>");
		bufwrite.println("	<Software_Traps>"+args [9]+"</Software_Traps>");
		
		
	}
	*/
	private static void print(String args []) {
		
		bufwrite.println("#Configuration File for SBLOMARS Re-Configuration");
		//bufwrite.println("FullCycle "+args [12]);
		bufwrite.println("Processor "+args [1]+ " "+args [2]);
		//bufwrite.println("Memory "+args [3]+ " "+args [4]);
		//bufwrite.println("Network "+args [5]+ " "+args [6]);
		//bufwrite.println("Storage "+args [7]+ " "+args [8]);
		//bufwrite.println("Software "+args [9]);
	}
	
	
	public static void main(String[] args) {
        try {
            long initial = System.currentTimeMillis();
            int processor_cicles = 0;
            int memory_cicles = 0;
            int network_cicles = 0;
            int storage_cicles = 0;
            int networklevel_cicles = 0;
            configuration_file = args[0];
            amount_of_days = Integer.parseInt(args[12]);
            write(args);
            if (Integer.parseInt(args[1]) != 0) {
                processor_interval_time = Integer.parseInt(args[1]);
                processor_cicles = Integer.parseInt(args[2]);
                ProcessorAgent processor = new ProcessorAgent(configuration_file, processor_interval_time, amount_of_days, processor_cicles, initial);
                processor.start();
                TimeProcessor processor_timing = new TimeProcessor(processor);
                processor_timing.start();
            }
            sleep(5000);    //Because of letting a gap time between each resource start in order to reduce cpu consumed.
            if (Integer.parseInt(args[3]) != 0) {
                //try {
                //	sleep (1500);
                //} catch (Exception e) {
                //	//System.out.println("interrupted out of wait" + e);
                //}
                memory_interval_time = Integer.parseInt(args[3]);
                memory_cicles = Integer.parseInt(args[4]);
                MemoryAgent memory = new MemoryAgent(configuration_file, memory_interval_time, amount_of_days, memory_cicles, initial);
                memory.start();
                TimeMemory memory_timing = new TimeMemory(memory);
                memory_timing.start();
            }
            sleep(5000);
            if (Integer.parseInt(args[5]) != 0) {
                network_interval_time = Integer.parseInt(args[5]);
                network_cicles = Integer.parseInt(args[6]);
                NetworkAgent network = new NetworkAgent(configuration_file, network_interval_time, amount_of_days, network_cicles, initial);
                network.start();
                TimeNetwork network_timing = new TimeNetwork(network);
                network_timing.start();
            }
            sleep(5000);
            if (Integer.parseInt(args[7]) != 0) {
                storage_interval_time = Integer.parseInt(args[7]);
                storage_cicles = Integer.parseInt(args[8]);
                StorageAgent storage = new StorageAgent(configuration_file, storage_interval_time, amount_of_days, storage_cicles);
                storage.start();
                TimeStorage storage_timing = new TimeStorage(storage);
                storage_timing.start();
            }
            sleep(5000);
            if (Integer.parseInt(args[9]) != 0) {
                software_interval_time = Integer.parseInt(args[9]);
                SoftwareAgent software = new SoftwareAgent(configuration_file, software_interval_time, amount_of_days);
                software.start();
                TimeSoftware software_timing = new TimeSoftware(software);
                software_timing.start();
            }
            sleep(5000);
            if (Integer.parseInt(args[10]) != 0) {
                networklevel_interval_time = Integer.parseInt(args[10]);
                networklevel_cicles = Integer.parseInt(args[11]);
                NetworkLevelAgent networklevel = new NetworkLevelAgent(configuration_file, networklevel_interval_time, amount_of_days, networklevel_cicles);
                networklevel.start();
                TimeNetworkLevel networklevel_timing = new TimeNetworkLevel(networklevel);
                networklevel_timing.start();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(PrincipalAgentDeployer.class.getName()).log(Level.SEVERE, null, ex);
        }
	
	}
}
