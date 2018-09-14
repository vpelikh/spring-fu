package org.springframework.boot.kofu.web

import org.springframework.boot.kofu.AbstractModule
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.registerBean
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

/**
 * @author Ireneusz Kozłowski
 */
class CorsModule(
	private val defaults: Boolean = true,
	private val init: CorsModule.() -> Unit
) : AbstractModule() {

	private val configuration = UrlBasedCorsConfigurationSource()

	override fun registerBeans(context: GenericApplicationContext) {
		init()
		context.registerBean("corsFilter") {
			CorsWebFilter(configuration)
		}
	}

	operator fun String.invoke(defaults: Boolean = this@CorsModule.defaults, init: CorsConfigurationDsl.() -> Unit) {
		val corsConfigurationDsl = CorsConfigurationDsl(defaults)
		corsConfigurationDsl.init()
		configuration.registerCorsConfiguration(this, corsConfigurationDsl.corsConfiguration)
	}
}

fun WebFluxServerModule.cors(
	defaults: Boolean = true,
	init: CorsModule.() -> Unit = {}) {
	initializers.add(CorsModule(defaults, init))
}
