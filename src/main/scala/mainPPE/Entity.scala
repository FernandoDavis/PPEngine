package mainPPE

import java.awt.event.KeyEvent
import java.awt.image.BufferedImage
import java.awt.{Graphics, Graphics2D}

import Entities.Player
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
    if (!this.isInstanceOf[Player]) {
      if (math.abs(velocity.getX) >= 0.5)
        moving = true
      else
        moving = false
      if (this.velocity.getX > 0)
        dir = 1
      else if (this.velocity.getX < 0)
        dir = -1
    }
  }

  override def getImage: BufferedImage = {
    if (this.animated && animation != null) {
      if (ground == null && gravity != 0) {
        if (dir >= 0)
          return animation.getJumpingImg
        else
          return ImageFunctions.flipX(animation.getJumpingImg)
      }
      var img: BufferedImage = null
      if (moving)
        img = animation.getMovingImg
      else
        img = animation.getStandingImg
      if (dir >= 0)
        return ImageFunctions.rotateRadians(img, this.imageRotation + rotationOffset)
      else
        return ImageFunctions.rotateRadians(ImageFunctions.flipX(img), this.imageRotation + rotationOffset)
    }
    super.getImage
  }

  //  override def drawObj(g: Graphics, comp: JComponent): Unit = {
  //    super.drawObj(g, comp)
  //    healthbar.draw(g)
  //  }

  override def drawObj(g: Graphics2D, comp: JComponent, offset: Vector2D): Unit = {
    super.drawObj(g, comp, offset)
    healthbar.draw(g)
  }
}
