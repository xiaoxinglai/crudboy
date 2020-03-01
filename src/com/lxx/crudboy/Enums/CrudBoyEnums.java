package com.lxx.crudboy.Enums;

/**
 * @ClassName CrudBoyEnums
 * @Author laixiaoxing
 * @Date 2020/2/28 下午2:12
 * @Description 项目枚举
 * @Version 1.0
 */
public enum CrudBoyEnums {

    /**
     * 执行
     */
    EXCUTE("EXUTE", "查询操作"),

    /**
     * 测试验证
     */
    TEST("TEST", "测试验证"),;

    private String code;
    private String name;

    CrudBoyEnums(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
