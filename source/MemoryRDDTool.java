package source;

import static org.rrd4j.ConsolFun.AVERAGE;

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import org.rrd4j.DsType;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.RrdDef;
import org.rrd4j.core.RrdSafeFileBackend;
import org.rrd4j.core.Sample;
import org.rrd4j.core.Util;
import org.rrd4j.graph.RrdGraph;
import org.rrd4j.graph.RrdGraphDef;

public class MemoryRDDTool extends Thread {
	
	
	String FILE = null;
	String Node = null;
	
	long START_TIMING; //Default VAlue
	
	
	int MAX_STEP = 15; //Default VAlue
	long t = 0;

	// Image Dimensions
	static final int IMG_WIDTH = 500;
	static final int IMG_HEIGHT = 155;
	
	RrdDef rrdDef; 
	RrdDb rrdDb;
	PrintWriter log;
	String rrdRestoredPath;
	String xmlPath;
	RrdDb rrdRestoredDb;
	Sample sample;
	String rrdPath;
	String logPath;
	
	
	MemoryRDDTool (String fileName, int _duration, long times, long residual_times,int i, int period) {
		
		FILE = fileName + "_memory";
		Node = fileName;
		//System.out.println("== Creating RDDTool DB by SBLOMARS");
		this.MAX_STEP = _duration*period;
		 try {
		
			 rrdPath = "database/" + FILE + ".rrd";
				
				
				//Here starts the accurate synchronization for assign a time value to RDd
			
				String temporalTime="1";
				long time_consumed=0;
				
				boolean first_time=(i==0)? true : false;
				
					while(((time_consumed=(System.currentTimeMillis()-times))<6000) || !first_time){
						if(first_time){					
							//System.out.println("Waiting "+(6000-time_consumed));
							temporalTime=waitejar(6000-time_consumed);
							first_time=true;
						}
						else
						{						
							temporalTime=waitejar(6000-(System.currentTimeMillis()-residual_times));
							first_time=true;
						}
					}
                               
				//Here ends

						
						START_TIMING = Long.parseLong(temporalTime);
						t = START_TIMING;
						rrdDef = new RrdDef(rrdPath, START_TIMING, MAX_STEP);
				
				xmlPath = "database/" + FILE + ".xml";
				rrdRestoredPath = "database/" + FILE + "_restored.rrd";
				
				logPath = "database/" + FILE + ".log";
				
				
				log = new PrintWriter(new BufferedOutputStream(new FileOutputStream(logPath, false)));
				// creation
		//System.out.println("== Creating RRD file " + rrdPath);
		//  RrdDef = String path, long startTime, long step)
		
		
		// addDatasource (String dsName, DsType dsType, long heartbeat, double minValue, double maxValue)
		rrdDef.addDatasource("memory", DsType.GAUGE, MAX_STEP*2, Double.NaN, Double.NaN);
		rrdDef.addDatasource("memoryDiff", DsType.GAUGE, MAX_STEP*2, Double.NaN, Double.NaN);
		
		
		// RRA Definition
		rrdDef.addArchive(AVERAGE, 0.5, 1, 40320);
		//rrdDef.addArchive(AVERAGE, 0.5, 4, 2048);
		
		//System.out.println(rrdDef.dump());
		log.println(rrdDef.dump());
		//System.out.println("Estimated file size: " + rrdDef.getEstimatedSize());
		rrdDb = new RrdDb(rrdDef);
		//System.out.println("== RRD file created.");
		if (rrdDb.getRrdDef().equals(rrdDef)) {
			//System.out.println("Checking RRD file structure... OK");
		}
		else {
			//System.out.println("Invalid RRD file created. This is a serious bug, bailing out");
			return;
		}
		rrdDb.close();
		//System.out.println("== RRD file closed.");
		
		rrdDb = new RrdDb(rrdPath);
		sample = rrdDb.createSample();
		sample.setTime(t);
		sample.setValue("memory", 50);
		sample.setValue("memoryDiff", 100);
		//rrdDb.close();
		log.close();
	}
	 catch (Exception e) {
			System.out.println("interrupted out of wait" + e);
		}
	
	}


	synchronized String waitejar(long waitingtime){
	
		String hola="0";
		try{
			wait(waitingtime);
			hola= Long.toString(System.currentTimeMillis()).substring(0, 10);
		}
		catch (Exception e) {
                        System.out.println("interrupted out of wait" + e);
			
         	}
		return hola;
	}




	synchronized void UpdateRDDTool (double averagePercentage) {
		// Update database
	 try {

		 log = new PrintWriter(new BufferedOutputStream(new FileOutputStream(logPath, true)));
		sample = rrdDb.createSample();
		
		//Parser parsing = new Parser (); 
		
		t = t+MAX_STEP;
		sample.setTime(t);
		sample.setValue("memory", averagePercentage);
		sample.setValue("memoryDiff", 100);
		log.println(sample.dump());
		sample.update();
		
		// close files
		//System.out.println("== Closing both RRD files");
		//rrdDb.close();
		//System.out.println("== First file closed");
				
		for (int graphics = 0; graphics <= 2; graphics++)
		{
			
		RrdGraphDef gDef = new RrdGraphDef();
		gDef.setWidth(IMG_WIDTH);
		gDef.setHeight(IMG_HEIGHT);
		gDef.setNoMinorGrid(false);
		String imgPath = "database/" + FILE + ".png";
		if (graphics == 1) {
			imgPath = "database/" + FILE + "_onehour.png";
			//String temporalTime = Long.toString(System.currentTimeMillis()).substring(0, 10);
			//long subTiming = Long.parseLong(temporalTime);
			//gDef.setStartTime(subTiming-3600);  
			gDef.setStartTime(t-3600); 
		}
		else if (graphics == 2) {
			imgPath = "database/" + FILE + "_oneday.png";
			/*String temporalTime = Long.toString(System.currentTimeMillis()).substring(0, 10);
			long subTiming = Long.parseLong(temporalTime);
			gDef.setStartTime(subTiming-86400); */
			gDef.setStartTime(t-86400); 	
		}
		else if (graphics == 0) {
			imgPath = "database/" + FILE + ".png";
			/*String temporalTime = Long.toString(START_TIMING).substring(0, 10);
			long subTiming = Long.parseLong(temporalTime);*/
			gDef.setStartTime(START_TIMING);   
		}
		
		gDef.setFilename(imgPath);  
		/*String temporalTime = Long.toString(t).substring(0, 10);
		long subTiming = Long.parseLong(temporalTime);
		gDef.setEndTime(subTiming);*/
		gDef.setEndTime(t);
		gDef.setTitle(Node);
		gDef.setVerticalLabel("Percentage (%)");	
		gDef.datasource("memory", rrdPath, "memory", AVERAGE);
		gDef.datasource("memoryDiff", rrdPath, "memoryDiff", AVERAGE);
				
		
		
		//gDef.hrule(50, Color.RED, "Threshold" );
		
		
		gDef.area("memoryDiff", Color.lightGray, "Memory Available");
		gDef.area("memory", Color.RED, "Memory Used");
		gDef.line("memory", Color.BLACK, null);

		

		gDef.setImageInfo("<img src='%s' width='%d' height = '%d'>");
		
		gDef.setPoolUsed(false);
		
		gDef.setImageFormat("png");
		//System.out.println("Rendering graph " + Util.getLapTime());

       // create graph finally
		RrdGraph graph = new RrdGraph(gDef);
		//System.out.println(graph.getRrdGraphInfo().dump());
		//System.out.println("== Graph created " + Util.getLapTime());
		// locks info
		//System.out.println("== Locks info ==");
		//System.out.println(RrdSafeFileBackend.getLockInfo());
		// demo ends
		log.close();
		 }
		 }
		
		catch (Exception e) {
			System.out.println("interrupted out of wait" + e);
		}
	}
}
