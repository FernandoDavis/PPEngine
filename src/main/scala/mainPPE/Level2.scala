package mainPPE
import java.awt.Color

import Entities.Player
import Objects.Box

class Level2 extends Level{

  var box: Box = new Box(200, 100, 50, 50)
  var floor: Box = new Box(340, 300, 400, 20)
  floor.setMaskColor(Color.black)

  var Airbox: Box = new Box(400, 100, 50, 50)
  var Airbox2: Box = new Box(680, 100, 50, 50)
  Airbox2.setMaskColor(Color.ORANGE)
  this.StartPosition = new Vector2D(300, 10)

  var platform7: Box = new Box(460, -220, 160, 20)
  platform7.setMaskColor(Color.GREEN)

  var platform6: Box = new Box(220, -115, 160, 20)
  platform6.setMaskColor(Color.GREEN)

  var platform5: Box = new Box(300, 10, 220, 20)
  platform5.setMaskColor(Color.GREEN)

  var platform4: Box = new Box(700, -15, 120, 20)
  platform4.setMaskColor(Color.BLACK)

  var platform: Box = new Box(850, 350, 250, 20)
  platform.setMaskColor(Color.BLACK)

  var Slidebox: Box = new Box(550, 40, 50, 50)
  Slidebox.setMaskColor(Color.green)

  var platform3: Box = new Box(900, 75, 200, 20)
  platform3.setMaskColor(Color.BLACK)

  var platform2: Box = new Box(1260, 160, 300, 20)
  platform2.setMaskColor(Color.BLACK)



  override def startLevel() {
    super.startLevel()
    Slidebox.setAnchored(false)
    this.addObject(floor,Slidebox,Airbox,Airbox2,player,platform,platform2, platform3, platform4, platform5, platform6, platform7)

  }
  override def tick(){
    super.tick()
  }

}
