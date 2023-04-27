package me.barny1094875.launchpad;

import me.barny1094875.launchpad.Commands.givePad;
import me.barny1094875.launchpad.Commands.reloadPadConfig;
import me.barny1094875.launchpad.Commands.setPad;
import me.barny1094875.launchpad.Listeners.onBlockBreak;
import me.barny1094875.launchpad.Listeners.onBlockPlace;
import me.barny1094875.launchpad.Listeners.onPlayerMove;
import me.barny1094875.launchpad.config.Config;
import org.bukkit.plugin.java.JavaPlugin;


public final class LaunchPad extends JavaPlugin {

    private static LaunchPad plugin;
    private static Config config;
//    private static Config padCoords;

    @Override
    public void onEnable() {
        plugin = this;
        // Plugin startup logic


        // config stuff
        config = new Config(this, this.getDataFolder(), "config", true, false);

        // if this is the first time the plugin is run, set cancraft to false
        if(config.getConfig().get("cancraft") == null){
            config.getConfig().set("cancraft", false);
            config.save();
        }
        // check for defaultXpower, defaultYpower, and defaultZpower
        if(config.getConfig().get("defaultXpower") == null){
            config().getConfig().set("defaultXpower", 3);
            config.save();
        }
        if(config.getConfig().get("defaultYpower") == null){
            config().getConfig().set("defaultYpower", 4);
            config.save();
        }
        if(config.getConfig().get("defaultZpower") == null){
            config().getConfig().set("defaultZpower", 3);
            config.save();
        }
        // if this is the first time the plugin is run, set power values to defaults
        if(config.getConfig().get("numberOfPads") == null){
            config.getConfig().set("numberOfPads", 0);
            config.save();
        }



        // if the launchpad is craftable, add the recipe to the server
        if((boolean) this.config.getConfig().get("cancraft")) {
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

        getServer().getPluginManager().registerEvents(new onPlayerMove(), this);
        getServer().getPluginManager().registerEvents(new onBlockBreak(), this);
        getServer().getPluginManager().registerEvents(new onBlockPlace(), this);
        getCommand("givepad").setExecutor(new givePad());
        getCommand("reloadpadconfig").setExecutor(new reloadPadConfig());
        getCommand("setpad").setExecutor(new setPad());

    }

    public static LaunchPad getPlugin(){
        return plugin;
    }

//    public static Config getPadCoords(){
//        return padCoords;
//    }

    public static Config config(){
        return config;
    }
}
