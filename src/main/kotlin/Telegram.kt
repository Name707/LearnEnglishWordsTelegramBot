import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

fun main(args: Array<String>) {

    val botToken = args[0]
    var updateId = 0
    var chatId = 0
    var textFromMessage = ""

    while (true) {
        Thread.sleep(2000)
        val updates: String = getUpdates(botToken, updateId)
        println(updates)

        sendMessage(chatId, botToken, textFromMessage)

        val updateIdFromLastUpdate: Regex = "\"update_id\":(.+?),\n\"".toRegex()
        val updateIdResultFromLastUpdate: MatchResult? = updateIdFromLastUpdate.find(updates)
        val updateIdGroups = updateIdResultFromLastUpdate?.groups
        val updateIdNumber = updateIdGroups?.get(1)?.value?.toInt()
        if (updateIdNumber == -1) continue
        if (updateIdNumber != null) {
            updateId = updateIdNumber + 1
        }

        val regexOfChatId: Regex = "\"chat\":\\{\"id\":(.+?),".toRegex()
        val chatIdResultFromLastUpdate: MatchResult? = regexOfChatId.find(updates)
        val chatIdGroups = chatIdResultFromLastUpdate?.groups
        val chatIdNumber = chatIdGroups?.get(1)?.value?.toInt()
        if (chatIdNumber != null) {
            chatId = chatIdNumber
        }

        val regexOfMessage: Regex = "\"text\":\"(.+?)\"}".toRegex()
        val messageResultFromLastUpdate: MatchResult? = regexOfMessage.find(updates)
        val messageTextGroups = messageResultFromLastUpdate?.groups
        val messageText = messageTextGroups?.get(1)?.value
        if (messageText != null) {
            textFromMessage = messageText
        }


    }
}

fun getUpdates(botToken: String, updateId: Int): String {
    val urlGetUpdates = "https://api.telegram.org/bot$botToken/getUpdates?offset=$updateId"
    val client: HttpClient = HttpClient.newBuilder().build()
    val request = HttpRequest.newBuilder().uri(URI.create(urlGetUpdates)).build()
    val response: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())
    return response.body()
}

fun sendMessage(chatId: Int, botToken: String, text: String): String {
    val urlSendMessage = "https://api.telegram.org/bot$botToken/sendMessage?chat_id=$chatId&text=$text"
    val client: HttpClient = HttpClient.newBuilder().build()
    val request = HttpRequest.newBuilder().uri(URI.create((urlSendMessage))).build()
    val response: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())
    return response.body()

}