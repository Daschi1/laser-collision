package de.daschi.lasercollision;

import de.daschi.lasercollision.util.Line;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class LaserCollision extends JavaPlugin implements Listener {

  @Override
  public void onEnable() {
    Bukkit.getPluginManager().registerEvents(this, this);
    Laser.startCollisionDetection(this); // important
  }

  @Override
  public void onDisable() {}

  @EventHandler
  public void onPlayerInteract(final PlayerInteractEvent playerInteractEvent) {
    if ((playerInteractEvent.getAction().equals(Action.RIGHT_CLICK_AIR)
            || playerInteractEvent.getAction().equals(Action.RIGHT_CLICK_BLOCK))
        && playerInteractEvent.getMaterial().equals(Material.STICK)) {
      final Player player = playerInteractEvent.getPlayer();
      final Laser laser =
          new Laser(
              new Line(player.getEyeLocation().toVector(), player.getEyeLocation().getDirection()),
              player.getWorld(),
              100);
      laser.setEntityCollision(System.out::println);
      laser.setBlockCollision(System.out::println);
      laser.fire();
      /*final ParticleLine particleLine =
          new ParticleLine(
              new Line(player.getEyeLocation().toVector(), player.getEyeLocation().getDirection()));
      //      particleLine.particleBeam(player.getWorld(), 1, 100, 50);
      Bukkit.getScheduler()
          .runTaskTimerAsynchronously(
              this,
              () -> {
                particleLine.particleLine(player.getWorld(), -5, 1, 10);
              },
              0,
              20);*/
    } /* else if (playerInteractEvent.getAction().equals(Action.RIGHT_CLICK_BLOCK)
          && playerInteractEvent.getMaterial().equals(Material.STICK)) {
        final Location location =
            Objects.requireNonNull(playerInteractEvent.getClickedBlock()).getLocation();
        final List<ParticlePlane> particlePlanes = new ArrayList<>();
        particlePlanes.add(
            new ParticlePlane(
                new Plane(location.toVector(), new Vector(0, 1, 0), new Vector(0, 0, 1))));
        particlePlanes.add(
            new ParticlePlane(
                new Plane(location.toVector(), new Vector(0, 1, 0), new Vector(1, 0, 0))));
        particlePlanes.add(
            new ParticlePlane(
                new Plane(location.toVector(), new Vector(1, 0, 0), new Vector(0, 0, 1))));
        particlePlanes.add(
            new ParticlePlane(
                new Plane(
                    location.clone().add(1, 1, 1).toVector(),
                    new Vector(0, -1, 0),
                    new Vector(0, 0, -1))));
        particlePlanes.add(
            new ParticlePlane(
                new Plane(
                    location.clone().add(1, 1, 1).toVector(),
                    new Vector(0, -1, 0),
                    new Vector(-1, 0, 0))));
        particlePlanes.add(
            new ParticlePlane(
                new Plane(
                    location.clone().add(1, 1, 1).toVector(),
                    new Vector(-1, 0, 0),
                    new Vector(0, 0, -1))));

        Bukkit.getScheduler()
            .runTaskTimerAsynchronously(
                this,
                () -> {
                  particlePlanes.forEach(
                      particlePlane -> particlePlane.particlePlane(location.getWorld(), -5, 1, 10));
                },
                0,
                20);
      }*/
  }
}
