package com.moppletop.connect4.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.fusesource.jansi.AnsiConsole;

public final class Utils
{

	private static final boolean ansi;
	private static final boolean clearConsole;
	private static final char END_OF_STRING_CHAR = '|';

	static
	{
		ansi = System.getProperty("disableAnsi") == null;
		clearConsole = System.getProperty("disableClearConsole") == null;
	}

	public static void enableAnsiIfNeeded()
	{
		if (ansi)
		{
			AnsiConsole.systemInstall();
		}
	}

	public static void clearConsole()
	{
		if (!clearConsole)
		{
			return;
		}

		try
		{
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		}
		catch (InterruptedException | IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void configureSocket(Socket socket)
	{
		try
		{
			socket.setKeepAlive(true);
			socket.setReuseAddress(true);
			socket.setSoTimeout((int) TimeUnit.MINUTES.toMillis(10));
		}
		catch (SocketException ex)
		{
			ex.printStackTrace();
		}
	}

	private static File asFile(InputStream stream) throws IOException
	{
		if (stream == null)
		{
			return null;
		}

		File tempFile = File.createTempFile(String.valueOf(stream.hashCode()), ".tmp");
		tempFile.deleteOnExit();

		try (FileOutputStream out = new FileOutputStream(tempFile))
		{
			byte[] buffer = new byte[1024];
			int bytesRead;

			while ((bytesRead = stream.read(buffer)) != -1)
			{
				out.write(buffer, 0, bytesRead);
			}
		}

		return tempFile;
	}

	public static List<String> readResource(String filePath) throws IOException
	{
		return Files.readAllLines(asFile(Utils.class.getResourceAsStream(filePath)).toPath());
	}

	public static String readString(InputStream stream) throws IOException
	{
		StringBuilder builder = new StringBuilder();

		int charCode;
		while ((charCode = stream.read()) != -1)
		{
			char ch = (char) charCode;

			if (ch == END_OF_STRING_CHAR)
			{
				break;
			}

			builder.append(ch);
		}

		return builder.toString();
	}

	public static void writeString(OutputStream stream, String message) throws IOException
	{
		stream.write(message.getBytes());
		stream.write(END_OF_STRING_CHAR);
	}
}
