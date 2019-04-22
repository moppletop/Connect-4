package com.moppletop.connect4.client.multiplayer;

import java.io.IOException;
import java.net.Socket;

import com.moppletop.connect4.client.game.OnlineRound;
import com.moppletop.connect4.common.multiplayer.Packet;
import com.moppletop.connect4.common.multiplayer.PacketInterpreter;
import com.moppletop.connect4.common.multiplayer.PacketListener;
import com.moppletop.connect4.common.multiplayer.PacketType;
import com.moppletop.connect4.common.multiplayer.out.PacketOutPlaceTile;
import com.moppletop.connect4.common.multiplayer.out.PacketOutPlayerInfo;
import com.moppletop.connect4.common.multiplayer.out.PacketOutPlayerTurn;
import com.moppletop.connect4.common.player.IdPlayer;

import static com.moppletop.connect4.common.util.Log.debug;

public class RemoteHandler
{

	private final PacketInterpreter packetInterpreter;
	private final RemoteServer server;

	private IdPlayer ourPlayer, theirPlayer;
	private OnlineRound round;

	public RemoteHandler(String host) throws IOException
	{
		packetInterpreter = new PacketInterpreter();

		packetInterpreter
				.addListener(PacketType.OUT_PLAYER_INFO, onPlayerInfo())
				.addListener(PacketType.OUT_PLAYER_TURN, onPlayerTurn())
				.addListener(PacketType.OUT_PLACE_TILE, onPlaceTile());

		server = new RemoteServer(packetInterpreter, new Socket(host, 4444));

		new Thread(server)
				.start();

		while (round == null || !round.shouldExit())
		{
			try
			{
				Thread.sleep(250);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}

		try
		{
			server.close();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	public void sendPacket(Packet packet)
	{
		packetInterpreter.sendPacket(server, packet);
	}

	private PacketListener<PacketOutPlayerInfo> onPlayerInfo()
	{
		return (source, packet) ->
		{
			if (ourPlayer == null)
			{
				debug("Set our player to " + packet.getPlayer());
				ourPlayer = packet.getPlayer();
			}
			else
			{
				debug("Set their player to " + packet.getPlayer());
				theirPlayer = packet.getPlayer();
				round = new OnlineRound(this, ourPlayer, theirPlayer);
			}
		};
	}

	private PacketListener<PacketOutPlayerTurn> onPlayerTurn()
	{
		return (source, packet) ->
		{
			debug("Set current player to " + packet.getId());
			round.setPlayer(packet.getId());
		};
	}

	private PacketListener<PacketOutPlaceTile> onPlaceTile()
	{
		return (source, packet) ->
		{
			debug("Tile placed by other player at " + packet.getColumn());
			round.placeTile(packet.getColumn());
		};
	}
}
