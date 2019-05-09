package Entities

import mainPPE._


class Crab1(x: Int, y: Int, w: Int, h: Int) extends Entity(x: Int, y: Int, w: Int, h: Int) with Enemy {
  def this() = this(0, 0, 36, 25)

  def this(v: Vector2D) = this(v.getX.toInt, v.getY.toInt, 36, 25)

  def this(x: Integer, y: Integer) = this(x, y, 54, 38)

  val spriteSheet: SpriteSheet = new SpriteSheet(loadImage("IMAGES/crab1/sheet.png"), 3, 2, 36, 25, 4)
  //this.animation=new Animation
  this.animated = true
  //  animation.setMovingAnimation(spriteSheet.getArrayListOfImages)
  //  animation.setStandingAnimation(loadImage("IMAGES/crab1/still.png"))
  //  animation.setJumpingAnimation(loadImage("IMAGES/crab1/still.png"))
  entityAnimation = new EntityAnimation
  entityAnimation.setMovingAnimation(spriteSheet.getArrayListOfImages)
  entityAnimation.setStandingAnimation(loadImage("IMAGES/crab1/still.png"))
  entityAnimation.setJumpingAnimation(loadImage("IMAGES/crab1/still.png"))

  //this.canTouch=true
  //this.canBeTouched=true
  this.setSpeed(3 + random.nextInt(2))
  this.setPersonality(Behaviour.attackPlayer(20) + (Behaviour.followPlayer ~ Behaviour.moveBackAndForthH(200 + random.nextInt(200), this.startPosition)) + Behaviour.jump)
  this.anchored = false
  this.imageOffset = new Vector2D(0, 2)
 // animation.setFrameSpeed(0.1 * this.speed)
  entityAnimation.setFrameSpeed(0.1 * this.speed)

  val sound: Sound = new Sound("Sounds/step1.wav")
  sound.setSoundRadius(200)
  entitySounds.movingSounds.add(sound)
  entitySounds.setSoundDelays(100)
  val delay: Int = 100
  var time: Long = 0


  override def tick(): Unit = {
    super.tick()
//    if (System.currentTimeMillis() - time >= delay && ground != null && math.abs(this.getVelocity.getX) > 0.5) {
//      time = System.currentTimeMillis()
//      sound.play(this.getPosition)
//    }
  }

  override def death(): Unit = {
    Main.getGame.currentLevel.getObjects.remove(this)
  }

  override def collisionFiltering(obj: Obj): Boolean = {
    obj.isInstanceOf[Enemy]
  }
}
