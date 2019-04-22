package com.moppletop.connect4.common.multiplayer.out;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.fusesource.jansi.Ansi.Color;

import com.moppletop.connect4.common.multiplayer.Packet;
import com.moppletop.connect4.common.multiplayer.PacketType;
import com.moppletop.connect4.common.player.IdPlayer;
import com.moppletop.connect4.common.util.Utils;

public class PacketOutPlayerInfo extends Packet
{

	private IdPlayer player;

	public PacketOutPlayerInfo()
	{
		super(PacketType.OUT_PLAYER_INFO);
	}

	public PacketOutPlayerInfo(IdPlayer player)
	{
		this();

		this.player = player;
	}

	public IdPlayer getPlayer()
	{
		return player;
	}

	@Override
	public void read(DataInputStream stream) throws IOException
	{
		player = new IdPlayer(stream.readByte(), Color.values()[stream.readByte()], Utils.readString(stream));
	}

	@Override
	public void write(DataOutputStream stream) throws IOException
	{
		stream.writeByte(player.getId());
		stream.writeByte(player.getColour().ordinal());
		Utils.writeString(stream, player.getName());
	}
}
