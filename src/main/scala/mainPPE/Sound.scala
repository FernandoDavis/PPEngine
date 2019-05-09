package mainPPE

import java.io.File

import javax.sound.sampled._

/**
  * author: Gabriel Soto Ramos
  */
class Sound {

  protected var audioStream: AudioInputStream = null
  protected var audioClip: Clip = null
  protected var audioFile: File = null
  protected var volume: Float = 100
  protected var panning: Float = 0
  protected var loops: Int = 0
  protected var volumeSuppress: Float = 0
  private val listener: LineListener = new LineListener {
    override def update(event: LineEvent): Unit = {
      event.getType match {
        case LineEvent.Type.STOP => runOnStop()
        case LineEvent.Type.START => runOnStart()
        case LineEvent.Type.OPEN => runOnOpen()
        case LineEvent.Type.CLOSE => runOnClose()
      }
    }

  }
  protected var soundRadius: Double = 100

  protected def runOnStop(): Unit = {
  }

  protected def runOnStart(): Unit = {
  }

  protected def runOnOpen(): Unit = {
  }

  protected def runOnClose(): Unit = {
  }

  def this(path: String) {
    this()
    this.setSound(path)
  }

  def this(file: File) {
    this()
    this.setSound(file)
  }

  private def getGain(vol: Float): Float = {
    if (vol <= 0)
      return -80.0f
    math.log10(vol / 100.0f).toFloat * 20.0f
  }

  def clamp(n: Double, min: Double, max: Double): Double = {
    if (n > max) {
      return max
    }
    if (n < min)
      return min
    n
  }

  def setSound(file: File): Unit = {
    if(audioFile!=null)
      stop()
    if (file.exists()) {
      this.audioFile = file
      this.audioStream = AudioSystem.getAudioInputStream(audioFile)
      val format = audioStream.getFormat
      val info = new DataLine.Info(classOf[Clip], format)
      this.audioClip = AudioSystem.getLine(info).asInstanceOf[Clip]
    }
  }

  def setSound(path: String): Unit = {
    if (path.substring(path.length - 3, path.length).toLowerCase != "wav") {
      println("This only accepts .wav files")
      return
    }
    try {
      val file: File = new File("src\\" + path)
      if (file.exists())
        this.setSound(file)
    } catch {
      case e: Exception =>
        e.printStackTrace()
    }
  }

  def play(): Unit = {
    if (audioFile == null || volume < 1 || volumeSuppress>=1)
      return
    else if (loops < 0)
      stop()
    audioStream = AudioSystem.getAudioInputStream(audioFile)
    val format = audioStream.getFormat
    val info = new DataLine.Info(classOf[Clip], format)
    try {
      audioClip = AudioSystem.getLine(info).asInstanceOf[Clip]
      if (!audioClip.isOpen)
        audioClip.open(audioStream)
      val gain: FloatControl = audioClip.getControl(FloatControl.Type.MASTER_GAIN).asInstanceOf[FloatControl]
      if (audioClip.isControlSupported(FloatControl.Type.PAN)) {
        val pan: FloatControl = audioClip.getControl(FloatControl.Type.PAN).asInstanceOf[FloatControl]
        pan.setValue(panning)
      }
      gain.setValue(getGain(volume*(1-volumeSuppress)))
      audioClip.setFramePosition(0)
      audioClip.addLineListener(listener)
      audioClip.start()
      if (loops < 0)
        audioClip.loop(Clip.LOOP_CONTINUOUSLY)
      else
        audioClip.loop(loops)
    } catch {
      case e: LineUnavailableException =>
        e.printStackTrace()
    }
  }

  def setVolume(float: Float): Unit = {
    this.volume = clamp(float, 0, 200).toFloat
  }

  def getVolume: Float = {
    this.volume
  }

  def setPanning(float: Float): Unit = {
    this.panning = float
  }

  def setLoop(count: Int): Unit = {
    this.loops = count
  }

  def stop(): Unit = {
    if (audioClip != null) {
      audioClip.stop()
      audioClip.close()
    }
  }


  def play(x: Double, y: Double): Unit = {
    val player = Main.getGame.getPlayer
    if (audioClip != null && volumeSuppress<1)
    if (player != null) {
      val distance: Double = player.getPosition.distance(x, y)
      if (distance < soundRadius) {
        this.setVolume(((soundRadius - distance)/soundRadius).toFloat*120.0f*(1-volumeSuppress))
        this.setPanning(((x-player.getX ) / soundRadius).toFloat)
        this.play()
      }
    }
  }

  def setSoundRadius(radius: Double): Unit = {
    this.soundRadius = radius
  }

  def play(position: Vector2D): Unit = {
    this.play(position.getX, position.getY)
  }

  def setVolumeSuppression(percentage: Float): Unit ={
    this.volumeSuppress=percentage
  }

  def play(x: Double, y: Double, width: Double, height: Double): Unit = {
    val player = Main.getGame.getPlayer
    if (audioClip != null && volumeSuppress<1)
      if (player != null) {
        val distance: Double = player.getPosition.distance(x, y)
        val radius: Double = soundRadius + math.sqrt(width * width + height * height)
        if (distance < radius) {
          this.setVolume(((radius - distance)/radius).toFloat * 120.0f*(1-volumeSuppress))
          this.setPanning(((x-player.getX) / radius).toFloat)
          this.play()
        }
      }
  }


}
