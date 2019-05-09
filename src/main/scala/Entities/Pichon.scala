package Entities

import Objects.Pichon_Laser
import mainPPE._


class Pichon(x: Int, y: Int, w: Int, h: Int) extends Entity(x: Int, y: Int, w: Int, h: Int) with Enemy {
  def this() = this(0, 0, 141, 141)

  def this(v: Vector2D) = this(v.getX.toInt, v.getY.toInt, 141, 141)

  def this(x: Integer, y: Integer) = this(x, y, 62, 42)

  val spriteSheet: SpriteSheet = new SpriteSheet(loadImage("IMAGES/pichon/sheet.png"), 3, 1, 141, 141, 3)
  //  this.animation = new Animation
  this.animated = true
  //  animation.setMovingAnimation(spriteSheet.getArrayListOfImages)
  //  animation.setStandingAnimation(spriteSheet.getArrayListOfImages)
  //  animation.setJumpingAnimation(spriteSheet.getArrayListOfImages)
  this.entityAnimation = new EntityAnimation
  entityAnimation.setMovingAnimation(spriteSheet.getArrayListOfImages)
  entityAnimation.setStandingAnimation(spriteSheet.getArrayListOfImages)
  entityAnimation.setJumpingAnimation(spriteSheet.getArrayListOfImages)

  this.canTouch = true
  this.canBeTouched = false
  this.setSpeed(5 + random.nextInt(2))
  this.imageOffset = new Vector2D(-8, -16)
  //this.setPersonality(Behaviour.attackPlayer(20) + Behaviour.randomFly)
  this.setPersonality(Behaviour.flyAroundPlayer)
  this.anchored = false
  this.gravity = 0
  this.hitBoxOffset = new Vector2D(-8, 5)
  this.setHitBoxDimensions(28, 20)
  this.setImageDimensions(141, 141)
//  animation.setFrameSpeed(0.2 * this.speed)
  entityAnimation.setFrameSpeed(0.2 * this.speed)
  val sound: Sound = new Sound("Sounds/big_insect.wav")
  val sound2: Sound = new Sound("Sounds/pichon/growl.wav")
  sound.setSoundRadius(1000)
  sound.setVolumeSuppression(0.5f)
  sound2.setSoundRadius(1000)
  this.entitySounds.movingSounds.add(sound)
  this.entitySounds.standingSounds.add(sound)
  this.entitySounds.jumpingSounds.add(sound)
  this.entitySounds.setSoundDelays(1500)
  var delay: Int = 1500
  var time: Long = 0
  val delay2: Int = 100
  var time2: Long = 0
  this.addPoint(-17,70)


  override def tick(): Unit = {
    super.tick()
    val player = Main.getGame.getPlayer
    this.imageRotation = math.sin(System.currentTimeMillis() / 400.0) * math.toRadians(20)
    if (System.currentTimeMillis() - time >= delay) {
      time=System.currentTimeMillis()
      this.random.setSeed(System.currentTimeMillis()+System.nanoTime())
      sound2.setVolumeSuppression((random.nextInt(5)+5)/10.0f)
      this.random.setSeed(System.currentTimeMillis()+System.nanoTime()+30)
      delay=random.nextInt(2500)+1500
      sound2.play(this.getPosition)
    }
    if (System.currentTimeMillis() - time2 >= delay2 && player!=null) {
      time2 = System.currentTimeMillis()
      Main.getGame.currentLevel.addObject(new Pichon_Laser(this.getPoint(0),this.getPoint(0),player.getCenter).setOwner(this))
    }

  }

  override def death(): Unit = {
    Main.getGame.currentLevel.getObjects.remove(this)
  }

  override def collisionFiltering(obj: Obj): Boolean = {
    obj.isInstanceOf[Enemy]
  }
}
