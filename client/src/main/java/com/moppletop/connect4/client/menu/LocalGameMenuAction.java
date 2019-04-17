package com.moppletop.connect4.client.menu;

import com.moppletop.connect4.client.Connect4Client;
import com.moppletop.connect4.client.game.LocalRound;

public class LocalGameMenuAction extends MenuAction
{

	public LocalGameMenuAction(Connect4Client client)
	{
		super(client, 1, "Local Game");
	}

	@Override
	public void run()
	{
		new LocalRound();
	}
}
