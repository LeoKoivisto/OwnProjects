package o1.gui

import smcl.modeling.d2.Pos as SMCLPos
import smcl.pictures.{Circle as SMCLCircle, Ellipse as SMCLEllipse, Line as SMCLLine, Rectangle as SMCLRectangle,
                      StarPentagon as SMCLStarPentagon, Triangle as SMCLTriangle, Polygon as SMCLPolygon}

import o1.gui.PicHistory.*
import o1.util.nice.number.*
import o1.util.proper.*
import Anchor.*


// N.B. This trait has defaults so doesn’t currently work in exports, but it would be nice to convert into an exported API.

/** This trait provides a variety of shape-manipulating methods. It is used internally by O1Library.
  *
  * **NOTE TO STUDENTS: In this course, you don’t need to understand the purpose of this trait.**
  * You’ll find these methods on [[Pic]]s. */
private[gui] trait ShapeAPI:

  private val StarCuspRadiusFactor: Double = 0.201
  private def creationOp(methodName: ProperString, shapeName: String) =
    PicHistory(op.Create(method = methodName, simpleDescription = shapeName + "-shape"))
  private def creationOp(methodName: ProperString): PicHistory =
    creationOp(methodName, methodName)


  /** Creates a new [[Pic]] that portrays a filled rectangle. Sets its
    * [[o1.world.objects.Anchor Anchor]] at [[o1.world.objects.Anchor.TopLeft TopLeft]].
    * @param bounds  the width and height of the rectangle and the [[Pic]]
    * @param color   the color of the rectangle and thus the only color visible in the [[Pic]]
    * @return a [[Pic]] of the rectangle (a vector graphic) */
  def rectangle(bounds: Bounds, color: Color): Pic =
    val smclContent = SMCLRectangle(baseLengthInPixels = bounds.width atLeast 0,
                                    heightInPixels = bounds.height atLeast 0, hasBorder = false,
                                    hasFilling = true, color = color.smclColor, fillColor = color.smclColor)
    Pic(smclContent, TopLeft, creationOp("rectangle".p))

  /** Creates a new [[Pic]] that portrays a filled rectangle.
    * @param width   the width of the rectangle and the [[Pic]]
    * @param height  the height of the rectangle and the [[Pic]]
    * @param color   the color of the rectangle and thus the only color visible in the [[Pic]]
    * @param anchor  an anchor for the new [[Pic]]; if unspecified, defaults to [[Center]]
    * @return a [[Pic]] of the rectangle (a vector graphic) */
  def rectangle(width: Double, height: Double, color: Color, anchor: Anchor = Center): Pic =
    val smclContent = SMCLRectangle(
        baseLengthInPixels = width atLeast 0,
        heightInPixels = height atLeast 0,
        hasBorder = false,
        hasFilling = true,
        color = color.smclColor,
        fillColor = color.smclColor)
    Pic(smclContent, anchor, creationOp("rectangle".p))

  /** Creates a new [[Pic]] that portrays a filled rectangle and sets its
    * [[o1.world.objects.Anchor Anchor]] at [[o1.world.objects.Anchor.TopLeft TopLeft]].
    * @param width   the width of the rectangle and the [[Pic]]
    * @param height  the height of the rectangle and the [[Pic]]
    * @param color   the color of the rectangle and thus the only color visible in the [[Pic]];
    *                if unspecified, defaults to `White`
    * @return a [[Pic]] of the rectangle (a vector graphic) */
  def emptyCanvas(width: Double, height: Double, color: Color = colors.White): Pic =
    val smclContent =
      SMCLRectangle(baseLengthInPixels = width atLeast 0, heightInPixels = height atLeast 0,
          hasBorder = false, hasFilling = true, color = color.smclColor, fillColor = color.smclColor
          ).moveUpperLeftCornerTo(SMCLPos.Origo).toPicture.setViewportToContentBoundary(None)
    Pic(smclContent, TopLeft, creationOp("emptyCanvas".p, "rectangle".p))

  /** Creates a new [[Pic]] that portrays a filled square.
    * @param side    the width and height of the square and the [[Pic]]
    * @param color   the color of the square and thus the only color visible in the [[Pic]]
    * @param anchor  an anchor for the new [[Pic]]; if unspecified, defaults to [[Center]]
    * @return a [[Pic]] of the square (a vector graphic) */
  def square(side: Double, color: Color, anchor: Anchor = Center): Pic =
    val smclContent = SMCLRectangle(sideLengthInPixels = side atLeast 0, hasBorder = false, hasFilling = true,
                                    color = color.smclColor, fillColor = color.smclColor)
    Pic(smclContent, anchor, creationOp("square".p))


  /** Creates a new [[Pic]] that portrays a filled polygon. The picture is just large
    * enough to contain the polygon; its background is fully transparent.
    * @param points  the polygon’s corners (only their relative positioning matters)
    * @param color   the polygon’s color
    * @param anchor  an anchor for the new [[Pic]]; if unspecified, defaults to [[Center]]
    * @return a [[Pic]] of the rectangle (a vector graphic) */
  def polygon(points: Seq[Pos], color: Color, anchor: Anchor = Center): Pic =
    val smclContent = SMCLPolygon(
        position = SMCLPos.Origo,
        referencePointRelativeToCenterAtOrigo = SMCLPos.Origo,
        pointsRelativeToCenterAtOrigo = points.map( _.toSMCLPos ),
        hasBorder = false,
        hasFilling = true,
        color = color.smclColor,
        fillColor = color.smclColor)
    Pic(smclContent, anchor, creationOp("polygon".p))


  /** Creates a new [[Pic]] that portrays a filled circle. The background is fully transparent.
    * @param diameter  the diameter of the circle, which also sets the width and height of the [[Pic]]
    * @param color     the circle’s color
    * @param anchor    an anchor for the new [[Pic]]; if unspecified, defaults to [[Center]]
    * @return a [[Pic]] of the circle (a vector graphic) */
  def circle(diameter: Double, color: Color, anchor: Anchor = Center): Pic =
    val smclContent = SMCLCircle(center = SMCLPos.Origo, radiusInPixels = (diameter / 2.0) atLeast 0,
          hasBorder = false, hasFilling = true, color = color.smclColor, fillColor = color.smclColor)
    Pic(smclContent, anchor, creationOp("circle".p))

  /** Creates a new [[Pic]] that portrays an ellipse. The background is fully transparent.
    * @param width     the width of the ellipse and the [[Pic]]
    * @param height    the height of the ellipse and the [[Pic]]
    * @param color     the ellipse’s color
    * @param anchor    an anchor for the new [[Pic]]; if unspecified, defaults to [[Center]]
    * @return a [[Pic]] of the ellipse (a vector graphic) */
  def ellipse(width: Double, height: Double, color: Color, anchor: Anchor = Center): Pic =
    val smclContent = SMCLEllipse(center = SMCLPos.Origo, semiMajorAxisInPixels = (width / 2.0) atLeast 0,
                                  semiMinorAxisInPixels = (height / 2.0) atLeast 0, hasBorder = false,
                                  hasFilling = true, color = color.smclColor, fillColor = color.smclColor)
    Pic(smclContent, anchor, creationOp("ellipse".p))


  /** Creates a new [[Pic]] that portrays an isosceles triangle. The triangle’s base is at the
    * bottom of the image, and its apex is at the top center. The background is fully transparent.
    * @param width     the width of the triangle’s base, which determines the width of the [[Pic]], too
    * @param height    the height of the triangle, which determines the height of the [[Pic]], too
    * @param color     the triangle’s color
    * @param anchor    an anchor for the new [[Pic]]; if unspecified, defaults to [[Center]]
    * @return a [[Pic]] of the triangle (a vector graphic) */
  def triangle(width: Double, height: Double, color: Color, anchor: Anchor = Center): Pic =
    val smclContent = SMCLTriangle.basedOnHeightAndBase(height = height atLeast 0, baseLength = width atLeast 0,
              hasBorder = false, hasFilling = true, color = color.smclColor, fillColor = color.smclColor)
    Pic(smclContent, anchor, creationOp("triangle".p))


  /** Creates a new [[Pic]] that portrays a five-pointed star. The background is fully transparent.
    * @param width   the width of the star, which determines the [[Pic]]’s dimensions
    * @param color   the star’s color
    * @param anchor  an anchor for the new [[Pic]]; if unspecified, defaults to [[Center]]
    * @return a [[Pic]] of the star (a vector graphic) */
  def star(width: Double, color: Color, anchor: Anchor = Center): Pic =
    val smclContent = SMCLStarPentagon(center = SMCLPos.Origo, widthInPixels = width atLeast 0,
      heightInPixels = width atLeast 0, cuspRadiusInPixels = (StarCuspRadiusFactor * width) atLeast 0,
      hasBorder = false, hasFilling = true, color = color.smclColor, fillColor = color.smclColor)
    Pic(smclContent, anchor, creationOp("star".p))


  /** Creates a new [[Pic]] that portrays a thin line. The line is specified in terms
    * of two [[o1.world.Pos Pos]] objects: imagine drawing a line between the two points
    * on a plane and then cropping the plane to just the part that contains the line.
    *
    * The line always runs from one corner of the resulting [[Pic]] to another. The
    * [[o1.world.objects.Anchor Anchor]] of the [[Pic]] is at one of the four corners:
    * the one that’s closest to `from`.
    *
    * The background is fully transparent.
    *
    * @param from      the line’s “starting point”; the [[Pic]] will anchor at the corresponding corner
    * @param to        the line’s “end point”
    * @param color     the color of the line
    * @return a [[Pic]] of the line (a vector graphic) */
  def line(from: Pos, to: Pos, color: Color): Pic =
    val anchor = (from.x < to.x, from.y < to.y) match
      case (true, true)   => TopLeft
      case (true, false)  => BottomLeft
      case (false, true)  => TopRight
      case (false, false) => BottomRight
    val smclContent = SMCLLine(startX = from.x, startY = from.y, endX = to.x, endY = to.y, color = color.smclColor)
    Pic(smclContent, anchor, creationOp("line".p))

end ShapeAPI

