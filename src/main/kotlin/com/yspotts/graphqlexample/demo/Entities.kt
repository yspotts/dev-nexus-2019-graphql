package com.yspotts.graphqlexample.demo

data class Book (
  val id: String,
  val name: String,
  val pageCount: Int,
  val author: Author
)

data class Author (
  val id: String,
  val firstName: String,
  val lastName: String
)