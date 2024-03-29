package com.moppletop.connect4.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.moppletop.connect4.common.multiplayer.PacketInterpreter;
import com.moppletop.connect4.common.multiplayer.PacketListener;
import com.moppletop.connect4.common.multiplayer.PacketType;
import com.moppletop.connect4.common.multiplayer.in.PacketInHandshake;
import com.moppletop.connect4.common.multiplayer.in.PacketInPlaceTile;
import com.moppletop.connect4.common.multiplayer.out.PacketOutPlayerInfo;
import com.moppletop.connect4.common.util.Utils;
import com.moppletop.connect4.server.player.RemotePlayer;

import static com.moppletop.connect4.common.util.Log.info;
import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

public class Connect4Server
{

	public static void main(String[] args) throws IOException
	{
		Utils.enableAnsiIfNeeded();
		new Connect4Server();
	}

	private final List<RemotePlayer> players;
	private final PacketInterpreter packetInterpreter;

	private ServerRound round;
	private byte nextId;

	private Connect4Server() throws IOException
	{
		showBanner();

		this.players = new ArrayList<>();
		this.packetInterpreter = new PacketInterpreter();
		this.nextId = Byte.MIN_VALUE;

		packetInterpreter
				.addListener(PacketType.IN_HANDSHAKE, onHandshake())
				.addListener(PacketType.IN_PLACE_TILE, onPlaceTile());

		ServerSocket serverSocket = new ServerSocket(4444);
		ExecutorService threadPool = Executors.newFixedThreadPool(5);

		while (true)
		{
			info("Waiting for connections...");
			threadPool.execute(new RemotePlayer(packetInterpreter, getNextId(), "Player 1", YELLOW, serverSocket.accept()));
			threadPool.execute(new RemotePlayer(packetInterpreter, getNextId(), "Player 2", RED, serverSocket.accept()));

			while (round == null || !round.isGameOver())
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

			players.clear();
			round = null;
		}
	}

	private void showBanner()
	{
		try
		{
			List<String> banner = Utils.readResource("/banner.txt");
			banner.forEach(line -> info(ansi()
					.fg(CYAN)
					.a(line)
					.reset()));
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	private byte getNextId()
	{
		if (nextId == Byte.MAX_VALUE)
		{
			nextId = Byte.MIN_VALUE;
		}

		return nextId++;
	}

	private PacketListener<PacketInHandshake> onHandshake()
	{
		return (source, packet) ->
		{
			if (source instanceof RemotePlayer)
			{
				RemotePlayer newPlayer = (RemotePlayer) source;

				players.add(newPlayer);

				if (players.size() == 2)
				{
					for (RemotePlayer forPlayer : players)
					{
						packetInterpreter.sendPacket(forPlayer, new PacketOutPlayerInfo(forPlayer));

						for (RemotePlayer targetPlayer : players)
						{
							if (forPlayer.equals(targetPlayer))
							{
								continue;
							}

							packetInterpreter.sendPacket(forPlayer, new PacketOutPlayerInfo(targetPlayer));
						}
					}

					round = new ServerRound(packetInterpreter, players.get(0), players.get(1));
					new Thread(round)
							.start();
				}
			}
		};
	}

	private PacketListener<PacketInPlaceTile> onPlaceTile()
	{
		return (source, packet) ->
		{
			if (round == null)
			{
				System.err.println("No round");
				return;
			}

			if (source instanceof RemotePlayer)
			{
				RemotePlayer newPlayer = (RemotePlayer) source;

//				if (!newPlayer.equals(round.getPlayer()))
//				{
//					System.err.println("Not correct player");
//					return;
//				}

				round.setNextColumn(packet.getColumn());
			}
		};
	}
}
