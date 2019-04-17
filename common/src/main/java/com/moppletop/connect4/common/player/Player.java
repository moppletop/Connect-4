package com.moppletop.connect4.common.player;

import org.fusesource.jansi.Ansi.Color;

public class Player
{

	private final String name;
	private final Color colour;

	public Player(String name, Color colour)
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

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Player)
		{
			return name.equals(((Player) obj).name);
		}

		return false;
	}

	@Override
	public int hashCode()
	{
		return name.hashCode();
	}
}
