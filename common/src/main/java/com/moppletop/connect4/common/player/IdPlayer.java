package com.moppletop.connect4.common.player;

import org.fusesource.jansi.Ansi.Color;

public class IdPlayer extends Player
{

	private final byte id;

	public IdPlayer(byte id, Color colour, String name)
	{
		super(name, colour);

		this.id = id;
	}

	public byte getId()
	{
		return id;
	}

	@Override
	public String toString()
	{
		return getName() + " (" + getId()+ ")";
	}
}
