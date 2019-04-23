package mainPPE

import scala.reflect.ClassTag

class ArrayList[E >: Null: ClassTag] extends Iterable[E] {
  private val minCap: Int = 10
  private var len: Int = 10

  def this(int: Int) {
    this()
    if (int > minCap)
      this.len = int
  }
  if (this.len < minCap)
    len = minCap
  private var elements: Array[E] = new Array[E](len)
  private var Size: Int = 0

  override def clone(): ArrayList[E] = {
    var newArr: ArrayList[E] = new ArrayList[E](Size)
    this.foreach(newArr.add)
    newArr
  }

  def add(e: E): Unit = {
    if (e != null) {
      if (this.Size >= this.elements.length)
        this.changeCapacity(Size * 2)
      this.elements(Size) = e
      this.Size += 1
    }
  }

  def addInto(e: E, index: Int): Unit = {
    if (e != null) {
      if (this.Size >= this.elements.length)
        this.changeCapacity(Size * 2)
      this.Size += 1
      this.shiftRight(index)
      this.elements(index) = e

    }
  }

  def +(arList: ArrayList[E]): ArrayList[E] = {
    var arr: ArrayList[E] = this.clone()
    arr += arList
    arr
  }

  def +=(arList: ArrayList[E]): Unit = {
    arList.foreach(add)
  }

  def +=(arr: E*): Unit ={
    if (arr != null) {
      arr.foreach(add)
    }
  }

  def +=(e: E): Unit ={
    this.add(e)
  }


  def add(arr: E*): Unit = {
    if (arr != null) {
      arr.foreach(add)
    }
  }

  def remove(e: E): Unit = {
    if (e != null) {
      for (i: Int <- 0 until Size) {
        if (e == this.elements(i)) {
          this.elements(i) = null
          if (i != this.Size - 1)
            this.shiftLeft(i)
          this.Size -= 1
          if (this.Size < this.elements.length / 2 && this.Size > minCap) {
            this.changeCapacity(Size * (3 / 4))
          }
          return
        }
      }
    }
  }

  def remove(index: Int): Unit = {
    if (index >= 0 && index < Size) {
      this.elements(index) = null
      if (index != this.Size - 1)
        this.shiftLeft(index)
      this.Size -= 1
      if (this.Size < this.elements.length / 2 && this.Size > minCap) {
        this.changeCapacity(Size * (3 / 4))
      }
    }
  }

  def removeIndex(index: Int): Unit = { //Incase this is an array of Integers
    if (index >= 0 && index < Size) {
      this.elements(index) = null
      if (index != this.Size - 1)
        this.shiftLeft(index)
      this.Size -= 1
      if (this.Size < this.elements.length / 2 && this.Size > minCap) {
        this.changeCapacity(Size * (3 / 4))
      }
    }
  }

  def remove(arr: E*): Unit = {
    if (arr != null) {
      arr.foreach(remove)
    }
  }

  def length: Int = this.Size

  override def size: Int = this.Size

  def capacity: Int = this.elements.length

  private class iter extends Iterator[E] {
    private var index: Int = 0

    override def hasNext: Boolean = {
      index >= 0 && index < Size
    }

    override def next(): E = {
      val e: E = elements(index)
      index += 1
      e

    }
  }

  override def iterator: Iterator[E] = new iter

  private def shiftLeft(index: Int): Unit = {
    for (i: Int <- index until Size) {
      this.elements(i) = this.elements(i + 1)
    }
  }

  private def shiftRight(index: Int): Unit = {
    for (i: Int <- (Size - 1) until index by -1) {
      this.elements(i) = this.elements(i - 1)
    }
  }

  private def changeCapacity(newCapacity: Int): Unit = {
    if(newCapacity<=0)
      {
        this.clear()
        return
      }
    var newElements: Array[E] = new Array[E](newCapacity)
    for (i <- 0 until Size) {
      newElements(i) = elements(i)
    }
    elements = newElements
  }

  def get(index: Int): E = {
    if (index < 0 || index >= Size)
      return null
    this.elements(index)
  }

  def getIndex(index: Int): E = { //Incase this is an array of Integers
    if (index < 0 || index >= Size)
      return null
    this.elements(index)
  }

  def set(index: Int, e: E): Unit = {
    if (index >= 0 || index < Size)
      this.elements(index) = e
  }

  def get(e: E): E = {
    for (i: Int <- 0 until Size)
      if (this.elements(i) == e)
        return e
    null
  }

  def clear(): Unit = {
    if (this.Size == 0)
      return
    for(i: Int <- this.indices){
      this.elements(i)=null
    }
    this.elements = new Array[E](minCap)
    Size = 0
  }

  override def isEmpty: Boolean = this.Size == 0

  override def toString(): String = {
    var sb: StringBuilder = new StringBuilder()
    sb.append("ArrayList [")
    for (i <- 0 until Size) {
      sb.append(this.elements(i))
      if (i != Size - 1)
        sb.append(", ")
    }
    sb.append("]")
    sb.toString()
  }

  def indices: Range = 0 until Size

}
