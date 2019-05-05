package mainPPE

import java.awt.image.BufferedImage

object ImageFunctions {

  def rotate(image: BufferedImage, degrees: Double): BufferedImage ={
    val img = new TransformableImage(image)
    img.rotate(degrees)
    img.getImage
  }

  def rotateRadians(image: BufferedImage, angle: Double): BufferedImage ={
    val img = new TransformableImage(image)
    img.rotateRadians(angle)
    img.getImage
  }

  def flipX(image: BufferedImage): BufferedImage ={
    val img = new TransformableImage(image)
    img.flipImageX()
    img.getImage
  }

  def flipY(image: BufferedImage): BufferedImage ={
    val img = new TransformableImage(image)
    img.flipImageY()
    img.getImage
  }

  def scale(image: BufferedImage, width: Double, height: Double): BufferedImage ={
    val img = new TransformableImage(image)
    img.scaleImg(width,height)
    img.getImage
  }

  def resize(image: BufferedImage, width: Double, height: Double): BufferedImage ={
    val img = new TransformableImage(image)
    img.resizeImg(width,height)
    img.getImage
  }

  def clone(image: BufferedImage, width: Double, height: Double): BufferedImage ={
    val img = new TransformableImage(image)
    img.clone.getImage
  }

  def tileBySize(image: BufferedImage, width: Int, height: Int, xOffset: Int, yOffset: Int): BufferedImage = {
    var xRepetitions = math.max(math.ceil(width.toDouble / image.getWidth.toDouble), 1).toInt
    var yRepetitions = math.max(math.ceil(height.toDouble / image.getHeight.toDouble), 1).toInt
    val newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

    if (xOffset > 0)
      xRepetitions += 1
    if (yOffset > 0)
      yRepetitions += 1

    val g = newImg.getGraphics
    for (x <- -1 until xRepetitions) {
      for (y <- -1 until yRepetitions) {
        g.drawImage(image, x * image.getWidth - xOffset % image.getWidth, y * image.getHeight - yOffset % image.getHeight, null)
      }
    }
    g.dispose()
    newImg
  }

  def tileByRepetiton(image: BufferedImage, xRep: Int, yRep: Int, xOffset: Int, yOffset: Int): BufferedImage = {
    var xRepetitions = xRep
    var yRepetitions = yRep
    val newImg = new BufferedImage(xRep * image.getWidth, yRep * image.getHeight, BufferedImage.TYPE_INT_ARGB)

    if (xOffset > 0)
      xRepetitions += 1
    if (yOffset > 0)
      yRepetitions += 1

    val g = newImg.getGraphics
    for (x <- -1 until xRepetitions) {
      for (y <- -1 until yRepetitions) {
        g.drawImage(image, x * image.getWidth - xOffset % image.getWidth, y * image.getHeight - yOffset % image.getHeight, null)
      }
    }
    g.dispose()
    newImg
  }

}