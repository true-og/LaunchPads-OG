package net.trueog.launchpadsog.Listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import net.trueog.launchpadsog.LaunchPadsOG;

public class OnEntityDamage implements Listener {

    // HIGHEST + ignoreCancelled: a cancelled event spends nothing, and little
    // runs after us that could un-cancel our own cancellation.
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {

        // check that this is a landing hurting a player
        if (!(event.getEntity() instanceof Player player) || !endsALaunch(event)) {

            return;

        }

        // Cancelled even at zero final damage, so the landing costs no hurt
        // animation or absorption; pre-liftoff damage is never consumed.
        if (LaunchPadsOG.consumeAirborneLaunch(player)) {

            event.setCancelled(true);

        }

    }

    // Landing on a stalagmite reports CONTACT rather than FALL, but it is
    // still the launch arc ending, so both count.
    private static boolean endsALaunch(EntityDamageEvent event) {

        switch (event.getCause()) {

            case FALL:
                return true;

            case CONTACT:
                if (event instanceof EntityDamageByBlockEvent blockDamage) {

                    final Block damager = blockDamage.getDamager();
                    return damager != null && damager.getType() == Material.POINTED_DRIPSTONE;

                }
                return false;

            default:
                return false;

        }

    }

}
