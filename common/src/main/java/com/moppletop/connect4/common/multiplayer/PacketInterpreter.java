package com.moppletop.connect4.common.multiplayer;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PacketInterpreter
{

	private final JsonParser parser;
	private final Gson gson;
	private final Map<Class<? extends Packet>, PacketListener<? extends Packet>> listeners;

	public PacketInterpreter()
	{
		parser = new JsonParser();
		gson = new Gson();
		listeners = new HashMap<>();
	}

	public <T extends Packet> PacketInterpreter addListener(Class<T> clazz, PacketListener<T> listener)
	{
		if (listeners.containsKey(clazz))
		{
			throw new IllegalStateException("There is already a listener registered for " + clazz.getSimpleName());
		}

		listeners.put(clazz, listener);
		System.out.println("Registered a listener: " + clazz.getSimpleName());
		return this;
	}

	public void onMessageReceive(PacketUser source, String message)
	{
		System.out.println("Received " + message);

		JsonElement json = parser.parse(message);
		String clazz = json.getAsJsonObject().get("className").getAsString();
		Class<? extends Packet> aClass;

		try
		{
			aClass = (Class<? extends Packet>) Class.forName(clazz);
		}
		catch (ClassNotFoundException ex)
		{
			ex.printStackTrace();
			return;
		}

		PacketListener listener = listeners.get(aClass);

		if (listener == null)
		{
			return;
		}

		Packet packet = gson.fromJson(json, aClass);

		listener.onPacketReceived(source, packet);
	}

	public void sendPacket(PacketUser destination, Packet packet)
	{
		JsonObject json = gson.toJsonTree(packet).getAsJsonObject();
		json.addProperty("className", packet.getClass().getName());

		System.out.println("Sending " + json.toString());
		destination.sendPacket(json.toString());
	}
}
