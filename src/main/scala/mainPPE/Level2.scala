package mainPPE
import java.awt.Color

import Entities.Player
import Objects.Box

//Slide on box, and Climb!
class Level2 extends Level{

  var box: Box = new Box(200, 100, 50, 50)
  var floor: Box = new Box(340, 300, 400, 20)


  var Airbox: Box = new Box(400, 100, 50, 50)
  var Airbox2: Box = new Box(680, 100, 50, 50)

  this.StartPosition = new Vector2D(300, 10)

  var platform7: Box = new Box(460, -220, 160, 20)


  var platform6: Box = new Box(220, -115, 160, 20)


  var platform5: Box = new Box(300, 10, 220, 20)


  var platform4: Box = new Box(700, -15, 120, 20)


  var platform: Box = new Box(850, 350, 250, 20)


  var Slidebox: Box = new Box(550, 40, 50, 50)


  var platform3: Box = new Box(900, 75, 200, 20)


  var platform2: Box = new Box(1260, 160, 300, 20)




  override def startLevel() {
    super.startLevel()
    Airbox2.setMaskColor(Color.ORANGE)
    floor.setMaskColor(Color.black)
    Slidebox.setMaskColor(Color.green)
    platform7.setMaskColor(Color.GREEN)
    platform6.setMaskColor(Color.GREEN)
    platform5.setMaskColor(Color.GREEN)
    platform4.setMaskColor(Color.BLACK)
    platform3.setMaskColor(Color.BLACK)
    platform2.setMaskColor(Color.BLACK)
    platform.setMaskColor(Color.BLACK)

    Slidebox.setAnchored(false)
    this.addObject(floor,Slidebox,Airbox,Airbox2,player,platform,platform2, platform3, platform4, platform5, platform6, platform7)
    this.addPlayer(this.getStartPosition)

  }
  override def tick(){
    super.tick()
  }

}
