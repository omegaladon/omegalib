package me.omega.omegalib.data.codec;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocationSerializer {

    private static final Pattern LOCATION_PATTERN = Pattern.compile("Location\\{world=CraftWorld\\{name=(.+?)},x=(.+?),y=(.+?),z=(.+?),pitch=(.+?),yaw=(.+?)}");

    public static Location deserialize(String location) {
        Matcher matcher = LOCATION_PATTERN.matcher(location);

        if (matcher.find()) {
            String worldName = matcher.group(1);
            double x = Double.parseDouble(matcher.group(2));
            double y = Double.parseDouble(matcher.group(3));
            double z = Double.parseDouble(matcher.group(4));
            float pitch = Float.parseFloat(matcher.group(5));
            float yaw = Float.parseFloat(matcher.group(6));
            World world = Bukkit.getWorld(worldName);
            return new Location(world, x, y, z, yaw, pitch);
        }

        return null;
    }

    public static String serialize(Location location) {
        return location.toString();
    }

}
