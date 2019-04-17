package com.moppletop.connect4.client.menu;

import java.io.IOException;

import com.moppletop.connect4.client.Connect4Client;
import com.moppletop.connect4.client.multiplayer.RemoteHandler;
import com.moppletop.connect4.common.util.Utils;

import static java.lang.System.*;
import static org.fusesource.jansi.Ansi.Color.CYAN;
import static org.fusesource.jansi.Ansi.ansi;

public class OnlineGameMenuAction extends MenuAction
{

	public OnlineGameMenuAction(Connect4Client client)
	{
		super(client, 2, "Online Game");
	}

	@Override
	public void run()
	{
		Utils.clearConsole();

		out.println(ansi()
				.fg(CYAN)
				.a("Enter the address of the server:")
				.reset());

		String input = client.getScanner().nextLine().trim();

		if (input.isEmpty())
		{
			input = "localhost";
		}
		else if (input.length() == 1)
		{
			input = "192.168.0." + input;
		}

		try
		{
			new RemoteHandler(input);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
