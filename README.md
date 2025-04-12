# d-rpc
my firtst wheel project: handwrting RPC

## What can d-rpc do?
### a simple rpc (2025/4/9)
- a web server bulit by Vert.x frame.
- a local rigistry
  - use ConcurrentHashMap to save the infomation about rigisted services
  - add, get, remove sservices
- a serializer
- rpcRequest and rpcResponse Class to transfer the infomation about service, such as
  - ServiceName
  - MethodName
  - ParamTypes
  - Param
  - ResponseData
  - DataTypes
  - Message
- a serverHandler: get sesvice from LocalRigistry, invoke the method and response the result
- a serviceProxy: send httpRequest to Provider, and get the response, return the result
- a proxyFactory: create the proxy

### Global Configration Loading (2025/4/10)
- a deafault RPC configration
  - name
  - version
  - host
  - port
  - will be more...
- an util about configration: load configration by hutool.Props, support multi environment
  - now it only support .properties file
  - will be more...
- an application to init configration
  - support customized configration
  - use Double-Checked Locking Singleton Pattern to ensure thread safety and avoid unnecessary overhead
- will be more...
  - 配置文件路径的灵活性：允许用户指定配置文件的路径，而不是固定为application.properties
  - 缓存配置：如果配置文件不经常变化，可以考虑缓存加载的配置，避免每次调用都重新加载文件。

 ### Interface Mock (2025/4/11)
 - a mock service proxy to return a deafault value when interface is not published
