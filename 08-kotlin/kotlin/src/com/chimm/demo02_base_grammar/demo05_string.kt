package com.chimm.demo02_base_grammar

/**
 * 字符串模板
 *
 * @author chimm
 * @date 2019/12/11
 */

/*
    $ 表示一个变量名或者变量值
    $varName 表示变量值
    ${varName.fun()} 表示变量的方法返回值
 */

fun main() {
    var a = 1
    // 模板中的简单名称：
    val s1 = "a is $a"

    a = 2
    // 模板中的任意表达式
    val s2 = "${s1.replace("is","was")},but now is $a"

    print(s2)
}

