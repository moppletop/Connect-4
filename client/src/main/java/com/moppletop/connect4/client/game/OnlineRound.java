package com.moppletop.connect4.client.game;

import com.moppletop.connect4.client.multiplayer.RemoteHandler;
import com.moppletop.connect4.common.game.Round;
import com.moppletop.connect4.common.multiplayer.in.PacketInPlaceTile;
import com.moppletop.connect4.common.player.Player;

import static java.lang.System.out;
import static org.fusesource.jansi.Ansi.Color.CYAN;
import static org.fusesource.jansi.Ansi.ansi;

public class OnlineRound extends Round
{

	private final RemoteHandler handler;
	private final Player ourPlayer;

	private boolean exit;

	public OnlineRound(RemoteHandler handler, Player ourPlayer)
	{
		this.handler = handler;
		this.ourPlayer = ourPlayer;
	}

	public void turnMade()
	{
		grid.showGrid();

		if (isOurTurn())
		{
			while (!nextTurn())
			{
			}

			if (isGameOver())
			{
				gameOver();
			}
		}
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

		if (grid.placeTile(column))
		{
			handler.sendPacket(new PacketInPlaceTile(column));
			return true;
		}

		return false;
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
		exit = true;
	}

	public void placeTile(int column)
	{
		grid.placeTile(column);

		if (isGameOver())
		{
			gameOver();
		}
	}

	public void setPlayer(Player player)
	{
		super.player = player;
		turnMade();
	}

	public boolean isOurTurn()
	{
		return ourPlayer.equals(player);
	}

	public boolean shouldExit()
	{
		return exit;
	}
}
