package com.chimm.demo02_base_grammar

/**
 * 函数定义
 *
 * @author chimm
 * @date 2019/12/11
 */

/**
 * 函数定义使用关键字 fun，参数格式为：参数 : 类型
 */
fun sum1(a: Int, b: Int): Int {  // Int 参数，返回值 Int
    return a + b
}

/**
 * 表达式作为函数体，返回类型自动推断
 */
fun sum2(a: Int, b: Int) = a + b
public fun sum3(a: Int, b: Int): Int = a + b // public 方法则必须明确写出返回类型

/**
 * 无返回值的函数(类似Java中的void)
 */
fun printSum1(a: Int, b: Int): Unit {
    print(a + b)
}
// 如果是返回 Unit类型，则可以省略(对于public方法也是这样)
public fun printSum2(a: Int, b: Int) {
    print(a + b)
}
