package com.yspotts.graphqlexample.demo

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component

@Component
class GraphQLDataFetchers {

    val bookFetcher: DataFetcher<*> =
        DataFetcher { dataFetchingEnvironment : DataFetchingEnvironment ->
            if (dataFetchingEnvironment.containsArgument("id")) {
                val bookId = dataFetchingEnvironment.getArgument<String>("id")
                listOf(books.find { it.id == bookId })
            } else {
                books
            }
        }

    val authorDataFetcher: DataFetcher<*> =
        DataFetcher { dataFetchingEnvironment ->
            val book = dataFetchingEnvironment.getSource<Book>()
            authors.find { it == book.author }
        }

    val createAuthorDataFetcher: DataFetcher<*> =
        DataFetcher { dataFetchingEnvironment ->
            val id = dataFetchingEnvironment.getArgument<String>("id")
            val firstName = dataFetchingEnvironment.getArgument<String>("firstName")
            val lastName = dataFetchingEnvironment.getArgument<String>("lastName")
            val author = Author(id, firstName, lastName)

            println ("Creating new author $author")
            author
        }


    companion object {

         private val authors = listOf(
            Author("author-1", "Joanne", "Rowling"),
            Author("author-2", "Herman","Melville"),
            Author("author-3", "Anne", "Rice")
        )

        private val books = listOf(
            Book("book-1", "Harry Potter and the Philosopher's Stone", 223, authors[0]),
            Book("book-2", "Moby Dick", 635, authors[1]),
            Book("book-3", "Interview with the vampire", 371, authors[2])
        )
    }
}
