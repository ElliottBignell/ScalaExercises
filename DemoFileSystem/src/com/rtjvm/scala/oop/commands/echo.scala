package com.rtjvm.scala.oop.commands
import com.rtjvm.scala.oop.files.{directory, file}
import com.rtjvm.scala.oop.filesystem.state

import scala.annotation.tailrec

class echo( args: Array[ String ] ) extends Command {

  override def apply(s: state): state = {
    /*
      if no args, state
      else if 1 arg print to console
      else if >1 args
      {
        operator = next to last arg
        >
          echo to a file
        >>
          append to a file, create if not there
        else
          echo everything to console
      }
     */

    if ( args.isEmpty ) s
    else if ( 1 == args.length ) s.setMessage( args( 0 ) )
    else {

      val operator = args( args.length - 2 )
      val filename =  args( args.length - 1 )
      val contents = createContent( args, args.length - 2)

      if ( ">>".equals( operator ) )
        doEcho( s, contents, filename, append = true )
      else if ( ">".equals( operator ) )
        doEcho( s, contents, filename, append = false )
      else
        s.setMessage( createContent( args, args.length ) )
    }
  }

  def getRootAfterEcho( currentDirectory: directory, path: List[ String ], contents: String, append: Boolean ): directory = {

    /*
      if path is empty, fail ( currentDir )
      else if no more things to explore = path.tal.isEmpty
        find the file to create/add content to
        if file not found, create file
        else if the entry is actually a directory then fail
        else
          replace or append content to the file
          replace the entry with the filename with the NEW file
        else
          find the next directory to navigate
          call gRAE recursively on that

          is recursive call failed, fail
          else replace entry with the NEW directory after the recursive call

     */

    if ( path.isEmpty ) currentDirectory
    else if ( path.tail.isEmpty ) {

      val dirEntry = currentDirectory.findEntry( path.head )

      if ( null == dirEntry )
        currentDirectory.addEntry(
          new file( currentDirectory.path, path.head, contents )
        )
      else if ( dirEntry.isDirectory ) currentDirectory
      else if ( append ) currentDirectory.replaceEntry( path.head, dirEntry.asFile.appendContents( contents ) )
      else currentDirectory.replaceEntry( path.head, dirEntry.asFile.setContents( contents ) )
    }
    else {

      val nextDirectory = currentDirectory.findEntry( path.head ).asDirectory
      val newNextDirectory = getRootAfterEcho( nextDirectory, path.tail, contents, append )

      if ( newNextDirectory == nextDirectory ) currentDirectory
      else currentDirectory.replaceEntry( path.head, newNextDirectory )
    }
  }

  def doEcho( s: state, contents: String, filename: String, append: Boolean ) = {

    if ( filename.contains( directory.SEPARATOR ) )
      s.setMessage( "Echo: filename must not contain separators ")
    else {

      val newRoot: directory = getRootAfterEcho( s.root, s.wd.getAllFoldersInPath :+ filename, contents, append )

      if ( newRoot == s.root )
        s.setMessage( filename + ":  No such file" )
      else
        state( newRoot, newRoot.findDescendent( s.wd.getAllFoldersInPath ) )
    }
  }

  // topindex NON-INCLUSIVE
  def createContent( agrs: Array[ String ], topindex: Int ): String = {

    @tailrec
    def createContentHelper( currentIndex: Int, accumulator: String ): String = {

      if (currentIndex >= topindex ) accumulator
      else createContentHelper( currentIndex + 1, accumulator + " " + args( currentIndex ) )
    }

    createContentHelper( 0, "" )
  }
}
