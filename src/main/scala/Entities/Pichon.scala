package Entities

import mainPPE._


class Pichon(x: Int, y: Int, w: Int, h: Int) extends Entity(x: Int, y: Int, w: Int, h: Int) with Enemy {
  def this() = this(0, 0, 141, 141)

  def this(v: Vector2D) = this(v.getX.toInt, v.getY.toInt, 141, 141)

  def this(x: Integer, y: Integer) = this(x, y, 141, 141)

  val spriteSheet: SpriteSheet = new SpriteSheet(loadImage("IMAGES/pichon/sheet.png"), 3, 1, 141, 141, 3)
  this.animation = new Animation
  this.animated = true
  animation.setMovingAnimation(spriteSheet.getArrayListOfImages)
  animation.setStandingAnimation(spriteSheet.getArrayListOfImages)
  animation.setJumpingAnimation(spriteSheet.getArrayListOfImages)

  this.canTouch = true
  this.canBeTouched=false
  this.setSpeed(5 + random.nextInt(2))
  this.imageOffset = new Vector2D(-8, -16)
  this.setPersonality(Behaviour.attackPlayer(20) + Behaviour.randomFly)
  this.anchored = false
  this.gravity = 0
  this.hitBoxOffset = new Vector2D(-8,5)
  this.setHitBoxDimensions(28,20)
  this.setMaskDimensions(62,42)
  animation.setFrameSpeed(0.2 * this.speed)


  override def tick(): Unit = {
    super.tick()
    this.imageRotation = math.sin(System.currentTimeMillis() / 400.0) * math.toRadians(20)
  }

  override def death(): Unit = {
    Main.getGame.currentLevel.getObjects.remove(this)
  }

  override def collisionFiltering(obj: Obj): Boolean = {
    obj.isInstanceOf[Enemy]
  }
}
