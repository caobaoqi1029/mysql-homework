<template>
  <div>
    <div class="findAdd">
      <el-input autocomplete="off" placeholder="请输入专业名称" v-model="majorName" clearable
                style="width: 300px"></el-input>
      <el-button type="primary" @click="searchMajorList" icon="el-icon-search">搜索</el-button>
      <el-button @click="resetMajorList" icon="el-icon-refresh" style="margin-left: 0">刷新</el-button>
      <el-button style="margin-left: 560px" type="primary" @click="openMajorForm(null)" icon="el-icon-plus">新增专业
      </el-button>
    </div>
    <div class="majorTable">
      <el-table :data="majorList" stripe size="small" style="width: 100%">
        <el-table-column label="序号" width="80" align="center">
          <template slot-scope="scope">
            {{
              (pageNum - 1) * pageSize + scope.$index + 1
            }}
          </template>
        </el-table-column>
        <el-table-column prop="majorId" label="专业编号" align="center" width="150" sortable></el-table-column>
        <el-table-column prop="majorName" label="专业名称" align="center" width="200" sortable></el-table-column>
        <el-table-column prop="classTotal" label="班级数量" align="center" width="150" sortable></el-table-column>
        <el-table-column prop="courseTotal" label="课程数量" align="center" width="150" sortable></el-table-column>
        <el-table-column prop="stuTotal" label="学生数量" align="center" width="150" sortable></el-table-column>
        <el-table-column prop="teaTotal" label="班主任数量" align="center" width="150" sortable></el-table-column>
        <el-table-column prop="handle" label="操作" align="center">
          <template slot-scope="scope">
            <el-button @click="openMajorForm(scope.row)" type="warning" size="small">编辑</el-button>
            <el-button @click="delMajor(scope.row)" type="danger" size="small">删除</el-button>
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
    <!-- 添加或编辑专业表单-->
    <div class="MajorDialog">
      <el-dialog
          @close="clearForm"
          :title="title"
          :visible.sync="formVisible">
        <el-form :model="majorForm" :rules="rules" ref="majorFormRef">
          <el-form-item label="专业名" label-width="150px" prop="majorName">
            <el-input v-model="majorForm.majorName" autocomplete="off" placeholder="请输入专业名" clearable></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="cancelForm(title)">取 消</el-button>
          <el-button type="primary" @click="saveMajor(title)">确 定</el-button>
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
  name: "MajorManage",
  data() {
    return {
      majorName: '',
      majorList: [{
        majorId: '',
        majorName: '',
        classTotal: 0,
        courseTotal: 0,
      }],
      title: '',
      pageNum: 1,
      pageSize: 10,
      total: 0,
      formVisible: false,
      majorForm: {
        majorId: '',
        majorName: '',
      },
      rules: {
        majorName: [{required: true, message: "请输入专业", trigger: "blur"},//非空验证,
          {min: 2, max: 20, message: "专业名长度为2到20个字符", trigger: "blur"}],
      },
    }
  },
  methods: {
    getMajorList() {
      axios.get(service.baseURL + "/major/majorPageList?pageNum=" + this.pageNum +
          "&pageSize=" + this.pageSize + "&majorName=" + this.majorName, {
        headers: {
          Authorization: this.$store.state.token
        }
      }).then((resp) => {
        if (resp.data.code == '20000') {
          this.majorList = resp.data.data.majorList
          this.total = resp.data.data.total
        } else {
          this.$message.error(resp.data.msg)
        }
      })
    },
    searchMajorList() {
      this.pageNum = 1
      this.getMajorList();
    }
    ,
    resetMajorList() {
      location.reload()
    },
    openMajorForm(row) {
      if (row == null) {
        this.formVisible = true
        this.title = '新增专业'
      } else {
        if (row.classTotal > 0 || row.courseTotal > 0) {
          this.$message.info("该专业还有班级或课程，无法编辑")
        } else {
          this.formVisible = true
          this.title = '编辑专业'
          axios.get(service.baseURL + "/major/getByMajorId?majorId=" + row.majorId, {
            headers: {
              Authorization: this.$store.state.token
            }
          })
              .then((resp) => {
                if (resp.data.code == '20000') {
                  this.majorForm = resp.data.data
                } else {
                  this.$message.error(resp.data.msg)
                }
              })
        }
      }
    },
    delMajor(row) {
      if (row.classTotal > 0 || row.courseTotal > 0) {
        this.$message.info("该专业还有班级或课程无法，无法删除")
      } else
        this.$confirm(`确认删除专业：${row.majorName} ？`, "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
          center: true,
        }).then(() => {
          axios.delete(service.baseURL + "/major/delById?majorId=" + row.majorId, {
            headers: {
              Authorization: this.$store.state.token
            }
          })
              .then((resp) => {
                if (resp.data.code == '20000') {
                  this.getMajorList();
                  this.$message.success(resp.data.msg)
                } else {
                  this.$message.error(resp.data.msg)
                }
              })
        }).catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.getMajorList()
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.pageNum = 1
      this.getMajorList()
    },
    clearForm() {
      this.majorForm = {}
      this.$refs.majorFormRef.clearValidate()
    },
    cancelForm(title) {
      this.formVisible = false
      this.clearForm()
      if (title == '新增专业') {
        this.$message.info("已取消新增")
      } else if (title == '编辑专业') {
        this.$message.info("已取消编辑")
      }
    },
    saveMajor(title) {
      this.$refs.majorFormRef.validate((valid) => {
        if (valid && title == '新增专业') {//如果表单的填写合法,发送axios请求
          axios.post(service.baseURL + "/major/add", {
                'majorName': this.majorForm.majorName,
              }, {
                headers: {
                  Authorization: this.$store.state.token
                }
              }
          ).then((resp) => {
            if (resp.data.code == '20000') {
              this.formVisible = false;//添加成功关闭表单
              this.clearForm()
              this.getMajorList();//刷新表格
              this.$message.success(resp.data.msg);
            } else {
              this.$message.error(resp.data.msg)
            }
          })
        } else if (valid && title == '编辑专业') {
          axios.put(service.baseURL + "/major/edit", {
            'majorName': this.majorForm.majorName,
            'majorId': this.majorForm.majorId
          }, {
            headers: {
              Authorization: this.$store.state.token
            }
          }).then((resp) => {
            if (resp.data.code == '20000') {
              this.formVisible = false;//添加成功关闭表单
              this.clearForm()
              this.getMajorList();//刷新表格
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
    this.getMajorList();
  }
}
</script>

<style scoped>
.findAdd {
  margin: 20px auto;
}

.el-input, .el-button {
  margin-right: 20px;
}
</style>