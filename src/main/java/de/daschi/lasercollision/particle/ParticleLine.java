package de.daschi.lasercollision.particle;

import com.google.common.util.concurrent.AtomicDouble;
import de.daschi.lasercollision.util.Line;
import de.daschi.lasercollision.util.ParticleUtil;
import java.util.Timer;
import java.util.TimerTask;
import org.bukkit.Particle;
import org.bukkit.World;

public class ParticleLine {

  private final Line line;

  public ParticleLine(final Line line) {
    this.line = line;
  }

  public void particleLine(
      final World world, final double offset, final double step, final double length) {
    for (double i = offset; i < length; i += step) {
      ParticleUtil.spawnParticle(
          Particle.FLAME,
          this.line.getPosition(offset + i).toLocation(world),
          1,
          0,
          0,
          0,
          0,
          null,
          true);
    }
  }

  public void particleBeam(
      final World world, final double offset, final double length, final long delay) {
    final AtomicDouble atomicDouble = new AtomicDouble(offset);
    final Timer timer = new Timer();
    timer.scheduleAtFixedRate(
        new TimerTask() {
          @Override
          public void run() {
            ParticleLine.this.particleLine(world, atomicDouble.get(), 1, atomicDouble.addAndGet(1));
            if (atomicDouble.get() >= length) {
              timer.cancel();
            }
          }
        },
        0,
        delay);
  }

  public Line getLine() {
    return this.line;
  }
}
