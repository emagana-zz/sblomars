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

public class NetworkRDDTool extends Thread {
	
	
	String FILE = null;
	String Node = null;
	
	long START_TIMING; //Default VAlue
	
	int MAX_STEP = 1; //Default VAlue
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
	
	NetworkRDDTool (String fileName,int duration,long times,long residual_times,int i,int period) {
		
		FILE = fileName + "_network";
		Node = fileName;
		//System.out.println("== Creating RDDTool DB by SBLOMARS");
		this.MAX_STEP = duration*period;
	
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
		rrdDef.addDatasource("network", DsType.GAUGE, MAX_STEP*2, Double.NaN, Double.NaN);
		
		
		// RRA Definition
		rrdDef.addArchive(AVERAGE, 0.5, 1, 1440);
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
		sample.setValue("network", 50);
		//rrdDb.close();
		
	}
	 catch (Exception e) {
			System.out.println("interrupted out of wait" + e);
		}
	
	}

synchronized String waitejar(long waitingslice){
	
		String hola="0";
		try{
			wait(waitingslice);
			hola= Long.toString(System.currentTimeMillis()).substring(0, 10);
		}
		catch (Exception e) {
                        System.out.println("interrupted out of wait" + e);
			
         	}
		return hola;
	}




	void UpdateRDDTool (double averagePercentage) {
		// Update database
	 try {

		sample = rrdDb.createSample();
		
		//Parser parsing = new Parser (); 
		
		t = t+MAX_STEP;
		sample.setTime(t);
		sample.setValue("network", averagePercentage);
		log.println(sample.dump());
		sample.update();
		
		// close files
		//System.out.println("== Closing both RRD files");
		//rrdDb.close();
		//System.out.println("== First file closed");
				
		RrdGraphDef gDef = new RrdGraphDef();
		gDef.setWidth(IMG_WIDTH);
		gDef.setHeight(IMG_HEIGHT);
		gDef.setNoMinorGrid(false);
		String imgPath = "database/" + FILE + ".png";
		gDef.setFilename(imgPath);
		gDef.setStartTime(START_TIMING);      
		gDef.setEndTime(t);
		gDef.setTitle(Node);
		gDef.setVerticalLabel("Percentage (%)");	
		gDef.datasource("network", rrdPath, "network", AVERAGE);
				
		
		
		gDef.hrule(50, Color.RED, "Threshold" );

		gDef.area("network", Color.BLUE, "network");

		gDef.setImageInfo("<img src='%s' width='%d' height = '%d'>");
		
		gDef.setPoolUsed(false);
		
		gDef.setImageFormat("png");
		//System.out.println("Rendering graph " + Util.getLapTime());

		RrdGraph graph = new RrdGraph(gDef);
		//System.out.println(graph.getRrdGraphInfo().dump());
		//System.out.println("== Graph created " + Util.getLapTime());
		// locks info
		//System.out.println("== Locks info ==");
		//System.out.println(RrdSafeFileBackend.getLockInfo());
		// demo ends
		log.close();
		 }
		
		catch (Exception e) {
			System.out.println("interrupted out of wait" + e);
		}
	}
}
