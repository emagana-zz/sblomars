/**
 *  Agent getting resources information
 *
 *     @author     Edgar Magana
 *     @version    1.2
 *     @see		   jdk 1.5.0_06
 */

package source;

import java.util.*;

public class ProcessorAgent extends Thread {
	int duration;
	int polling;
	public int timing_seconds_per_cicle;
	int processor_cicles;
	public String  configuration_file;
	public Vector cpuParameters; 
	public boolean reset = false; 

	long times;
	double percentage=0;
	long residual_time=-1;
	

	ProcessorAgent (String _configuration_file, int network_interval_time, int amount_of_days, int _processor_cicles, long times)  {
		
		configuration_file = _configuration_file;
		// This is the amout of seconds per day
		this.timing_seconds_per_cicle = amount_of_days;
		this.processor_cicles = _processor_cicles; //Number of Traps to Generate a Report
		this.duration = network_interval_time; //Timing Traps
		this.times=times;
	}

	public void run() {
		long startmillis=System.currentTimeMillis(); //Initial agent start time
		long incr=duration;
		
		int i = 0;
		SblomarsProcessor agents = new SblomarsProcessor ();
		String localhost = agents.startingHosts(configuration_file);
		
		//These two lines activate the socket server
		Processor_Server sendingParameters = new Processor_Server ();
		sendingParameters.set_processorAgent(this);	
		
		ProcessorGenerator report = new ProcessorGenerator ();
		
		while (true) {
			if(i>0)		
				startmillis=System.currentTimeMillis();				//Initial agent start time after one duration elapsed time.
			int time = 0;
			polling = 0;
			incr=duration;	
			Processor_Client activation = new Processor_Client ();
			activation.notifyingAgent(localhost);
			
			//Here line to create the RDDTool Database
			this.times=(i==0)? this.times : startmillis;			
			ProcessorRDDTool databaseRDD = new  ProcessorRDDTool (localhost, duration,times,residual_time,i,this.processor_cicles);
			//Done!!
			
			do {
				
				
				long startMillis=System.currentTimeMillis();	

				//System.out.println("Starting Processor Report Agent "+duration+" Seconds...");
				int osValue = agents.getOS();
				polling ++;
				int counter_cicle = polling;
				
				if (osValue == 1) {
					if (cpuParameters != null)
						cpuParameters.removeAllElements();
					cpuParameters = agents.getCpuParameters ();
					if (i == 0 && polling == 1) {
						// It is just activated the first time
						sendingParameters.new_parameters();
					}
					//Here Is refreshing the value of the parameters
					sendingParameters.refreshing_parameters(cpuParameters);
					report.generate(duration, cpuParameters, counter_cicle, processor_cicles, databaseRDD,System.currentTimeMillis());
					time = 1000;
					System.gc();
				}
				else if (osValue == 0) {
					
					cpuParameters = agents.getCpuParametersUnix ();
					
					
					if (i == 0 && polling == 1) {
						// It is just activated the first time, this is the Socket
						sendingParameters.new_parameters();
					}
					// Here Is refreshing the value of the parameters
					sendingParameters.refreshing_parameters(cpuParameters);
					
					report.generate(duration, cpuParameters, counter_cicle, processor_cicles, databaseRDD,System.currentTimeMillis());
					
					time = 1000;
					System.gc();
					
				}
				try {
					

			
					
			long durationFinal = (startmillis+(incr*1000))-System.currentTimeMillis();
			incr=incr+duration;
					
					
					//System.out.println("Final Timing: " + durationFinal);
					
					long sleeping = (durationFinal);	
					
				double lambda=sleeping;
				double beta=(duration*time);	
				
			//System.out.println("==================== Final Timing ======================== \n "+ (System.currentTimeMillis()- startMillis)+"\n ==========");
					
				
				if(sleeping<0)
				{
					//Here we have negative value from variable sleeping,so we register the shyncronitzation's 
					//desviation of the reporting Agent. Something like if I passed the borderline limit I have to recover.
										
					percentage=percentage+((((-1)*lambda)/(beta)));
					//System.out.println("--------- Percentage ------- \n "+percentage);
					//System.out.println(" ---------------------------------- Sleep 1 ------------------------------------");
					residual_time=System.currentTimeMillis()+1;
					sleep(1);	//To allow other threads to acquire the run context avoiding starvation
				}
				else{
					
					if(percentage>0){
						double auxiliar_calc=percentage-(((lambda)/(beta)));
						percentage=(auxiliar_calc<0)? 0 :percentage-(((lambda)/(beta)));	//decrease of the percentage overrun alarm 
						
						//System.out.println("--------- Percentage ----------- \n "+percentage);
						if(percentage==0)
						{
							long tutu=(long)(((-1)*(percentage-(((lambda)/(beta)))))*beta);
							residual_time=System.currentTimeMillis()+tutu;
							sleep(tutu);
						}
			
					}
					else
					{
						//System.out.println("----------- Percentage ------------- \n "+percentage);
						//System.out.println("---------- Sleep ---------  "+sleeping+" segons");	
						residual_time=System.currentTimeMillis()+sleeping;
						sleep(sleeping);
					}
				}	
				
				} catch (Exception e) {
					System.out.println("interrupted out of wait" + e);
				}
				////System.out.println("Timing for each polling is: " + duration);
				////System.out.println("Number of polling: " + polling);
				System.gc();

	
			}
			//In this condition of timing I have code "8640" is the number of pollings
			// in one day!!!!
			//while (polling >= timing_seconds_per_cicle/duration);
			while (!reset);			
			
			reset = false;
			//System.out.println("Here ends this thread... Processor Measuring");
			if (cpuParameters != null)
					cpuParameters.removeAllElements();
			i = 1;
			//sendingParameters.closingSocket();
			System.gc();
		}
	}

	public void creatingSockets() {
		//Pending...
		////System.out.println("This is the code for opening sockets");
	}

	public static void main(String[] args) {
		// The first argument is time (seconds) second is path of config file
		//ProcessorAgent t1 = new ProcessorAgent(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
		//Running threads of each file
		//t1.start();
	}
}

class TimeProcessor extends Thread {
	ProcessorAgent timing_pointer;
	
	TimeProcessor (ProcessorAgent pointer){
		timing_pointer = pointer;
	}
	
	public void run () {
	
		while (true)
		{
			try {
				sleep (timing_pointer.timing_seconds_per_cicle); 
				timing_pointer.reset = true;
			} catch (Exception e) {
				System.out.println("interrupted out of wait" + e);
			}
		}
		
	}
}

