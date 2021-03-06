package Objects

import java.awt.Color

import mainPPE.{Obj, Vector2D}

class Box(x: Integer, y: Integer, w: Int, h: Int) extends Obj(x: Int, y: Int, w: Int, h: Int) {
  def this() = this(0, 0, 40, 40)

  def this(x: Integer, y: Integer) = this(x, y, 40, 40)

  def this(position: Vector2D,dimensions: Vector2D) = {
    this()
    this.setPosition(position)
    this.setDimensions(dimensions)
  }

  this.MaskColor = Color.CYAN
  this.elasticity = 3
}