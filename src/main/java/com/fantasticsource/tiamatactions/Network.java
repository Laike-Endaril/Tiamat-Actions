package com.fantasticsource.tiamatactions;

import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tiamatactions.config.TiamatActionsConfig;
import com.fantasticsource.tiamatactions.gui.ActionSelectionGUI;
import com.fantasticsource.tiamatactions.gui.actioneditor.ActionEditorGUI;
import com.fantasticsource.tiamatactions.gui.actioneditor.MainActionEditorGUI;
import com.fantasticsource.tools.Tools;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class Network
{
    protected static final LinkedHashMap<String, String> KEYBOUND_ACTION_NAMES = new LinkedHashMap<>();

    public static final SimpleNetworkWrapper WRAPPER = new SimpleNetworkWrapper(MODID);
    private static int discriminator = 0;

    public static void init()
    {
        WRAPPER.registerMessage(RequestOpenActionSelectorPacketHandler.class, RequestOpenActionSelectorPacket.class, discriminator++, Side.SERVER);
        WRAPPER.registerMessage(OpenActionSelectorPacketHandler.class, OpenActionSelectorPacket.class, discriminator++, Side.CLIENT);

        WRAPPER.registerMessage(OpenMainActionEditorPacketHandler.class, OpenMainActionEditorPacket.class, discriminator++, Side.CLIENT);

        WRAPPER.registerMessage(RequestOpenActionEditorPacketHandler.class, RequestOpenActionEditorPacket.class, discriminator++, Side.SERVER);
        WRAPPER.registerMessage(OpenActionEditorPacketHandler.class, OpenActionEditorPacket.class, discriminator++, Side.CLIENT);

        WRAPPER.registerMessage(SaveActionPacketHandler.class, SaveActionPacket.class, discriminator++, Side.SERVER);
        WRAPPER.registerMessage(DeleteActionPacketHandler.class, DeleteActionPacket.class, discriminator++, Side.SERVER);

        WRAPPER.registerMessage(ExecuteKeyboundActionPacketHandler.class, ExecuteKeyboundActionPacket.class, discriminator++, Side.SERVER);

        for (String s : TiamatActionsConfig.modpackSettings.keyboundActions)
        {
            String[] tokens = Tools.fixedSplit(s, ";");
            if (tokens.length != 2) continue;


            KEYBOUND_ACTION_NAMES.put(tokens[0].trim(), tokens[1].trim());
        }
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


    public static class OpenMainActionEditorPacket implements IMessage
    {
        String[] list;

        public OpenMainActionEditorPacket()
        {
            //Required
        }

        @Override
        public void toBytes(ByteBuf buf)
        {
            buf.writeInt(CAction.ALL_ACTIONS.size());
            for (String s : CAction.ALL_ACTIONS.keySet()) ByteBufUtils.writeUTF8String(buf, s);
        }

        @Override
        public void fromBytes(ByteBuf buf)
        {
            int size = buf.readInt();
            list = new String[size];

            for (int i = 0; i < size; i++) list[i] = ByteBufUtils.readUTF8String(buf);
        }
    }

    public static class OpenMainActionEditorPacketHandler implements IMessageHandler<OpenMainActionEditorPacket, IMessage>
    {
        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(OpenMainActionEditorPacket packet, MessageContext ctx)
        {
            Minecraft.getMinecraft().addScheduledTask(() ->
            {
                GuiScreen gui = Minecraft.getMinecraft().currentScreen;
                if (gui == null || gui instanceof MainActionEditorGUI) new MainActionEditorGUI(packet.list);
            });
            return null;
        }
    }


    public static class RequestOpenActionEditorPacket implements IMessage
    {
        String actionName;

        public RequestOpenActionEditorPacket()
        {
            //Required
        }

        public RequestOpenActionEditorPacket(String actionName)
        {
            this.actionName = actionName;
        }

        @Override
        public void toBytes(ByteBuf buf)
        {
            ByteBufUtils.writeUTF8String(buf, actionName);
        }

        @Override
        public void fromBytes(ByteBuf buf)
        {
            actionName = ByteBufUtils.readUTF8String(buf);
        }
    }

    public static class RequestOpenActionEditorPacketHandler implements IMessageHandler<RequestOpenActionEditorPacket, IMessage>
    {
        @Override
        public IMessage onMessage(RequestOpenActionEditorPacket packet, MessageContext ctx)
        {
            EntityPlayerMP player = ctx.getServerHandler().player;
            if (player.isCreative())
            {
                if (!CAction.ALL_ACTIONS.containsKey(packet.actionName)) new CAction(packet.actionName).save();
                CAction action = CAction.ALL_ACTIONS.get(packet.actionName);
                FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> WRAPPER.sendTo(new OpenActionEditorPacket(action), player));
            }
            return null;
        }
    }


    public static class OpenActionEditorPacket implements IMessage
    {
        CAction action = new CAction();
        String[] otherActionNames;

        public OpenActionEditorPacket()
        {
            //Required
        }

        public OpenActionEditorPacket(CAction action)
        {
            this.action = action;
            otherActionNames = new String[CAction.ALL_ACTIONS.size() - 1];
            int i = 0;
            for (String name : CAction.ALL_ACTIONS.keySet())
            {
                if (name.equals(action.name)) continue;

                otherActionNames[i++] = name;
            }
        }

        @Override
        public void toBytes(ByteBuf buf)
        {
            action.write(buf);

            buf.writeInt(otherActionNames.length);
            for (String name : otherActionNames) ByteBufUtils.writeUTF8String(buf, name);
        }

        @Override
        public void fromBytes(ByteBuf buf)
        {
            action.read(buf);

            otherActionNames = new String[buf.readInt()];
            for (int i = 0; i < otherActionNames.length; i++) otherActionNames[i] = ByteBufUtils.readUTF8String(buf);
        }
    }

    public static class OpenActionEditorPacketHandler implements IMessageHandler<OpenActionEditorPacket, IMessage>
    {
        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(OpenActionEditorPacket packet, MessageContext ctx)
        {
            Minecraft mc = Minecraft.getMinecraft();
            mc.addScheduledTask(() -> new ActionEditorGUI(packet.action, packet.otherActionNames));
            return null;
        }
    }


    public static class SaveActionPacket implements IMessage
    {
        String oldName;
        CAction action = new CAction();

        public SaveActionPacket()
        {
            //Required
        }

        public SaveActionPacket(String oldName, CAction action)
        {
            this.oldName = oldName;
            this.action = action;
        }

        @Override
        public void toBytes(ByteBuf buf)
        {
            ByteBufUtils.writeUTF8String(buf, oldName);
            action.write(buf);
        }

        @Override
        public void fromBytes(ByteBuf buf)
        {
            oldName = ByteBufUtils.readUTF8String(buf);
            action.read(buf);
        }
    }

    public static class SaveActionPacketHandler implements IMessageHandler<SaveActionPacket, IMessage>
    {
        @Override
        public IMessage onMessage(SaveActionPacket packet, MessageContext ctx)
        {
            EntityPlayerMP player = ctx.getServerHandler().player;
            if (player.isCreative())
            {
                CAction oldAction = CAction.ALL_ACTIONS.get(packet.oldName);
                if (oldAction != null) oldAction.delete();
                packet.action.save();

                WRAPPER.sendTo(new OpenMainActionEditorPacket(), ctx.getServerHandler().player);
            }
            return null;
        }
    }


    public static class DeleteActionPacket implements IMessage
    {
        String name;

        public DeleteActionPacket()
        {
            //Required
        }

        public DeleteActionPacket(String name)
        {
            this.name = name;
        }

        @Override
        public void toBytes(ByteBuf buf)
        {
            ByteBufUtils.writeUTF8String(buf, name);
        }

        @Override
        public void fromBytes(ByteBuf buf)
        {
            name = ByteBufUtils.readUTF8String(buf);
        }
    }

    public static class DeleteActionPacketHandler implements IMessageHandler<DeleteActionPacket, IMessage>
    {
        @Override
        public IMessage onMessage(DeleteActionPacket packet, MessageContext ctx)
        {
            EntityPlayerMP player = ctx.getServerHandler().player;
            if (player.isCreative())
            {
                CAction action = CAction.ALL_ACTIONS.get(packet.name);
                if (action != null) action.delete();
            }
            return null;
        }
    }


    public static class ExecuteKeyboundActionPacket implements IMessage
    {
        String key;

        public ExecuteKeyboundActionPacket()
        {
            //Required
        }

        public ExecuteKeyboundActionPacket(String key)
        {
            this.key = key;
        }

        @Override
        public void toBytes(ByteBuf buf)
        {
            ByteBufUtils.writeUTF8String(buf, key);
        }

        @Override
        public void fromBytes(ByteBuf buf)
        {
            key = ByteBufUtils.readUTF8String(buf);
        }
    }

    public static class ExecuteKeyboundActionPacketHandler implements IMessageHandler<ExecuteKeyboundActionPacket, IMessage>
    {
        @Override
        public IMessage onMessage(ExecuteKeyboundActionPacket packet, MessageContext ctx)
        {
            String actionName = KEYBOUND_ACTION_NAMES.get(packet.key);
            if (actionName == null) return null;

            CAction action = CAction.ALL_ACTIONS.get(actionName);
            if (action == null) return null;

            EntityPlayerMP player = ctx.getServerHandler().player;
            action.queue(player, "Main", null);
            return null;
        }
    }
}
