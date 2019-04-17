package com.moppletop.connect4.common.grid;

public class GridPos
{

	private final int row, column;

	GridPos(int row, int column)
	{
		this.row = row;
		this.column = column;
	}

	public int getRow()
	{
		return row;
	}

	public int getColumn()
	{
		return column;
	}
}
