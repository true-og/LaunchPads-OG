package net.trueog.launchpadsog;

import java.util.ArrayList;
import net.trueog.launchpadsog.Commands.LaunchPadCommandCompleter;
import net.trueog.launchpadsog.Commands.LaunchPadCommands;
import net.trueog.launchpadsog.Listeners.OnBlockBreak;
import net.trueog.launchpadsog.Listeners.OnBlockPlace;
import net.trueog.launchpadsog.Listeners.OnEntityDamage;
import net.trueog.launchpadsog.Listeners.OnPlayerMove;
import net.trueog.launchpadsog.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class LaunchPadsOG extends JavaPlugin {

    private static LaunchPadsOG plugin;
    private static Config config;

    // this list will hold the players that have used a launchpad and are flying through the air
    // then, when they land, I remove them from the list, and stop them from taking fall damage
    private static ArrayList<Player> launchedPlayers = new ArrayList<Player>();

    @Override
    public void onEnable() {
        plugin = this;

        // config stuff
        config = new Config(this, this.getDataFolder(), "config", true, false);

        // if this is the first time the plugin is run, set cancraft to false
        if (config.getConfig().get("cancraft") == null) {
            config.getConfig().set("cancraft", false);
            config.save();
        }
        // if this is the first time the plugin is run, set power values to defaults
        // check for defaultXpower, defaultYpower, defaultZpower, and numberOfPads
        if (config.getConfig().get("defaultXpower") == null) {
            config.getConfig().set("defaultXpower", 3.0);
            config.save();
        }
        if (config.getConfig().get("defaultYpower") == null) {
            config.getConfig().set("defaultYpower", 4.0);
            config.save();
        }
        if (config.getConfig().get("defaultZpower") == null) {
            config.getConfig().set("defaultZpower", 3.0);
            config.save();
        }
        if (config.getConfig().get("numberOfPads") == null) {
            config.getConfig().set("numberOfPads", 0);
            config.save();
        }
        // check for ambientParticleCount and ambientParticlePower
        if (config.getConfig().get("idleParticleCount") == null) {
            config.getConfig().set("idleParticleCount", 4);
            config.save();
        }
        if (config.getConfig().get("idleParticlePower") == null) {
            config.getConfig().set("idleParticlePower", 1.5);
            config.save();
        }
        // check for padLaunchParticleCount and padLaunchParticlePower
        if (config.getConfig().get("padLaunchParticleCount") == null) {
            config.getConfig().set("padLaunchParticleCount", 80);
            config.save();
        }
        if (config.getConfig().get("padLaunchParticlePower") == null) {
            config.getConfig().set("padLaunchParticlePower", 1.0);
            config.save();
        }
        // check for playerLaunchParticleCountMultiplier
        if (config.getConfig().get("playerLaunchParticleCountMultipier") == null) {
            config.getConfig().set("playerLaunchParticleCountMultipier", 1.0);
            config.save();
        }

        // if the launchpad is craftable, add the recipe to the server
        if ((boolean) config.getConfig().get("cancraft")) {
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
        Bukkit.getScheduler()
                .runTaskTimer(
                        this,
                        () -> {
                            for (int i = 1; i < config.getConfig().getInt("numberOfPads") + 1; i++) {

                                String worldName = config.getConfig().getString(i + ".world");
                                World world = Bukkit.getWorld(worldName);
                                double padX = config.getConfig().getInt(i + ".x") + 0.5;
                                double padY = config.getConfig().getInt(i + ".y");
                                double padZ = config.getConfig().getInt(i + ".z") + 0.5;
                                Location padLocation = new Location(world, padX, padY, padZ);
                                int idleParticleCount = config.getConfig().getInt("idleParticleCount");
                                double idleParticlePower = config.getConfig().getDouble("idleParticlePower");

                                // spawn particles on the pad if the pad is loaded
                                if (world.isChunkLoaded(padLocation.getChunk())) {
                                    world.spawnParticle(
                                            Particle.DRAGON_BREATH,
                                            padLocation,
                                            idleParticleCount,
                                            0,
                                            0,
                                            0,
                                            0.05 * idleParticlePower);
                                }
                            }
                        },
                        1,
                        30);
    }

    public static LaunchPadsOG getPlugin() {
        return plugin;
    }

    public static Config config() {
        return config;
    }

    public static ArrayList<Player> getLaunchedPlayers() {
        return launchedPlayers;
    }
}
