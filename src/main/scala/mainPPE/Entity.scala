package mainPPE

import java.awt.{Graphics, Graphics2D}

import javax.swing.JComponent

abstract class Entity(x: Int, y: Int, w: Int, h: Int) extends Obj(x: Int, y: Int, w: Int, h: Int) {
  protected var Health: Double = 100
  protected var Lives: Int = 0
  protected var MaxHealth: Double = 100
  this.canBeTouched = false
  protected var healthbar: Healthbar = new Healthbar(this)
  healthbar.setConstant(false)
  healthbar.setAlpha(0)

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
  }

  override def drawObj(g: Graphics, comp: JComponent): Unit = {
    super.drawObj(g, comp)
    healthbar.draw(g)
  }

  override def drawObj(g: Graphics2D, comp: JComponent, offset: Vector2D): Unit = {
    super.drawObj(g, comp, offset)
    healthbar.draw(g)
  }
}
