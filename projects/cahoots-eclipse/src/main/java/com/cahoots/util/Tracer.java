package com.cahoots.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.jetty.io.WriterOutputStream;

public class Tracer {
	private BufferedWriter out;

	public Tracer(final String filename) {

		final String path = String.format("%s/Desktop/ops_%s_%d.txt",
				System.getProperty("user.home"), filename,
				System.currentTimeMillis());

		try {
			// Create file
			final FileWriter fstream = new FileWriter(path);
			out = new BufferedWriter(fstream);
		} catch (final Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	public void write(final String string) {
		try {
			out.write(string);
			flush();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void writeln(final String text) {
		write(text + '\n');
	}

	public void writeln(final String text, final Object... objects) {
		write(String.format(text + '\n', objects));
	}
	
	public void writeln(final Exception e) {
		final StringWriter writer = new StringWriter();
		e.printStackTrace(new PrintStream(new WriterOutputStream(writer)));
		writeln(writer.toString());
	}
	
	public void writeStacktrace() {
		try {
			throw new Exception();
		} catch (final Exception e) {
			final StringWriter writer = new StringWriter();
			e.printStackTrace(new PrintStream(new WriterOutputStream(writer)));
			writeln(writer.toString());
		}
	}

	public void flush() {
		try {
			out.flush();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
