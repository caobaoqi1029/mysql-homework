<template>
  <div>
    <div class="findAdd">
      <el-input autocomplete="off" placeholder="请输入课程名" v-model="courseName" clearable
                style="width: 240px"></el-input>
      <el-input autocomplete="off" placeholder="请输入专业名" v-model="majorName" clearable
                style="width: 240px"></el-input>
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
      <el-button type="primary" @click="openAddCourseForm()" icon="el-icon-plus" style="margin-left: 70px">新增课程
      </el-button>
    </div>
    <div class="classTable">
      <el-table :data="courseList" stripe size="small" style="width: 100%" :row-style="{ height:'40px'}">
        <el-table-column label="序号" width="80" align="center">
          <template slot-scope="scope">
            {{
              (pageNum - 1) * pageSize + scope.$index + 1
            }}
          </template>
        </el-table-column>
        <el-table-column prop="courseId" label="课程编号" align="center" width="130" sortable></el-table-column>
        <el-table-column prop="courseName" label="课程名" align="center" width="150" sortable></el-table-column>
        <el-table-column prop="majorName" label="所属专业" align="center" width="200" sortable></el-table-column>
        <el-table-column prop="ifDegree" label="课程类型" width="140" align="center" sortable>
          <template slot-scope="scope">
            <el-tag v-if="scope.row.ifDegree == 1">必修</el-tag>
            <el-tag v-else-if="scope.row.ifDegree == 0" type="success">选修</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="coursePeriod" label="开课时期" align="center" width="150" sortable></el-table-column>
        <el-table-column prop="stuChooseNum" label="选课人数" align="center" width="100" sortable></el-table-column>
        <el-table-column prop="handle" label="操作" align="center">
          <template slot-scope="scope">
            <el-button @click="openEditCourseForm(scope.row)" type="warning" size="small">编辑</el-button>
            <el-button @click="delCourse(scope.row)" type="danger" size="small">删除</el-button>
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
    <!-- 添加课程表单-->
    <div class="addCourseDialog">
      <el-dialog
          @close="clearAddForm"
          title="新增课程"
          :visible.sync="addFormVisible">
        <el-form :model="addCourseForm" :rules="addRules" ref="addFormRef">
          <el-form-item label="专业" label-width="150px" prop="majorName">
            <el-select v-model="addCourseForm.majorName" placeholder="请选择专业" clearable>
              <el-option
                  v-for="item in majorNames"
                  :key="item.majorId"
                  :label="item.majorName"
                  :value="item.majorName">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="开课时期" label-width="150px" prop="coursePeriod">
            <el-select v-model="addCourseForm.coursePeriod" placeholder="请选择开课时期" clearable>
              <el-option
                  v-for="item in coursePeriods"
                  :key="item.value"
                  :label="item.label"
                  :value="item.label">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="课程名" label-width="150px" prop="courseName">
            <el-input v-model="addCourseForm.courseName" autocomplete="off" placeholder="请输入课程名"
                      clearable></el-input>
          </el-form-item>
          <el-form-item label="是否必修" label-width="150px" prop="ifDegree">
            <el-switch v-model="addCourseForm.ifDegree" :active-value="1" :inactive-value="0"></el-switch>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="cancelAddForm()">取 消</el-button>
          <el-button type="primary" @click="addCourse()">确 定</el-button>
        </div>
      </el-dialog>
    </div>
    <!-- 编辑课程表单-->
    <div class="editCourseDialog">
      <el-dialog
          @close="clearEditForm"
          title="编辑课程"
          :visible.sync="editFormVisible">
        <el-form :model="editCourseForm" :rules="editRules" ref="editFormRef">
          <el-form-item label="课程编号" label-width="150px" prop="courseId">
            <el-input v-model="editCourseForm.courseId" autocomplete="off" disabled></el-input>
          </el-form-item>
          <el-form-item label="专业" label-width="150px" prop="majorName">
            <el-input v-model="editCourseForm.majorName" autocomplete="off" placeholder="请选择专业"
                      disabled></el-input>
          </el-form-item>
          <el-form-item label="开课时期" label-width="150px" prop="coursePeriod">
            <el-select v-model="editCourseForm.coursePeriod" placeholder="请选择开课时期" clearable>
              <el-option
                  v-for="item in coursePeriods"
                  :key="item.value"
                  :label="item.label"
                  :value="item.label">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="课程名" label-width="150px" prop="courseName">
            <el-input v-model="editCourseForm.courseName" autocomplete="off" placeholder="请输入课程名"
                      clearable></el-input>
          </el-form-item>
          <el-form-item label="是否必修" label-width="150px" prop="ifDegree">
            <el-switch v-model="editCourseForm.ifDegree" :active-value="'1'" :inactive-value="'0'"></el-switch>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="cancelEditForm()">取 消</el-button>
          <el-button type="primary" @click="editCourse()">确 定</el-button>
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
  name: "CourseManage",
  data() {
    return {
      coursePeriod: '',
      courseName: '',
      majorName: '',
      pageNum: 1,
      pageSize: 50,
      total: 0,
      addFormVisible: false,
      editFormVisible: false,
      courseList: [{
        courseId: '',
        courseName: '',
        majorName: '',
        coursePeriod: '',
        stuChooseNum: 0,
        ifDegree: '',
      }],
      addCourseForm: {},
      editCourseForm: {},
      addRules: {
        majorName: {required: true, message: "请选择专业", trigger: "blur"},//非空验证,
        coursePeriod: {required: true, message: "请选择开课时期", trigger: "blur"},
        courseName: [{required: true, message: "请输入课程名", trigger: "blur"},
          {min: 2, max: 20, message: "课程长度为2到20个字符", trigger: "blur"}],
      },
      editRules: {
        coursePeriod: {required: true, message: "请选择开课时期", trigger: "blur"},
        courseName: [{required: true, message: "请输入课程名", trigger: "blur"},
          {min: 2, max: 20, message: "课程名长度为2到20个字符", trigger: "blur"}],
      },
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
      majorNames: []
    }
  },
  methods: {
    getCourseList() {
      axios.get(service.baseURL + "/course/coursePageList?pageNum=" + this.pageNum +
          "&pageSize=" + this.pageSize + "&courseName=" + this.courseName + "&majorName=" +
          this.majorName + "&coursePeriod=" + this.coursePeriod, {
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
    searchCourseList() {
      this.pageNum = 1
      this.getCourseList();
    },
    resetCourseList() {
      location.reload()
    },
    openAddCourseForm() {
      this.addFormVisible = true
      axios.get(service.baseURL + "/major/getAllMajorName", {
        headers: {
          Authorization: this.$store.state.token
        }
      }).then((resp) => {
        this.majorNames = resp.data.data;
      })
    },
    openEditCourseForm(row) {
      if (row.stuChooseNum > 0) {
        this.$message.info("还有学生有该课,无法编辑")
      } else
        axios.get(service.baseURL + "/course/getCourseById?courseId=" + row.courseId, {
          headers: {
            Authorization: this.$store.state.token
          }
        }).then((resp) => {
          if (resp.data.code = "20000") {
            this.editFormVisible = true
            // axios.get("http://localhost:8091/major/getAllMajorName", {//课程不能换专业
            //   headers: {
            //     Authorization: this.$store.state.token
            //   }
            // })//获取所有专业
            //     .then((resp) => {
            //       this.majorNames = resp.data.data;
            //     })
            this.editCourseForm = resp.data.data//渲染编辑表单
          } else {
            this.$message.error(resp.data.msg)
          }
        })
    },
    delCourse(row) {
      if (row.stuChooseNum > 0) {
        this.$message.info("还有学生有该课，无法删除")
      } else
        this.$confirm(`确认删除课程：${row.courseName} ？`, "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
          center: true,
        }).then(() => {
          axios.delete(service.baseURL + "/course/delCourseById?courseId=" + row.courseId, {
            headers: {
              Authorization: this.$store.state.token
            }
          })
              .then((resp) => {
                if (resp.data.code == '20000') {
                  this.getCourseList()
                  this.$message.success(resp.data.msg)
                } else {
                  this.$message.error(resp.data.msg)
                }
              })
        }).catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          })
        });
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.getCourseList()
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.pageNum = 1
      this.getCourseList()
    },
    clearAddForm() {
      this.addCourseForm = {}
      this.$refs.addFormRef.clearValidate()
    },
    clearEditForm() {
      this.addCourseForm = {}
      this.$refs.editFormRef.clearValidate()
    },

    cancelAddForm() {
      this.addFormVisible = false
      this.$message.info("已取消新增")
    },
    cancelEditForm() {
      this.editFormVisible = false
      this.$message.info("已取消编辑")
    },
    addCourse() {
      this.$refs.addFormRef.validate((valid) => {
        if (valid) {//如果表单的填写合法,发送axios请求
          axios.post(service.baseURL + "/course/add", {
                'coursePeriod': this.addCourseForm.coursePeriod,
                'majorName': this.addCourseForm.majorName,
                'courseName': this.addCourseForm.courseName,
                'ifDegree': this.addCourseForm.ifDegree
              }, {
                headers: {
                  Authorization: this.$store.state.token
                }
              }
          ).then((resp) => {
            if (resp.data.code == '20000') {
              this.addFormVisible = false;//添加成功关闭表单
              this.clearAddForm()
              this.getCourseList();//刷新表格
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
    editCourse() {
      this.$refs.editFormRef.validate((valid) => {
        if (valid) {//如果表单的填写合法,发送axios请求
          axios.put(service.baseURL + "/course/edit", {
                'courseId': this.editCourseForm.courseId,
                'coursePeriod': this.editCourseForm.coursePeriod,
                'majorName': this.editCourseForm.majorName,
                'courseName': this.editCourseForm.courseName,
                'ifDegree': this.editCourseForm.ifDegree
              }, {
                headers: {
                  Authorization: this.$store.state.token
                }
              }
          ).then((resp) => {
            if (resp.data.code == '20000') {
              this.editFormVisible = false;//编辑成功关闭表单
              this.clearEditForm()
              this.getCourseList();//刷新表格
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
  },
  created() {
    this.getCourseList()
  }
}
</script>

<style scoped>
.findAdd {
  margin: 20px auto;
}

.el-input, .el-button, .el-select {
  margin-right: 20px;
}
</style>