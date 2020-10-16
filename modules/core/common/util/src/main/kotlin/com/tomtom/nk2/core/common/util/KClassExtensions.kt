/*
 * Copyright (c) 2020 - 2020 TomTom N.V. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom N.V. and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * licensee agreement between you and TomTom. If you are the licensee, you are only permitted
 * to use this Software in accordance with the terms of your license agreement. If you are
 * not the licensee then you are not authorised to use this software in any manner and should
 * immediately return it to TomTom N.V.
 */

package com.tomtom.nk2.core.common.util

import kotlin.reflect.KClass

val KClass<*>.packageName: String
    get() = this.java.`package`!!.name

/**
 * @return The immediately enclosing class of the underlying class. If the underlying class is a top
 *     level class this method returns `null`.
 */
val KClass<*>.enclosingClass: KClass<*>?
    get() = javaObjectType.enclosingClass?.kotlin
