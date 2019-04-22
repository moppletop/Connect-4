package com.moppletop.connect4.common.multiplayer;

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static com.moppletop.connect4.common.util.Log.debug;

public class PacketInterpreter
{

	private final Map<PacketType, PacketListener<? extends Packet>> listeners;

	public PacketInterpreter()
	{
		listeners = new HashMap<>();
	}

	public <T extends Packet> PacketInterpreter addListener(PacketType type, PacketListener<T> listener)
	{
		if (listeners.containsKey(type))
		{
			throw new IllegalStateException("There is already a listener registered for " + type.toString());
		}

		listeners.put(type, listener);
		debug("Registered a listener: " + type.toString());
		return this;
	}

	public void onMessageReceive(PacketUser source, byte packetId, DataInputStream stream)
	{
		PacketType type = PacketType.getFromId(packetId);

		if (type == null)
		{
			debug("No PacketType mapped for " + packetId);
			return;
		}

		debug("Received " + type);

		Packet packet;

		try
		{
			packet = type.getClazz().getConstructor().newInstance();
		}
		catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
		{
			e.printStackTrace();
			return;
		}

		try
		{
			packet.read(stream);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}

		PacketListener listener = listeners.get(packet.getPacketType());

		if (listener == null)
		{
			debug("No PacketListener mapped for " + type);
			return;
		}

		listener.onPacketReceived(source, packet);
	}

	public void sendPacket(PacketUser destination, Packet packet)
	{
		debug("Sending " + packet.getPacketType());

		try
		{
			destination.sendPacket(packet);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
