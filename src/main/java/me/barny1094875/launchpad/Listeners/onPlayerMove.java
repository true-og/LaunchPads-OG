package me.barny1094875.launchpad.Listeners;

import me.barny1094875.launchpad.LaunchPad;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;


public class onPlayerMove implements Listener {

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event){
        if(event.hasChangedBlock()){
            // loop through every launch pad and look for one that has the same coords as the player
            FileConfiguration padConfig = LaunchPad.config().getConfig();
            Player player = event.getPlayer();
            String worldName = player.getWorld().getName();
            for(int i = 1; i < padConfig.getInt("numberOfPads") + 1; i++){
                // if the launch pad is in the same world as the player
                if(padConfig.getString("" + i + ".world").equals(worldName)){
                    Location playerLocation = event.getTo();
                    // check each cardinal direction individually so that it can run faster
                    // check x location
                    if(padConfig.getInt("" + i + ".x") == playerLocation.getBlockX()){
                        // check y location
                        if(padConfig.getInt("" + i + ".y") == playerLocation.getBlockY()){
                            // check z location
                            if(padConfig.getInt("" + i + ".z") == playerLocation.getBlockZ()){
                                // calculate the player's y-speed using getFrom() and getTo()
                                // y speed is the only one calculated so that a jump will affect
                                // how far you get launched
                                Location from = event.getFrom();
                                Location to = event.getTo();

                                // speed = distance/time
                                // distance = abs(from-to)
                                // time = 1 tick
                                double ySpeed = to.getY() - from.getY();

                                // if the ySpeed is below a threshold of 0.2,
                                // then set the ySpeed to 0.2 so that there is some y movement
                                if(ySpeed < 0.2){
                                    ySpeed = 0.2;
                                }

                                // add 0.2 to the ySpeed so that there is more movement,
                                // without reducing the effect of a jump
                                ySpeed += 0.2;

                                // if the player is crouching, reduce the velocity to 1/3
                                if(player.isSneaking()){
                                    ySpeed /= 3;
                                }

                                // reduce the x,z velocity depending on how straight
                                // the player is facing
                                // more straight, more distance
                                double velocityMult;
                                double pitch = Location.normalizePitch(playerLocation.getPitch());
                                velocityMult = Math.abs(pitch - 90) / 90;

                                // get the unit vector for the x,z directions
                                Vector directionVector = playerLocation.getDirection();

                                FileConfiguration launchConfig = LaunchPad.config().getConfig();

                                player.setVelocity(new Vector(
                                /* x */  launchConfig.getInt(i + ".xpower") * directionVector.getX() * velocityMult,
                                /* y */  ySpeed * launchConfig.getInt(i + ".ypower"),
                                /* z */  launchConfig.getInt(i + ".zpower") * directionVector.getZ() * velocityMult));

                                // player a wither sound pitched up by 2.0
                                player.playSound(playerLocation, Sound.ENTITY_WITHER_SHOOT, 1.0f, 1.0f);
                                // spawn a particle at the launch pad
                                Location padLocation = new Location(player.getWorld(),
                                        launchConfig.getInt(i + ".x"),
                                        launchConfig.getInt(i + ".y"),
                                        launchConfig.getInt(i + ".z"));
                                player.spawnParticle(Particle.SPELL, padLocation, 100);
                                for(int j = 0; j < 20; j++){
                                    Bukkit.getScheduler().runTaskLater(LaunchPad.getPlugin(), () -> {
                                        player.spawnParticle(Particle.SPELL, player.getLocation(), 10);
                                    }, j);
                                }

                            }
                        }
                    }
                }
            }
        }
    }

}
