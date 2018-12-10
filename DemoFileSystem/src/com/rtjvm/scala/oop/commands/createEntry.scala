package com.rtjvm.scala.oop.commands

import com.rtjvm.scala.oop.files.{dirEntry, directory}
import com.rtjvm.scala.oop.filesystem.state

abstract class createEntry( name: String ) extends Command {

  override def apply(s: state): state = {

    val wd = s.wd

    if ( wd.hasEntry( name ) ) {
      s.setMessage("Entry " + name + " already exists!")
    }
    else if ( name.contains( directory.SEPARATOR ) ) {
      s.setMessage( name + " must not comtain separators! ")
    }
    else if ( checkIllegal( name ) ) {
      s.setMessage( name + ": illegal entry name!" )
    }
    else {
      doCreateEntry( s, name )
    }
  }

  def checkIllegal( name: String ): Boolean = {
    name.contains( "." )
  }

  def doCreateEntry( s: state, name: String) : state = {

    def updateStructure( currentDirectory: directory, path: List[ String ], newEntry: dirEntry ) : directory = {
      if ( path.isEmpty ) currentDirectory.addEntry( newEntry )
      else {
        val oldEntry = currentDirectory.findEntry( path.head ).asDirectory

        currentDirectory.replaceEntry( oldEntry.name, updateStructure( oldEntry, path.tail, newEntry ) )
      }
    }

    val wd = s.wd

    // 1. all the directories in the full path
    val allDirsInPath = wd.getAllFoldersInPath

    // 2. create new directory entry in the working dir
    // TOFO implement this
    val newEntry:dirEntry = createSpecificEntry( s )

    // 3. update the whole directory structure starting from the root
    // (the directory structure is IMMUTABLE)
    val newRoot = updateStructure( s.root, allDirsInPath, newEntry )

    // 4. find new working directory INSTANCE given wd's full path, in the new directory structure
    val newWd = newRoot.findDescendent( allDirsInPath )

    state( newRoot, newWd )
  }

  def createSpecificEntry( s: state ): dirEntry
}
