package com.rtjvm.scala.oop.files

abstract class dirEntry( val parentPath: String, val name: String ) {

  def path: String = {

    val separatorIfNecessary =
      if ( directory.ROOT_PATH.equals( parentPath ) ) ""
      else directory.SEPARATOR

    parentPath + separatorIfNecessary + name
  }

  def asDirectory: directory
  def asFile: file
  def getType: String

  def isDirectory: Boolean
  def isFile: Boolean
}
