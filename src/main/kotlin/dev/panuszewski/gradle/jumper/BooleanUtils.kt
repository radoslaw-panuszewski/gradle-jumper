package dev.panuszewski.gradle.jumper

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