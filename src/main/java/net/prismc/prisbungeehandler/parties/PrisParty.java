package net.prismc.prisbungeehandler.parties;

import net.md_5.bungee.api.ProxyServer;
import net.prismc.prisbungeehandler.PrisBungeeHandler;
import net.prismc.prisbungeehandler.api.PrisApi;
import net.prismc.prisbungeehandler.api.UtilApi;
import net.prismc.prisbungeehandler.prisplayer.OfflinePrisPlayer;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class PrisParty {

    private final HashMap<OfflinePrisPlayer, String> members = new HashMap<>();
    boolean open;
    private OfflinePrisPlayer leader;
    private int leaderQuitID;

    public PrisParty(OfflinePrisPlayer leader, boolean open) {
        this.leader = leader;
        this.open = open;
        PrisBungeeHandler.getInstance().cache.parties.add(this);
    }

    public int getLeaderQuitID() {
        return leaderQuitID;
    }

    public void setLeaderQuitID(int newID) {
        leaderQuitID = newID;
    }

    public OfflinePrisPlayer getLeader() {
        return leader;
    }

    public void setLeader(OfflinePrisPlayer newLeader) {
        leader = newLeader;
        members.remove(newLeader);
        members.put(newLeader, "leader");
        PartyCommunication.update(this);
    }

    public void setMod(OfflinePrisPlayer newMod) {
        members.remove(newMod);
        members.put(newMod, "mod");
        PartyCommunication.update(this);
    }

    public void setMember(OfflinePrisPlayer newMember) {
        members.remove(newMember);
        members.put(newMember, "member");
        PartyCommunication.update(this);
    }

    public void removeUser(OfflinePrisPlayer member) {
        members.remove(member);
        PartyCommunication.update(this);
    }

    public HashMap<OfflinePrisPlayer, String> getUsers() {
        return members;
    }

    public ArrayList<OfflinePrisPlayer> getMembers() {
        ArrayList<OfflinePrisPlayer> list = new ArrayList<>();
        for (OfflinePrisPlayer player : members.keySet()) {
            if (members.get(player).equals("member")) {
                list.add(player);
            }
        }
        list.sort(Comparator.comparing(OfflinePrisPlayer::getUsername));
        return list;
    }

    public ArrayList<OfflinePrisPlayer> getMods() {
        ArrayList<OfflinePrisPlayer> list = new ArrayList<>();
        for (OfflinePrisPlayer player : members.keySet()) {
            if (members.get(player).equals("mod")) {
                list.add(player);
            }
        }
        list.sort(Comparator.comparing(OfflinePrisPlayer::getUsername));
        return list;
    }

    public boolean containsPlayer(OfflinePrisPlayer player) {
        return members.containsKey(player);
    }

    public String getRole(OfflinePrisPlayer player) {
        return members.get(player);
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean value) {
        open = value;
        PartyCommunication.update(this);
    }

    public void disband() {
        PrisApi api = new PrisApi();
        for (OfflinePrisPlayer p : members.keySet()) {
            p.leaveParty();
            try {
                PrisPlayer player = api.wrapPlayer(ProxyServer.getInstance().getPlayer(p.getUniqueId()));
                player.sendMessage(UtilApi.getString(player.getLanguageFile(), "Party.Disband.HasBeenDisbanded"));
            } catch (Exception ignored) {
            }
        }
        this.members.clear();
        PrisBungeeHandler.getInstance().cache.parties.remove(this);
    }
}
