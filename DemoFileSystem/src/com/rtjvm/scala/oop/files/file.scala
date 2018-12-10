package com.rtjvm.scala.oop.files

import java.nio.file.FileSystemException

class file( override val parentPath: String, override val name: String, val contents: String )
  extends dirEntry( parentPath, name ) {


  def asDirectory: directory = throw new FileSystemException( "A file cannot be converted to a directory" )
  def asFile: file = this

  def isDirectory: Boolean = false
  def isFile: Boolean = true

  def getType: String = "File"

  def setContents( newContents: String ): file =
    new file( parentPath, name, newContents )

  def appendContents( newContents: String ): file =
    setContents( contents + "\n" + newContents )
}

object file {

  def empty( parentPath: String, name: String ): file =
    new file( parentPath, name, "" )
}
