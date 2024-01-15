package o1.util

/////////////////////////////////////
// INTERNAL: SMCL INITIALIZATION
/////////////////////////////////////

private val smclInitialization = DoneStatus()

private final class DoneStatus:
  private var hasNotBeenDone = true

  def isNotDone: Boolean = hasNotBeenDone

  def setDone(): Unit =
    if hasNotBeenDone then
      synchronized( if hasNotBeenDone then hasNotBeenDone = false )

end DoneStatus


private[o1] def smclInit(): Unit =
  import smcl.*
  import settings.jvmawt.*
  if smclInitialization.isNotDone then
    synchronized {
      if smclInitialization.isNotDone then
        infrastructure.jvmawt.Initializer()
        settings.DefaultBackgroundColor = colors.rgb.Transparent // Affects the bg color for bitmap rotations
        AffineTransformationInterpolationMethod = Bicubic        // Affects affine transformations incl. scaling, rotations, and flips
        // smcl.settings.jvmawt.DrawingIsAntialiased = false
        smclInitialization.setDone()
    }

private val _ = smclInit()

