package com.fantasticsource.tiamatactions;

import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tiamatactions.gui.ActionSelectionGUI;
import com.fantasticsource.tiamatactions.gui.MainActionEditorGUI;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
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
        WRAPPER.registerMessage(RequestOpenActionSelectorPacketHandler.class, RequestOpenActionSelectorPacket.class, discriminator++, Side.SERVER);
        WRAPPER.registerMessage(OpenActionSelectorPacketHandler.class, OpenActionSelectorPacket.class, discriminator++, Side.CLIENT);
        WRAPPER.registerMessage(OpenActionEditorPacketHandler.class, OpenActionEditorPacket.class, discriminator++, Side.CLIENT);
    }


    public static class RequestOpenActionSelectorPacket implements IMessage
    {
        public RequestOpenActionSelectorPacket()
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

    public static class RequestOpenActionSelectorPacketHandler implements IMessageHandler<RequestOpenActionSelectorPacket, IMessage>
    {
        @Override
        public IMessage onMessage(RequestOpenActionSelectorPacket packet, MessageContext ctx)
        {
            EntityPlayerMP player = ctx.getServerHandler().player;
            if (player.isCreative())
            {
                FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> WRAPPER.sendTo(new OpenActionSelectorPacket(), player));
            }
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
            list = CAction.ALL_ACTIONS.keySet().toArray(new String[0]);
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
            Minecraft mc = Minecraft.getMinecraft();
            mc.addScheduledTask(() -> new ActionSelectionGUI(packet.list));
            return null;
        }
    }


    public static class OpenActionEditorPacket implements IMessage
    {
        String[] list;

        public OpenActionEditorPacket()
        {
            //Required
        }

        @Override
        public void toBytes(ByteBuf buf)
        {
            list = CAction.ALL_ACTIONS.keySet().toArray(new String[0]);
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

    public static class OpenActionEditorPacketHandler implements IMessageHandler<OpenActionEditorPacket, IMessage>
    {
        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(OpenActionEditorPacket packet, MessageContext ctx)
        {
            Minecraft.getMinecraft().addScheduledTask(() ->
            {
                new MainActionEditorGUI(packet.list);
            });
            return null;
        }
    }
}
