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

package eagle.jfaster.org.rpc.support;

import java.lang.reflect.Method;

import eagle.jfaster.org.config.common.MergeConfig;
import eagle.jfaster.org.rpc.Request;
import eagle.jfaster.org.util.ReflectUtil;

/**
 * 远端调用实现
 *
 * Created by fangyanpeng1 on 2017/7/29.
 */
public class EagleRpcJdkRemoteInvoke<T> extends AbstractRemoteInvoke<T, Method> {

    public EagleRpcJdkRemoteInvoke(Class<T> interfaceClz, T invokeImpl, MergeConfig config) {
        super(interfaceClz, invokeImpl, config);
    }

    @Override
    protected void init() {
        Method[] methods = interfaceClz.getMethods();
        for (Method method : methods) {
            String methodDesc = ReflectUtil.getMethodDesc(method);
            methodInvoke.put(methodDesc, method);
        }
    }

    @Override
    protected Object invoke(Request request, Method method) throws Throwable {
        return method.invoke(invokeImpl, request.getParameters());
    }
}