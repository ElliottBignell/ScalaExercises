package com.rtjvm.scala.oop.commands
import com.rtjvm.scala.oop.filesystem.state

class cat( filename: String ) extends Command {

  override def apply(s: state): state = {

    val wd = s.wd
    val dirEntry = wd.findEntry( filename )

    if ( dirEntry == null || !dirEntry.isFile )
      s.setMessage( filename + ": No such file")
    else
      s.setMessage( dirEntry.asFile.contents )
  }
}

