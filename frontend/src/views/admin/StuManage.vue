<template>
  <div>
    <div class="findAdd">
      <el-input autocomplete="off" placeholder="请输入学号" v-model="stuId" clearable style="width: 240px"></el-input>
      <el-input autocomplete="off" placeholder="请输入学生姓名" v-model="stuName" clearable style="width: 240px"></el-input>
      <el-input autocomplete="off" placeholder="请输入班级名称" v-model="className" clearable
                style="width: 240px"></el-input>
      <el-button type="primary" @click="searchStuList" icon="el-icon-search">搜索</el-button>
      <el-button @click="resetStuList" icon="el-icon-refresh" style="margin-left:0">刷新</el-button>
      <el-button style="margin-left: 45px" type="primary" @click="openAddStu" icon="el-icon-plus">新增学生</el-button>
    </div>
    <div class="stuTable">
      <el-table :data="stuList" stripe size="small" style="width: 100%">
        <el-table-column label="序号" width="80" align="center">
          <template slot-scope="scope">
            {{
              (pageNum - 1) * pageSize + scope.$index + 1
            }}
          </template>
        </el-table-column>
        <el-table-column prop="stuId" label="学号" sortable align="center" width="130"></el-table-column>
        <el-table-column prop="stuName" label="学生姓名" align="center" sortable width="150"></el-table-column>
        <el-table-column prop="majorName" sortable align="center" width="200" label="专业名称"></el-table-column>
        <el-table-column prop="className" align="center" width="220" label="班级名称" sortable></el-table-column>
        <el-table-column prop="teaName" sortable align="center" width="150" label="班主任姓名"></el-table-column>
        <el-table-column prop="handle" align="center" label="操作">
          <template slot-scope="scope">
            <el-button @click="openStuScore(scope.row)" type="primary" size="small">打分</el-button>
            <el-button @click="openEditStu(scope.row)" type="warning" size="small">编辑</el-button>
            <el-button @click="delStu(scope.row)" type="danger" size="small">删除</el-button>
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
          :page-sizes="[5, 10, 20, 50,100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>
    <!--    添加学生表单-->
    <div class="addStuDialog">
      <el-dialog
          @close="clearAddForm"
          title="新增学生"
          :visible.sync="addFormVisible">
        <el-form :model="addStuForm" :rules="addRules" ref="addStuFormRef">
          <el-form-item label="入学年份" label-width="150px" prop="admissionYear">
            <el-select v-model="addStuForm.admissionYear" placeholder="请选择入学年份" clearable @change="add_Year(addStuForm)">
              <el-option
                  v-for="item in admissionYears"
                  :key="item.value"
                  :label="item.label"
                  :value="item.label">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="专业" label-width="150px" prop="majorName">
            <el-select v-model="addStuForm.majorName" placeholder="请选择专业" clearable @change="add_Major(addStuForm)">
              <el-option
                  v-for="item in majorNames"
                  :key="item.majorId"
                  :label="item.majorName"
                  :value="item.majorName">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="班号" label-width="150px" prop="classId">
            <el-select v-model="addStuForm.classId" placeholder="请选择班号" clearable id="selectClassId">
              <el-option
                  v-for="item in classNos"
                  :key="item.classId"
                  :label="item.classId.substring(6,7)"
                  :value="item.classId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="学生姓名" label-width="150px" prop="stuName">
            <el-input v-model="addStuForm.stuName" autocomplete="off" placeholder="请输入学生姓名" clearable></el-input>
          </el-form-item>
          <el-form-item label="身份证" label-width="150px" prop="stuIdCard">
            <el-input v-model="addStuForm.stuIdCard" autocomplete="off" placeholder="请输入身份证"
                      clearable></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="cancelAdd">取 消</el-button>
          <el-button type="primary" @click="addStu">确 定</el-button>
        </div>
      </el-dialog>
    </div>
    <!--    编辑学生表单-->
    <div class="editStuDialog">
      <el-dialog
          @close="clearEditForm"
          title="编辑学生"
          :visible.sync="editFormVisible">
        <el-form :model="editStuForm" :rules="editRules" ref="editStuFormRef">
          <el-form-item label="学号" label-width="150px" prop="stuId">
            <el-input v-model="editStuForm.stuId" disabled></el-input>
          </el-form-item>
          <el-form-item label="入学年份" label-width="150px" prop="admissionYear">
            <el-input v-model="editStuForm.admissionYear" disabled></el-input>
          </el-form-item>
          <el-form-item label="专业" label-width="150px" prop="majorName">
            <el-input v-model="editStuForm.majorName" disabled></el-input>
          </el-form-item>
          <el-form-item label="班号" label-width="150px" prop="classId">
            <el-input v-model="editStuForm.classNo" disabled></el-input>
          </el-form-item>
          <el-form-item label="学生姓名" label-width="150px" prop="stuName">
            <el-input v-model="editStuForm.stuName" autocomplete="off" placeholder="请输入学生姓名" clearable></el-input>
          </el-form-item>
          <el-form-item label="身份证" label-width="150px" prop="stuIdCard">
            <el-input v-model="editStuForm.stuIdCard" placeholder="请输入身份证" disabled></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="cancelEdit">取 消</el-button>
          <el-button type="primary" @click="editStu">确 定</el-button>
        </div>
      </el-dialog>
    </div>
    <!--        学生打分表单-->
    <div class="scoreStuDialog">
      <el-dialog
          @close="clearScoreForm"
          title="学生打分"
          :visible.sync="scoreFormVisible">

        <el-form :model="scoreStuForm" :rules="scoreRules" ref="scoreStuFormRef">

          <el-form-item label="学号" label-width="150px" prop="stuId">
            <el-input v-model="scoreStuForm.stuId" disabled></el-input>
          </el-form-item>

          <el-form-item label="学生姓名" label-width="150px" prop="stuName">
            <el-input v-model="scoreStuForm.stuName" disabled></el-input>
          </el-form-item>

          <el-form-item label="课程" label-width="150px" prop="courseName">
            <el-select v-model="scoreStuForm.courseName" clearable placeholder="请选择课程" @change="getScore">
              <el-option
                  v-for="item in courseNames"
                  :key="item.courseId"
                  :label="item.courseName"
                  :value="item.courseName">
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="分数" label-width="150px" prop="score">
            <el-input v-model="scoreStuForm.score" clearable placeholder="请给予分数" type="number"></el-input>
          </el-form-item>

        </el-form>

        <div slot="footer" class="dialog-footer">
          <el-button @click="cancelScore">取 消</el-button>
          <el-button type="primary" @click="scoreStu">确 定</el-button>
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
  name: "StuManage",
  data() {
    let nowTime = new Date()
    let nowYear = nowTime.getFullYear();
    let maxYear = nowYear - 1//默认是当前年-1
    let startTime = new Date(nowYear + '-9-01 00:00:00')//今年9月份开学时间
    if (nowTime > startTime) {
      maxYear += 1
    }
    ;
    let checkStuIdCard = (rule, value, callback) => {
      let reg = /^\d{6}(18|19|20)?\d{2}(0[1-9]|1[012])(0[1-9]|[12]\d|3[01])\d{3}(\d|[xX])$/;
      if (!reg.test(value)) {
        return callback(new Error("身份证格式错误"));
      }
      callback();
    };
    let checkScore = (rule, value, callback) => {
      if (value < 0) {
        return callback(new Error("分数范围在0到100之间"));
      } else if (value > 100) {
        return callback(new Error("分数范围在0到100之间"));
      }
      callback();
    };
    return {
      addStuForm: {
        admissionYear: '',
        majorName: '',
        classId: '',
        stuName: '',
        stuIdCard: '',
      },
      editStuForm: {
        stuId: '',
        admissionYear: '',
        majorName: '',//改
        classNo: '',//改
        stuName: '',//改
        stuIdCard: '',
      },
      scoreStuForm: {
        score: '',
        courseName: ''
      },
      courseNames: [],

      pageNum: 1,
      pageSize: 100,
      stuId: '',
      stuName: '',
      className: '',
      total: 0,
      addFormVisible: false,
      editFormVisible: false,
      scoreFormVisible: false,
      stuList: [],
      admissionYears: [{value: '1', label: maxYear}, {value: '2', label: maxYear - 1},
        {value: '3', label: maxYear - 2}, {value: '4', label: maxYear - 3}],
      majorNames: [],
      classNos: [],
      addRules: {
        admissionYear: {required: true, message: "请选择入学年份", trigger: "blur"}, //非空验证,
        majorName: {required: true, message: "请选择专业", trigger: "blur"}, //非空验证
        classId: {required: true, message: "请选择班号", trigger: "blur"}, //非空验证
        stuName: [
          {required: true, message: "请输入学生姓名", trigger: "blur"}, //非空验证
          {min: 2, max: 20, message: "姓名长度为2到20个字符", trigger: "blur",}, //规则验证
        ],
        stuIdCard: [
          {required: true, message: "请输入身份证", trigger: "blur"}, //非空验证
          {validator: checkStuIdCard, trigger: "blur"},
        ]
      },
      editRules: {
        // majorName: {required: true, message: "请选择专业", trigger: "blur"},
        // classId: {required: true, message: "请选择班号", trigger: "blur"},
        stuName: [
          {required: true, message: "请输入学生姓名", trigger: "blur"},
          {min: 2, max: 20, message: "姓名长度为2到20个字符", trigger: "blur"},
        ]
      },
      scoreRules: {
        courseName: {required: true, message: "请选择课程", trigger: "blur"},
        score: [{required: true, message: "请给予分数", trigger: "blur"},
          {validator: checkScore, trigger: "blur"}]
      },
    }
  },
  created() {
    this.getStuList();
  },
  methods: {
    scoreStu() {
      this.$refs.scoreStuFormRef.validate((valid) => {
        if (valid) {//如果表单的填写合法,发送axios请求
          axios.put(service.baseURL+"/score/giveNewScore", this.scoreStuForm,
              {
                headers: {
                  Authorization: this.$store.state.token
                }
              })
              .then((resp) => {
                if (resp.data.code == '20000') {
                  this.scoreFormVisible = false;//打分成功关闭表单
                  this.clearScoreForm()
                  this.getStuList();//刷新表格
                  this.$message.success(resp.data.msg);
                } else {
                  this.$message.error(resp.data.msg)
                }
              })
        } else {//表单验证失败
          return false;
        }
      })
    },
    getScore() {
      axios.get(service.baseURL+"/score/getScoreByCourseName?courseName=" + this.scoreStuForm.courseName +
          "&stuId=" + this.scoreStuForm.stuId, {
        headers: {
          Authorization: this.$store.state.token
        }
      }).then((resp) => {
        if (resp.data.code == '20000') {
          if (resp.data.data != null) {
            this.$message.info(this.scoreStuForm.courseName + "成绩为" + resp.data.data)
          }
        }
      })
    },
    add_Year(stuForm) {//新增学生时改专业重新选班级
      // setTimeout(()=>{
      //   this.addStuForm.classId=''
      // }, 100)
      this.$set(this.addStuForm,'classId','')
      if ((stuForm.majorName!=""&&stuForm.majorName!=undefined)&&(stuForm.admissionYear!=""&&stuForm.admissionYear!=undefined))
      this.getClasses(stuForm)
    },
    add_Major(stuForm) {
      // setTimeout(()=>{
      //   this.addStuForm.classId=''
      // }, 100)
      this.$set(this.addStuForm,'classId','')
      if ((stuForm.majorName!=""&&stuForm.majorName!=undefined)&&(stuForm.admissionYear!=""&&stuForm.admissionYear!=undefined))
      this.getClasses(stuForm)
    },
    getClasses(stuForm) {
      axios.get(service.baseURL+"/class/getClassesByMajorName?majorName=" + stuForm.majorName + "&classYear=" + stuForm.admissionYear,
          {
            headers: {
              Authorization: this.$store.state.token
            }
          })
          .then((resp) => {
            if (resp.code = "20000") {
              this.classNos = resp.data.data
            }
          })
    },
    resetStuList() {
      location.reload();
    },
    searchStuList() {
      this.pageNum = 1
      this.getStuList();
    },
    cancelEdit() {
      this.editFormVisible = false
      this.clearAddForm()
      this.$message.info("已取消编辑")
    },
    cancelAdd() {
      this.addFormVisible = false
      this.classNos=[]
      this.clearAddForm()
      this.$set(this.addStuForm,'classId','')
      this.$message.info("已取消新增")
    },
    cancelScore() {
      this.scoreFormVisible = false
      this.clearScoreForm()
      this.$message.info("已取消打分")
    },
    delStu(row) {
      this.$confirm(`确认删除学生：${row.stuName} ？`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      }).then(() => {
        axios.delete(service.baseURL+"/student/delete?stuId=" + row.stuId,
            {
              headers: {
                Authorization: this.$store.state.token
              }
            })
            .then((resp) => {
              if (resp.data.code == '20000') {
                this.getStuList();
                this.$message.success(resp.data.msg)
              } else {
                this.$message.error(resp.data.msg)
              }
            })
      }).catch(() => {
        this.$message({
          type: "info",
          message: "已取消删除",
        })
      })
    },
    openStuScore(row) {
      axios.get(service.baseURL+"/student/getByStuId?stuId=" + row.stuId,
          {
            headers: {
              Authorization: this.$store.state.token
            }
          })
          .then((resp) => {
            if (resp.data.code == '20000') {
              this.scoreFormVisible = true;
              this.scoreStuForm = resp.data.data;
              axios.get(service.baseURL+"/chooseCourse/getCanScoreCourse?stuId=" + row.stuId,
                  {
                    headers: {
                      Authorization: this.$store.state.token
                    }
                  })
                  .then((resp) => {
                    this.courseNames = resp.data.data;
                  })
            } else {
              this.$message.error(resp.data.msg)
            }
          })
    },
    openEditStu(row) {
      axios.get(service.baseURL+"/student/getByStuId?stuId=" + row.stuId,
          {
            headers: {
              Authorization: this.$store.state.token
            }
          })
          .then((resp) => {
            if (resp.data.code == '20000') {
              this.editFormVisible = true;
              this.editStuForm = resp.data.data//不能换专业！
              //   axios.get("http://localhost:8091/major/getAllMajorName",
              //       {
              //         headers: {
              //           Authorization: this.$store.state.token
              //         }
              //       })
              //       .then((resp) => {
              //         this.majorNames = resp.data.data;
              //         this.getClasses(this.editStuForm)
              //       })
              // } else {
              //   this.$message.error(resp.data.msg)
            }
          })
    },
    editStu() {
      this.$refs.editStuFormRef.validate((valid) => {
        if (valid) {//如果表单的填写合法,发送axios请求
          axios.put(service.baseURL+"/student/editByStuId", this.editStuForm,
              {
                headers: {
                  Authorization: this.$store.state.token
                }
              })
              .then((resp) => {
                if (resp.data.code == '20000') {
                  this.editFormVisible = false;//编辑成功关闭表单
                  this.clearEditForm()
                  this.getStuList();//刷新表格
                  this.$message.success(resp.data.msg);
                } else {
                  this.$message.error(resp.data.msg)
                }
              })
        } else {//表单验证失败
          return false;
        }
      })
    },
    openAddStu() {
      this.addFormVisible = true
      axios.get(service.baseURL+"/major/getAllMajorName",
          {
            headers: {
              Authorization: this.$store.state.token
            }
          })
          .then((resp) => {
            this.majorNames = resp.data.data;
          })
    },
    getStuList() {
      axios.get(service.baseURL+"/student/stuPageList?pageNum=" + this.pageNum +
          "&pageSize=" + this.pageSize + "&stuId=" + this.stuId +
          "&stuName=" + this.stuName + "&className=" + this.className,
          {
            headers: {
              Authorization: this.$store.state.token
            }
          }
      ).then((resp) => {
        if (resp.data.code == '20000') {
          this.stuList = resp.data.data.stuList
          this.total = resp.data.data.total
        } else {
          this.$message.error(resp.data.msg)
        }
      })
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.getStuList()
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.pageNum = 1
      this.getStuList()
    },
    clearAddForm() {//每次打开弹框应该清空数据
      this.addStuForm = {}
      // setTimeout(()=>{
      //   this.addStuForm.classId=''
      // }, 100)
      this.$set(this.addStuForm,'classId','')
      this.$refs.addStuFormRef.clearValidate()
    },
    clearEditForm() {
      this.editStuForm = {}
      this.$refs.editStuFormRef.clearValidate()
    },
    clearScoreForm() {
      this.scoreStuForm = {}
      this.$refs.scoreStuFormRef.clearValidate()
    },
    addStu() {//添加学生表单确定触发
      console.log(this.addStuForm.classId)
      this.$refs.addStuFormRef.validate((valid) => {
        if (valid) {//如果表单的填写合法,发送axios请求
          axios.post(service.baseURL+"/student/add", {
                'admissionYear': this.addStuForm.admissionYear,
                'majorName': this.addStuForm.majorName,
                'classId': this.addStuForm.classId,
                'stuName': this.addStuForm.stuName,
                'stuIdCard': this.addStuForm.stuIdCard,
              },
              {
                headers: {
                  Authorization: this.$store.state.token
                }
              }
          ).then((resp) => {
            if (resp.data.code == '20000') {
              this.addFormVisible = false;//添加成功关闭表单
              this.clearAddForm()
              this.getStuList();//刷新表格
              this.$message.success(resp.data.msg);
            } else {
              this.$message.error(resp.data.msg)
            }
          })
        } else {//表单验证失败
          return false;
        }
      })
    }
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