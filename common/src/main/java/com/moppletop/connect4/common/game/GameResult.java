package com.moppletop.connect4.common.game;

import java.util.List;

import com.moppletop.connect4.common.player.Player;
import com.moppletop.connect4.common.grid.GridPos;

public class GameResult
{

	private final Player winner;
	private final List<GridPos> winningTiles;

	public GameResult(Player winner, List<GridPos> winningTiles)
	{
		this.winner = winner;
		this.winningTiles = winningTiles;
	}

	public Player getWinner()
	{
		return winner;
	}

	public List<GridPos> getWinningTiles()
	{
		return winningTiles;
	}
}
