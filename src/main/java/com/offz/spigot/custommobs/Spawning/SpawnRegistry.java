package com.offz.spigot.custommobs.Spawning;

import com.offz.spigot.custommobs.ConfigManager;
import com.offz.spigot.custommobs.CustomType;
import com.offz.spigot.custommobs.Spawning.Regions.SpawnRegion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SpawnRegistry {
    private static Map<String, SpawnRegion> layerSpawns = new HashMap<>();

    static {
/*

        addSpawn("The Goblet of Giants",
                new MobSpawn.MobSpawnBuilder().setEntityType(CustomType.OTTOBAS).setWhitelist(Material.WATER).build(),
                new MobSpawn.MobSpawnBuilder().setEntityType(CustomType.ROHANA).setMaxLightLevel(7).setMinAmount(4).setMaxAmount(5).setRadius(6).setWhitelist(Material.GRASS_BLOCK).build(),
                new MobSpawn.MobSpawnBuilder().setEntityType(CustomType.KAZURA).setMaxLightLevel(7).setWhitelist(Material.WATER).build(),
                new MobSpawn.MobSpawnBuilder().setEntityType(CustomType.KUONGATARI).setMinAmount(2).setMaxAmount(4).setRadius(6).setMaxLightLevel(7).setWhitelist(Material.GRASS_BLOCK).build()
        );*/

        addSpawn("Sea of Corpses");

        addSpawn("The Capital of the Unreturned");

        addSpawn("The Final Maelstrom");
    }

    public static void readCfg(ConfigManager configManager) {
        FileConfiguration config = configManager.getSpawnCfg();
        List<Map<?, ?>> regionList = config.getMapList("regions");

        for (Map<?, ?> region : regionList) {
            String name = (String) region.get("name");
            SpawnRegion spawnRegion = new SpawnRegion(name);
            try {
                List<Map<?, ?>> spawnList = (List<Map<?, ?>>) region.get("spawns");

                for (Map<?, ?> spawn : spawnList) {
                    MobSpawn.Builder mobSpawn = new MobSpawn.Builder();

                    //create a new builder from the MobSpawn found inside of an already created layer
                    if (spawn.containsKey("reuse")) {
                        String reusedMob = (String) spawn.get("reuse");
                        mobSpawn = new MobSpawn.Builder(getLayerSpawns().get(reusedMob.substring(0, reusedMob.indexOf(':'))).getSpawnOfType(CustomType.getType(reusedMob.substring(reusedMob.indexOf(':') + 1))));
                    }
                    //required information
                    else if (!spawn.containsKey("mob"))
                        break;

                    if (spawn.containsKey("mob"))
                        mobSpawn.setEntityType(CustomType.getType((String) spawn.get("mob")));
                    if (spawn.containsKey("priority"))
                        mobSpawn.setBasePriority((Double) spawn.get("priority"));
                    if (spawn.containsKey("min amount"))
                        mobSpawn.setMaxAmount((Integer) spawn.get("min amount"));
                    if (spawn.containsKey("max amount"))
                        mobSpawn.setMaxAmount((Integer) spawn.get("max amount"));
                    if (spawn.containsKey("min gap"))
                        mobSpawn.setMinGap((Integer) spawn.get("min gap"));
                    if (spawn.containsKey("max gap"))
                        mobSpawn.setMaxGap((Integer) spawn.get("max gap"));
                    if (spawn.containsKey("min light"))
                        mobSpawn.setMinLightLevel((Integer) spawn.get("min light"));
                    if (spawn.containsKey("max light"))
                        mobSpawn.setMaxLightLevel((Integer) spawn.get("max light"));
                    if (spawn.containsKey("min time"))
                        mobSpawn.setMinTime((Long) spawn.get("min time"));
                    if (spawn.containsKey("max time"))
                        mobSpawn.setMaxTime((Long) spawn.get("max time"));
                    if (spawn.containsKey("min y"))
                        mobSpawn.setMinY((Integer) spawn.get("min y"));
                    if (spawn.containsKey("max y"))
                        mobSpawn.setMaxY((Integer) spawn.get("max y"));
                    if (spawn.containsKey("radius"))
                        mobSpawn.setRadius((Integer) spawn.get("radius"));
                    //TODO won't be needed when regions are defined in WorldGuard/by ourselves
                    if (spawn.containsKey("sections"))
                        mobSpawn.setSections((List<String>) spawn.get("sections"));
                    if (spawn.containsKey("spawnpos")) {
                        MobSpawn.SpawnPosition spawnPos = MobSpawn.SpawnPosition.GROUND;
                        switch ((String) spawn.get("spawnpos")) {
                            case "AIR":
                                spawnPos = MobSpawn.SpawnPosition.AIR;
                                break;
                            case "GROUND":
                                spawnPos = MobSpawn.SpawnPosition.GROUND;
                                break;
                            case "OVERHANG":
                                spawnPos = MobSpawn.SpawnPosition.OVERHANG;
                                break;
                        }
                        mobSpawn.setSpawnPos(spawnPos);
                    }
                    if (spawn.containsKey("block whitelist")) {
                        List<Material> materialWhiteist = ((List<String>) spawn.get("block whitelist")).stream()
                                .map(Material::valueOf)
                                .collect(Collectors.toList());

                        mobSpawn.setWhitelist(materialWhiteist);
                    }

                    spawnRegion.addSpawn(mobSpawn.build());
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Skipped region in spawns.yml because of misformatted config");
            }
            layerSpawns.put(name, spawnRegion);
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Reloaded spawns.yml");
    }

    private static void addSpawn(String name, MobSpawn... spawns) {
        layerSpawns.put(name, new SpawnRegion(name, spawns));
    }

    public static Map<String, SpawnRegion> getLayerSpawns() {
        return layerSpawns;
    }
}