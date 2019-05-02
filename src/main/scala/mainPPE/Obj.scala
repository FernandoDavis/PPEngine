package mainPPE

//import java.awt.geom.Point2D
import java.awt.image.BufferedImage
import java.awt._

import javax.swing._

abstract class Obj(var x: Int, var y: Int, var width: Int, var height: Int) {

  def this() = this(0, 0, 0, 0)

  def this(v: Vector2D) = this(v.getX.toInt, v.getY.toInt, 0, 0)

  def this(x: Int, y: Int) = this(x, y, 0, 0)

  protected var Position: Vector2D = new Vector2D(x, y)
  protected var img: BufferedImage = null
  protected var noFriction: Boolean = false
  //  protected var height: Int = h
  //  protected var width: Int = w
  protected var mask: Rectangle = new Rectangle(this.width, this.height, this.Position.getX.toInt, this.Position.getY.toInt)
  protected var anchored: Boolean = true
  private var speedMask: SpeedMask = new SpeedMask(mask)
  protected var gravity: Double = 9.81
  protected var solid: Boolean = true
  protected var inAir: Boolean = true
  protected var airTime: Double = 0
  protected var velocity: Vector2D = new Vector2D(0, 0)
  protected var initVelocity: Vector2D = new Vector2D(0, 0)
  protected var yThru: Int = 0
  protected var xThru: Int = 0
  protected var canPassThru: Boolean = false
  protected var ground: Obj = null
  protected var elasticity: Double = 0
  protected var MaskColor: Color = Color.BLUE
  protected var maskOffset: Vector2D = new Vector2D(0, 0)
  protected var possessed: Boolean = false
  protected var left: Obj = null
  protected var right: Obj = null
  protected var lockMovement: Vector2D = new Vector2D(0, 0)
  protected var personality: Personality = new Personality(this)
  val startPosition: Vector2D = new Vector2D(x, y)
  protected var targetPos: Vector2D = null
  protected var targetObj: Obj = null
  protected var speed: Double = 0
  var canBeTouched: Boolean = true
  var canTouch: Boolean = true
  var targetDistance: Double = 30


  def leftCollision: Boolean = this.left != null

  def setTargetDistance(distance: Double): Unit = {
    this.targetDistance = distance
  }

  def rightCollision: Boolean = this.right != null

  def getTargetPos: Vector2D = targetPos

  def getPersonality: Personality = personality

  def setPersonality(personality: Personality): Unit = {
    personality.setOwner(this)
    this.personality = personality
  }

  def setPersonality(behaviour: Behaviour): Unit = {
    behaviour.setOwner(this)
    this.personality+=behaviour
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[Obj]

  def isPossessed: Boolean = this.possessed

  def getMask: Rectangle = {
    if (this.mask == null)
      this.mask = new Rectangle(this.width, this.height, this.Position.getX.toInt, this.Position.getY.toInt)
    mask
  }

  def getMass: Double = this.getMask.getWidth * this.getMask.getHeight

  def getMaskDimensions: Vector2D = {
    new Vector2D(this.mask.getWidth, this.mask.getHeight)
  }

  def setMaskColor(color: Color): Unit = {
    this.MaskColor = color
  }

  def getMaskColor: Color = {
    this.MaskColor
  }

  def getArea: Double = this.width * this.height

  def getImage: BufferedImage = {
    img
  }

  def setMask(mask: Rectangle) {
    this.mask = mask
  }

  def setImg(img: BufferedImage) {
    this.img = img
  }

  protected def refreshMask() {
    mask.setLocation(this.Position.getX.toInt + maskOffset.getX.toInt, this.Position.getY.toInt + maskOffset.getY.toInt)
    mask.setSize(width, height)
  }

  def setDimensions(width: Int, height: Int) {
    this.width = width
    this.height = height
    //this.refreshMask()
  }

  //  def isPassable(): Boolean={
  //    this.canPassThru
  //  }
  def setPassable(bool: Boolean) {
    this.canPassThru = bool
  }

  def thruX(): Int = {
    if (canPassThru)
      this.xThru
    else
      0
  }

  def thruY(): Int = {
    if (canPassThru)
      this.yThru
    else
      0
  }

  def getCenterX: Double = {
    this.Position.getX + this.width / 2.0
  }

  def getCenterY: Double = {
    this.Position.getY + this.height / 2.0
  }

  def getCenter: Vector2D = {
    this.Position + (this.width / 2.0, this.height / 2.0)
  }

  def getX: Double = {
    this.Position.getX
  }

  def getY: Double = {
    this.Position.getY
  }

  def setY(y: Double) {
    this.Position.setY(y)
  }

  def setX(x: Double) {
    this.Position.setX(x)
  }

  def setPosition(p: Vector2D) {
    this.Position = p
  }

  def getPosition: Vector2D = {
    if (this.Position == null)
      this.Position = new Vector2D(0, 0)
    this.Position.clone()
  }

  def setPosition(x: Double, y: Double) {
    this.setPosition(new Vector2D(x, y))
  }

  def translate(p: Vector2D) {
    this.setPosition(p.getX + this.getX, p.getY + this.getY)
  }

  def translate(x: Int, y: Int) {
    this.translate(new Vector2D(x, y))
  }

  def drawObj(g: Graphics, comp: JComponent) {
    this.refreshMask()
    if (this.getImage == null) {
      g.setColor(this.MaskColor)
      g.fillRect(this.Position.getX.toInt, this.Position.getY.toInt, this.width, this.height)
      g.setColor(Color.BLUE)
      g.drawRect(this.Position.getX.toInt, this.Position.getY.toInt, this.width, this.height)
    } else {
      g.drawImage(this.getImage, this.getX.toInt, this.getY.toInt, comp)
    }
  }

  def drawObj(g: Graphics2D, comp: JComponent, offset: Vector2D): Unit = {
    this.refreshMask()
    val shift: Vector2D = this.Position + offset
    if (this.getImage == null) {
      g.setColor(MaskColor)
      g.fillRect(shift.getX.toInt, shift.getY.toInt, this.width, this.height)
      g.setColor(Color.BLUE)
      g.drawRect(shift.getX.toInt, shift.getY.toInt, this.width, this.height)
    } else {
      g.drawImage(this.getImage, shift.getX.toInt, shift.getY.toInt,this.width,this.height,comp)
    }
  }

  def intersects(obj: Obj): Boolean = {
    if (this != obj) {
      if (this.ground == obj) {
        val mask2: Rectangle = obj.getMask.clone().asInstanceOf[Rectangle]
        mask2.grow(1, 1)
        if (mask2.intersects(this.getMask))
          return true
        else
          this.ground = null
      }
      this.speedMask.refresh(mask, this.velocity)
      return this.getMask.intersects(obj.getMask)
    }
    false
  }

  def isAnchored: Boolean = this.anchored

  def collision(obj: Obj) {
    if (!this.anchored && obj.canBeTouched && this.canTouch) {
      personality.runCollisionBehaviours(obj)
      obj.getPersonality.runCollisionBehaviours(this)
      if (this.getPosition.getY + this.getMask.getHeight * 3 / 4 - (this.velocity.getY * 2) < obj.getY) {
        if (this.velocity.getY > 0) { // && obj.yThru <= 0) {
          this.airTime = 0 //DO NOT REMOVE
          if (!obj.isAnchored) {
            if (math.abs(obj.getVelocity.getY) <= 0.2)
              this.velocity.setX(obj.getVelocity.getX)
            else
              this.velocity = obj.getVelocity
          }
          else
            this.velocity.setY(0)
          this.ground = obj
          this.setY(obj.getY - this.getMask.getHeight)
        }
      } else {
        this.overlap2(obj)
        if (!obj.isAnchored) {
          if(this.getY+this.height-2<obj.getY)
          this.velocity = this.velocity.getMax(obj.getVelocity)
        }
        else {
          //this.repulse(obj)
        }
      }
    }
  }

  def repulse(obj: Obj) {
    if (this.velocity.getMagnitude <= 0.1)
      return
    val r: Rectangle = this.mask.intersection(obj.getMask)
    val intersection: Vector2D = new Vector2D(r.getCenterX, r.getCenterY)
    this.velocity = (this.velocity - this.getCenter.UnitVector(intersection)) //* math.sqrt(r.getHeight*r.getHeight+r.getWidth*r.getWidth))
  }

  def overlap(obj: Obj) {
    if (this.getArea <= obj.getArea && obj != this) {
      val r: Rectangle = this.mask.intersection(obj.getMask)
      val intersection: Vector2D = new Vector2D(r.getCenterX, r.getCenterY)
      this.Position = this.Position - this.getCenter.UnitVector(intersection) * 4 //4 since the distance from the center to the edge is half the distance from edge to edge
      //this.velocity = new Vector(0,0)
    }
  }

  def overlap2(obj: Obj) {

    if (this.getArea <= obj.getArea && obj != this) {
      if (this.getY > obj.getCenterY + obj.height / 2 - this.height / 2) {
        if(this.ground!=null)
          return
        this.Position.setY(obj.getY + obj.height + 1)
        if (this.velocity.getY < 0)
          this.velocity.setY(0)
        return
      }

      if (this.getX <= obj.getCenterX) {
        this.Position.setX(obj.getX - this.width - 1)
        this.right = obj
        if (this.velocity.getX > 0)
          this.velocity.setX(0)
        return
      }
      if (this.getX > obj.getCenterX) {
        this.Position.setX(obj.getX + obj.width + 1)
        this.left = obj
        if (this.velocity.getX < 0)
          this.velocity.setX(0)
        return
      }

      if (this.getY + this.height - 1 < obj.getY) {
        this.Position.setY(obj.getY - obj.height - 1)
        if (this.velocity.getY < 0)
          this.velocity.setY(0)
      }
    } else if (this.getArea > obj.getArea && obj != this){

      if (this.getX > obj.getCenterX && obj.leftCollision) {
        this.Position.setX(obj.getX + obj.width + 1)
        this.left = obj
        if (this.velocity.getX < 0)
          this.velocity.setX(0)
        return
      }
      if (this.getX <= obj.getCenterX && obj.rightCollision) {
        this.Position.setX(obj.getX - this.width - 1)
        this.right = obj
        if (this.velocity.getX > 0)
          this.velocity.setX(0)
        return
      }
    }

  }

  def l(obj1: Obj, obj2: Obj): Double = {
    val angle: Double = obj1.Position.getDifferenceAngle(obj2.Position)
    val length: Double = obj1.Position.distance(obj2.Position)
    var x: Double = math.sin(angle) * length
    var xS: Double = 1
    if (x < 0)
      xS = -1
    var y: Double = math.cos(angle) * length
    var yS: Double = 1
    if (y < 0)
      yS = -1
    //    var L: Double = obj2.getX-obj1.getX
    //    if(obj1.getX>obj2.getX)
    //      L = obj1.getX-obj2.getX
    if (x * xS > obj1.width / 2)
      x = obj1.width / 2 * xS
    if (y * yS > obj1.height / 2)
      y = obj1.height / 2 * yS
    math.sqrt(x * x + y * y)
  }

  def tick() {
    this.refreshMask()
    if (left != null) {
      if (left.getY > this.getY + this.height - 1 || left.getY + left.height < this.getY || left.getX + left.width < this.getX - this.speed) {
        left = null
      }
    }
    if (right != null) {
      if (right.getY > this.getY + this.height - 1 || right.getY + right.height < this.getY || right.getX > this.getX + this.width + this.speed) {
        right = null
      }
    }
    if (gravity != 0 && (!anchored) && ground == null)
      this.doGravity()
    else if (ground != null) {
      this.velocity.setY(0)
      //if (math.abs(this.velocity.getX)<=0.2)
      if (math.abs(this.velocity.getX - ground.velocity.getX) <= 0.1 || math.abs(this.velocity.getX) <= 0.1) {
        this.velocity.setX(ground.getVelocity.getX)
      } else
      if (math.abs(this.velocity.getX) < 0.2) { //X velocity damping
        this.velocity.setX(0)
      } else if (!noFriction)
        this.velocity.setX(this.velocity.getX * 0.75) //Friction

    }
    if (!this.isPossessed && !this.anchored) {
      if (targetPos != null || targetObj != null) {
        var target: Vector2D = targetPos
        if (targetObj != null)
          target = targetObj.getCenter
        if (target.distance(this.getCenter) >= targetDistance) {
          val direction: Vector2D = this.getCenter.UnitVector(target)*speed
          if (this.gravity != 0)
            this.velocity.setX(direction.getX)
          else
            this.velocity = direction
        }
        else {
          if (targetObj != null)
            targetObj = null
          else
            targetPos = null
          if (this.gravity > 0)
            this.velocity.setX(0)
          else {
            this.velocity.setX(0)
            this.velocity.setY(0)
          }
        }
      }
    }
    else {
      //wut
    }
    if (this.personality != null)
      personality.run()
    if (this.velocity.getY > 30) //speed limits
      this.velocity.setY(30)
    else if (this.velocity.getY < (-30))
      this.velocity.setY(-30)
    if (this.velocity.getX > 30)
      this.velocity.setX(30)
    else if (this.velocity.getX < (-30))
      this.velocity.setX(-30)

    if((velocity.getX>0&&this.rightCollision)||(velocity.getX<0&&this.leftCollision))
      velocity.setX(0)
    this.setPosition(this.Position + this.velocity)
  }

  def setAnchored(anchored: Boolean) {
    this.anchored = anchored
  }

  def toggleAnchored(): Unit = {
    this.anchored = !this.anchored
  }

  def getVelocity: Vector2D = this.velocity

  def setVelocity(velocity: Vector2D) {
    this.velocity = velocity
    this.initVelocity = velocity
  }

  def setVelocity(x: Double, y: Double) {
    this.velocity = new Vector2D(x, y)
    this.initVelocity = velocity
  }

  def isSolid: Boolean = this.solid

  def getGround: Obj = this.ground

  def setGround(obj: Obj) {
    this.ground = obj
  }

  def setElasticity(elasticity: Double) {
    this.elasticity = elasticity
  }

  def getElasticity: Double = {
    this.elasticity
  }

  def doGravity() {
    if (ground != null) {
      if (!(this.getY + this.getMask.getY - 2 < ground.getY)) {
        //Do nothing
      }
    } else {
      this.velocity = this.velocity + (new Vector2D(0, gravity) * airTime)
      if (airTime == 0)
        this.initVelocity = velocity
      airTime += 0.001
    }

  }

  override def toString: String = {
    var space: String = ""
    if (ground != null)
      space = "\n\r"
    this.getClass.getSimpleName + ":\n[\nPosition: " + this.Position + " ,\n Width: " + this.width + " ,\n Height: " + this.height + " ,\n Velocity: " + this.velocity + " ,\n Is in screen: "+isInScreen+" ,\n Ground: " + space + "(" + this.ground + ")\n]"
  }

  override def equals(other: Any): Boolean = other match {
    case that: Obj =>
      (that canEqual this) &&
        Position == that.Position &&
        img == that.img &&
        height == that.height &&
        width == that.width &&
        mask == that.mask &&
        anchored == that.anchored &&
        gravity == that.gravity &&
        solid == that.solid &&
        inAir == that.inAir &&
        airTime == that.airTime &&
        velocity == that.velocity &&
        initVelocity == that.initVelocity &&
        yThru == that.yThru &&
        xThru == that.xThru &&
        canPassThru == that.canPassThru &&
        //ground == that.ground &&
        elasticity == that.elasticity &&
        MaskColor == that.MaskColor
    case _ => false
  }

  def setGravity(gravity: Double): Unit = {
    this.gravity = gravity
  }

  def getGravity: Double = {
    gravity
  }

  def jump(jumpSpeed: Double): Unit = {
    if (this.ground != null && math.abs(this.velocity.getY) <= 0.01) {
      this.airTime = 0
      this.ground = null
      this.translate(0, -4)
      this.velocity.setY(-jumpSpeed)
    }
  }

  def setTargetPosition(position: Vector2D): Unit = {
    this.targetPos = position
  }

  override def hashCode(): Int = {
    val state = Seq(Position, img, height, width, mask, anchored, gravity, solid, inAir, airTime, velocity, initVelocity, yThru, xThru, canPassThru, ground, elasticity, MaskColor)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }

  def getSpeed: Double = speed

  def setSpeed(newSpeed: Double) {
    this.speed = newSpeed
  }

  def setTargetObj(obj: Obj): Unit = {
    this.targetObj = obj
  }

  def getTargetObj: Obj = targetObj

  def currentLevel: Level = Main.getGame.currentLevel
  def game: Component = Main.getGame
  def setRight(obj: Obj): Unit = this.right=obj
  def setLeft(obj: Obj): Unit = this.left=obj
  def isInScreen: Boolean = {
    game.isInScreen(this)
  }

  def setVelocityX(n: Double): Unit ={
    this.velocity.setX(n)
  }
  def setVelocityY(n: Double): Unit ={
    this.velocity.setY(n)
  }
  def setPositionX(n: Double): Unit ={
    this.Position.setX(n)
  }
  def setPositionY(n: Double): Unit ={
    this.Position.setY(n)
  }

}