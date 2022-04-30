package net.prismc.prisbungeehandler.commands.party.subcommands;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.prismc.prisbungeehandler.PrisBungeeHandler;
import net.prismc.prisbungeehandler.api.PrisApi;
import net.prismc.prisbungeehandler.api.UtilApi;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;
import net.prismc.prisbungeehandler.utils.PatternCollection;
import net.prismc.prisbungeehandler.utils.SubCommand;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PartyInvite extends SubCommand {

    long time = 1; // Timeout for party invite in minutes

    @Override
    public List<String> getNames() {
        return Collections.singletonList("invite");
    }

    @Override
    public void perform(PrisPlayer p, String[] args) {
        if (args.length > 1) {
            invite(p, args[1]);
        } else {
            p.sendMessage(new TextComponent(UtilApi.getString(p.getLanguageFile(), "Party.General.MissingArgs")));
        }
    }

    public void invite(PrisPlayer p, String username) {
        PrisBungeeHandler instance = PrisBungeeHandler.getInstance();
        PrisApi playerApi = new PrisApi();

        try {
            PrisPlayer receiver = playerApi.wrapPlayer(instance.getProxy().getPlayer(username));

            if (p == receiver) {
                p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Invite.CantInviteSelf"));
                return;
            }

            if (p.toOfflinePlayer().getParty() != null) {
                if (p.toOfflinePlayer().getParty().containsPlayer(receiver.toOfflinePlayer())) {
                    p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Invite.AlreadyInParty"));
                    return;
                }

                if (p.toOfflinePlayer().getParty().getRole(p.toOfflinePlayer()).equals("member")) {
                    p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Invite.NotModOrLeader"));
                    return;
                }
            }

            if (receiver.toOfflinePlayer().getSetting(4).equals("0")) {
                p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Invite.CantPartyPlayer"));
                return;
            }

            if (receiver.toOfflinePlayer().getSetting(4).equals("1")) {
                if (!p.getFriendCache().contains(receiver.toOfflinePlayer())) {
                    p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Invite.CantPartyPlayer"));
                    return;
                }
            }

            if (receiver.containsIncomingPartyInvite(p.toOfflinePlayer())) {
                p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Invite.AlreadySent"));
                return;
            }

            try {
                if (p.toOfflinePlayer().getParty().getLeader().getMaxPartyPlayers() <= p.toOfflinePlayer().getParty().getUsers().size()) {
                    p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Invite.Full"));
                    return;
                }
            } catch (Exception ignored) {
            }

            String message = PatternCollection.PLAYER_PATTERN.matcher(UtilApi.getString(receiver.getLanguageFile(), "Party.Invite.ReceivedInvite")).replaceAll(p.toOfflinePlayer().getRankColor() + p.getUsername());
            receiver.sendMessage(UtilApi.getString(receiver.getLanguageFile(), "Party.General.Spacer"));
            UtilApi.sendCenteredMessage(receiver, message);
            TextComponent JOIN = new TextComponent(UtilApi.getString(receiver.getLanguageFile(), "Party.Invite.JOIN"));
            JOIN.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(UtilApi.getString(receiver.getLanguageFile(), "Party.Invite.JOINHover"))));
            JOIN.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party join " + p.getUsername()));
            ComponentBuilder builder = new ComponentBuilder().append(JOIN);
            UtilApi.sendCenteredMessage(receiver, builder);
            receiver.sendMessage(UtilApi.getString(receiver.getLanguageFile(), "Party.General.Spacer"));
            p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.Invite.SentAnInvite"));
            int id = timeOut(p, receiver);
            receiver.addIncomingPartyInvite(p.toOfflinePlayer().getID(), id);

        } catch (Exception e) {
            p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.General.NotOnline"));
        }
    }

    public int timeOut(PrisPlayer sender, PrisPlayer receiver) {
        return ProxyServer.getInstance().getScheduler().schedule(PrisBungeeHandler.getInstance(), () -> {
            sender.removeIncomingPartyInvite(receiver.toOfflinePlayer());
            receiver.removeIncomingPartyInvite(sender.toOfflinePlayer());

            try {
                String message = PatternCollection.PLAYER_PATTERN.matcher(UtilApi.getString(receiver.getLanguageFile(), "Party.Invite.TimedOutFrom")).replaceAll(sender.toOfflinePlayer().getRankColor() + sender.getUsername());
                receiver.sendMessage(message);
            } catch (Exception e) {
            }

            try {
                String message = PatternCollection.PLAYER_PATTERN.matcher(UtilApi.getString(sender.getLanguageFile(), "Party.Invite.TimedOutTo")).replaceAll(receiver.toOfflinePlayer().getRankColor() + receiver.getUsername());
                sender.sendMessage(message);
            } catch (Exception e) {
            }

        }, time, TimeUnit.MINUTES).getId();
    }
}
