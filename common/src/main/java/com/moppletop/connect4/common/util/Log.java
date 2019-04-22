package com.moppletop.connect4.common.util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Log
{

	private static final Logger LOG;

	static
	{
		LOG = Logger.getLogger("Connect-4");

		ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(new Formatter()
		{
			@Override
			public String format(LogRecord record)
			{
				return record.getMessage() + "\n";
			}
		});

		LOG.addHandler(handler);
		LOG.setUseParentHandlers(false);

		if (System.getProperty("debug") != null)
		{
			handler.setLevel(Level.ALL);
			LOG.setLevel(Level.ALL);
		}
		else
		{
			handler.setLevel(Level.INFO);
			LOG.setLevel(Level.INFO);
		}
	}

	public static void debug(Object message)
	{
		LOG.log(Level.ALL, String.valueOf(message));
	}

	public static void info(Object message)
	{
		LOG.log(Level.INFO, String.valueOf(message));
	}

	public static void blankLine()
	{
		info("");
	}
}
