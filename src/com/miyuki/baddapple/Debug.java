package com.miyuki.baddapple;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Debug {

	private static boolean CaptureSTD = false;
	public static boolean HadErrors = false;
	public static String LastLine;
	public static String CapturedBuffer;

	private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		
	/**Prints out an information message.*/
	public static void Info(String msg) {
		PrintOut(GetMessage("INFO", msg));
	}

	/**Prints out a warning message.*/
	public static void Warn(String msg) {
		PrintOut(GetMessage("WARN", msg));
	}
	
	/**Prints out an error message.*/
	public static void Error(String msg) {
		PrintOut(GetMessage("ERROR", msg));
		HadErrors = true;
	}
	
	private static void PrintOut(String a) {
		if (System.out instanceof StandardOut) {
			((StandardOut)System.out).sst.println(a);
		} else {
			System.out.println(a);
		}
	}
		
	public static boolean IsCapturing() {
		return CaptureSTD;
	}
	
	public static void CaptureSTD() {
		CaptureSTD = true;
		CapturedBuffer = "";
	}

	public static void StopCapture() {
		CaptureSTD = false;
		CapturedBuffer = "";
	}

	public static String GetCapturedBuffer() {
		return CapturedBuffer;
	}
	
	public static String GetMessage(String type,Object e) {
		String smh = dtf.format(LocalDateTime.now()) + " [" + type + "] "
				+ Thread.currentThread().getStackTrace()[3].getClassName() + " > " + e.toString();
		LastLine = smh;
		if (CaptureSTD) {
			CapturedBuffer += smh + "\n";
		} 

		return smh;
	}
}
