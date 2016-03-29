/**
 *  Agent getting resources information
 *
 *     @author     Edgar Magana
 *     @version    1.2
 *     @see		   jdk 1.5.0_06
 */

package source;

import java.util.*;

public class SoftwareAgent extends Thread {
	int duration;
	int polling;
	public int timing_seconds_per_cicle;
	public String  configuration_file;
	public Vector softwareParameters;
	public boolean reset = false; 
	
	double percentage=0;

	SoftwareAgent (String _configuration_file, int software_interval_time, int amount_of_days)  {
		duration = software_interval_time;
		configuration_file = _configuration_file;
		// Cambiar esto por 86400 segundos en un dia
		this.timing_seconds_per_cicle = amount_of_days;
	}

	public void run() {
		long startmillis=System.currentTimeMillis(); //Initial agent start time
		long incr=duration;
		
		int i = 0;
		SblomarsSoftware agents = new SblomarsSoftware ();
		String localhost = agents.startingHosts(configuration_file);
		
		//These two lines activate the socket server
		Software_Server sendingParameters = new Software_Server ();
		sendingParameters.set_softwareAgent(this);
		
		SoftwareGenerator software_report = new SoftwareGenerator ();
		
		while (true) {
			if(i>0)		
				startmillis=System.currentTimeMillis();				//Initial agent start time after one duration elapsed time.

			int time = 0;
			polling = 0;
			incr=duration;	
			//This is the instance to register Processor Agent to Resource Manager
			Software_Client activation = new Software_Client ();
			activation.notifyingAgent(localhost);
			
			do {
				
				//////Here is coming the Timing Part
				long startMillis = System.currentTimeMillis();
				
				//System.out.println("Starting Software Report Agent "+duration+" Seconds...");
				polling ++;
				if (softwareParameters != null)
					softwareParameters.removeAllElements();
				softwareParameters = agents.getSoftwareParameters();
				if (i == 0 && polling == 1) {
					// It is just activated the first time
					sendingParameters.new_parameters();
				}
				//Here is refreshing the value of the parameters
				sendingParameters.refreshing_parameters(softwareParameters);
				software_report.generate(duration, softwareParameters, polling);
				time = 1000;
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
					//System.out.println("--------- Percentage ------------ \n "+percentage);
					//System.out.println(" ------- Sleep 1 -------------");
					sleep(1);	//To allow other threads to acquire the run context avoiding starvation
				}
				else{
					
					if(percentage>0){
						double auxiliar_calc=percentage-(((lambda)/(beta)));
						percentage=(auxiliar_calc<0)? 0 :percentage-(((lambda)/(beta)));	//decrease of the percentage overrun alarm 
						
						//System.out.println("--------- Percentage ------------ \n "+percentage);
						if(percentage==0)
						{
							long time_aux=(long)(((-1)*(percentage-(((lambda)/(beta)))))*beta);
							
							sleep(time_aux);
						}
			
					}
					else
					{
						//System.out.println("-------- Percentage --------- \n "+percentage);
						//System.out.println("---------- Sleep ----------  "+sleeping+" segons");	
					
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
			//System.out.println("Here ends this thread... Software Measuring");
			if (softwareParameters != null)
					softwareParameters.removeAllElements();
			i = 1;
			//sendingParameters.closingSocket();
			System.gc();
		}
	}

	public static void main(String[] args) {
		// The first argument is path of config file second is time (seconds) 
		SoftwareAgent t1 = new SoftwareAgent(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
		//Running thread by solitary
		t1.start();
	}
}

class TimeSoftware extends Thread {
	SoftwareAgent timing_pointer;
	
	TimeSoftware (SoftwareAgent pointer){
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
