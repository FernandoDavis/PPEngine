package mainPPE

import java.awt.Color

import Entities.Player
import Objects.Box

class Level1 extends Level {
  var box: Box = new Box(200, 100, 50, 50)
  var floor: Box = new Box(0, 300, 1000, 20)
  var box3: Box = new Box(300, 50, 50, 50)
  var box4: Box = new Box(400, 100, 50, 50)
  var box5: Box = new Box(600, 200, 50, 50)
  var wall1: Box = new Box(0, 300 - 50, 50, 500)
  var wall2: Box = new Box(400, 300 - 50, 50, 500)
  var wall3: Box = new Box(1000, 300 - 500, 50, 500)
  var wall4: Box = new Box(1000 - 100, 300 - 500 - 50, 50, 500)
  this.StartPosition = new Vector2D(300, 10)

  override def startLevel() {
    super.startLevel()
    box3.canBeTouched=false
    box3.setAnchored(false)
    box3.setSpeed(3)
    box3.setBehaviour(Behaviour.randomFly(box3))
    box5.setMaskColor(Color.RED)
    this.addObject(floor, box3, box4, wall1, wall2, wall3, wall4, box5)
    this.addPlayer(this.getStartPosition)

  }

  override def tick() {
    super.tick()
  }

}