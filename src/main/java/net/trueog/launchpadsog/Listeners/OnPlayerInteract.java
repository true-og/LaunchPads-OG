package net.trueog.launchpadsog.Listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import net.trueog.launchpadsog.LaunchPadsOG;

public class OnPlayerInteract implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onInteract(PlayerInteractEvent event) {

        if (event.getAction() != Action.PHYSICAL) {

            return;

        }

        final Block block = event.getClickedBlock();
        if (block == null) {

            return;

        }

        if (block.getType() != Material.STONE_PRESSURE_PLATE) {

            return;

        }

        final Location loc = block.getLocation();
        final String worldName = loc.getWorld().getName();
        final int x = loc.getBlockX();
        final int y = loc.getBlockY();
        final int z = loc.getBlockZ();

        final FileConfiguration cfg = LaunchPadsOG.getConfiguration();
        final int count = cfg.getInt("numberOfPads");

        for (int i = 1; i <= count; i++) {

            final String padWorld = cfg.getString(i + ".world");
            if (!worldName.equals(padWorld)) {

                continue;

            }

            if (cfg.getInt(i + ".x") != x) {

                continue;

            }

            if (cfg.getInt(i + ".y") != y) {

                continue;

            }

            if (cfg.getInt(i + ".z") != z) {

                continue;

            }

            event.setUseInteractedBlock(Event.Result.DENY);
            event.setCancelled(true);

            return;

        }

    }

}