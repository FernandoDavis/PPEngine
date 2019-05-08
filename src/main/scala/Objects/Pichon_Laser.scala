package Objects

import Entities.Pichon
import mainPPE.{Main, Obj, Vector2D}

class Pichon_Laser extends Projectile {

  this.speed = 5
  this.setWidth(20)
  this.setHeight(20)

  def this(position: Vector2D, direction: Vector2D) {
    this()
    this.setPosition(position)
    this.setDirection(direction)
  }

  def this(position: Vector2D, from: Vector2D, to: Vector2D) {
    this()
    this.setPosition(position)
    this.setDirection(from, to)
  }

  override def collisionFiltering(obj: Obj): Boolean = {
    obj.isInstanceOf[Pichon_Laser] || super.collisionFiltering(obj)
  }

  override def collision(obj: Obj): Unit = {
    if (obj != this && !collisionFiltering(obj)) {
      destroy()
    }
  }

}
