package mainPPE

import java.awt.{Dimension, Point}
import java.awt.geom.Point2D

class Vector2D {
  var x: Double = 0
  var y: Double = 0

  def this(x: Double, y: Double) {
    this()
    this.x = x
    this.y = y
  }

  def this(X: Int, Y: Int) = this(X.toDouble, Y.toDouble)

  def this(p: Point) = this(p.getX, p.getY)

  def this(p: Point2D) = this(p.getX, p.getY)

  def +(v: Vector2D) =
    new Vector2D(this.x + v.getX, this.y + v.getY)

  def +(x: Double, y: Double) =
    new Vector2D(this.x + x, this.y + y)

  def -(x: Double, y: Double) =
    new Vector2D(this.x - x, this.y - y)

  def *(x: Double, y: Double) =
    new Vector2D(this.x * x, this.y * y)

  def -(v: Vector2D): Vector2D =
    new Vector2D(this.x - v.getX, this.y - v.getY)

  def -(n: Double): Vector2D =
    new Vector2D(this.x - n, this.y - n)

  def +(n: Double): Vector2D =
    new Vector2D(this.x + n, this.y + n)

  def *(v: Vector2D): Vector2D =
    new Vector2D(this.x * v.getX, this.y * v.getY)

  def *(n: Double): Vector2D =
    new Vector2D(this.x * n, this.y * n)

  def /(v: Vector2D): Vector2D =
    new Vector2D(this.x / v.getX, this.y / v.getY)

  def /(n: Double): Vector2D =
    new Vector2D(this.x / n, this.y / n)

  def ^(n: Double): Vector2D =
    new Vector2D(math.pow(this.x, n), math.pow(this.y, n))


  def getDataFrom(point: Point): Unit = {
    this.setX(point.getX)
    this.setY(point.getY)
  }

  def getDataFrom(point: Point2D): Unit = {
    this.setX(point.getX)
    this.setY(point.getY)
  }

  def +=(x: Double, y: Double) {
    this.translate(x, y)
  }

  def -=(x: Double, y: Double) {
    this.translate(-x, -y)
  }

  def +=(v: Vector2D) {
    this.translate(v)
  }

  def -=(v: Vector2D) {
    this.translate(new Vector2D(0, 0) - v)
  }

  def getMax(v: Vector2D): Vector2D = {
    if (this.getMagnitude > v.getMagnitude)
      return this
    return v.clone()
  }

  def getDifferenceAngle(v: Vector2D): Double = {
    math.atan2(v.getY - this.y, v.getX - this.x)
  }

  def getDirectedAngle(v: Vector2D): Double = {
    math.atan2(v.getY, v.getX) - math.atan2(this.y, this.x)
  }

  def unitVector(v: Vector2D): Vector2D = {
    val a: Double = this.getDifferenceAngle(v)
    new Vector2D(math.cos(a), math.sin(a))
  }
  def unitVectorAngle: Double = {
    math.atan2(y, x)
  }

  def getMagnitude: Double = {
    math.sqrt(this.y * this.y + this.x * this.x)
  }

  def crossProduct(n: Double): Vector2D = {
    new Vector2D(n * this.y, -n * this.x)
  }

  def crossProduct(v: Vector2D): Double = {
    this.x * v.y - this.y * v.getX
  }

  override def toString(): String =
    "Vector(" + x + " , " + y + ")"

  def getX: Double = x

  def getY: Double = y

  def set(x: Double, y: Double) {
    this.x = x
    this.y = y
  }

  def set(vector: Vector2D) {
    this.x = vector.x
    this.y = vector.y
  }

  def setX(x: Double) {
    this.x = x
  }

  def setY(y: Double) {
    this.y = y
  }

  override def clone(): Vector2D = new Vector2D(x, y)

  def distance(v: Vector2D): Double = {
    return math.sqrt(math.pow(x - v.getX, 2) + math.pow(y - v.getY, 2))
  }

  def distance(x: Double, y: Double): Double = {
    return math.sqrt(math.pow(this.x - x, 2) + math.pow(this.y - y, 2))
  }

  def translate(x: Double, y: Double): Unit = {
    this.setY(this.getY + y)
    this.setX(this.getX + x)
  }

  def translate(vector: Vector2D): Unit = {
    this.setY(this.getY + vector.getY)
    this.setX(this.getX + vector.getX)
  }

  def toPoint(): Point = {
    new Point(this.getX.toInt, this.getY.toInt)
  }

  def toPoint2D(): Point2D.Double = {
    new Point2D.Double(this.getX, this.getY)
  }

  def toDimension(): Dimension = {
    new Dimension(this.getX.toInt, this.getY.toInt)
  }

  def roundToNDigits(n: Int): Vector2D = {
    val power: Double = math.pow(10, n)
    new Vector2D(math.floor(this.getX * power) / power, math.floor(this.getY * power) / power)
  }


}