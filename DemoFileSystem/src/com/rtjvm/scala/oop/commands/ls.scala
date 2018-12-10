package com.rtjvm.scala.oop.commands
import com.rtjvm.scala.oop.files.dirEntry
import com.rtjvm.scala.oop.filesystem.state

class ls extends Command {

  override def apply(s: state): state = {
    val contents = s.wd.contents
    val niceOutput = createNiceOutput( contents )
    s.setMessage( niceOutput )
  }

  def createNiceOutput( contents: List[ dirEntry ] ): String = {

    if ( contents.isEmpty ) ""
    else {

      val entry = contents.head
      entry.name + "{" + entry.getType + "}\n" + createNiceOutput( contents.tail )
    }
  }
}
