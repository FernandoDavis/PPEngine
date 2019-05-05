package mainPPE

import java.awt.image.BufferedImage
import java.io.File

import javax.imageio.ImageIO

class Animation {

  def loadImage(path: String): BufferedImage = {
    val file: File = new File("src\\" + path)
    if (file.exists()) {
      return ImageIO.read(file)
    }
    null
  }

  protected var Standing: ArrayList[BufferedImage] = new ArrayList[BufferedImage]()
  protected var sIndex = 0
  protected var Moving: ArrayList[BufferedImage] = new ArrayList[BufferedImage]()
  protected var mIndex = 0
  protected var Jumping: ArrayList[BufferedImage] = new ArrayList[BufferedImage]()
  protected var jIndex = 0


  private def next(int: Int, arrayList: ArrayList[BufferedImage]): Int = {
    (int + 1) % arrayList.size
  }

  def loadImage(spriteSheet: SpriteSheet): ArrayList[BufferedImage] = {
    spriteSheet.getArrayListOfImages
  }

  def resetIndexes(): Unit = {
    mIndex = 0
    sIndex = 0
    jIndex = 0
  }

  def resetMoving(): Unit = {
    mIndex = 0
  }

  def resetJumping(): Unit = {
    jIndex = 0
  }

  def resetStanding(): Unit = {
    sIndex = 0
  }

  def setMovingAnimation(list: BufferedImage*): Unit = {
    Moving.clear()
    Moving.addSeq(list)
  }
  def setJumpingAnimation(list: BufferedImage*): Unit = {
    Jumping.clear()
    Jumping.addSeq(list)
  }
  def setStandingAnimation(list: BufferedImage*): Unit = {
    Standing.clear()
    Standing.addSeq(list)
  }

  def setMovingAnimation(list: ArrayList[BufferedImage]): Unit = {
    this.Moving = list
  }

  def setJumpingAnimation(list: ArrayList[BufferedImage]): Unit = {
    this.Jumping = list
  }

  def setStandingAnimation(list: ArrayList[BufferedImage]): Unit = {
    this.Standing = list
  }

  def getJumpingImg: BufferedImage = {
    resetStanding()
    resetMoving()
    val img = Jumping.get(jIndex)
    jIndex = next(jIndex, Jumping)
    img
  }

  def getStandingImg: BufferedImage = {
    resetJumping()
    resetMoving()
    val img = Standing.get(sIndex)
    sIndex = next(sIndex, Standing)
    img
  }

  def getMovingImg: BufferedImage = {
    resetJumping()
    resetStanding()
    val img = Moving.get(mIndex)
    mIndex = next(mIndex, Moving)
    img
  }


}
