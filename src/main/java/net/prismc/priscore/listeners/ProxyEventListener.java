package net.prismc.priscore.listeners;

import com.google.gson.JsonObject;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.node.types.PermissionNode;
import net.luckperms.api.node.types.PrefixNode;
import net.luckperms.api.node.types.SuffixNode;
import net.prismc.priscore.PrisCore;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.io.DataOutputStream;
import java.io.IOException;

public class ProxyEventListener implements Listener {
    private final PrisCore plugin;
    private final LuckPerms luckPerms;

    public ProxyEventListener(PrisCore plugin, LuckPerms luckPerms) {
        this.plugin = plugin;
        this.luckPerms = luckPerms;
    }

    public void register() {
        EventBus eventBus = this.luckPerms.getEventBus();
        eventBus.subscribe(this.plugin, NodeAddEvent.class, this::onNodeAdd);
    }

    private void onNodeAdd(NodeAddEvent e) {
        if (!e.isUser()) {
            return;
        }
        User target = (User) e.getTarget();
        Node node = e.getNode();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Player player = Bukkit.getPlayer(target.getUniqueId());
            JsonObject object = new JsonObject();
            if (player == null) {
                return;
            }

            if (node instanceof PermissionNode) {
                String permission = ((PermissionNode) node).getPermission();
                object.addProperty("event", "PermissionAddEvent");
                object.addProperty("permission", permission);

            } else if (node instanceof InheritanceNode) {
                String groupName = ((InheritanceNode) node).getGroupName();
                object.addProperty("event", "GroupAddEvent");
                object.addProperty("group", groupName);

            } else if (node instanceof PrefixNode) {
                String prefix = ((PrefixNode) node).getMetaValue();
                object.addProperty("event", "PrefixUpdateEvent");
                object.addProperty("prefix", prefix);

            } else if (node instanceof SuffixNode) {
                String suffix = ((SuffixNode) node).getMetaValue();
                object.addProperty("event", "SuffixUpdateEvent");
                object.addProperty("suffix", suffix);
            }

            send(player, object);
        });
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent e) {
        JsonObject object = new JsonObject();
        object.addProperty("event", "blockBreakEvent");
        object.addProperty("block", String.valueOf(e.getBlock().getType()));
        object.addProperty("blockLocation", e.getBlock().getLocation().toString());
        object.addProperty("xpToDrop", e.getExpToDrop());
        send(e.getPlayer(), object);
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent e) {
        JsonObject object = new JsonObject();
        object.addProperty("event", "blockPlaceEvent");
        object.addProperty("block", String.valueOf(e.getBlockPlaced().getType()));
        object.addProperty("blockLocation", e.getBlockPlaced().getLocation().toString());
        send(e.getPlayer(), object);
    }

    @EventHandler
    private void onItemDrop(PlayerDropItemEvent e) {
        JsonObject object = new JsonObject();
        object.addProperty("event", "playerDropItemEvent");
        object.addProperty("itemDrop", e.getItemDrop().getItemStack().getType().toString());
        object.addProperty("itemLocation", e.getItemDrop().getLocation().toString());
        send(e.getPlayer(), object);
    }

    @EventHandler
    private void onItemPickup(EntityPickupItemEvent e) {
        JsonObject object = new JsonObject();
        object.addProperty("event", "EntityPickupItemEvent");
        object.addProperty("itemPickup", e.getItem().getItemStack().getType().toString());
        object.addProperty("itemLocation", e.getEntity().getLocation().toString());
        send((Player) e.getEntity(), object);
    }

    private void send(Player p, JsonObject toSend) {
        Bukkit.getScheduler().runTaskAsynchronously(PrisCore.getInstance(), () -> {
            toSend.addProperty("username", p.getName());
            toSend.addProperty("uuid", p.getUniqueId().toString());
            toSend.addProperty("subchannel", "bukkitEvents");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(stream);
            try {
                out.writeUTF(toSend.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            p.sendPluginMessage(PrisCore.getInstance(), PrisCore.BUNGEE_CHANNEL, stream.toByteArray());
        });
    }
}
