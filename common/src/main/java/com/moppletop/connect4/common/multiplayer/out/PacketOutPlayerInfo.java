package com.moppletop.connect4.common.multiplayer.out;

import org.fusesource.jansi.Ansi.Color;

import com.moppletop.connect4.common.multiplayer.Packet;

public class PacketOutPlayerInfo implements Packet
{

	private final String name;
	private final Color colour;

	public PacketOutPlayerInfo(String name, Color colour)
	{
		this.name = name;
		this.colour = colour;
	}

	public String getName()
	{
		return name;
	}

	public Color getColour()
	{
		return colour;
	}
}
