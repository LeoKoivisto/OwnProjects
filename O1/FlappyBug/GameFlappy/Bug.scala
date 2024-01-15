package o1.flappy
import o1._

class Bug(initialPos: Pos):
  private var currentPos = initialPos
  def pos = this.currentPos
    currentPos
  val radius = BugRadius
  private var yVelocity = 0.0
  def flap(strength: Double) =
    this.yVelocity = -strength
  def fall() =
    if currentPos._2 < 350 then
    this.yVelocity += 2
    this.move(this.yVelocity)
  override def toString = "center at " + this.pos + ", radius " + this.radius

  def move(arvo: Double) =
    this.currentPos = this.currentPos.addY(arvo)
    this.currentPos = this.currentPos.clampY(0, 350)

  def isInBounds : Boolean =
    val maxY = 350
    val minY = 0
    currentPos._2 > minY && currentPos._2 < maxY

end Bug
