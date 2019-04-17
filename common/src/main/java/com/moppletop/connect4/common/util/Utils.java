package com.moppletop.connect4.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.fusesource.jansi.AnsiConsole;

public final class Utils
{

	private static boolean ansi;

	public static void enableAnsiIfNeeded()
	{
		ansi = System.getProperty("ansi") != null;

		if (ansi)
		{
			AnsiConsole.systemInstall();
		}
	}

	public static void clearConsole()
	{
		if (!ansi)
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
		return Files.readAllLines(Utils.asFile(Utils.class.getResourceAsStream(filePath)).toPath());
	}
}
