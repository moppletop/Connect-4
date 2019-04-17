package com.moppletop.connect4.client.menu;

import com.moppletop.connect4.client.Connect4Client;

public class QuitMenuAction extends MenuAction
{

	public QuitMenuAction(Connect4Client client)
	{
		super(client, 0, "Quit");
	}

	@Override
	public void run()
	{
		System.exit(0);
	}
}
