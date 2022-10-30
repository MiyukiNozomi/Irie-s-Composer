package com.miyuki.baddapple;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** class to keep track of Java's Standard Out*/
public class ErrorSTD extends PrintStream {

	private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
	public BufferedWriter writer;
	PrintStream sst;
	
	public ErrorSTD(PrintStream sst) {
		super(sst);
		this.sst = sst;
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
		if (writer == null)  {
			try {
				writer = new BufferedWriter(new FileWriter(BadApple.ExecutionDir.getPath() + File.separator + "crash-report_" + Math.random() + ".txt"));
			} catch (IOException e1) {
				// no System.err, that would result in a StackOverflow Error
				System.out.println("Failed to open crash file.");
				e1.printStackTrace();
			}
		}
		String content = e.toString();
		String className = Thread.currentThread().getStackTrace()[4].getClassName();
		
		String smh = "STDOUT " + dtf.format(LocalDateTime.now()) + " [ERROR] "
				+ className + " > " + content;
		Debug.LastLine = smh;
		if (Debug.IsCapturing()) {
			Debug.CapturedBuffer += smh + "\n";
		} 
		
		try {
			writer.write(smh + '\n');
			writer.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return smh;
	}
}