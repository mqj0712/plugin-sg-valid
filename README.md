# plugin-sgvalid

####使用 `mvn clean package` 编译打包, 复制`plugin-sg-valid-X.X.X-jar-with-dependencies.jar` 到ja-netfilterde plugin目录, 并且重命名为 sgvalid.jar, 需要于conf文件同名

## Config file: `sgvalid.conf`
提供给按配置在运行时修改指定函数体为直接return的功能, 适用于无返回值的函数. 使用方式参见其他ja-netfilter插件

配置文件项的格式为, 
~~~
EQUAL, <class name>|<methodname>|<method description>
~~~

conf文件配置示例:
~~~
[Methods]
EQUAL,smartgit/Xs|a|(Lsmartgit/TN;Ljava/security/MessageDigest;I)V
~~~

v1.1.0版本支持正则表达式匹配, 以适应函数名变化的情况, 例如
~~~
[Methods]
REGEXP,smartgit/.*|.*|\(.*;Ljava/security/MessageDigest;I\)V
~~~