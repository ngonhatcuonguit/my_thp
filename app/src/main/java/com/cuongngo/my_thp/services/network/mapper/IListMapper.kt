package com.cuongngo.my_thp.services.network.mapper

interface ListMapper<I, O> : IMapper<List<I>, List<O>>

open class ListMapperImpl<I, O>(private val mapper: IMapper<I, O>) : ListMapper<I, O> {
    override fun map(input: List<I>): List<O> {
        return input.map { mapper.map(it) }
    }
}