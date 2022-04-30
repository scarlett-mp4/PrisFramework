package net.prismc.prisbungeehandler.communication.spigot;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.prismc.prisbungeehandler.PrisBungeeHandler;
import net.prismc.prisbungeehandler.achievements.utils.AchievementHandler;
import net.prismc.prisbungeehandler.api.PrisApi;
import net.prismc.prisbungeehandler.communication.spigot.depends.SpigotTask;
import net.prismc.prisbungeehandler.communication.spigot.tasks.*;
import net.prismc.prisbungeehandler.events.bukkit.*;
import net.prismc.prisbungeehandler.parties.PrisParty;
import net.prismc.prisbungeehandler.prisplayer.OfflinePrisPlayer;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

public class PrisPluginMessage implements Listener {

    private final ArrayList<SpigotTask> tasks = new ArrayList<>();

    public PrisPluginMessage() {
        tasks.add(new AddFriend());
        tasks.add(new RemoveFriend());
        tasks.add(new JoinParty());
        tasks.add(new LeaveParty());
        tasks.add(new PlayerUpdate());
        tasks.add(new GetFriendList());
        tasks.add(new CreateParty());
        tasks.add(new PartyStatus());
        tasks.add(new InviteParty());
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent e) {
        try {
            if (!e.getTag().equals(PrisBungeeHandler.BUNGEE_CHANNEL)) {
                return;
            }
            if (!(e.getSender() instanceof Server)) {
                return;
            }
            final Server server = (Server) e.getSender();
            try {
                ByteArrayInputStream stream = new ByteArrayInputStream(e.getData());
                DataInputStream in = new DataInputStream(stream);
                final JsonObject jsonObject = PrisBungeeHandler.getGson().fromJson(in.readUTF(), JsonObject.class);
                if (jsonObject.has("subchannel")) {
                    if (jsonObject.get("subchannel").getAsString().equals("bukkitEvents")) {
                        PrisBungeeHandler.getInstance().getProxy().getScheduler().runAsync(PrisBungeeHandler.getInstance(), () -> sendEvent(jsonObject));
                        return;
                    } else if (jsonObject.get("subchannel").getAsString().equals("achievementCheck")) {
                        PrisApi.achievementCheckAndExecute(new PrisApi().wrapPlayer(ProxyServer.getInstance().getPlayer(UUID.fromString(jsonObject.get("uuid").getAsString()))));
                    } else if (jsonObject.get("subchannel").getAsString().equals("achievementList")) {
                        achievementList(server);
                    } else {
                        PrisBungeeHandler.getInstance().getProxy().getScheduler().runAsync(PrisBungeeHandler.getInstance(), () -> toDo(jsonObject, server));
                    }
                }
            } catch (IOException ignored) {
            }
        } catch (NullPointerException ignored) {
        }
    }

    private void achievementList(Server server) {
        JsonObject object = new JsonObject();
        JsonArray array = new JsonArray();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);

        for (AchievementHandler handler : PrisBungeeHandler.getInstance().achievementHandlers) {
            JsonObject handlerObject = new JsonObject();
            handlerObject.addProperty("name", handler.getName());
            handlerObject.addProperty("type", handler.getType().getCategory());
            handlerObject.addProperty("rarity", handler.getRarity().getRarity());
            handlerObject.addProperty("xp", handler.getXpAward());
            array.add(handlerObject);
        }

        object.add("achievements", array);
        object.addProperty("subchannel", "achievementList");

        try {
            out.writeUTF(object.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.getInfo().sendData(PrisBungeeHandler.BUNGEE_CHANNEL, stream.toByteArray());
    }

    private void toDo(JsonObject object, Server server) {
        try {
            PrisApi api = new PrisApi();
            String subChannel = object.get("subchannel").getAsString().replaceAll("\"", "");

            for (SpigotTask task : tasks)
                if (task.isApplicable(subChannel)) {
                    PrisPlayer p = api.wrapPlayer(ProxyServer.getInstance().getPlayer(UUID.fromString(object.get("uuid").getAsString())));
                    task.executeTask(p, server, object);
                }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendEvent(JsonObject object) {
        PrisApi api = new PrisApi();
        PrisPlayer p = api.wrapPlayer(ProxyServer.getInstance().getPlayer(UUID.fromString(object.get("uuid").getAsString())));

        switch (object.get("event").getAsString()) {
            case "PermissionAddEvent":
                ProxyServer.getInstance().getPluginManager().callEvent(new PermissionAddEvent(p, object));
                break;
            case "GroupAddEvent":
                ProxyServer.getInstance().getPluginManager().callEvent(new GroupAddEvent(p, object));
                break;
            case "PrefixUpdateEvent":
                ProxyServer.getInstance().getPluginManager().callEvent(new PrefixUpdateEvent(p, object));
                break;
            case "SuffixUpdateEvent":
                ProxyServer.getInstance().getPluginManager().callEvent(new SuffixUpdateEvent(p, object));
                break;
            case "blockBreakEvent":
                ProxyServer.getInstance().getPluginManager().callEvent(new BlockBreakEvent(p, object));
                break;
            case "blockPlaceEvent":
                ProxyServer.getInstance().getPluginManager().callEvent(new BlockPlaceEvent(p, object));
                break;
            case "playerDropItemEvent":
                ProxyServer.getInstance().getPluginManager().callEvent(new PlayerDropItemEvent(p, object));
                break;
            case "EntityPickupItemEvent":
                ProxyServer.getInstance().getPluginManager().callEvent(new EntityPickupItemEvent(p, object));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + object.get("event").getAsString());
        }
    }


    // -----| Public Methods |----------------------------------------------------------------------------------------------------------------

    public void createPrisBukkitPlayer(PrisPlayer p) {
        JsonObject object = new JsonObject();
        JsonObject partyObject = new JsonObject();
        if (p.getServer().getInfo() == null)
            return;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);

        object.addProperty("subchannel", "playerupdate");
        object.addProperty("id", p.toOfflinePlayer().getID());
        object.addProperty("uuid", p.getUniqueId().toString());
        object.addProperty("username", p.getUsername());
        object.addProperty("lang", p.toOfflinePlayer().getLanguage());
        object.addProperty("lastseen", p.toOfflinePlayer().getLastOnline());
        object.addProperty("firstjoined", p.toOfflinePlayer().getFirstJoin());
        object.addProperty("ip", p.getSocketAddress().toString());
        object.addProperty("playtime", p.toOfflinePlayer().getPlayTime());
        object.addProperty("level", p.toOfflinePlayer().getLevel());
        object.addProperty("progress", p.toOfflinePlayer().getProgress());
        object.addProperty("settings", p.toOfflinePlayer().getAllSettings());
        object.addProperty("priscoins", p.toOfflinePlayer().getPriscoins());
        object.addProperty("rubies", p.toOfflinePlayer().getRubies());
        object.addProperty("karma", p.toOfflinePlayer().getKarma());

        if (!p.toOfflinePlayer().inParty()) {
            partyObject.addProperty("leader", "null");
        } else {
            PrisParty party = p.toOfflinePlayer().getParty();
            JsonArray users = new JsonArray();
            for (OfflinePrisPlayer offlinePrisPlayer : party.getUsers().keySet()) {
                JsonObject jsonMember = new JsonObject();
                jsonMember.addProperty("name", offlinePrisPlayer.getUsername());
                jsonMember.addProperty("uuid", String.valueOf(offlinePrisPlayer.getUniqueId()));
                jsonMember.addProperty("id", offlinePrisPlayer.getID());
                jsonMember.addProperty("role", party.getRole(offlinePrisPlayer));
                jsonMember.addProperty("online", offlinePrisPlayer.isOnline());
                users.add(jsonMember);
            }
            partyObject.add("members", users);
            partyObject.addProperty("open", party.isOpen());
        }
        object.add("party", partyObject);

        try {
            out.writeUTF(object.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        p.getServer().getInfo().sendData(PrisBungeeHandler.BUNGEE_CHANNEL, stream.toByteArray());
    }
}
