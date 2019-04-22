package mainPPE

abstract class Entity(x: Int, y: Int, w: Int, h: Int) extends Obj(x: Int, y: Int, w: Int, h: Int) {
  protected var Health: Double = 100
  protected var Lives: Int = 0
  protected var MaxHealth: Double = 100
  this.canBeTouched=false

  def death()

  //def behaviour()

  def getHealth: Double = Health

  def jump(): Unit = this.jump(this.getSpeed*1.2)

  def setHealth(newHealth: Double) {
    if (newHealth <= 0) {
      this.Health = 0
      this.death()
    }
    else if (newHealth > this.MaxHealth)
      this.Health = MaxHealth
    else
      this.Health = newHealth
  }

  def setLives(newLives: Int) {
    if (newLives < 0)
      this.Lives = 0
    else
      this.Lives = newLives
  }

  def getLives: Int = Lives

  def takeDamage(damage: Double) {
    val newHealth: Double = this.Health - damage
    if (newHealth <= 0) {
      this.Health = 0
      this.death()
    }
    else
      this.Health = newHealth
  }

  override def tick(): Unit = {
    super.tick()
    if(left!=null){
      if(left.getY>this.getY+this.height-1||left.getY+left.height<this.getY||left.getX+left.width<this.getX-this.speed){
        left=null
      }
    }
    if(right!=null){
      if(right.getY>this.getY+this.height-1||right.getY+right.height<this.getY||right.getX>this.getX+this.width+this.speed){
        right=null
      }
    }
  }
}
