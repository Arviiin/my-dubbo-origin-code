/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.common.compiler;


import com.alibaba.dubbo.common.extension.SPI;

/**
 * Compiler. (SPI, Singleton, ThreadSafe)
 *
 * @author william.liangf
 */
@SPI("javassist")
public interface Compiler {

    /**
     * Compile java source code.
     *
     * @param code        Java source code
     * @param classLoader TODO
     * @return Compiled class
     */
    Class<?> compile(String code, ClassLoader classLoader);
    //我们从AdaptiveCompiler中的compile和com.alibaba.dubbo.common.compiler.Compiler配置文件就不难分析出两点
    //1.JDK的spi要用for循环,然后if判断才能获取到指定的spi对象,dubbo用指定的key就可以获取
        //返回指定名字的扩展    public T getExtension(String name){}
    //2.JDK的spi不支持默认值,dubbo增加了默认值的设计
        //@SPI("javassist")代表默认的spi对象,比如Compiler默认使用的是javassist,可通过
        //ExtensionLoader<Compiler> loader = ExtensionLoader.getExtensionLoader(Compiler.class);
        //compiler = loader.getDefaultExtension();
        //方式获取实现类,根据配置,即为
        //com.alibaba.dubbo.common.compiler.support.JavassistCompiler

}
