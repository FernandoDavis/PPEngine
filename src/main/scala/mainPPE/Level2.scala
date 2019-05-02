package mainPPE
import java.awt.Color

import Entities.Player
import Objects.Box
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.io.File


//Slide on box, and Climb!
class Level2 extends Level{

  var brick: BufferedImage = ImageIO.read(new File("src\\IMAGES/BRICK50X50.jpg"))
  var box: Box = new Box(200, 100, 50, 50)
  var floor: Box = new Box(340, 300, 400, 20)
  var Airbox: Box = new Box(400, 100, 50, 50)
  var Airbox2: Box = new Box(680, 100, 50, 50)
  var Airbox3: Box = new Box(350, 130, 60, 60)
  this.StartPosition = new Vector2D(550, 10)
  var platform7: Box = new Box(460, -220, 160, 20)
  var platform6: Box = new Box(220, -115, 160, 20)
  var platform5: Box = new Box(300, 10, 220, 20)
  var platform4: Box = new Box(700, -15, 120, 20)
  var platform: Box = new Box(850, 350, 250, 20)
  var Slidebox: Box = new Box(550, 40, 100, 50)
  var platform3: Box = new Box(900, 75, 200, 20)
  var platform2: Box = new Box(1260, 160, 300, 20)
  var boxHere: Box = new Box(1050, 300, 50, 50)




  override def startLevel() {
    super.startLevel()
    Airbox2.setMaskColor(Color.ORANGE)
    Airbox3.setMaskColor(Color.pink)
    floor.setMaskColor(Color.BLUE)
    Slidebox.setMaskColor(Color.green)
    platform7.setMaskColor(Color.GREEN)
    platform6.setMaskColor(Color.BLUE)
    platform5.setMaskColor(Color.BLUE)
    platform4.setMaskColor(Color.BLUE)
    platform3.setMaskColor(Color.BLUE)
    platform2.setMaskColor(Color.BLUE)
    platform.setMaskColor(Color.BLUE)
    boxHere.setMaskColor(Color.GRAY)
    boxHere.canBeTouched=false
    boxHere.setPassable(true)
    Airbox.canBeTouched=false
    Airbox2.canBeTouched=false
    Airbox3.canBeTouched=false
    Airbox.setAnchored(false)
    Airbox2.setAnchored(false)
    Airbox3.setAnchored(false)
    Airbox.setSpeed(1.5)
    Airbox2.setSpeed(1.5)
    Airbox3.setSpeed(2.5)
    Airbox.setPersonality(Behaviour.randomFly)
    Airbox2.setPersonality(Behaviour.randomFly)
    Airbox3.setPersonality(Behaviour.randomFly)
    Slidebox.setAnchored(false)
    Slidebox.setImg(brick) //SET IMAGE TEXTURE
    this.addObject(floor,Slidebox,Airbox,Airbox2,Airbox3,player,platform,platform2, platform3, platform4, platform5, platform6, platform7, boxHere)
    this.addPlayer(this.getStartPosition)

  }
  override def tick(){
    super.tick()
  }
}
