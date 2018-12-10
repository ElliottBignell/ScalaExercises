package com.rtjvm.scala.oop.commands
import com.rtjvm.scala.oop.filesystem.state

class pwd extends Command {

  override def apply(s: state): state = {
    s.setMessage( s.wd.path )
  }
}
