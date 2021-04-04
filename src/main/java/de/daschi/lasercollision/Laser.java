package de.daschi.lasercollision;

import de.daschi.lasercollision.util.Line;
import de.daschi.lasercollision.util.ParticleUtil;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Consumer;
import org.bukkit.util.Vector;

public class Laser {
  private static final List<Laser> lasers = new ArrayList<>();
  private static final List<Laser> lasersToRemove = new ArrayList<>();
  private static final List<Laser> lasersToAdd = new ArrayList<>();
  private static Plugin plugin;

  private final Line line;
  private final World world;
  private final double liveTime;
  private double offset;
  private Consumer<Entity> entityCollision;
  private Consumer<Block> blockCollision;

  public Laser(final Line line, final World world, final double liveTime) {
    this.line = line;
    this.world = world;
    this.liveTime = liveTime;
  }

  public static void startCollisionDetection(final Plugin plugin) {
    Laser.plugin = plugin;
    Bukkit.getScheduler()
        .runTaskTimerAsynchronously(
            plugin,
            () -> {
              Laser.lasers.removeAll(Laser.lasersToRemove);
              Laser.lasersToRemove.clear();
              Laser.lasers.addAll(Laser.lasersToAdd);
              Laser.lasersToAdd.clear();
              Laser.lasers.forEach(Laser::handle);
            },
            0,
            1);
  }

  private void handle() {
    this.move();

    this.collide();

    this.spawnParticle();

    if (this.offset > this.liveTime) {
      this.discard();
    }
  }

  private void move() {
    this.offset++;
  }

  private void collide() {
    Bukkit.getScheduler()
        .runTask(
            Laser.plugin,
            () ->
                this.getLocation()
                    .getNearbyEntities(1, 1, 1)
                    .forEach(this.entityCollision::accept));

    if (!this.getLocation().getBlock().getType().isAir()) {
      this.blockCollision.accept(this.getLocation().getBlock());
      System.out.println(this.getLocation().getBlock().getLocation());
      System.out.println(this.getLocation(this.offset - 1).getBlock().getLocation());
      System.out.println(
          this.getLocation().getBlockX() - this.getLocation(this.offset - 1).getBlockX());
      System.out.println(
          this.getLocation().getBlockY() - this.getLocation(this.offset - 1).getBlockY());
      System.out.println(
          this.getLocation().getBlockZ() - this.getLocation(this.offset - 1).getBlockZ());

      Vector reflection =
          new Vector(
              -this.line.getDirection().getX(),
              -this.line.getDirection().getY(),
              -this.line.getDirection().getZ());
      if (this.getLocation().getBlockX() - this.getLocation(this.offset - 1).getBlockX() == 1
          && this.getLocation().getBlockY() - this.getLocation(this.offset - 1).getBlockY() == 1
          && this.getLocation().getBlockZ() - this.getLocation(this.offset - 1).getBlockZ()
              == 1) { // bottom_north_west
        if (!this.getLocation().add(-1, 0, 0).getBlock().getType().isAir()
            && !this.getLocation().add(0, 0, -1).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  this.line.getDirection().getX(),
                  -this.line.getDirection().getY(),
                  this.line.getDirection().getZ()); // bottom
        } else if (!this.getLocation().add(-1, 0, 0).getBlock().getType().isAir()
            && !this.getLocation().add(0, -1, 0).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  this.line.getDirection().getX(),
                  this.line.getDirection().getY(),
                  -this.line.getDirection().getZ()); // north
        } else if (!this.getLocation().add(0, -1, 0).getBlock().getType().isAir()
            && !this.getLocation().add(0, 0, -1).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  -this.line.getDirection().getX(),
                  this.line.getDirection().getY(),
                  this.line.getDirection().getZ()); // west
        }
      } else if (this.getLocation().getBlockX() - this.getLocation(this.offset - 1).getBlockX()
              == -1
          && this.getLocation().getBlockY() - this.getLocation(this.offset - 1).getBlockY() == 1
          && this.getLocation().getBlockZ() - this.getLocation(this.offset - 1).getBlockZ()
              == 1) { // bottom_north_east
        if (!this.getLocation().add(1, 0, 0).getBlock().getType().isAir()
            && !this.getLocation().add(0, 0, -1).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  this.line.getDirection().getX(),
                  -this.line.getDirection().getY(),
                  this.line.getDirection().getZ()); // bottom
        } else if (!this.getLocation().add(1, 0, 0).getBlock().getType().isAir()
            && !this.getLocation().add(0, -1, 0).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  this.line.getDirection().getX(),
                  this.line.getDirection().getY(),
                  -this.line.getDirection().getZ()); // north
        } else if (!this.getLocation().add(0, -1, 0).getBlock().getType().isAir()
            && !this.getLocation().add(0, 0, -1).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  -this.line.getDirection().getX(),
                  this.line.getDirection().getY(),
                  this.line.getDirection().getZ()); // east
        }
      } else if (this.getLocation().getBlockX() - this.getLocation(this.offset - 1).getBlockX()
              == -1
          && this.getLocation().getBlockY() - this.getLocation(this.offset - 1).getBlockY() == 1
          && this.getLocation().getBlockZ() - this.getLocation(this.offset - 1).getBlockZ()
              == -1) { // bottom_south_east
        if (!this.getLocation().add(1, 0, 0).getBlock().getType().isAir()
            && !this.getLocation().add(0, 0, 1).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  this.line.getDirection().getX(),
                  -this.line.getDirection().getY(),
                  this.line.getDirection().getZ()); // bottom
        } else if (!this.getLocation().add(1, 0, 0).getBlock().getType().isAir()
            && !this.getLocation().add(0, -1, 0).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  this.line.getDirection().getX(),
                  this.line.getDirection().getY(),
                  -this.line.getDirection().getZ()); // south
        } else if (!this.getLocation().add(0, -1, 0).getBlock().getType().isAir()
            && !this.getLocation().add(0, 0, 1).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  -this.line.getDirection().getX(),
                  this.line.getDirection().getY(),
                  this.line.getDirection().getZ()); // east
        }
      } else if (this.getLocation().getBlockX() - this.getLocation(this.offset - 1).getBlockX() == 1
          && this.getLocation().getBlockY() - this.getLocation(this.offset - 1).getBlockY() == 1
          && this.getLocation().getBlockZ() - this.getLocation(this.offset - 1).getBlockZ()
              == -1) { // bottom_south_west
        if (!this.getLocation().add(-1, 0, 0).getBlock().getType().isAir()
            && !this.getLocation().add(0, 0, 1).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  this.line.getDirection().getX(),
                  -this.line.getDirection().getY(),
                  this.line.getDirection().getZ()); // bottom
        } else if (!this.getLocation().add(-1, 0, 0).getBlock().getType().isAir()
            && !this.getLocation().add(0, -1, 0).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  this.line.getDirection().getX(),
                  this.line.getDirection().getY(),
                  -this.line.getDirection().getZ()); // south
        } else if (!this.getLocation().add(0, -1, 0).getBlock().getType().isAir()
            && !this.getLocation().add(0, 0, 1).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  -this.line.getDirection().getX(),
                  this.line.getDirection().getY(),
                  this.line.getDirection().getZ()); // west
        }
      } else if (this.getLocation().getBlockX() - this.getLocation(this.offset - 1).getBlockX() == 1
          && this.getLocation().getBlockY() - this.getLocation(this.offset - 1).getBlockY() == -1
          && this.getLocation().getBlockZ() - this.getLocation(this.offset - 1).getBlockZ()
              == 1) { // top_north_west
        if (!this.getLocation().add(-1, 0, 0).getBlock().getType().isAir()
            && !this.getLocation().add(0, 0, -1).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  this.line.getDirection().getX(),
                  -this.line.getDirection().getY(),
                  this.line.getDirection().getZ()); // top
        } else if (!this.getLocation().add(-1, 0, 0).getBlock().getType().isAir()
            && !this.getLocation().add(0, 1, 0).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  this.line.getDirection().getX(),
                  this.line.getDirection().getY(),
                  -this.line.getDirection().getZ()); // north
        } else if (!this.getLocation().add(0, 1, 0).getBlock().getType().isAir()
            && !this.getLocation().add(0, 0, -1).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  -this.line.getDirection().getX(),
                  this.line.getDirection().getY(),
                  this.line.getDirection().getZ()); // west
        }
      } else if (this.getLocation().getBlockX() - this.getLocation(this.offset - 1).getBlockX()
              == -1
          && this.getLocation().getBlockY() - this.getLocation(this.offset - 1).getBlockY() == -1
          && this.getLocation().getBlockZ() - this.getLocation(this.offset - 1).getBlockZ()
              == 1) { // top_north_east
        if (!this.getLocation().add(1, 0, 0).getBlock().getType().isAir()
            && !this.getLocation().add(0, 0, -1).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  this.line.getDirection().getX(),
                  -this.line.getDirection().getY(),
                  this.line.getDirection().getZ()); // top
        } else if (!this.getLocation().add(1, 0, 0).getBlock().getType().isAir()
            && !this.getLocation().add(0, 1, 0).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  this.line.getDirection().getX(),
                  this.line.getDirection().getY(),
                  -this.line.getDirection().getZ()); // north
        } else if (!this.getLocation().add(0, 1, 0).getBlock().getType().isAir()
            && !this.getLocation().add(0, 0, -1).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  -this.line.getDirection().getX(),
                  this.line.getDirection().getY(),
                  this.line.getDirection().getZ()); // east
        }
      } else if (this.getLocation().getBlockX() - this.getLocation(this.offset - 1).getBlockX()
              == -1
          && this.getLocation().getBlockY() - this.getLocation(this.offset - 1).getBlockY() == -1
          && this.getLocation().getBlockZ() - this.getLocation(this.offset - 1).getBlockZ()
              == -1) { // top_south_east
        if (!this.getLocation().add(1, 0, 0).getBlock().getType().isAir()
            && !this.getLocation().add(0, 0, 1).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  this.line.getDirection().getX(),
                  -this.line.getDirection().getY(),
                  this.line.getDirection().getZ()); // top
        } else if (!this.getLocation().add(1, 0, 0).getBlock().getType().isAir()
            && !this.getLocation().add(0, 1, 0).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  this.line.getDirection().getX(),
                  this.line.getDirection().getY(),
                  -this.line.getDirection().getZ()); // south
        } else if (!this.getLocation().add(0, 1, 0).getBlock().getType().isAir()
            && !this.getLocation().add(0, 0, 1).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  -this.line.getDirection().getX(),
                  this.line.getDirection().getY(),
                  this.line.getDirection().getZ()); // east
        }
      } else if (this.getLocation().getBlockX() - this.getLocation(this.offset - 1).getBlockX() == 1
          && this.getLocation().getBlockY() - this.getLocation(this.offset - 1).getBlockY() == -1
          && this.getLocation().getBlockZ() - this.getLocation(this.offset - 1).getBlockZ()
              == -1) { // top_south_west
        if (!this.getLocation().add(-1, 0, 0).getBlock().getType().isAir()
            && !this.getLocation().add(0, 0, 1).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  this.line.getDirection().getX(),
                  -this.line.getDirection().getY(),
                  this.line.getDirection().getZ()); // top
        } else if (!this.getLocation().add(-1, 0, 0).getBlock().getType().isAir()
            && !this.getLocation().add(0, 1, 0).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  this.line.getDirection().getX(),
                  this.line.getDirection().getY(),
                  -this.line.getDirection().getZ()); // south
        } else if (!this.getLocation().add(0, 1, 0).getBlock().getType().isAir()
            && !this.getLocation().add(0, 0, 1).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  -this.line.getDirection().getX(),
                  this.line.getDirection().getY(),
                  this.line.getDirection().getZ()); // west
        }
      } else if (this.getLocation().getBlockX() - this.getLocation(this.offset - 1).getBlockX() == 1
          && this.getLocation().getBlockY() - this.getLocation(this.offset - 1).getBlockY()
              == 1) { // bottom_west
        if (!this.getLocation().add(-1, 0, 0).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  this.line.getDirection().getX(),
                  -this.line.getDirection().getY(),
                  this.line.getDirection().getZ()); // bottom
        } else if (!this.getLocation().add(0, -1, 0).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  -this.line.getDirection().getX(),
                  this.line.getDirection().getY(),
                  this.line.getDirection().getZ()); // west
        }
      } else if (this.getLocation().getBlockX() - this.getLocation(this.offset - 1).getBlockX()
              == -1
          && this.getLocation().getBlockY() - this.getLocation(this.offset - 1).getBlockY()
              == 1) { // bottom_east
        if (!this.getLocation().add(1, 0, 0).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  this.line.getDirection().getX(),
                  -this.line.getDirection().getY(),
                  this.line.getDirection().getZ()); // bottom
        } else if (!this.getLocation().add(0, -1, -0).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  -this.line.getDirection().getX(),
                  this.line.getDirection().getY(),
                  this.line.getDirection().getZ()); // east
        }
      } else if (this.getLocation().getBlockX() - this.getLocation(this.offset - 1).getBlockX()
              == -1
          && this.getLocation().getBlockY() - this.getLocation(this.offset - 1).getBlockY()
              == -1) { // top_east
        if (!this.getLocation().add(1, 0, 0).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  this.line.getDirection().getX(),
                  -this.line.getDirection().getY(),
                  this.line.getDirection().getZ()); // top
        } else if (!this.getLocation().add(0, 1, 0).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  -this.line.getDirection().getX(),
                  this.line.getDirection().getY(),
                  this.line.getDirection().getZ()); // east
        }
      } else if (this.getLocation().getBlockX() - this.getLocation(this.offset - 1).getBlockX() == 1
          && this.getLocation().getBlockY() - this.getLocation(this.offset - 1).getBlockY()
              == -1) { // top_west
        if (!this.getLocation().add(-1, 0, 0).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  this.line.getDirection().getX(),
                  -this.line.getDirection().getY(),
                  this.line.getDirection().getZ()); // top
        } else if (!this.getLocation().add(0, 1, 0).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  -this.line.getDirection().getX(),
                  this.line.getDirection().getY(),
                  this.line.getDirection().getZ()); // west
        }
      } else if (this.getLocation().getBlockX() - this.getLocation(this.offset - 1).getBlockX() == 1
          && this.getLocation().getBlockZ() - this.getLocation(this.offset - 1).getBlockZ()
              == 1) { // north_west
        if (!this.getLocation().add(-1, 0, 0).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  this.line.getDirection().getX(),
                  this.line.getDirection().getY(),
                  -this.line.getDirection().getZ()); // north
        } else if (!this.getLocation().add(0, 0, -1).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  -this.line.getDirection().getX(),
                  this.line.getDirection().getY(),
                  this.line.getDirection().getZ()); // west
        }
      } else if (this.getLocation().getBlockX() - this.getLocation(this.offset - 1).getBlockX()
              == -1
          && this.getLocation().getBlockZ() - this.getLocation(this.offset - 1).getBlockZ()
              == 1) { // north_east
        if (!this.getLocation().add(1, 0, 0).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  this.line.getDirection().getX(),
                  this.line.getDirection().getY(),
                  -this.line.getDirection().getZ()); // north
        } else if (!this.getLocation().add(0, 0, -1).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  -this.line.getDirection().getX(),
                  this.line.getDirection().getY(),
                  this.line.getDirection().getZ()); // east
        }
      } else if (this.getLocation().getBlockX() - this.getLocation(this.offset - 1).getBlockX()
              == -1
          && this.getLocation().getBlockZ() - this.getLocation(this.offset - 1).getBlockZ()
              == -1) { // south_east
        if (!this.getLocation().add(1, 0, 0).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  this.line.getDirection().getX(),
                  this.line.getDirection().getY(),
                  -this.line.getDirection().getZ()); // south
        } else if (!this.getLocation().add(0, 0, 1).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  -this.line.getDirection().getX(),
                  this.line.getDirection().getY(),
                  this.line.getDirection().getZ()); // east
        }
      } else if (this.getLocation().getBlockX() - this.getLocation(this.offset - 1).getBlockX() == 1
          && this.getLocation().getBlockZ() - this.getLocation(this.offset - 1).getBlockZ()
              == -1) { // south_west
        if (!this.getLocation().add(-1, 0, 0).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  this.line.getDirection().getX(),
                  this.line.getDirection().getY(),
                  -this.line.getDirection().getZ()); // south
        } else if (!this.getLocation().add(0, 0, 1).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  -this.line.getDirection().getX(),
                  this.line.getDirection().getY(),
                  this.line.getDirection().getZ()); // west
        }
      } else if (this.getLocation().getBlockY() - this.getLocation(this.offset - 1).getBlockY() == 1
          && this.getLocation().getBlockZ() - this.getLocation(this.offset - 1).getBlockZ()
              == 1) { // north_bottom
        if (!this.getLocation().add(0, -1, 0).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  this.line.getDirection().getX(),
                  this.line.getDirection().getY(),
                  -this.line.getDirection().getZ()); // north
        } else if (!this.getLocation().add(0, 0, -1).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  this.line.getDirection().getX(),
                  -this.line.getDirection().getY(),
                  this.line.getDirection().getZ()); // bottom
        }
      } else if (this.getLocation().getBlockY() - this.getLocation(this.offset - 1).getBlockY()
              == -1
          && this.getLocation().getBlockZ() - this.getLocation(this.offset - 1).getBlockZ()
              == 1) { // north_top
        if (!this.getLocation().add(0, 1, 0).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  this.line.getDirection().getX(),
                  this.line.getDirection().getY(),
                  -this.line.getDirection().getZ()); // north
        } else if (!this.getLocation().add(0, 0, -1).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  this.line.getDirection().getX(),
                  -this.line.getDirection().getY(),
                  this.line.getDirection().getZ()); // top
        }
      } else if (this.getLocation().getBlockY() - this.getLocation(this.offset - 1).getBlockY()
              == -1
          && this.getLocation().getBlockZ() - this.getLocation(this.offset - 1).getBlockZ()
              == -1) { // south_top
        if (!this.getLocation().add(0, 1, 0).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  this.line.getDirection().getX(),
                  this.line.getDirection().getY(),
                  -this.line.getDirection().getZ()); // south
        } else if (!this.getLocation().add(0, 0, 1).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  this.line.getDirection().getX(),
                  -this.line.getDirection().getY(),
                  this.line.getDirection().getZ()); // top
        }
      } else if (this.getLocation().getBlockY() - this.getLocation(this.offset - 1).getBlockY() == 1
          && this.getLocation().getBlockZ() - this.getLocation(this.offset - 1).getBlockZ()
              == -1) { // south_bottom
        if (!this.getLocation().add(0, -1, 0).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  this.line.getDirection().getX(),
                  this.line.getDirection().getY(),
                  -this.line.getDirection().getZ()); // south
        } else if (!this.getLocation().add(0, 0, 1).getBlock().getType().isAir()) {
          reflection =
              new Vector(
                  this.line.getDirection().getX(),
                  -this.line.getDirection().getY(),
                  this.line.getDirection().getZ()); // bottom
        }
      } else if (this.getLocation().getBlockX() - this.getLocation(this.offset - 1).getBlockX()
          != 0) { // west + east
        reflection =
            new Vector(
                -this.line.getDirection().getX(),
                this.line.getDirection().getY(),
                this.line.getDirection().getZ()); // west + east
      } else if (this.getLocation().getBlockY() - this.getLocation(this.offset - 1).getBlockY()
          != 0) { // top + bottom
        reflection =
            new Vector(
                this.line.getDirection().getX(),
                -this.line.getDirection().getY(),
                this.line.getDirection().getZ()); // top + bottom
      } else if (this.getLocation().getBlockZ() - this.getLocation(this.offset - 1).getBlockZ()
          != 0) { // north + south
        reflection =
            new Vector(
                this.line.getDirection().getX(),
                this.line.getDirection().getY(),
                -this.line.getDirection().getZ()); // north + south
      }

      final Laser reflectedLaser =
          new Laser(
              new Line(this.getLocation().toVector(), reflection),
              this.world,
              this.liveTime - this.offset);
      reflectedLaser.setEntityCollision(this.entityCollision);
      reflectedLaser.setBlockCollision(this.blockCollision);
      reflectedLaser.fire();
      this.discard();
    }
  }

  public void spawnParticle() {
    ParticleUtil.spawnParticle(Particle.FLAME, this.getLocation(), 1, 0, 0, 0, 0, null, true);
  }

  public void fire() {
    Laser.lasersToAdd.add(this);
  }

  public void discard() {
    Laser.lasersToRemove.add(this);
  }

  private Location getLocation(final double offset) {
    return this.line.getPosition(offset).toLocation(this.world);
  }

  private Location getLocation() {
    return this.getLocation(this.offset);
  }

  public World getWorld() {
    return this.world;
  }

  public double getLiveTime() {
    return this.liveTime;
  }

  public void setEntityCollision(final Consumer<Entity> entityCollision) {
    this.entityCollision = entityCollision;
  }

  public void setBlockCollision(final Consumer<Block> blockCollision) {
    this.blockCollision = blockCollision;
  }
}
