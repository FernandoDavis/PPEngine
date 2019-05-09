package mainPPE

import java.awt.event._

import mainPPE.Input.{isClicking, mouseClick, mousePosition, updateMouse}

object Input extends KeyListener {

  var keys: Array[Boolean] = new Array[Boolean](255)
  private var keyTime: Array[Long] = new Array[Long](255)
  var mousePosition: Vector2D = new Vector2D(0,0)
  var mouseScreenPosition: Vector2D = new Vector2D(0,0)
  var isClicking: Boolean = false
  var mouseClick: Vector2D = new Vector2D(0,0)

  def Input() {
    reset()
  }

  def isPressing(key: Int): Boolean = this.keys(key)
  def getTime(key: Int): Long = this.keyTime(key)

  def updateMouse(e: MouseEvent): Unit={
    mousePosition.getDataFrom(e.getPoint)
    mouseScreenPosition.getDataFrom(e.getLocationOnScreen)
  }

  def reset() {
    keys = new Array[Boolean](255)
  }

  override def keyPressed(k: KeyEvent) {
    //super.keyPressed(k)
    keys(k.getKeyCode) = true
    keyTime(k.getKeyCode) +=1
    //println(k.getKeyChar)
  }

  override def keyTyped(k: KeyEvent) {
    //super.keyTyped(k)
  }

  override def keyReleased(k: KeyEvent) {
    // super.keyReleased(k)
    keys(k.getKeyCode) = false
    keyTime(k.getKeyCode) = 0
  }

}

object MouseClicks extends MouseListener {

  override def mouseClicked(e: MouseEvent): Unit = {
    updateMouse(e)
  }

  override def mousePressed(e: MouseEvent): Unit = {
    updateMouse(e)
    isClicking=true
    mouseClick=mousePosition
  }

  override def mouseReleased(e: MouseEvent): Unit = {
    updateMouse(e)
    isClicking=false
  }

  override def mouseEntered(e: MouseEvent): Unit = {
    updateMouse(e)
  }

  override def mouseExited(e: MouseEvent): Unit = {
    updateMouse(e)
  }
}