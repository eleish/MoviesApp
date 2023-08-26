package com.eleish.yassirtask.core

import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.addOnBottomReachedListener(listener: () -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val isBottomReached = recyclerView.canScrollVertically(1).not()
            if (isBottomReached)
                listener.invoke()
        }
    })
}