package com.lxx.crudboy.config;
/**
 * @Author laixiaoxing
 * @Description 监听配置变化的回调接口
 * @Date 下午1:10 2020/2/14
 */
public interface ConfigListenersRegistry<T> {

    /**
     * 根据配置刷新资源
     * @param settings
     * @return
     */
    String refresh(T settings);
}
