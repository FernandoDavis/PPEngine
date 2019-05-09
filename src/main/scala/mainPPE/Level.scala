package mainPPE

import java.awt.image.BufferedImage

import Entities.Player

abstract class Level {
  //var input = Input
  //protected var Objects: Array[Obj] = new Array[Obj](255)
//  protected var Objects: ArrayList[Obj] = new ArrayList[Obj](255)
  protected var Objects: LinkedList[Obj] = new LinkedList[Obj]
  protected var StartPosition: Vector2D = new Vector2D(300, 300)
  protected var LevelSpeed: Integer = 10
  protected var nextLvl: Level = null
  private var time: Int = 0
  private var busy: Boolean = false
  private var Timers: Array[Int] = new Array[Int](20)
  private var LevelNumber: Int = 0
  protected var player: Player = null
  protected var deathY: Int = 500
  protected var music: Sound = new Sound("Sounds/overworld3.wav")
  music.setVolumeSuppression(0.5f)
  protected var background: BufferedImage = null
  protected var backgroundOffset: Vector2D = new Vector2D(0,0)
  music.setLoop(-1)

  def getDeathY: Int = this.deathY

  def getBackground: BufferedImage =background
  def getBackgroundOffset: Vector2D = backgroundOffset
  def getMusic: Sound = music

  def clearObjects(): Unit ={
    println("Cleared all objects")
    Objects.clear()
  }

  def setDeathY(int: Int) {
    this.deathY = int
  }

  def addPlayer() {
    this.addPlayer(this.getStartPosition)
  }

  def addPlayer(v: Vector2D) {
    this.player = new Player(v)
    this.addObject(player)
  }

  def getIndex: Int = LevelNumber

  def changeIndex(index: Int) {
    this.LevelNumber = index
  }

  def startLevel() {
    music.play()
    time = 0
  }

  def addObject(obj: Obj) {
    this.Objects.add(obj)
  }
  def removeObject(obj: Obj) {
    this.Objects.remove(obj)
  }

  def addObject(objs: Obj*) {
    objs.foreach(this.Objects.add(_)) //Hmm it didnt let me use this.Objects.add(objs) even tho the function for it is defined
  }

  def getObjects: LinkedList[Obj] = {
    this.Objects
  }

  def getLevelSpeed: Int = {
    this.LevelSpeed
  }

  def getStartPosition: Vector2D = {
    this.StartPosition
  }

  def getObject(index: Int): Obj = {
    this.Objects.get(index)
  }

  def goal(): Boolean = {
    false //NOT YET IMPLEMENTED
  }

  def tick() {
    this.time += 1
  }

  def goToNextLevel(): Unit = {
    if (Main.getGame == null)
      return //wtf
    val level: Level = Main.getGame.nextLevel
    if (level != null)
      Main.getGame.loadLevel(level)
  }

  def goToPrevLevel(): Unit = {
    if (Main.getGame == null)
      return //wtf
    val level: Level = Main.getGame.prevLevel
    if (level != null)
      Main.getGame.loadLevel(level)
  }

  def goToLevelPreviouslyIn(): Unit = {
    if (Main.getGame == null)
      return //wtf lol
    val level: Level = Main.getGame.b4Level
    if (level != null)
      Main.getGame.loadLevel(level)
  }

  def getTime: Int = time

}