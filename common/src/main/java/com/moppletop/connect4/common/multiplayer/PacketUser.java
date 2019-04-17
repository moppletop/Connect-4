package com.moppletop.connect4.common.multiplayer;

import java.io.IOException;

public interface PacketUser
{

	void sendPacket(String packetContents);

	void close() throws IOException;

}
