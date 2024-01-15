package o1.gui

import scala.collection.mutable.ListBuffer
import o1.gui.PicHistory.*
import o1.util.nice.number.*
import scala.annotation.targetName
import o1.util.proper.*

private[o1] object PicHistory:
  private val MinimumLength: Int = 1   // Has to be at least one operation (the creation)
  private val DefaultLength: Int = 10  // The default total length of a new PicHistory; includes creation and processing.

  private[o1] object op:

    sealed trait PicOp private[gui](method: ProperString, val metadata: Option[Map[String, Boolean]]):

      val calledMethod = method

      override def toString =
        val b = StringBuilder()
        b ++= this.calledMethod
        if metadata.isDefined then
          b ++= " (" ++= metadata.toString ++= ")"
        b.toString()

      def booleanValue(key: String, defaultValue: Boolean): Boolean =
        this.metadata.flatMap( _.get(key) ) getOrElse defaultValue

    end PicOp

    final class Create(method: ProperString, val simpleDescription: String, metadata: Option[Map[String, Boolean]] = None)
      extends PicOp(method, metadata)

    sealed trait Process extends PicOp:
      val isTransformation: Boolean = false  // Whether this operation is expected to represent a color or geometry transformation

    final class AdjustViewport(method: ProperString, metadata: Option[Map[String, Boolean]] = None)
      extends PicOp(method, metadata), Process

    final class Transform(method: ProperString, metadata: Option[Map[String, Boolean]] = None)
      extends PicOp(method, metadata), Process:
      override val isTransformation: Boolean = true

    final class Miscellaneous(method: ProperString, metadata: Option[Map[String, Boolean]] = None)
      extends PicOp(method, metadata), Process

  end op

end PicHistory


private[o1] final class PicHistory private(val creationOp: op.Create, val processingOps: List[op.Process], val maximumLength: Int):
  private lazy val actualMaxLength = maximumLength atLeast MinimumLength

  private[gui] def this(creationOp: op.Create, maximumHistoryLength: Int) =
    this(creationOp, List(), maximumHistoryLength)

  private[gui] def this(creationOp: op.Create) =
    this(creationOp, DefaultLength)

  override def toString = methodList.mkString(", ")

  private[gui] def copy(newCreationOp: op.Create = creationOp, newProcessingOpList: List[op.Process] = processingOps): PicHistory =
    PicHistory(newCreationOp, newProcessingOpList, actualMaxLength)

  private[gui] def setHistoryLength(newMaximumLength: Int): PicHistory =
    val length = newMaximumLength atLeast PicHistory.MinimumLength
    val newOpList = shortenedProcessingOpList(targetLength = length, forAddition = false)
    PicHistory(this.creationOp, newOpList, length)

  @targetName("cons")
  private[gui] def ::(processingOp: op.Process): PicHistory =
    if actualMaxLength <= PicHistory.MinimumLength then
      this.copy(newProcessingOpList = List())
    else
      val normalizedOpList = shortenedProcessingOpList(targetLength = actualMaxLength, forAddition = true)
      val newOpList = processingOp :: normalizedOpList
      this.copy(newProcessingOpList = newOpList)

  private def shortenedProcessingOpList(targetLength: Int, forAddition: Boolean): List[op.Process] =
    val oneForCreationOp = 1
    val possiblyOneForAddition = if forAddition then 1 else 0
    val numberOfOpsToDrop = this.processingOps.length - (targetLength - oneForCreationOp - possiblyOneForAddition)
    this.processingOps.dropRight(numberOfOpsToDrop)

  def methodList: List[String] =
    val methodNames = List.newBuilder[String]
    for op <- this.processingOps do
      methodNames += op.calledMethod
    methodNames += this.creationOp.calledMethod
    methodNames.result()

  private[gui] def containsTransformations: Boolean = this.processingOps.exists( _.isTransformation )

end PicHistory

