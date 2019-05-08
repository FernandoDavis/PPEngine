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

  protected var frameSpeed: Double = 1
  protected var frames: ArrayList[BufferedImage] = new ArrayList[BufferedImage]()
  protected var index: Double = 0


  private def next(n: Double, arrayList: ArrayList[BufferedImage]): Double = {
    (n + frameSpeed) % arrayList.size
  }

  def loadImage(spriteSheet: SpriteSheet): ArrayList[BufferedImage] = {
    spriteSheet.getArrayListOfImages
  }

  def reset(): Unit = {
    index = 0
  }

  def getIndex: Int = index.toInt

  def setFrameSpeed(frameSpeed: Double): Unit ={
    this.frameSpeed=frameSpeed
  }

  def setAnimationSeq(list: Seq[BufferedImage]): Unit = {
    frames.clear()
    frames.addSeq(list)
  }

  def setAnimation(list: ArrayList[BufferedImage]): Unit = {
    this.frames = list
  }

  def setAnimation(list: BufferedImage*): Unit = {
    frames.clear()
    frames.addSeq(list)
  }



  def getImg: BufferedImage = {
    val img = frames.get(index.toInt)
    index = next(index, frames)
    img
  }

  def getFrameSpeed: Double = frameSpeed

}
