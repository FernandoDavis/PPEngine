package mainPPE

import scala.util.Random

object Behaviour {
  def randomFly(owner: Obj): Behaviour = {
    new Behaviour(owner) {
      owner.setGravity(0)
      delay=1000
      override def run(): Unit = {
        super.run()
        if(System.currentTimeMillis()-timer>delay){
          timer=System.currentTimeMillis()
          val rand: Random = new Random()
          rand.setSeed(System.nanoTime()+System.currentTimeMillis())
          var x= rand.nextInt(500)
          if(rand.nextBoolean())
            x*= -1
          rand.setSeed(System.nanoTime()+System.currentTimeMillis()+2)
          var y= rand.nextInt(500)
          if(rand.nextBoolean())
            y*= -1
          this.owner.goTo(this.owner.startPosition+(x,y))
        }
      }
    }
  }
}

abstract class Behaviour(var owner: Obj) {
  //this() = this(null)
  var delay: Double = 1000
  var timer: Long = 0

  def setOwner(owner: Obj): Unit ={
    this.owner=owner
  }
  def run(): Unit = {
    if (owner == null)
      return
  }
}
