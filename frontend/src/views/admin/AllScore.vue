<template>
  <div>
    <div class="findAdd">
      <el-input autocomplete="off" placeholder="请输入学生姓名" v-model="stuName" clearable style="width: 240px;margin-left: 0"></el-input>
      <el-input autocomplete="off" placeholder="请输入班号" v-model="className" clearable
                style="width: 240px"></el-input>
      <el-select placeholder="请选择年级（必填）" clearable v-model="admissionYear">
        <el-option
            v-for="item in classYears"
            :key="item.value"
            :label="item.label"
            :value="item.label">
        </el-option>
      </el-select>
      <el-select placeholder="请选择专业（必填）" clearable v-model="major">
        <el-option
            v-for="item in majors"
            :key="item.majorId"
            :label="item.majorName"
            :value="item.majorId">
        </el-option>
      </el-select>
      <el-button type="primary" @click="searchStuList" icon="el-icon-search">搜索</el-button>
      <el-button @click="resetStuList" icon="el-icon-refresh" style="margin-left: 0">刷新</el-button>
    </div>

    <div class="stuTable">
      <el-table :data="stuScoreList" stripe size="small" style="width: 100%">
        <el-table-column label="排名" width="40" align="center">
          <template slot-scope="scope">
            {{
              (pageNum - 1) * pageSize + scope.$index + 1
            }}
          </template>
        </el-table-column>
        <el-table-column prop="stuId" label="学号" sortable align="center" width="90"></el-table-column>
        <el-table-column prop="stuName" label="学生姓名" align="center" sortable width="100"></el-table-column>
        <el-table-column prop="className" align="center" width="180" label="班级名称" sortable></el-table-column>
        <el-table-column prop="" :label="item.courseName" v-for="(item,index) in courseList" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.courseScore[index] }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="scoreTotal" label="必修课总分" sortable align="center"></el-table-column>
      </el-table>
    </div>
    <div class="page">
      <el-pagination style="text-align: center"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
          :pager-count="11"
          background
          :current-page="pageNum"
          :page-size="pageSize"
          :page-sizes="[5, 10, 20, 50,100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>
    <el-backtop :visibility-height=200>
      <div
          style="{
        height: 100%;
        width: 100%;
        background-color: #f2f5f6;
        box-shadow: 0 0 6px rgba(0,0,0, .30);
        text-align: center;
        line-height: 40px;
        color: #1989fa;
      }">
        ↑
      </div>
    </el-backtop>
  </div>
</template>

<script>
import axios from "axios";
import service from "@/request";
export default {
  stuName: "MajorScore",
  data() {
    let nowTime = new Date()
    let nowYear = nowTime.getFullYear();
    let maxYear = nowYear - 1//默认是当前年-1
    let startTime = new Date(nowYear + '-9-01 00:00:00')//今年9月份开学时间
    if (nowTime > startTime) {
      maxYear += 1
    }
    return {
      majors:[],
      major:'',
      admissionYear:'',
      //查询本专业，本年级的所有课程
      courseList: [],
      stuScoreList: [
        {courseScore: []},
        {courseScore: []},
        {courseScore: []},
        {courseScore: []},
      ],

      pageNum: 1,
      pageSize: 50,
      stuId: '',
      stuName: '',
      className: '',
      total: 0,
      classYears: [{value: '1', label: maxYear}, {value: '2', label: maxYear - 1},
        {value: '3', label: maxYear - 2}, {value: '4', label: maxYear - 3}],

    }
  },
  created() {
    this.getMajors()
  },
  methods: {
    getStuScoreList() {
      axios.get(service.baseURL+"/score/getStuScoreList?admissionYearMajor=" + this.admissionYear +
          this.major + "&stuName=" + this.stuName+"&pageSize="+this.pageSize+"&pageNum="+this.pageNum
          +"&className="+this.className, {
        headers: {
          Authorization: this.$store.state.token
        }
      }).then((resp => {
        if (resp.data.code == '20000') {
          this.stuScoreList = resp.data.data.stuScoreList
          this.total=resp.data.data.total
        }
      }))
    },
    getCourseList() {//渲染表头
      axios.get(service.baseURL+"/course/getScoreCourse?majorId=" + this.major +
          "&admissionYear=" + this.admissionYear, {
        headers: {
          Authorization: this.$store.state.token
        }
      }).then((resp => {
        if (resp.data.code == '20000') {
          this.courseList = resp.data.data
        }
      }))
    },
    getMajors(){
      axios.get(service.baseURL+'/major/getAllMajorName',{
        headers: {
          Authorization: this.$store.state.token
        }
      }).then((resp)=>{
        this.majors=resp.data.data
      })
    },
    resetStuList() {
      location.reload();
    },
    searchStuList() {
      if ((this.major!=''&&this.major!=null)&&(this.admissionYear!=''&&this.admissionYear!=null)){
        this.pageNum = 1
        this.getCourseList()
        this.getStuScoreList()
      }
    },

    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.getStuScoreList()
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.pageNum = 1
      this.getStuScoreList()
    },
  }
}
</script>

<style scoped>
.findAdd {
  margin: 20px auto;
}

.el-input,.el-select,.el-button  {
  margin-right: 20px;
}
/*.el-select {*/
/*  margin-left: 15px;*/
/*}*/

/*.el-button {*/
/*  margin-left: 20px;*/
/*}*/

</style>