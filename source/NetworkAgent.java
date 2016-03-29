/**
 *  Agent getting resources information
 *
 *     @author     Edgar Magana
 *     @version    1.2
 *     @see		   jdk 1.5.0_06
 */

package source;

import java.util.*;

public class NetworkAgent extends Thread {
	int duration;
	int polling;
	public int timing_seconds_per_cicle;
	int network_cicles;
	public String  configuration_file;
	public Vector networkParameters;
	float half_duplex_bw;
	public boolean reset = false; 
	long times;
	double percentage=0;
	long residual_time=-1;
	
	

	NetworkAgent (String _configuration_file, int network_interval_time, int amount_of_days, int _network_cicles, long times)  {
		duration = network_interval_time;
		configuration_file = _configuration_file;
		this.timing_seconds_per_cicle = amount_of_days;
		this.network_cicles = _network_cicles;
		this.times=times;
	}

	public void run() {
		long startmillis=System.currentTimeMillis(); //Initial agent start time
		long incr=duration;
		
		
		int i = 0;
		SblomarsNetwork agents = new SblomarsNetwork ();
		String localhost = agents.startingHosts(configuration_file);

		// These two lines from below activate the socket server
		Network_Server sendingParameters = new Network_Server ();
		sendingParameters.set_networkAgent(this);	
		
		NetworkGenerator network_report = new NetworkGenerator ();
		
		while (true) {
			if(i>0)		
				startmillis=System.currentTimeMillis();				//Initial agent start time after one duration elapsed time.

			int time = 0;
			polling = 0;
			incr=duration;				
			Network_Client activation = new Network_Client ();
			activation.notifyingAgent(localhost);
			
			this.times=(i==0)? this.times : startmillis;				
			
			int osValue = agents.getOS();
			if (osValue == 1) {
				networkParameters = agents.getNetworkParameters();
			}
			else if (osValue == 0) {
				networkParameters = agents.getNetworkParametersUnix();
				}
			
			NetworkRDDTool databaseRDD = null;
			int network_elements = networkParameters.size();			
			for (int j = 0; j < network_elements; j++){		//Creating the RDDTool Database
				databaseRDD = new  NetworkRDDTool (localhost + "_Interface_" + Integer.toString(j+1),duration,times,residual_time,i,this.network_cicles);
			//Done!!
			}
			
			
			do {
				long startillis = System.currentTimeMillis();
				///// Here is coming the Timing Part
								
				
				//System.out.println("Starting Network Report Agent "+duration+" Seconds...");
				//int osValue = agents.getOS();
				polling ++;
				int counter_cicle = polling;
				if (networkParameters != null)
					networkParameters.removeAllElements();
				if (osValue == 1) {
					networkParameters = agents.getNetworkParameters();
					if (i == 0 && polling == 1) {
					// It is just activated the first time
						sendingParameters.new_parameters();
					}
					//Here Is refreshing the value of the parameters
					sendingParameters.refreshing_parameters(networkParameters);
					network_report.generate(duration, networkParameters, counter_cicle, network_cicles, databaseRDD);
					time = 1000;
					System.gc();
					}
				else if (osValue == 0) {
					networkParameters = agents.getNetworkParametersUnix();
					if (i == 0 && polling == 1) {
						// It is just activated the first time
						sendingParameters.new_parameters();
					}
					//Here Is refreshing the value of the parameters
					sendingParameters.refreshing_parameters(networkParameters);
					network_report.generate(duration, networkParameters, counter_cicle, network_cicles, databaseRDD);
					time = 1000;
					System.gc();
				}
				try {
					
					////System.out.println("== Completed in " +	((System.currentTimeMillis() - startMillis) / 1000.0) + " sec");
						
							
					long durationFinal = (startmillis+(incr*1000))-System.currentTimeMillis();
			incr=incr+duration;
					
					
					//System.out.println("Final Timing: " + (System.currentTimeMillis()-startillis));
					
					long sleeping = (durationFinal);	
					
				double lambda=sleeping;
				double beta=(duration*time);	
				
			//System.out.println("==================== Final Timing ======================== \n "+ (System.currentTimeMillis()-startillis)+"\n ===============");
					
				
				if(sleeping<0)
				{
					//Here we have negative value from variable sleeping,so we register the shyncronitzation's 
					//desviation of the reporting Agent. Something like if I passed the borderline limit I have to recover.					
					
					percentage=percentage+((((-1)*lambda)/(beta)));
					//System.out.println("------- Percentage --------- \n "+percentage);
					//System.out.println(" --------- Sleep 1 ------------");
					residual_time=System.currentTimeMillis()+1;
					sleep(1);	//To allow other threads to acquire the run context avoiding starvation
				}
				else{
					
					if(percentage>0){
						double auxiliar_calc=percentage-(((lambda)/(beta)));
						percentage=(auxiliar_calc<0)? 0 :percentage-(((lambda)/(beta)));	//decrease of the percentage overrun alarm 
						
						//System.out.println("------- Percentage ------- \n "+percentage);
						if(percentage==0)
						{
							long time_aux=(long)(((-1)*(percentage-(((lambda)/(beta)))))*beta);
							residual_time=System.currentTimeMillis()+time_aux;
							sleep(time_aux);
						}
			
					}
					else
					{
						//System.out.println("--------- Percentage ---------- \n "+percentage);
						//System.out.println("---------------- Sleep ----1-------  "+sleeping+" segons");	
						residual_time=System.currentTimeMillis()+sleeping;
						sleep(sleeping);
					}
				}	
				} catch (Exception e) {
					System.out.println("interrupted out of wait" + e);
				}
				////System.out.println("Timing for each polling is: " + (duration+(60*2)));
				////System.out.println("Number of polling: " +polling);
				System.gc();
			}
			//	In this condition of timing I have code "8640" is the number of pollings
			// in one day!!
			//Here I need to change the second value.. network=20 + 60 (each card)
			//while (polling != timing_seconds_per_cicle/(duration+(60*1)));
			while (!reset);
			reset = false;
			//System.out.println("Here ends this thread... Network Measuring");
if (networkParameters != null)
					networkParameters.removeAllElements();
			i = 1;
			//sendingParameters.closingSocket();
			System.gc();
		}
	}

	public static void main(String[] args) {

		// The first argument is time (seconds) second is path of config file
		//NetworkAgent t1 = new NetworkAgent(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
		//Running threads of each file
		//t1.start();
	}
}

class TimeNetwork extends Thread {
	NetworkAgent timing_pointer;
	
	TimeNetwork (NetworkAgent pointer){
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
