package mainPPE

import java.awt._

import javax.swing._

class Component extends JComponent {

  var levelList = new ArrayList[Level]()
  var level: Level = _
  var Speed: Integer = 10
  val DebugMode: Boolean = true
  var objInfo: Obj = _
  var Player: Obj = _
  var shift: Vector2D = new Vector2D(0, 0)

  def mousePosition: Vector2D = {
    new Vector2D(MouseInfo.getPointerInfo.getLocation) - new Vector2D(this.getLocationOnScreen)
  }

  def loadLevel(level: Level) {
    this.level = level
    this.Speed = level.getLevelSpeed
    this.level.startLevel()
  }

  def tick() {
    if (this.level != null) {
      if (this.Player != null)
        shift = this.Player.getPosition * (-1) + (this.getWidth / 2, this.getHeight / 2)
      else
        shift = new Vector2D(0, 0)
      for (i <- this.level.getObjects.indices) {
        val obj1: Obj = this.level.getObject(i)
        if (obj1 != null) {
          val mousepos: Point = (mousePosition - shift).toPoint()
          if (mousepos != null) {
            if (obj1.getMask.contains(mousepos)) {
              this.objInfo = obj1
            }
          }
          if (obj1.isPossessed && this.Player != obj1)
            this.Player = obj1
          obj1.tick()
          if (obj1.isSolid)
            for (j <- i until this.level.getObjects.length) {
              val obj2: Obj = this.level.getObject(j)
              if (obj2 != null)
                if (obj1 != obj2)
                  if (obj1.intersects(obj2) || obj2.intersects(obj1)) {
                    obj1.collision(obj2)
                    obj2.collision(obj1)
                  } else {
                    if (obj1.getGround == obj2)
                      obj1.setGround(null)
                    if (obj2.getGround == obj1)
                      obj2.setGround(null)
                  }
            }
        }
      }
    }
  }

  def getLevel: Level = {
    this.level
  }

  override def paintComponent(g: Graphics) {
    super.paintComponent(g)
    val g2: Graphics2D = g.asInstanceOf[Graphics2D]
    this.tick()
    val mouse = mousePosition //Incase it changes while the code below loads
    if (this.level != null) {
      this.level.tick()
      for (i <- this.level.getObjects.indices) {
        val obj: Obj = this.level.getObject(i)
        if (obj != null) {
          obj.drawObj(g, this, shift)
          //println(this.get)
        }
      }
      if (mouse != null && this.DebugMode) {
        g.fillRect(mouse.getX.toInt, mouse.getY.toInt, 10, 10)
        if (objInfo != null) {
          g.setColor(Color.green)
          g2.draw(new Line(objInfo.getPosition + shift, objInfo.getPosition + objInfo.getMaskDimensions + shift))
          g.setColor(Color.blue)
          LineSplitter.draw(objInfo.toString,40,20,g)
          //g.drawString(objInfo.toString(), 40, 20)
          if (objInfo.getGround != null) {
            g2.setColor(Color.red)
            g2.draw(new Line(objInfo.getCenter + shift, objInfo.getGround.getCenter + shift))
            //g2.drawString("Currently on top of " + objInfo.getGround.toString(), 40, 60)
          }
        }
      }
    }
  }
}

object Main {
  def main(args: Array[String]) {
    val frame: JFrame = new JFrame()
    //var input: Input = new Input()
    val comp: Component = new Component()
    //   var timer: Int = 0
//    var changed: Boolean = false
    frame.setSize(800, 800)
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    frame.add(comp)
    frame.setVisible(true)
    frame.addKeyListener(Input)
    frame.addMouseListener(MouseClicks)
    // frame.addMouseMotionListener(MouseMotion)


//    var box: Box = new Box(200, 100, 50, 50) //This is for testing purposes
//    var box2: Box = new Box(0, 300, 1000, 20)//This is for testing purposes
//    var box3: Box = new Box(300, 50, 50, 50)//This is for testing purposes
//    var box4: Box = new Box(400, 100, 50, 50)//This is for testing purposes
//    var wall1: Box = new Box(0, 300-50, 50, 50)//This is for testing purposes
//    var wall2: Box = new Box(400, 300-50, 50, 50)//This is for testing purposes
//    var player: Player = new Player(300,10)
//    box3.setAnchored(false)//This is for testing purposes
//
//    val lvl: Level = new Level {}//This is for testing purposes
//    lvl.addObject(box2,box3,box4,player,wall1,wall2)//This is for testing purposes
//    lvl.startLevel()//This is for testing purposes
    comp.loadLevel(new Level1)
    while (true) {
      frame.repaint()
      try {
        Thread.sleep(1000 / 100)
      } catch {
        case e: Exception => e.printStackTrace();
      }
    }
  }
}