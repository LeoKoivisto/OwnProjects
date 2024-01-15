package o1.flappy
import o1._

class Game:
  
  val bug = Bug(Pos(ViewWidth / 10, ViewHeight / 10))
  
  val obstacles = Vector(Obstacle(70), Obstacle(30), Obstacle(20)) 
  
  def activateBug() =
    this.bug.flap(FlapStrength)
  
  def timePasses() =
    
    for (obstacle <- this.obstacles) 
      obstacle.approach()
    
    this.bug.fall()
    
  def isLost: Boolean =
    val touchesObstacle = this.obstacles.exists(_.touches(this.bug)) 
    val isOutOfBounds = !this.bug.isInBounds
    touchesObstacle || isOutOfBounds
    
end Game
