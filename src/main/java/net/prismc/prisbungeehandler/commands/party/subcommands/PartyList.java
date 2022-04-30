package net.prismc.prisbungeehandler.commands.party.subcommands;

import net.md_5.bungee.api.ChatColor;
import net.prismc.prisbungeehandler.api.UtilApi;
import net.prismc.prisbungeehandler.prisplayer.OfflinePrisPlayer;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;
import net.prismc.prisbungeehandler.utils.PatternCollection;
import net.prismc.prisbungeehandler.utils.SubCommand;

import java.util.Arrays;
import java.util.List;

public class PartyList extends SubCommand {

    @Override
    public List<String> getNames() {
        return Arrays.asList("list", "l");
    }

    @Override
    public void perform(PrisPlayer p, String[] args) {

        if (p.toOfflinePlayer().inParty()) {
            String header = PatternCollection.PLAYER_PATTERN.matcher(UtilApi.getString(p.getLanguageFile(), "Party.List.PartyHeader")).replaceAll(p.toOfflinePlayer().getParty().getLeader().getUsername());
            String statusOPEN = UtilApi.getString(p.getLanguageFile(), "Party.List.Status.OPEN");
            String statusCLOSED = UtilApi.getString(p.getLanguageFile(), "Party.List.Status.CLOSED");
            String displayMods = "";
            String displayMembers = "";
            String statusHeader = "";

            if (p.toOfflinePlayer().getParty().isOpen()) {
                statusHeader = PatternCollection.STATUS_PATTERN.matcher(UtilApi.getString(p.getLanguageFile(), "Party.List.Status.STATUS")).replaceAll(statusOPEN);
            } else {
                statusHeader = PatternCollection.STATUS_PATTERN.matcher(UtilApi.getString(p.getLanguageFile(), "Party.List.Status.STATUS")).replaceAll(statusCLOSED);
            }

            String leaderStatus;
            if (p.toOfflinePlayer().getParty().getLeader().isOnline()) {
                leaderStatus = ChatColor.GREEN + "♦ ";
            } else {
                leaderStatus = ChatColor.RED + "♦ ";
            }

            StringBuilder mods = new StringBuilder();
            for (OfflinePrisPlayer offlinePrisPlayer : p.toOfflinePlayer().getParty().getMods()) {
                String status;
                if (offlinePrisPlayer.isOnline()) {
                    status = ChatColor.GREEN + "♦ ";
                } else {
                    status = ChatColor.RED + "♦ ";
                }
                mods.append(status).append(offlinePrisPlayer.getRankColor()).append(offlinePrisPlayer.getUsername()).append(ChatColor.YELLOW).append(", ");
            }
            if (p.toOfflinePlayer().getParty().getMods().size() != 0) {
                displayMods = PatternCollection.MEMBERS_PATTERN.matcher(UtilApi.getString(p.getLanguageFile(), "Party.List.Mods")).replaceAll(mods.substring(0, mods.length() - 2));
            }

            StringBuilder members = new StringBuilder();
            for (OfflinePrisPlayer offlinePrisPlayer : p.toOfflinePlayer().getParty().getMembers()) {
                String status;
                if (offlinePrisPlayer.isOnline()) {
                    status = ChatColor.GREEN + "♦ ";
                } else {
                    status = ChatColor.RED + "♦ ";
                }
                members.append(status).append(offlinePrisPlayer.getRankColor()).append(offlinePrisPlayer.getUsername()).append(ChatColor.YELLOW).append(", ");
            }
            if (p.toOfflinePlayer().getParty().getMembers().size() != 0) {
                displayMembers = PatternCollection.MEMBERS_PATTERN.matcher(UtilApi.getString(p.getLanguageFile(), "Party.List.Members")).replaceAll(members.substring(0, members.length() - 2));
            }

            p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.General.Spacer"));
            UtilApi.sendCenteredMessage(p, header);
            UtilApi.sendCenteredMessage(p, statusHeader);
            p.sendMessage(" ");
            p.sendMessage(PatternCollection.PLAYER_PATTERN.matcher(UtilApi.getString(p.getLanguageFile(), "Party.List.Leader")).replaceAll(leaderStatus
                    + p.toOfflinePlayer().getParty().getLeader().getRankColor() + p.toOfflinePlayer().getParty().getLeader().getUsername()));
            if (p.toOfflinePlayer().getParty().getMods().size() != 0) {
                p.sendMessage(displayMods);
            }
            if (p.toOfflinePlayer().getParty().getMembers().size() != 0) {
                p.sendMessage(displayMembers);
            }
            p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.General.Spacer"));
        } else {
            p.sendMessage(UtilApi.getString(p.getLanguageFile(), "Party.List.NotInParty"));
        }
    }
}
