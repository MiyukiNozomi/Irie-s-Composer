package com.miyuki.baddapple;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.miyuki.baddapple.ui.ConsolePanel;

/** class to keep track of Java's Standard Out*/
public class StandardOut extends PrintStream {

	private static boolean CaptureSTD = false;
	public static String LastLine;
	private static String CapturedBuffer;

	private String prefix;
	private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
	
	public StandardOut(PrintStream sst, String prefix) {
		super(sst);
		this.prefix = prefix;
	}

	@Override
	public void print(boolean b) {super.print(GetMessage(b));}

	@Override
	public void print(char b) {super.print(GetMessage(b));}

	@Override
	public void print(char[] s) {super.print(GetMessage(s));}

	@Override
	public void print(float s) {super.print(GetMessage(s));}

	@Override
	public void print(String s) {super.print(GetMessage(s));}

	@Override
	public void print(double s) {super.print(GetMessage(s));}

	@Override
	public void print(int s) {super.print(GetMessage(s));}

	@Override
	public void print(long s) {super.print(GetMessage(s));}

	@Override
	public void print(Object s) {super.print(GetMessage(s));}
	
	@Override
	public void println() {
		super.println(GetMessage(""));
	}
	
	private String GetMessage(Object e) {
		String smh = dtf.format(LocalDateTime.now()) + " [" + prefix + "] "
				+ Thread.currentThread().getStackTrace()[4].getClassName() + " > " + e.toString();
		LastLine = smh;
		if (CaptureSTD) {
			CapturedBuffer += smh + "\n";
			ConsolePanel.textPane.setText(CapturedBuffer);
		} 

		return smh;
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
	}

	public static String GetCapturedBuffer() {
		return CapturedBuffer;
	}
}