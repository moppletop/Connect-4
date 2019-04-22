package com.moppletop.connect4.server.player;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import org.fusesource.jansi.Ansi.Color;

import com.moppletop.connect4.common.multiplayer.Packet;
import com.moppletop.connect4.common.multiplayer.PacketInterpreter;
import com.moppletop.connect4.common.multiplayer.PacketUser;
import com.moppletop.connect4.common.player.IdPlayer;
import com.moppletop.connect4.common.util.Log;
import com.moppletop.connect4.common.util.Utils;

public class RemotePlayer extends IdPlayer implements Runnable, PacketUser
{

	private final PacketInterpreter interpreter;
	private final Socket socket;

	private DataOutputStream output;

	public RemotePlayer(PacketInterpreter interpreter, byte id, String name, Color colour, Socket socket)
	{
		super(id, colour, name);

		this.interpreter = interpreter;
		this.socket = socket;

		Utils.configureSocket(socket);
	}

	@Override
	public void run()
	{
		Log.info("Connection made from " + socket.getInetAddress().getHostAddress());

		try
		{
			DataInputStream input = new DataInputStream(socket.getInputStream());
			this.output = new DataOutputStream(socket.getOutputStream());

			int packetId;
			while ((packetId = input.read()) != -1 && !socket.isClosed())
			{
				interpreter.onMessageReceive(this, (byte) packetId, input);
			}
		}
		catch (SocketException ex)
		{
			if (socket.isClosed())
			{
				Log.debug("Socket was closed");
			}
			else
			{
				ex.printStackTrace();
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
				close();
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}

	@Override
	public void sendPacket(Packet packet) throws IOException
	{
		output.writeByte(packet.getPacketType().getId());
		packet.write(output);
		output.flush();
	}

	@Override
	public void close() throws IOException
	{
		socket.close();
	}
}
