package mainPPE

import scala.util.Random

object Behaviour {
  def randomFly(owner: Obj): Behaviour = {
    new Behaviour(owner) {
      owner.setGravity(0)
      delay = 1000
      override def run(): Unit = {
        super.run()
        if (System.currentTimeMillis() - timer > delay) {
          timer = System.currentTimeMillis()
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

  def followPlayer(owner: Obj): Behaviour = {
    new Behaviour(owner) {
      delay = 300

      override def run(): Unit = {
        super.run()
        if (System.currentTimeMillis() - timer > delay) {
          timer = System.currentTimeMillis()
          val player: Obj = Main.getGame.getPlayer
          if (player != null) {
            this.owner.setTargetObj(player)
          }
          if (owner.leftCollision || owner.rightCollision) {
            owner.jump(owner.getSpeed*1.2)
          }
        }
      }
    }
  }

  def moveBackAndForthH(owner: Obj, distance: Double): Behaviour = {
    new Behaviour(owner) {
      delay = 300
      owner.setTargetDistance(5)
      var next: Vector2D = new Vector2D(owner.startPosition.getX,owner.getCenterY) + (distance / 2, 0)
      var h: Int = 1
      override def run(): Unit = {
        super.run()
        if (System.currentTimeMillis() - timer > delay) {
          timer = System.currentTimeMillis()
          if (owner.getTargetPos == null) {
            owner.setTargetPosition(next)
            h*= -1
            next =  new Vector2D(owner.startPosition.getX,owner.getCenterY) + (h*distance / 2, 0)
          }
          else
            owner.setTargetPosition(new Vector2D(owner.getTargetPos.getX,owner.getCenterY))
        }
      }
    }
  }
}

abstract class Behaviour(var owner: Obj) {
  //this() = this(null)
  var delay: Double = 1000
  var timer: Long = 0

  def setOwner(owner: Obj): Unit = {
    this.owner = owner
  }

  def run(): Unit = {
    if (owner == null)
      return
  }
}
