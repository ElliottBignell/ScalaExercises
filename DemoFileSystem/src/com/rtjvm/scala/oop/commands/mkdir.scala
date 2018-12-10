package com.rtjvm.scala.oop.commands
import com.rtjvm.scala.oop.files.{dirEntry, directory}
import com.rtjvm.scala.oop.filesystem.state

class mkdir( name: String ) extends createEntry( name ) {

  override def createSpecificEntry( s: state ): dirEntry =
    directory.empty( s.wd.path, name )
}
