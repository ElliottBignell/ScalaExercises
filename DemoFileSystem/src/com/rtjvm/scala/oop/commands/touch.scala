package com.rtjvm.scala.oop.commands

import com.rtjvm.scala.oop.files.{dirEntry, directory, file}
import com.rtjvm.scala.oop.filesystem.state

class touch( name: String ) extends createEntry( name ) {

  override def createSpecificEntry( s: state ): dirEntry =
     file.empty( s.wd.path, name )
}
