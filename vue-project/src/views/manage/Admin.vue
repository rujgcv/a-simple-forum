<template>
    <div class="select">
        <el-input
            v-model="selectName"
            style="width: 240px"
            placeholder="请输入昵称"
            :prefix-icon="Search"
            class="input"
        />
        <el-input
            v-model="selectPhone"
            style="width: 240px"
            placeholder="请输入电话"
            :prefix-icon="Search"
            class="input"
        />
        <el-button type="primary" @click="select()">搜索</el-button>
        <el-button type="success" @click="reset()">重置</el-button>
    </div>

    <div class="select">
        <el-button type="primary" @click="add()">新增</el-button>
        <el-popconfirm
            width="220"
            confirm-button-text="确定"
            cancel-button-text="取消"
            :icon="InfoFilled"
            icon-color="red"
            title="你确定要删除吗"
            @confirm="delBatch()"
        >
            <template #reference>
                <el-button type="danger" >批量删除</el-button>
            </template>
        </el-popconfirm>
    </div>

    <el-table 
        @selection-change="handleSelectionChange"
        :data="tableData"
        style="width: 100%"
        :header-cell-style="{background:'rgba(86, 150, 206, 0.13)',}"
    >
        <el-table-column type="selection" width="40" />
        <el-table-column prop="avatarUrl" label="头像" width="100" >
            <template v-slot="scope">
                <el-avatar :size="40" :src="scope.row.avatarUrl+'?t='+new Date().getTime()" />
            </template>
        </el-table-column>
        <el-table-column prop="name" label="姓名" width="160" />
        <el-table-column prop="phone" label="电话" width="160" />
        <el-table-column prop="createTime" label="创建时间" width="220" />
        <el-table-column prop="updateTime" label="上一次更改时间" width="220" />
        <el-table-column prop="status" label="状态" width="200">
        <template #default="scope">
            <el-switch 
            v-model="scope.row.status" 
            active-text="开启" 
            inactive-text="禁用"
            @change="statusChange(scope.row)"
            />
        </template>
        </el-table-column>
        <el-table-column label="操作">
            <template v-slot="scope">
                <el-button type="primary" @click="handleEdit(scope.row)">编辑</el-button>
                <el-popconfirm
                width="220"
                confirm-button-text="确定"
                cancel-button-text="取消"
                :icon="InfoFilled"
                icon-color="red"
                title="你确定要删除吗"
                @confirm="handleDel(scope.row)"
                >
                    <template #reference>
                        <el-button type="danger">删除</el-button>
                    </template>
                </el-popconfirm>
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

    <!-- 新增/编辑用户信息 -->
    <el-dialog
        v-model="dialogFormVisible"
        :title="formTitle"
        width="500"
        align-center
    >
        <el-form :model="form" label-width="auto" style="max-width: 360px;padding:0px 40px" @close="formCancel">
            <el-form-item label="头像 : ">
                <el-upload
                    action="#"
                    :auto-upload="false"
                    :show-file-list="false"
                    accept=".png,.jpg,.jpeg"
                    :on-change="handleAvatarChange"
                    class="avatar-uploader"
                >
                    <el-avatar v-if="form.avatarUrl" :size="120" :src="form.avatarUrl">
                    </el-avatar>
                    <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
                </el-upload>
                
                <el-dialog v-model="dialogVisible" title="头像预览">
                    <img :src="form.avatarUrl" class="preview-image"/>
                </el-dialog>
            </el-form-item>
            <el-form-item label="用户名 : ">
                <el-input v-model="form.name" />
            </el-form-item>
            <el-form-item label="手机号码 : ">
                <el-input v-model="form.phone" />
            </el-form-item>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button type="primary" @click="submitForm">确认</el-button>
                <el-button @click="dialogFormVisible = false">取消</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script lang="ts" setup>
    import { Search,InfoFilled,Plus, Delete, ZoomIn, Phone, List } from '@element-plus/icons-vue'
    import { computed, ref,reactive, onMounted } from 'vue'
    import type { UploadFile } from 'element-plus'
    import { ElMessage } from 'element-plus'
    import axios from '@/utils/request';

    // 控制编辑用户对话框是否可见
    const dialogFormVisible = ref(false)
    const formTitle = ref("")
    // 多选
    let multipleSelection = reactive([])
    
    // 表单
    let form = reactive({
        id: 0,
        avatarUrl: '',
        name: '',
        phone: '',
        createTime: '',
        updateTime: ''
    })

    // 分页
    var total = ref(10)
    const page = ref(1)
    const pageSize = ref(10)
    const size = ref('default')

    // 搜索框
    const selectName = ref('')
    const selectPhone = ref('')

    // 搜索
    function select(){
        load()
    }
    function reset(){
        selectName.value = ''
        selectPhone.value = ''
        load()
    }

    // 新增用户
    function add(){
        // 彻底重置表单对象
        Object.assign(form, {
            id: 0,
            avatarUrl: '',
            name: '',
            phone: '',
            createTime: '',
            updateTime: ''
        });
        
        // 清除头像文件缓存
        avatarFile.value = undefined;

        formTitle.value = "新增用户"
        dialogFormVisible.value = true
    }

    
    // 修改编辑方法
    function handleEdit(row: any) {
        // 使用 Object.assign 保持响应式
        Object.assign(form, row);
        avatarFile.value = undefined;
        formTitle.value = "编辑用户";
        dialogFormVisible.value = true;
    }

    // 行内删除用户
    function handleDel(row: any){
        const id = row.id;
        axios.delete(`/user/${id}`)
            .then(response => {
                const res = response.data;
                if (res.code === 1) {
                    ElMessage.success('删除成功');
                    load(); // 刷新表格数据
                } else {
                    ElMessage.error(res.msg || '删除失败');
                }
            }
        )
    }

    // 多选
    function handleSelectionChange(val){
        multipleSelection = val
    }

    // 批量删除用户
    function delBatch() {
        if (multipleSelection.length === 0) {
            ElMessage.warning('请先选择要删除的用户');
            return;
        }

        let ids = multipleSelection.map(v => v["id"])

        axios.post(`/user/delBatch`,ids)
            .then(response => {
                const res = response.data;
                if (res.code === 1) {
                    ElMessage.success('删除成功');
                    load();
                } else {
                    ElMessage.error(res.msg || '删除失败');
                }
            }
        )
    }

    // 取消表单
    function formCancel(){
        dialogFormVisible.value = false;
        load()
    }

    // 分页
    const handleSizeChange = (val: number) => {
        pageSize.value = val;
        load()
    };
    const handleCurrentChange = (val: number) => {
        page.value = val;
        load()
    };

    // 账号状态更改
    function statusChange(data){
        axios.put(`/user/status/${data.id}`)
        .then(response => {
            var code = response.data.code
            if(code){
                ElMessage.success("更改成功!")
            }else{
                ElMessage.warning("未知错误!")
            }
        })
    }

    const tableData = ref()

    // 新增头像相关状态
    const dialogVisible = ref(false)
    const avatarFile = ref<File>()

    // 处理头像选择
    const handleAvatarChange = (file: UploadFile) => {
        // 验证文件类型
        const allowedTypes = ['image/png', 'image/jpeg', 'image/jpg']
        if (!file.raw || !allowedTypes.includes(file.raw.type)) {
            ElMessage.error('仅支持 PNG/JPG/JPEG 格式图片')
            return false
        }

        // 生成预览
        const reader = new FileReader()
        reader.onload = (e) => {
            form.avatarUrl = e.target?.result as string
        }
        reader.readAsDataURL(file.raw)
        avatarFile.value = file.raw
        return false
    }

    const submitForm = async () => {
        try {
            // 1. 提交用户基本信息
            const userData = {
                id: form.id,
                name: form.name ? form.name.trim() : '',
                phone: form.phone ? form.phone.trim() : '',
                password: null
            };

            // 根据ID判断是编辑还是新增
            const api = form.id ? '/user/modify' : '/admin/add';
            
            // 发送请求并等待响应
            const userResponse = await axios.post(api, userData);
            const res = userResponse.data;

            // 处理用户提交结果
            if (res.code !== 1) {
                ElMessage.error(res.msg || '用户信息保存失败');
                return;
            }

            // 获取用户ID（新增时从响应中获取，编辑时用原有ID）
            const userId = form.id ? form.id : res.data; 
            ElMessage.success(form.id ? "用户信息更新成功" : "用户创建成功");

            // 2. 处理头像上传
            if (avatarFile.value) {
                try {
                    const formData = new FormData();
                    formData.append('userId', userId);
                    formData.append('file', avatarFile.value);

                    const { data } = await axios.post('/upload/userAvatar', formData);
                    if (data.code !== 1) {
                        ElMessage.warning('用户信息已保存，但头像上传失败: ' + data.msg);
                    } else {
                        ElMessage.success('头像上传成功');
                    }
                } catch (uploadError) {
                    ElMessage.warning('用户信息已保存，但头像上传异常: ' + uploadError.message);
                }
            }

            // 3. 关闭弹窗并刷新
            dialogFormVisible.value = false;
            load();
        } catch (err) {
            ElMessage.error('操作失败: ' + err.message);
        }
    };
    // 新增预览处理
    const handlePreview = () => {
        if (form.avatarUrl) {
            dialogVisible.value = true
        }
    }

    // 初始化
    onMounted(() => {
        load()
    })

    // 分页查询
    function load(){
        if(selectName)
        axios.get('/admin/page',{
            params: {
                name: selectName.value.trim() || undefined, // 去空格后转undefined
                phone: selectPhone.value.trim() || undefined,
                role: null,
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
        --el-avatar-size: 120px;
    }

    /* 修改1：上传区域外框变为圆形 */
    .avatar-uploader :deep(.el-upload) {
        border: 1px dashed var(--el-border-color);
        border-radius: 50% !important;  /* 关键修改 */
        cursor: pointer;
        position: relative;
        overflow: hidden;  /* 确保内容被裁剪为圆形 */
        transition: var(--el-transition-duration-fast);
    }

    .avatar-uploader :deep(.el-upload:hover) {
        border-color: var(--el-color-primary);
    }

    /* 修改2：未上传时的加号图标容器变为圆形 */
    .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 120px;
        height: 120px;
        border-radius: 50% !important;  /* 关键修改 */
        text-align: center;
    }

    /* 修改3：已上传的头像强制显示为圆形 */
    .el-avatar {
        --el-avatar-border-radius: 50% !important;  /* 关键修改 */
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
</style>
