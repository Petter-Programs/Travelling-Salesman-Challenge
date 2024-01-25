package pes00038.cs.stir.ac.uk.dissertation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Debug {
	
	private static String DEBUG_PATH_FROM_ARGS;
	
	private static final Long START_TIME = System.currentTimeMillis();
	private static final String DEBUG_FILE = Settings.DEBUG_PATH + "debug" + START_TIME + ".txt";
	private static boolean ENABLE_DEBUG_WRITE = Settings.COLLECT_DEBUG;
	private static boolean ONLY_AUTOMATIC_DEBUG = false;
	
	static PrintWriter writer;
	
	public static void startDebugger()
	{
		if(!ENABLE_DEBUG_WRITE)
			return;
		
		String filePath = DEBUG_PATH_FROM_ARGS==null ? DEBUG_FILE : DEBUG_PATH_FROM_ARGS;
		
		File outputFile = new File(filePath);
		if(!outputFile.exists())
		{
			outputFile.getParentFile().mkdirs();
			try {
				outputFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath, true)));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void writeDebug(String text)
	{ 
		if(!ENABLE_DEBUG_WRITE || ONLY_AUTOMATIC_DEBUG)
			return;
		
		writer.println(text);
		
	}
	
	public static void printAndWrite(String text)
	{
		if(!ONLY_AUTOMATIC_DEBUG)
		System.out.println(text);
		
		writeDebug(text);
	}
	
	public static void writeAutomaticFeedback(String text)
	{
		writer.println(text);
	}
	
	
	public static void closeDebugger()
	{
		if(ENABLE_DEBUG_WRITE)
		writer.close();
	}
	
	public static void setDebugPath(String directoryPath)
	{
		DEBUG_PATH_FROM_ARGS = directoryPath + START_TIME + ".txt";
	}
	
	public static void setAutomaticMode(boolean automaticMode)
	{
		ENABLE_DEBUG_WRITE = true;
		ONLY_AUTOMATIC_DEBUG = automaticMode;
	}

}
