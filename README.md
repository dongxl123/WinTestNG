# WinTestNG

> 基于TestNG开发的自动化测试框架，自动化测试平台后端接口参考项目:WinTestNG-platform-api

## 概述
- 适用于HTTP接口测试
- 测试数据维护在DB
- 支持Redis、Mongo、Mysql等操作
- 支持测试模板设置，可抽象通用功能、
- 全局变量设置
- 支持断言
- 使用FreeMarker作为模板引擎
- 生成测试报告数据存储在DB
- 涉及测试概念:测试用例、数据驱动、断言、测试报告、接口测试、自动化测试、测试模板
- 建表语句: wintestng-core/src/resources/WinTestNG.sql
 
## 使用方法
 
- 执行SQL语句 *wintestng-core/src/main/resources/WinTestNG.sql*
- 修改配置文件 *wintestng-core/src/main/resources/application.yml*
- 执行TestNG文件 *example/src/test/resources/projectname/example.xml*

## 测试计划执行逻辑
![logic](./logic.jpg)

## 配置详细文档
### 资源配置
> 涉及
- 表resource_config 

##### mysql
```json
{
    "url":"jdbc:mysql://xx.mysql.rds.aliyuncs.com:3306/baoxian",
    "userName":"userName",
    "password":"password"
}
```

#### redis
```json
{
    "host":"12.26.1.94",
    "port":3736,
    "database":8,
    "password":"D6eAtdxxxxxxsaisf"
}
```

#### mongo
```json
{
    "url":"mongodb://wyxx_app:xxxddd@11.40.88.39:3717,11.41.53.215:27017/wyxx_app"
}
```

### 数据准备配置
> 涉及
- 表test_cases（测试计划） 字段data_preparation_config

#### text
```json
{
    "showTitle":"3333-${mobile}",
    "text":"[{\"mobile\":\"15967126512\"},{\"mobile\":\"15967122137\"}]",
    "type":"text"
}
```

#### csv
```json
{
    "fieldNameList":[
        "id",
        "name",
        "mobile"
    ],
    "fileName":"http://xx.winbaoxian.com/xx.1csv",
    "separator":",",
    "type":"csv"
}
```

#### resource
```json
{
    "resourceId":2,
    "showTitle":"3333-${mobile}",
    "sql":"select mobile from sales_user limit 3",
    "type":"resource"
}
```

### 步骤配置 
> 涉及
- 表action_template（测试模板） 字段actions
- 表test_cases（测试计划） 字段pre_action、main_actions、post_actions

> 示例
```json
[
    {
        "name":"调用APP登录",
        "actionType":"TPL",
        "templateId":21,
        "mappings":{
            "a":"18757101098",
            "b":"0000"
        },
        "delayTimes":2000,
        "alias":"template"
    },
    {
        "name":"更新数据库",
        "delayTimes":2000,
        "actionType":"RESOURCE",
        "resourceId":1,
        "sql":"update big_guy_share_info set status=0 where id=8"
    },
    {
        "name":"教你赚钱-获取大咖分享分页数据-20条",
        "actionType":"HTTP",
        "requestHeader":{

        },
        "requestUrl":"http://xx.winbaoxian.cn/xx/1/getBigGuyShareInfoPageList",
        "requestParams":{
            "t":1,
            "n":1,
            "u":"10608088@winbaoxian.com",
            "di":"mockDeviceId",
            "m":1,
            "s":"${template.token}"
        },
        "requestMethod":"POST",
        "requestContentType":"e/b",
        "requestBody":"{\"pageNo\": 1}",
        "alias":"getBigGuyShareInfoPageList",
        "delayTimes":2000
    },
    {
        "actionType":"ASSERTION",
        "delayTimes":2000,
        "name":"断言",
        "verifyList":[
            {
                "type":"eq",
                "value1":"${getBigGuyShareInfoPageList.body.r.isEnd?c}",
                "value2":"true"
            }
        ]
    }
]

```

#### HTTP请求
```json
{
    "name":"教你赚钱-获取大咖分享分页数据-20条",
    "actionType":"HTTP",
    "requestHeader":{},
    "requestUrl":"http://xx.winbaoxian.cn/xx/1/getBigGuyShareInfoPageList",
    "requestParams":{
        "t":1,
        "n":1,
        "u":"10608088@winbaoxian.com",
        "di":"mockDeviceId",
        "m":1,
        "s":"${template.token}"
    },
    "requestMethod":"POST",
    "requestContentType":"e/b",
    "requestBody":"{\"pageNo\": 1}",
    "delayTimes":2000,
    "alias":"template"
}
```

#### 资源操作
```json
{
    "name":"更新数据库",
    "actionType":"RESOURCE",
    "resourceId":1,
    "sql":"SELECT id,`name`,mobile from sales_user limit 1",
    "fetchOne":true,
    "delayTimes":2000,
    "alias":"ret"
}
```

#### 常量设置
```json
{
    "name":"常量设置",
    "actionType":"SETV",
    "params":{
        "ip":"x-api.winbaoxian.cn",
        "calculate":"/api/product/data/calculate",
        "get_captcha":"/api/policy/getCaptcha",
        "pre_order":"/api/policy/pre-order"
    },
    "delayTimes":2000,
    "alias":"ret"
}
```

#### 调用模板
```json
{
    "name":"调用APP登录",
    "actionType":"TPL",
    "templateId":21,
    "mappings":{
        "a":"18757101098",
        "b":"0000"
    },
    "delayTimes":2000,
    "alias":"template"
}
```

#### ASSERTION
```json
{
    "name":"断言",
    "actionType":"ASSERTION",
    "templateId":21,
    "verifyList":[
        {
            "type":"condition",
            "value1":"${getExpirePolicyCollate.body.r.policyExpireRemindList?exists?c}==true"
        },
        {
            "type":"eq",
            "value1":"${getExpirePolicyCollate.body.r.policyExpireRemindList[0].policyUuid}",
            "value2":"5108"
        }
    ],
    "delayTimes":2000,
    "alias":"v"
}
```

#### GROUP分组
```json
{
    "name":"分组",
    "actionType":"GROUP",
    "stepList":[
        {
            "name":"调用APP登录",
            "actionType":"TPL",
            "templateId":21,
            "mappings":{
                "a":"18757101098",
                "b":"0000"
            },
            "delayTimes":2000,
            "alias":"template"
        }
    ]
}
```

#### IF判断
```json
{
    "name":"IF判断",
    "actionType":"IF",
    "condition": "${res.success?c}",
    "stepList":[
        {
            "name":"调用APP登录",
            "actionType":"TPL",
            "templateId":21,
            "mappings":{
                "a":"18757101098",
                "b":"0000"
            },
            "delayTimes":2000,
            "alias":"template"
        }
    ]
}
```

#### FOR循环
```json
{
    "name":"IF判断",
    "actionType":"IF",
    "iterData": "${toJSONString(res.list)}",
    "iterAlias": "a",
    "stepList":[
        {
            "name":"调用APP登录",
            "actionType":"TPL",
            "templateId":21,
            "mappings":{
                "a":"${a.mobile}",
                "b":"0000"
            },
            "delayTimes":2000,
            "alias":"template"
        }
    ]
}
```

#### SCRIPT
```json
{
    "name":"脚本",
    "actionType":"SCRIPT",
    "lang":"PYTHON",
    "content":"a=1+2",
    "extractVars": ["a"],
    "alias": "xx"
}
```
---
## 联系方式

- QQ: <span><a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=245684715&site=qq&menu=yes"><img border="0" src="http://wpa.qq.com/pa?p=2:245684715:51" alt="点击这里给我发消息" title="点击这里给我发消息"/></a></span>
- Email: dongxl123@163.com
