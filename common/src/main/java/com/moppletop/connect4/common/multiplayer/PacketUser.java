package com.moppletop.connect4.common.multiplayer;

import java.io.IOException;

public interface PacketUser
{

	void sendPacket(Packet packet) throws IOException;

	void close() throws IOException;

}
