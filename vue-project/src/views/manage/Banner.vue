<template>
  <div class="carousel-manage">
    <!-- 操作栏 -->
    <div class="action-bar">
      <el-button 
        type="primary" 
        size="large" 
        @click="handleAdd"
        :disabled="!canAdd"
      >
        <el-icon><Plus /></el-icon>新增轮播图
        <span v-if="!canAdd">（已达上限）</span>
      </el-button>
    </div>

    <!-- 数据表格 -->
    <el-table 
      :data="tableData" 
      v-loading="loading"
      border 
      stripe
      style="width: 100%; --el-table-border-color: #ebeef5;"
      header-row-class-name="custom-header"
    > 
      <el-table-column label="轮播图" width="280">
        <template #default="{ row, $index }">
          <div class="image-wrapper">
            <el-image 
              :src="row.imgUrl+'?t='+new Date().getTime()" 
              fit="cover"
              class="carousel-image"
              :preview-src-list="imgList"
              :preview-teleported="true"
              :initial-index="$index"
            />
          </div>
        </template>
      </el-table-column>

      <el-table-column prop="link" label="跳转链接" min-width="240">
        <template #default="{ row }">
          <el-link type="primary" :href="row.link" target="_blank">{{ row.link }}</el-link>
        </template>
      </el-table-column>

      <el-table-column 
        label="操作" 
        width="180" 
        fixed="right"
        align="center"
      >
        <template #default="{ row }">
          <div class="action-buttons">
            <el-button 
              type="primary" 
              size="small" 
              plain
              @click="handleEdit(row)"
            >
              <el-icon><Edit /></el-icon>编辑
            </el-button>
            <el-popconfirm 
              title="确定要删除这条记录吗？" 
              @confirm="handleDel(row)"
              :disabled="!canDelete"
            >
              <template #reference>
                <el-button 
                  type="danger" 
                  size="small" 
                  plain
                  :disabled="!canDelete"
                >
                  <el-icon><Delete /></el-icon>删除
                </el-button>
              </template>
            </el-popconfirm>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <!-- 编辑对话框 -->
    <el-dialog 
      v-model="dialogVisible" 
      :title="dialogTitle" 
      width="680px"
      class="custom-dialog"
    >
      <div v-if="form.id === 0" class="form-tips">
        <el-icon><InfoFilled /></el-icon>
        当前已有 {{ tableData.length }} 张轮播图（最多5张）
      </div>

      <el-form :model="form" label-width="100px">
        <el-form-item label="轮播图：" required>
          <div class="upload-card">
            <el-upload
              action="#"
              :auto-upload="false"
              :show-file-list="false"
              accept=".png,.jpg,.jpeg"
              :on-change="handleImgChange"
              class="avatar-uploader"
            >
              <template #default>
                <template v-if="form.imgUrl">
                  <div class="preview-wrapper">
                    <img :src="form.imgUrl" class="preview-image">
                    <div class="hover-mask">
                      <el-icon :size="32" color="#fff"><Edit /></el-icon>
                    </div>
                  </div>
                </template>
                <template v-else>
                  <div class="upload-empty">
                    <el-icon :size="48" color="var(--el-color-primary)"><UploadFilled /></el-icon>
                    <div class="upload-tip">点击上传图片 (支持JPG/PNG)</div>
                    <div class="upload-subtip">建议尺寸：700×240px</div>
                  </div>
                </template>
              </template>
            </el-upload>
          </div>
        </el-form-item>

        <el-form-item label="跳转链接：">
          <el-input 
            v-model="form.link" 
            placeholder="请输入跳转地址 (https://)" 
            clearable
            class="custom-input"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确认提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
  import { ref, reactive, computed, onMounted } from 'vue'
  import axios from '@/utils/request';
  import type { UploadFile } from 'element-plus'
  import { ElMessage } from 'element-plus'
  import { Plus, Edit, Delete, UploadFilled, InfoFilled } from '@element-plus/icons-vue'

  // 响应式数据
  const tableData = ref([])
  const dialogVisible = ref(false)
  const loading = ref(false)
  const dialogTitle = ref('新增轮播图')
  const form = reactive({
    id: 0,
    imgUrl: '',
    link: ''
  })
  const imgFile = ref<File>()
  const imgList = ref([])

  // 验证逻辑
  const canAdd = computed(() => tableData.value.length < 5)
  const canDelete = computed(() => tableData.value.length > 1)

  const submitForm = async () => {
    if (!form.imgUrl) {
      ElMessage.warning('请上传轮播图');
      return;
    }

    if(!form.link.trim()){
      ElMessage.warning('请输入跳转链接');
      return;
    }

    try {
      loading.value = true;
      const data = {
        id: form.id,
        link: form.link.trim(),
      };

      if (form.id === 0) {
        // 新增
        const response = await axios.post("/banner/add", data);
        const res = response.data;
        if (res.code !== 1) {
          ElMessage.error(res.msg);
          return;
        }

        if (!imgFile.value) {
          ElMessage.warning('未找到上传的文件');
          return;
        }
        // 上传图片
        uploadImg(res.data);

        ElMessage.success('新增轮播图成功');
      } else {
        // 修改
        const response = await axios.post("/banner/modify", data);
        const res = response.data;
        if (res.code !== 1) {
          ElMessage.error(res.msg);
          return;
        }

        if (imgFile.value) {
          // 上传图片
          uploadImg(form.id);
          return;
        }

        ElMessage.success('修改轮播图成功');
      }
    } catch (error) {
      ElMessage.error('操作失败: ' + error.message);
    } finally {
      load()
      loading.value = false;
    }
  };

  // 文件上传
  async function uploadImg(id){
    const uploadFormData = new FormData();
    uploadFormData.append('id', id);
    uploadFormData.append('file', imgFile.value);

    try {
      const { data } = await axios.post('/upload/banner', uploadFormData);
      if (data.code !== 1) {
        ElMessage.warning('轮播图信息已保存，但图片上传失败: ' + data.msg);
      } else {
        dialogVisible.value = false;
        load(); // 刷新数据
      }
    } catch (uploadError) {
      ElMessage.error('图片上传失败');
    }
  }

  // 修改后的文件上传处理
  const handleImgChange = (file: UploadFile) => {
    if (!file.raw) return
      
    const allowedTypes = ['image/png', 'image/jpeg', 'image/jpg']
    if (!file.raw || !allowedTypes.includes(file.raw.type)) {
      ElMessage.error('仅支持 PNG/JPG/JPEG 格式图片')
      return false
    }

    if (file.size > 2 * 1024 * 1024) {
      ElMessage.error('图片大小不能超过2MB')
      return false
    }

    const reader = new FileReader()
    reader.onload = (e) => {
      if (e.target?.result) {
        form.imgUrl = e.target?.result as string
      }
    }
    reader.readAsDataURL(file.raw)
    imgFile.value = file.raw
    return false
  }

  // 打开新增对话框
  const handleAdd = () => {
    if (!canAdd.value) {
      ElMessage.warning('最多只能添加5张轮播图')
      return
    }

    dialogTitle.value = '新增轮播图'
    Object.assign(form, {
      id: 0,
      imgUrl: '',
      link: ''
    })
    dialogVisible.value = true
  }

  // 打开编辑对话框
  const handleEdit = (row: typeof form) => {
    dialogTitle.value = '编辑轮播图'
    Object.assign(form, { ...row })
    dialogVisible.value = true
  }

  // 删除条目
  const handleDel = async (row) => {
    if (!canDelete.value) {
      ElMessage.warning('至少需要保留1张轮播图')
      return
    }

    loading.value = true
    axios.delete(`/banner/${row.id}`).then(response => {
      var res = response.data
      if(res.code !== 1){
        ElMessage.error("删除失败: " + res.msg)
        return;
      }
      ElMessage.success("删除成功")
      load()
    })
    loading.value = false
  }

  onMounted(() => {
    load()
  })

  function load(){
    axios.get("/banner/getAll").then(response => {
      console.log(response)
      var res = response.data.data

      imgList.value = []
      console.log(res)
      for(var i=0;i<res.length;i++){
        imgList.value.push(res[i].imgUrl)
      }
      console.log(imgList.value)
      tableData.value = res
    })
  }
</script>

<style scoped>
    .carousel-manage {
        padding: 24px;
        background: #f8fafc;
        min-height: 100vh;
    }

    .action-bar {
        margin-bottom: 24px;
        padding: 16px;
        background: #fff;
        border-radius: 8px;
        box-shadow: 0 2px 12px rgba(0,0,0,0.05);
    }

    .custom-header th {
        background: #f8fafc !important;
        font-weight: 600;
        color: #334155;
    }

    .image-wrapper {
        padding: 8px;
        background: #fff;
        border-radius: 6px;
        border: 1px solid #e2e8f0;
    }

    .carousel-image {
        width: 100%;
        height: 160px;
        border-radius: 4px;
        transition: transform 0.2s;
    }

    .carousel-image:hover {
        transform: scale(1.02);
    }

    .action-buttons {
        display: flex;
        gap: 8px;
        justify-content: center;
    }

    .upload-card {
        border: 2px dashed #e2e8f0;
        border-radius: 12px;
        background: #fff;
        min-height: 280px;
        display: flex;
        align-items: center;
        justify-content: center;
        transition: all 0.3s;
        cursor: pointer;
        overflow: hidden;
    }

    .upload-card:hover {
        border-color: var(--el-color-primary);
        background: #f8fafc;
    }

    .preview-wrapper {
        position: relative;
        width: 100%;
        height: 280px;
    }

    .preview-image {
        width: 100%;
        height: 100%;
        object-fit: contain;
        border-radius: 8px;
    }

    .hover-mask {
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: rgba(0,0,0,0.5);
        display: flex;
        align-items: center;
        justify-content: center;
        opacity: 0;
        transition: opacity 0.3s;
    }

    .preview-wrapper:hover .hover-mask {
        opacity: 1;
    }

    .upload-empty {
        text-align: center;
        padding: 32px;
    }

    .upload-tip {
        margin-top: 16px;
        color: #64748b;
        font-weight: 500;
    }

    .upload-subtip {
        color: #94a3b8;
        font-size: 12px;
        margin-top: 8px;
    }

    .custom-dialog {
        border-radius: 12px;
    }

    .custom-input :deep(.el-input__wrapper) {
        border-radius: 8px;
        padding: 8px 16px;
    }

    .form-tips {
        padding: 8px 16px;
        background: #f0f9ff;
        border-radius: 4px;
        color: #409eff;
        margin-bottom: 16px;
        display: flex;
        align-items: center;
        gap: 8px;
    }

    .el-button.is-disabled {
        opacity: 0.6;
        cursor: not-allowed;
    }
</style>