package com.moppletop.connect4.common.multiplayer.in;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.moppletop.connect4.common.multiplayer.Packet;
import com.moppletop.connect4.common.multiplayer.PacketType;

public class PacketInPlaceTile extends Packet
{

	private int column;

	public PacketInPlaceTile()
	{
		super(PacketType.IN_PLACE_TILE);
	}

	public PacketInPlaceTile(int column)
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
