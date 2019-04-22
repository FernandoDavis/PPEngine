package mainPPE
import Entities.Player
import Objects.Box

class Level2 extends Level{
  var box: Box = new Box(200, 100, 50, 50)
  var floor: Box = new Box(0, 300, 1000, 20)
  var box3: Box = new Box(300, 50, 50, 50)
  var box4: Box = new Box(400, 100, 50, 50)
  var wall1: Box = new Box(0, 300-50, 50, 500)
  var wall2: Box = new Box(400, 300-50, 50, 500)
  var wall3: Box = new Box(1000, 300-500, 50, 500)
  var wall4: Box = new Box(1000-100, 300-500-50, 50, 500)

  override def startLevel() {
    super.startLevel()
    this.addObject(floor,box4,player,wall1,wall2,wall3,wall4)
    this.addPlayer(this.getStartPosition)

  }
  override def tick(){
    super.tick()
  }
  
}