package com.fantasticsource.tiamatactions;

import com.fantasticsource.mctools.PlayerData;
import com.fantasticsource.tiamatactions.action.ActionQueue;
import com.fantasticsource.tiamatactions.action.CAction;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nullable;
import java.util.*;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;
import static net.minecraft.util.text.TextFormatting.AQUA;

public class Commands extends CommandBase
{
    private static ArrayList<String> subcommands = new ArrayList<>();

    static
    {
        subcommands.addAll(Arrays.asList("reload"));
        subcommands.addAll(Arrays.asList("execute"));
    }


    @Override
    public String getName()
    {
        return "tactions";
    }

    @Override
    public List<String> getAliases()
    {
        ArrayList<String> names = new ArrayList<>();

        names.add(MODID);

        return names;
    }

    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return subUsage("");
    }

    public String subUsage(String subcommand)
    {
        if (!subcommands.contains(subcommand))
        {
            StringBuilder s = new StringBuilder(AQUA + "/" + getName() + " <" + subcommands.get(0));
            for (int i = 1; i < subcommands.size(); i++) s.append(" | ").append(subcommands.get(i));
            s.append(">");
            return s.toString();
        }

        return MODID + ".cmd." + subcommand + ".usage";
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args)
    {
        if (args.length == 0) sender.getCommandSenderEntity().sendMessage(new TextComponentString(subUsage("")));
        else subCommand(sender, args);
    }

    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        ArrayList<String> result = new ArrayList<>();

        String partial = args[args.length - 1];
        switch (args.length)
        {
            case 1:
                result.addAll(subcommands);
                break;

            case 2:
                switch (args[0])
                {
                    case "execute":
                        result.addAll(CAction.ALL_ACTIONS.keySet());
                        break;
                }
                break;

            case 3:
                switch (args[0])
                {
                    case "execute":
                        result.addAll(ActionQueue.existingQueues());
                        break;
                }
                break;

            case 4:
                switch (args[0])
                {
                    case "execute":
                        result.addAll(Arrays.asList(server.getOnlinePlayerNames()));
                        break;
                }
                break;
        }

        if (partial.length() != 0) result.removeIf(k -> partial.length() > k.length() || !k.substring(0, partial.length()).equalsIgnoreCase(partial));
        return result;
    }

    private void subCommand(ICommandSender sender, String[] args)
    {
        String cmd = args[0];
        switch (cmd)
        {
            case "reload":
                for (String s : CAction.reloadAll())
                {
                    notifyCommandListener(sender, this, s);
                }
                break;

            case "execute":
                CAction action = CAction.ALL_ACTIONS.get(args[1]);
                if (action == null)
                {
                    notifyCommandListener(sender, this, subUsage(cmd));
                    return;
                }

                String queue = args.length > 2 ? args[2] : "Main";
                if (!ActionQueue.queueExists(queue))
                {
                    notifyCommandListener(sender, this, subUsage(cmd));
                    return;
                }

                Entity entity = null;
                if (args.length > 3)
                {
                    entity = PlayerData.getEntity(args[3]);
                    if (entity == null) entity = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(UUID.fromString(args[3]));
                }
                else if (sender instanceof Entity) entity = (Entity) sender;
                if (entity == null)
                {
                    notifyCommandListener(sender, this, subUsage(cmd));
                    return;
                }

                if (args.length > 5)
                {
                    LinkedHashMap<String, Object> vars = new LinkedHashMap<>();
                    for (int i = 4; i < args.length - 1; i += 2)
                    {
                        vars.put(args[i], args[i + 1]);
                    }
                    action.queue(entity, queue, null, null, vars);
                }
                else action.queue(entity, queue);
                break;

            default:
                notifyCommandListener(sender, this, subUsage(cmd));
        }
    }
}
