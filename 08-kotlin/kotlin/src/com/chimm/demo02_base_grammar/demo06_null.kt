package com.chimm.demo02_base_grammar

/**
 * Null检查机制
 *
 * @author chimm
 * @date 2019/12/11
 */

/*
    Kotlin的空安全设计对于声明可为空的参数，在使用时要进行空判断处理，有两种处理方式，
    字段后加!!像Java一样抛出空异常，另一种字段后加?可不做处理返回值为 null或配合?:做空判断处理
 */

fun demoFun01() {
    //类型后面加 ? 表示可以为空
    var age: String? = "23"

    //抛出空指针异常
    val ages = age!!.toInt()

    //不做处理返回 null
    val ages1 = age?.toInt()

    //age为空返回-1
    val ages2 = age?.toInt() ?: -1
}

/*
    当一个引用可能为 null 值时, 对应的类型声明必须明确地标记为可为 null。
    当 str 中的字符串内容不是一个整数时, 返回 null
 */
fun parseInt(str: String): Int? {
    //...
    return null
}

/*
    以下实例演示如何使用一个返回值可为 null 的函数:
 */
fun main(args: Array<String>) {
    if (args.size < 2) {
        print("Tow Integers expected")
        return
    }

    val x = parseInt(args[0])
    val y = parseInt(args[1])

    // 直接使用 `x * y` 会导致错误，因为它们可能为null
    if (x != null && y != null) {
        // 在进行过null值检查之后，x和y的类型会被自动转换为非null变量
        print(x * y)
    } else {
        print("x 和 y 其中一个为null")
    }
}