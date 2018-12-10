package com.rtjvm.scala.oop.files

import java.nio.file.FileSystemException

import scala.annotation.tailrec

class directory( override val parentPath: String, override val name: String, val contents: List[ dirEntry] )
  extends dirEntry( parentPath, name ) {

  def hasEntry( name: String ): Boolean =
    findEntry( name ) != null

  def getAllFoldersInPath : List[ String ] =
    path.substring( 1 ).split( directory.SEPARATOR ).toList.filter( x => !x.isEmpty )
  // /a/b/c/d => List[ "a", "b", ... ]

  def findDescendent( path: List[ String ] ): directory =
    if ( path.isEmpty ) this
    else findEntry( path.head ).asDirectory.findDescendent( path.tail )

  def findDescendent( relativePath: String ): directory =
    if ( relativePath.isEmpty ) this
    else findDescendent( relativePath.split( directory.SEPARATOR ).toList )

  def removeEntry( entry: String ): directory =
    if ( !hasEntry( entry ) ) this
    else new directory( parentPath, name, contents.filter( x => !x.name.equals( entry ) ) )

  def addEntry( newEntry: dirEntry ): directory =
    new directory( parentPath, name, contents :+ newEntry )

  def findEntry( entryName: String ): dirEntry = {

    @tailrec
    def findEntryHelper( name: String, contentList: List[ dirEntry] ): dirEntry =
      if (contentList.isEmpty) null
      else if ( contentList.head.name.equals( name ) ) contentList.head
      else  findEntryHelper(name, contentList.tail)

    findEntryHelper( entryName, contents )
  }

  def isRoot: Boolean = parentPath.isEmpty

  def replaceEntry( entryName: String, newEntry: dirEntry ): directory =
    new directory( parentPath, name, contents.filter( e => !e.name.equals( entryName ) ) :+ newEntry )

  def asDirectory: directory = this
  def asFile: file = throw new FileSystemException( "A directory cannot be converted to a file" )

  def isDirectory: Boolean = true
  def isFile: Boolean = false

  def getType: String = "Directory"
}

object directory {

  val SEPARATOR = "/"
  val ROOT_PATH = "/"

  def ROOT: directory = directory.empty( "", "" )

  def empty( parentPath: String, name: String ) =
    new directory( parentPath, name, List() )
}
