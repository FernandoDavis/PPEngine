package mainPPE

import Entities.Player

import scala.util.Random

object Behaviour {
  def randomFly: Behaviour = {
    new Behaviour() {
      delay = 1000

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

      override def run(): Unit = {
        super.run()
        if (System.currentTimeMillis() - timer > delay) {
          timer = System.currentTimeMillis()
          val player: Obj = Main.getGame.getPlayer
          if (owner != null)
            if (player != null) {
              this.owner.setTargetObj(player)
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
      delay = 0

      override def run(): Unit = {
        super.run()
        if (owner != null) {
          owner.setGravity(0)
          owner.setVelocity(v)
        }
      }
    }
  }

  def jump: Behaviour = {
    new Behaviour() {
      delay = 200

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
      delay = 300
      activation = activationType.collision
      override def collision(obj: Obj) {
        super.collision(obj)
        if (System.currentTimeMillis() - timer > delay) {
          timer = System.currentTimeMillis()
          if (obj.isInstanceOf[Player]) {
            val player = obj.asInstanceOf[Player]
            player.takeDamage(damage)
          }
        }
      }
    }
  }

  def moveBackAndForthH(distance: Double): Behaviour = {
    new Behaviour() {
      delay = 300

      var next: Vector2D = new Vector2D(0, 0)

      override def setOwner(owner: Obj): Unit = {
        if (owner != null) {
          super.setOwner(owner)
          next = new Vector2D(owner.startPosition.getX, owner.getCenterY) + (distance / 2, 0)
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

  object activationType extends Enumeration {
    type activationType = Value
    val collision, default, takeDamage = Value
  }

}


abstract class Behaviour {

  var activation: Behaviour.activationType.Value = Behaviour.activationType.default
  var owner: Obj = null
  var delay: Double = 1000
  var timer: Long = 0

  def this(owner: Obj) {
    this()
    this.owner = owner
  }

  def collision(obj: Obj): Unit = {
    if (obj == owner)
      return
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
  }

  def run(): Unit = {
    if (owner == null)
      return
  }
}
