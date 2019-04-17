package com.moppletop.connect4.client.game;

import com.moppletop.connect4.common.game.Round;
import com.moppletop.connect4.common.player.Player;

import static java.lang.System.out;
import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

public class LocalRound extends Round
{

	private Player playerA, playerB;

	public LocalRound()
	{
		while (true)
		{
			changePlayers();
			grid.showGrid();

			while (!nextTurn())
			{
			}

			if (isGameOver())
			{
				gameOver();
				return;
			}
		}
	}

	private void changePlayers()
	{
		if (player == null)
		{
			playerA = new Player("Player 1", YELLOW);
			playerB = new Player("Player 2", RED);
			player = Math.random() < 0.5 ? playerA : playerB;
		}

		player = player.equals(playerA) ? playerB : playerA;
	}

	@Override
	protected boolean nextTurn()
	{
		out.println();
		out.println(ansi()
				.fg(CYAN)
				.a("Your move: ")
				.reset());

		String input = scanner.nextLine().trim();
		int column;

		try
		{
			column = Integer.parseInt(input);
		}
		catch (NumberFormatException ex)
		{
			return false;
		}

		return grid.placeTile(column);
	}

	@Override
	protected void gameOver()
	{
		grid.showGrid();
		out.println(ansi()
				.fg(CYAN)
				.a("Enter any key to return to the menu.")
				.reset());
		scanner.next();
	}
}
