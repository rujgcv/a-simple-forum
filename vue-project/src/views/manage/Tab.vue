<template>
    <div class="select">
        <el-input
            v-model="selectName"
            style="width: 240px"
            placeholder="请输入标题"
            :prefix-icon="Search"
            class="input"
        />
        <el-input
            v-model="selectDesc"
            style="width: 240px"
            placeholder="请输入简介"
            :prefix-icon="Search"
            class="input"
        />
        <el-select v-model="selectType" placeholder="Select" style="width: 240px;margin-right: 10px;">
            <el-option
                v-for="item in types"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
        </el-select>
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
        <el-table-column prop="imgUrl" label="头像" width="100" >
            <template v-slot="scope">
                <!-- <el-avatar :size="40" :src="scope.row.imgUrl+'?t='+new Date().getTime()" /> -->
                <el-image class="img" :src="scope.row.imgUrl+'?t='+new Date().getTime()" fit="cover" />
            </template>
        </el-table-column>
        <el-table-column prop="name" label="名称" width="200" />
        <el-table-column prop="type" label="类别" width="100" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column prop="updateTime" label="上一次更改时间" width="180" />
        <el-table-column prop="approveName" label="审批人" width="100" />
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
                <el-button type="success" @click="handleDetail(scope.row)">详情</el-button>
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

    <!-- 新增/编辑贴吧信息 -->
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
                    :on-change="handleImgChange"
                    class="avatar-uploader"
                >
                    <el-image :size="100" v-if="form.imgUrl" :src="form.imgUrl" fit="cover" style="width: 100px; height: 100px; border-radius: 10px;" />
                    <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
                </el-upload>
                
                <el-dialog v-model="dialogVisible" title="头像预览">
                    <img :src="form.imgUrl" class="preview-image"/>
                </el-dialog>
            </el-form-item>
            <el-form-item label="名称 : ">
                <el-input v-model="form.name" maxlength="20" @blur="nameCheck()"/>
            </el-form-item>
            <el-form-item label="类别 : ">
                <el-select v-model="form.typeId" placeholder="Select" style="width: 160px;margin-right: 10px;">
                    <el-option
                        v-for="item in types.slice(1)"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                    />
                </el-select>
            </el-form-item>
            <el-form-item label="简介 : ">
                <el-input
                    v-model="form.description"
                    maxlength="50"
                    style="width: 360px"
                    placeholder="请输入简介"
                    show-word-limit
                    type="textarea"
                />
            </el-form-item>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button type="primary" @click="submitForm">确认</el-button>
                <el-button @click="dialogFormVisible = false">取消</el-button>
            </div>
        </template>
    </el-dialog>

    
    <!-- 贴吧详情 -->
    <el-dialog
        v-model="detailFormVisible"
        title="贴吧详情"
        width="800"
        align-center
    >
        <el-descriptions
            direction="vertical"
            border
            style="margin-top: 10px;margin-bottom: 50px;"
        >
            <el-descriptions-item
            :rowspan="2"
            :width="140"
            label="贴吧头像"
            align="center"
            >
            <el-image
                style="width: 100px; height: 100px; border-radius: 10px;"
                :src="form.imgUrl+'?t='+new Date().getTime()" fit="cover"
            />
            </el-descriptions-item>
            <el-descriptions-item label="贴吧名称">{{ form.name }}</el-descriptions-item>
            <el-descriptions-item label="贴吧类别">{{ form.type }}</el-descriptions-item>
            <el-descriptions-item label="关注数量">{{ form.focusNum }}</el-descriptions-item>
            <el-descriptions-item label="帖子数量">{{ form.postNum }}</el-descriptions-item>
            <el-descriptions-item label="贴吧简介">{{ form.description }}</el-descriptions-item>
        </el-descriptions>
    </el-dialog>
</template>

<script lang="ts" setup>
    import { Search,InfoFilled,Plus } from '@element-plus/icons-vue'
    import { ref,reactive, onMounted } from 'vue'
    import type { UploadFile } from 'element-plus'
    import { ElMessage } from 'element-plus'
    import { ElSelect, ElOption } from 'element-plus'
    import axios from '@/utils/request';

    // 控制编辑用户对话框是否可见
    const dialogFormVisible = ref(false)
    const detailFormVisible = ref(false)
    const formTitle = ref("")
    // 多选
    let multipleSelection = reactive([])
    
    // 表单
    let form = reactive({
        id: 0,
        imgUrl: '',
        name: '',
        approveName:'',
        type: '',
        typeId: '',
        createTime: '',
        updateTime: '',
        description:'',
        postNum: '',
        focusNum: ''
    })

    // 分页
    var total = ref(10)
    const page = ref(1)
    const pageSize = ref(10)
    const size = ref('default')

    // 搜索框
    const selectName = ref('')
    const selectDesc = ref('')
    const selectType = ref('0')

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
    

    // 搜索
    function select(){
        load()
    }
    function reset(){
        selectName.value = ''
        selectDesc.value = ''
        selectType.value = '0'
        load()
    }

    // 检查name
    function nameCheck(){
        if(form.name.charAt(form.name.length-1) != '吧'){
            if(form.name.length == 20){
                form.name = form.name.slice(0,form.name.length-1) + "吧"
            }else {
                form.name = form.name + "吧"
            }
        }
    }

    // 新增贴吧
    function add(){
        // 彻底重置表单对象
        Object.assign(form, {
            id: 0,
            imgUrl: '',
            name: '',
            type: '',
            typeId: '',
            createTime: '',
            updateTime: '',
            description:'',
        });
        
        // 清除头像文件缓存
        imgFile.value = undefined;
        formTitle.value = "新增贴吧"
        dialogFormVisible.value = true
    }

    
    // 修改编辑方法
    function handleEdit(row: any) {
        // 使用 Object.assign 保持响应式
        Object.assign(form, row);
        form.typeId = form.typeId.toString()
        imgFile.value = undefined;
        formTitle.value = "编辑贴吧";
        dialogFormVisible.value = true;
    }

    // 展示详情列表
    function handleDetail(row: any) {
        // 使用 Object.assign 保持响应式
        Object.assign(form, row);
        imgFile.value = undefined;
        detailFormVisible.value = true;
    }

    // 行内删除用户
    function handleDel(row: any){
        const id = row.id;
        axios.delete(`/tab/${id}`)
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

        axios.post(`/tab/delBatch`,ids)
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

    function detailformCancel(){
        detailFormVisible.value = false;
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
        axios.put(`/tab/status/${data.id}`)
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
    const imgFile = ref<File>()

    // 处理头像选择
    const handleImgChange = (file: UploadFile) => {
        // 验证文件类型
        const allowedTypes = ['image/png', 'image/jpeg', 'image/jpg']
        if (!file.raw || !allowedTypes.includes(file.raw.type)) {
            ElMessage.error('仅支持 PNG/JPG/JPEG 格式图片')
            return false
        }

        if (file.size > 2 * 1024 * 1024) {
            ElMessage.error('图片大小不能超过2MB')
            return false
        }

        // 生成预览
        const reader = new FileReader()
        reader.onload = (e) => {
            form.imgUrl = e.target?.result as string
        }
        reader.readAsDataURL(file.raw)
        imgFile.value = file.raw
        return false
    }

    const submitForm = async () => {
        try {
            // 1. 提交基本信息
            const data = {
                id: form.id,
                name: form.name.trim(),
                typeId: form.typeId.trim(),
                description: form.description.trim()
            };

            console.log(data)

            if(data.name == '') return ElMessage.error("贴吧名称不能为空!")
            if(data.typeId == '') return ElMessage.error("贴吧类别不能为空!")
            if(form.imgUrl == undefined && imgFile.value == undefined) return ElMessage.error("贴吧头像不能为空!")

            // 根据ID判断是编辑还是新增
            const api = form.id ? '/tab/modify' : '/tab/add/system';
            
            // 发送请求并等待响应
            const tabResponse = await axios.post(api, data);
            const res = tabResponse.data;

            // 处理提交结果
            if (res.code !== 1) {
                ElMessage.error(res.msg || '贴吧保存失败');
                return;
            }

            // 获取ID（新增时从响应中获取，编辑时用原有ID）
            const tabId = form.id ? form.id : res.data; 
            ElMessage.success(form.id ? "贴吧更新成功" : "贴吧创建成功");

            // 2. 处理头像上传
            if (imgFile.value) {
                try {
                    const formData = new FormData();
                    formData.append('tabId', tabId);
                    formData.append('file', imgFile.value);

                    const { data } = await axios.post('/upload/tabImg', formData);
                    if (data.code !== 1) {
                        ElMessage.warning('贴吧信息已保存，但贴吧头像上传失败: ' + data.msg);
                    } else {
                        ElMessage.success('贴吧头像上传成功');
                    }
                } catch (uploadError) {
                    ElMessage.warning('信息已保存，但贴吧头像上传异常: ' + uploadError.message);
                }
            }

            // 3. 关闭弹窗并刷新
            dialogFormVisible.value = false;
            load();
        } catch (err) {
            ElMessage.error('操作失败: ' + err.message);
        }
    };
    // 初始化
    onMounted(() => {
        load()
    })

    // 分页查询
    function load(){
        axios.get('/tab/page',{
            params: {
                name: selectName.value.trim() || undefined, // 去空格后转undefined
                description: selectDesc.value.trim() || undefined,
                typeId: selectType.value,
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
</style>
