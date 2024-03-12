<template>
  <div>
    <div class="findAdd">
      <el-input autocomplete="off" placeholder="请输入学号" v-model="stuId" clearable style="width: 240px"></el-input>
      <el-input autocomplete="off" placeholder="请输入学生姓名" v-model="stuName" clearable
                style="width: 240px"></el-input>
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
        <el-table-column prop="handle" label="操作" align="center">
          <template slot-scope="scope">
            <el-button @click="openMessage(scope.row)" type="primary" size="mini">私信</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="page">
      <el-pagination style="text-align: center"          @current-change="handleCurrentChange"
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

    <div class="msgDialog">
      <el-dialog
          @close="clearMsgForm"
          title="给学生发消息"
          :visible.sync="msgFormVisible">
        <el-form :model="msgForm" :rules="msgRules" ref="msgRef">

          <el-form-item label="学生姓名" label-width="80px" prop="stuName">
            <el-input v-model="msgForm.stuName" disabled></el-input>
          </el-form-item>

          <el-form-item label="消息" label-width="80px" prop="msgContent">
            <el-input v-model="msgForm.msgContent" placeholder="请输入消息" autocomplete="off" clearable type="textarea"
                      rows="5"></el-input>
          </el-form-item>
        </el-form>

        <div slot="footer" class="dialog-footer">
          <el-button @click="cancelMsg">取 消</el-button>
          <el-button type="primary" @click="msgToStu">确 定</el-button>
        </div>
      </el-dialog>
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
  name: "ClassScore",
  data() {
    return {
      msgRules: {
        msgContent: [
          {required: true, message: "消息不能为空", trigger: "blur"},
          {min: 1, max: 255, message: "消息长度为1到255个字符", trigger: "blur"},
        ]
      },
      msgForm: {
        msgContent: ''
      },
      msgFormVisible: false,
      //查询本专业，本年级的所有课程
      courseList: [],
      stuScoreList: [
        {courseScore: []},
        {courseScore: []},
        {courseScore: []},
        {courseScore: []},
      ],

      pageNum: 1,
      pageSize: 20,
      stuId: '',
      stuName: '',
      className: '',
      total: 0,

    }
  },
  created() {
    this.getCourseList()
    this.getStuScoreList()
  },
  methods: {

    msgToStu(row) {
      this.$refs.msgRef.validate((valid) => {
        if (valid) {
          axios.post(service.baseURL + "/stuMessage/addMsg", {
            stuId: this.msgForm.stuId,
            stuName: this.msgForm.stuName,
            teaId: this.$store.state.userInfo.teaId,
            msgContent: this.msgForm.msgContent
          }, {
            headers: {
              Authorization: this.$store.state.token
            }
          })
          axios.post(service.baseURL + "/teaMessage/addMsg", {
            stuId: this.msgForm.stuId,
            stuName: this.msgForm.stuName,
            teaId: this.$store.state.userInfo.teaId,
            msgContent: this.msgForm.msgContent
          }, {
            headers: {
              Authorization: this.$store.state.token
            }
          }).then((resp) => {
            if (resp.data.code == '20000') {
              this.msgFormVisible = false;//编辑成功关闭表单
              this.clearMsgForm()
              this.$message.success(resp.data.msg)
            } else {
              this.$message.error(resp.data.msg)
            }
          })
        } else {
          return false
        }
      })
    },
    cancelMsg() {
      this.msgFormVisible = false
      this.clearMsgForm()
      this.$message.info("已取消发消息")
    },
    clearMsgForm() {
      this.msgForm = {}
      this.$set(this.msgForm, 'msgContent', '')
      this.$refs.msgRef.clearValidate()
    },
    openMessage(row) {
      this.msgFormVisible = true
      this.msgForm.stuId = row.stuId
      this.msgForm.stuName = row.stuName
    },
    getStuScoreList() {
      axios.get(service.baseURL + "/score/getStuScoreList?admissionYearMajor=" + this.$store.state.userInfo.classYear +
          this.$store.state.userInfo.majorId + "&stuName=" + this.stuName + "&pageSize=" + this.pageSize + "&pageNum=" + this.pageNum
          + "&stuId=" + this.stuId + "&className=" + this.$store.state.userInfo.classNo, {
        headers: {
          Authorization: this.$store.state.token
        }
      }).then((resp => {
        if (resp.data.code == '20000') {
          this.stuScoreList = resp.data.data.stuScoreList
          this.total = resp.data.data.total
        }
      }))
    },
    getCourseList() {//渲染表头
      axios.get(service.baseURL + "/course/getScoreCourse?majorId=" + this.$store.state.userInfo.majorId +
          "&admissionYear=" + this.$store.state.userInfo.classYear, {
        headers: {
          Authorization: this.$store.state.token
        }
      }).then((resp => {
        if (resp.data.code == '20000') {
          this.courseList = resp.data.data
        }
      }))
    },
    resetStuList() {
      location.reload();
    },
    searchStuList() {
      this.pageNum = 1
      this.getStuScoreList()
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

.el-input {
  width: 400px;
  margin-right: 20px;
}

.el-button {
  margin-right: 20px;
}

</style>