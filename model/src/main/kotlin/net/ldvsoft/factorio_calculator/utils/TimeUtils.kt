package net.ldvsoft.factorio_calculator.utils

import java.time.Duration

operator fun Duration.div(that: Duration): Double = (this.toMillis().toDouble()) / that.toMillis()