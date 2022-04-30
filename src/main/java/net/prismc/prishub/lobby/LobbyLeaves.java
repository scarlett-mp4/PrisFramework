package net.prismc.prishub.lobby;

import net.prismc.prishub.PrisHub;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;
import xyz.xenondevs.particle.data.texture.BlockTexture;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class LobbyLeaves {
    private final List<Material> list = new ArrayList<>();

    public LobbyLeaves() {
        list.add(Material.OAK_LEAVES);
        list.add(Material.SPRUCE_LEAVES);
        list.add(Material.BIRCH_LEAVES);
        list.add(Material.JUNGLE_LEAVES);
        list.add(Material.ACACIA_LEAVES);
        list.add(Material.DARK_OAK_LEAVES);
    }

    public void initLeaves() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(PrisHub.getInstance(), () -> {
            try {
                Random random = new Random();
                for (Player p : Bukkit.getOnlinePlayers()) {
                    Location location = p.getLocation();
                    int radius = 15;

                    for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
                        for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                            for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                                Block block = location.getWorld().getBlockAt(x, y, z);
                                for (Material material : list) {
                                    if (block.getType() == material) {
                                        if (block.getLocation().clone().add(0, -1, 0).getBlock().getType() == Material.AIR) {
                                            int rand = random.nextInt(30);
                                            if (rand == 0) {
                                                Location loc = new Location(p.getWorld(), ThreadLocalRandom.current().nextDouble(block.getLocation().getX(), block.getLocation().getX() + 1.0),
                                                        block.getLocation().getY(), ThreadLocalRandom.current().nextDouble(block.getLocation().getZ(), block.getLocation().getZ() + 1.0));

                                                new ParticleBuilder(ParticleEffect.FALLING_DUST, loc)
                                                        .setParticleData(new BlockTexture(material))
                                                        .setAmount(1)
                                                        .setOffsetX(0F)
                                                        .setOffsetY(0F)
                                                        .setOffsetZ(0F)
                                                        .setSpeed(0F)
                                                        .display(p.getPlayer());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 100, 15);
    }
}
