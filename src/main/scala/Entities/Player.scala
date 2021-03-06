package Entities

import java.awt.Color
import java.awt.event.KeyEvent

import mainPPE.{Entity, Input, Main, Vector2D}

class Player(x: Int, y: Int, w: Int, h: Int) extends Entity(x: Int, y: Int, w: Int, h: Int) {
  def this() = this(0, 0, 30, 30)

  def this(v: Vector2D) = this(v.getX.toInt, v.getY.toInt, 30, 30)

  def this(x: Integer, y: Integer) = this(x, y, 30, 30)

  this.MaskColor = Color.red
  this.anchored = false
  this.possessed = true
  this.setSpeed(5)
  private var jumpTime: Long = 0
  private val jumpDelay: Double = 100
  private val horizontalDelay: Double = 1000
  private var horizontalTime: Double = 0
  this.canBeTouched = true
  private var downShiftSet = 0
  private var toggleDownShift = false
  private val downShiftDelay: Int = 300
  private var downShiftTime: Long = 0

  protected var HPress: Boolean = false

  override def tick() {
    super.tick()
    if ((Input.keys(KeyEvent.VK_SPACE) || Input.keys(KeyEvent.VK_W)) && System.currentTimeMillis() - jumpTime > jumpDelay) {
      jumpTime = System.currentTimeMillis()
      if (math.abs(this.velocity.getY) <= 0.01) {
        this.airTime = 0
        this.ground = null
        this.translate(0, -4)
        this.velocity.setY(-this.getSpeed * 1.2)
      } else if (this.velocity.getY < 10 && this.ground == null) {
        if (left != null && Input.keys(KeyEvent.VK_D)) {
          horizontalTime = System.currentTimeMillis()
          this.velocity.setX(this.getSpeed)
          this.airTime = 0
          this.velocity.setY(-this.getSpeed * 0.6)
        }
        else if (right != null && Input.keys(KeyEvent.VK_A)) {
          horizontalTime = System.currentTimeMillis()
          this.velocity.setX(-this.getSpeed)
          this.airTime = 0
          this.velocity.setY(-this.getSpeed * 0.6)
        }

      }
    }
    // def diff = this.getSpeed - math.abs(this.velocity.getX) <<-this is gay af

    val diff: Double = math.abs(math.abs(this.getVelocity.getX) - this.getSpeed)
    var mov: Double = this.getSpeed

    if (diff < this.getSpeed)
      mov = diff

    if (Input.keys(KeyEvent.VK_A)) {
      this.HPress = true
      this.noFriction = true
      if (!(System.currentTimeMillis() - horizontalTime < horizontalDelay && ground == null)) {
        if (this.velocity.getX > 0)
          this.velocity.translate(-this.getSpeed, 0)
        else if (this.velocity.getX > -this.getSpeed) {
          if (left != null) {
            if (this.left.getX + this.left.getWidth < this.getX - mov)
              this.velocity.translate(-mov, 0)
          }
          else
            this.velocity.translate(-mov, 0)
        }
      }
    }

    if (Input.keys(KeyEvent.VK_D)) {
      this.HPress = true
      this.noFriction = true
      if (!(System.currentTimeMillis() - horizontalTime < horizontalDelay && ground == null)) {
        if (this.velocity.getX < 0)
          this.velocity.translate(this.getSpeed, 0)
        else if (this.velocity.getX < this.getSpeed) {
          if (right != null) {
            if (this.right.getX > this.getX + this.width + mov)
              this.velocity.translate(mov, 0)
          }
          else
            this.velocity.translate(mov, 0)
        }
      }
    }

    if (Input.isPressing(KeyEvent.VK_S)) {
      if (ground == null)
        this.setVelocityY(this.getVelocity.getY + 2)
      else {
        if (Main.getGame.downShift < 300 && Input.getTime(KeyEvent.VK_S) > 5) {
          Main.getGame.downShift += 5
        }
      }
    }
    else {
      if (Main.getGame.downShift > downShiftSet)
        Main.getGame.downShift -= 10
      else if (Main.getGame.downShift < downShiftSet)
        Main.getGame.downShift = downShiftSet
    }

    if(toggleDownShift){
      downShiftSet=Main.getGame.downShift
    }
    else{
      downShiftSet=0
    }

    if(Input.isPressing(KeyEvent.VK_L)&&System.currentTimeMillis()-downShiftTime>downShiftDelay){
      downShiftTime=System.currentTimeMillis()
      toggleDownShift= !toggleDownShift
    }

    if (!(Input.keys(KeyEvent.VK_D) || Input.keys(KeyEvent.VK_A))) {
      this.noFriction = false
      if (this.getGround == null && math.abs(this.getVelocity.getX) <= this.getSpeed && HPress == true) {
        this.velocity.setX(this.velocity.getX * 0.5)
        this.HPress = false
      }

    }

    if (this.getCenterY >= currentLevel.getDeathY)
      this.death()
  }

  override def reset() {
    super.reset()
    this.setHealth(this.getMaxHealth)
  }

  override def death(): Unit = {
    Main.getGame.reloadLevel()
  }

}
