package net.prismc.priscore.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.prismc.priscore.prisplayer.PrisPartyMember;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;

public class PartyParseApi {
    private final JsonObject object;

    public PartyParseApi(JsonObject object) {
        this.object = object;
    }

    public boolean inParty() {
        try {
            object.get("open");
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    public ArrayList<PrisPartyMember> getUsers() {
        JsonArray array = object.getAsJsonArray("members");
        ArrayList<PrisPartyMember> list = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JsonObject memberObject = array.get(i).getAsJsonObject();
            PrisPartyMember member = new PrisPartyMember(memberObject.get("name").getAsString(), UUID.fromString(memberObject.get("uuid").getAsString()),
                    memberObject.get("id").getAsInt(), memberObject.get("role").getAsString(), memberObject.get("online").getAsBoolean());
            list.add(member);
        }
        list.sort(Comparator.comparing(PrisPartyMember::getName));
        return list;
    }

    public PrisPartyMember getLeader() {
        for (PrisPartyMember prisPartyMember : getUsers()) {
            if (prisPartyMember.getRole().equals("leader")) {
                return prisPartyMember;
            }
        }
        return null;
    }

    public ArrayList<PrisPartyMember> getPeasants() {
        ArrayList<PrisPartyMember> list = new ArrayList<>();
        ArrayList<PrisPartyMember> online = new ArrayList<>();
        ArrayList<PrisPartyMember> offline = new ArrayList<>();
        for (PrisPartyMember prisPartyMember : getUsers()) {
            if (!prisPartyMember.getRole().equals("leader")) {
                if (prisPartyMember.isOnline()) {
                    online.add(prisPartyMember);
                } else {
                    offline.add(prisPartyMember);
                }
            }
        }
        online.sort(Comparator.comparing(PrisPartyMember::getName));
        offline.sort(Comparator.comparing(PrisPartyMember::getName));
        list.addAll(online);
        list.addAll(offline);
        return list;
    }

    public boolean isOpen() {
        return object.get("open").getAsBoolean();
    }
}
