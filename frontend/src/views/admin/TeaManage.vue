<template>
  <div>
    <div class="findAdd">
      <el-input autocomplete="off" placeholder="请输入班主任编号" v-model="teaId" clearable
                style="width: 240px"></el-input>
      <el-input autocomplete="off" placeholder="请输入班主任姓名" v-model="teaName" clearable
                style="width: 240px"></el-input>
      <el-input autocomplete="off" placeholder="请输入班级名称" v-model="className" clearable
                style="width: 240px"></el-input>
      <el-button type="primary" @click="searchTeaList" icon="el-icon-search">搜索</el-button>
      <el-button @click="resetTeaList" icon="el-icon-refresh" style="margin-left:0">刷新</el-button>
      <el-button style="margin-left: 40px" type="primary" @click="openAddTea" icon="el-icon-plus">新增班主任</el-button>
    </div>
    <div class="teaTable">
      <el-table :data="teaList" stripe size="small" style="width: 100%">
        <el-table-column label="序号" width="80" align="center">
          <template slot-scope="scope">
            {{
              (pageNum - 1) * pageSize + scope.$index + 1
            }}
          </template>
        </el-table-column>
        <el-table-column prop="teaId" label="班主任编号" sortable align="center" width="150"></el-table-column>
        <el-table-column prop="teaName" label="班主任姓名" align="center" sortable width="150"></el-table-column>
        <el-table-column prop="majorName" sortable align="center" width="200" label="专业名称"></el-table-column>
        <el-table-column prop="className" align="center" width="200" label="班级名称" sortable></el-table-column>
        <el-table-column prop="classYear" align="center" width="150" label="年级" sortable></el-table-column>
        <el-table-column prop="handle" align="center" label="操作">
          <template slot-scope="scope">
            <el-button @click="openEditTea(scope.row)" type="warning" size="small">编辑</el-button>
            <el-button @click="delTea(scope.row)" type="danger" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="page">
      <el-pagination style="text-align: center" @current-change="handleCurrentChange"
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
    <!--添加班主任表单-->
    <div class="addTeaDialog">
      <el-dialog
          @close="clearAddForm"
          title="新增班主任"
          :visible.sync="addFormVisible">
        <el-form :model="addTeaForm" :rules="addRules" ref="addTeaFormRef">
          <el-form-item label="年级" label-width="150px" prop="classYear">
            <el-select v-model="addTeaForm.classYear" placeholder="请选择年级" clearable
                       @change="getClasses(addTeaForm)">
              <el-option
                  v-for="item in classYears"
                  :key="item.value"
                  :label="item.label"
                  :value="item.label">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="专业" label-width="150px" prop="majorName">
            <el-select v-model="addTeaForm.majorName" placeholder="请选择专业" clearable
                       @change="getClasses(addTeaForm)">
              <el-option
                  v-for="item in majorNames"
                  :key="item.majorId"
                  :label="item.majorName"
                  :value="item.majorName">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="班号" label-width="150px" prop="classId">
            <el-select v-model="addTeaForm.classId" placeholder="请选择班号" clearable id="selectClassId">
              <el-option
                  v-for="item in classNos"
                  :key="item.classId"
                  :label="item.classId.substring(6,7)"
                  :value="item.classId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="班主任姓名" label-width="150px" prop="teaName">
            <el-input v-model="addTeaForm.teaName" autocomplete="off" placeholder="请输入班主任姓名"
                      clearable></el-input>
          </el-form-item>
          <el-form-item label="身份证" label-width="150px" prop="teaIdCard">
            <el-input v-model="addTeaForm.teaIdCard" autocomplete="off" placeholder="请输入身份证" clearable></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="cancelAdd">取 消</el-button>
          <el-button type="primary" @click="addTea">确 定</el-button>
        </div>
      </el-dialog>

    </div>
    <!--编辑班主任表单-->
    <div class="editStuDialog">
      <el-dialog
          @close="clearEditForm"
          title="编辑班主任"
          :visible.sync="editFormVisible">
        <el-form :model="editTeaForm" :rules="editRules" ref="editTeaFormRef">
          <el-form-item label="年级" label-width="150px" prop="classYear" disabled>
            <el-input v-model="editTeaForm.classYear" autocomplete="off" placeholder="请输入专业" disabled></el-input>
          </el-form-item>
          <el-form-item label="专业" label-width="150px" prop="majorName">
            <el-input v-model="editTeaForm.majorName" autocomplete="off" placeholder="请输入专业" disabled></el-input>
          </el-form-item>
          <el-form-item label="班号" label-width="150px" prop="classNo">
            <el-input v-model="editTeaForm.classNo" autocomplete="off" placeholder="请输入班号" disabled></el-input>
          </el-form-item>
          <el-form-item label="班主任姓名" label-width="150px" prop="teaName">
            <el-input v-model="editTeaForm.teaName" autocomplete="off" placeholder="请输入班主任姓名"
                      clearable></el-input>
          </el-form-item>
          <el-form-item label="身份证" label-width="150px" prop="teaIdCard">
            <el-input v-model="editTeaForm.teaIdCard" autocomplete="off" placeholder="请输入身份证" disabled></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="cancelEdit">取 消</el-button>
          <el-button type="primary" @click="editTea">确 定</el-button>
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
  name: "AddTea",
  data() {
    let nowTime = new Date()
    let nowYear = nowTime.getFullYear();
    let maxYear = nowYear - 1//默认是当前年-1
    let startTime = new Date(nowYear + '-9-01 00:00:00')//今年9月份开学时间
    if (nowTime > startTime) {
      maxYear += 1
    }
    let checkTeaIdCard = (rule, value, callback) => {
      let reg = /^\d{6}(18|19|20)?\d{2}(0[1-9]|1[012])(0[1-9]|[12]\d|3[01])\d{3}(\d|[xX])$/;//未开发
      if (!reg.test(value)) {
        return callback(new Error("身份证格式错误"));
      }
      callback();
    };
    return {
      teaId: '',
      teaName: '',
      className: '',
      majorName: '',
      classNo: '',
      pageNum: 1,
      pageSize: 20,
      total: 0,
      teaList: [{
        teaId: '',
        teaName: '',
        majorName: '',
        classNo: ''
      }],

      addFormVisible: false,
      editFormVisible: false,
      addTeaForm: {
        teaIdCard: '',
        majorName: '',
        teaName: '',
        classId: ''
      },
      editTeaForm: {
        teaIdCard: '',
        majorName: '',
        teaName: ''
      },
      majorNames: [],
      classNos: [],
      addRules: {
        classYear: {required: true, message: "请选择年级", trigger: "blur"},
        majorName: {required: true, message: "请选择专业", trigger: "blur"}, //非空验证
        classId: {required: true, message: "请选择班号", trigger: "blur"}, //非空验证
        teaName: [
          {required: true, message: "请输入班主任姓名", trigger: "blur"}, //非空验证
          {min: 2, max: 20, message: "姓名长度为2到20个字符", trigger: "blur",}, //规则验证
        ],
        teaIdCard: [
          {required: true, message: "请输入身份证", trigger: "blur"}, //非空验证
          {validator: checkTeaIdCard, trigger: "blur"},
        ]
      },
      editRules: {
        teaName: [
          {required: true, message: "请输入班主任姓名", trigger: "blur"}, //非空验证
          {min: 2, max: 20, message: "姓名长度为2到20个字符", trigger: "blur",}, //规则验证
        ],
        // teaIdCard: [
        //   {required: true, message: "请输入身份证", trigger: "blur"}, //非空验证
        //   {validator: checkTeaIdCard, trigger: "blur"},
        // ]
      },
      classYears: [{value: '1', label: maxYear}, {value: '2', label: maxYear - 1},
        {value: '3', label: maxYear - 2}, {value: '4', label: maxYear - 3}],
    }
  },
  methods: {
    getClasses(teaForm) {
      this.$set(this.addTeaForm, 'classId', '')
      if ((teaForm.majorName != '' && teaForm.majorName != undefined) && (teaForm.classYear != '' && teaForm.classYear != undefined))
        axios.get(service.baseURL + "/class/getClassesByMajorName?majorName=" + teaForm.majorName + "&classYear=" + teaForm.classYear,
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
    getTeaList() {//分页查询班主任表格
      axios.get(service.baseURL + "/teacher/teaPageList?pageNum=" + this.pageNum +
          "&pageSize=" + this.pageSize + "&teaId=" + this.teaId + "&teaName=" + this.teaName +
          "&className=" + this.className,
          {
            headers: {
              Authorization: this.$store.state.token
            }
          })
          .then((resp) => {
            if (resp.data.code == '20000') {
              this.teaList = resp.data.data.teaList
              this.total = resp.data.data.total
            } else {
              this.$message.error(resp.data.msg)
            }
          })
    },
    resetTeaList() {//刷新
      location.reload()
    },
    searchTeaList() {//待条件的分页查询
      this.pageNum = 1
      this.getTeaList();
    },
    openAddTea() {//显示班主任添加表单
      this.addFormVisible = true
      axios.get(service.baseURL + "/major/getAllMajorName",
          {
            headers: {
              Authorization: this.$store.state.token
            }
          })
          .then((resp) => {
            this.majorNames = resp.data.data;
          })
    },
    openEditTea(row) {//显示班主任编辑表单
      axios.get(service.baseURL + "/teacher/getTeaById?teaId=" + row.teaId,
          {
            headers: {
              Authorization: this.$store.state.token
            }
          })
          .then((resp) => {
            if (resp.data.code == '20000') {
              this.editFormVisible = true;
              this.editTeaForm = resp.data.data;
              // axios.get("http://localhost:8091/major/getAllMajorName",//不能换专业
              //     {
              //       headers: {
              //         Authorization: this.$store.state.token
              //       }
              //     })
              //     .then((resp) => {
              //       this.majorNames = resp.data.data;
              //     })
            } else {
              this.$message.error(resp.data.msg)
            }
          })
    },
    handleCurrentChange(pageNum) {//分页
      this.pageNum = pageNum
      this.getTeaList()
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.pageNum = 1
      this.getTeaList()
    },
    clearAddForm() {//关闭表单时清空数据
      this.addTeaForm = {}
      this.$refs.addTeaFormRef.clearValidate()
    },
    clearEditForm() {
      this.editTeaForm = {}
      this.$refs.editTeaFormRef.clearValidate()
    },
    cancelAdd() {//取消增加
      this.addFormVisible = false
      this.classNos = []
      this.clearAddForm()
      this.$set(this.addTeaForm, 'classId', '')
      this.$message.info("已取消新增")
    },
    cancelEdit() {//取消编辑
      this.editFormVisible = false
      this.clearEditForm()
      this.$message.info("已取消编辑")
    },
    addTea() {//确定添加班主任
      this.$refs.addTeaFormRef.validate((valid) => {
        if (valid) {//如果表单的填写合法,发送axios请求
          axios.post(service.baseURL + "/teacher/add", {
                'classYear': this.addTeaForm.classYear,
                'majorName': this.addTeaForm.majorName,
                'classId': this.addTeaForm.classId,
                'teaName': this.addTeaForm.teaName,
                'teaIdCard': this.addTeaForm.teaIdCard,
              },
              {
                headers: {
                  Authorization: this.$store.state.token
                }
              }
          ).then((resp) => {
            if (resp.data.code == '20000') {
              this.addFormVisible = false
              this.clearAddForm()
              this.getTeaList()
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
    delTea(row) {
      this.$confirm(`确认删除班主任：${row.teaName} ？`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      }).then(() => {
        axios.delete(service.baseURL + "/teacher/delTea?teaId=" + row.teaId,
            {
              headers: {
                Authorization: this.$store.state.token
              }
            })
            .then((resp) => {
              if (resp.data.code == '20000') {
                this.getTeaList();
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
    editTea() {
      this.$refs.editTeaFormRef.validate((valid) => {
        if (valid) {//如果表单的填写合法,发送axios请求
          axios.put(service.baseURL + "/teacher/editById", this.editTeaForm,
              {
                headers: {
                  Authorization: this.$store.state.token
                }
              }).then((resp) => {
            if (resp.data.code == '20000') {
              this.editFormVisible = false;//添加成功关闭表单
              this.clearEditForm()
              this.getTeaList();//刷新表格
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
    this.searchTeaList()
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