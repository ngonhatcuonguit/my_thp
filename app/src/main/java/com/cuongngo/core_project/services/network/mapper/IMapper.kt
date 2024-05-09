package com.cuongngo.core_project.services.network.mapper

interface IMapper<in I,out O>{
    fun map(input:I):O
}