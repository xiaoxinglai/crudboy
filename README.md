# crudboy
计划是一个提高crud效率的idea插件，目前实现了在代码中直接testSql以及根据模版生成DO Mapper xml等
开发了一个用于在代码中校验和执行sql的插件，效果如下：

在mapper.java中 ，右键选择 goto->testSQL 

以json形式输入参数，即可将执行结果显示出来
![在这里插入图片描述](https://img-blog.csdnimg.cn/2020030111045240.gif)


但是大多数场景，我只需要看下对于的sql对不对，那么就直接
goto->testValid 
会使用自动生成的默认参数去执行sql，不再需要自己输入参数

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200301111001809.gif)


同理的，复杂的参数也支持,insert update delete 也支持
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200301111341256.gif)
goto->testValid也可以使用默认参数测试
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200301111557371.gif)


此次演示用的xml，就是mybatis正常的带标签的xml
支持大多数的xml标签解析
![在这里插入图片描述](https://img-blog.csdnimg.cn/2020030111190980.gif)

这个插件也实现了根据数据库表自动生成DO mapper mapper.xml的功能

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200301225707109.gif)