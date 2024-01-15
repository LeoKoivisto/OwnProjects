package o1.flappy
import o1.*
import scala.util.Random

class Obstacle(val radius: Int):

  private var currentPos = this.randomLaunchPosition()

  def pos = this.currentPos

  override def toString = s"center at $pos, radius $radius"

  def touches(bug: Bug) = bug.pos.distance(this.pos) < bug.radius + this.radius

  def isActive = this.pos.x >= -this.radius
  
  def approach() =
    this.currentPos =
      if this.isActive then
        this.currentPos.addX(-ObstacleSpeed)
      else
        this.randomLaunchPosition()

  private def randomLaunchPosition() =
    val launchX = ViewWidth + this.radius + Random.nextInt(500)
    val launchY = Random.nextInt(ViewHeight)
    Pos(launchX, launchY)
    
end Obstacle

