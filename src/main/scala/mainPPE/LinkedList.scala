package mainPPE

import scala.reflect.ClassTag

class LinkedList[E >: Null : ClassTag] extends Iterable[E] {

  private val Head: Node = new Node
  private val Tail: Node = new Node
  private var Size: Int = 0
  Head.setNext(Tail)
  Tail.setPrev(Head)

  def this(e: E*) {
    this()
    addSeq(e)
  }

  def addSeq(e: Seq[E]): Unit = {
    if (e != null)
      e.foreach(this.add)
  }

  def add(e: E): Unit = {
    if (e != null) {
      val node: Node = new Node(e)
      node.insertBetween(Tail.getPrev, Tail)
      Size += 1
    }
  }

  def add(e: E*): Unit = {
    if (e != null)
      e.foreach(this.add)
  }

  def +=(e: E*): Unit = {
    this.addSeq(e)
  }

  def +=(e: E): Unit = {
    this.add(e)
  }

  def +=(list: LinkedList[E]): Unit = {
    if (list != null)
      list.foreach(add)
  }

  def +(list: LinkedList[E]): LinkedList[E] = {
    val c = this.clone()
    if (list != null)
      c += list
    c
  }

  def -=(e: E): Unit = {
    this.remove(e)
  }

  def -=(e: E*): Unit = {
    this.removeSeq(e)
  }

  def getFirst: E = Head.getNext.getData

  def getLast: E = Tail.getPrev.getData

  def addFirst(e: E): Unit = {
    if (e != null) {
      val node: Node = new Node(e)
      node.insertBetween(Head, Head.getNext)
      Size += 1
    }
  }

  def addAtIndex(e: E, index: Int): Unit = {
    if (index >= this.Size || index < 0) {
      println("wtf")
      return
    }
    if (e != null) {
      var cnode = Head.getNext
      for (i: Int <- 0 to index) {
        cnode = cnode.getNext
      }
      val node: Node = new Node(e)
      node.insertBetween(cnode.getPrev, cnode)
      Size += 1
    }
  }

  private def getNode(index: Int): Node = {
    if (index >= this.Size || index < 0) {
      println("wtf")
      return null
    }
    var cnode = Head.getNext
    for (i: Int <- 0 until index) {
      cnode = cnode.getNext
    }
    cnode
  }

  def get(index: Int): E = {
    val node: Node = getNode(index)
    if (node != null)
      return node.getData
    null
  }

  def remove(e: E): Boolean = {
    val node = containsNode(e)
    if (node != null && node != Tail && node != Head) {
      node.remove()
      Size -= 1
      return true
    }
    false
  }

  def removeSeq(e: Seq[E]): Unit = {
    var cnode = Head.getNext
    while (cnode != Tail && cnode != Head) {
      if (cnode.getData.equals(e) || cnode.getData == e) {
        val node = cnode.getPrev
        cnode.remove()
        cnode = node
      }
      cnode = cnode.getNext
    }
  }

  def remove(e: E*): Unit = {
    if (e != null)
      e.foreach(remove)
  }

  def removeIndex(index: Int): Boolean = {
    val node = getNode(index)
    if (node != null) {
      node.remove()
      Size -= 1
      return true
    }
    false
  }

  def contains(e: E): Boolean = {
    containsNode(e) != null
  }

  private def containsNode(e: E): Node = {
    var cnode = Head.getNext
    while (cnode != Tail && cnode != Head) {
      if (cnode.getData.equals(e) || cnode.getData == e) {
        return cnode
      }
      cnode = cnode.getNext
    }
    null
  }

  override def clone(): LinkedList[E] = {
    val list = new LinkedList[E]()
    foreach(list.add)
    list
  }

  def clear(): Unit = {
    var cnode = Head.getNext
    while (cnode != Tail && cnode != Head) {
      cnode.remove()
      cnode = Head.getNext
    }
    Size=0
  }
  def length: Int = this.Size

  override def size: Int = this.Size

  def indices: Range = 0 until Size

  override def isEmpty: Boolean = this.Size == 0

  override def iterator: Iterator[E] = {
    new iter
  }

  private class iter extends Iterator[E] {
    private var cnode: Node = Head

    override def hasNext: Boolean = {
      cnode.getNext != null && cnode.getNext != Tail && cnode != Tail
    }

    override def next(): E = {
      val e = cnode.getNext.getData
      cnode = cnode.getNext
      e
    }
  }


  private class Node {
    private var next: Node = null
    private var prev: Node = null
    private var data: E = null

    def this(data: E) {
      this()
      this.data = data
    }

    def this(data: E, prev: Node, next: Node) {
      this()
      this.data = data
      this.prev = prev
      this.next = next
    }

    def getData: E = data

    def setData(element: E): Unit = {
      this.data = element
    }

    def getNext: Node = next

    def setNext(node: Node): Unit = {
      this.next = node
    }

    def getPrev: Node = prev

    def setPrev(node: Node): Unit = {
      this.prev = node
    }

    def insertBetween(prev: Node, next: Node): Unit = {
      this.prev = prev
      this.next = next
      prev.setNext(this)
      next.setPrev(this)
    }

    def remove(): Unit = {
      this.prev.setNext(next)
      this.next.setPrev(prev)
      this.setNext(null)
      this.setPrev(null)
      this.setData(null)
    }


  }

}
