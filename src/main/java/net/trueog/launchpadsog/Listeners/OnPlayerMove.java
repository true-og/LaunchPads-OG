package net.trueog.launchpadsog.Listeners;

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

import net.trueog.launchpadsog.LaunchPadsOG;

public class OnPlayerMove implements Listener {

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {

        if (!event.hasChangedBlock()) {

            return;

        }

        // Loop through every launch pad and look for one that has the same coordinates
        // as
        // the player.
        final FileConfiguration padConfig = LaunchPadsOG.getConfiguration();
        final Player player = event.getPlayer();
        final String worldName = player.getWorld().getName();

        final Location playerLocation = event.getTo();
        if (playerLocation == null) {

            return;

        }

        for (int i = 1; i < padConfig.getInt("numberOfPads") + 1; i++) {

            final String padWorld = padConfig.getString(i + ".world");
            if (!worldName.equals(padWorld)) {

                continue; // wrong world -> skip this pad

            }

            final boolean condition = padConfig.getInt(Integer.toString(i) + ".x") == playerLocation.getBlockX()
                    && padConfig.getInt(Integer.toString(i) + ".y") == playerLocation.getBlockY()
                    && padConfig.getInt(Integer.toString(i) + ".z") == playerLocation.getBlockZ();
            // check y location
            // check z location
            // check each cardinal direction individually so that it can run faster
            // check x location
            if (condition) {

                // calculate the player's y-speed using getFrom() and getTo()
                // y speed is the only one calculated so that a jump will affect
                // how far you get launched
                final Location from = event.getFrom();
                final Location to = playerLocation;

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

                // Multiply the velocity by how far the player is looking up or down.
                final double velocityMult;
                final double lookPitch = Location.normalizePitch(playerLocation.getPitch());
                velocityMult = Math.abs(lookPitch - 90) / 90;

                // Get the unit vector for the x,z directions.
                final Vector directionVector = playerLocation.getDirection();

                // Get the xpower, ypower, and zpower.
                final double xPower = padConfig.getDouble(i + ".xpower");
                final double yPower = padConfig.getDouble(i + ".ypower");
                final double zPower = padConfig.getDouble(i + ".zpower");

                final double vx = xPower * directionVector.getX() * velocityMult;
                final double vy = yPower * ySpeed;
                final double vz = zPower * directionVector.getZ() * velocityMult;

                player.setVelocity(new Vector(/* x */ vx, /* y */ vy, /* z */ vz));

                // Spawn particles at the launch pad.
                final Location padLocation = new Location(player.getWorld(), padConfig.getInt(i + ".x") + 0.5,
                        padConfig.getInt(i + ".y"), padConfig.getInt(i + ".z") + 0.5);

                // Hive java style launchpad woosh (audible to nearby players, fades with
                // distance).
                final float basePitch = (float) Math.max(1.62, Math.min(1.99, 1.80 + (velocityMult * 0.18)));
                final float treblePitch = Math.min(2.0f, basePitch + 0.22f);

                for (int t = 0; t < 6; t++) {

                    final int delay = t;

                    Bukkit.getScheduler().runTaskLater(LaunchPadsOG.getPlugin(), () -> {

                        final float falloff = Math.max(0.0f, 1.0f - (delay * 0.16f));

                        final float jitter = ((delay & 1) == 0) ? 0.06f : -0.03f;
                        final float pMain = Math.max(0.5f, Math.min(2.0f, basePitch + jitter));
                        final float pTreble = Math.max(0.5f, Math.min(2.0f, treblePitch + jitter));

                        player.getWorld().playSound(padLocation, Sound.ENTITY_WITHER_SHOOT, 0.34f * falloff, pMain);
                        player.getWorld().playSound(padLocation, Sound.ENTITY_GHAST_SHOOT, 0.20f * falloff, pTreble);

                        player.getWorld().playSound(padLocation, Sound.ENTITY_BAT_TAKEOFF, 0.16f * falloff, 2.0f);
                        player.getWorld().playSound(padLocation, Sound.ENTITY_BLAZE_SHOOT, 0.10f * falloff, 2.0f);

                    }, delay);

                }

                final double padParticleCount = padConfig.getDouble("padLaunchParticleCount");
                final double padParticlePower = padConfig.getDouble("padLaunchParticlePower");
                player.spawnParticle(Particle.FIREWORKS_SPARK, padLocation, (int) padParticleCount, 0, 0, 0,
                        0.2 * padParticlePower);

                final double playerParticleMult = padConfig.getDouble("playerLaunchParticleCountMultipier");

                // Spawn particles on the player as they fly through the air
                // This equation was gotten with a linear regression on different amounts of
                // yPower
                // to find what the vertex of the player's jump is
                // maxes out at 25 ticks
                // \/ --------- \/
                for (int j = 0; j < Math.min(yPower * 8 - 15, 25); j++) {

                    // Max out the number of particles at 50
                    Bukkit.getScheduler().runTaskLater(LaunchPadsOG.getPlugin(),
                            () -> player.getWorld().spawnParticle(Particle.SPELL, player.getLocation(),
                                    (int) Math.min(xPower * zPower * playerParticleMult, 50)),
                            j);

                }

                // Add the player to the launchedPlayers list
                // 1 tick later so that the player is off the ground
                // and isn't removed immediately,
                Bukkit.getScheduler().runTaskLater(LaunchPadsOG.getPlugin(), () -> {

                    // Check if the player is in survival. Otherwise, don't add them
                    if (player.getGameMode().equals(GameMode.SURVIVAL)) {

                        LaunchPadsOG.getLaunchedPlayers().add(player);

                    }

                }, 1);

                break;

            }

        }

    }

}