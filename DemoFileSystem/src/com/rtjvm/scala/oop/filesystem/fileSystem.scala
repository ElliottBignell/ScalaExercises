package com.rtjvm.scala.oop.filesystem

import java.util.Scanner

import com.rtjvm.scala.oop.commands.Command
import com.rtjvm.scala.oop.files.directory

object fileSystem extends App {

  val root = directory.ROOT

  // [ 1, 2, 3, 4]
  /*
    0 (op) 1 => 1
    1 (op) 2 => 3
    3 (op) => 6
    6 (op) => 4 your last value 10

    List( 1, 2, 3, 4 ).foldLeft( 0 )( ( x, y ) => x + y )
   */
  io.Source.stdin.getLines().foldLeft( state( root, root ) )( ( currentState, newLine ) => {

    currentState.show
    val newState = Command.from( newLine ).apply( currentState )
    newState
  })

//   var s = state( root, root )
//   val scanner = new Scanner( System.in )
//
//   while ( true ) {
//
//     s.show
//     val input = scanner.nextLine()
//     s = Command.from( input ).apply( s )
//   }
}
