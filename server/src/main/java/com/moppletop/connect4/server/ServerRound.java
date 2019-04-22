package com.moppletop.connect4.server;

import com.moppletop.connect4.common.game.IdRound;
import com.moppletop.connect4.common.multiplayer.Packet;
import com.moppletop.connect4.common.multiplayer.PacketInterpreter;
import com.moppletop.connect4.common.multiplayer.out.PacketOutPlaceTile;
import com.moppletop.connect4.common.multiplayer.out.PacketOutPlayerTurn;
import com.moppletop.connect4.server.player.RemotePlayer;

public class ServerRound extends IdRound<RemotePlayer> implements Runnable
{

	private final PacketInterpreter packetInterpreter;
	private final RemotePlayer playerA, playerB;

	private Integer nextColumn;

	public ServerRound(PacketInterpreter packetInterpreter, RemotePlayer playerA, RemotePlayer playerB)
	{
		this.packetInterpreter = packetInterpreter;
		this.playerA = playerA;
		this.playerB = playerB;

		playerById.put(playerA.getId(), playerA);
		playerById.put(playerB.getId(), playerB);
	}

	@Override
	public void run()
	{
		while (true)
		{
			changePlayers();
			grid.showGrid();

			while (!nextTurn())
			{
				try
				{
					Thread.sleep(50);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}

			if (isGameOver())
			{
				gameOver();
				return;
			}
		}
	}

	private void changePlayers()
	{
		if (player == null)
		{
			player = Math.random() < 0.5 ? playerA : playerB;
		}

		player = player.equals(playerA) ? playerB : playerA;
		broadcastPacket(new PacketOutPlayerTurn(player.getId()));
	}

	@Override
	protected boolean nextTurn()
	{
		if (nextColumn == null)
		{
			return false;
		}

		if (grid.placeTile(nextColumn))
		{
			packetInterpreter.sendPacket(player.equals(playerA) ? playerB : playerA, new PacketOutPlaceTile(nextColumn));
			nextColumn = null;
			return true;
		}

		return false;
	}

	@Override
	protected void gameOver()
	{
	}

	public void setNextColumn(Integer nextColumn)
	{
		this.nextColumn = nextColumn;
	}

	private void broadcastPacket(Packet packet)
	{
		playerById.values().forEach(destination -> packetInterpreter.sendPacket(destination, packet));
	}
}
