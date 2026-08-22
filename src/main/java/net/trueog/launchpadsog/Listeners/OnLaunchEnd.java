package net.trueog.launchpadsog.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.spigotmc.event.entity.EntityMountEvent;

import net.trueog.launchpadsog.LaunchPadsOG;

// Every way of leaving a launch arc drops its protection, so normal
// fall damage applies again afterwards.
public class OnLaunchEnd implements Listener {

    // opening an elytra turns the arc into controlled flight
    @EventHandler(ignoreCancelled = true)
    public void onGlide(EntityToggleGlideEvent event) {

        if (event.isGliding() && event.getEntity() instanceof Player player) {

            LaunchPadsOG.clearLaunch(player);

        }

    }

    // a mount catches the player; later falls belong to the ride
    @EventHandler(ignoreCancelled = true)
    public void onMount(EntityMountEvent event) {

        if (event.getEntity() instanceof Player player) {

            LaunchPadsOG.clearLaunch(player);

        }

    }

    // a teleport abandons the arc entirely
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent event) {

        LaunchPadsOG.clearLaunch(event.getPlayer());

    }

    // portal travel is dispatched on its own handler list, so the teleport
    // handler above never sees it
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPortal(PlayerPortalEvent event) {

        LaunchPadsOG.clearLaunch(event.getPlayer());

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(PlayerDeathEvent event) {

        LaunchPadsOG.clearLaunch(event.getEntity());

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {

        LaunchPadsOG.clearLaunch(event.getPlayer());

    }

}
