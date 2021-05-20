webpackJsonp([1],{0:function(e,t){},1:function(e,t){},2:function(e,t){},3:function(e,t){},4:function(e,t){},5:function(e,t){},"5Q7h":function(e,t){},6:function(e,t){},7:function(e,t){},"78dc":function(e,t){},GHeb:function(e,t){},NHnr:function(e,t,i){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a=i("7+uW"),o={render:function(){var e=this.$createElement,t=this._self._c||e;return t("div",[t("el-menu",{staticClass:"el-menu-demo",attrs:{"default-active":this.$route.path,router:"",mode:"horizontal","background-color":"#545c64","text-color":"#fff","active-text-color":"#ffd04b"},on:{select:this.handleSelect}},[t("el-menu-item",{attrs:{index:"/"}},[this._v("硬件管理")]),this._v(" "),t("el-menu-item",{attrs:{index:"/realtime"}},[this._v("实时数据")]),this._v(" "),t("el-menu-item",{attrs:{index:"/history"}},[this._v("历史数据")])],1)],1)},staticRenderFns:[]},n={name:"App",components:{ViewHeader:i("VU/8")({name:"ViewHeader",data:function(){return{}},methods:{handleSelect:function(e,t){}}},o,!1,null,null,null).exports}},l={render:function(){var e=this.$createElement,t=this._self._c||e;return t("div",{attrs:{id:"app"}},[t("el-container",[t("el-header",[t("view-header")],1),this._v(" "),t("el-main",[t("router-view")],1)],1)],1)},staticRenderFns:[]};var r=i("VU/8")(n,l,!1,function(e){i("uYIX")},null,null).exports,s=i("/ocq"),c=i("//Fk"),d=i.n(c),u=i("mtWM"),f=i.n(u),v=i("zL8q"),p=i.n(v),m=f.a.create({timeout:3e4});m.defaults.withCredentials=!0,m.defaults.headers.post["Content-Type"]="application/json",m.interceptors.request.use(function(e){var t=e.url.replace("api/","");return e.url=t,e}),m.interceptors.response.use(function(e){return 200===e.status?e:(Object(v.Message)({message:e.status+" - "+(e.data?e.data:e.statusText),type:"warning"}),e)},function(e){return e.message.includes("timeout")?Object(v.Message)({message:"访问超时，请重试!",type:"warning"}):401===e.response.status?(localStorage.removeItem("token"),G.push("/")):405===e.response.status?G.push("/"):502===e.response.status?Object(v.Message)({message:"与服务器连接超时!",type:"error"}):504===e.response.status?Object(v.Message)({message:"与服务器连接断开!",type:"error"}):(console.log(e.response),Object(v.Message)({message:e.response.status+" - "+(e.response.data?e.response.data:e.response.statusText),type:"error"})),d.a.reject(e)});var h=m,g={login:function(e){return h.post("api/login",e)}},b={name:"Login",data:function(){return{form:{loaginName:"",loginPassword:""}}},components:{},methods:{onLogin:function(){var e=this;g.login({loginName:"root",loginPassword:"root"}).then(function(t){if(console.log(t),t.data){localStorage.token=t,e.$route.query.redirect;var i=e.$route.query.redirect;e.$route.push(i)}else e.$route.push({name:"DeviceList"})})}}},_={render:function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",[i("el-form",{ref:"form",attrs:{model:e.form,"label-width":"80px"}},[i("el-form-item",{attrs:{label:"活动名称"}},[i("el-input",{model:{value:e.form.loaginName,callback:function(t){e.$set(e.form,"loaginName",t)},expression:"form.loaginName"}})],1),e._v(" "),i("el-form-item",{attrs:{label:"活动名称"}},[i("el-input",{model:{value:e.form.loginPassword,callback:function(t){e.$set(e.form,"loginPassword",t)},expression:"form.loginPassword"}})],1),e._v(" "),i("el-form-item",[i("el-button",{attrs:{type:"primary"},on:{click:e.onLogin}},[e._v("登陆")])],1)],1)],1)},staticRenderFns:[]},C=i("VU/8")(b,_,!1,null,null,null).exports,y=i("Cg+j"),w=i.n(y),k={getConfig:function(){return h.get("api/mqtt-config")}},x=i("GP3o"),D={getDevices:function(e){return h.get("api/devices/person-position-devices/list",{params:e})},getBaseConfig:function(e){return h.get("api/devices/"+e+"/baseConfig")},getNetConfig:function(e){return h.get("api/devices/"+e+"/networkParams")},getPortConfig:function(e,t){return h.get("api/devices/"+e+"/port"+t)},updateDeviceConfig:function(e,t){return h.put("api/devices/"+e+"/baseConfig",t)},updateNetConfig:function(e,t){return h.put("api/devices/"+e+"/networkParams",t)},updatePortConfig:function(e,t,i){return h.put("api/devices/"+e+"/port"+t,i)},reboot:function(e){return h.get("api/devices/"+e+"/reset")},getDeviceStatistics:function(e){return h.get("api/devices-count",{params:e})}},S=i("mw3O"),T=i.n(S),$={name:"BaseDeviceEdit",props:{deviceCode:0},inject:["closeEdit"],data:function(){return{loading:!1,labelWidth:"160px",form:{},currentPage:1,pageSize:5,pageTotal:1,devices:[],devInfo:{},onlineDeviceCount:0,offlineDeviceCount:0,deviceSearchForm:{deviceState:null,deviceCode:null,deviceType:null,deviceIp:null}}},computed:{},created:function(){},mounted:function(){this.init()},methods:{init:function(){var e=this,t=this;t.loading=!0,D.getBaseConfig(this.deviceCode).then(function(i){t.loading=!1,i.data&&(e.form=i.data)},function(e){t.loading=!1})},updateDeviceConfig:function(){var e=this,t=this;this.loading=!0,this.closeEdit(this.deviceCode,!0),D.updateDeviceConfig(this.deviceCode,this.form).then(function(i){t.loading=!1,t.dialogFormVisible(),e.$message({message:"更新设备配置命令已下发，设备正在重启，大约需要一分钟。",type:"success"})},function(e){t.loading=!1})},dialogFormVisible:function(){this.$parent.$parent.$parent.hide()}}},F={render:function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",[i("el-form",{attrs:{inline:!0,model:e.form,"label-position":"right"}},[i("el-form-item",{attrs:{label:"设备编号","label-width":e.labelWidth}},[i("el-input",{attrs:{autocomplete:"off"},model:{value:e.form.deviceId,callback:function(t){e.$set(e.form,"deviceId",t)},expression:"form.deviceId"}})],1),e._v(" "),i("el-form-item",{attrs:{label:"发送增益","label-width":e.labelWidth}},[i("el-select",{attrs:{placeholder:"请选择发送增益"},model:{value:e.form.cain1,callback:function(t){e.$set(e.form,"cain1",t)},expression:"form.cain1"}},e._l(4,function(e,t){return i("el-option",{key:t,attrs:{label:t,value:t}})}),1)],1),e._v(" "),i("el-form-item",{attrs:{label:"接收增益","label-width":e.labelWidth}},[i("el-select",{attrs:{placeholder:"请选择接收增益"},model:{value:e.form.cain2,callback:function(t){e.$set(e.form,"cain2",t)},expression:"form.cain2"}},e._l(32,function(e,t){return i("el-option",{key:t,attrs:{label:t,value:t}})}),1)],1),e._v(" "),i("el-form-item",{attrs:{label:"空中波特率","label-width":e.labelWidth}},[i("el-select",{attrs:{placeholder:"请选择空中波特率"},model:{value:e.form.airBaudrate,callback:function(t){e.$set(e.form,"airBaudrate",t)},expression:"form.airBaudrate"}},[i("el-option",{attrs:{label:"250K",value:0}}),e._v(" "),i("el-option",{attrs:{label:"1M",value:1}}),e._v(" "),i("el-option",{attrs:{label:"2M",value:2}})],1)],1),e._v(" "),i("el-form-item",{attrs:{label:"波特率","label-width":e.labelWidth}},[i("el-select",{attrs:{placeholder:"请选择波特率"},model:{value:e.form.baudrate,callback:function(t){e.$set(e.form,"baudrate",t)},expression:"form.baudrate"}},[i("el-option",{attrs:{label:"4800",value:0}}),e._v(" "),i("el-option",{attrs:{label:"9600",value:1}}),e._v(" "),i("el-option",{attrs:{label:"19200",value:2}}),e._v(" "),i("el-option",{attrs:{label:"38400",value:3}}),e._v(" "),i("el-option",{attrs:{label:"57600",value:4}}),e._v(" "),i("el-option",{attrs:{label:"115200",value:5}})],1)],1),e._v(" "),i("el-form-item",{attrs:{label:"蜂鸣器状态","label-width":e.labelWidth}},[i("el-select",{attrs:{placeholder:"请选择蜂鸣器状态"},model:{value:e.form.buzzType,callback:function(t){e.$set(e.form,"buzzType",t)},expression:"form.buzzType"}},[i("el-option",{attrs:{label:"关",value:0}}),e._v(" "),i("el-option",{attrs:{label:"开",value:1}})],1)],1),e._v(" "),i("el-form-item",{attrs:{label:"地感值","value-key":"value","label-width":e.labelWidth}},[i("el-select",{attrs:{placeholder:"请选择地感值"},model:{value:e.form.ioInput,callback:function(t){e.$set(e.form,"ioInput",t)},expression:"form.ioInput"}},[i("el-option",{attrs:{label:"无地感",value:0}}),e._v(" "),i("el-option",{attrs:{label:"有地感",value:1}})],1)],1),e._v(" "),i("el-form-item",{attrs:{label:"两秒内收到相同ID阀值","label-width":e.labelWidth}},[i("el-select",{attrs:{placeholder:"请选择阀值"},model:{value:e.form.critical,callback:function(t){e.$set(e.form,"critical",t)},expression:"form.critical"}},e._l(8,function(e,t){return i("el-option",{key:t,attrs:{label:t,value:t}})}),1)],1),e._v(" "),i("el-form-item",{attrs:{label:"相同ID过滤时间","label-width":e.labelWidth}},[i("el-select",{attrs:{placeholder:"请选择过滤时间"},model:{value:e.form.filterTagTime,callback:function(t){e.$set(e.form,"filterTagTime",t)},expression:"form.filterTagTime"}},e._l(250,function(e,t){return i("el-option",{key:t,attrs:{label:t,value:t}})}),1)],1),e._v(" "),i("el-form-item",{attrs:{label:"韦根数据发送间隔","label-width":e.labelWidth}},[i("el-select",{attrs:{placeholder:"请选择韦根数据的发送间隔"},model:{value:e.form.sendInterval,callback:function(t){e.$set(e.form,"sendInterval",t)},expression:"form.sendInterval"}},e._l(250,function(e,t){return i("el-option",{key:t,attrs:{label:t,value:t}})}),1)],1),e._v(" "),i("el-form-item",{attrs:{label:"标签类型","label-width":e.labelWidth}},[i("el-select",{attrs:{placeholder:"请选择标签类型"},model:{value:e.form.tagType,callback:function(t){e.$set(e.form,"tagType",t)},expression:"form.tagType"}},e._l(250,function(e,t){return i("el-option",{key:t,attrs:{label:t,value:t}})}),1)],1),e._v(" "),i("el-form-item",{attrs:{label:"设备CRC状态","label-width":e.labelWidth}},[i("el-select",{attrs:{placeholder:"请选择CRC状态"},model:{value:e.form.crcEn,callback:function(t){e.$set(e.form,"crcEn",t)},expression:"form.crcEn"}},[i("el-option",{attrs:{label:"取消",value:0}}),e._v(" "),i("el-option",{attrs:{label:"有效",value:1}})],1)],1)],1),e._v(" "),i("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[i("el-button",{attrs:{type:"primary"},on:{click:e.updateDeviceConfig}},[e._v("确 定")]),e._v(" "),i("el-button",{on:{click:e.dialogFormVisible}},[e._v("取 消")])],1)],1)},staticRenderFns:[]};var I={name:"PortEdit",props:{deviceCode:0,port:0},inject:["closeEdit"],data:function(){return{loading:!1,labelWidth:"160px",form:{},currentPage:1,pageSize:5,pageTotal:1,devices:[],onlineDeviceCount:0,offlineDeviceCount:0,deviceSearchForm:{deviceState:null,deviceCode:null,deviceType:null,deviceIp:null}}},computed:{},created:function(){},mounted:function(){this.init()},methods:{init:function(){var e=this,t=this;t.loading=!0,D.getPortConfig(this.deviceCode,this.port).then(function(i){t.loading=!1,i.data&&(e.form=i.data)},function(e){t.loading=!1})},updatePortConfig:function(){var e=this,t=this;this.loading=!0,this.form.port=this.port,this.closeEdit(this.deviceCode,!0),D.updatePortConfig(this.deviceCode,this.form.port,this.form).then(function(i){t.loading=!1,t.dialogFormVisible(),e.$message({message:"更新端口配置命令已下发，设备正在重启，大约需要一分钟。",type:"success"})},function(e){t.loading=!1})},dialogFormVisible:function(){this.$parent.$parent.$parent.hide()}}},E={render:function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",[i("el-form",{attrs:{inline:!0,model:e.form,"label-position":"right"}},[i("el-row",{attrs:{gutter:20}},[i("el-col",{attrs:{span:12,offset:5}},[i("div",{staticClass:"grid-content bg-purple"},[i("el-form-item",{attrs:{label:"目标IP","label-width":e.labelWidth}},[i("el-input",{attrs:{autocomplete:"off",placeholder:"例如192.168.1.253"},model:{value:e.form.socket0DIP,callback:function(t){e.$set(e.form,"socket0DIP",t)},expression:"form.socket0DIP"}})],1),e._v(" "),i("el-form-item",{attrs:{label:"目标端口","label-width":e.labelWidth}},[i("el-input",{attrs:{autocomplete:"off",placeholder:"例如32500"},model:{value:e.form.dport,callback:function(t){e.$set(e.form,"dport",t)},expression:"form.dport"}})],1),e._v(" "),i("el-form-item",{attrs:{label:"本地端口","label-width":e.labelWidth}},[i("el-input",{attrs:{autocomplete:"off",placeholder:"例如32500"},model:{value:e.form.sport,callback:function(t){e.$set(e.form,"sport",t)},expression:"form.sport"}})],1),e._v(" "),i("el-form-item",{attrs:{label:"通讯模式","label-width":e.labelWidth}},[i("el-select",{attrs:{placeholder:"请选择通讯模式"},model:{value:e.form.mode,callback:function(t){e.$set(e.form,"mode",t)},expression:"form.mode"}},[i("el-option",{attrs:{label:"TCPServer",value:0}}),e._v(" "),i("el-option",{attrs:{label:"TCPClient",value:1}}),e._v(" "),i("el-option",{attrs:{label:"UDPClient",value:2}})],1)],1),e._v(" "),i("el-form-item",{attrs:{label:"激活状态","label-width":e.labelWidth}},[i("el-select",{attrs:{placeholder:"请选择激活状态"},model:{value:e.form.enable,callback:function(t){e.$set(e.form,"enable",t)},expression:"form.enable"}},[i("el-option",{attrs:{label:"Disable",value:0}}),e._v(" "),i("el-option",{attrs:{label:"Enable",value:1}})],1)],1)],1)])],1)],1),e._v(" "),i("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[i("el-button",{attrs:{type:"primary"},on:{click:e.updatePortConfig}},[e._v("确 定")]),e._v(" "),i("el-button",{on:{click:e.dialogFormVisible}},[e._v("取 消")])],1)],1)},staticRenderFns:[]};var P={name:"NetEdit",props:{deviceCode:0},inject:["closeEdit"],data:function(){return{loading:!1,labelWidth:"160px",form:{},devInfo:{}}},computed:{},created:function(){},mounted:function(){this.init()},methods:{init:function(){var e=this,t=this;t.loading=!0,D.getNetConfig(this.deviceCode).then(function(i){t.loading=!1,i.data&&(e.form=i.data)},function(e){t.loading=!1})},updateNetDeviceConfig:function(){var e=this,t=this;this.loading=!0,this.closeEdit(this.deviceCode,!0),D.updateNetConfig(this.deviceCode,this.form).then(function(i){t.loading=!1,t.dialogFormVisible(),e.$message({message:"更新网络配置命令已下发，设备正在重启，大约需要一分钟。",type:"success"})},function(e){t.loading=!1})},dialogFormVisible:function(){this.$parent.$parent.$parent.hide()}}},z={render:function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",[i("el-form",{attrs:{inline:!0,model:e.form,"label-position":"right"}},[i("el-row",{attrs:{gutter:20}},[i("el-col",{attrs:{span:12,offset:5}},[i("div",{staticClass:"grid-content bg-purple"},[i("el-form-item",{attrs:{label:"IP","label-width":e.labelWidth}},[i("el-input",{attrs:{autocomplete:"off"},model:{value:e.form.sourceIp,callback:function(t){e.$set(e.form,"sourceIp",t)},expression:"form.sourceIp"}})],1),e._v(" "),i("el-form-item",{attrs:{label:"MAC地址","label-width":e.labelWidth}},[i("el-input",{attrs:{autocomplete:"off"},model:{value:e.form.sourceHardware,callback:function(t){e.$set(e.form,"sourceHardware",t)},expression:"form.sourceHardware"}})],1),e._v(" "),i("el-form-item",{attrs:{label:"网关","label-width":e.labelWidth}},[i("el-input",{attrs:{autocomplete:"off"},model:{value:e.form.gatway,callback:function(t){e.$set(e.form,"gatway",t)},expression:"form.gatway"}})],1),e._v(" "),i("el-form-item",{attrs:{label:"子网掩码","label-width":e.labelWidth}},[i("el-input",{attrs:{autocomplete:"off"},model:{value:e.form.subnetMask,callback:function(t){e.$set(e.form,"subnetMask",t)},expression:"form.subnetMask"}})],1)],1)])],1)],1),e._v(" "),i("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[i("el-button",{attrs:{type:"primary"},on:{click:e.updateNetDeviceConfig}},[e._v("确 定")]),e._v(" "),i("el-button",{on:{click:e.dialogFormVisible}},[e._v("取 消")])],1)],1)},staticRenderFns:[]};var H={name:"DeviceEdit",data:function(){return{dialogFormVisible:!1,activeName:"base",devInfo:{}}},components:{BaseDeviceEdit:i("VU/8")($,F,!1,function(e){i("xMbe")},null,null).exports,PortEdit:i("VU/8")(I,E,!1,function(e){i("UbnL")},null,null).exports,NetEdit:i("VU/8")(P,z,!1,function(e){i("5Q7h")},null,null).exports},computed:{},created:function(){},mounted:function(){},methods:{show:function(e){this.devInfo=e,this.dialogFormVisible=!0},hide:function(){this.dialogFormVisible=!1},resetTab:function(e){this.activeName=e},handleClick:function(e,t){}}},W={render:function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",[e.dialogFormVisible?i("el-dialog",{attrs:{title:"设备配置",visible:e.dialogFormVisible,width:"80%"},on:{"update:visible":function(t){e.dialogFormVisible=t}}},[i("el-tabs",{on:{"tab-click":e.handleClick},model:{value:e.activeName,callback:function(t){e.activeName=t},expression:"activeName"}},[i("el-tab-pane",{attrs:{label:"基础配置",name:"base"}},[i("base-device-edit",{ref:"baseConfig",attrs:{deviceCode:e.devInfo.deviceCode}})],1),e._v(" "),i("el-tab-pane",{attrs:{label:"网络配置",name:"net"}},[i("net-edit",{ref:"netConfig",attrs:{deviceCode:e.devInfo.deviceCode}})],1),e._v(" "),i("el-tab-pane",{attrs:{label:"端口1",name:"port1"}},[i("port-edit",{ref:"portConfig",attrs:{deviceCode:e.devInfo.deviceCode,port:0}})],1),e._v(" "),i("el-tab-pane",{attrs:{label:"端口2",name:"port2"}},[i("port-edit",{ref:"portConfig",attrs:{deviceCode:e.devInfo.deviceCode,port:1}})],1)],1)],1):e._e()],1)},staticRenderFns:[]};var B={name:"DeviceList",provide:function(){return{closeEdit:this.closeEdit}},data:function(){return{currentPage:1,pageSize:10,pageTotal:1,devices:[],onlineDeviceCount:0,offlineDeviceCount:0,deviceSearchForm:{deviceState:null,deviceCode:null,deviceType:null,deviceIp:null}}},components:{DeviceEdit:i("VU/8")(H,W,!1,function(e){i("P2W7")},null,null).exports},computed:{devicesHandler:function(){return this.devices}},created:function(){},mounted:function(){this.init(),this.getDeives()},methods:{init:function(){var e=this;D.getDeviceStatistics().then(function(t){t.data&&(e.onlineDeviceCount=t.data.onlineDevice,e.offlineDeviceCount=t.data.offlineDevice)}),k.getConfig().then(function(t){if(t.data){var i=t.data;e.mqttInit(i.ip,i.port,i.user,i.password)}})},getDeives:function(){var e=this,t={page:this.currentPage-1,deviceState:this.deviceSearchForm.deviceState,deviceCode:this.deviceSearchForm.deviceCode,deviceType:this.deviceSearchForm.deviceType,ip:this.deviceSearchForm.deviceIp,size:this.pageSize};D.getDevices(t).then(function(t){t.data&&(e.devices=t.data.content,e.pageTotal=t.data.totalElements-1,e.currentPage=t.data.pageable.pageNumber+1)})},handleEdit:function(e,t){this.$refs.deviceEditDialog.resetTab("base"),this.$refs.deviceEditDialog.show(t)},handleReboot:function(e,t){var i=this;this.$confirm("此操作将重启设备，是否确定重启?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){D.reboot(t.deviceCode).then(function(e){i.$message({message:"重启设备命令已下发。",type:"success"})},function(e){i.$message.error("重启设备失败！"+e)})}).catch(function(){})},goBack:function(){window.history.back()},handleSizeChange:function(e){this.pageSize=e,this.getDeives()},handleCurrentChange:function(e){this.currentPage=e,this.getDeives()},closeEdit:function(e,t){for(var i=0;i<this.devices.length;i++)if(e===this.devices[i].deviceCode){this.$set(this.devices[i],"disableState",t);break}},mqttInit:function(e,t,i,a){var o=this,n=this,l={connectTimeout:4e4,clientId:"device-list-client-"+x.a.v1(),username:i,password:a,clean:!0},r="ws://"+e+":"+t+"/mqtt";this.client=w.a.connect(r,l),this.client.on("connect",function(e){console.log("连接成功！！！"),o.client.subscribe("personpositon/+/config",{qos:0},function(e){e?console.log("订阅config失败"):console.log("订阅config成功")}),o.client.subscribe("personpositon/+/timeout",{qos:1},function(e){e?console.log("订阅timeout失败"):console.log("订阅timeout成功")})}),this.client.on("message",function(e,t){-1!=e.indexOf("config")&&n.getDeives(),-1!=e.indexOf("timeout")&&o.$message({message:"网络开小差了，请等待一段时间后重试",type:"error"})})}}},V={render:function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",[i("el-row",[i("el-page-header",{attrs:{content:"设备列表"},on:{back:e.goBack}})],1),e._v(" "),i("el-row",{attrs:{justify:"start"}},[i("el-col",{attrs:{span:24}},[i("el-form",{staticClass:"device-list-charts",attrs:{inline:!0}},[i("el-form-item",{attrs:{label:"当前活动数量"}},[i("span",[e._v(e._s(e.onlineDeviceCount))])]),e._v(" "),i("el-form-item",{attrs:{label:"当前离线设备总数"}},[i("span",[e._v(e._s(e.offlineDeviceCount))])])],1)],1)],1),e._v(" "),i("el-row",{attrs:{justify:"start"}},[i("el-col",{attrs:{span:24}},[i("el-form",{staticClass:"demo-form-inline",attrs:{inline:!0,model:e.deviceSearchForm}},[i("el-form-item",{attrs:{label:"状态"}},[i("el-select",{attrs:{placeholder:"状态"},model:{value:e.deviceSearchForm.deviceState,callback:function(t){e.$set(e.deviceSearchForm,"deviceState",t)},expression:"deviceSearchForm.deviceState"}},[i("el-option",{attrs:{label:"全部",value:""}}),e._v(" "),i("el-option",{attrs:{label:"在线",value:"online"}}),e._v(" "),i("el-option",{attrs:{label:"离线",value:"offline"}})],1)],1),e._v(" "),i("el-form-item",{attrs:{label:"设备编号"}},[i("el-input",{attrs:{placeholder:"设备编号"},model:{value:e.deviceSearchForm.deviceCode,callback:function(t){e.$set(e.deviceSearchForm,"deviceCode",t)},expression:"deviceSearchForm.deviceCode"}})],1),e._v(" "),i("el-form-item",{attrs:{label:"IP地址"}},[i("el-input",{attrs:{placeholder:"IP地址"},model:{value:e.deviceSearchForm.deviceIp,callback:function(t){e.$set(e.deviceSearchForm,"deviceIp",t)},expression:"deviceSearchForm.deviceIp"}})],1),e._v(" "),i("el-form-item",{attrs:{label:"设备类型"}},[i("el-select",{attrs:{placeholder:"设备类型"},model:{value:e.deviceSearchForm.deviceType,callback:function(t){e.$set(e.deviceSearchForm,"deviceType",t)},expression:"deviceSearchForm.deviceType"}},[i("el-option",{attrs:{label:"全部",value:""}}),e._v(" "),i("el-option",{attrs:{label:"人员定位",value:"人员定位"}})],1)],1),e._v(" "),i("el-form-item",[i("el-button",{attrs:{type:"primary"},on:{click:e.getDeives}},[e._v("查询")])],1)],1)],1)],1),e._v(" "),i("el-row",[i("el-col",{attrs:{span:24}},[[i("el-table",{staticStyle:{width:"100%"},attrs:{data:e.devicesHandler,stripe:""}},[i("el-table-column",{attrs:{prop:"deviceCode",label:"设备编号"}}),e._v(" "),i("el-table-column",{attrs:{prop:"deviceType",label:"设备类型"}}),e._v(" "),i("el-table-column",{attrs:{prop:"deviceState",label:"设备状态"},scopedSlots:e._u([{key:"default",fn:function(t){return["online"===t.row.deviceState?i("span",[i("el-tag",{attrs:{type:"online"===t.row.deviceState?"primary":"danger"}},[e._v("在线")])],1):e._e(),e._v(" "),"offline"===t.row.deviceState?i("span",[i("el-tag",{attrs:{type:"online"===t.row.deviceState?"primary":"danger"}},[e._v("离线")])],1):e._e()]}}])}),e._v(" "),i("el-table-column",{attrs:{prop:"ip",label:"IP"}}),e._v(" "),i("el-table-column",{attrs:{prop:"updateTime",label:"更新日期"}}),e._v(" "),i("el-table-column",{attrs:{label:"操作",width:"100",fixed:"right"},scopedSlots:e._u([{key:"default",fn:function(t){return["online"===t.row.deviceState?i("el-button",{attrs:{type:"text",size:"small",disabled:t.row.disableState},on:{click:function(i){return e.handleEdit(t.$index,t.row)}}},[e._v("编辑")]):e._e(),e._v(" "),"online"===t.row.deviceState?i("el-button",{attrs:{type:"text",size:"small"},on:{click:function(i){return e.handleReboot(t.$index,t.row)}}},[e._v("重启")]):e._e()]}}])})],1)]],2)],1),e._v(" "),i("el-row",{staticClass:"row-bg device-list-pagination",attrs:{type:"flex",justify:"end"}},[i("el-pagination",{attrs:{background:"",layout:"prev, pager, next","current-page":e.currentPage,"page-sizes":[10,20,50],"page-size":e.pageSize,total:e.pageTotal,"prev-text":"上一页","next-text":"下一页"},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}})],1),e._v(" "),i("device-edit",{ref:"deviceEditDialog"})],1)},staticRenderFns:[]};var N=i("VU/8")(B,V,!1,function(e){i("78dc")},null,null).exports,M=i("ifoU"),O=i.n(M),R={name:"RealtimeData",data:function(){return{client:null,deviceMap:new O.a,onlineDeviceCount:0,offlineDeviceCount:0}},created:function(){},mounted:function(){this.init()},methods:{goBack:function(){window.history.back()},init:function(){var e=this;D.getDeviceStatistics().then(function(t){t.data&&(e.onlineDeviceCount=t.data.onlineDevice,e.offlineDeviceCount=t.data.offlineDevice)}),k.getConfig().then(function(t){if(t.data){var i=t.data;e.mqttInit(i.ip,i.port,i.user,i.password)}})},mqttInit:function(e,t,i,a){var o=this,n=this,l={connectTimeout:4e4,clientId:"realtime-data-client-"+x.a.v1(),username:i,password:a,clean:!0},r="ws://"+e+":"+t+"/mqtt";this.client=w.a.connect(r,l),this.client.on("connect",function(e){console.log("连接成功！！！"),o.client.subscribe("personpositon/+/tags/+",{qos:0},function(e){e?console.log("订阅失败"):console.log("订阅成功")})}),this.client.on("message",function(e,t){var i=JSON.parse(t.toString());0==n.deviceMap.has(i.deviceCode)&&n.deviceMap.set(i.deviceCode,[]);var a=n.deviceMap.get(i.deviceCode);a.splice(0,0,i),a.length>5&&a.pop(),n.$forceUpdate()})}}},j={render:function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",[i("el-row",[i("el-page-header",{attrs:{content:"实时数据"},on:{back:e.goBack}})],1),e._v(" "),i("el-row",[i("el-col",{attrs:{span:24}},[i("el-form",{staticClass:"device-list-charts",attrs:{inline:!0}},[i("el-form-item",{attrs:{label:"当前活动数量"}},[i("span",[e._v(e._s(e.onlineDeviceCount))])]),e._v(" "),i("el-form-item",{attrs:{label:"当前离线设备总数"}},[i("span",[e._v(e._s(e.offlineDeviceCount))])])],1)],1)],1),e._v(" "),i("el-row",{model:{value:e.deviceMap,callback:function(t){e.deviceMap=t},expression:"deviceMap"}},e._l(e.deviceMap,function(t,a){return i("el-col",{key:a,staticClass:"box-card",attrs:{span:6}},[i("el-card",[i("div",{staticClass:"clearfix",attrs:{slot:"header"},slot:"header"},[i("span",[e._v("设备:"+e._s(t[0]))])]),e._v(" "),e._l(t[1],function(t,a){return i("div",{key:a,staticClass:"text item"},[e._v("\n          "+e._s("["+t.createTime+"]  "+t.tagId)+"\n        ")])})],2)],1)}),1)],1)},staticRenderFns:[]};var q=i("VU/8")(R,j,!1,function(e){i("abB5")},null,null).exports,U={getTagHistorys:function(e){return h.get("api/tags-history/list?"+e)},downloadTagHistorys:function(e){return h.get("api/tags-history/exportExcel?"+e)}},L={name:"HistoryData",data:function(){return{currentPage:1,pageSize:20,pageTotal:1,searchTimeBetween:null,selectedDevices:[],allSelectDeviceCheck:!1,devices:[],tagHistorySearchForm:{},tagHistorys:[],deviceCodeOption:null}},mounted:function(){this.getDeives(),this.getTagHistorys()},methods:{onAllSelectDeviceCode:function(){var e=this;this.devices.forEach(function(t){t.checked=e.allSelectDeviceCheck})},onSelectDeviceCode:function(){},onTagHistoryExcelExport:function(){var e=this;this.$message({message:"正在导出数据，请勿执行其它操作",type:"success"});var t={page:1,size:9999999};this.searchTimeBetween&&(t.startTime=this.changeTimestyle(this.searchTimeBetween[0]),t.endTime=this.changeTimestyle(this.searchTimeBetween[1])),this.selectedDevices=[],this.devices.forEach(function(t){t.checked&&e.selectedDevices.push(t.deviceCode)}),this.deviceCodeOption&&this.selectedDevices.push(this.deviceCodeOption),this.selectedDevices.length>0&&(t.deviceCode=this.selectedDevices),t=T.a.stringify(t,{arrayFormat:"repeat"});var i="http://"+window.location.host+"/tags-history/exportExcel?"+t,a=document.createElement("a");a.style.display="none",a.href=i,a.setAttribute("download","历史数据.xlsx"),document.body.appendChild(a),a.click()},onTagHistorySearchSubmit:function(){this.getTagHistorys()},goBack:function(){window.history.back()},handleSizeChange:function(e){this.pageSize=e,this.getTagHistorys()},handleCurrentChange:function(e){this.currentPage=e,this.getTagHistorys()},getDeives:function(){var e=this;D.getDevices({page:0,size:99999}).then(function(t){t.data&&(e.devices=t.data.content)})},changeTimestyle:function(e){var t=new Date(e).toJSON();return new Date(+new Date(t)+288e5).toISOString().replace(/T/g," ").replace(/\.[\d]{3}Z/,"")},getTagHistorys:function(){var e=this,t={page:this.currentPage-1,size:this.pageSize};this.searchTimeBetween&&(t.startTime=this.changeTimestyle(this.searchTimeBetween[0]),t.endTime=this.changeTimestyle(this.searchTimeBetween[1])),this.selectedDevices=[],this.deviceCodeOption&&this.selectedDevices.push(this.deviceCodeOption),this.selectedDevices.length>0&&(t.deviceCode=this.selectedDevices),t=T.a.stringify(t,{arrayFormat:"repeat"}),U.getTagHistorys(t).then(function(t){t.data&&(e.tagHistorys=t.data.content,e.pageTotal=t.data.totalElements-1,e.currentPage=t.data.pageable.pageNumber+1)})}}},A={render:function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",[i("el-row",[i("el-page-header",{attrs:{content:"历史数据"},on:{back:e.goBack}})],1),e._v(" "),i("el-row",{staticClass:"top",attrs:{type:"flex",justify:"start"}},[i("el-col",{attrs:{span:24}},[i("el-form",{staticClass:"align-left",attrs:{inline:!0,model:e.tagHistorySearchForm}},[i("el-form-item",{attrs:{label:"设备编号"}},[i("el-select",{attrs:{placeholder:"设备编号"},model:{value:e.deviceCodeOption,callback:function(t){e.deviceCodeOption=t},expression:"deviceCodeOption"}},[i("el-option",{attrs:{label:"全部",value:""}}),e._v(" "),e._l(e.devices,function(t){return i("el-option",{key:t.deviceCode,attrs:{value:t.deviceCode}},[e._v(e._s(t.deviceCode))])})],2)],1),e._v(" "),i("el-form-item",{attrs:{label:"查询时间"}},[i("el-date-picker",{attrs:{type:"datetimerange","range-separator":"至","start-placeholder":"开始日期","end-placeholder":"结束日期"},model:{value:e.searchTimeBetween,callback:function(t){e.searchTimeBetween=t},expression:"searchTimeBetween"}})],1),e._v(" "),i("el-form-item",[i("el-button",{attrs:{type:"primary"},on:{click:e.onTagHistorySearchSubmit}},[e._v("查询")])],1),e._v(" "),i("el-form-item",[i("el-button",{attrs:{type:"primary"},on:{click:e.onTagHistoryExcelExport}},[e._v("导出")])],1)],1)],1)],1),e._v(" "),i("el-row",[i("el-col",{attrs:{span:24}},[i("el-table",{staticStyle:{width:"100%"},attrs:{data:e.tagHistorys,stripe:""}},[i("el-table-column",{attrs:{prop:"tagType",label:"标签类型",width:"180"}}),e._v(" "),i("el-table-column",{attrs:{prop:"createTime",label:"时间",width:"180"}}),e._v(" "),i("el-table-column",{attrs:{prop:"deviceCode",label:"设备编号",width:"180"}}),e._v(" "),i("el-table-column",{attrs:{prop:"tagId",label:"卡号"}})],1)],1)],1),e._v(" "),i("el-row",{staticClass:"row-bg device-list-pagination",attrs:{type:"flex",justify:"end"}},[i("el-pagination",{attrs:{background:"",layout:"prev, pager, next","current-page":e.currentPage,"page-sizes":[20,50,100],"page-size":e.pageSize,total:e.pageTotal,"prev-text":"上一页","next-text":"下一页"},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}})],1)],1)},staticRenderFns:[]};var X=i("VU/8")(L,A,!1,function(e){i("GHeb")},null,null).exports;a.default.use(s.a);var G=new s.a({routes:[{path:"/",component:N,meta:{title:"人员定位-登陆"}},{path:"/login",name:"Login",component:C,meta:{title:"人员定位-登陆"}},{path:"/device",name:"DeviceList",component:N,meta:{title:"人员定位-设备列表"}},{path:"/realtime",name:"RealtimeData",component:q,meta:{title:"人员定位-实时数据",requireAuth:!0}},{path:"/history",name:"HistoryData",component:X,meta:{title:"人员定位-历史数据",requireAuth:!0}}]});i("tvR6"),i("e0XP");a.default.config.productionTip=!1,a.default.use(p.a),a.default.prototype.axios=f.a,G.beforeEach(function(e,t,i){e.meta.title&&(document.title=e.meta.title),i()}),new a.default({el:"#app",router:G,components:{App:r},template:"<App/>"})},P2W7:function(e,t){},UbnL:function(e,t){},abB5:function(e,t){},e0XP:function(e,t){},tvR6:function(e,t){},uYIX:function(e,t){},xMbe:function(e,t){}},["NHnr"]);
//# sourceMappingURL=app.0e18f4c0c47d271f870e.js.map