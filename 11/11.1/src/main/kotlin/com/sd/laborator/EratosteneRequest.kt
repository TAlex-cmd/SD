package com.sd.laborator

import io.micronaut.core.annotation.Introspected

@Introspected
class EratosteneRequest {
	private lateinit var numbers: List<Int>

	fun getNumbers(): List<Int> {
		return numbers
	}
}