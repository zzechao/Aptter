package com.zhouz.aptter

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class MethodProcessor(val name: String = "Method")
