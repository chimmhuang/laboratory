package com.chimm.demo02_base_grammar

/**
 * 类型检测及自动类型转换
 *
 * @author chimm
 * @date 2019/12/11
 */

/*
    我们可以使用 is 运算符检测一个表达式是否某类型的一个实例(类似于Java中的instanceof关键字)。
 */
fun getStringLength1(obj: Any): Int? {
    if (obj is String) {
        // 做过类型判断以后，obj会被系统自动转换为String类型
        return obj.length
    }

    //在这里还有一种方法，与Java中instanceof不同，使用 !is
    if (obj !is String) {
        // do something...
    }

    // 这里的obj任然是Any类型的引用
    return null
}

/*
    或者
 */
fun getStringLength2(obj: Any): Int? {
    if (obj !is String) {
        return null
    }
    // 在这个分支中，`obj`的类型会被自动转换为 'String'
    return obj.length
}

/*
    甚至还可以
 */
fun getStringLength3(obj: Any): Int? {
    // 在 `&&` 运算符的有右侧，`obj`的类型会被自动转换为 String
    if (obj is String && obj.length > 0) {
        return obj.length
    }
    return null
}