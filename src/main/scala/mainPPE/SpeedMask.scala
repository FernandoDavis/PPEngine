package mainPPE

import java.awt.{Color, Graphics, Rectangle}

class SpeedMask(rectangle: Rectangle) {

  var mask: Rectangle = (rectangle.clone()).asInstanceOf[Rectangle]
  var Lines: Array[Line] = new Array[Line](4)

  def getMask: Rectangle = {this.mask}
  def refresh(originalMask: Rectangle, velocity: Vector2D) {
    var offset: Vector2D = (velocity)
    mask.setSize(originalMask.getSize)
    mask.setLocation((offset.getX + originalMask.getX).toInt, (offset.getY + originalMask.getY).toInt)
    
    Lines = Array(
      new Line(
        originalMask.getX,
        originalMask.getY,
        originalMask.getX + offset.getX,
        originalMask.getY + offset.getY),
      new Line(
        originalMask.getX + originalMask.getWidth(),
        originalMask.getY,
        originalMask.getX + offset.getX + originalMask.getWidth(),
        originalMask.getY + offset.getY),
      new Line(
        originalMask.getX + originalMask.getWidth(),
        originalMask.getY + originalMask.getHeight(),
        originalMask.getX + offset.getX + originalMask.getWidth(),
        originalMask.getY + offset.getY + originalMask.getHeight()),
      new Line(
        originalMask.getX,
        originalMask.getY + originalMask.getHeight(),
        originalMask.getX + offset.getX,
        originalMask.getY + offset.getY + originalMask.getHeight()))
  }

  def intersection(originalMask: Rectangle, velocity: Vector2D, other: Rectangle): Vector2D = {
    this.refresh(originalMask, velocity)
    for (i <- 0 to Lines.length - 1) {
      val line: Line = Lines(i)
      val point: Vector2D = line.intersection(other)
      if (point != null) {
        println(point)
        i match {
          case 0 => return point
          case 1 => return point + new Vector2D(0, originalMask.width)
          case 2 => return point + new Vector2D(originalMask.height, originalMask.width)
          case 3 => return point + new Vector2D(originalMask.height, 0)
        }
      }
    }
    if (mask.intersects(other)) {
      val temp: Rectangle = mask.intersection(other)
      //println(new Point(temp.getX-originalMask.getWidth,temp.getY-originalMask.getHeight))
      return new Vector2D(this.mask.getX,this.mask.getY)
    }
    return null
  }

  def getIntersectionPoint(originalMask: Rectangle, velocity: Vector2D, other: Rectangle): Vector2D = {
    this.refresh(originalMask, velocity)
    for (i <- 0 to Lines.length - 1) {
      val line: Line = Lines(i)
      val point: Vector2D = line.intersection(other)
      if (point != null) {
        return point
      }
    }

    if (mask.intersects(other)) {
      val temp: Rectangle = mask.intersection(other)
      return new Vector2D(temp.getCenterX, temp.getCenterY)
    }
    return null
  }

  def visualize(g: Graphics) {
    g.setColor(Color.BLACK)
    for (line <- Lines) {
      if (line != null)
        g.drawLine(line.getX1.toInt, line.getY1.toInt, line.getX2.toInt, line.getY2.toInt)
    }
    g.drawRect(mask.x, mask.y, mask.width, mask.height)
  }

}