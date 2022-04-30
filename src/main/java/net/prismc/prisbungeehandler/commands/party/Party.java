package net.prismc.prisbungeehandler.commands.party;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;
import net.prismc.prisbungeehandler.PrisBungeeHandler;
import net.prismc.prisbungeehandler.api.PrisApi;
import net.prismc.prisbungeehandler.api.UtilApi;
import net.prismc.prisbungeehandler.commands.party.subcommands.*;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;
import net.prismc.prisbungeehandler.utils.SubCommand;

import java.util.ArrayList;

public class Party extends Command {

    private final ArrayList<SubCommand> subcommands = new ArrayList<>();

    public Party(String name, String permission, String... alias) {
        super(name, permission, alias);
        subcommands.add(new PartyInvite());
        subcommands.add(new PartyJoin());
        subcommands.add(new PartyHelp());
        subcommands.add(new PartyList());
        subcommands.add(new PartyDisband());
        subcommands.add(new PartyLeave());
        subcommands.add(new PartyKick());
        subcommands.add(new PartyCreate());
        subcommands.add(new PartyPromote());
        subcommands.add(new PartyStatus());
        subcommands.add(new PartyWarp());
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {

        if (commandSender instanceof ProxiedPlayer) {
            PrisApi api = new PrisApi();
            PrisPlayer player = api.wrapPlayer((ProxiedPlayer) commandSender);
            Configuration lang = player.getLanguageFile();

            if (args.length > 0) {
                for (int i = 0; i < getSubcommands().size(); i++) {
                    for (String s : getSubcommands().get(i).getNames()) {
                        if (args[0].equalsIgnoreCase(s)) {
                            getSubcommands().get(i).perform(player, args);
                            return;
                        }
                    }
                }
                try {
                    PrisPlayer receiver = api.wrapPlayer(PrisBungeeHandler.getInstance().getProxy().getPlayer(args[0]));
                    PartyInvite invite = new PartyInvite();
                    invite.invite(player, receiver.getUsername());
                    return;
                } catch (Exception ignored) {
                }
                player.sendMessage(UtilApi.getString(lang, "Party.General.UnknownCommand"));
            } else {
                player.sendMessage(UtilApi.getString(lang, "Party.General.Spacer"));
                UtilApi.sendCenteredMessage(player, UtilApi.getString(lang, "Party.Help.Header"));
                player.sendMessage(UtilApi.getString(lang, "Party.Help.Create"));
                player.sendMessage(UtilApi.getString(lang, "Party.Help.Invite"));
                player.sendMessage(UtilApi.getString(lang, "Party.Help.Join"));
                player.sendMessage(UtilApi.getString(lang, "Party.Help.Kick"));
                player.sendMessage(UtilApi.getString(lang, "Party.Help.Leave"));
                player.sendMessage(UtilApi.getString(lang, "Party.Help.Disband"));
                player.sendMessage(UtilApi.getString(lang, "Party.Help.List"));
                player.sendMessage(UtilApi.getString(lang, "Party.Help.Promote"));

                player.sendMessage(UtilApi.getString(lang, "Party.Help.Chat"));
                player.sendMessage(UtilApi.getString(lang, "Party.Help.Mute"));
                player.sendMessage(UtilApi.getString(lang, "Party.Help.Status"));
                player.sendMessage(UtilApi.getString(lang, "Party.Help.Warp"));
                player.sendMessage(UtilApi.getString(lang, "Party.General.Spacer"));
            }
        }
    }

    public ArrayList<SubCommand> getSubcommands() {
        return subcommands;
    }

}