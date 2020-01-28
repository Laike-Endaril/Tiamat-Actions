package com.fantasticsource.tiamatactions;

import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tiamatactions.gui.ActionSelectorGUI;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class Network
{
    public static final SimpleNetworkWrapper WRAPPER = new SimpleNetworkWrapper(MODID);
    private static int discriminator = 0;

    public static void init()
    {
        WRAPPER.registerMessage(RequestOpenActionEditorPacketHandler.class, RequestOpenActionEditorPacket.class, discriminator++, Side.SERVER);
        WRAPPER.registerMessage(OpenActionSelectorPacketHandler.class, OpenActionSelectorPacket.class, discriminator++, Side.CLIENT);
    }


    public static class RequestOpenActionEditorPacket implements IMessage
    {
        public RequestOpenActionEditorPacket()
        {
            //Required
        }

        @Override
        public void toBytes(ByteBuf buf)
        {
        }

        @Override
        public void fromBytes(ByteBuf buf)
        {
        }
    }

    public static class RequestOpenActionEditorPacketHandler implements IMessageHandler<RequestOpenActionEditorPacket, IMessage>
    {
        @Override
        public IMessage onMessage(RequestOpenActionEditorPacket packet, MessageContext ctx)
        {
            EntityPlayerMP player = ctx.getServerHandler().player;
            if (player.isCreative()) WRAPPER.sendTo(new OpenActionSelectorPacket(), player);
            return null;
        }
    }


    public static class OpenActionSelectorPacket implements IMessage
    {
        String[] list;

        public OpenActionSelectorPacket()
        {
            //Required
        }

        @Override
        public void toBytes(ByteBuf buf)
        {
            list = CAction.allActions.keySet().toArray(new String[0]);
            buf.writeInt(list.length);
            for (String s : list) ByteBufUtils.writeUTF8String(buf, s);
        }

        @Override
        public void fromBytes(ByteBuf buf)
        {
            int size = buf.readInt();
            list = new String[size];

            for (int i = 0; i < size; i++) list[i] = ByteBufUtils.readUTF8String(buf);
        }
    }

    public static class OpenActionSelectorPacketHandler implements IMessageHandler<OpenActionSelectorPacket, IMessage>
    {
        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(OpenActionSelectorPacket packet, MessageContext ctx)
        {
            Minecraft.getMinecraft().addScheduledTask(() ->
            {
                new ActionSelectorGUI(packet.list);
            });
            return null;
        }
    }
}