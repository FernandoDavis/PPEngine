package Objects

import Entities.Pichon
import mainPPE._

class Pichon_Laser extends Projectile {

  this.speed = 5
  this.setWidth(32)
  this.setHeight(12)
  val spriteSheet: SpriteSheet = new SpriteSheet(loadImage("IMAGES/projectiles/pichon1.png"),2,2,32,32,3)
  this.animation=new Animation
  animation.setAnimation(spriteSheet.getArrayListOfImages)
  this.animated=true
  val sound: Sound = new Sound("Sounds/shoot1.wav")
  sound.setSoundRadius(1000)
  sound.setVolumeSuppression(0.7f)

  def this(position: Vector2D, direction: Vector2D) {
    this()
    this.setCenter(position)
    this.setDirection(direction)
    sound.play(this.getCenter)
  }

  def this(position: Vector2D, from: Vector2D, to: Vector2D) {
    this()
    this.setCenter(position)
    this.setDirection(from, to)
    sound.play(this.getCenter)
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
