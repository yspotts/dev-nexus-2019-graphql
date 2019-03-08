package com.yspotts.graphqlexample.demo

import com.google.common.base.Charsets
import com.google.common.io.Resources
import graphql.GraphQL
import graphql.schema.GraphQLSchema
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct
import java.io.IOException

import graphql.schema.idl.TypeRuntimeWiring.newTypeWiring

@Component
class GraphQLProvider {

    private var graphQL: GraphQL? = null

    @Autowired
    internal var graphQLDataFetchers: GraphQLDataFetchers? = null

    @Bean
    fun graphQL(): GraphQL? {
        return graphQL
    }

    @PostConstruct
    @Throws(IOException::class)
    fun init() {
        val url = Resources.getResource("schema.graphqls")
        val sdl = Resources.toString(url, Charsets.UTF_8)
        val graphQLSchema = buildSchema(sdl)
        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build()
    }

    private fun buildSchema(sdl: String): GraphQLSchema {
        val typeRegistry = SchemaParser().parse(sdl)
        val runtimeWiring = buildWiring()
        val schemaGenerator = SchemaGenerator()
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring)
    }

    private fun buildWiring(): RuntimeWiring {
        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring("Query")
                        .dataFetcher("book", graphQLDataFetchers!!.bookFetcher))
                .type(newTypeWiring("Book")
                        .dataFetcher("author", graphQLDataFetchers!!.authorDataFetcher))
                .type(newTypeWiring("Mutation")
                        .dataFetcher("createAuthor", graphQLDataFetchers!!.createAuthorDataFetcher))

                .build()
    }
}
