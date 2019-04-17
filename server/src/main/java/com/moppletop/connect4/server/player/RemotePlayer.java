package com.moppletop.connect4.server.player;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import org.fusesource.jansi.Ansi.Color;

import com.moppletop.connect4.common.multiplayer.PacketInterpreter;
import com.moppletop.connect4.common.multiplayer.PacketUser;
import com.moppletop.connect4.common.player.Player;
import com.moppletop.connect4.common.util.Utils;

public class RemotePlayer extends Player implements Runnable, PacketUser
{

	private final PacketInterpreter interpreter;
	private final Socket socket;

	private Scanner input;
	private PrintWriter output;

	public RemotePlayer(PacketInterpreter interpreter, String name, Color colour, Socket socket)
	{
		super(name, colour);

		this.interpreter = interpreter;
		this.socket = socket;

		Utils.configureSocket(socket);
	}

	@Override
	public void run()
	{
		try
		{
			this.input = new Scanner(socket.getInputStream());
			this.output = new PrintWriter(socket.getOutputStream(), true);

			while (input.hasNextLine() && !socket.isClosed())
			{
				interpreter.onMessageReceive(this, input.nextLine());
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			try
			{
				socket.close();
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}

	@Override
	public void sendPacket(String packetContents)
	{
		output.println(packetContents);
	}

	@Override
	public void close() throws IOException
	{
		socket.close();
	}
}
