package com.example.rssfeedparsinginkotlin

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream
import java.lang.IllegalStateException

data class Question(val title: String?, val summary: String?) {
}

class XMLParser {
    private val ns: String? = null

    fun parse(inputStream: InputStream): List<Question>{
        inputStream.use { inputStream ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()
            return readSongsRssFeed(parser)
        }
    }

    private fun readSongsRssFeed(parser: XmlPullParser): List<Question>{
        val songs = mutableListOf<Question>()

        parser.require(XmlPullParser.START_TAG, ns, "feed")

        while (parser.next() != XmlPullParser.END_TAG){
            if(parser.eventType != XmlPullParser.START_TAG){
                continue
            }
            if(parser.name == "entry"){
                parser.require(XmlPullParser.START_TAG, ns, "entry")
                var title: String? = null
                var summary: String? = null
                while (parser.next() != XmlPullParser.END_TAG){
                    if(parser.eventType != XmlPullParser.START_TAG){
                        continue
                    }
                    when(parser.name){
                        "title" -> title = readTitle(parser)
                        "summary" -> summary = readSummary(parser)
                        else -> skip(parser)
                    }
                }
                songs.add(Question(title,summary))
            } else{
                skip(parser)
            }
        }

        return songs
    }

    private fun readTitle(parser: XmlPullParser): String{
        parser.require(XmlPullParser.START_TAG, ns, "title")
        val title = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "title")
        return title
    }

    private fun readSummary(parser: XmlPullParser): String{
        parser.require(XmlPullParser.START_TAG, ns, "summary")
        val summary = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "summary")
        return summary
    }

    private fun readText(parser: XmlPullParser): String{
        var result = ""
        if(parser.next() == XmlPullParser.TEXT){
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    private fun skip(parser: XmlPullParser){
        if(parser.eventType != XmlPullParser.START_TAG){
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0){
            when(parser.next()){
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}