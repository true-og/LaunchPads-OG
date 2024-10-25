package net.trueog.launchpadsog.Listeners;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import net.trueog.launchpadsog.LaunchPadsOG;

public class OnEntityDamage implements Listener {

	//    @EventHandler(priority = EventPriority.HIGHEST)
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event){

		// check if the damage was fall damage
		if(event.getCause().equals(EntityDamageEvent.DamageCause.FALL)){
			// check if the entity was a player
			if(event.getEntity() instanceof Player) {
				// check the entity was in the launchedPlayers list
				ArrayList<Player> launchedList = LaunchPadsOG.getLaunchedPlayers();
				Player eventPlayer = (Player) event.getEntity();
				if (launchedList.contains(eventPlayer)) {
					event.setCancelled(true);

					launchedList.remove(eventPlayer);
				}
			}
		}

	}


}