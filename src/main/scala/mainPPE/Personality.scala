package mainPPE

class Personality {
  private var owner: Obj = null
  private val behaviours: ArrayList[Behaviour] = new ArrayList[Behaviour]()

  def runCollisionBehaviours(obj: Obj): Unit = {
    for (behavior <- behaviours.par) {
      if (behavior.owner != this.owner)
        behavior.setOwner(owner)
      if (behavior.activation == Behaviour.activationType.collision) {
        behavior.collision(obj)
      }
    }
  }

  def this(owner: Obj) {
    this()
    this.owner = owner
  }

  def setOwner(owner: Obj) {
    this.owner = owner
  }

  def getOwner: Obj = owner

  def run(): Unit = {
    if (owner != null && behaviours.size > 0)
      for (behavior <- behaviours.par) {
        if (behavior.owner != this.owner)
          behavior.setOwner(owner)
        if (behavior.activation == Behaviour.activationType.default)
          behavior.run()
      }
  }

  def run(activation: Behaviour.activationType.Value): Unit = {
    if (owner != null && behaviours.size > 0 && activation != Behaviour.activationType.collision)
      for (behavior <- behaviours.par) {
        if (behavior.owner != this.owner)
          behavior.setOwner(owner)
        if (behavior.activation == activation)
          behavior.run()
      }
  }

  def +=(behaviour: Behaviour): Unit = {
    behaviour.setOwner(owner)
    behaviours += behaviour
  }

  def add(behaviour: Behaviour): Unit = {
    behaviour.setOwner(owner)
    behaviours += behaviour
  }

  def add(behaviour: Behaviour*): Unit = {
    for (b <- behaviour) {
      b.setOwner(owner)
      this.add(b)
    }
  }

  def +=(behaviour: Behaviour*): Unit = {
    for (b <- behaviour) {
      b.setOwner(owner)
      this.add(b)
    }
  }

  def +=(behaviour: ArrayList[Behaviour]): Unit = {
    for (b <- behaviour) {
      b.setOwner(owner)
      this.add(b)
    }
  }

  def -=(behaviour: Behaviour): Unit = {
    behaviours.remove(behaviour)
  }

  def +(personality: Personality): Personality = {
    val p = new Personality()
    p += (personality.behaviours + behaviours)
    p
  }

  def +=(personality: Personality): Unit = {
    this += personality.behaviours
  }

  def +(behaviour: Behaviour): Personality = {
    val p = this.clone()
    p += behaviour
    p
  }

  override def clone(): Personality = {
    val p = new Personality()
    p.setOwner(this.owner)
    for(behaviour <- behaviours){
      p.add(behaviour.clone())
    }
    p
  }


  def size: Int = this.behaviours.size
}
