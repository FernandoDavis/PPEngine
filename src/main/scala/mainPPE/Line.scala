package mainPPE

import java.awt.Rectangle
import java.awt.geom.Line2D

class Line(x1: Double, y1: Double, x2: Double, y2: Double) extends Line2D.Double(x1: Double, y1: Double, x2: Double, y2: Double) {
  def this() = this(0, 0, 0, 0)
  def this(p1: Vector2D, p2: Vector2D) = this(p1.getX, p1.getY, p2.getX, p2.getY)

  def intersection(rectangle: Rectangle): Vector2D = {
    var m: Double = getSlope()
    if (m.isNaN()) {
      if (this.getStartPoint().distance(new Vector2D(rectangle.getCenterX(), rectangle.getCenterY)) <= math.abs(this.y2 - this.y1)) {
        return new Vector2D(this.x1, this.y1 - (rectangle.getY+rectangle.getHeight))
      }
      return null
    } else {
      if (this.getStartPoint().distance(new Vector2D(rectangle.getCenterX(), rectangle.getCenterY)) > this.getLength()) {
        return null
      }
      var b: Double = this.y1 - m * this.x1
      var bottom: Double = ((rectangle.getY + rectangle.getHeight) - b) / m
      var top: Double = (rectangle.getY - b) / m
      if (math.abs(top - rectangle.getCenterX) <= rectangle.getWidth/2.0)
        return new Vector2D(top, rectangle.getY)
      if (math.abs(bottom - rectangle.getCenterX) <= rectangle.getWidth/2.0)
        return new Vector2D(bottom, rectangle.getY + rectangle.getHeight())
      var left: Double = rectangle.getX * m + b
      var right: Double = (rectangle.getX + rectangle.getWidth()) * m + b
      if (math.abs(left - rectangle.getCenterY()) <= rectangle.getHeight/2.0) {
        return new Vector2D(rectangle.getX, left)
      }
      if (math.abs(right - rectangle.getCenterY()) <= rectangle.getHeight/2.0) {
        return new Vector2D(rectangle.getX + rectangle.getWidth(), right)
      }
    }
    return null
  }

  def getLength(): Double = {
    this.getStartPoint().distance(this.getEndPoint())
  }

  def getSlope(): Double = {
    var dX: Double = this.x2 - this.x1
    var dY: Double = this.y2 - this.y1
    if (dX == 0)
      return Double.NaN //straight vertical line
    if (dY == 0)
      return 0 //straight horizontal line
    return dY / dX
  }

  def getAngle(): Double = {
    this.getStartPoint().getDifferenceAngle(this.getEndPoint())
  }

  def getYIntercept(): Double = {
    return this.y1 - this.getSlope() * this.x1
  }

  def intersectsLine(line: Line): Boolean = {
    super.intersectsLine(line.getX1, line.getY1, line.getX2, line.getY2)
  }

  def getIntersection(line: Line): Vector2D = {
    if (!this.intersectsLine(line)){
      return null}
    var m1: Double = this.getSlope()
    var m2: Double = line.getSlope()
    var b1: Double = this.getYIntercept()
    var b2: Double = line.getYIntercept()
    var x: Double = (b2 - b1) / (m1 - m2)
    var y: Double = (-(m1 * b2) + (m2 * b1)) / ((m2 - m1))
    if (x.isNaN() || y.isNaN() || x.isInfinite() || y.isInfinite()) {
      return null
    }
    if (m1 == 0) {
      y = this.getY1()
    }
    if (m2 == 0) {
      y = line.getY1()
    }
    if (m1.isNaN()) {
      x = this.getX1()
    }
    if (m2.isNaN()) {
      x = line.getX1()
    }
    return new Vector2D(x, y)
  }

  def getStartPoint(): Vector2D = {
    new Vector2D(this.getX1, this.getY1)
  }
  def getEndPoint(): Vector2D = {
    new Vector2D(this.getX2, this.getY2)
  }

}