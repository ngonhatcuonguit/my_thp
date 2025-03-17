package com.cuongngo.my_thp.services.network.mapper

interface IMapper<in I,out O>{
    fun map(input:I):O
}