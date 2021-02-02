package com.bs.ecommerce

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    private fun minimumBribes(arr: IntArray) {
        var swapCount = 0
        for (i in arr.indices.reversed()) {
            if (arr[i] != i + 1) { // filter cases, when you do not bribe, be at the ith position wherever you are
                if (i - 1 >= 0 && arr[i - 1] == i + 1) { // 1)Being at initial ith position, valid current
                    // position after given bribe can be (i-1)th position
                    swapCount++
                    swap(arr, i, i - 1)
                } else if (i - 2 >= 0 && arr[i - 2] == i + 1) { // 2)Being at initial ith position, valid current
                    // position after given bribes can be (i-2)th
                    // position
                    swapCount += 2
                    swap(arr, i - 2, i - 1)
                    swap(arr, i - 1, i)
                } else { // 3)Too chaotic (trying to bribe more than 2 people which is ahead of you)
                    println("Too chaotic")
                    return
                }
            }
        }
        println(swapCount)
    }

    private fun swap(arr: IntArray, a: Int, b: Int) {
        val temp = arr[a]
        arr[a] = arr[b]
        arr[b] = temp
    }
}
