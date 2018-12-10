package com.rtjvm.scala.oop.commands
import com.rtjvm.scala.oop.files.{dirEntry, directory}
import com.rtjvm.scala.oop.filesystem.state

import scala.annotation.tailrec

class cd( dir: String ) extends Command {

  override def apply(s: state): state = {
    /*
      cd /something/somethingelse/.../
      cd a/b/c -> relative to cwd
     */

    // 1. Find root
    val root = s.root
    val wd = s.wd

    // 2. Find the absolute path of the directory I want to cd to
    val absolutePath =
       if ( dir.startsWith( directory.SEPARATOR ) ) dir
       else if ( wd.isRoot ) wd.path + dir
       else wd.path + directory.SEPARATOR + dir

    // 3. Find the directory top CD to, given the path
    val destinationDirectory = doFindEntry( root, absolutePath )

    // 4. Change the state given the new directory
    if ( destinationDirectory == null || !destinationDirectory.isDirectory )
      s.setMessage( dir + ": No such directory" )
    else
      state( root, destinationDirectory.asDirectory )
  }

  def doFindEntry( root: directory, path: String ): dirEntry = {

    @tailrec
    def findEntryHelper( currentDirectory: directory, path: List[String] ): dirEntry =
      if ( path.isEmpty || path.head.isEmpty ) currentDirectory
      else if ( path.tail.isEmpty ) currentDirectory.findEntry( path.head )
      else {
        val nextDir = currentDirectory.findEntry( path.head )
        if ( nextDir == null || !nextDir.isDirectory ) null
        else findEntryHelper( nextDir.asDirectory, path.tail )
      }

    @tailrec
    def collapseRelativeTokens( path: List[ String ], result: List[ String ] ): List[ String ] = {

      if ( path.isEmpty ) result
      else if ( ".".equals( path.head ) ) collapseRelativeTokens( path.tail, result )
      else if ( "..".equals( path.head ) ) {

        if ( result.isEmpty ) null
        else collapseRelativeTokens( path.tail, result.init )
      }
      else collapseRelativeTokens( path.tail, result :+ path.head )
    }

    // 1. tokens
    val tokens: List[ String ] = path.substring( 1 ).split( directory.SEPARATOR ).toList

    // 1b. eliminate or collapse relative tokens
    /*
      /a => [ "a", "." ] => [ "a" ]
      [ "a", "b", ".", "."] => [ "a", "b" ]

      /a/../ => [ "a", ".." ] => []
      /a/b/.. => [ "a", "b", ".."] => [ "a" ]
     */

    val newTokens = collapseRelativeTokens( tokens, List() )

    // 2. navigate to the correct entry
    if ( newTokens == null ) null
    else findEntryHelper( root, newTokens )
  }
}
