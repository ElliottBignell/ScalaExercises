package com.rtjvm.scala.oop.commands

import com.rtjvm.scala.oop.filesystem.state

trait Command extends ( state => state ) {

  def apply( s: state ): state
}

object Command {

  val MKDIR = "mkdir"
  val LS = "ls"
  val PWD = "pwd"
  val TOUCH = "touch"
  val CD = "cd"
  val RM = "rm"
  val CAT = "cat"
  val ECHO = "echo"

  def emptyCommand: Command = new Command {
    override def apply(s: state): state = s
  }
  def incompleteCommand( name: String ): Command = new Command {
    override def apply(s: state): state = {
      s.setMessage( name + ": incomplete command" )
    }
  }

  def from( input: String ): Command = {

    val tokens: Array[ String ] = input.split( " " )

    if ( input.isEmpty || tokens.isEmpty ) emptyCommand
    else tokens( 0 ) match {
      case MKDIR =>
        if ( tokens.length < 2 ) incompleteCommand( MKDIR )
        else new mkdir( tokens( 1 ) )
      case LS =>
        new ls
      case PWD =>
        new pwd
      case TOUCH =>
        if ( tokens.length < 2 ) incompleteCommand( TOUCH )
        else new touch( tokens( 1 ) )
      case CD =>
        if ( tokens.length < 2 ) incompleteCommand( CD )
        else new cd( tokens( 1 ) )
      case RM =>
        if ( tokens.length < 2 ) incompleteCommand( RM )
        else new rm( tokens( 1 ) )
      case ECHO =>
        if ( tokens.length < 2 ) incompleteCommand( ECHO )
        else new echo( tokens.tail )
      case CAT =>
        if ( tokens.length < 2 ) incompleteCommand( CAT )
        else new cat( tokens( 1 ) )
      case _ =>
        new unknownCommand
    }
  }
}