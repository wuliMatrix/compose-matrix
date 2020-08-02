package com.github.zsoltk.compose.router

import androidx.compose.getValue
import androidx.compose.mutableStateOf
import androidx.compose.setValue


class BackStack<T> internal constructor(
    initialElement: T,
    private var onElementRemoved: ((Int) -> Unit)
) {
    private var elements  by mutableStateOf(listOf(initialElement))

    val lastIndex: Int
        get() = elements.lastIndex

    val size: Int
        get() = elements.size

    fun last(): T =
        elements.last()

    fun push(element: T) {
        elements = elements.plus(element)
    }

    fun pushAndDropNested(element: T) {
        onElementRemoved.invoke(lastIndex)
        push(element)
    }

    fun pop(): Boolean =
        // we won’t let the last item to be popped
        //我们不会让最后一项弹出
        if (size <= 1) false else {
            onElementRemoved.invoke(lastIndex)
            elements = ArrayList(
                elements.subList(0, lastIndex) // exclusive
            )
            true
        }

    fun replace(element: T) {
        onElementRemoved.invoke(lastIndex)
        elements = elements
             //subList 返回此列表在指定的[fromIndex]（包括）和[toIndex]（不包括）之间的视图。
            // 返回列表由该列表支持，因此返回列表中的非结构性更改会反映在此列表中，
            // 反之亦然。基本列表中的结构更改使视图的行为不确定。
            .subList(0, elements.lastIndex) // exclusive
            .plus(element)
    }

    fun newRoot(element: T) {
        elements.indices.reversed().forEach { index ->
            onElementRemoved.invoke(index)
        }
        elements = arrayListOf(element)
    }
}
