package mainPPE

import java.awt.{Color, Graphics}

class UIBox {
  var x: Int = 0
  var y: Int = 0
  var w: Int = 0
  var h: Int = 0

  def this(x: Int, y: Int, w: Int, h: Int) {
    this()
    this.x = x
    this.y = y
    this.w = w
    this.h = h
  }

  def this(position: Vector2D, dimensions: Vector2D) {
    this(position.getX.toInt, position.getY.toInt, dimensions.getX.toInt, dimensions.getY.toInt)
  }

  def setPosition(v: Vector2D): Unit ={
    this.y=v.getY.toInt
    this.x=v.getX.toInt
  }

  def getPosition: Vector2D = new Vector2D(x,y)

  def getDimensions: Vector2D = new Vector2D(w,h)

  def setX(x: Int): Unit = {
    this.x = x
  }

  def setY(y: Int): Unit = {
    this.y = y
  }

  def setWidth(w: Int): Unit = {
    this.w = w
  }

  def setHeight(h: Int): Unit = {
    this.h = h
  }

  if (x < 0)
    x = 0
  if (y < 0)
    y = 0
  if (w < 0)
    w = 0
  if (h < 0)
    h = 0

  var borderWidth: Int = 0
  var color: Color = Color.white
  var borderColor: Color = Color.BLACK

  def setBorderWidth(width: Int): Unit = {
    if (width > 0)
      this.borderWidth = width
  }

  def getBorderWidth: Int = this.borderWidth

  def setColor(color: Color): Unit = {
    if (color != null)
      this.color = color
  }

  def getColor: Color = this.color

  def setBorderColor(color: Color): Unit = {
    if (color != null)
      this.borderColor = color
  }

  def getBorderColor: Color = this.borderColor

  def draw(g: Graphics): Unit = {
    val oldColor = g.getColor
    g.setColor(borderColor)
    g.fillRect(x, y, w, h)
    g.setColor(color)
    g.fillRect(x + borderWidth, y + borderWidth, w - borderWidth * 2, h - borderWidth * 2)
    g.setColor(oldColor)
  }

}
