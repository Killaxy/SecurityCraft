package org.freeforums.geforce.securitycraft.network.packets;

import org.freeforums.geforce.securitycraft.api.IPasswordProtected;
import org.freeforums.geforce.securitycraft.main.Utils.BlockUtils;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSCheckPassword implements IMessage{
	
	private String password;
	private int x, y, z;
	
	public PacketSCheckPassword(){
		
	}
	
	public PacketSCheckPassword(int x, int y, int z, String code){
		this.x = x;
		this.y = y;
		this.z = z;
		this.password = code;
	}

	public void toBytes(ByteBuf par1ByteBuf) {
		par1ByteBuf.writeInt(x);
		par1ByteBuf.writeInt(y);
		par1ByteBuf.writeInt(z);
		ByteBufUtils.writeUTF8String(par1ByteBuf, password);
	}

	public void fromBytes(ByteBuf par1ByteBuf) {
		this.x = par1ByteBuf.readInt();
		this.y = par1ByteBuf.readInt();
		this.z = par1ByteBuf.readInt();
		this.password = ByteBufUtils.readUTF8String(par1ByteBuf);
	}
	
public static class Handler extends PacketHelper implements IMessageHandler<PacketSCheckPassword, IMessage> {

	public IMessage onMessage(PacketSCheckPassword packet, MessageContext ctx) {
		BlockPos pos = BlockUtils.toPos(packet.x, packet.y, packet.z);
		String password = packet.password;
		EntityPlayer player = ctx.getServerHandler().playerEntity;

		if(getWorld(player).getTileEntity(pos) != null && getWorld(player).getTileEntity(pos) instanceof IPasswordProtected){
			if(((IPasswordProtected) getWorld(player).getTileEntity(pos)).getPassword().matches(password)){
				((EntityPlayerMP) player).closeScreen();
				((IPasswordProtected) getWorld(player).getTileEntity(pos)).activate(player);
			}
		}
		
		return null;
	}
	
}

}
