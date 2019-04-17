package com.moppletop.connect4.client.menu;

import com.moppletop.connect4.client.Connect4Client;

public abstract class MenuAction implements Runnable
{

	protected final Connect4Client client;
	private final int id;
	private final String name;

	protected MenuAction(Connect4Client client, int id, String name)
	{
		this.client = client;
		this.id = id;
		this.name = name;
	}

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}
}
