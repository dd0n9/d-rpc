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
