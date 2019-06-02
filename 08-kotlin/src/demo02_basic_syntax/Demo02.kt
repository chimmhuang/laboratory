package demo02_basic_syntax

/**
 * @author huangshuai
 * @date 2019-06-01
 */
fun main() {
    //println(sum(1,3))
    //vars(1,2,3,10)
    //lambdaTest()
    testStr()
}


/**
 * ===========
 * 【函数定义】
 * ===========
 */
/** 函数定义使用关键字 fun，参数格式为：参数 : 类型 */
/*
    fun sum(a: Int, b: Int) : Int {
        return a + b
    }
 */


/** 表达式作为函数体，返回类型自动推断： */
/*
    fun sum(a: Int, b: Int) = a + b
    public fun sum(a: Int, b: Int): Int = a + b   // public 方法则必须明确写出返回类型
 */

/** 无返回值的函数(类似Java中的void)： */

fun sum(a: Int, b: Int) {
    println(a+b)
}
/** 如果是返回 Unit类型，则可以省略(对于public方法也是这样)： */
public fun printSum(a: Int, b: Int) {
    print(a + b)
}


/**
 * ===============
 * 【可变长参数函数】
 * ===============
 */
/** 函数的变长参数可以用 vararg 关键字进行标识： */
fun vars(vararg v: Int) {
    for (vt in v) {
        println(vt)
    }
}

/**
 * 【lambda(匿名函数)】
 */
fun lambdaTest() {
    var sumLambda: (Int, Int) -> Int = { x, y -> x + y }
    println(sumLambda(1,2))
}

/**
 * 【定义常量与变量】
 */
/*

    【可变变量定义：var 关键字】
        var <标识符> : <类型> = <初始化值>

    【不可变变量定义：val 关键字，只能赋值一次的变量(类似Java中final修饰的变量)】
        val <标识符> : <类型> = <初始化值>


    常量与变量都可以没有初始化值,但是在引用前必须初始化
    编译器支持自动类型判断,即声明时可以不指定类型,由编译器判断。

    val a: Int = 1
    val b = 1       // 系统自动推断变量类型为Int
    val c: Int      // 如果不在声明时初始化则必须提供变量类型
    c = 1           // 明确赋值


    var x = 5        // 系统自动推断变量类型为Int
    x += 1           // 变量可修改
 */

/**
 * 【字符串模板】
 */
fun testStr() {
    var a = 1
    // 模版中的简单名称
    val s1 = "a is $a"

    a = 2
    var s2 = "${s1.replace("is","was")}, but now is $a"

    println(s1)
    println(s2)
}

