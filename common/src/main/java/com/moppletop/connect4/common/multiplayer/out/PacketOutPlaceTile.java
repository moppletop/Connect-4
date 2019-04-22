package com.moppletop.connect4.common.multiplayer.out;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.moppletop.connect4.common.multiplayer.Packet;
import com.moppletop.connect4.common.multiplayer.PacketType;

public class PacketOutPlaceTile extends Packet
{

	private int column;

	public PacketOutPlaceTile()
	{
		super(PacketType.OUT_PLACE_TILE);
	}

	public PacketOutPlaceTile(int column)
	{
		this();

		this.column = column;
	}

	public int getColumn()
	{
		return column;
	}

	@Override
	public void read(DataInputStream stream) throws IOException
	{
		column = stream.readInt();
	}

	@Override
	public void write(DataOutputStream stream) throws IOException
	{
		stream.writeInt(column);
	}
}
