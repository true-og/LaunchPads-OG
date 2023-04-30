# LaunchPads-OG
Add launch pads to minecraft

Current Target: Purpur 1.18.2

Authors: Barny1094875.

<h3>To build:</h3>

./gradlew build

The resulting .jar file will be in build/libs



<h3>How to use:</h3>
Use /launchpad give to give yourself a launch pad item. When placed down, a launchpad will be created there with the default x,y,z powers in the config file. This command requires the cangive permission. This will require a server restart to take effect.

Use /launchpad set to create a pad where you are standing with the default x,y,z powers set in the config file. This requires the canset permision.

Use /launchpad set <xpower> <ypower> <zpower> to create a pad where you are standing with the x,y,z powers provided. This requires the canset permision.

Use /launchpad set <x> <y> <z> <xpower> <ypower> <zpower> to create a pad at the specified x,y,z coordinates with the specified x,y,z powers. This requires the canset permision.

Use /launchpad delete to remove the pad at the block you are pointing at. This requires the candelete permission.

Use /launchpad delete <x> <y> <z> to delete the pad at the specified coordinates in the world you are standing in. This requires the candelete permission.

Use /launchpad delete <world> <x> <y> <z> to delete the pad at the specified coordinates in the specified world. For world, enter any of: end, nether, overworld, world, world_nether, world_the_end. This requires the candelete permission.

Launchpads can also be removed by placing a block in their location and breaking it. This requires either cancraft to be set to true, or the player to have the canbreak permission.

<h3>Crafting Recipes</h3>
The launch pad can be crafted if cancraft is set to true in the config. The crafting recipes are as follows:
<br>

![2023-04-19_19 01 42](https://user-images.githubusercontent.com/128558829/233219063-d7055c71-250b-4f46-99b5-2f62fc51c7e9.png)
<br>
![2023-04-19_19 02 04](https://user-images.githubusercontent.com/128558829/233219065-326bf3df-ade4-473c-8fcd-b3f944744249.png)
<br>
![2023-04-19_19 02 34](https://user-images.githubusercontent.com/128558829/233219069-a4ac5816-16e9-4a96-979f-e0c1e5e79d63.png)
<br>
![2023-04-19_19 00 48](https://user-images.githubusercontent.com/128558829/233219072-eafc4d0d-d13a-4aa7-93dc-2de4e0bbaf1b.png)
<br>
![2023-04-19_19 03 31](https://user-images.githubusercontent.com/128558829/233219070-59513790-5742-4259-947c-9902bba93b08.png)


<h3>Config Information:</h3>

cancraft allows the launch pad to be crafted.

defaultXpower is the default x power value that will be used when creating a launch pad.

defaultYpower is the default y power value that will be used when creating a launch pad.

defaultZpower is the default z power value that will be used when creating a launch pad.

idleParticleCount is the number of particles emmited by the launch pads when they are not in use

idleParticlePower is how far the particles are shot from the center of the launch pad when they are not in use

padLaunchParticleCount is the number of particles produced by the launch pads when a launch occurs

padLaunchParticlePower is how far the particles are shot from the center of the launch pads when a launch occurs

playerLaunchParticleCountMultiplier is a scalar for the number of particles emmited by the player as they fly through the air


<br>
Each pad has it's own x, y, z, xpower, ypower, zpower attached to it. If you want to change the power of a single pad, go into the config file, and look for that pad. For convenience, you can CTRL+F for a pad by typing in its x, y, z coordinates with spaces in between. For example, if the pad has coordinates x=112, y=61, z=434, you can search for it in your config file by using CTRL+F and typing "112 61 434"

Once you have found your pad, you can change the xpower, ypower, and zpower to change how far the pad will launch you.

When your changes have been made, you will need to run the command /launchpad reloadconfig. This command will update the config with your changes. This command requires the canreload permission.
