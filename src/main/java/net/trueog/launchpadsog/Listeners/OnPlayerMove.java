package net.trueog.launchpadsog.Listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import net.trueog.launchpadsog.LaunchPadsOG;

public class OnPlayerMove implements Listener {

    // One boost per pass: a glider re-arms a pad only by leaving its radius.
    private final Map<UUID, Integer> glidePads = new HashMap<>();

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {

        final Player player = event.getPlayer();
        final Location playerLocation = event.getTo();
        if (playerLocation == null) {

            return;

        }

        final LaunchPadsOG.LaunchPhase phase = LaunchPadsOG.launchPhase(player);
        if (phase == LaunchPadsOG.LaunchPhase.AWAITING_LIFTOFF) {

            // A slow client reports the pad position for a full round trip, so
            // the protection only arms once liftoff is actually seen.
            if (player.getFallDistance() > 0.0F || !hasLandedAt(playerLocation))
                LaunchPadsOG.markAirborne(player);

        } else if (phase == LaunchPadsOG.LaunchPhase.AIRBORNE) {

            // Arc over: held up again, not ascending (fallDistance stays 0 all
            // ascent), no fall live. getTo(): this step's damage fires after us.
            if (!player.isFlying() && player.getFallDistance() <= 0.0F
                    && playerLocation.getY() <= event.getFrom().getY() && hasLandedAt(playerLocation))
                LaunchPadsOG.clearLaunch(player);

        }

        if (!event.hasChangedBlock()) {

            return;

        }

        // Riders are skipped: their move events carry the vehicle's position,
        // and setVelocity cannot move a mounted player anyway.
        final Integer padId = player.isInsideVehicle() ? null : padNear(player, playerLocation);

        if (padId != null) {

            // A glider skimming the radius matches on several packets in a
            // row; only the first one fires the boost.
            if (player.isGliding() && padId.equals(glidePads.put(player.getUniqueId(), padId))) {

                return;

            }

            final FileConfiguration padConfig = LaunchPadsOG.getConfiguration();
            final int i = padId;

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
            // Visible to everyone nearby, matching the in-air trail below.
            LaunchPadsOG.spawnParticleForAllClients(Particle.FIREWORKS_SPARK, padLocation, (int) padParticleCount,
                    0.2 * padParticlePower);

            final double playerParticleMult = padConfig.getDouble("playerLaunchParticleCountMultipier");

            // Spawn particles on the player as they fly through the air
            // This equation was gotten with a linear regression on different amounts of
            // yPower
            // to find what the vertex of the player's jump is
            // maxes out at 25 ticks
            // \/ --------- \/
            for (int j = 0; j < Math.min(yPower * 8 - 15, 25); j++) {

                // Max out the number of particles at 50, speed 1 as before.
                Bukkit.getScheduler().runTaskLater(LaunchPadsOG.getPlugin(),
                        () -> LaunchPadsOG.spawnParticleForAllClients(Particle.SPELL, player.getLocation(),
                                (int) Math.min(xPower * zPower * playerParticleMult, 50), 1),
                        j);

            }

            // Eligibility sampled at launch: adventure falls like survival,
            // and an already-gliding player is in controlled flight.
            final GameMode launchGamemode = player.getGameMode();
            final boolean protectable = (launchGamemode == GameMode.SURVIVAL || launchGamemode == GameMode.ADVENTURE)
                    && !player.isGliding();

            if (protectable) {

                if (phase == LaunchPadsOG.LaunchPhase.AIRBORNE && player.getFallDistance() > 0.0F) {

                    // Landing here out of a previous arc: that arc's landing
                    // damage spends first, so the new arc arms a tick later.
                    Bukkit.getScheduler().runTaskLater(LaunchPadsOG.getPlugin(), () -> {

                        if (player.isOnline() && !player.isDead()) {

                            LaunchPadsOG.markLaunched(player);

                        }

                    }, 1);

                } else {

                    // Immediate: the pre-liftoff phase can neither be spent
                    // nor dropped, so there is nothing to wait for.
                    LaunchPadsOG.markLaunched(player);

                }

            }

        } else {

            // out of every pad's radius again: the glide debounce re-arms
            glidePads.remove(player.getUniqueId());

        }

    }

    // Gliders skim rather than stand, so a pad within one block still fires;
    // everyone else must be inside the pad's own block, as always.
    private static Integer padNear(Player player, Location location) {

        final String worldName = location.getWorld().getName();
        final int x = location.getBlockX();
        final int y = location.getBlockY();
        final int z = location.getBlockZ();

        final Integer exact = LaunchPadsOG.padAt(worldName, x, y, z);
        if (exact != null || !player.isGliding()) {

            return exact;

        }

        for (int dy = -1; dy <= 1; dy++) {

            for (int dx = -1; dx <= 1; dx++) {

                for (int dz = -1; dz <= 1; dz++) {

                    final Integer pad = LaunchPadsOG.padAt(worldName, x + dx, y + dy, z + dz);
                    if (pad != null) {

                        return pad;

                    }

                }

            }

        }

        return null;

    }

    // Whether something at or right under this spot ends a fall: standing
    // room on a block, water, or a climbable the player has grabbed.
    private static boolean hasLandedAt(Location location) {

        final Block feet = location.getBlock();
        if (feet.isLiquid() || Tag.CLIMBABLE.isTagged(feet.getType())) {

            return true;

        }

        // 0.4 covers blocks, slabs, and carpets; 1.6 covers fences and walls,
        // whose standing surface sits above the block that holds them.
        final Block shallow = location.clone().subtract(0.0, 0.4, 0.0).getBlock();
        if (shallow.isLiquid() || !shallow.isPassable()) {

            return true;

        }

        return !location.clone().subtract(0.0, 1.6, 0.0).getBlock().isPassable();

    }

}