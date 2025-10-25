<template>
  <div class="box">
    <div class="header">
      <div class="total">
        <div><img class="icon" src="../../assets/img/peding.svg" ></div>
        <div class="tent">
          <div class="t1">{{ total }}</div>
          <div class="t2">待审核贴吧</div>
        </div>
      </div>
    </div>
    <el-table 
      :data="tableData"
      style="width: 100%;"
      :header-cell-style="{background:'rgba(86, 150, 206, 0.13)',paddingLeft: '20px'}"
      :cell-style="{ paddingLeft: '20px' }"
    >
      <el-table-column prop="imgUrl" label="头像" width="100" >
        <template v-slot="scope">
          <el-image class="img" :src="scope.row.imgUrl+'?t='+new Date().getTime()" fit="cover" />
        </template>
      </el-table-column>
      <el-table-column prop="name" label="名称" width="300" show-overflow-tooltip/>
      <el-table-column prop="type" label="类别" width="140" />
      <el-table-column prop="description" label="简介" width="300" show-overflow-tooltip/>
      <el-table-column prop="createTime" label="创建时间" width="220" />
      <el-table-column label="操作">
        <template v-slot="scope">
          <el-button type="primary" @click="handleApprove(scope.row)">审核</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="demo-pagination-block">
      <el-pagination
      v-model:current-page="page"
      v-model:page-size="pageSize"
      :page-sizes="[5, 10, 15, 20]"
      :size="size"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      />
    </div>

    <!-- 审核弹窗 -->
    <el-dialog
      v-model="approveDialogVisible"
      :title="`贴吧审核 - ${currentTab?.name}`"
      width="600px"
    >
      <div class="approve-content">
        <!-- 贴吧详情 -->
        <div class="tab-detail">
          <h3 class="section-title">贴吧详情</h3>
          <div class="detail-grid">
            <div class="detail-item diagram-img">
              <el-image 
                v-if="currentTab?.imgUrl"
                :src="currentTab.imgUrl" 
                class="detail-avatar"
                fit="cover"
              />
              <span v-else class="no-avatar">无头像</span>
            </div>
            <div class="content">
              <div class="detail-item">
                <label>贴吧名称：</label>
                <span>{{ currentTab?.name }}</span>
              </div>
              <div class="detail-item">
                <label>贴吧类别：</label>
                <span>{{ currentTab?.type }}</span>
              </div>
              <div class="detail-item">
                <label>创建时间：</label>
                <span>{{ formatTime(currentTab?.createTime) }}</span>
              </div>
              <div class="detail-item description-range">
                <label>贴吧简介：</label>
                <span>{{ currentTab?.description || '暂无描述' }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 审核操作 -->
        <div class="approve-action">
          <h3 class="section-title">审核操作</h3>
          <div class="action-buttons">
            <el-radio-group v-model="approveStatus">
              <el-radio label="pass">审核通过</el-radio>
              <el-radio label="reject">审核不通过</el-radio>
            </el-radio-group>
          </div>

          <!-- 不通过理由 -->
          <div v-if="approveStatus === 'reject'" class="reject-reason">
            <el-input
              v-model="banReason"
              type="textarea"
              :rows="3"
              placeholder="请输入不通过理由（必填）"
              maxlength="100"
              show-word-limit
            />
          </div>
        </div>
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="approveDialogVisible = false">取消</el-button>
          <el-button 
            type="primary" 
            @click="submitApprove"
            :disabled="approveStatus === 'reject' && !banReason.trim()"
          >
            确认提交
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
  import { ref, reactive, onMounted } from 'vue'
  import { ElMessage } from 'element-plus'
  import axios from '@/utils/request';

  // 表单
  let form = reactive({
    id: 0,
    imgUrl: '',
    name: '',
    type: '',
    typeId: '',
    createTime: '',
    description:'',
  })

  // 分页
  var total = ref(10)
  const page = ref(1)
  const pageSize = ref(10)
  const size = ref('default')

  // 审核相关
  const approveDialogVisible = ref(false)
  const currentTab = ref<any>(null)
  const approveStatus = ref('pass')
  const banReason = ref('')

  // 分页
  const handleSizeChange = (val: number) => {
    pageSize.value = val;
    load()
  };
  const handleCurrentChange = (val: number) => {
    page.value = val;
    load()
  };

  const tableData = ref()

  // 时间格式化
  const formatTime = (time: string) => {
    if (!time) return '-'
    return new Date(time).toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    }).replace(/\//g, '-')
  }

  // 打开审核弹窗
  const handleApprove = (row: any) => {
    currentTab.value = { ...row }
    approveStatus.value = 'pass'
    banReason.value = ''
    approveDialogVisible.value = true
  }

  // 提交审核
  const submitApprove = async () => {
    if (approveStatus.value === 'reject' && !banReason.value.trim()) {
      ElMessage.error('请填写不通过理由')
      return
    }

    try {
      const requestData = {
        id: currentTab.value.id,
        status: approveStatus.value, // 'pass' 或 'reject'
        banReason: approveStatus.value === 'reject' ? banReason.value : null
      }

      // 调用后端审核接口
      const response = await axios.post('/peding/approve', requestData)
      
      if (response.data.code === 1) {
        ElMessage.success(approveStatus.value === 'pass' ? '审核通过' : '已拒绝该贴吧申请')
        approveDialogVisible.value = false
        load() // 刷新列表
      } else {
        ElMessage.error(response.data.msg || '审核失败')
      }
      
    } catch (error) {
      ElMessage.error('提交失败，请重试')
      console.error('审核提交错误:', error)
    }
  }

  // 初始化
  onMounted(() => {
    load()
  })

  // 分页查询
  function load(){
    axios.get('/peding/page',{
      params: {
        page: page.value,
        pageSize: pageSize.value
      }
    }).then(response => {
      console.log(response)
      var res = response.data.data
      tableData.value = res.records
      total.value = res.total
    })
  }
</script>

<style scoped>
  .demo-pagination-block{
      padding: 10px 0 20px 10px
  }
  .select{
      margin: 10px 10px;
  }
  .input{
      margin-right: 10px;
  }
  .avatar-uploader {
      --el-avatar-size: 100px;
  }

  .avatar-uploader :deep(.el-upload) {
    border: 1px dashed var(--el-border-color);
    border-radius: 10px !important;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: var(--el-transition-duration-fast);
  }

  .avatar-uploader :deep(.el-upload:hover) {
    border-color: var(--el-color-primary);
  }

  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 100px;
    height: 100px;
    border-radius: 10px !important;
    text-align: center;
  }

  .el-avatar {
    --el-avatar-border-radius: 10px !important;
  }

  .avatar-actions {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0,0,0,0.5);
    display: none;
    align-items: center;
    justify-content: center;
    gap: 20px;
  }

  .avatar-uploader:hover .avatar-actions {
    display: flex;
  }

  .action-icon {
    color: white;
    font-size: 24px;
    cursor: pointer;
    transition: all 0.3s;
  }

  .action-icon:hover {
    transform: scale(1.2);
  }

  .preview-image {
    width: 100%;
    max-height: 70vh;
    object-fit: contain;
  }

  .img{
    width: 44px; 
    height: 44px;
    display: flex;
    border-radius: 5px;
  }

  .box{
    display: flex;
    flex-direction: column;
  }

  .header{
    width: 100%;
    height: 140px;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .total{
    width: 96%;
    height: 90px;
    background-color: white;
    border-radius: 10px;
    min-width: 400px;
    display: flex;
    flex-direction: row;
    align-items: center;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  }
  
  .icon{
    margin-left: 20px;
    margin-right: 10px;
  }

  .t1{
    font-size: 30px;
  }

  .t2{
    font-size: 16px;
    color: #8c939d;
  }

  /* 审核弹窗样式 */
  .approve-content {
    max-height: 60vh;
    overflow-y: auto;
  }

  .section-title {
    font-size: 16px;
    font-weight: 600;
    color: #2c3e50;
    margin-bottom: 16px;
    padding-bottom: 8px;
    border-bottom: 1px solid #e4e7ed;
  }

  .text{
    display: flex;
    flex-direction: column;
  }

  .content{
    display: flex;
    flex-direction: column;
    gap: 12px;
  }

  .detail-grid {
    display: flex;
    height: 180px;
    margin-bottom: 20px;
  }

  .diagram-img {
    margin-left: 20px;
    margin-right: 40px;
  }

  .detail-item {
    display: flex;
    align-items: flex-start;
  }

  .detail-item label {
    font-weight: 500;
    color: #606266;
    min-width: 80px;
    margin-right: 8px;
  }

  .detail-item span {
    color: #2c3e50;
    word-break: break-all;
  }

  .detail-avatar {
    width: 100px;
    height: 100px;
    border-radius: 6px;
  }

  .no-avatar {
    color: #7f8c8d;
    font-style: italic;
  }

  .approve-action {
    border-top: 1px solid #e4e7ed;
    padding-top: 20px;
  }

  .action-buttons {
    margin-bottom: 16px;
  }

  .reject-reason {
    margin-top: 16px;
  }

  .dialog-footer {
    display: flex;
    justify-content: flex-end;
    gap: 12px;
  }

  .description-range{
    padding-right: 40px;
  }
</style>