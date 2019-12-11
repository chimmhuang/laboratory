package com.chimm.demo02_base_grammar

/**
 * 定义常量与变量
 *
 * @author chimm
 * @date 2019/12/11
 */

/*
    可变变量定义：var 关键字
    var <标识符> : <类型> = <初始化值>

    不可变变量定义：val 关键字，只能赋值一次的变量(类似Java中final修饰的变量)
    val <标识符> : <类型> = <初始化值>

    常量与变量都可以没有初始化值,但是在引用前必须初始化

    编译器支持自动类型判断,即声明时可以不指定类型,由编译器判断。
 */

var a:Int = 1
val b = 1 // 系统自动推断变量类型为Int


fun fun1(): Unit {
    val c : Int //如果不在声明时初始化则必须提供变量类型
    c = 1 // 明确赋值
}


fun fun2(): Unit {
    var x = 5 // 系统自动推断变量类型为Int
    x += 1 // 变量可修改
}