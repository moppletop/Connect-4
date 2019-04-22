package com.moppletop.connect4.common.multiplayer.in;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.moppletop.connect4.common.multiplayer.Packet;
import com.moppletop.connect4.common.multiplayer.PacketType;

public class PacketInHandshake extends Packet
{

	public PacketInHandshake()
	{
		super(PacketType.IN_HANDSHAKE);
	}

	@Override
	public void read(DataInputStream stream) throws IOException
	{
	}

	@Override
	public void write(DataOutputStream stream) throws IOException
	{
	}
}
