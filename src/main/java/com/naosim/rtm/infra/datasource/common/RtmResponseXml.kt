package com.naosim.rtm.infra.datasource.common
import org.w3c.dom.Element

class RtmResponseXml(val rootElement: Element) {
    val isOk = rootElement.getAttribute("stat") == "ok"
    val isFailed = !isOk
    val failedResponse: RtmResponseFailed? = if(!isOk) RtmResponseFailed(getFirstElementByTagName("err").getAttribute("code"), getFirstElementByTagName("err").getAttribute("msg")) else  null

    fun getFirstElementByTagName(tagName: String): Element {
        return rootElement.getElementsByTagName(tagName).item(0) as Element
    }

    fun getFirstElementValueByTagName(tagName: String): String {
        return getFirstElementByTagName(tagName).firstChild.nodeValue.orEmpty()
    }
}

class RtmResponseFailed(val code: String, val msg: String) {}