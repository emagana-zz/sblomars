/**
 *  Agent getting resources information
 *
 *     @author     Edgar Magana
 *     @version    1.2
 *     @see		   jdk 1.5.0_06
 */

package source;

import java.util.*;

public class NetworkLevelAgent extends Thread {
	int duration;
	int polling;
	public int timing_seconds_per_cicle;
	int networklevel_cicles;
	public String  configuration_file;
	public Vector networkMetricsParameters; 
	public boolean reset = false; 
	
	double percentage=0;

	NetworkLevelAgent (String _configuration_file, int networklevel_interval_time, int amount_of_days, int _networklevel_cicles)  {
		duration = networklevel_interval_time;
		configuration_file = _configuration_file;
		// Cambiar esto por 86400 segundos en un dia
		this.timing_seconds_per_cicle = amount_of_days;
		this.networklevel_cicles = _networklevel_cicles;
	}

	public void run() {
		long startmillis=System.currentTimeMillis(); //Initial agent start time
		long incr=duration;
		int i = 0;
		SblomarsNetworkLevel agents = new SblomarsNetworkLevel ();	
		String parametersInputAgent [] = agents.startingHosts(configuration_file);
		
		
		//These two lines activate the socket server
		NetworkLevel_Server sendingParameters = new NetworkLevel_Server ();
		sendingParameters.set_NetworkLevelAgent(this);
		
		NetworkLevelGenerator networklevel_report = new NetworkLevelGenerator ();
		
		//This is the instance to register Processor Agent to Resource Manager
		NetworkLevel_Client activation = new NetworkLevel_Client ();
		activation.notifyingAgent(parametersInputAgent [1]);
		
		while (true) {
			if(i>0)		
				startmillis=System.currentTimeMillis();				//Initial agent start time after one duration elapsed time.

			int time = 0;
			polling = 0;
			incr=duration;
			do {
			
				long startMillis=System.currentTimeMillis();	
				//System.out.println("Starting Network Level Report Agent "+duration+" Seconds...");
				polling ++;
				int counter_cicle = polling;
				if (networkMetricsParameters != null)
					networkMetricsParameters.removeAllElements();
				networkMetricsParameters = agents.getNetworkLevelParameters ();
				if (i == 0 && polling == 1) {
					// It is just activated the first time
					sendingParameters.new_parameters();
				}
				//Here Is refreshing the value of the parameters
				sendingParameters.refreshing_parameters(networkMetricsParameters);
				String probeID = parametersInputAgent [2];
				networklevel_report.generate(duration, networkMetricsParameters, counter_cicle, networklevel_cicles, probeID);
				time = 1000;
				try {
				long durationFinal = (startmillis+(incr*1000))-System.currentTimeMillis();
			incr=incr+duration;
					
					
					//System.out.println("Final Timing: " + durationFinal);
					
					long sleeping = (durationFinal);	
					
				double lambda=sleeping;
				double beta=(duration*time);	
				
				//System.out.println("==================== Final Timing ======================== \n "+ (System.currentTimeMillis()- startMillis)+"\n ================");
					
				
				if(sleeping<0)
				{
					//Here we have negative value from variable sleeping,so we register the shyncronitzation's 
					//desviation of the reporting Agent. Something like if I passed the borderline limit I have to recover.
					
					percentage=percentage+((((-1)*lambda)/(beta)));
					//System.out.println("-------- Percentage ---------- \n "+percentage);
					//System.out.println(" ---------- Sleep 1 ----------");
				
					sleep(1);	//To allow other threads to acquire the run context avoiding starvation
				}
				else{
					
					if(percentage>0){
						double auxiliar_calc=percentage-(((lambda)/(beta)));
						percentage=(auxiliar_calc<0)? 0 :percentage-(((lambda)/(beta)));	//decrease of the percentage overrun alarm 
						
						//System.out.println("------------- Percentage --------------- \n "+percentage);
						if(percentage==0)
						{
							long time_aux=(long)(((-1)*(percentage-(((lambda)/(beta)))))*beta);
							
							sleep(time_aux);
						}
			
					}
					else
					{
						//System.out.println("--------- Percentage ------------- \n "+percentage);
						//System.out.println("--------------- Sleep -------------  "+sleeping+" segons");	
				
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
			//	In this condition of timing I have code "8640" is the number of pollings
			// in one day!!
			while (!reset);
			reset = false;
			//System.out.println("Here ends this thread... NetworkLevel Measuring");
			if (networkMetricsParameters != null)
				networkMetricsParameters.removeAllElements();
			i = 1;
			//sendingParameters.closingSocket();
			System.gc();
		}
	}

	public static void main(String[] args) {
		// The first argument is time (seconds) second is path of config file
		//NetworkLevelAgent t1 = new NetworkLevelAgent(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
		//Running threads of each file
		//t1.start();
	}
}

class TimeNetworkLevel extends Thread {
	NetworkLevelAgent timing_pointer;
	
	TimeNetworkLevel (NetworkLevelAgent pointer){
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
