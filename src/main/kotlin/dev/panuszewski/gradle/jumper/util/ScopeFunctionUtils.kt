package dev.panuszewski.gradle.jumper.util

internal inline fun <reified T> Any.takeIfInstance() =
    this as? T