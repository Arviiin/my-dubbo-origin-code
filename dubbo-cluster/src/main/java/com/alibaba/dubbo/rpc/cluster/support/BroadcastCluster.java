/*
 * Copyright 1999-2012 Alibaba Group.
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
 * BroadcastCluster
 *
 * @author william.liangf
 */
public class BroadcastCluster implements Cluster {
    //广播调用所有提供者，逐个调用，任意一台报错则报错。通常用于通知所有提供者更新缓存或日志等本地资源信息。
    // 这个的逻辑也是很简单,遍历所有Invokers, 逐个调用每个调用,有异常就catch,以免影响到剩下的调用,
    // 那这个和AvailableCluster有什么区别?当然有啊,你仔细看就知道,BroadcastCluster是要遍历调用完全部的invoker,而AvailableCluster是只要有一个调用就return了.
    public <T> Invoker<T> join(Directory<T> directory) throws RpcException {
        return new BroadcastClusterInvoker<T>(directory);
    }

}