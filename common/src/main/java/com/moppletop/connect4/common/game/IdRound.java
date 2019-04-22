package com.moppletop.connect4.common.game;

import java.util.HashMap;
import java.util.Map;

import com.moppletop.connect4.common.player.Player;

public abstract class IdRound<T extends Player> extends Round<T>
{

	protected final Map<Byte, T> playerById;

	protected IdRound()
	{
		super();

		playerById = new HashMap<>();
	}
}
