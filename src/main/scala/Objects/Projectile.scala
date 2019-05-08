package Objects

import mainPPE.{Obj, Vector2D}

abstract class Projectile extends Obj {
  this.gravity = 0
  protected var direction: Vector2D = new Vector2D(0, 0)
  protected var owner: Obj = null
  protected var lifeTime: Double = 100

  def getOwner: Obj = owner

  override def reset(): Unit = {
    destroy()
  }

  def setOwner(obj: Obj): Projectile = {
    this.owner = obj
    this
  }

  def setDirection(x: Double, y: Double): Unit = {
    this.direction = new Vector2D(x, y)
  }

  def setDirection(unitVector: Vector2D): Unit = {
    this.direction = unitVector
  }

  def setDirection(from: Vector2D, to: Vector2D): Unit = {
    this.direction = from.UnitVector(to)
  }

  override def tick(): Unit = {
    super.tick()
    this.velocity = direction * speed
    if (lifeTime > 0) {
      lifeTime -= 1
    }
    else {
      println("S A S")
      destroy()
    }
  }

  override def collisionFiltering(obj: Obj): Boolean = {
    this.owner == obj
  }

  override def collision(obj: Obj)

}
