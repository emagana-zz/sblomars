package source;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class reconfiguringFile {

	private  PrintWriter bufwrite;
	
//	create a file with arguments
	// Made file
	public  void write(String args []) {
		try {
			bufwrite = new PrintWriter(new FileWriter(args [0]));
			print(args);
			bufwrite.flush();
		} catch (IOException e) {
			System.out.println("Report Generator: " + e.getMessage());
		}
	}
	
	private  void print(String args []) {
		
		bufwrite.println("#Configuration File for SBLOMARS Re-Configuration");
		bufwrite.println("Processor "+args [1]+ " "+args [2]);
	}
	
	
	
}
