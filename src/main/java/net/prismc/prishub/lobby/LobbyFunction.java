package net.prismc.prishub.lobby;

import net.prismc.priscore.api.PrisCoreApi;
import net.prismc.priscore.api.UtilApi;
import net.prismc.priscore.prisplayer.PrisBukkitPlayer;
import net.prismc.prishub.PrisHub;
import net.prismc.prishub.menus.profilemenu.pageprofile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.potion.PotionEffect;

public class LobbyFunction implements Listener {

    public void lobbyEnter(PrisBukkitPlayer p) {
        p.getInventory().clear();
        p.setLevel(0);
        p.setExp(0);
        p.setGameMode(GameMode.ADVENTURE);
        p.getPlayer().getInventory().setHeldItemSlot(0);
        p.createLevelBar();
        for (PotionEffect potion : p.getPlayer().getActivePotionEffects())
            p.getPlayer().removePotionEffect(potion.getType());

        p.getInventory().setItem(0, UtilApi.createItem(Material.YELLOW_DYE, UtilApi.getString(p.getLanguageFile(), "Main.Items.ServerSelector")));
        p.getInventory().setItem(1, UtilApi.createItem(Material.PAPER, UtilApi.getString(p.getLanguageFile(), "Main.Items.Profile")));

        Bukkit.getScheduler().runTaskLater(PrisHub.getInstance(), () -> LobbyAnimation.playAnimation(p.getPlayer()), PrisHub.getInstance().getConfig().getInt("Animation.LoginDelay"));
    }

    @EventHandler
    private void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (p.getOpenInventory().getType().equals(InventoryType.CHEST))
            return;

        if (e.getAction() == Action.PHYSICAL)
            return;

        // Server Selector
        if (e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.YELLOW_DYE)) {
            // Placeholder
        }

        // Profile Menu
        if (e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.PAPER)) {
            Bukkit.getScheduler().runTaskAsynchronously(PrisHub.getInstance(), () -> {
                Profile menu = new Profile();
                menu.open(PrisCoreApi.wrapPrisBukkitPlayer(p), true);
            });
        }

    }

    @EventHandler
    private void onDrop(PlayerDropItemEvent e) {
        if (!e.getPlayer().hasPermission("prismc.admin")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onOffHandSwap(PlayerSwapHandItemsEvent e) {
        if (!e.getPlayer().hasPermission("prismc.admin")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getWhoClicked().hasPermission("prismc.admin")) {
            return;
        }
        if (e.getWhoClicked().getOpenInventory().getType().equals(InventoryType.CRAFTING)) {
            e.setCancelled(true);
        }
    }
}
