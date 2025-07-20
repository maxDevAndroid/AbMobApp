package com.globo.admoblibray.util

import android.app.Activity
import android.content.Context

internal fun Context.getActivity(): Activity? = when (this) {
    is Activity -> this
    is android.content.ContextWrapper -> baseContext.getActivity()
    else -> null
}