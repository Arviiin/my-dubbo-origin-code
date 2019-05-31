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
package com.alibaba.dubbo.rpc.cluster.support;

import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.cluster.Cluster;
import com.alibaba.dubbo.rpc.cluster.Directory;

/**
 * 失败转移，当出现失败，重试其它服务器，通常用于读操作，但重试会带来更长延迟。
 * <p>
 * <a href="http://en.wikipedia.org/wiki/Failover">Failover</a>
 *
 * @author william.liangf
 */
public class FailoverCluster implements Cluster {
    //这个是dubbo里面容错方案的缺省值.让我们来看官网介绍
    //失败自动切换，当出现失败，重试其它服务器。通常用于读操作，但重试会带来更长延迟。可通过 retries="2" 来设置重试次数(不含第一次)
    //也就是说,默认的情况下,如果第一次调用失败,会重试两次,也就是一共是调用三次.所以len = 3的(经过调试确实是3).

    //这个时候面试题就来了,注意我上面提到的FailfastCluster,
    // 面试会问,dubbo中"读接口"和"写接口"有什么区别?
    // 答案也是很明显的,因为默认FailoverCluster会重试,如果是"写"类型的接口,
    // 如果在网络抖动情况下写入多个值,所以"写"类型的接口要换成FailfastCluster
    public final static String NAME = "failover";

    public <T> Invoker<T> join(Directory<T> directory) throws RpcException {
        return new FailoverClusterInvoker<T>(directory);
    }

}