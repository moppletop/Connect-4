package com.moppletop.connect4.common.multiplayer.out;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.moppletop.connect4.common.multiplayer.Packet;
import com.moppletop.connect4.common.multiplayer.PacketType;

public class PacketOutPlayerTurn extends Packet
{

	private byte id;

	public PacketOutPlayerTurn()
	{
		super(PacketType.OUT_PLAYER_TURN);
	}

	public PacketOutPlayerTurn(byte id)
	{
		this();

		this.id = id;
	}

	@Override
	public void read(DataInputStream stream) throws IOException
	{
		id = stream.readByte();
	}

	@Override
	public void write(DataOutputStream stream) throws IOException
	{
		stream.writeByte(id);
	}

	public byte getId()
	{
		return id;
	}
}
