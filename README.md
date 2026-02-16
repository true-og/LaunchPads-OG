# LaunchPads-OG

Add launch pads to Minecraft.

- **Current target:** Purpur 1.19.4
- **Authors:** Barny1094875, NotAlexNoyle
- **License:** Public Domain (The Unlicense)

## Build

```bash
./gradlew build
```

The resulting `.jar` file will be in `build/libs`.

## Changelog

### 1.7
- Pressure plates on Launch Pads no longer trigger redstone
- Improved launch feel and consistency
- New “woosh” launch sound effect
- Cleaner particle effects while launching
- Config defaults handled more reliably
- General polish and small command autocomplete improvements

## How to use

- `/launchpad give`
  Gives you a Launch Pad item. When placed, a launchpad is created using the default x/y/z powers in the config.
  **Permission:** `cangive`
  **Note:** Requires a server restart to take effect.

- `/launchpad set`
  Creates a pad where you are standing using the default x/y/z powers in the config.
  **Permission:** `canset`

- `/launchpad set <xpower> <ypower> <zpower>`
  Creates a pad where you are standing with the provided x/y/z powers.
  **Permission:** `canset`

- `/launchpad set <x> <y> <z> <xpower> <ypower> <zpower>`
  Creates a pad at the specified coordinates with the specified powers.
  **Permission:** `canset`

- `/launchpad delete`
  Removes the pad at the block you are pointing at.
  **Permission:** `candelete`

- `/launchpad delete <x> <y> <z>`
  Deletes the pad at the specified coordinates in the world you are standing in.
  **Permission:** `candelete`

- `/launchpad delete <world> <x> <y> <z>`
  Deletes the pad at the specified coordinates in the specified world.
  For `<world>`, enter any of: `end`, `nether`, `overworld`, `world`, `world_nether`, `world_the_end`
  **Permission:** `candelete`

Launchpads can also be removed by placing a block in their location and breaking it. This requires either `cancraft` to be set to `true`, or the player to have the `canbreak` permission.

## Crafting Recipes

The Launch Pad can be crafted if `cancraft` is set to `true` in the config. The crafting recipes are as follows:

![2023-04-19_19 01 42](https://user-images.githubusercontent.com/128558829/233219063-d7055c71-250b-4f46-99b5-2f62fc51c7e9.png)

![2023-04-19_19 02 04](https://user-images.githubusercontent.com/128558829/233219065-326bf3df-ade4-473c-8fcd-b3f944744249.png)

![2023-04-19_19 02 34](https://user-images.githubusercontent.com/128558829/233219069-a4ac5816-16e9-4a96-979f-e0c1e5e79d63.png)

![2023-04-19_19 00 48](https://user-images.githubusercontent.com/128558829/233219072-eafc4d0d-d13a-4aa7-93dc-2de4e0bbaf1b.png)

![2023-04-19_19 03 31](https://user-images.githubusercontent.com/128558829/233219070-59513790-5742-4259-947c-9902bba93b08.png)

## Config Information

- `cancraft` allows the launch pad to be crafted
- `defaultXpower` is the default x power value used when creating a launch pad
- `defaultYpower` is the default y power value used when creating a launch pad
- `defaultZpower` is the default z power value used when creating a launch pad
- `idleParticleCount` is the number of particles emitted by launch pads when they are not in use
- `idleParticlePower` is how far particles are shot from the center of the launch pad when not in use
- `padLaunchParticleCount` is the number of particles produced when a launch occurs
- `padLaunchParticlePower` is how far particles are shot from the center during a launch
- `playerLaunchParticleCountMultiplier` is a scalar for the number of particles emitted by the player while launching

Each pad has its own `x`, `y`, `z`, `xpower`, `ypower`, `zpower` in the config. To change the power of a single pad, find it in the config file.

For convenience, you can use `CTRL+F` and search for the pad by typing its coordinates with spaces in between. For example, if the pad is at `x=112`, `y=61`, `z=434`, search for:

`112 61 434`

After editing values, run:

- `/launchpad reloadconfig`
  Reloads the config and validates it.
  **Permission:** `canreload`

