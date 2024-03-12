<template>
  <div>
    <div class="findAdd">
      <el-input autocomplete="off" placeholder="请输入课程名" v-model="courseName" clearable style="width: 240px"></el-input>
      <el-select v-model="coursePeriod" placeholder="请选择开课时期" clearable>
        <el-option
            v-for="item in coursePeriods"
            :key="item.value"
            :label="item.label"
            :value="item.label">
        </el-option>
      </el-select>
      <el-button type="primary" @click="searchCourseList" icon="el-icon-search">搜索</el-button>
      <el-button @click="resetCourseList" icon="el-icon-refresh" style="margin-left: 0">刷新</el-button>
    </div>

    <div class="classTable">
      <el-table :data="courseList" stripe size="small" style="width: 100%">

        <el-table-column label="序号" width="80" align="center">
          <template slot-scope="scope">
            {{
              (pageNum - 1) * pageSize + scope.$index + 1
            }}
          </template>
        </el-table-column>

        <el-table-column prop="courseId" label="课程编号" align="center" width="130" sortable></el-table-column>
        <el-table-column prop="courseName" label="课程名" align="center" width="200" sortable></el-table-column>
        <el-table-column prop="majorName" label="所属专业" align="center" width="200" sortable></el-table-column>

        <el-table-column prop="ifDegree" label="课程类型" width="100" align="center" sortable>
          <template slot-scope="scope">
            <el-tag v-if="scope.row.ifDegree == 1">必修</el-tag>
            <el-tag v-else-if="scope.row.ifDegree == 0" type="success">选修</el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="coursePeriod" label="开课时期" align="center" width="150" sortable></el-table-column>
        <el-table-column prop="stuChooseNum" label="选课人数" align="center" width="100" sortable></el-table-column>
        <el-table-column prop="state" label="状态" align="center" width="180" sortable>
          <template slot-scope="scope" >
            <el-tag type="success" v-if="scope.row.state==1">已选</el-tag>
            <el-tag type="primary" v-else-if="scope.row.state!=1">未选</el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="handle" label="操作" align="center" >
          <template slot-scope="scope" v-if="scope.row.state!=1">
            <el-button @click="chooseCourse(scope.row)" type="primary" size="small">选课</el-button>
          </template>
        </el-table-column>

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
          :background="pageBackground"
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
  name: "ChooseCourse",
  data() {
    return {
      coursePeriod:'',
      pageBackground:true,
      courseName: '',
      pageNum: 1,
      pageSize: 20,
      total: 0,
      schoolPeriodNum: 0,
      courseList: [],
      coursePeriods: [
        {value: '1', label: '大一上'},
        {value: '2', label: '大一下'},
        {value: '3', label: '大二上'},
        {value: '4', label: '大二下'},
        {value: '5', label: '大三上'},
        {value: '6', label: '大三下'},
        {value: '7', label: '大四上'},
        {value: '8', label: '大四下'},
      ],
    }
  },
  methods: {
    refreshSchoolPeriodNum() {
      let MyAdmissionYear = this.$store.state.userInfo.admissionYear
      let myAdmissionTime = new Date(MyAdmissionYear + '-9-01 00:00:00')//今年9月份开学时间
      let nowTime = new Date()
      let schoolTime = nowTime - myAdmissionTime
      let schoolPeriodNum = Math.floor(schoolTime / 1000 / (3600 * 24) / 180.625)
      this.schoolPeriodNum = schoolPeriodNum
    },
    getCourseList() {
      axios.get(service.baseURL+"/student/coursePageList?pageNum=" + this.pageNum +
          "&pageSize=" + this.pageSize + "&courseName=" + this.courseName +
          "&majorName=" + this.$store.state.userInfo.majorName + "&schoolPeriodNum=" + this.schoolPeriodNum+
          "&coursePeriod="+this.coursePeriod, {
            headers: {
              Authorization: this.$store.state.token
            }
          }
      ).then((resp) => {
        if (resp.data.code == '20000') {
          this.courseList = resp.data.data.courseList
          this.total = resp.data.data.total
        } else {
          this.$message.error(resp.data.msg)
        }
      })
    },
    chooseCourse(row) {//选课
      axios.post(service.baseURL+"/chooseCourse/add", {
            "stuId":this.$store.state.userInfo.stuId,
            "courseId":row.courseId,
            "courseName":row.courseName,
            "stuName":this.$store.state.userInfo.stuName,
            "majorName":row.majorName,
            "ifDegree":row.ifDegree,
            "coursePeriod":row.coursePeriod,
          },
          {
            headers: {
              Authorization: this.$store.state.token
            }
          }).then((resp=>{
            if (resp.data.code=='20000'){
              this.getCourseList()
              this.$message.success(resp.data.msg)
            }
            else {
              this.$message.error(resp.data.msg)
            }
      }))
    },
    resetCourseList() {
      location.reload()
    },

    handleCurrentChange(pageNum) {//2
      this.pageNum = pageNum
      this.getCourseList()
    },
    handleSizeChange(pageSize) {//1
      this.pageSize = pageSize
      this.pageNum = 1
      this.getCourseList()
    },
    searchCourseList() {
      this.pageNum = 1
      this.getCourseList();
    }
  },
  created() {
    this.refreshSchoolPeriodNum()
    this.getCourseList()
  }
}
</script>

<style scoped>
.findAdd {
  margin: 20px auto;
}
.el-input,.el-button,.el-select{
  margin-right: 20px;
}
</style>