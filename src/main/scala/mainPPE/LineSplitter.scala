package mainPPE

import java.awt.{Color, Graphics}

object LineSplitter {

  val colors: Array[Color] = Array(Color.red, Color.orange, Color.yellow, Color.green, Color.cyan, Color.blue)

  def draw(str: String, X: Int, Y: Int, g: Graphics): Unit = {
    var index: Int = 0
    var y: Int = Y
    val color: Color = g.getColor
    for (line: String <- str.split("\n")) {
      if (line.indexOf("\r") != -1) {
        g.setColor(colors(index))
        index=(index+1)%colors.length
      }
      g.drawString(line, X, y)
      y += g.getFontMetrics.getHeight

    }
    g.setColor(color)
  }


}
