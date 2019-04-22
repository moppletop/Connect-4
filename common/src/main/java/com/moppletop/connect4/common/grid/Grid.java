package com.moppletop.connect4.common.grid;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.fusesource.jansi.Ansi;

import com.moppletop.connect4.common.player.Player;
import com.moppletop.connect4.common.game.GameResult;
import com.moppletop.connect4.common.game.Round;
import com.moppletop.connect4.common.util.Utils;

import static com.moppletop.connect4.common.util.Log.*;
import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

public class Grid
{

	public static final int WIDTH = 7, HEIGHT = 6, LENGTH_TO_WIN = 4;
	private static final char TILE_CHAR = 'X';

	private final Round round;
	private final Player[][] grid;
	private final String bottomLine;

	public Grid(Round round)
	{
		this.round = round;
		this.grid = new Player[HEIGHT][WIDTH];

		StringBuilder builder = new StringBuilder()
				.append(" ");

		for (int i = 1; i <= WIDTH; i++)
		{
			builder.append(" ").append(i).append(" ");
		}

		this.bottomLine = builder.append(" ").toString();
	}

	public void showGrid()
	{
		Utils.clearConsole();

		Player player = round.isGameOver() ? round.getResult().getWinner() : round.getPlayer();

		Ansi ansi = ansi();

		if (player == null)
		{
			ansi
					.fg(WHITE)
					.a("Draw!")
					.reset();
		}
		else
		{
			ansi
					.fg(player.getColour())
					.a(player.getName())

					.fg(BLUE)
					.a(round.isGameOver() ? " Won!" : "'s Turn")

					.reset();
		}

		info(ansi);

		boolean winningTiles = round.getResult() != null && round.getResult().getWinningTiles() != null;

		for (int row = 0; row < grid.length; row++)
		{
			Player[] columns = grid[row];

			ansi = ansi()
					.fg(BLUE)
					.a("|");

			columnLoop:
			for (int column = 0; column < columns.length; column++)
			{
				if (winningTiles)
				{
					for (GridPos pos : round.getResult().getWinningTiles())
					{
						if (pos.getRow() == row && pos.getColumn() == column)
						{
							ansi
									.fg(WHITE)
									.a(" " + TILE_CHAR + " ");
							continue columnLoop;
						}
					}
				}

				colour(ansi, columns[column]);
			}

			ansi
					.fg(BLUE)
					.a("|");

			info(ansi.reset());
		}

		info(ansi()
				.fg(BLUE)
				.a(bottomLine)
				.reset());
	}

	public boolean placeTile(int column)
	{
		if (column < 1 || column > Grid.WIDTH)
		{
			return false;
		}

		column--;

		int row = getLowest(column);

		if (row < 0)
		{
			return false;
		}

		grid[row][column] = round.getPlayer();
		round.setResult(hasWon(row, column));

		return true;
	}

	private int getLowest(int column)
	{
		for (int i = 0; i < grid.length; i++)
		{
			if (grid[i][column] != null)
			{
				return --i;
			}
		}

		return grid.length - 1;
	}

	private GameResult hasWon(int row, int column)
	{
		Map<Integer, List<GridPos>> directionCount = new HashMap<>(4);

		int startRow, startColumn;
		int endRow, endColumn;
		int offset = LENGTH_TO_WIN - 1;

		{
			startRow = Math.max(0, row - offset);
			startColumn = Math.max(0, column - offset);
			endRow = Math.min(HEIGHT - 1, row + offset);
			endColumn = Math.min(WIDTH - 1, column + offset);

			// Vertical
			for (int thisRow = startRow; thisRow <= endRow; thisRow++)
			{
				incrementOrRemove(directionCount, 0, thisRow, column);
			}

			// Horizontal
			for (int thisColumn = startColumn; thisColumn <= endColumn; thisColumn++)
			{
				incrementOrRemove(directionCount, 1, row, thisColumn);
			}
		}
		{
			startRow = row - offset;
			startColumn = column - offset;
			endRow = row + offset;
			endColumn = column + offset;

			// Top Left -> Bottom Right
			for (int thisRow = startRow, thisColumn = startColumn; thisRow <= endRow && thisColumn <= endColumn; thisRow++, thisColumn++)
			{
				incrementOrRemove(directionCount, 2, thisRow, thisColumn);
			}

			// Bottom Left -> Top Right
			for (int thisRow = endRow, thisColumn = startColumn; thisRow >= startRow && thisColumn <= endColumn; thisRow--, thisColumn++)
			{
				incrementOrRemove(directionCount, 3, thisRow, thisColumn);
			}
		}

		List<GridPos> winningTiles = directionCount.values().stream()
				.filter(gridPos -> gridPos.size() >= LENGTH_TO_WIN)
				.flatMap(Collection::stream)
				.collect(Collectors.toList());

		if (winningTiles.size() >= LENGTH_TO_WIN)
		{
			return new GameResult(round.getPlayer(), winningTiles);
		}
		else if (isDraw())
		{
			return new GameResult(null, null);
		}

		return null;
	}

	private boolean isDraw()
	{
		for (Player[] rows : grid)
		{
			for (Player columns : rows)
			{
				if (columns == null)
				{
					return false;
				}
			}
		}

		return true;
	}

	private void incrementOrRemove(Map<Integer, List<GridPos>> map, int key, int row, int column)
	{
		if (row < 0 || column < 0 || row >= HEIGHT || column >= WIDTH)
		{
			return;
		}

		if (round.getPlayer().equals(grid[row][column]))
		{
			map.computeIfAbsent(key, k -> new LinkedList<>()).add(new GridPos(row, column));
		}
		else if (map.containsKey(key))
		{
			List<GridPos> pos = map.get(key);

			if (pos.size() < LENGTH_TO_WIN)
			{
				pos.clear();
			}
		}
	}

	private void colour(Ansi ansi, Player player)
	{
		if (player == null)
		{
			ansi.a("   ");
		}
		else
		{
			ansi
					.fg(player.getColour())
					.a(" " + TILE_CHAR + " ");
		}
	}
}
