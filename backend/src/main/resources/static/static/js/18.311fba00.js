"use strict";(self["webpackChunkstu_score_front"]=self["webpackChunkstu_score_front"]||[]).push([[18],{2018:function(t,e,s){s.r(e),s.d(e,{default:function(){return m}});var r=function(){var t=this,e=t._self._c;return e("div",{staticClass:"login"},[e("div",{staticClass:"content"},[e("h1",[t._v("大学生成绩管理系统")]),e("el-form",{ref:"loginForm",staticClass:"demo-ruleForm",staticStyle:{"text-align":"center"},attrs:{model:t.loginForm,"status-icon":"",rules:t.rules}},[e("el-form-item",{attrs:{prop:"id"}},[e("el-input",{attrs:{autocomplete:"off",placeholder:"请输入学号/班主任编号/管理员编号",clearable:""},model:{value:t.loginForm.id,callback:function(e){t.$set(t.loginForm,"id",e)},expression:"loginForm.id"}})],1),e("el-form-item",{attrs:{prop:"password"}},[e("el-input",{attrs:{"show-password":"",autocomplete:"off",placeholder:"请输入密码(非管理员初始为身份证后6位)",clearable:""},model:{value:t.loginForm.password,callback:function(e){t.$set(t.loginForm,"password",e)},expression:"loginForm.password"}})],1),e("el-form-item",{attrs:{prop:"state"}},[e("el-select",{attrs:{clearable:"",placeholder:"请选择身份"},model:{value:t.loginForm.state,callback:function(e){t.$set(t.loginForm,"state",e)},expression:"loginForm.state"}},t._l(t.options,(function(t){return e("el-option",{key:t.value,attrs:{label:t.label,value:t.label}})})),1)],1),e("el-form-item",[e("el-button",{staticStyle:{width:"145px"},attrs:{type:"primary",round:"",disabled:t.ifDisabled},on:{click:function(e){return t.submitForm("loginForm")}}},[t._v("登录")]),e("el-button",{staticStyle:{width:"145px"},attrs:{round:""},on:{click:function(e){return t.resetForm("loginForm")}}},[t._v("重置")])],1),e("el-form-item",[e("el-button",{staticStyle:{width:"300px"},attrs:{type:"primary",round:""},on:{click:t.toRegister}},[t._v("管理员注册")])],1)],1)],1)])},o=[],a=(s(7658),s(4161)),i=s(7344),l={name:"Login",data(){return{loginForm:{id:"",password:"",state:""},options:[{value:"1",label:"学生"},{value:"2",label:"班主任"},{value:"3",label:"管理员"}],rules:{id:[{required:!0,message:"编号不能为空",trigger:"blur"}],password:[{required:!0,message:"密码不能为空",trigger:"blur"},{min:6,max:16,message:"密码长度为6到16个字符",trigger:"blur"}],state:[{required:!0,message:"请选择身份",trigger:"blur"}]},ifDisabled:null}},methods:{toRegister(){this.$router.push("/addAdmin")},submitForm(t){this.$refs.loginForm.validate((t=>{if(!t)return!1;a.Z.post(i.Z.baseURL+"/login?id="+this.loginForm.id+"&password="+this.loginForm.password+"&state="+this.loginForm.state).then((t=>{"20000"==t.data.code?(this.$message.success(t.data.msg),this.$store.commit("SET_TOKEN",t.data.data.token),this.$store.commit("SET_USERINFO",t.data.data.user),"管理员"==t.data.data.state?(this.ifDisabled=!0,this.$router.push("/admin")):"学生"==t.data.data.state?(this.ifDisabled=!0,this.$router.push("/student")):(this.ifDisabled=!0,this.$router.push("/teacher")),this.$refs["loginForm"].resetFields()):(this.ifDisabled=!1,this.$message.error(t.data.msg))})).catch((()=>{}))}))},resetForm(t){this.$refs.loginForm.resetFields()}}},n=l,u=s(3736),d=(0,u.Z)(n,r,o,!1,null,"0e76f4ba",null),m=d.exports},7344:function(t,e){const s={assertsSubDirectory:"static",assertsPublicPath:"/",baseURL:"http://118.31.60.230:8091",timeout:2e4,changeOrigin:!0};e["Z"]=s}}]);
//# sourceMappingURL=18.311fba00.js.map