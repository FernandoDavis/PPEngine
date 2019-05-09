package mainPPE

import java.awt.event.KeyEvent
import java.awt.image.BufferedImage
import java.awt.{Graphics, Graphics2D}
import java.io.File

import Entities.Player
import javax.imageio.ImageIO
import javax.swing.JComponent

import scala.util.Random

abstract class Entity(x: Int, y: Int, w: Int, h: Int) extends Obj(x: Int, y: Int, w: Int, h: Int) {
  protected var Health: Double = 100
  protected var Lives: Int = 0
  protected var MaxHealth: Double = 100
  //protected var dir: Int = 0
  protected var moving: Boolean = false
  this.canBeTouched = false
  protected var healthbar: Healthbar = new Healthbar(this)
  protected var random: Random = new Random()
  protected var entityAnimation: EntityAnimation = null
  protected var entitySounds: EntitySounds = new EntitySounds
  protected var touchedGround = false
  healthbar.setConstant(false)
  healthbar.setAlpha(0)
  random.setSeed(this.getHealth.toLong + System.currentTimeMillis() + System.nanoTime() + this.MaxHealth.toLong)

  def death()

  //def behaviour()

  def getHealth: Double = Health

  def getMaxHealth: Double = MaxHealth

  def jump(): Unit = this.jump(this.getSpeed * 1.2)

  def setHealth(newHealth: Double) {
    if (newHealth <= 0) {
      this.Health = 0
      this.death()
    }
    else if (newHealth > this.MaxHealth)
      this.Health = MaxHealth
    else
      this.Health = newHealth
  }

  def setLives(newLives: Int) {
    if (newLives < 0)
      this.Lives = 0
    else
      this.Lives = newLives
  }

  def getLives: Int = Lives

  def takeDamage(damage: Double) {
    val newHealth: Double = this.Health - damage
    healthbar.appear()
    if (newHealth <= 0) {
      this.Health = 0
      this.death()
    }
    else
      this.Health = newHealth
  }

  override def tick(): Unit = {
    super.tick()
    if (ground != null)
      this.touchedGround = true
    if (!this.isInstanceOf[Player]) {
      if (math.abs(velocity.getX) >= 0.5) {
        moving = true
      }
      else
        moving = false
      if (this.velocity.getX > 0)
        dir = 1
      else if (this.velocity.getX < 0)
        dir = -1
    }
    if ((ground != null && moving) || gravity == 0) {
      this.entitySounds.playMovingSound(this.getCenter)
    } else if (ground == null && touchedGround && gravity != 0) {
      touchedGround = false
      this.entitySounds.playJumpingSound(this.getCenter)
    } else if (!moving && ground != null)
      this.entitySounds.playStandingSound(this.getCenter)

  }

  override def getImage: BufferedImage = {
    if (this.animated && entityAnimation != null) {
      if (ground == null && gravity != 0) {
        if (dir >= 0)
          return entityAnimation.getJumpingImg
        else
          return ImageFunctions.flipX(entityAnimation.getJumpingImg)
      }
      var img: BufferedImage = null
      if (moving) {
        img = entityAnimation.getMovingImg
      }
      else
        img = entityAnimation.getStandingImg
      if (dir >= 0)
        return ImageFunctions.rotateRadians(img, this.imageRotation + rotationOffset)
      else
        return ImageFunctions.rotateRadians(ImageFunctions.flipX(img), this.imageRotation + rotationOffset)
    }
    super.getImage
  }

  //  override def getImage: BufferedImage = {
  //    if (this.animated && animation != null) {
  //      if (ground == null && gravity != 0) {
  //        if (dir >= 0)
  //          return animation.getJumpingImg
  //        else
  //          return ImageFunctions.flipX(animation.getJumpingImg)
  //      }
  //      var img: BufferedImage = null
  //      if (moving)
  //        img = animation.getMovingImg
  //      else
  //        img = animation.getStandingImg
  //      if (dir >= 0)
  //        return ImageFunctions.rotateRadians(img, this.imageRotation + rotationOffset)
  //      else
  //        return ImageFunctions.rotateRadians(ImageFunctions.flipX(img), this.imageRotation + rotationOffset)
  //    }
  //    super.getImage
  //  }

  //  override def drawObj(g: Graphics, comp: JComponent): Unit = {
  //    super.drawObj(g, comp)
  //    healthbar.draw(g)
  //  }

  override def drawObj(g: Graphics2D, comp: JComponent, offset: Vector2D): Unit = {
    super.drawObj(g, comp, offset)
    healthbar.draw(g)
  }


  protected class EntitySounds {
    protected var volumeSuppression: Float = 0.0f
    protected var Standing: LinkedList[Sound] = new LinkedList[Sound]()
    protected var sDelay: Int = 100
    protected var sTime: Long = 0
    protected var Moving: LinkedList[Sound] = new LinkedList[Sound]()
    protected var mDelay: Int = 100
    protected var mTime: Long = 0
    protected var Jumping: LinkedList[Sound] = new LinkedList[Sound]()
    protected var jDelay: Int = 100
    protected var jTime: Long = 0
    private val random: Random = new Random()

    def setSoundDelays(standingDelay: Int, movingDelay: Int, jumpingDelay: Int): Unit = {
      this.sDelay = standingDelay
      this.mDelay = movingDelay
      this.jDelay = jumpingDelay
    }

    def setSoundDelays(delay: Int): Unit = {
      this.sDelay = delay
      this.mDelay = delay
      this.jDelay = delay
    }

    def standingSounds: LinkedList[Sound] = Standing

    def jumpingSounds: LinkedList[Sound] = Jumping

    def movingSounds: LinkedList[Sound] = Moving

    def getStandingSound: Sound = {
      random.setSeed(System.currentTimeMillis() + System.nanoTime())
      if (System.currentTimeMillis() - sTime < sDelay)
        return null
      if (standingSounds.size > 0)
        return standingSounds.get(random.nextInt(standingSounds.size))
      null
    }

    def getJumpingSound: Sound = {
      random.setSeed(System.currentTimeMillis() + System.nanoTime())
      if (System.currentTimeMillis() - jTime < jDelay)
        return null
      if (jumpingSounds.size > 0)
        return jumpingSounds.get(random.nextInt(jumpingSounds.size))
      null
    }

    def playMovingSound(position: Vector2D): Unit = {
      this.playMovingSound(position.getX, position.getY)
    }

    def playJumpingSound(position: Vector2D): Unit = {
      this.playJumpingSound(position.getX, position.getY)
    }

    def playStandingSound(position: Vector2D): Unit = {
      this.playStandingSound(position.getX, position.getY)
    }

    def playMovingSound(x: Double, y: Double): Unit = {
      random.setSeed(System.currentTimeMillis() + System.nanoTime())
      if (System.currentTimeMillis() - mTime < mDelay)
        return
      if (movingSounds.size > 0) {
        mTime=System.currentTimeMillis()
        movingSounds.get(random.nextInt(movingSounds.size)).play(x, y)
      }
    }

    def playStandingSound(x: Double, y: Double): Unit = {
      random.setSeed(System.currentTimeMillis() + System.nanoTime())
      if (System.currentTimeMillis() - sTime < sDelay)
        return
      if (standingSounds.size > 0) {
        sTime = System.currentTimeMillis()
        standingSounds.get(random.nextInt(standingSounds.size)).play(x, y)
      }
    }

    def playJumpingSound(x: Double, y: Double): Unit = {
      random.setSeed(System.currentTimeMillis() + System.nanoTime())
      if (System.currentTimeMillis() - jTime < jDelay)
        return
      if (jumpingSounds.size > 0) {
        jTime = System.currentTimeMillis()
        jumpingSounds.get(random.nextInt(jumpingSounds.size)).play(x, y)
      }

    }

    def getMovingSound: Sound = {
      random.setSeed(System.currentTimeMillis() + System.nanoTime())
      if (System.currentTimeMillis() - mTime < mDelay)
        return null
      if (movingSounds.size > 0)
        return movingSounds.get(random.nextInt(movingSounds.size))
      null
    }

  }

  protected class EntityAnimation {

    protected var Standing: Animation = new Animation
    protected var Moving: Animation = new Animation
    protected var Jumping: Animation = new Animation

    def setFrameSpeed(frameSpeed: Double): Unit = {
      Standing.setFrameSpeed(frameSpeed)
      Jumping.setFrameSpeed(frameSpeed)
      Moving.setFrameSpeed(frameSpeed)
    }

    def setMovingAnimation(list: BufferedImage*): Unit = {
      Moving.setAnimationSeq(list)
    }

    def setJumpingAnimation(list: BufferedImage*): Unit = {
      Jumping.setAnimationSeq(list)
    }

    def setStandingAnimation(list: BufferedImage*): Unit = {
      Standing.setAnimationSeq(list)
    }

    def setMovingAnimation(list: ArrayList[BufferedImage]): Unit = {
      this.Moving.setAnimation(list)
    }

    def setJumpingAnimation(list: ArrayList[BufferedImage]): Unit = {
      this.Jumping.setAnimation(list)
    }

    def setStandingAnimation(list: ArrayList[BufferedImage]): Unit = {
      this.Standing.setAnimation(list)
    }

    def setMovingAnimation(anim: Animation): Unit = {
      this.Moving = anim
    }

    def setJumpingAnimation(anim: Animation): Unit = {
      this.Jumping = anim
    }

    def setStandingAnimation(anim: Animation): Unit = {
      this.Standing = anim
    }

    def getStandingImg: BufferedImage = {
      Moving.reset()
      Jumping.reset()
      this.Standing.getImg
    }

    def getJumpingImg: BufferedImage = {
      Moving.reset()
      Standing.reset()
      this.Jumping.getImg
    }

    def getMovingImg: BufferedImage = {
      Jumping.reset()
      Standing.reset()
      this.Moving.getImg
    }

  }

}
