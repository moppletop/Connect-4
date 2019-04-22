package com.moppletop.connect4.client.multiplayer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import com.moppletop.connect4.common.multiplayer.Packet;
import com.moppletop.connect4.common.multiplayer.PacketInterpreter;
import com.moppletop.connect4.common.multiplayer.PacketUser;
import com.moppletop.connect4.common.multiplayer.in.PacketInHandshake;
import com.moppletop.connect4.common.util.Log;
import com.moppletop.connect4.common.util.Utils;

public class RemoteServer implements Runnable, PacketUser
{

	private final PacketInterpreter interpreter;
	private final Socket socket;

	private DataOutputStream output;

	public RemoteServer(PacketInterpreter interpreter, Socket socket)
	{
		this.interpreter = interpreter;
		this.socket = socket;

		Utils.configureSocket(socket);
	}

	@Override
	public void run()
	{
		try
		{
			DataInputStream input = new DataInputStream(socket.getInputStream());
			this.output = new DataOutputStream(socket.getOutputStream());

			sendPacket(new PacketInHandshake());

			int packetId;
			while ((packetId = input.read()) != -1)
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
