package dev.panuszewski.gradle.jumper

internal inline fun <reified T> Any.takeIfInstance() =
    this as? T