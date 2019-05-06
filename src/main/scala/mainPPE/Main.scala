package mainPPE

import java.awt._
import java.awt.event.KeyEvent

import Entities.Player
import javax.swing._

class Component extends JComponent {

  private var levelList = new ArrayList[Level]()
  private var level: Level = null
  var speed: Integer = 10
  val DebugMode: Boolean = true
  var objInfo: Obj = null
  var player: Obj = null
  private var shift: Vector2D = new Vector2D(0, 0)
  private var previousLevel: Int = 0

  def isInScreen(obj: Obj): Boolean = {
    val pos = obj.getPosition + shift
    pos.getX + obj.getWidth >= 0 && pos.getX <= this.getWidth && pos.getY + obj.getHeight >= 0 && pos.getY <= this.getHeight
  }

  def getScreenShift: Vector2D = {
    if (player != null)
      return shift//player.getCenter + (this.getWidth / 2, this.getHeight / 2)
    new Vector2D(0, 0)
  }

  def mousePosition: Vector2D = {
    new Vector2D(MouseInfo.getPointerInfo.getLocation) - new Vector2D(this.getLocationOnScreen)
  }

  def loadLevel(level: Level) {
    if (this.level != null)
      this.previousLevel = this.level.getIndex
    this.level = level
    this.speed = level.getLevelSpeed
    this.level.startLevel()
  }

  def reloadLevel(): Unit = {
    this.level.clearObjects()
    this.level.startLevel()
    this.level.getObjects.foreach(_.reset())
  }

  def tick() {
    if (this.level != null) {
      if (this.player != null) {
        shift = this.player.getCenter * (-1) + (this.getWidth / 2, this.getHeight / 2)
      }
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
          if (obj1.isPossessed && this.player != obj1)
            this.player = obj1
          obj1.tick()
          if (obj1.isSolid)
            for (j <- (i until this.level.getObjects.length).par) { //parallelized
              val obj2: Obj = this.level.getObject(j)
              if (obj2 != null)
                if (obj1 != obj2)
                  if (obj1.intersects(obj2) || obj2.intersects(obj1)) {
                    obj1.getPersonality.runCollisionBehaviours(obj2)
                    obj2.getPersonality.runCollisionBehaviours(obj1)
                    obj1.collision_NEW(obj2)
                    obj2.collision_NEW(obj1)
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
    if (Input.keys(KeyEvent.VK_N)) {
      this.level.goToNextLevel()
    }
  }


  def currentLevel: Level = {
    this.level
  }

  def addLevel(level: Level) {
    if (level == null)
      return
    level.changeIndex(this.levelList.size)
    this.levelList += level
  }

  def addLevel(levels: Level*) {
    if (levels == null)
      return
    levels.foreach(addLevel)
  }

  def nextLevel: Level = {
    val next: Int = level.getIndex + 1
    if (next < 0 || next > this.levelList.size - 1)
      return null
    this.levelList.get(next)
  }

  def prevLevel: Level = {
    val prev: Int = level.getIndex - 1
    if (prev < 0 || prev > this.levelList.size - 1)
      return null
    this.levelList.get(prev)
  }

  def b4Level: Level = {
    if (this.previousLevel < 0 || this.previousLevel > this.levelList.size - 1)
      return null
    this.levelList.get(this.previousLevel)
  }

  def getPlayer: Obj = this.player

  def start(): Unit = {
    if (this.level == null)
      this.loadLevel(this.levelList.get(0))
  }

  override def paintComponent(g: Graphics) {
    super.paintComponent(g)
    val g2: Graphics2D = g.asInstanceOf[Graphics2D]
    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
    this.tick()
    val mouse = mousePosition //Incase it changes while the code below loads
    if (this.level != null) {
      val deathY = level.getDeathY + shift.getY.toInt
      val grad = new GradientPaint(0, deathY - 10, Color.lightGray, 0, deathY + 40, Color.gray)
      if (deathY < this.getHeight)
        g2.setPaint(grad)
      else
        g2.setPaint(Color.lightGray)
      g2.fillRect(0, 0, this.getWidth, this.getHeight)
      this.level.tick()
      for (i <- this.level.getObjects.indices) {
        val obj: Obj = this.level.getObject(i)
        if (obj != null) {
          if (isInScreen(obj))
            obj.drawObj(g2, this, shift)
        }
      }
      if (mouse != null && this.DebugMode) {
        //g.fillRect(mouse.getX.toInt, mouse.getY.toInt, 10, 10)
        if (objInfo != null) {
          g.setColor(Color.green)
          g2.draw(new Line(objInfo.getPosition + shift, objInfo.getPosition + objInfo.getDimensions + shift))
          //g.setColor(Color.GREEN)
          g.drawRect(objInfo.getMask.getX.toInt+shift.getX.toInt,objInfo.getMask.getY.toInt+shift.getY.toInt,objInfo.getMask.getWidth.toInt,objInfo.getMask.getHeight.toInt)
          g.setColor(Color.blue)
          g.drawRect(objInfo.getHitBox.getX.toInt+shift.getX.toInt,objInfo.getHitBox.getY.toInt+shift.getY.toInt,objInfo.getHitBox.getWidth.toInt,objInfo.getHitBox.getHeight.toInt)
          LineSplitter.draw(objInfo.toString, 40, 20, g)
          //g.drawString(objInfo.toString(), 40, 20)
          if (objInfo.getGround != null) {
            g2.setColor(Color.red)
            g2.draw(new Line(objInfo.getCenter + shift, objInfo.getGround.getCenter + shift))
            //g2.drawString("Currently on top of " + objInfo.getGround.toString(), 40, 60)
          }
          if (objInfo.getTargetPos != null || objInfo.getTargetObj != null) {
            var target: Vector2D = objInfo.getTargetPos
            if (objInfo.getTargetObj != null)
              target = objInfo.getTargetObj.getCenter
            g.setColor(Color.GREEN)
            g2.draw(new Line(objInfo.getCenter + shift, target + shift))
            g2.draw(new Line(target + shift + (20 * math.cos(Main.currentTime / 500.0), 20 * math.sin(Main.currentTime / 500.0)), target + shift + (20 * math.cos(Main.currentTime / 500.0 + math.Pi), 20 * math.sin(Main.currentTime / 500.0 + math.Pi))))
            g2.draw(new Line(target + shift + (20 * math.cos(Main.currentTime / 500.0 + math.Pi / 2), 20 * math.sin(Main.currentTime / 500.0 + math.Pi / 2)), target + shift + (20 * math.cos(Main.currentTime / 500.0 + math.Pi + math.Pi / 2), 20 * math.sin(Main.currentTime / 500.0 + math.Pi + math.Pi / 2))))
            g.setColor(Color.BLACK)
            g2.drawString("Target Position", (target.getX + shift.getX).toInt, (target.getY + shift.getY).toInt)
          }
        }
        // g.setColor(Color.red)
        // g.drawLine(0, (currentLevel.getDeathY + shift.getY).toInt, this.getWidth, (currentLevel.getDeathY + shift.getY).toInt)
      }

    }

  }
}

object Main {
  private val game: Component = new Component()

  def currentTime: Long = System.currentTimeMillis()

  def getGame: Component = game

  var objArray: ArrayList[ArrayList[Obj]] = new ArrayList[ArrayList[Obj]](255)
  objArray.add(new ArrayList[Obj](255))
  var currentLevelIndex: Int = 0

  def run() {
    val frame: JFrame = new JFrame()
    //var input: Input = new Input()
    //    game = new Component()
    //   var timer: Int = 0
    //    var changed: Boolean = false
    frame.setSize(800, 800)
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    frame.add(game)
    frame.setVisible(true)
    frame.addKeyListener(Input)
    frame.addMouseListener(MouseClicks)
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
    //    game.addLevel(new Level1, new Level2)
    game.start()
    //game.loadLevel(new Level1)
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