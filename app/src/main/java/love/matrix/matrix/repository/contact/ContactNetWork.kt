package love.matrix.matrix.repository.contact

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import love.matrix.matrix.data.ContactList
import java.net.URL

private const val JSON_URL ="https://www.wanandroid.com/article/top/json"
//private const val JSON_URL =
   // "https://gist.githubusercontent.com/anacoimbrag/50cb96b6c57100c1881be36d98bc4d2a/raw/1c2176ffaafc54baeb82f62d81e7a3fa4615244a/contactData.json"

suspend fun getContacts(): ContactList = withContext(Dispatchers.IO) {

    //使用UTF-8或指定的[charset]以字符串形式读取此URL的全部内容。
    val json = URL(JSON_URL).readText()
    Log.d("getContacts", "Read: $json")

    Gson().fromJson(json, ContactList::class.java)
}