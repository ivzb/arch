package com.ivzb.arch.data.links

import android.webkit.URLUtil
import com.ivzb.arch.model.LinkMetaData
import org.jsoup.Jsoup
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException

object LinkMetaDataJsonParser {

    @Throws(IOException::class)
    fun fetch(url: String): LinkMetaData {
        val metaData = LinkMetaData(url = url)

        val doc = Jsoup.connect(url)
            .timeout(5 * 1000)
            .get()

        val elements = doc.getElementsByTag("meta")

        var title: String? = doc.select("meta[property=og:title]").attr("content")

        if (title == null || title.isEmpty()) {
            title = doc.title()
        }

        metaData.title = title ?: ""

        val imageElements = doc.select("meta[property=og:image]")

        if (imageElements.size > 0) {
            val image = imageElements.attr("content")
            if (!image.isEmpty()) {
                metaData.imageUrl = resolveURL(url, image)
            }
        }

        if (metaData.imageUrl == null || metaData.imageUrl!!.isEmpty()) {
            var src = doc.select("link[rel=image_src]").attr("href")

            if (src.isNotEmpty()) {
                metaData.imageUrl = resolveURL(url, src)
            } else {
                src = doc.select("link[rel=apple-touch-icon]").attr("href")

                if (src.isNotEmpty()) {
                    metaData.imageUrl = resolveURL(url, src)
                } else {
                    src = doc.select("link[rel=icon]").attr("href")

                    if (src.isNotEmpty()) {
                        metaData.imageUrl = resolveURL(url, src)
                    }
                }
            }
        }

        for (element in elements) {
            if (element.hasAttr("property")) {
                val property = element.attr("property").toString().trim { it <= ' ' }

                if (property == "og:site_name") {
                    metaData.sitename = element.attr("content").toString()
                }
            }
        }

        return metaData
    }

    private fun resolveURL(url: String, part: String): String {
        if (URLUtil.isValidUrl(part)) {
            return part
        }

        return try{
            URI(url).resolve(part).toString()
        } catch (e: URISyntaxException) {
            ""
        }
    }
}
