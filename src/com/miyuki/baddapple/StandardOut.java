package com.miyuki.baddapple;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** class to keep track of Java's Standard Out*/
public class StandardOut extends PrintStream {

	private String prefix;
	private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
	PrintStream sst;
	
	public StandardOut(PrintStream sst, String prefix) {
		super(sst);
		this.sst = sst;
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
		String content = e.toString();
		String className = Thread.currentThread().getStackTrace()[4].getClassName();
		
		String smh = "STDOUT " + dtf.format(LocalDateTime.now()) + " [" + prefix + "] "
				+ className + " > " + content;
		Debug.LastLine = smh;
		if (Debug.IsCapturing()) {
			Debug.CapturedBuffer += smh + "\n";
		} 

		return smh;
	}
}