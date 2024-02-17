package dev.panuszewski.gradlenavigation

inline fun <reified T> Any.takeIfInstance() = this as? T

/**
 * Just to have multiline AND expressions nicely aligned
 *
 * Traditional:
 * ```
 * a == 1 &&
 *     b == 2
 * ```
 *
 * With this method:
 * ```
 * and(
 *     a == 1,
 *     b == 2
 * )
 * ```
 */
internal fun and(vararg booleans: Boolean) =
    booleans.reduce { acc, elem -> acc && elem }

/**
 * Just to have multiline OR expressions nicely aligned
 *
 * Traditional:
 * ```
 * a == 1 ||
 *     b == 2
 * ```
 *
 * With this method:
 * ```
 * or(
 *     a == 1,
 *     b == 2
 * )
 * ```
 */
internal fun or(vararg booleans: Boolean) =
    booleans.reduce { acc, elem -> acc || elem }