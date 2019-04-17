package com.moppletop.connect4.common.multiplayer.in;

import com.moppletop.connect4.common.multiplayer.Packet;

public class PacketInPlaceTile implements Packet
{

	private final int column;

	public PacketInPlaceTile(int column)
	{
		this.column = column;
	}

	public int getColumn()
	{
		return column;
	}
}
