package com.chimm.demo02_base_grammar

/**
 * 区间
 *
 * @author chimm
 * @date 2019/12/11
 */

/*
    区间表达式由具有操作符形式 .. 的 rangeTo 函数辅以 in 和 !in 形成。
    区间是为任何可比较类型定义的，但对于整形原生类型，它有一个优化的实现。以下使用区间的一些实例
 */
fun test(i:Int): Unit {
    for (i in 1..4) print(i) // 输出“1234”

    for (i in 4..1) print(i) // 什么都不输出

    if (i in 1..10) { // 等同于 1 <= i && i <= 10
        println(i)
    }

    // 使用 step 指定步长
    for (i in 1..4 step 2) print(i) // 输出“13”

    for (i in 4 downTo 1 step 2) print(i) // 输出“42”

    // 使用 until 函数排除结束元素
    for (i in 1 until 10) print(i) // i in [1,10) 排除了10
}

/*
    实例测试
 */
fun main(array: Array<String>) {
    print("循环输出：")
    for (i in 1..4) print(i) // 输出“1234”

    println("\n-------------------")
    print("设置步长：")
    for (i in 1..4 step 2) print(i) // 输出“13”

    println("\n-------------------")
    print("使用 downTo：")
    for (i in 4 downTo 1 step 2) print(i) // 输出“42”

    println("\n-------------------")
    print("使用until：")
    for (i in 1 until 10) print(i) // i in [1,10) 排除了10
}