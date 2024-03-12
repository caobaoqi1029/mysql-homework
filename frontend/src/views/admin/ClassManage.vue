<template>
  <div>
    <div class="findAdd">
      <el-input autocomplete="off" placeholder="请输入班级名称" v-model="className" clearable
                style="width: 300px"></el-input>
      <el-input autocomplete="off" placeholder="请输入班主任姓名" v-model="teaName" clearable
                style="width: 300px"></el-input>
      <el-button type="primary" @click="searchClassList" icon="el-icon-search">搜索</el-button>
      <el-button @click="resetClassList" icon="el-icon-refresh" style="margin-left: 0">刷新</el-button>
      <el-button style="margin-left: 240px" type="primary" @click="openClassForm()" icon="el-icon-plus">新增班级
      </el-button>
    </div>
    <div class="classTable">
      <el-table :data="classList" stripe size="small" style="width: 100%">
        <el-table-column label="序号" width="80" align="center">
          <template slot-scope="scope">
            {{
              (pageNum - 1) * pageSize + scope.$index + 1
            }}
          </template>
        </el-table-column>
        <el-table-column prop="classId" label="班级编号" align="center" width="150" sortable></el-table-column>
        <el-table-column prop="className" label="班级名称" align="center" width="200" sortable></el-table-column>
        <el-table-column prop="majorName" label="所属专业" align="center" width="200" sortable></el-table-column>
        <el-table-column prop="classYear" label="年级" align="center" width="100" sortable></el-table-column>
        <el-table-column prop="stuTotal" label="学生数量" align="center" width="150" sortable></el-table-column>
        <el-table-column prop="teaName" label="班主任姓名" align="center" width="150" sortable></el-table-column>
        <el-table-column prop="handle" label="操作" align="center">
          <template slot-scope="scope">
            <el-button @click="delClass(scope.row)" type="danger" size="small">删除</el-button>
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
    <!-- 添加班级表单-->
    <div class="classDialog">
      <el-dialog
          @close="clearForm"
          title="新增班级"
          :visible.sync="formVisible">
        <el-form :model="classForm" :rules="rules" ref="classFormRef">
          <el-form-item label="年级" label-width="150px" prop="classYear">
            <el-select v-model="classForm.classYear" placeholder="请选择年级" clearable>
              <el-option
                  v-for="item in classYears"
                  :key="item.value"
                  :label="item.label"
                  :value="item.label">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="专业" label-width="150px" prop="majorName">
            <el-select v-model="classForm.majorName" placeholder="请选择专业" clearable>
              <el-option
                  v-for="item in majorNames"
                  :key="item.majorId"
                  :label="item.majorName"
                  :value="item.majorName">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="cancelForm()">取 消</el-button>
          <el-button type="primary" @click="saveClass()">确 定</el-button>
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
  name: "ClassManage",
  data() {
    let nowTime = new Date()
    let nowYear = nowTime.getFullYear();
    let maxYear = nowYear - 1//默认是当前年-1
    let startTime = new Date(nowYear + '-9-01 00:00:00')//今年9月份开学时间
    if (nowTime > startTime) {
      maxYear += 1
    }
    return {
      className: '',
      teaName: '',
      classList: [{
        classId: '',
        className: '',
        majorName: '',
        stuTotal: 0,
        classYear: ''
      }],
      pageNum: 1,
      pageSize: 20,
      total: 0,
      formVisible: false,
      classForm: {
        majorName: '',
        classYear: ''
      },
      rules: {
        classYear: {required: true, message: "请选择年级", trigger: "blur"},
        majorName: {required: true, message: "请选择专业", trigger: "blur"}
      },
      majorNames: [],
      classYears: [{value: '1', label: maxYear}, {value: '2', label: maxYear - 1},
        {value: '3', label: maxYear - 2}, {value: '4', label: maxYear - 3}],
    }
  },
  methods: {
    getClassList() {
      axios.get(service.baseURL+"/class/classPageList?pageNum=" + this.pageNum +
          "&pageSize=" + this.pageSize + "&className=" + this.className + "&teaName=" + this.teaName, {
        headers: {
          Authorization: this.$store.state.token
        }
      })
          .then((resp) => {
            if (resp.data.code == '20000') {
              this.classList = resp.data.data.classList
              this.total = resp.data.data.total
            } else {
              this.$message.error(resp.data.msg)
            }
          })
    },
    searchClassList() {
      this.pageNum = 1
      this.getClassList();
    },
    resetClassList() {
      location.reload()
    },
    openClassForm() {
      this.formVisible = true
      axios.get(service.baseURL+"/major/getAllMajorName", {
        headers: {
          Authorization: this.$store.state.token
        }
      }).then((resp) => {
        this.majorNames = resp.data.data;
      })
    },
    delClass(row) {
      //有班主任，无法删除
      if (row.stuTotal > 0) {
        this.$message.info("该班级还有学生，无法删除")
      }
      if (row.teaName != '' && row.teaName != null) {
        this.$message.info("该班级还有班主任，无法删除")
      } else
        this.$confirm(`确认删除班级：${row.className} ？`, "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
          center: true,
        }).then(() => {
          axios.delete(service.baseURL+"/class/delClassById?classId=" + row.classId, {
            headers: {
              Authorization: this.$store.state.token
            }
          }).then((resp) => {
            if (resp.data.code == '20000') {
              this.$message.success(resp.data.msg)
              this.getClassList();
            } else {
              this.$message.error(resp.data.msg)
            }
          })
        }).catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          })
        });
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.getClassList()
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.pageNum = 1
      this.getClassList()
    },
    clearForm() {
      this.classForm = {}
      this.$refs.classFormRef.clearValidate()
    },
    cancelForm() {
      this.formVisible = false
      this.clearForm()
      this.$message.info("已取消新增")
    },
    saveClass() {
      this.$refs.classFormRef.validate((valid) => {
        if (valid) {//如果表单的填写合法,发送axios请求
          axios.post(service.baseURL+"/class/add", {
                'majorName': this.classForm.majorName,
                'classYear': this.classForm.classYear
              }, {
                headers: {
                  Authorization: this.$store.state.token
                }
              }
          ).then((resp) => {
            if (resp.data.code == '20000') {
              this.formVisible = false
              this.clearForm()
              this.getClassList()
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
    this.getClassList();
  }
}
</script>

<style scoped>
.findAdd {
  margin: 20px auto;
}

.el-input,.el-button{
  margin-right: 20px;
}
</style>