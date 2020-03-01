package com.lxx.crudboy.model;

/**
 * @ClassName ReadResult
 * @Author laixiaoxing
 * @Date 2020/2/24 下午11:54
 * @Description 读取字符的返回结果
 * @Version 1.0
 */
public class ReadResult {

    /**
     * 读到的字符
     */
    private String read;

    /**
     * 下一个结果
     */
    private int i;

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }
}
