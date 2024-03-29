package com.moppletop.connect4.client;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.fusesource.jansi.Ansi;

import com.moppletop.connect4.client.menu.LocalGameMenuAction;
import com.moppletop.connect4.client.menu.MenuAction;
import com.moppletop.connect4.client.menu.OnlineGameMenuAction;
import com.moppletop.connect4.client.menu.QuitMenuAction;
import com.moppletop.connect4.common.util.Utils;

import static com.moppletop.connect4.common.util.Log.*;
import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

public class Connect4Client
{

	public static void main(String[] args) throws IOException
	{
		Utils.enableAnsiIfNeeded();
		new Connect4Client();
	}

	private final Scanner scanner;
	private final List<String> banner;

	private final List<MenuAction> actions;

	private Connect4Client() throws IOException
	{
		scanner = new Scanner(System.in);
		banner = Utils.readResource("/banner.txt");

		actions = Arrays.asList
				(
						new LocalGameMenuAction(this),
						new OnlineGameMenuAction(this),
						null,
						new QuitMenuAction(this)
				);

		while (true)
		{
			showMenu();
		}
	}

	private void showMenu()
	{
		Utils.clearConsole();

		Ansi header = ansi()
				.fg(BLUE)
				.a("---------------------------------------------------------------------")
				.reset();

		info(header);

		banner.forEach(line -> info(ansi()
				.fg(CYAN)
				.a(line)
				.reset()));

		blankLine();

		for (MenuAction action : actions)
		{
			if (action == null)
			{
				blankLine();
				continue;
			}

			info(ansi()
					.fg(YELLOW)
					.a("   " + action.getId() + " ")
					.fg(CYAN)
					.a(action.getName())
					.reset());
		}

		blankLine();
		info(header);

		blankLine();
		info(ansi()
				.fg(CYAN)
				.a("Enter a menu action: ")
				.reset());

		String input = scanner.nextLine().trim();

		int actionId;

		try
		{
			actionId = Integer.parseInt(input);
		}
		catch (NumberFormatException ex)
		{
			showMenu();
			return;
		}

		for (MenuAction action : actions)
		{
			if (action != null && action.getId() == actionId)
			{
				action.run();
				return;
			}
		}
	}

	public Scanner getScanner()
	{
		return scanner;
	}
}
