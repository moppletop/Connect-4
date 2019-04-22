package com.moppletop.connect4.common.multiplayer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Packet
{

	private final PacketType packetType;

	public Packet(PacketType packetType)
	{
		this.packetType = packetType;
	}

	public abstract void read(DataInputStream stream) throws IOException;

	public abstract void write(DataOutputStream stream) throws IOException;

	public PacketType getPacketType()
	{
		return packetType;
	}
}
