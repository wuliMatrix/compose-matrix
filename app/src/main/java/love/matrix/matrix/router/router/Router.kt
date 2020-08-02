package com.github.zsoltk.compose.router

import androidx.compose.Composable
import androidx.compose.Observe
import androidx.compose.ProvidableAmbient
import androidx.compose.Providers
import androidx.compose.ambientOf
import androidx.compose.onCommit
import androidx.compose.remember
import com.github.zsoltk.compose.backpress.AmbientBackPressHandler
import com.github.zsoltk.compose.backpress.BackPressHandler
import com.github.zsoltk.compose.savedinstancestate.AmbientSavedInstanceState
import com.github.zsoltk.compose.savedinstancestate.BundleScope

private fun key(backStackIndex: Int) = //将数字index转变成 “K+index"字符串
    "K$backStackIndex"

private val backStackMap: MutableMap<Any, BackStack<*>> =
    mutableMapOf()

/**
 * Currently only used for deep link based Routing.
 *
 * Can be set to store a list of Routing elements of different types.
 * The idea is that when we walk through this list in sequence - provided that the sequence
 * is correct - we can set the app into any state that is a combination of Routing on different levels.
 *
 * 当前仅用于基于深层链接的路由。可以设置为存储不同类型的路由元素列表。
 * 这个想法是，当我们按顺序浏览此列表时（前提是顺序正确），我们可以将应用设置为处于不同级别上的路由组合的任何状态。
 *
 * See [com.example.lifelike.DeepLinkKt.parseProfileDeepLink] in :app-lifelike module for usage
 * example.
 */
val AmbientRouting: ProvidableAmbient<List<Any>> = ambientOf {
    listOf<Any>()
}

/**
 * Adds back stack functionality with bubbling up fallbacks if the back stack cannot be popped
 * on this level.
 * 如果无法在此级别弹出后置堆栈，则添加带有后备气泡的后置堆栈功能。
 *
 * @param defaultRouting    The default routing to initialise the back stack with
                                 他使用默认路由来初始化后端堆栈
 * @param children          The @Composable to wrap with this BackHandler. It will have access to the back stack.
                             @Composable与此BackHandler一起包装。它将有权访问后堆栈。
 */




//reified  相当于在参数里的clazz: Class<T>，详见：https://juejin.im/post/5cc7eae5e51d4514df420710
//inline  复制代码块到代码块里执行，
// 并且避免在循环的代码块里反复创建相同新的对象 （将对象提取到循环外）
// 使用 inline，内联函数到调用的地方，能减少函数调用造成的额外开销，在循环中尤其有效
//使用 inline 能避免函数的 lambda 形参额外创建 Function 对象
//使用 noinline 可以拒绝形参 lambda 内联
//详见 : https://juejin.im/post/5ccef976f265da038733b377

//我的理解是：就是不断套壳 （（（（（（）））））），由于最里面先执行，所以有堆栈效果

@Composable
inline fun <reified T> Router(//reified 整齐的
   // clazz: Class<T>，
    defaultRouting: T,
    noinline children: @Composable() (BackStack<T>) -> Unit
) {
    Router(T::class.java.name, defaultRouting, children)
}

@Composable
fun <T> Router(
    contextId: String,
    defaultRouting: T,
    children: @Composable() (BackStack<T>) -> Unit
) {
    val route = AmbientRouting.current //注入这个环境变量 路由元素

    val routingFromAmbient = route.firstOrNull() as? T //判断获得的数据是不是它期望的类型

    //takeLast 返回包含最后[n]个元素的列表。 环境里有多少个值
    val downStreamRoute = if (route.size > 1) route.takeLast(route.size - 1) else emptyList()

    val upstreamHandler = AmbientBackPressHandler.current//此环境值记录一此key,route应该就会当value
     //key 为Root.aaa,Root.bbb这种形式
    val localHandler = remember { BackPressHandler("${upstreamHandler.id}.$contextId") }


    val backStack = fetchBackStack(localHandler.id, defaultRouting, routingFromAmbient)

    val handleBackPressHere: () -> Boolean = { localHandler.handle() || backStack.pop() }

    onCommit {
        upstreamHandler.children.add(handleBackPressHere)
        onDispose { upstreamHandler.children.remove(handleBackPressHere) }
    }

    Observe {
        // Not recomposing router on backstack operation
        // 在后堆栈操作中不重组路由器
        BundleScope(key(backStack.lastIndex), autoDispose = false) {
            Providers(
                AmbientBackPressHandler provides localHandler,
                AmbientRouting provides downStreamRoute
            ) {
                children(backStack)
            }
        }
    }
}

@Composable
private fun <T> fetchBackStack(key: String, defaultElement: T, override: T?): BackStack<T> {


    val upstreamBundle = AmbientSavedInstanceState.current//与Bundle打交道时的key
    val onElementRemoved: (Int) -> Unit = { upstreamBundle.remove(key(it)) }

    val existing = backStackMap[key] as BackStack<T>? //将key设置成可观察的 时刻观察现在还有多少key

    return when {
        //路由元素不为空，路由元素交给BackStack来处理，BackStack可以对这些元素进行出栈入栈操作
        override != null -> BackStack(override, onElementRemoved)

        existing != null -> existing//一直观察它是不是为空

        else -> BackStack(defaultElement, onElementRemoved)

    }.also {
        backStackMap[key] = it
    }
}

