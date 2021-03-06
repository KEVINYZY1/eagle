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

package eagle.jfaster.org.cluster.proxy;

import eagle.jfaster.org.cluster.ReferCluster;
import eagle.jfaster.org.rpc.Request;
import eagle.jfaster.org.util.ReflectUtil;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by fangyanpeng1 on 2017/8/7.
 */
public class AsyncInvokeHandler<T> extends AbstractReferInvokeHandler<T> {

    public AsyncInvokeHandler(List<ReferCluster<T>> referClusters, Class<T> clz) {
        super(referClusters, clz);
    }

    @Override
    protected Object handle(Method method, Request request) {
        Object ret = this.defaultCluster.call(request);
        if (ret != null) {
            return ret;
        }
        return ReflectUtil.getDefaultReturnValue(method.getReturnType());
    }

}
