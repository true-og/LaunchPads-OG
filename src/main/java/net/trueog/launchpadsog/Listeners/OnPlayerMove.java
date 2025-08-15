package net.trueog.launchpadsog.Listeners;

import net.trueog.launchpadsog.LaunchPadsOG;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class OnPlayerMove implements Listener {

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {

        if (event.hasChangedBlock()) {

            // loop through every launch pad and look for one that has the same coords as
            // the player
            FileConfiguration padConfig = LaunchPadsOG.config().getConfig();
            Player player = event.getPlayer();
            String worldName = player.getWorld().getName();
            for (int i = 1; i < padConfig.getInt("numberOfPads") + 1; i++) {

                // if the launch pad is in the same world as the player
                if (padConfig.getString("" + i + ".world").equals(worldName)) {

                    Location playerLocation = event.getTo();
                    // check each cardinal direction individually so that it can run faster
                    // check x location
                    if (padConfig.getInt("" + i + ".x") == playerLocation.getBlockX()) {

                        // check y location
                        if (padConfig.getInt("" + i + ".y") == playerLocation.getBlockY()) {

                            // check z location
                            if (padConfig.getInt("" + i + ".z") == playerLocation.getBlockZ()) {

                                // calculate the player's y-speed using getFrom() and getTo()
                                // y speed is the only one calculated so that a jump will affect
                                // how far you get launched
                                Location from = event.getFrom();
                                Location to = event.getTo();

                                // speed = distance/time
                                // distance = abs(from-to)
                                // time = 1 tick
                                double ySpeed = to.getY() - from.getY();

                                // if the ySpeed is below a threshold of 0.15,
                                // then set the ySpeed to 0.15 so that there is some y movement
                                if (ySpeed < 0.15) {

                                    ySpeed = 0.15;

                                }

                                // add 0.25 to the ySpeed so that there is more movement,
                                // without reducing the effect of a jump
                                ySpeed += 0.25;

                                // if the player is crouching, reduce the velocity to 1/3
                                if (player.isSneaking()) {

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

                                FileConfiguration launchConfig = LaunchPadsOG.config().getConfig();
                                // get the xpower, ypower, and zpower
                                double xPower = launchConfig.getDouble(i + ".xpower");
                                double yPower = launchConfig.getDouble(i + ".ypower");
                                double zPower = launchConfig.getDouble(i + ".zpower");

                                player.setVelocity(new Vector(/* x */ xPower * directionVector.getX() * velocityMult,
                                        /* y */ yPower * ySpeed,
                                        /* z */ zPower * directionVector.getZ() * velocityMult));

                                // player a wither shoot sound
                                player.playSound(playerLocation, Sound.ENTITY_WITHER_SHOOT, 1.0f, 1.0f);
                                // spawn particles at the launch pad
                                Location padLocation = new Location(player.getWorld(),
                                        launchConfig.getInt(i + ".x") + 0.5, launchConfig.getInt(i + ".y"),
                                        launchConfig.getInt(i + ".z") + 0.5);
                                double padParticleCount = launchConfig.getDouble("padLaunchParticleCount");
                                double padParticlePower = launchConfig.getDouble("padLaunchParticlePower");
                                player.spawnParticle(Particle.FIREWORKS_SPARK, padLocation, (int) padParticleCount, 0,
                                        0, 0, 0.2 * padParticlePower);

                                double playerParticleMult = launchConfig
                                        .getDouble("playerLaunchParticleCountMultipier");
                                // spawn particles on the player as they fly through the air
                                // This equation was gotten with a linear regression on different amounts of
                                // yPower
                                // to find what the vertex of the player's jump is
                                // maxes out at 25 ticks
                                // \/ --------- \/
                                for (int j = 0; j < Math.min(yPower * 8 - 15, 25); j++) {

                                    Bukkit.getScheduler().runTaskLater(LaunchPadsOG.getPlugin(), () -> {

                                        // max out the number of particles at 50
                                        player.getWorld().spawnParticle(Particle.SPELL, player.getLocation(),
                                                (int) Math.min(xPower * zPower * playerParticleMult, 50));

                                    }, j);

                                }

                                // add the player to the launchedPlayers list
                                // 1 tick later so that the player is off the ground
                                // and isn't removed immediately
                                Bukkit.getScheduler().runTaskLater(LaunchPadsOG.getPlugin(), () -> {

                                    // check if the player is in survival. Otherwise, don't add them
                                    if (player.getGameMode().equals(GameMode.SURVIVAL)) {

                                        LaunchPadsOG.getLaunchedPlayers().add(player);

                                    }

                                }, 1);

                            }

                        }

                    }

                }

            }

        }

    }

}
