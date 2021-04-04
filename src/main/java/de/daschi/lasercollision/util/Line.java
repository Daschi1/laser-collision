package de.daschi.lasercollision.util;

import org.bukkit.util.Vector;

public class Line {

  private Vector location;
  private Vector direction;

  public Line(final Vector location, final Vector direction) {
    this.location = location;
    this.direction = direction;
  }

  public Vector getPosition(final double offset) {
    return this.location.clone().add(this.direction.clone().multiply(offset));
  }

  public Vector getLocation() {
    return this.location;
  }

  public void setLocation(final Vector location) {
    this.location = location;
  }

  public Vector getDirection() {
    return this.direction;
  }

  public void setDirection(final Vector direction) {
    this.direction = direction;
  }
}
