package mainPPE

import Entities.Player

import scala.util.Random

object Behaviour {

  def randomFly: Behaviour = {
    new Behaviour() {
      delay = 1000
      val index = 0

      override def run(): Unit = {
        super.run()
        if (System.currentTimeMillis() - timer > delay) {
          timer = System.currentTimeMillis()
          if (owner != null)
            if (owner.getGravity != 0)
              owner.setGravity(0)
          val rand: Random = new Random()
          rand.setSeed(System.nanoTime() + System.currentTimeMillis())
          var x = rand.nextInt(500)
          if (rand.nextBoolean())
            x *= -1
          rand.setSeed(System.nanoTime() + System.currentTimeMillis() + 2)
          var y = rand.nextInt(500)
          if (rand.nextBoolean())
            y *= -1
          this.owner.setTargetPosition(this.owner.startPosition + (x, y))
        }
      }
    }
  }

  def followPlayer: Behaviour = {
    new Behaviour() {
      delay = 300
      var lostIt: Boolean = true
      val index = 1

      override def run(): Unit = {
        super.run()
        if (System.currentTimeMillis() - timer > delay) {
          timer = System.currentTimeMillis()
          val player: Obj = Main.getGame.getPlayer
          if (owner != null)
            if (player != null) {
              if (player.getPosition.distance(owner.getPosition) <= 200) {
                lostIt = false
                this.owner.setTargetObj(player)
              }
              else if (this.owner.getTargetObj == player) {
                this.owner.setTargetObj(null)
                this.owner.setTargetPosition(null)
              } else {
                if (secondary.index == 6 && !lostIt) {
                  lostIt=true
                  secondary.doubleParam2 = owner.getCenterX
                  secondary.doubleParam3 = owner.getCenterY
                }
                runSecondary()
              }
            }
        }
      }
    }
  }

  def constantVelocity(x: Double, y: Double): Behaviour = {
    constantVelocity(new Vector2D(x, y))
  }

  def constantVelocity(v: Vector2D): Behaviour = {
    new Behaviour() {
      val index = 2
      delay = 0
      doubleParam1 = v.getX
      doubleParam2 = v.getY

      override def run(): Unit = {
        super.run()
        if (owner != null) {
          owner.setGravity(0)
          owner.setVelocity(dp1, dp2)
        }
      }
    }
  }

  def jump: Behaviour = {
    new Behaviour() {
      delay = 200
      val index = 3

      override def run(): Unit = {
        super.run()
        if (System.currentTimeMillis() - timer > delay) {
          timer = System.currentTimeMillis()
          if (owner != null)
            if (owner.leftCollision || owner.rightCollision) {
              owner.jump(owner.getSpeed * 1.2)
            }
        }
      }
    }
  }

  def attackPlayer(damage: Int): Behaviour = {
    new Behaviour() {
      val index = 4
      delay = 300
      activation = activationType.collision
      doubleParam1 = damage

      override def collision(obj: Obj) {
        super.collision(obj)
        if (System.currentTimeMillis() - timer > delay) {
          timer = System.currentTimeMillis()
          if (obj.isInstanceOf[Player]) {
            val player = obj.asInstanceOf[Player]
            player.takeDamage(dp1)
          }
        }
      }
    }
  }

  def moveBackAndForthH(distance: Double): Behaviour = {
    new Behaviour() {
      val index = 5
      delay = 300
      var next: Vector2D = new Vector2D(0, 0)
      doubleParam1 = distance

      override def setOwner(owner: Obj): Unit = {
        if (owner != null) {
          super.setOwner(owner)
          next = new Vector2D(owner.startPosition.getX, owner.getCenterY) + (this.dp1 / 2, 0)
          owner.setTargetDistance(5)
        }
      }

      var h: Int = 1

      override def run(): Unit = {
        super.run()
        if (System.currentTimeMillis() - timer > delay) {
          timer = System.currentTimeMillis()
          if (owner.getTargetPos == null) {
            owner.setTargetPosition(next)
            h *= -1
            next = new Vector2D(owner.startPosition.getX, owner.getCenterY) + (h * distance / 2, 0)
          }
          else
            owner.setTargetPosition(new Vector2D(owner.getTargetPos.getX, owner.getCenterY))
        }
      }
    }
  }

  def moveBackAndForthH(distance: Double, x: Double, y:Double): Behaviour ={
    this.moveBackAndForthH(distance,new Vector2D(x,y))
  }

  def moveBackAndForthH(distance: Double, aroundPosition: Vector2D): Behaviour = {
    new Behaviour() {
      val index = 6
      delay = 300
      var next: Vector2D = new Vector2D(0, 0)
      doubleParam1 = distance
      doubleParam2 = aroundPosition.getX
      doubleParam3 = aroundPosition.getY

      override def setOwner(owner: Obj): Unit = {
        if (owner != null) {
          super.setOwner(owner)
          next = new Vector2D(dp2, owner.getCenterY) + (this.dp1 / 2, 0)
          owner.setTargetDistance(5)
        }
      }
      val rand = new Random()
      rand.setSeed(System.nanoTime()*2+System.currentTimeMillis()*3)


      var h: Int = 1
      if(rand.nextBoolean())
        h= -1

      override def run(): Unit = {
        super.run()
        if (System.currentTimeMillis() - timer > delay) {
          timer = System.currentTimeMillis()
          if (owner.getTargetPos == null) {
            owner.setTargetPosition(next)
            h *= -1
            next = new Vector2D(dp2, owner.getCenterY) + (h * distance / 2, 0)
          }
          else
            owner.setTargetPosition(new Vector2D(owner.getTargetPos.getX, owner.getCenterY))
        }
      }
    }
  }

  object activationType extends Enumeration {
    type activationType = Value
    val collision, default, takeDamage = Value
  }

}


abstract class Behaviour() {

  var activation: Behaviour.activationType.Value = Behaviour.activationType.default
  var owner: Obj = null
  var delay: Double = 1000
  var timer: Long = 0
  protected var doubleParam1: Double = 0
  protected var doubleParam2: Double = 0
  protected var doubleParam3: Double = 0
  protected var objParam1: Obj = null
  protected var objParam2: Obj = null
  protected var objParam3: Obj = null
  protected var secondary: Behaviour = null


  protected def dp1: Double = this.doubleParam1

  protected def dp2: Double = this.doubleParam2

  protected def dp3: Double = this.doubleParam3

  protected def op1: Obj = this.objParam1

  protected def op2: Obj = this.objParam2

  protected def op3: Obj = this.objParam3


  val index: Int

  def this(owner: Obj) {
    this()
    this.owner = owner
  }

  def collision(obj: Obj): Unit = {
    if (obj == owner)
      return
  }

  def ~(behaviour: Behaviour): Behaviour = {
    if (behaviour.owner == null)
      behaviour.setOwner(owner)
    else if (this.owner == null && behaviour.owner != null)
      this.setOwner(behaviour.owner)
    this.secondary = behaviour
    this
  }

  def runSecondary(): Unit = {
    if (secondary != null)
      this.secondary.run()
  }

  def +(behaviour: Behaviour): Personality = {
    if (behaviour.owner == null)
      behaviour.setOwner(owner)
    else if (this.owner == null && behaviour.owner != null)
      this.setOwner(behaviour.owner)
    val p = new Personality(owner)
    p += this
    p += behaviour
    p
  }


  def setOwner(owner: Obj): Unit = {
    if (owner == null)
      return
    this.owner = owner
    if (secondary != null)
      this.secondary.setOwner(owner)
  }

  override def clone(): Behaviour = {
    index match {
      case 0 => return Behaviour.randomFly
      case 1 => return Behaviour.followPlayer
      case 2 => return Behaviour.constantVelocity(dp1, dp2)
      case 3 => return Behaviour.jump
      case 4 => return Behaviour.attackPlayer(dp1.toInt)
      case 5 => return Behaviour.moveBackAndForthH(dp1)
      case 6 => return Behaviour.moveBackAndForthH(dp1,dp2,dp3)
      case _ => throw new NullPointerException("The behaviour with index "+index+" has not been added to the clone list")
    }
    null
  }

  def setDelay(d: Double): Unit = {
    this.delay = d
  }

  def getDelay: Double = delay

  def run(): Unit = {
    if (owner == null)
      return
  }

}
