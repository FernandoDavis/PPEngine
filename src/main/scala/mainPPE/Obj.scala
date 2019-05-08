package mainPPE

//import java.awt.geom.Point2D
import java.awt.image.BufferedImage
import java.awt._
import java.io.File

import Objects.Projectile
import javax.imageio.ImageIO
import javax.swing._

/**
  * @author: Gabriel Soto Ramos
  */
abstract class Obj() {

  protected var x: Int = 0
  protected var y: Int = 0
  protected var width: Int = 0
  protected var height: Int = 0

  def this(x: Int, y: Int, width: Int, height: Int) {
    this()
    this.x = x
    this.y = y
    this.width = width
    this.height = height
    this.Position = new Vector2D(x, y)
    this.mask = new Rectangle(this.width, this.height, this.Position.getX.toInt, this.Position.getY.toInt)
    this.startPosition = new Vector2D(x, y)
  }


  def this(v: Vector2D) = this(v.getX.toInt, v.getY.toInt, 0, 0)

  def this(x: Int, y: Int) = this(x, y, 0, 0)

  protected var Position: Vector2D = new Vector2D(x, y)
  protected var img: BufferedImage = null
  protected var noFriction: Boolean = false
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
  var startPosition: Vector2D = new Vector2D(x, y)
  protected var targetPos: Vector2D = null
  protected var targetObj: Obj = null
  protected var speed: Double = 0
  protected var canBeTouched: Boolean = true
  protected var canTouch: Boolean = true
  var targetDistance: Double = 30
  protected var tileableTexture: Boolean = false
  protected var textureFollowsObject: Boolean = false
  protected var topTexture: BufferedImage = null
  protected var animated = false
  protected var animation: Animation = null
  protected var imageOffset: Vector2D = new Vector2D(0, 0)
  protected var imageRotation: Double = 0
  protected var rotationOffset: Double = 0
  protected var imageDimensions: Vector2D = new Vector2D(0, 0)
  protected var customImageDimensions: Boolean = false
  protected var customMaskDimensions: Boolean = false
  protected var customHitBoxDimensions: Boolean = false
  protected var maskDimensions: Vector2D = new Vector2D(width, height)
  protected var hitBox: Rectangle = new Rectangle(this.width, this.height, this.Position.getX.toInt, this.Position.getY.toInt)
  protected var hitBoxDimensions: Vector2D = new Vector2D(width, height)
  protected var dir = 1
  protected var vdir = 1
  protected var hitBoxOffset: Vector2D = new Vector2D(0, 0)
  protected var originalPoints: ArrayList[Vector2D] = new ArrayList[Vector2D]()
  protected var points: ArrayList[Vector2D] = new ArrayList[Vector2D]()

  def setCanBeTouchedByOthers(boolean: Boolean): Unit = {
    this.canBeTouched = boolean
  }

  def setCanTouchOthers(boolean: Boolean): Unit = {
    this.canTouch = boolean
  }

  def reset(): Unit = {
    this.setPosition(this.startPosition)
    val newPersonality = this.personality.clone()
    this.personality = newPersonality
  }

  def isTouchable: Boolean = this.canBeTouched

  def isAbleToTouch: Boolean = this.canTouch

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
    this.personality += behaviour
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
    if(this.animated&&this.animation!=null)
      return this.animation.getImg
    img
  }

  def setMask(mask: Rectangle) {
    this.mask = mask
  }

  def setTopTexture(path: String) {
    val file: File = new File("src\\" + path)
    if (file.exists()) {
      this.topTexture = ImageIO.read(file)
    }
  }

  def setTopTexture(image: BufferedImage): Unit = {
    this.topTexture = image
  }

  def setImg(path: String) {
        val file: File = new File("src\\" + path)
        if (file.exists()) {
          this.img = ImageIO.read(file)
        }
  }


  def setImg(img: BufferedImage) {
    this.img = img
  }

  def getTopTexture: BufferedImage = this.topTexture

  def setAnimation(animation: Animation): Unit = {
    this.animation = animation
  }

  def getAnimation: Animation = this.animation

  def setTileable(boolean: Boolean): Unit = {
    this.tileableTexture = boolean
  }

  def setTextureFollowing(boolean: Boolean): Unit = {
    this.textureFollowsObject = boolean
  }

  def setMaskDimensions(width: Double, height: Double): Unit = {
    this.setMaskDimensions(new Vector2D(width, height))
  }

  def setMaskDimensions(dimensions: Vector2D): Unit = {
    this.customMaskDimensions = true
    this.maskDimensions = dimensions
  }

  def setHitBoxDimensions(width: Double, height: Double): Unit = {
    this.setHitBoxDimensions(new Vector2D(width, height))
  }

  def setHitBoxDimensions(dimensions: Vector2D): Unit = {
    this.customHitBoxDimensions = true
    this.hitBoxDimensions = dimensions
  }

  def getHitBox: Rectangle = this.hitBox

  def setHitBoxOffset(offset: Vector2D): Unit = {
    this.hitBoxOffset = offset
  }

  def setImageDimensions(width: Double, height: Double): Unit = {
    this.customImageDimensions = true
    this.imageDimensions = new Vector2D(width, height)
  }

  def setImageDimensions(dimension: Vector2D): Unit = {
    this.customImageDimensions = true
    this.imageDimensions = dimension
  }

  def setCustomImageDimensions(boolean: Boolean): Unit = {
    this.customImageDimensions = boolean
  }

  def setHitBoxOffset(x: Double, y: Double): Unit = {
    this.hitBoxOffset = new Vector2D(x, y)
  }

  def clearPoints(): Unit ={
    this.points.clear()
    this.originalPoints.clear()
  }

  def addPoint(x: Double, y: Double): Unit ={
    this.addPoint(new Vector2D(x,y))
  }

  def addPoint(point: Vector2D): Unit ={
    this.originalPoints.add(point)
    this.points.add(point.clone())
  }

  def getPoint(index: Int): Vector2D ={
    points.get(index)
  }
  def getPoints: ArrayList[Vector2D] = points

  def getHitBoxDimensions: Vector2D = new Vector2D(hitBox.getWidth, hitBox.getHeight)

  protected def refreshMask() {
    //mask.setLocation((this.Position+this.maskDimensions).toPoint())//(this.Position.getX.toInt + maskOffset.getX.toInt, this.Position.getY.toInt + maskOffset.getY.toInt)
    mask.setSize(width, height)
    if (!customHitBoxDimensions)
      hitBox.setSize(width, height)
    else
      hitBox.setSize(hitBoxDimensions.toDimension())
    val hbL: Double = hitBoxOffset.getMagnitude
    val cos: Double = math.cos(this.imageRotation)
    val sin: Double = math.sin(this.imageRotation)
    val xhr: Double = cos * hbL * dir
    val yhr: Double = sin * hbL * vdir
    val mL: Double = maskOffset.getMagnitude
    val xmr: Double = cos * mL * dir
    val ymr: Double = sin * mL * vdir
    mask.setLocation((this.Position + new Vector2D(xmr, ymr) + this.maskOffset * (dir, vdir) + this.getDimensions / 2 - this.getMaskDimensions / 2).toPoint())
    hitBox.setLocation((this.Position + new Vector2D(xhr, yhr) + this.hitBoxOffset * (dir, vdir) + this.getDimensions / 2 - this.getHitBoxDimensions / 2).toPoint())
    if(originalPoints!=null)
      if(originalPoints.size>0)
        for(i <- originalPoints.indices.par){
          val op = originalPoints.get(i)*(dir,vdir)
          val p = points.get(i)
          val x = op.getX*cos-op.getY*sin
          val y = op.getY*cos+op.getX*sin
          p.set(this.Position+this.getDimensions/2+(x,y)+imageOffset)
        }
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

  //  def drawObj(g: Graphics, comp: JComponent) {
  //    this.refreshMask()
  //    if (this.getImage == null) {
  //      g.setColor(this.MaskColor)
  //      g.fillRect(this.Position.getX.toInt, this.Position.getY.toInt, this.width, this.height)
  //      g.setColor(Color.BLUE)
  //      g.drawRect(this.Position.getX.toInt, this.Position.getY.toInt, this.width, this.height)
  //    } else {
  //      var img = this.getImage
  //      if (tileableTexture)
  //        img = ImageFunctions.tileBySize(this.getImage, this.getWidth, this.getHeight, this.getX.toInt, this.getY.toInt)
  //      g.drawImage(img, this.getX.toInt, this.getY.toInt, comp)
  //    }
  //  }

  def drawObj(g: Graphics2D, comp: JComponent, offset: Vector2D): Unit = {
    this.refreshMask()
    val shift: Vector2D = this.Position + offset
    if (this.getImage == null) {
      g.setColor(MaskColor)
      g.fillRect(shift.getX.toInt, shift.getY.toInt, this.width, this.height)
      g.setColor(Color.BLUE)
      g.drawRect(shift.getX.toInt, shift.getY.toInt, this.width, this.height)
    } else {
      if (tileableTexture) {
        var sx: Int = this.getX.toInt
        var sy: Int = this.getY.toInt
        if (!textureFollowsObject) {
          sx = 0
          sy = 0
        }
        val Img: BufferedImage = ImageFunctions.tileBySize(this.getImage, this.getWidth, this.getHeight, sx, sy)
        g.drawImage(Img, shift.getX.toInt, shift.getY.toInt, comp)
        if (this.getTopTexture != null) {
          val TopImg: BufferedImage = ImageFunctions.tileBySize(this.getTopTexture, this.getWidth, this.getTopTexture.getHeight(), sx, 0)
          g.drawImage(TopImg, shift.getX.toInt, shift.getY.toInt, comp)
        }
      }
      else {
        var w: Double = this.width
        var h: Double = this.height
        if (this.customImageDimensions) {
          w = imageDimensions.getX
          h = imageDimensions.getY
        }
        g.drawImage(processImage(this.getImage, w.toInt, h.toInt), shift.getX.toInt + imageOffset.getX.toInt * dir - (w / 2).toInt + this.width / 2, shift.getY.toInt + imageOffset.getY.toInt * vdir - (h / 2).toInt + this.height / 2, w.toInt, h.toInt, comp)
        if (this.getTopTexture != null)
          g.drawImage(this.getTopTexture, shift.getX.toInt, shift.getY.toInt, this.width, this.height / 4, comp)
      }
    }
  }

  def processImage(img: BufferedImage, w: Int, h: Int): BufferedImage = {
    if (w != img.getWidth || h != img.getHeight) {
      return ImageFunctions.resize(img, this.width, this.height)
    }
    img
  }

  def collisionFiltering(obj: Obj): Boolean = {
    false //This gets overriden in entities and objects that need it, by default it should be false
  }

  def hasCustomImageDimensions: Boolean = customImageDimensions

  def getCustomImageDimensions: Vector2D = {
    if (this.customImageDimensions)
      this.imageDimensions
    else
      new Vector2D(this.width, this.height)
  }

  def intersects(obj: Obj): Boolean = {
    if (this != obj && !collisionFiltering(obj)) {
      if (this.ground == obj) {
        val mask2: Rectangle = obj.getMask.clone().asInstanceOf[Rectangle]
        mask2.grow(1, 1)
        if (mask2.intersects(this.getMask))
          return true
        else
          this.ground = null
      }
      // this.speedMask.refresh(mask, this.velocity)
      return this.getMask.intersects(obj.getMask)
    }
    false
  }

  def isAnchored: Boolean = this.anchored

  def collision_NEW(obj: Obj) {
    if (!this.anchored && obj.canBeTouched && this.canTouch) {
      //personality.runCollisionBehaviours(obj)
      //obj.getPersonality.runCollisionBehaviours(this)
      if (this.getMask.getY + this.getMask.getHeight * 3 / 4 - (this.velocity.getY * 2) < obj.getMask.getY) {
        if (this.velocity.getY > 0) { // && obj.yThru <= 0) {
          this.airTime = 0 //DO NOT REMOVE
          if (!obj.isAnchored) {
            if (math.abs(obj.getVelocity.getY) <= 0.2)
              this.velocity.setX(obj.getVelocity.getX)
            else
              this.velocity = obj.getVelocity
          }
          else
            this.velocity.setY(obj.getVelocity.getY) //IT used to be 0
          this.ground = obj
          this.setY(obj.getMask.getY - this.getMask.getHeight + (deltaDimensions / 2).getY)
        }
      } else {
        this.overlap_NEW(obj)
        if (!obj.isAnchored) {
          if (mask.getY + mask.getHeight - 2 < obj.getMask.getY)
            this.velocity = this.velocity.getMax(obj.getVelocity)
        }
        else {
          //this.repulse(obj)
        }
      }
    }
  }

  def projectileCollision(projectile: Projectile): Unit ={

  }

  def collision(obj: Obj) {
    if (!this.anchored && obj.canBeTouched && this.canTouch) {
      if(obj.isInstanceOf[Projectile]){
        projectileCollision(obj.asInstanceOf[Projectile])
        return
      }
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
            this.velocity.setY(obj.getVelocity.getY) //IT used to be 0
          this.ground = obj
          this.setY(obj.getY - this.getMask.getHeight)
        }
      } else {
        this.overlap(obj)
        if (!obj.isAnchored) {
          if (this.getY + this.height - 2 < obj.getY)
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
    this.velocity = this.velocity - this.getCenter.UnitVector(intersection) //* math.sqrt(r.getHeight*r.getHeight+r.getWidth*r.getWidth))
  }

  //  def overlap_OLD(obj: Obj) {
  //    if (this.getArea <= obj.getArea && obj != this) {
  //      val r: Rectangle = this.mask.intersection(obj.getMask)
  //      val intersection: Vector2D = new Vector2D(r.getCenterX, r.getCenterY)
  //      this.Position = this.Position - this.getCenter.UnitVector(intersection) * 4 //4 since the distance from the center to the edge is half the distance from edge to edge
  //      //this.velocity = new Vector(0,0)
  //    }
  //  }

  def deltaDimensions: Vector2D = getDimensions - getMaskDimensions

  def overlap_NEW(obj: Obj) {
    val dd: Vector2D = (getDimensions - getMaskDimensions) / 2
    val odd: Vector2D = (obj.getDimensions - obj.getMaskDimensions) / 2
    if ((this.getArea <= obj.getArea || obj.isAnchored) && obj != this) {
      if (mask.getY > obj.getMask.getCenterY + obj.getMask.getHeight / 2 - mask.getHeight / 2) {
        if (this.ground != null)
          return
        this.Position.setY(obj.getMask.getY + obj.getMask.getHeight + 0 - dd.getY)
        if (this.velocity.getY < 0)
          this.velocity.setY(0)
        return
      }

      if (mask.getX <= obj.getMask.getCenterX && !leftCollision) {
        this.Position.setX(obj.getMask.getX - mask.getWidth - 0 + dd.getX)
        this.right = obj
        if (this.velocity.getX > 0)
          this.velocity.setX(0)
        return
      }
      if (mask.getX > obj.getMask.getCenterX && !rightCollision) {
        this.Position.setX(obj.getMask.getX + obj.getMask.getWidth + 0 - dd.getX)
        this.left = obj
        if (this.velocity.getX < 0)
          this.velocity.setX(0)
        return
      }

      if (mask.getY + mask.getHeight - 1 < obj.getMask.getY) {
        this.Position.setY(obj.getMask.getY - mask.getHeight - 1 + dd.getY)
        if (this.velocity.getY < 0)
          this.velocity.setY(0)
      }
    } else if (this.getArea > obj.getArea && obj != this) {

      if (mask.getX > obj.getMask.getCenterX && obj.leftCollision) {
        this.Position.setX(obj.getMask.getX + obj.getMask.getWidth + 0 - dd.getX)
        this.left = obj
        if (this.velocity.getX < 0)
          this.velocity.setX(0)
        return
      }
      if (mask.getX <= obj.getMask.getCenterX && obj.rightCollision) {
        this.Position.setX(obj.getMask.getX - mask.getWidth - 0 + dd.getX)
        this.right = obj
        if (this.velocity.getX > 0)
          this.velocity.setX(0)
        return
      }
    }

  }

  def overlap(obj: Obj) {

    if (this.getArea <= obj.getArea && obj != this) {
      if (this.getY > obj.getCenterY + obj.height / 2 - this.height / 2) {
        if (this.ground != null)
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
    } else if (this.getArea > obj.getArea && obj != this) {

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
      } else if (math.abs(this.velocity.getX) < 0.2) { //X velocity damping
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
          val direction: Vector2D = this.getCenter.UnitVector(target) * speed
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

    if ((velocity.getX > 0 && this.rightCollision) || (velocity.getX < 0 && this.leftCollision))
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
    if (ground == null) {
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
    this.getClass.getSimpleName + ":\n[\nPosition: " + this.Position + " ,\n Width: " + this.width + " ,\n Height: " + this.height + " ,\n Velocity: " + this.velocity + " ,\n Is in screen: " + isInScreen + " ,\n Ground: " + space + "(" + this.ground + ")\n]"
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

  def setRight(obj: Obj): Unit = this.right = obj

  def setLeft(obj: Obj): Unit = this.left = obj

  def isInScreen: Boolean = {
    game.isInScreen(this)
  }

  def setVelocityX(n: Double): Unit = {
    this.velocity.setX(n)
  }

  def setVelocityY(n: Double): Unit = {
    this.velocity.setY(n)
  }

  def setPositionX(n: Double): Unit = {
    this.Position.setX(n)
  }

  def setPositionY(n: Double): Unit = {
    this.Position.setY(n)
  }

  def setWidth(width: Int): Unit = {
    this.width = width
  }

  def setHeight(height: Int): Unit = {
    this.height = height
  }

  def getWidth: Int = this.width

  def getHeight: Int = this.height

  def setDimensions(dimensions: Vector2D): Unit = {
    this.setWidth(dimensions.getX.toInt)
    this.setHeight(dimensions.getY.toInt)
  }

  def loadImage(path: String): BufferedImage = {
    val file: File = new File("src\\" + path)
    if (file.exists()) {
      return ImageIO.read(file)
    }
    null
  }

  def getDimensions: Vector2D = new Vector2D(width, height)

  def destroy(): Unit ={
    Main.getGame.currentLevel.removeObject(this)
  }

}