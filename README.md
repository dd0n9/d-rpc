# d-rpc
my firtst wheel project: handwrting RPC

## What d-rpc can do?
### a simple rpc (4/10/2025)
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
