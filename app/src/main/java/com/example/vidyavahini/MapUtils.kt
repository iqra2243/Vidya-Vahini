package com.example.vidyavahini

import android.content.Context
import android.content.Intent
import android.net.Uri

object MapUtils {

    fun openLocation(context: Context, stop: String) {
        val query = when (stop.lowercase()) {
            "village" -> "Village bus stop"
            "bridge" -> "Bridge bus stop"
            "temple" -> "Temple near bus stop"
            "market" -> "Market bus stop"
            "college" -> "College campus"
            else -> stop
        }

        val uri = Uri.parse("geo:0,0?q=${Uri.encode(query)}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")

        context.startActivity(intent)
    }
}