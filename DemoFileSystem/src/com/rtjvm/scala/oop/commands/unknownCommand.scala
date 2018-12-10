package com.rtjvm.scala.oop.commands

import com.rtjvm.scala.oop.filesystem.state

class unknownCommand extends Command {

  override def apply( s: state ): state =
    s.setMessage( "Command not found!" )
}
