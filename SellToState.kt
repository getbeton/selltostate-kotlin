// Generated from OpenAPI. Do not edit methods by hand.
package com.selltostate

import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

const val DEFAULT_BASE_URL = "https://app.selltostate.com"

class AuthError(val body: String?) : Exception("unauthorized")
class PaymentError(val body: String?) : Exception("payment_required") {
    val payment_link: String? = field(body, "payment_link")
    val plan: String? = field(body, "plan")
    val machine_payment: String? = field(body, "machine_payment")
}
class RateLimitError(val body: String?) : Exception("rate_limited")

fun interface Transport {
    fun exchange(url: String, method: String, headers: Map<String, String>, body: String?): Pair<Int, String>
}

class SellToState(
    apiKey: String? = null,
    private val clientId: String? = null,
    baseUrl: String? = null,
    private val transport: Transport? = null,
) {
    private val apiKey: String? = apiKey ?: System.getenv("STS_API_KEY")
    private val baseUrl: String = (baseUrl ?: System.getenv("STS_API_URL") ?: DEFAULT_BASE_URL).trimEnd('/')

    fun request(method: String, path: String, args: Map<String, Any?>, pathParams: List<String>): Any? {
        var urlPath = path
        for (name in pathParams) {
            val value = args[name] ?: throw IllegalArgumentException("missing path param: " + name)
            urlPath = urlPath.replace("{" + name + "}", URLEncoder.encode(value.toString(), "UTF-8"))
        }
        val qs = args.entries
            .filter { it.key != "body" && it.key !in pathParams && it.value != null && it.value != "" }
            .joinToString("&") { URLEncoder.encode(it.key, "UTF-8") + "=" + URLEncoder.encode(it.value.toString(), "UTF-8") }
        val url = baseUrl + urlPath + if (qs.isEmpty()) "" else "?" + qs
        val headers = mutableMapOf("accept" to "application/json")
        if (!apiKey.isNullOrEmpty()) headers["x-api-key"] = apiKey
        else if (!clientId.isNullOrEmpty()) headers["authorization"] = "Bearer client:" + clientId
        val body = if (args["body"] != null && method != "GET") args["body"].toString() else null
        if (body != null) headers["content-type"] = "application/json"
        val (status, text) = if (transport != null) {
            transport.exchange(url, method, headers, body)
        } else {
            val builder = HttpRequest.newBuilder(URI.create(url)).method(
                method,
                if (body == null) HttpRequest.BodyPublishers.noBody() else HttpRequest.BodyPublishers.ofString(body),
            )
            headers.forEach { (k, v) -> builder.header(k, v) }
            val res = HttpClient.newHttpClient().send(builder.build(), HttpResponse.BodyHandlers.ofString())
            res.statusCode() to res.body()
        }
        when (status) {
            401 -> throw AuthError(text)
            402 -> throw PaymentError(text)
            429 -> throw RateLimitError(text)
        }
        if (status >= 400) throw RuntimeException("HTTP " + status)
        return text
    }

    fun searchAwards(args: Map<String, Any?> = emptyMap()): Any? {
        return request("GET", "/api/v1/awards", args, listOf())
    }

    fun exportAwardsCsv(args: Map<String, Any?> = emptyMap()): Any? {
        return request("GET", "/api/v1/awards.csv", args, listOf())
    }

    fun getPrice(args: Map<String, Any?> = emptyMap()): Any? {
        return request("GET", "/api/v1/price", args, listOf())
    }

    fun payAutonomously(args: Map<String, Any?> = emptyMap()): Any? {
        return request("POST", "/api/v1/pay", args, listOf())
    }

    fun registerClient(args: Map<String, Any?> = emptyMap()): Any? {
        return request("POST", "/api/oidc/register", args, listOf())
    }

    fun mcp(args: Map<String, Any?> = emptyMap()): Any? {
        return request("POST", "/api/mcp", args, listOf())
    }

    fun protectedResourceMetadata(args: Map<String, Any?> = emptyMap()): Any? {
        return request("GET", "/.well-known/oauth-protected-resource", args, listOf())
    }

    fun authorizationServerMetadata(args: Map<String, Any?> = emptyMap()): Any? {
        return request("GET", "/.well-known/oauth-authorization-server", args, listOf())
    }

    fun whoami(args: Map<String, Any?> = emptyMap()): Any? {
        return request("GET", "/api/v1/whoami", args, listOf())
    }

    fun search(args: Map<String, Any?> = emptyMap()): Any? {
        return request("GET", "/api/v1/search", args, listOf())
    }

    fun listBuyers(args: Map<String, Any?> = emptyMap()): Any? {
        return request("GET", "/api/v1/buyers", args, listOf())
    }

    fun getBuyer(args: Map<String, Any?> = emptyMap()): Any? {
        return request("GET", "/api/v1/buyers/{slug}", args, listOf("slug"))
    }

    fun listSuppliers(args: Map<String, Any?> = emptyMap()): Any? {
        return request("GET", "/api/v1/suppliers", args, listOf())
    }

    fun getSupplier(args: Map<String, Any?> = emptyMap()): Any? {
        return request("GET", "/api/v1/suppliers/{slug}", args, listOf("slug"))
    }

    fun listSectors(args: Map<String, Any?> = emptyMap()): Any? {
        return request("GET", "/api/v1/sectors", args, listOf())
    }

    fun getSector(args: Map<String, Any?> = emptyMap()): Any? {
        return request("GET", "/api/v1/sectors/{slug}", args, listOf("slug"))
    }

    fun getTender(args: Map<String, Any?> = emptyMap()): Any? {
        return request("GET", "/api/v1/tenders/{id}", args, listOf("id"))
    }

    fun getLists(args: Map<String, Any?> = emptyMap()): Any? {
        return request("GET", "/api/v1/lists", args, listOf())
    }

    fun createList(args: Map<String, Any?> = emptyMap()): Any? {
        return request("POST", "/api/v1/lists", args, listOf())
    }

    fun getList(args: Map<String, Any?> = emptyMap()): Any? {
        return request("GET", "/api/v1/lists/{id}", args, listOf("id"))
    }

    fun renameList(args: Map<String, Any?> = emptyMap()): Any? {
        return request("PATCH", "/api/v1/lists/{id}", args, listOf("id"))
    }

    fun deleteList(args: Map<String, Any?> = emptyMap()): Any? {
        return request("DELETE", "/api/v1/lists/{id}", args, listOf("id"))
    }

    fun addToList(args: Map<String, Any?> = emptyMap()): Any? {
        return request("POST", "/api/v1/lists/{id}/items", args, listOf("id"))
    }

    fun removeFromList(args: Map<String, Any?> = emptyMap()): Any? {
        return request("DELETE", "/api/v1/lists/{id}/items", args, listOf("id"))
    }

    fun getSavedSearches(args: Map<String, Any?> = emptyMap()): Any? {
        return request("GET", "/api/v1/saved-searches", args, listOf())
    }

    fun saveSearch(args: Map<String, Any?> = emptyMap()): Any? {
        return request("POST", "/api/v1/saved-searches", args, listOf())
    }

    fun getSavedSearch(args: Map<String, Any?> = emptyMap()): Any? {
        return request("GET", "/api/v1/saved-searches/{id}", args, listOf("id"))
    }

    fun renameSavedSearch(args: Map<String, Any?> = emptyMap()): Any? {
        return request("PATCH", "/api/v1/saved-searches/{id}", args, listOf("id"))
    }

    fun deleteSavedSearch(args: Map<String, Any?> = emptyMap()): Any? {
        return request("DELETE", "/api/v1/saved-searches/{id}", args, listOf("id"))
    }
}

private fun field(json: String?, name: String): String? {
    if (json == null) return null
    val needle = "\"" + name + "\":\""
    val at = json.indexOf(needle)
    if (at < 0) return null
    val start = at + needle.length
    val end = json.indexOf("\"", start)
    return if (end < 0) null else json.substring(start, end)
}
