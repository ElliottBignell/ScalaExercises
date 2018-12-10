package com.rtjvm.scala.oop.commands
import com.rtjvm.scala.oop.files.directory
import com.rtjvm.scala.oop.filesystem.state

class rm( name: String ) extends Command {

  override def apply(s: state): state = {

    // 1. get working dir
    val wd = s.wd

    // 2. get absolute path
    val absolutePath =
      if ( name.startsWith( directory.SEPARATOR ) ) name
      else if ( wd.isRoot ) wd.path + name
      else wd.path+ directory.SEPARATOR + name

    // 3. do some checks
    if ( directory.ROOT_PATH.equals( absolutePath ) )
      s.setMessage( "Nukular war not supported yet!" )
    else
      doRm( s, absolutePath )
  }

  def doRm( s: state, path: String ): state = {

    def rmHelper( currentDirectory: directory, path: List[ String ] ): directory = {

      if ( path.isEmpty ) currentDirectory
      else if ( path.tail.isEmpty ) currentDirectory.removeEntry( path.head )
      else {

        val nextDirectory = currentDirectory.findEntry( path.head )

        if ( !nextDirectory.isDirectory ) currentDirectory
        else {

          val newNextDirectory = rmHelper( nextDirectory.asDirectory, path.tail )

          if (newNextDirectory == nextDirectory ) currentDirectory
          else currentDirectory.replaceEntry( path.head, newNextDirectory )
        }
      }
    }
    // 4. find the entry to remove
    // 5. update structure like we do for mkdir

    val tokens = path.substring( 1 ).split( directory.SEPARATOR ).toList
    val newRoot: directory = rmHelper( s.root, tokens )

    if ( newRoot == s.root )
      s.setMessage( path + ": no such file of directory!" )
    else
      state( newRoot, newRoot.findDescendent( s.wd.path.substring( 1 ) ) )
  }
}
