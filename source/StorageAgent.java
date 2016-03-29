/**
 *  Agent getting resources information
 *
 *     @author     Edgar Magana
 *     @version    1.2
 *     @see		   jdk 1.5.0_06
 */

package source;

import java.util.*;

public class StorageAgent extends Thread {
	int duration;
	int polling;
	public int timing_seconds_per_cicle;
	int storage_cicles;
	public String  configuration_file;
	public Vector hddParameters; 
	public boolean reset = false; 

	double percentage=0;

	StorageAgent (String _configuration_file, int storage_interval_time, int amount_of_days, int _storage_cicles)  {
		duration = storage_interval_time;
		configuration_file = _configuration_file;
		// Cambiar esto por 86400 segundos en un dia
		this.timing_seconds_per_cicle = amount_of_days;
		this.storage_cicles = _storage_cicles;
	}

	public void run() {
		long startmillis=System.currentTimeMillis(); //Initial agent start time
		long incr=duration;
		
		int i = 0;
		SblomarsStorage agents = new SblomarsStorage ();	
		String localhost = agents.startingHosts(configuration_file);
		
		//These two lines activate the socket server
		Storage_Server sendingParameters = new Storage_Server ();
		sendingParameters.set_storageAgent(this);
		
		StorageGenerator storage_report = new StorageGenerator ();
		
		while (true) {
			if(i>0)		
				startmillis=System.currentTimeMillis();				//Initial agent start time after one duration elapsed time.
			int time = 0;
			polling = 0;
			incr=duration;	
			//This is the instance to register Processor Agent to Resource Manager
			Storage_Client activation = new Storage_Client ();
			activation.notifyingAgent(localhost);
			
			do {
				
				//////  Here is coming the Timing Part
				long startMillis = System.currentTimeMillis();
				
				//System.out.println("Starting Storage Report Agent "+duration+" Seconds...");
				polling ++;
				int counter_cicle = polling;
				if (hddParameters != null)
					hddParameters.removeAllElements();
				hddParameters = agents.getHddParameters ();
				if (i == 0 && polling == 1) {
					// It is just activated the first time
					sendingParameters.new_parameters();
				}
				//Here Is refreshing the value of the parameters
				sendingParameters.refreshing_parameters(hddParameters);
				storage_report.generate(duration, hddParameters, counter_cicle, storage_cicles);
				time = 1000;
				try {
					
					////System.out.println("== Completed in " +	((System.currentTimeMillis() - startMillis) / 1000.0) + " sec");
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
					//System.out.println("------- Percentage -------- \n "+percentage);
					//System.out.println(" ----------- Sleep 1 ---------");
									sleep(1);	//To allow other threads to acquire the run context avoiding starvation
				}
				else{
					
					if(percentage>0){
						double auxiliar_calc=percentage-(((lambda)/(beta)));
						percentage=(auxiliar_calc<0)? 0 :percentage-(((lambda)/(beta)));	//decrease of the percentage overrun alarm 
						
						//System.out.println("------- Percentage ---------- \n "+percentage);
						if(percentage==0)
						{
							long time_aux=(long)(((-1)*(percentage-(((lambda)/(beta)))))*beta);
							sleep(time_aux);
						}
			
					}
					else
					{
						//System.out.println("------- Percentage --------- \n "+percentage);
						//System.out.println("---------- Sleep ----------  "+sleeping+" segons");	
						sleep(sleeping);	
					}
				}					} catch (Exception e) {
					System.out.println("interrupted out of wait" + e);
				}
				////System.out.println("Timing for each polling is: " + duration);
				////System.out.println("Number of polling: " + polling);
				System.gc();
			}
			//	In this condition of timing I have code "8640" is the number of pollings
			// in one day!!
			while (!reset);
			reset = false;
			//System.out.println("Here ends this thread... Storage Measuring");
			if (hddParameters != null)
					hddParameters.removeAllElements();
			i = 1;
			//sendingParameters.closingSocket();
			System.gc();
		}
	}

	public static void main(String[] args) {
		// The first argument is time (seconds) second is path of config file
		//StorageAgent t1 = new StorageAgent(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
		//Running threads of each file
		//t1.start();
	}
}

class TimeStorage extends Thread {
	StorageAgent timing_pointer;
	
	TimeStorage (StorageAgent pointer){
		timing_pointer = pointer;
	}
	
	public void run () {
	
		while (true)
		{
			try {
				////System.out.println(timing_pointer.timing_seconds_per_cicle);
				sleep (timing_pointer.timing_seconds_per_cicle); 
				timing_pointer.reset = true;
			} catch (Exception e) {
				System.out.println("interrupted out of wait" + e);
			}
		}
		
	}
}
