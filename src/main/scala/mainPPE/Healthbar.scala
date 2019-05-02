package mainPPE

import java.awt.{Color, Graphics, Graphics2D}

import Entities.Player

class Healthbar extends UIBox {

  protected var obj: Obj = null
  protected var secondaryColor: Color = Color.RED
  protected var health: Double = 100
  protected var maxHealth: Double = 100
  protected var anchoredToObject: Boolean = true
  this.color = Color.GREEN
  this.w = 50
  this.h = 10
  this.borderWidth = 1
  var goodHealth: Double = 100
  var goodHealthFlash: Double = 1000
  var badHealth: Double = 25
  var badHealthFlash: Double = 70
  var alpha: Float = 100
  var constant: Boolean = true
  var timer: Long = 0
  var delay: Long = 400

  def this(obj: Obj) {
    this()
    this.obj = obj
  }

  def setAnchoredToObject(boolean: Boolean): Unit = {
    this.anchoredToObject = boolean
  }

  def isAnchoredToObject: Boolean = this.anchoredToObject

  def setObj(obj: Obj): Unit = {
    this.obj = obj
  }

  def setHealth(health: Double): Unit = {
    this.health = health
  }

  def setMaxHealth(health: Double): Unit = {
    this.maxHealth = health
  }

  def setSecondaryColor(color: Color): Unit = {
    this.secondaryColor = color
  }

  def setFlashSpeed(goodHealth: Double, goodHealthFlash: Double, badHealth: Double, badHealthFlash: Double): Unit = {
    this.goodHealth = goodHealth
    this.goodHealthFlash = goodHealthFlash
    this.badHealth = badHealth
    this.badHealthFlash = badHealthFlash
    if (this.goodHealth == this.badHealth)
      this.goodHealth += 0.01
  }

  def getFlashSpeed(n: Double): Double = {
    val m: Double = (goodHealthFlash - badHealthFlash) / (goodHealth - badHealth)
    val b: Double = goodHealthFlash - m * goodHealth
    clamp(n * m + b, 70, 2000)
  }

  def clamp(n: Double, min: Double, max: Double): Double = {
    var result: Double = n
    if (result < min)
      result = min
    if (result > max)
      result = max
    result
  }

  def setAlpha(alpha: Float): Unit ={
    this.alpha = clamp(alpha,0,100).toFloat
  }

  private def setColorAlpha(color: Color, alpha: Float): Color = {
    val newAlpha = clamp(alpha, 0, 1).toFloat
    new Color(color.getRed / 255.0f, color.getGreen / 255.0f, color.getBlue / 255.0f, newAlpha)
  }


  def appear(): Unit = {
    this.alpha = 100 //AND BAM IT APPEARS!
  }

  def setConstant(boolean: Boolean): Unit = {
    this.constant = boolean
  }

  def draw(g2: Graphics2D): Unit = {
    this.draw(g2.asInstanceOf[Graphics])
  }

  override def draw(g: Graphics): Unit = {
    if (!constant) {
      if (alpha > 0) {
        alpha -= 0.5f*(health/maxHealth).toFloat
      }
      else
        return
    }
    if (obj != null) {
      if (anchoredToObject) {
        this.setPosition((obj.getCenter - (0, obj.height / 2 + 5) - this.w / 2) + Main.getGame.getScreenShift)
      }
      if (obj.isInstanceOf[Entity]) {
        val entity: Entity = obj.asInstanceOf[Entity]
        health = entity.getHealth
        maxHealth = entity.getMaxHealth
      }
    }

    val oldColor = g.getColor
    borderColor = setColorAlpha(borderColor, alpha / 100.0f)
    secondaryColor = setColorAlpha(secondaryColor, alpha / 100.0f)
    g.setColor(borderColor)
    g.fillRect(x, y, w, h)
    g.setColor(secondaryColor)
    g.fillRect(x + borderWidth, y + borderWidth, w - borderWidth * 2, h - borderWidth * 2)
    val flash: Double = (math.sin(System.currentTimeMillis() / getFlashSpeed(health)) + 1.0) / 2.0
    val R: Double = (255 - color.getRed) * flash
    val G: Double = (255 - color.getGreen) * flash
    val B: Double = (255 - color.getBlue) * flash
    var tempColor: Color = new Color((color.getRed + R).toInt, (color.getGreen + G).toInt, (color.getBlue + B).toInt)
    tempColor = setColorAlpha(tempColor, alpha / 100.0f)
    g.setColor(tempColor)
    g.fillRect(x + borderWidth, y + borderWidth, ((w - borderWidth * 2) * health / maxHealth).toInt, h - borderWidth * 2)
    g.setColor(oldColor)
  }


}
