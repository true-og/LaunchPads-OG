package net.trueog.launchpadsog.Listeners;

import net.trueog.launchpadsog.LaunchPadsOG;
import org.bukkit.GameEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.GenericGameEvent;

public class OnGenericGameEvent implements Listener {

    // use this event to see if the player hit the ground
    // if they did, then we can check if they were launched
    // this way, even if they don't take fall damage from the fall
    // say, they were using an elytra,
    // this will ensure that the OnEntityDamage event is still called
    // to remove them from the launched player List
    @EventHandler
    public void onGenericGameEvent(GenericGameEvent event) {

        // check if the event was an entity hitting the ground
        if (event.getEvent().equals(GameEvent.HIT_GROUND)) {

            // if the entity was a player
            if (event.getEntity() instanceof Player) {

                Player player = (Player) event.getEntity();

                // if the player was in the launched list
                if (LaunchPadsOG.getLaunchedPlayers().contains(player)) {

                    // set the player's fall distance to 4
                    player.setFallDistance(4.0f);

                }

            }

        }

    }

}
