package com.rtjvm.scala.oop.filesystem

import com.rtjvm.scala.oop.files.directory

class state( val root: directory, val wd: directory, val output: String ) {

  def show: Unit = {

    println( output )
    print( state.SHELL_TOKEN )
  }

  def setMessage( message: String ): state =
    state( root, wd, message )
}

object state {

  val SHELL_TOKEN = "$ "

  def apply( root: directory, wd: directory, output: String = "" ): state =
    new state( root, wd, output )
}

