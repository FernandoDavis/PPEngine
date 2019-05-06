package mainPPE

import java.awt.Color

import Entities.{Crab1, Pichon, Player}
import Objects.Box

class Level1 extends Level {

  this.StartPosition = new Vector2D(300, 10)

  override def startLevel() {
    super.startLevel()
    val box: Box = new Box(200, 100, 50, 50)
    val floor: Box = new Box(0, 320, 1000, 100)
    val box3: Box = new Box(300, 50, 50, 50)
    val box4: Box = new Box(400, 100, 50, 50)
    val box6: Box = new Box(500, -25, 50, 50)
    val box5: Box = new Box(600, 200, 50, 50)
    val wall1: Box = new Box(0, 300 - 50, 50, 500)
    val wall2: Box = new Box(400, 300 - 50, 50, 500)
    val wall3: Box = new Box(1000, 300 - 500, 50, 500)
    val wall4: Box = new Box(1000 - 100, 300 - 500 - 50, 50, 500)
    val wall5: Box = new Box(1050 , -300, 50, 500)
    val insect: Pichon = new Pichon(300,0)
//    val crab: Crab1 = new Crab1(300,100)
//    val crab2: Crab1 = new Crab1(500,100)
    box3.setAnchored(false)
    box3.setSpeed(3)
    box3.setPersonality(Behaviour.jump+Behaviour.moveBackAndForthH(400)+Behaviour.attackPlayer(20))
    box5.setMaskColor(Color.RED)
    this.addObject(floor, box3, box4, wall1, wall2, wall3, wall4, box5,wall5,box6,insect)

//    for(i <- 0 until 10){
//      this.addObject(new Crab1(300+i*60,100))
//    }

    for(obj <- this.getObjects){
      if(obj.isInstanceOf[Box]&&obj.getPersonality.size==0){
        obj.setImg("IMAGES/zigzagbrick.png")
        obj.setTopTexture("IMAGES/grassTop.png")
        obj.setTileable(true)
        obj.setTextureFollowing(true)
      }
    }
    this.addPlayer()

  }

  override def tick() {
    super.tick()
  }

}