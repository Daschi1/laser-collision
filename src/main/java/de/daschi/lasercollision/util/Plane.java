package de.daschi.lasercollision.util;

import org.bukkit.util.Vector;

public class Plane {

  private Vector position;
  private Vector directionA;
  private Vector directionB;

  public Plane(final Vector position, final Vector directionA, final Vector directionB) {
    this.position = position;
    this.directionA = directionA;
    this.directionB = directionB;
  }

  public Vector getPosition(final double offsetA, final double offsetB) {
    return this.position
        .clone()
        .add(this.directionA.clone().multiply(offsetA))
        .add(this.directionB.clone().multiply(offsetB));
  }

  public Vector getNormalVector() {
    return new Vector(
        this.directionA.getY() * this.directionB.getZ()
            - this.directionA.getZ() * this.directionB.getY(),
        this.directionA.getZ() * this.directionB.getX()
            - this.directionA.getX() * this.directionB.getZ(),
        this.directionA.getX() * this.directionB.getY()
            - this.directionA.getY() * this.directionB.getX());
  }

  public double getAngleWithLine(final Line line) {
    return Math.asin(
        this.getNormalVector().multiply(line.getDirection()).length()
            / (this.getNormalVector().length() * line.getDirection().length()));
  }

  public Vector getPosition() {
    return this.position;
  }

  public void setPosition(final Vector position) {
    this.position = position;
  }

  public Vector getDirectionA() {
    return this.directionA;
  }

  public void setDirectionA(final Vector directionA) {
    this.directionA = directionA;
  }

  public Vector getDirectionB() {
    return this.directionB;
  }

  public void setDirectionB(final Vector directionB) {
    this.directionB = directionB;
  }
}
