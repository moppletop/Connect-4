package com.moppletop.connect4.common.multiplayer.out;

import com.moppletop.connect4.common.multiplayer.Packet;

public class PacketOutPlaceTile implements Packet
{

	private final int column;

	public PacketOutPlaceTile(int column)
	{
		this.column = column;
	}

	public int getColumn()
	{
		return column;
	}
}
