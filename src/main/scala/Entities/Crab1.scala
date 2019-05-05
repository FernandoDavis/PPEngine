package Entities

import mainPPE._


class Crab1(x: Int, y: Int, w: Int, h: Int) extends Entity(x: Int, y: Int, w: Int, h: Int) {
  def this() = this(0, 0, 36, 25)

  def this(v: Vector2D) = this(v.getX.toInt, v.getY.toInt, 36, 25)

  def this(x: Integer, y: Integer) = this(x, y, 54, 38)

  val spriteSheet: SpriteSheet = new SpriteSheet(loadImage("IMAGES/crab1/sheet.png"),3,2,36,25,4)
  this.animation=new Animation
  this.animated=true
  animation.setMovingAnimation(spriteSheet.getArrayListOfImages)
  animation.setStandingAnimation(loadImage("IMAGES/crab1/still.png"))
  animation.setJumpingAnimation(loadImage("IMAGES/crab1/still.png"))
  this.canTouch=true
  this.setSpeed(4)
  this.setPersonality(Behaviour.attackPlayer(20)+(Behaviour.followPlayer~Behaviour.moveBackAndForthH(200,this.startPosition))+Behaviour.jump)
  this.anchored = false
  this.imageOffset=new Vector2D(0,2)


  override def death(): Unit = {
    Main.getGame.currentLevel.getObjects.remove(this)
  }
}
