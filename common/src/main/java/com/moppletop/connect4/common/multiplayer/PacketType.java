package com.moppletop.connect4.common.multiplayer;

import java.util.HashMap;
import java.util.Map;

import com.moppletop.connect4.common.multiplayer.in.PacketInHandshake;
import com.moppletop.connect4.common.multiplayer.in.PacketInPlaceTile;
import com.moppletop.connect4.common.multiplayer.out.PacketOutPlaceTile;
import com.moppletop.connect4.common.multiplayer.out.PacketOutPlayerInfo;
import com.moppletop.connect4.common.multiplayer.out.PacketOutPlayerTurn;

public enum PacketType
{

	// Inward bound packets
	IN_HANDSHAKE(0x01, PacketInHandshake.class),
	IN_PLACE_TILE(0x02, PacketInPlaceTile.class),

	// Outward bound packets
	OUT_PLACE_TILE(0x10, PacketOutPlaceTile.class),
	OUT_PLAYER_INFO(0x11, PacketOutPlayerInfo.class),
	OUT_PLAYER_TURN(0x12, PacketOutPlayerTurn.class)

	;

	private static Map<Byte, PacketType> FROM_ID = new HashMap<>();

	static
	{
		for (PacketType type : values())
		{
			FROM_ID.put(type.getId(), type);
		}
	}

	public static PacketType getFromId(byte id)
	{
		return FROM_ID.get(id);
	}

	private final byte id;
	private final Class<? extends Packet> clazz;

	PacketType(int id, Class<? extends Packet> clazz)
	{
		this.id = (byte) id;
		this.clazz = clazz;
	}

	public byte getId()
	{
		return id;
	}

	public Class<? extends Packet> getClazz()
	{
		return clazz;
	}
}
