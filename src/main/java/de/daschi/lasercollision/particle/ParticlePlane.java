package de.daschi.lasercollision.particle;

import de.daschi.lasercollision.util.ParticleUtil;
import de.daschi.lasercollision.util.Plane;
import org.bukkit.Particle;
import org.bukkit.World;

public class ParticlePlane {

  private final Plane plane;

  public ParticlePlane(final Plane plane) {
    this.plane = plane;
  }

  public void particlePlane(
      final World world, final double offset, final double step, final double length) {
    for (double a = offset; a < length; a += step) {
      for (double b = offset; b < length; b += step) {
        ParticleUtil.spawnParticle(
            Particle.FLAME,
            this.plane.getPosition(offset + a, offset + b).toLocation(world),
            1,
            0,
            0,
            0,
            0,
            null,
            true);
      }
    }
  }

  /* public void particleBeam(
      final World world, final double offset, final double length, final long delay) {
    final AtomicDouble atomicDouble = new AtomicDouble(offset);
    final Timer timer = new Timer();
    timer.scheduleAtFixedRate(
        new TimerTask() {
          @Override
          public void run() {
            ParticlePlane.this.particleLine(world, atomicDouble.get(), 1, atomicDouble.addAndGet(1));
            if (atomicDouble.get() >= length) {
              timer.cancel();
            }
          }
        },
        0,
        delay);
  }*/

  public Plane getPlane() {
    return this.plane;
  }
}
