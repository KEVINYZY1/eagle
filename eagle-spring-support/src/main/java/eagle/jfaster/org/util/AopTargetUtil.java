/*
 * Copyright 2017 eagle.jfaster.org.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */
package eagle.jfaster.org.util;

import eagle.jfaster.org.exception.EagleFrameException;

import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Field;

/**
 * Created by fangyanpeng on 2017/12/16.
 */
public class AopTargetUtil {

    /**
     * 获取目标对象class.
     *
     * @param proxy 代理对象
     * @return 目标对象class
     */
    public static Class<?> getTargetClass(Object proxy) throws Exception {
        if (!AopUtils.isAopProxy(proxy)) {
            return proxy.getClass();//不是代理对象
        }
        return AopUtils.getTargetClass(proxy);
    }

    /**
     * 获取目标对象.
     *
     * @param proxy 代理对象
     * @return 目标对象
     */
    public static Object getTarget(final Object proxy) {
        if (!AopUtils.isAopProxy(proxy)) {
            return proxy;
        }
        if (AopUtils.isJdkDynamicProxy(proxy)) {
            return getProxyTargetObject(proxy, "h");
        } else {
            return getProxyTargetObject(proxy, "CGLIB$CALLBACK_0");
        }
    }

    private static Object getProxyTargetObject(final Object proxy, final String proxyType) {
        Field h;
        try {
            h = proxy.getClass().getSuperclass().getDeclaredField(proxyType);
        } catch (final NoSuchFieldException ex) {
            return getProxyTargetObjectForCglibAndSpring4(proxy);
        }
        h.setAccessible(true);
        try {
            return getTargetObject(h.get(proxy));
        } catch (final IllegalAccessException ex) {
            throw new EagleFrameException(ex);
        }
    }

    private static Object getProxyTargetObjectForCglibAndSpring4(final Object proxy) {
        Field h;
        try {
            h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
            h.setAccessible(true);
            Object dynamicAdvisedInterceptor = h.get(proxy);
            Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
            advised.setAccessible(true);
            return ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
            // CHECKSTYLE:OFF
        } catch (final Exception ex) {
            // CHECKSTYLE:ON
            throw new EagleFrameException(ex);
        }
    }

    private static Object getTargetObject(final Object object) {
        try {
            Field advised = object.getClass().getDeclaredField("advised");
            advised.setAccessible(true);
            return ((AdvisedSupport) advised.get(object)).getTargetSource().getTarget();
            // CHECKSTYLE:OFF
        } catch (final Exception ex) {
            // CHECKSTYLE:ON
            throw new EagleFrameException(ex);
        }
    }
}
