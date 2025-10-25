<template>
  <Header class="header"></Header>
  <div class="main-container">
    <div class="form-card">
      <div class="card-header">
        <h2 class="title">创建新贴吧</h2>
        <p class="subtitle">填写贴吧基本信息，让更多人发现你的贴吧</p>
      </div>
      
      <el-form :model="form" label-width="100px" class="custom-form">
        <el-form-item label="头像" class="form-item-custom">
          <div class="avatar-section">
            <el-upload
              action="#"
              :auto-upload="false"
              :show-file-list="false"
              accept=".png,.jpg,.jpeg"
              :on-change="handleImgChange"
              class="avatar-uploader"
            >
              <div class="avatar-preview">
                <el-image 
                  v-if="form.imgUrl" 
                  :src="form.imgUrl" 
                  fit="cover" 
                  class="avatar-image"
                />
                <div v-else class="avatar-placeholder">
                  <el-icon class="placeholder-icon"><Plus /></el-icon>
                  <span class="placeholder-text">上传头像</span>
                </div>
              </div>
            </el-upload>
            
            <div class="avatar-tips">
              <p>支持 JPG、PNG 格式</p>
              <p>图片大小不超过 2MB</p>
              <p>建议尺寸 200×200 像素</p>
            </div>
          </div>
        </el-form-item>
        
        <el-form-item label="贴吧名称" class="form-item-custom">
          <el-input 
            v-model="form.name" 
            maxlength="20" 
            @blur="handleNameCheck"
            placeholder="请输入贴吧名称"
            class="custom-input"
            size="large"
            :class="{ 'name-exists': nameExists }"
          />
          <div class="input-tips">
            <span v-if="nameExists" class="error-text">该贴吧名称已存在</span>
          </div>
        </el-form-item>
        
        <el-form-item label="贴吧类别" class="form-item-custom">
          <el-select 
            v-model="form.typeId" 
            placeholder="选择贴吧类别" 
            class="custom-select"
            size="large"
          >
            <el-option
              v-for="item in types.slice(1)"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="贴吧简介" class="form-item-custom">
          <el-input
            v-model="form.description"
            maxlength="50"
            placeholder="请简单描述贴吧的主题和内容"
            type="textarea"
            :rows="3"
            resize="none"
            class="custom-textarea"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item class="form-actions">
          <el-button 
            type="primary" 
            @click="submitForm" 
            class="submit-btn"
            size="large"
            :disabled="!formValid"
            :loading="submitting"
          >
            {{ submitting ? '提交中...' : '创建贴吧' }}
          </el-button>
          <el-button 
            @click="resetForm" 
            class="reset-btn"
            size="large"
          >
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script lang="ts" setup>
    import Header from '@/components/public/Header.vue';
    import { ref, reactive, computed, onMounted } from 'vue'
    import type { UploadFile } from 'element-plus'
    import { ElMessage } from 'element-plus'
    import axios from '@/utils/request';

    // 表单数据
    const form = reactive({
        imgUrl: '',
        name: '',
        typeId: '',
        description: ''
    })

    // 判断重名
    const nameExists = ref(false)
    const submitting = ref(false)

    const imgFile = ref<File>()

    // 贴吧分类
    const types = ref([
        { value: '0', label: '全部' },
        { value: '1', label: '游戏' },
        { value: '2', label: '动漫' },
        { value: '3', label: '小说' },
        { value: '4', label: '音乐' },
        { value: '5', label: '数码' },
        { value: '6', label: '娱乐' },
        { value: '7', label: '影视' },
        { value: '8', label: '科技' },
    ])

    // 表单验证
    const formValid = computed(() => {
        return form.name && 
               form.typeId && 
               form.imgUrl && 
               !nameExists.value &&
               !submitting.value
    })

    // 处理名称检查
    const handleNameCheck = async () => {
        if (!form.name.trim()) {
            nameExists.value = false
            return
        }

        // 自动添加"吧"后缀
        if (!form.name.endsWith('吧')) {
            form.name = form.name.length === 20 ? 
                form.name.slice(0, -1) + '吧' : 
                form.name + '吧'
        }

        // 查重验证
        axios.get("/tab/checkname", {
            params: {
                tabName: form.name
            }
        }).then(response => {
            if(response.data.code != 1){
                nameExists.value = true
            }else{
                nameExists.value = false
            }
        })
    }

    // 处理头像选择
    const handleImgChange = (file: UploadFile) => {
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
            form.imgUrl = e.target?.result as string
        }
        reader.readAsDataURL(file.raw)
        imgFile.value = file.raw
        return false
    }

    // 重置表单
    const resetForm = () => {
        Object.assign(form, {
            imgUrl: '',
            name: '',
            typeId: '',
            description: ''
        })
        nameExists.value = false
        imgFile.value = undefined
    }

    // 提交表单
    const submitForm = async () => {
        if (!formValid.value) {
            ElMessage.warning('请完善表单信息')
            return
        }

        submitting.value = true

        try {
            // 提交基本信息
            const data = {
                name: form.name.trim(),
                typeId: form.typeId.trim(),
                description: form.description.trim(),
            }

            // 后端提供新建贴吧进入待审核功能
            const tabResponse = await axios.post('/peding/add', data)
            const res = tabResponse.data

            if (res.code !== 1) {
                ElMessage.error(res.msg || '贴吧创建失败')
                return
            }

            const tabId = res.data
            ElMessage({
                type: 'success',
                message: '贴吧提交成功,等待审核中',
                duration: 5000,
            })

            // 上传头像
            if (imgFile.value) {
                try {
                    const formData = new FormData()
                    formData.append('tabId', tabId)
                    formData.append('file', imgFile.value)

                    const uploadResponse = await axios.post('/upload/pedingTabImg', formData)
                } catch (uploadError) {
                    console.warn('贴吧头像上传失败:', uploadError)
                }
            }

            resetForm()
            
        } catch (err: any) {
            ElMessage.error('操作失败: ' + (err.message || '网络错误'))
        } finally {
            submitting.value = false
        }
    }
</script>

<style scoped>
    .header{
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        z-index: 1000;   
        background: white;
        box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
    }
    
    .main-container {
        min-height: 100vh;
        background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
        padding: 120px 20px 60px;
        display: flex;
        justify-content: center;
        align-items: flex-start;
    }
    
    .form-card {
        background: white;
        border-radius: 16px;
        box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
        padding: 40px;
        width: 100%;
        max-width: 600px;
    }
    
    .card-header {
        text-align: center;
        margin-bottom: 40px;
    }
    
    .title {
        font-size: 28px;
        font-weight: 700;
        color: #2c3e50;
        margin-bottom: 8px;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
    }
    
    .subtitle {
        font-size: 16px;
        color: #7f8c8d;
        margin: 0;
    }
    
    .custom-form {
        width: 100%;
    }
    
    .form-item-custom {
        margin-bottom: 32px;
    }
    
    .form-item-custom :deep(.el-form-item__label) {
        font-weight: 600;
        color: #2c3e50;
        font-size: 16px;
    }
    
    .avatar-section {
        display: flex;
        align-items: flex-start;
        gap: 30px;
    }
    
    .avatar-uploader {
        border: 2px dashed #dcdfe6;
        border-radius: 12px;
        cursor: pointer;
        transition: all 0.3s ease;
    }
    
    .avatar-uploader:hover {
        border-color: #409eff;
        transform: scale(1.02);
    }
    
    .avatar-preview {
        width: 120px;
        height: 120px;
        display: flex;
        align-items: center;
        justify-content: center;
        border-radius: 12px;
        overflow: hidden;
    }
    
    .avatar-image {
        width: 100%;
        height: 100%;
        border-radius: 12px;
        object-fit: cover;
    }
    
    .avatar-placeholder {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        color: #c0c4cc;
        padding: 20px;
    }
    
    .placeholder-icon {
        font-size: 36px;
        margin-bottom: 8px;
    }
    
    .placeholder-text {
        font-size: 14px;
    }
    
    .avatar-tips {
        flex: 1;
    }
    
    .avatar-tips p {
        margin: 4px 0;
        font-size: 14px;
        color: #7f8c8d;
    }
    
    .custom-input, .custom-select, .custom-textarea {
        width: 100%;
    }

    .name-exists :deep(.el-input__wrapper) {
        box-shadow: 0 0 0 2px rgba(245, 108, 108, 0.2) !important;
        border-color: #ea4343;
    }
    
    .input-tips {
        font-size: 12px;
        margin-top: 6px;
    }
    
    .error-text {
        color: #ea4343;
        font-weight: 500;
    }
    
    .form-actions {
        margin-top: 40px;
        text-align: center;
    }
    
    .submit-btn {
        /* background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); */
        background-color: #8e43eb;
        border: none;
        border-radius: 8px;
        padding: 12px 36px;
        font-weight: 600;
        transition: all 0.3s ease;
    }
    
    .submit-btn:not(:disabled):hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px #8f43eb4a;
    }
    
    .submit-btn:disabled {
        background: #c0c4cc;
        cursor: not-allowed;
        transform: none;
        box-shadow: none;
    }
    
    .reset-btn {
        border-radius: 8px;
        padding: 12px 36px;
        font-weight: 600;
        transition: all 0.3s ease;
    }
    
    .reset-btn:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    }
    
    @media (max-width: 768px) {
        .main-container {
            padding: 100px 15px 40px;
        }
        
        .form-card {
            padding: 30px 20px;
        }
        
        .avatar-section {
            flex-direction: column;
            gap: 20px;
        }
        
        .title {
            font-size: 24px;
        }
    }
</style>