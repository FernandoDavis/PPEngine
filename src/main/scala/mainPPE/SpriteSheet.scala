package mainPPE

import java.awt.image.BufferedImage

class SpriteSheet(img: BufferedImage) extends Iterable[BufferedImage] {

  private var DivH: Int = -1
  private var DivV: Int = -1
  private var width: Int = -1
  private var height: Int = -1
  private var number = -1
  private val images: ArrayList[BufferedImage] = new ArrayList[BufferedImage]()
  private var index: Int = 0
  private var speed: Int = 500
  private var time: Long = 0

  def this(img: BufferedImage, DivH: Int, DivV: Int, width: Int, height: Int, number: Int) {
    this(img)
    this.DivH = DivH
    this.DivV = DivV
    this.width = width
    this.height = height
    this.number = number
  }

  def setNumberOfImages(n: Int): Unit = {
    if (n <= 0)
      throw new IllegalArgumentException("The number of images cannot be less than 0")
    this.number = n
  }

  def setHorizontalDivisions(n: Int): Unit = {
    if (n <= 0)
      throw new IllegalArgumentException("The number of horizontal images cannot be less than 0")
    this.DivH = n
  }

  def setVerticalDivisions(n: Int): Unit = {
    if (n <= 0)
      throw new IllegalArgumentException("The number of vertical images cannot be less than 0")
    this.DivV = n
  }

  def setHeight(height: Int): Unit = {
    if (height <= 0)
      throw new IllegalArgumentException("The height of the images cannot be less than 0")
    this.height = height
  }

  def setWidth(width: Int): Unit = {
    if (width <= 0)
      throw new IllegalArgumentException("The width of the images cannot be less than 0")
    this.width = width
  }

  def getHorizontalDivisions: Int = DivH

  def getVerticalDivisions: Int = DivV

  def getArrayListOfImages: ArrayList[BufferedImage] = {
    if (DivH <= -1 || DivV <= -1 || height <= -1 || width <= -1 || number <= -1)
      throw new IllegalArgumentException("Something is less than 0; the divisions, dimensions or number of images")
    images.clear()
    for (i: Int <- 0 until number) {
      val x: Int = (i % DivH) * width
      val y: Int = (math.floor(i / DivH) * height).toInt
      images.add(img.getSubimage(x, y, width, height))
    }
    images
  }

  def getNext: BufferedImage = {
    val img: BufferedImage = images.get(index)
    index = (index + 1) % images.size
    img
  }

  def getPrev: BufferedImage = {
    val img: BufferedImage = images.get(index)
    index = (index + images.size - 1) % images.size
    img
  }

  def getFrame(index: Int): BufferedImage = {
    images.get(index)
  }

  def setIndex(index: Int) {
    this.index = index
  }

  def getIndex: Int = index

  def getImg: BufferedImage = {
    val img: BufferedImage = images.get(index)
    if (System.currentTimeMillis() - time > speed) {
      time = System.currentTimeMillis()
      index += 1
    }
    img
  }

  override def iterator: Iterator[BufferedImage] = images.iterator
}
