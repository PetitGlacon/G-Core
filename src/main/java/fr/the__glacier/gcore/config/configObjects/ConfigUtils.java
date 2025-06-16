package fr.the__glacier.gcore.config.configObjects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class ConfigUtils {
    public static class LocationConfig{
        public String world;
        public int x;
        public int y;
        public int z;
        public Float pitch;
        public Float yaw;
        public LocationConfig(){}
        public LocationConfig(String world, int x, int y, int z){
            this.world = world;
            this.x = x;
            this.y = y;
            this.z = z;
        }
        public LocationConfig(String world, int x, int y, int z, float pitch, float yaw){
            this.world = world;
            this.x = x;
            this.y = y;
            this.z = z;
            this.pitch = pitch;
            this.yaw = yaw;
        }
        @JsonIgnore
        public Location getLocation(){
            if (pitch == null && yaw == null){
                return new Location(Bukkit.getWorld(world), x, y, z);
            } else {
                return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
            }
        }
    }
}
