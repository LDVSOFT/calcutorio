package net.ldvsoft.calcutorio.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

fun calcutorioObjectMapper(): ObjectMapper = jacksonObjectMapper().findAndRegisterModules()