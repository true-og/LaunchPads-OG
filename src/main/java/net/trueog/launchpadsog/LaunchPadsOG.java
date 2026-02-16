package net.trueog.launchpadsog;

import java.util.ArrayList;

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
import net.trueog.launchpadsog.Listeners.OnPlayerInteract;
import net.trueog.launchpadsog.Listeners.OnPlayerMove;

public final class LaunchPadsOG extends JavaPlugin {

    private static LaunchPadsOG plugin;
    private static FileConfiguration config;

    // This list will hold the players that have used a launchpad and are flying
    // through the air
    // then, when they land, I remove them from the list, and stop them from taking
    // fall damage.
    private static final ArrayList<Player> launchedPlayers = new ArrayList<>();

    @Override
    public void onEnable() {

        plugin = this;

        saveDefaultConfig();
        config = this.getConfig();

        reloadConfig();

        // If the launchpad is craftable, add the recipe to the server.
        if ((boolean) config.get("cancraft")) {

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
        getCommand("launchpad").setExecutor(new LaunchPadCommands());
        getCommand("launchpad").setTabCompleter(new LaunchPadCommandCompleter());

        // Make all launchpads emit particles when not in use
        // to signify that they are launchpads.
        Bukkit.getScheduler().runTaskTimer(this, () -> {

            for (int i = 1; i < config.getInt("numberOfPads") + 1; i++) {

                final String worldName = config.getString(i + ".world");
                final World world = Bukkit.getWorld(worldName);
                final double padX = config.getInt(i + ".x") + 0.5;
                final double padY = config.getInt(i + ".y");
                final double padZ = config.getInt(i + ".z") + 0.5;
                final Location padLocation = new Location(world, padX, padY, padZ);
                final int idleParticleCount = config.getInt("idleParticleCount");
                final double idleParticlePower = config.getDouble("idleParticlePower");

                // Spawn particles on the pad if the pad is loaded.
                if (world.isChunkLoaded(padLocation.getChunk())) {

                    world.spawnParticle(Particle.DRAGON_BREATH, padLocation, idleParticleCount, 0, 0, 0,
                            0.05 * idleParticlePower);

                }

            }

        }, 1, 30);

    }

    public static LaunchPadsOG getPlugin() {

        return plugin;

    }

    public static FileConfiguration getConfiguration() {

        return config;

    }

    public static ArrayList<Player> getLaunchedPlayers() {

        return launchedPlayers;

    }

}