package com.moppletop.connect4.common.game;

import java.util.Scanner;

import com.moppletop.connect4.common.grid.Grid;
import com.moppletop.connect4.common.player.Player;

import static java.lang.System.in;

public abstract class Round<T extends Player>
{

	protected final Grid grid;
	protected final Scanner scanner;

	protected T player;
	private GameResult result;

	protected Round()
	{
		grid = new Grid(this);
		scanner = new Scanner(in);
	}

	protected abstract boolean nextTurn();

	protected abstract void gameOver();

	public T getPlayer()
	{
		return player;
	}

	public void setResult(GameResult result)
	{
		this.result = result;
	}

	public GameResult getResult()
	{
		return result;
	}

	public boolean isGameOver()
	{
		return result != null;
	}
}
