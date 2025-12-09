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
import net.trueog.launchpadsog.Listeners.OnPlayerMove;

public final class LaunchPadsOG extends JavaPlugin {

    private static LaunchPadsOG plugin;
    private static FileConfiguration config;

    // this list will hold the players that have used a launchpad and are flying
    // through the air
    // then, when they land, I remove them from the list, and stop them from taking
    // fall damage
    private static ArrayList<Player> launchedPlayers = new ArrayList<Player>();

    @Override
    public void onEnable() {

        plugin = this;

        config = this.getConfig();

        reloadConfig();

        // if this is the first time the plugin is run, set cancraft to false
        if (config.get("cancraft") == null) {

            config.set("cancraft", false);
            saveConfig();

        }

        // if this is the first time the plugin is run, set power values to defaults
        // check for defaultXpower, defaultYpower, defaultZpower, and numberOfPads
        if (config.get("defaultXpower") == null) {

            config.set("defaultXpower", 3.0);
            saveConfig();

        }

        if (config.get("defaultYpower") == null) {

            config.set("defaultYpower", 4.0);
            saveConfig();

        }

        if (config.get("defaultZpower") == null) {

            config.set("defaultZpower", 3.0);
            saveConfig();

        }

        if (config.get("numberOfPads") == null) {

            config.set("numberOfPads", 0);
            saveConfig();

        }

        // check for ambientParticleCount and ambientParticlePower
        if (config.get("idleParticleCount") == null) {

            config.set("idleParticleCount", 4);
            saveConfig();

        }

        if (config.get("idleParticlePower") == null) {

            config.set("idleParticlePower", 1.5);
            saveConfig();

        }

        // check for padLaunchParticleCount and padLaunchParticlePower
        if (config.get("padLaunchParticleCount") == null) {

            config.set("padLaunchParticleCount", 80);
            saveConfig();

        }

        if (config.get("padLaunchParticlePower") == null) {

            config.set("padLaunchParticlePower", 1.0);
            saveConfig();

        }

        // check for playerLaunchParticleCountMultiplier
        if (config.get("playerLaunchParticleCountMultipier") == null) {

            config.set("playerLaunchParticleCountMultipier", 1.0);
            saveConfig();

        }

        // if the launchpad is craftable, add the recipe to the server
        if ((boolean) config.get("cancraft")) {
            // create the recipe for the launchpad
            // make it really expensive by locking it
            // behind many other crafting items

            // launcher base
            Recipes.addLauncherBase();
            // launcher launcher
            Recipes.addLauncherLauncher();
            // launcher explosion
            Recipes.addLauncherExploder();
            // launcher activator
            Recipes.addLauncherActivator();
            // final Launch Pad
            Recipes.addLaunchPad();

        }

        getServer().getPluginManager().registerEvents(new OnPlayerMove(), this);
        getServer().getPluginManager().registerEvents(new OnBlockBreak(), this);
        getServer().getPluginManager().registerEvents(new OnBlockPlace(), this);
        getServer().getPluginManager().registerEvents(new OnEntityDamage(), this);
        getCommand("launchpad").setExecutor(new LaunchPadCommands());
        getCommand("launchpad").setTabCompleter(new LaunchPadCommandCompleter());

        // make all launchpads emit particles when not in use
        // to signify that they are launchpads
        Bukkit.getScheduler().runTaskTimer(this, () -> {

            for (int i = 1; i < config.getInt("numberOfPads") + 1; i++) {

                String worldName = config.getString(i + ".world");
                World world = Bukkit.getWorld(worldName);
                double padX = config.getInt(i + ".x") + 0.5;
                double padY = config.getInt(i + ".y");
                double padZ = config.getInt(i + ".z") + 0.5;
                Location padLocation = new Location(world, padX, padY, padZ);
                int idleParticleCount = config.getInt("idleParticleCount");
                double idleParticlePower = config.getDouble("idleParticlePower");

                // spawn particles on the pad if the pad is loaded
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
