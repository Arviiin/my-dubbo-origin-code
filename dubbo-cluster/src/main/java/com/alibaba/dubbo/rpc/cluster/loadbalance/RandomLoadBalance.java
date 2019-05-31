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
package com.alibaba.dubbo.rpc.cluster.loadbalance;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;

import java.util.List;
import java.util.Random;

/**
 * random load balance.
 *
 * @author qianlei
 * @author william.liangf
 */
public class RandomLoadBalance extends AbstractLoadBalance {//随机，按权重设置随机概率。在一个截面上碰撞的概率高，但调用量越大分布越均匀，而且按概率使用权重后也比较均匀，有利于动态调整提供者权重。
// 这个随机的策略是默认的策略,
// 但是这个随机和我们理解上的随机还是不一样的,因为他还有个概念叫weight(权重),这个"权重"的概念Android的同学一定不会陌生,
// 因为在LinearLayout布局中就有这个概念,在前端的CSS框架Bootstrap的栅格和也有类似概念.说白了,这里说的权重就是用来控制这个随机的概率的,
// 假设有四个集群节点A,B,C,D,对应的权重分别是1,2,3,4,那么请求到A节点的概率就为1/(1+2+3+4) = 10%.B,C,D节点依次类推为20%,30%,40%.
// 我们知道总权重为10(1+2+3+4),那么怎么做到按权重随机呢?根据10随机出一个整数,假如为随机出来的是2.
// 然后依次和权重相减,比如2(随机数)-1(A的权重) = 1,然后1(上一步计算的结果)-2(B的权重) = -1,此时-1 < 0,那么则调用B,其他的以此类推


    public static final String NAME = "random";

    private final Random random = new Random();

    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        int length = invokers.size(); // 总个数
        int totalWeight = 0; // 总权重
        boolean sameWeight = true; // 权重是否都一样
        for (int i = 0; i < length; i++) {
            int weight = getWeight(invokers.get(i), invocation);
            totalWeight += weight; // 累计总权重
            if (sameWeight && i > 0
                    && weight != getWeight(invokers.get(i - 1), invocation)) {
                sameWeight = false; // 计算所有权重是否一样
            }
        }
        if (totalWeight > 0 && !sameWeight) {
            // 如果权重不相同且权重大于0则按总权重数随机
            int offset = random.nextInt(totalWeight);
            // 并确定随机值落在哪个片断上
            for (int i = 0; i < length; i++) {
                offset -= getWeight(invokers.get(i), invocation);
                if (offset < 0) {
                    return invokers.get(i);
                }
            }
        }
        // 如果权重相同或权重为0则均等随机
        return invokers.get(random.nextInt(length));
    }

}