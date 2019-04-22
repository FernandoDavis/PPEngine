package mainPPE
import Objects.Box

class Level2 extends Level{
  this.LevelNumber=2;
  override def startLevel(){
    super.startLevel()
    var box: Box = new Box(300,500)
    //box.MaskColor=Color.RED
    //this.Objects(0)=(box)
  }
  
}