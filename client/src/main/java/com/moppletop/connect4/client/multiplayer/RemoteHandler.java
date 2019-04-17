package com.moppletop.connect4.client.multiplayer;

import java.io.IOException;
import java.net.Socket;

import com.moppletop.connect4.client.game.OnlineRound;
import com.moppletop.connect4.common.multiplayer.Packet;
import com.moppletop.connect4.common.multiplayer.PacketInterpreter;
import com.moppletop.connect4.common.multiplayer.PacketListener;
import com.moppletop.connect4.common.multiplayer.in.PacketInHandshake;
import com.moppletop.connect4.common.multiplayer.out.PacketOutPlaceTile;
import com.moppletop.connect4.common.multiplayer.out.PacketOutPlayerInfo;
import com.moppletop.connect4.common.multiplayer.out.PacketOutPlayerTurn;
import com.moppletop.connect4.common.player.Player;

public class RemoteHandler
{

	private final PacketInterpreter packetInterpreter;
	private final RemoteServer server;

	private OnlineRound round;

	public RemoteHandler(String host) throws IOException
	{
		packetInterpreter = new PacketInterpreter();

		packetInterpreter
				.addListener(PacketOutPlayerInfo.class, onPlayerInfo())
				.addListener(PacketOutPlayerTurn.class, onPlayerTurn())
				.addListener(PacketOutPlaceTile.class, onPlaceTile());

		server = new RemoteServer(packetInterpreter, new Socket(host, 4444));

		new Thread(server)
				.start();

		try
		{
			Thread.sleep(50);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		sendPacket(new PacketInHandshake());

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
			System.out.println("Set our player to " + packet.getName());
			round = new OnlineRound(this, new Player(packet.getName(), packet.getColour()));
		};
	}

	private PacketListener<PacketOutPlayerTurn> onPlayerTurn()
	{
		return (source, packet) ->
		{
			System.out.println("Set current player to " + packet.getName());

			round.setPlayer(new Player(packet.getName(), packet.getColour()));
		};
	}

	private PacketListener<PacketOutPlaceTile> onPlaceTile()
	{
		return (source, packet) ->
		{
			System.out.println("Tile placed by other player");
			round.placeTile(packet.getColumn());
		};
	}
}
