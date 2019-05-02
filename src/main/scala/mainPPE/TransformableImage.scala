package mainPPE

import java.awt.{Graphics, Image}
import java.awt.geom.AffineTransform
import java.awt.image.{AffineTransformOp, BufferedImage, ImageObserver, ImageProducer}

class TransformableImage {

  private var image: BufferedImage = null
  private var originalImage: BufferedImage = null

  def this(image: BufferedImage) {
    this()
    this.image = image
    this.originalImage = cloneImage(image)
  }

  def getWidth: Int = image.getWidth()
  def getHeight: Int = image.getHeight()

  def rotate(degrees: Double) {
    val rotation = Math.toRadians(degrees)
    val CenterX = image.getWidth / 2
    val CenterY = image.getHeight / 2
    val tx = AffineTransform.getRotateInstance(rotation, CenterX, CenterY)
    val operation = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR)
    val newImage = new BufferedImage(image.getWidth, image.getHeight, image.getType)
    operation.filter(image, newImage)
    this.image = newImage
  }

  def flipImageX() {
    val tx = AffineTransform.getScaleInstance(-1, 1)
    tx.translate(-image.getWidth(null), 0)
    val op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR)
    val newImage = new BufferedImage(image.getWidth, image.getHeight, image.getType)
    op.filter(image, newImage)
    this.image = newImage
  }

  def flipImageY() {
    val tx = AffineTransform.getScaleInstance(1, -1)
    tx.translate(-image.getWidth(null), 0)
    val op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR)
    val newImage = new BufferedImage(image.getWidth, image.getHeight, image.getType)
    op.filter(image, newImage)
    this.image = newImage
  }

  def getImg: BufferedImage = image

  def getImage: BufferedImage = image

  def setImg(image: BufferedImage): Unit = {
    this.image = image
  }

  def setImage(image: BufferedImage): Unit = {
    this.image = image
  }

  def render(g: Graphics, x: Int, y: Int, observer: ImageObserver): Unit = {
    g.drawImage(image, x, y, observer)
  }
  def renderCenter(g: Graphics, x: Int, y: Int, observer: ImageObserver): Unit = {
    g.drawImage(image, x-image.getWidth()/2, y-image.getHeight()/2, observer)
  }

  override def clone: TransformableImage = {
    val newImage: BufferedImage = cloneImage(image)
    new TransformableImage(newImage)
  }

  def cloneImage(image: BufferedImage): BufferedImage = {
    val b = new BufferedImage(image.getWidth, image.getHeight, image.getType)
    val g = b.getGraphics
    g.drawImage(image, 0, 0, null)
    g.dispose()
    b
  }

  def cloneImage: BufferedImage = this.cloneImage(image)

  def scaleImg(img: BufferedImage, width: Double, height: Double): Unit = {
    val w = img.getWidth
    val h = img.getHeight
    val at = new AffineTransform
    at.scale(width, height)
    val op = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR)
    val after = new BufferedImage((w * width).toInt, (h * height).toInt, BufferedImage.TYPE_INT_ARGB)
    op.filter(img, after)
    this.image = after
  }

  def resizeImg(img: BufferedImage, width: Double, height: Double): Unit = {
    val w = img.getWidth
    val h = img.getHeight
    scaleImg(img, width / w, height / h)
  }

  def reset(): Unit ={
    this.image=originalImage
  }

}
