"use strict";(self["webpackChunkstu_score_front"]=self["webpackChunkstu_score_front"]||[]).push([[310],{7310:function(e,t,a){a.r(t),a.d(t,{default:function(){return d}});var s=function(){var e=this,t=e._self._c;return t("div",[t("div",{staticClass:"findAdd"},[t("el-input",{staticStyle:{width:"240px"},attrs:{autocomplete:"off",placeholder:"请输入课程名",clearable:""},model:{value:e.courseName,callback:function(t){e.courseName=t},expression:"courseName"}}),t("el-select",{attrs:{placeholder:"请选择开课时期",clearable:""},model:{value:e.coursePeriod,callback:function(t){e.coursePeriod=t},expression:"coursePeriod"}},e._l(e.coursePeriods,(function(e){return t("el-option",{key:e.value,attrs:{label:e.label,value:e.label}})})),1),t("el-button",{attrs:{type:"primary",icon:"el-icon-search"},on:{click:e.searchCourseList}},[e._v("搜索")]),t("el-button",{staticStyle:{"margin-left":"0"},attrs:{icon:"el-icon-refresh"},on:{click:e.resetCourseList}},[e._v("刷新")])],1),t("div",{staticClass:"classTable"},[t("el-table",{staticStyle:{width:"100%"},attrs:{data:e.courseList,stripe:"",size:"small"}},[t("el-table-column",{attrs:{label:"序号",width:"80",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(" "+e._s((e.pageNum-1)*e.pageSize+t.$index+1)+" ")]}}])}),t("el-table-column",{attrs:{prop:"courseId",label:"课程编号",align:"center",width:"130",sortable:""}}),t("el-table-column",{attrs:{prop:"courseName",label:"课程名",align:"center",width:"200",sortable:""}}),t("el-table-column",{attrs:{prop:"majorName",label:"所属专业",align:"center",width:"200",sortable:""}}),t("el-table-column",{attrs:{prop:"ifDegree",label:"课程类型",width:"100",align:"center",sortable:""},scopedSlots:e._u([{key:"default",fn:function(a){return[1==a.row.ifDegree?t("el-tag",[e._v("必修")]):0==a.row.ifDegree?t("el-tag",{attrs:{type:"success"}},[e._v("选修")]):e._e()]}}])}),t("el-table-column",{attrs:{prop:"coursePeriod",label:"开课时期",align:"center",width:"150",sortable:""}}),t("el-table-column",{attrs:{prop:"stuChooseNum",label:"选课人数",align:"center",width:"100",sortable:""}}),t("el-table-column",{attrs:{prop:"state",label:"状态",align:"center",width:"180",sortable:""},scopedSlots:e._u([{key:"default",fn:function(a){return[1==a.row.state?t("el-tag",{attrs:{type:"success"}},[e._v("已选")]):1!=a.row.state?t("el-tag",{attrs:{type:"primary"}},[e._v("未选")]):e._e()]}}])}),t("el-table-column",{attrs:{prop:"handle",label:"操作",align:"center"},scopedSlots:e._u([{key:"default",fn:function(a){return 1!=a.row.state?[t("el-button",{attrs:{type:"primary",size:"small"},on:{click:function(t){return e.chooseCourse(a.row)}}},[e._v("选课")])]:void 0}}],null,!0)})],1)],1),t("div",{staticClass:"page"},[t("el-pagination",{attrs:{"pager-count":11,background:"","current-page":e.pageNum,"page-size":e.pageSize,background:e.pageBackground,"page-sizes":[5,10,20,50,100],layout:"total, sizes, prev, pager, next, jumper",total:e.total},on:{"current-change":e.handleCurrentChange,"size-change":e.handleSizeChange}})],1),t("el-backtop",{attrs:{"visibility-height":200}},[t("div",{staticStyle:{"{\n      height":"100%",width:"100%","background-color":"#f2f5f6","box-shadow":"0 0 6px rgba(0,0,0, .30)","text-align":"center","line-height":"40px",color:"#1989fa"}},[e._v(" ↑ ")])])],1)},r=[],o=a(4161),l=a(7344),i={name:"ChooseCourse",data(){return{coursePeriod:"",pageBackground:!0,courseName:"",pageNum:1,pageSize:20,total:0,schoolPeriodNum:0,courseList:[],coursePeriods:[{value:"1",label:"大一上"},{value:"2",label:"大一下"},{value:"3",label:"大二上"},{value:"4",label:"大二下"},{value:"5",label:"大三上"},{value:"6",label:"大三下"},{value:"7",label:"大四上"},{value:"8",label:"大四下"}]}},methods:{refreshSchoolPeriodNum(){let e=this.$store.state.userInfo.admissionYear,t=new Date(e+"-9-01 00:00:00"),a=new Date,s=a-t,r=Math.floor(s/1e3/86400/180.625);this.schoolPeriodNum=r},getCourseList(){o.Z.get(l.Z.baseURL+"/student/coursePageList?pageNum="+this.pageNum+"&pageSize="+this.pageSize+"&courseName="+this.courseName+"&majorName="+this.$store.state.userInfo.majorName+"&schoolPeriodNum="+this.schoolPeriodNum+"&coursePeriod="+this.coursePeriod,{headers:{Authorization:this.$store.state.token}}).then((e=>{"20000"==e.data.code?(this.courseList=e.data.data.courseList,this.total=e.data.data.total):this.$message.error(e.data.msg)}))},chooseCourse(e){o.Z.post(l.Z.baseURL+"/chooseCourse/add",{stuId:this.$store.state.userInfo.stuId,courseId:e.courseId,courseName:e.courseName,stuName:this.$store.state.userInfo.stuName,majorName:e.majorName,ifDegree:e.ifDegree,coursePeriod:e.coursePeriod},{headers:{Authorization:this.$store.state.token}}).then((e=>{"20000"==e.data.code?(this.getCourseList(),this.$message.success(e.data.msg)):this.$message.error(e.data.msg)}))},resetCourseList(){location.reload()},handleCurrentChange(e){this.pageNum=e,this.getCourseList()},handleSizeChange(e){this.pageSize=e,this.pageNum=1,this.getCourseList()},searchCourseList(){this.pageNum=1,this.getCourseList()}},created(){this.refreshSchoolPeriodNum(),this.getCourseList()}},u=i,c=a(3736),n=(0,c.Z)(u,s,r,!1,null,"8477ad7c",null),d=n.exports},7344:function(e,t){const a={assertsSubDirectory:"static",assertsPublicPath:"/",baseURL:"http://118.31.60.230:8091",timeout:2e4,changeOrigin:!0};t["Z"]=a}}]);
//# sourceMappingURL=310.9c873bae.js.map