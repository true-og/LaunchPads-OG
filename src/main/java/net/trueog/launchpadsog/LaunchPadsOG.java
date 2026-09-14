package net.trueog.launchpadsog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.trueog.launchpadsog.Commands.LaunchPadCommandCompleter;
import net.trueog.launchpadsog.Commands.LaunchPadCommands;
import net.trueog.launchpadsog.Listeners.OnBlockBreak;
import net.trueog.launchpadsog.Listeners.OnBlockPlace;
import net.trueog.launchpadsog.Listeners.OnEntityDamage;
import net.trueog.launchpadsog.Listeners.OnLaunchEnd;
import net.trueog.launchpadsog.Listeners.OnPlayerInteract;
import net.trueog.launchpadsog.Listeners.OnPlayerMove;

public final class LaunchPadsOG extends JavaPlugin {

    private static LaunchPadsOG plugin;

    // Only an AIRBORNE protection can be spent or dropped, so a slow client
    // still reporting the pad position cannot burn it before liftoff.
    public enum LaunchPhase {
        AWAITING_LIFTOFF, AIRBORNE
    }

    // Players inside a live launch arc, tracked by id so relogs and repeated
    // pad hits can neither stack protections nor strand stale entries.
    private static final Map<UUID, LaunchPhase> launchedPlayers = new HashMap<>();

    // Pad ids indexed by "world:x:y:z", so the move handler does one map
    // lookup instead of scanning every configured pad on every step.
    private static final Map<String, Integer> padsByBlock = new HashMap<>();

    // Particle reach, squared. Vanilla un-forced range on modern clients.
    private static final double PARTICLE_RANGE_SQUARED = 32 * 32;

    @Override
    public void onEnable() {

        plugin = this;

        saveDefaultConfig();
        reloadConfig();

        // If the launchpad is craftable, add the recipe to the server.
        if (getConfig().getBoolean("cancraft")) {

            // Create the recipe for the launchpad
            // make it really expensive by locking it
            // behind many other crafting items.
            // Launcher base.
            Recipes.addLauncherBase();
            // Launcher launcher.
            Recipes.addLauncherLauncher();
            // Launcher explosion.
            Recipes.addLauncherExploder();
            // Launcher activator.
            Recipes.addLauncherActivator();
            // Final launchpd.
            Recipes.addLaunchPad();

        }

        getServer().getPluginManager().registerEvents(new OnPlayerMove(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerInteract(), this);
        getServer().getPluginManager().registerEvents(new OnBlockBreak(), this);
        getServer().getPluginManager().registerEvents(new OnBlockPlace(), this);
        getServer().getPluginManager().registerEvents(new OnEntityDamage(), this);
        getServer().getPluginManager().registerEvents(new OnLaunchEnd(), this);
        getCommand("launchpad").setExecutor(new LaunchPadCommands());
        getCommand("launchpad").setTabCompleter(new LaunchPadCommandCompleter());

        // Make all launchpads emit particles when not in use
        // to signify that they are launchpads.
        Bukkit.getScheduler().runTaskTimer(this, () -> {

            final FileConfiguration padConfig = getConfig();
            final int idleParticleCount = padConfig.getInt("idleParticleCount");
            final double idleParticlePower = padConfig.getDouble("idleParticlePower");
            for (int i = 1; i < padConfig.getInt("numberOfPads") + 1; i++) {

                final String worldName = padConfig.getString(i + ".world");
                final World world = worldName == null ? null : Bukkit.getWorld(worldName);
                if (world == null) {

                    continue;

                }

                final int padX = padConfig.getInt(i + ".x");
                final int padY = padConfig.getInt(i + ".y");
                final int padZ = padConfig.getInt(i + ".z");

                // Asked by chunk coordinates on purpose: getChunk() on a
                // location loads and pins the chunk just to answer.
                if (!world.isChunkLoaded(padX >> 4, padZ >> 4)) {

                    continue;

                }

                final Location padLocation = new Location(world, padX + 0.5, padY, padZ + 0.5);

                // Use PORTAL, what ViaRewind downgrades DRAGON_BREATH to, so 1.8 and modern
                // clients match.
                spawnParticleForAllClients(Particle.PORTAL, padLocation, idleParticleCount, 0.05 * idleParticlePower);

            }

        }, 1, 30);

    }

    // Forced particle within 32 blocks, so 1.8 and modern clients match.
    public static void spawnParticleForAllClients(Particle particle, Location location, int count, double speed) {

        final World world = location.getWorld();
        if (world == null) {

            return;

        }

        final List<Player> receivers = new ArrayList<>();
        for (final Player player : world.getPlayers()) {

            if (player.getLocation().distanceSquared(location) <= PARTICLE_RANGE_SQUARED) {

                receivers.add(player);

            }

        }

        if (receivers.isEmpty()) {

            return;

        }

        world.spawnParticle(particle, receivers, null, location.getX(), location.getY(), location.getZ(), count, 0, 0,
                0, speed, null, true);

    }

    @Override
    public void onDisable() {

        launchedPlayers.clear();
        padsByBlock.clear();

    }

    // Every save and reload funnels through these, so the pad index can never
    // go stale against the configuration.
    @Override
    public void saveConfig() {

        super.saveConfig();
        rebuildPadIndex();

    }

    @Override
    public void reloadConfig() {

        super.reloadConfig();
        rebuildPadIndex();

    }

    public static LaunchPadsOG getPlugin() {

        return plugin;

    }

    // Always the live configuration: the old cached object was silently
    // swapped out by reloadConfig(), losing /launchpad set edits on restart.
    public static FileConfiguration getConfiguration() {

        return plugin.getConfig();

    }

    public static void markLaunched(Player player) {

        launchedPlayers.put(player.getUniqueId(), LaunchPhase.AWAITING_LIFTOFF);

    }

    public static LaunchPhase launchPhase(Player player) {

        return launchedPlayers.get(player.getUniqueId());

    }

    public static void markAirborne(Player player) {

        launchedPlayers.replace(player.getUniqueId(), LaunchPhase.AIRBORNE);

    }

    // Spends the protection of an arc in flight; damage before liftoff is the
    // arrival fall onto the pad, which stays the player's own to eat.
    public static boolean consumeAirborneLaunch(Player player) {

        if (launchedPlayers.get(player.getUniqueId()) != LaunchPhase.AIRBORNE)
            return false;
        launchedPlayers.remove(player.getUniqueId());
        return true;

    }

    public static void clearLaunch(Player player) {

        launchedPlayers.remove(player.getUniqueId());

    }

    // The pad id at a block position, or null when no pad sits there.
    public static Integer padAt(String worldName, int x, int y, int z) {

        return padsByBlock.get(worldName + ":" + x + ":" + y + ":" + z);

    }

    private void rebuildPadIndex() {

        padsByBlock.clear();
        final FileConfiguration padConfig = getConfig();
        for (int i = 1; i < padConfig.getInt("numberOfPads") + 1; i++) {

            // isSet ignores the jar defaults; plain getters resolve through
            // them and would resurrect every hand-deleted pad.
            if (!padConfig.isSet(i + ".world")) {

                continue;

            }

            // putIfAbsent keeps the old scan's first-match-wins order when two
            // entries share one block.
            padsByBlock.putIfAbsent(padConfig.getString(i + ".world") + ":" + padConfig.getInt(i + ".x") + ":"
                    + padConfig.getInt(i + ".y") + ":" + padConfig.getInt(i + ".z"), i);

        }

    }

}
