package com.moppletop.connect4.common.multiplayer;

@FunctionalInterface
public interface PacketListener<T extends Packet>
{

	void onPacketReceived(PacketUser source, T packet);

}
