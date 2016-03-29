/**
 *  Agent getting resources information
 *
 *     @author     Edgar Magana
 *     @version    1.2
 *     @see		   jdk 1.5.0_06
 */

package source;

import java.util.*;

public class MemoryAgent extends Thread {
	int duration;
	int polling;
	public int timing_seconds_per_cicle;
	int memory_cicles;
	public String  configuration_file;
	public Vector memoryParameters;
	public boolean reset = false;
	
	
	long times;
	double percentage=0;
	long residual_time=-1;
	

	MemoryAgent (String _configuration_file, int memory_interval_time, int amount_of_days, int _memory_cicles,long times)  {
		duration = memory_interval_time;
		configuration_file = _configuration_file;
		this.timing_seconds_per_cicle = amount_of_days;	
		this.memory_cicles = _memory_cicles;
		this.times=times;
	}

	public void run() {
		long startmillis=System.currentTimeMillis(); //Initial agent start time
		long incr=duration;
		
		int i = 0;
		SblomarsMemory agents = new SblomarsMemory ();
		String localhost = agents.startingHosts(configuration_file);
		
		//These two lines from below activate the socket server
		Memory_Server sendingParameters = new Memory_Server ();
		sendingParameters.set_memoryAgent(this);
		
		MemoryGenerator memory_report = new MemoryGenerator ();
		
		
				
		while (true) {
			if(i>0)		
				startmillis=System.currentTimeMillis();				//Initial agent start time after one duration elapsed time.
			int time = 0;
			polling = 0;
			incr=duration;	
			Memory_Client activation = new Memory_Client ();
			activation.notifyingAgent(localhost);	//This is the instance to register Processor Agent to Resource Manager
			
			//Here line to create the RDDTool Database
			this.times=(i==0)? this.times : startmillis;	
			MemoryRDDTool databaseRDD = new  MemoryRDDTool (localhost, duration,times,residual_time,i,this.memory_cicles);
			//Done!!
			
			
			do {
				
				///  Here is coming the Timing Part
				long startMillis = System.currentTimeMillis();
				
				//System.out.println("Starting Memory Report Agent "+duration+" Seconds...");
							
				int osValue = agents.getOS();
				polling ++;
				int counter_cicle = polling;
				if (osValue == 1) {
					if (memoryParameters != null)
						memoryParameters.removeAllElements();
					memoryParameters = agents.getMemoryParameters();
					if (i == 0 && polling == 1) {
						// It is just activated the first time
						sendingParameters.new_parameters();
					}
					//	Here Is refreshing the value of the parameters
					sendingParameters.refreshing_parameters(memoryParameters);
					memory_report.generate(duration, memoryParameters, counter_cicle, memory_cicles, databaseRDD);
					time = 1000;
				}
				else if (osValue == 0) {
					//if (memoryParameters != null)
						//memoryParameters.removeAllElements();
					memoryParameters = agents.getMemoryParametersUnix();
					if (i == 0 && polling ==1) {
						// It is just activated the first time
						sendingParameters.new_parameters();
					}
					//		Here Is refreshing the value of the parameters
					sendingParameters.refreshing_parameters(memoryParameters);
					memory_report.generate(duration, memoryParameters, counter_cicle, memory_cicles, databaseRDD);
					time = 1000;
	
				}
				try {
					
					//System.out.println("== Completed in " +	((System.currentTimeMillis() - startMillis) / 1000.0) + " sec");
				long durationFinal = (startmillis+(incr*1000))-System.currentTimeMillis();
			incr=incr+duration;
					
					
					//System.out.println("Final Timing: " + durationFinal);
					
					long sleeping = (durationFinal);	
					
				double lambda=sleeping;
				double beta=(duration*time);	
				
		
			//System.out.println("==================== Final Timing ======================== \n "+ (System.currentTimeMillis()-startMillis)+"\n =============");
					
				
				if(sleeping<0)
				{
					//Here we have negative value from variable sleeping,so we register the shyncronitzation's 
					//desviation of the reporting Agent. Something like if I passed the borderline limit I have to recover.
					
					percentage=percentage+((((-1)*lambda)/(beta)));
					//System.out.println("------------- Percentage ----------- \n "+percentage);
					//System.out.println(" -------- Sleep 1 --------");
					residual_time=System.currentTimeMillis()+1;
					sleep(1);	//Allowing other threads to acquire the run context in order to avoid starvation
				}
				else{
					
					if(percentage>0){
						double auxiliar_calc=percentage-(((lambda)/(beta)));
						percentage=(auxiliar_calc<0)? 0 :percentage-(((lambda)/(beta)));	//decrease of the percentage overrun alarm 
						
						//System.out.println("--------- Percentage ----------- \n "+percentage);
						if(percentage==0)
						{
							long time_aux=(long)(((-1)*(percentage-(((lambda)/(beta)))))*beta);
							residual_time=System.currentTimeMillis()+time_aux;
							sleep(time_aux);
						}
			
					}
					else
					{
						//System.out.println("---------- Percentage ------------ \n "+percentage);
						//System.out.println("------------ Sleep -----------  "+sleeping+" segons");	
						residual_time=System.currentTimeMillis()+sleeping;
						sleep(sleeping);					
					}
				}
				} catch (Exception e) {
					System.out.println("interrupted out of wait" + e);
				}
				//System.out.println("Timing for each polling is: " + duration);
				//System.out.println("Number of polling: " + polling);
				System.gc();
			}
			//	In this condition of timing I have code "8640" is the number of pollings
			// in one day!!
			while (!reset);
			reset = false;
			//System.out.println("Here ends this thread... Memory Measuring");
			if (memoryParameters != null)
				memoryParameters.removeAllElements();
			i = 1;
			//sendingParameters.closingSocket();
			System.gc();
			
		}
	}

	public static void main(String[] args) {
		// The first argument is time (seconds) second is path of config file
		//MemoryAgent t1 = new MemoryAgent(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
		//Running threads of each file
		//t1.start();
	}
}

class TimeMemory extends Thread {
	MemoryAgent timing_pointer;
	
	TimeMemory (MemoryAgent pointer){
		timing_pointer = pointer;
	}
	
	public void run () {
	
		while (true)
		{
			try {
				//System.out.println(timing_pointer.timing_seconds_per_cicle);
				sleep (timing_pointer.timing_seconds_per_cicle); 
				timing_pointer.reset = true;
			} catch (Exception e) {
				System.out.println("interrupted out of wait" + e);
			}
		}
		
	}
}
