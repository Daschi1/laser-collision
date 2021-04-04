package de.daschi.lasercollision.util;

import org.bukkit.Location;
import org.bukkit.Particle;

public class ParticleUtil {

  public static void spawnParticle(
      final Particle particle,
      final Location location,
      final int count,
      final double offsetX,
      final double offsetY,
      final double offsetZ,
      final double extra,
      final Object data,
      final boolean force) {
    location
        .getWorld()
        .spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, extra, data, force);
  }
}
