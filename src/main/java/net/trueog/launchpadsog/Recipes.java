package net.trueog.launchpadsog;

import java.util.Collections;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class Recipes {

    private static java.util.Vector<Component> loreList = new java.util.Vector<>();
    private static ItemStack launcherBase;
    private static ItemStack launcherLaunch;
    private static ItemStack launcherExploder;
    private static ItemStack launcherActivator;

    public static void addLauncherBase() {

        launcherBase = new ItemStack(Material.PISTON);
        ItemMeta baseMeta = launcherBase.getItemMeta();

        baseMeta.displayName(Component.text("Launcher Base").decorate(TextDecoration.ITALIC));

        loreList.clear();
        loreList.add((Component.text("The base component for")));
        loreList.add((Component.text("making a launchpad")));
        baseMeta.lore(Collections.list(loreList.elements()));

        // add a useless enchantment to the item to make it
        // stand out in the inventory
        baseMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, false);

        launcherBase.setItemMeta(baseMeta);

        NamespacedKey baseKey = new NamespacedKey(LaunchPadsOG.getPlugin(), "launcherBase");
        ShapedRecipe baseRecipe = new ShapedRecipe(baseKey, launcherBase);
        baseRecipe.shape("iii", "grg");
        baseRecipe.setIngredient('i', Material.IRON_BLOCK);
        baseRecipe.setIngredient('g', Material.GOLD_BLOCK);
        baseRecipe.setIngredient('r', Material.REDSTONE_BLOCK);

        Bukkit.addRecipe(baseRecipe);
    }

    public static void addLauncherLauncher() {

        launcherLaunch = new ItemStack(Material.DISPENSER);
        ItemMeta launchMeta = launcherLaunch.getItemMeta();

        launchMeta.displayName(Component.text("Launcher").decorate(TextDecoration.ITALIC));

        loreList.clear();
        loreList.add(Component.text("The launcher for"));
        loreList.add(Component.text("a launchpad"));
        launchMeta.lore(Collections.list(loreList.elements()));

        // add a useless enchantment to the item to make it
        // stand out in the inventory
        launchMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, false);

        launcherLaunch.setItemMeta(launchMeta);

        NamespacedKey armKey = new NamespacedKey(LaunchPadsOG.getPlugin(), "launcherArm");
        ShapedRecipe armRecipe = new ShapedRecipe(armKey, launcherLaunch);
        armRecipe.shape("  s", " ib", "i  ");
        armRecipe.setIngredient('s', Material.STRING);
        armRecipe.setIngredient('i', Material.IRON_BLOCK);
        armRecipe.setIngredient('b', Material.BUCKET);

        Bukkit.addRecipe(armRecipe);
    }

    public static void addLauncherExploder() {

        launcherExploder = new ItemStack(Material.TNT);
        ItemMeta exploderMeta = launcherExploder.getItemMeta();

        exploderMeta.displayName(Component.text("Launcher Exploder").decorate(TextDecoration.ITALIC));

        loreList.clear();
        loreList.add(Component.text("The launcher arm for"));
        loreList.add(Component.text("a launchpad"));
        exploderMeta.lore(Collections.list(loreList.elements()));

        // add a useless enchantment to the item to make it
        // stand out in the inventory
        exploderMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, false);

        launcherExploder.setItemMeta(exploderMeta);

        NamespacedKey exploderKey = new NamespacedKey(LaunchPadsOG.getPlugin(), "launcherExploder");
        ShapedRecipe exploderRecipe = new ShapedRecipe(exploderKey, launcherExploder);
        exploderRecipe.shape("ttt", "tbt", "ttt");
        exploderRecipe.setIngredient('t', Material.TNT);
        exploderRecipe.setIngredient('b', Material.TARGET);

        Bukkit.addRecipe(exploderRecipe);
    }

    public static void addLauncherActivator() {

        launcherActivator = new ItemStack(Material.TRIPWIRE_HOOK);
        ItemMeta activatorMeta = launcherActivator.getItemMeta();

        activatorMeta.displayName(Component.text("Launcher Activator").decorate(TextDecoration.ITALIC));

        loreList.clear();
        loreList.add(Component.text("The activator for"));
        loreList.add(Component.text("a launchpad"));
        activatorMeta.lore(Collections.list(loreList.elements()));

        // add a useless enchantment to the item to make it
        // stand out in the inventory
        activatorMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, false);

        launcherActivator.setItemMeta(activatorMeta);

        NamespacedKey activatorKey = new NamespacedKey(LaunchPadsOG.getPlugin(), "launcherActivator");
        ShapedRecipe activatorRecipe = new ShapedRecipe(activatorKey, launcherActivator);
        activatorRecipe.shape("rrr", "rdr", "rrr");
        activatorRecipe.setIngredient('r', Material.REDSTONE_BLOCK);
        activatorRecipe.setIngredient('d', Material.DIAMOND_BLOCK);

        Bukkit.addRecipe(activatorRecipe);
    }

    public static void addLaunchPad() {

        ItemStack launchPad = new ItemStack(Material.STONE_PRESSURE_PLATE);
        ItemMeta padMeta = launchPad.getItemMeta();

        padMeta.displayName(Component.text("Launch Pad").decorate(TextDecoration.ITALIC));

        loreList.clear();
        loreList.add(Component.text("Launches you in the"));
        loreList.add(Component.text("direction you are facing"));
        padMeta.lore(Collections.list(loreList.elements()));

        // add a useless enchantment to the item to make it
        // stand out in the inventory
        padMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, false);

        launchPad.setItemMeta(padMeta);

        NamespacedKey padKey = new NamespacedKey(LaunchPadsOG.getPlugin(), "launchPad");
        ShapedRecipe padRecipe = new ShapedRecipe(padKey, launchPad);
        padRecipe.shape("gng", "geg", "lba");
        padRecipe.setIngredient('g', Material.GOLD_BLOCK);
        padRecipe.setIngredient('n', Material.NOTE_BLOCK);
        padRecipe.setIngredient('e', launcherExploder);
        padRecipe.setIngredient('l', launcherLaunch);
        padRecipe.setIngredient('b', launcherBase);
        padRecipe.setIngredient('a', launcherActivator);

        Bukkit.addRecipe(padRecipe);
    }
}
