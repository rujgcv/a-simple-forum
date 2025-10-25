<template>
    <div class="select">
        <el-input
            v-model="selectTitle"
            style="width: 240px"
            placeholder="请输入贴子标题"
            :prefix-icon="Search"
            class="input"
        />
        <el-input
            v-model="selectUser"
            style="width: 240px"
            placeholder="请输入发贴人"
            :prefix-icon="Search"
            class="input"
        />
        <el-autocomplete
            v-model="selectTab"
            style="width: 240px;margin-right: 10px;"
            placeholder="请输入贴吧"
            :prefix-icon="Search"
            class="input"
            :fetch-suggestions="querySearchAsync"
            :trigger-on-focus="false"
            clearable
            @select="handleTabSelect"
        >
            <template #loading>
                <svg class="circular" viewBox="0 0 50 50">
                    <circle class="path" cx="25" cy="25" r="20" fill="none" />
                </svg>
            </template>
        </el-autocomplete>
        <el-input
            v-model="selectDesc"
            style="width: 240px"
            placeholder="请输入描述"
            :prefix-icon="Search"
            class="input"
        />
        <el-button type="primary" @click="select()">搜索</el-button>
        <el-button type="success" @click="reset()">重置</el-button>
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
                <el-button type="danger" style="margin-left: 90px;" >批量删除</el-button>
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
        <el-table-column prop="title" label="标题" width="200" show-overflow-tooltip />
        <el-table-column prop="username" label="发贴人" width="150" show-overflow-tooltip />
        <el-table-column prop="tabName" label="贴吧" width="200" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" width="190" />
        <el-table-column prop="updateTime" label="上一次更改时间" width="190" />
        <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
                <el-tag :type="scope.row.status ? 'success' : 'danger'">
                    {{ scope.row.status ? '正常' : '已封禁' }}
                </el-tag>
            </template>
        </el-table-column>
        <el-table-column label="操作">
            <template v-slot="scope">
                <el-button type="success" @click="handleDetail(scope.row)">详情</el-button>
                <el-button 
                    :color="scope.row.status ? '#536976' : '#626aef'"
                    style="color: #fff"
                    @click="statusChange(scope.row)"
                >
                    <el-icon v-if="scope.row.status"><Remove /></el-icon>
                    <el-icon v-else><Check /></el-icon>
                    <span>{{ scope.row.status ? '禁用' : '启用' }}</span>
                </el-button>
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

    <!-- 分页 -->
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
    
    <!-- 贴子详情 -->
    <el-dialog
        v-model="detailFormVisible"
        title="贴子详情"
        width="1000px"
        align-center
    >
        <div class="detail-container">
            <el-row :gutter="30">
                <!-- 左侧图片和内容区域 -->
                <el-col :span="14">
                    <!-- 图片展示 -->
                    <div class="image-section">
                        <h3 class="section-title">图片列表（共{{ form.imgList?.length || 0 }}张）</h3>
                        <div class="image-grid">
                            <el-image 
                                v-for="(img, index) in form.imgList"
                                :key="index"
                                :src="img + '?t=' + Date.now()"
                                :preview-src-list="form.imgList"
                                :preview-teleported="true"
                                fit="cover"
                                class="grid-image"
                                :initial-index="index"
                                hide-on-click-modal
                            >
                                <template #error>
                                    <div class="image-error">
                                        <el-icon><Picture /></el-icon>
                                        <span>加载失败</span>
                                    </div>
                                </template>
                            </el-image>
                            <div v-if="!form.imgList?.length" class="empty-image">
                                <el-icon><Picture /></el-icon>
                                <span>暂无图片</span>
                            </div>
                        </div>
                    </div>

                    <!-- 内容描述 -->
                    <div class="content-section">
                        <h3 class="section-title">内容描述</h3>
                        <div class="content-box">
                            <pre>{{ form.description || '暂无描述' }}</pre>
                        </div>
                    </div>
                </el-col>

                <!-- 右侧信息区域 -->
                <el-col :span="10">
                    <!-- 基础信息 -->
                    <div class="info-section">
                        <h3 class="section-title">基本信息</h3>
                        <el-descriptions 
                            border
                            :column="1"
                            direction="vertical"
                        >
                            <el-descriptions-item label="标题">{{ form.title }}</el-descriptions-item>
                            <el-descriptions-item label="发帖人">{{ form.username }}</el-descriptions-item>
                            <el-descriptions-item label="所属贴吧">{{ form.tabName }}</el-descriptions-item>
                            <el-descriptions-item label="状态">
                                <el-tag :type="form.status ? 'success' : 'danger'">
                                    {{ form.status ? '正常' : '已封禁' }}
                                </el-tag>
                            </el-descriptions-item>
                            <el-descriptions-item label="封禁原因" v-if="!form.status">
                                {{ form.banReason || '无' }}
                            </el-descriptions-item>
                        </el-descriptions>
                    </div>

                    <!-- 时间信息 -->
                    <div class="time-section">
                        <h3 class="section-title">时间信息</h3>
                        <div class="time-box">
                            <div class="time-item">
                                <label>创建时间：</label>
                                <span>{{ form.createTime }}</span>
                            </div>
                            <div class="time-item">
                                <label>更新时间：</label>
                                <span>{{ form.updateTime }}</span>
                            </div>
                        </div>
                    </div>

                    <!-- 互动数据 -->
                    <div class="stats-section">
                        <h3 class="section-title">互动数据</h3>
                        <el-space :size="30" wrap>
                            <el-statistic 
                                title="评论数" 
                                :value="form.commentNum" 
                                value-style="color: #409EFF;"
                            />
                            <el-statistic
                                title="点赞数"
                                :value="form.likeNum"
                                value-style="color: #67C23A;"
                            />
                            <el-statistic
                                title="收藏数"
                                :value="form.favoriteNum"
                                value-style="color: #E6A23C;"
                            />
                        </el-space>
                    </div>
                </el-col>
            </el-row>
        </div>
    </el-dialog>

    <el-dialog
        v-model="banDialogVisible"
        title="禁用贴子"
        width="500px"
    >
        <el-form :model="banForm">
            <el-form-item label="禁用原因" required>
            <el-input
                v-model="banForm.banReason"
                type="textarea"
                :rows="3"
                placeholder="请输入禁用原因"
                maxlength="50"
                show-word-limit
            />
            </el-form-item>
        </el-form>
        
        <template #footer>
            <el-button @click="banDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="confirmBan()">确定</el-button>
        </template>
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

    // 多选
    let multipleSelection = reactive([])
    
    // 表单
    let form = reactive({
        id: 0,
        title: '',
        status: true,
        username: '',
        tabName: '',
        createTime: '',
        updateTime: '',
        banReason: '',
        imgList: [],
        description:'',
        commentNum: 0,
        likeNum: 0,
        favoriteNum: 0
    })
    
    // 远程搜索列表
    const tabNameSelectList = ref([]);
    // 远程搜索操作
    let timeout: ReturnType<typeof setTimeout>
    // 优化后的远程搜索方法
    const querySearchAsync = (queryString: string, cb: (arg: any) => void) => {
        if (timeout) {
            clearTimeout(timeout);
            timeout = null;
        }
        
        // 空查询时不发送请求
        if (!queryString.trim()) {
            cb([]);
            return;
        }
        
        timeout = setTimeout(async () => {
            try {
                const response = await axios.get("/tab/queryTabName", {
                    params: {
                        tabName: queryString.trim()
                    }
                });
                
                // 确保数据结构正确：{ value: string }[]
                tabNameSelectList.value = response.data.data
                
                // 过滤结果
                const results = queryString
                ? tabNameSelectList.value.filter(item => 
                    item.value.toLowerCase().includes(queryString.toLowerCase())
                    )
                : tabNameSelectList.value.slice(0, 10);
                
                cb(results);
            } catch (error) {
                console.error("搜索贴吧失败:", error);
                ElMessage.error("搜索贴吧失败，请稍后重试");
                cb([]);
            }
        }, 500); // 500ms防抖
    };

    // 新增响应式变量
    const banDialogVisible = ref(false)
    const currentRow = ref<any>(null)
    const banForm = reactive({
        banReason: ''
    })

    // 分页
    var total = ref(10)
    const page = ref(1)
    const pageSize = ref(10)
    const size = ref('default')

    // 搜索框
    const selectTitle = ref('')
    const selectUser = ref('')
    const selectTab = ref('')
    const selectDesc = ref('')
    
    // 搜索
    function select(){
        load()
    }
    function reset(){
        selectTitle.value = ''
        selectUser.value = ''
        selectTab.value = ''
        selectDesc.value = ''
        load()
    }

    // 展示详情列表
    function handleDetail(row: any) {
        // 使用 Object.assign 保持响应式
        Object.assign(form, row);
        detailFormVisible.value = true;
    }

    // 行内删除贴子
    function handleDel(row: any){
        const id = row.id;
        axios.delete(`/post/${id}`)
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

    // 批量删除贴子
    function delBatch() {
        if (multipleSelection.length === 0) {
            ElMessage.warning('请先选择要删除的贴子');
            return;
        }

        let ids = multipleSelection.map(v => v["id"])

        axios.post(`/post/delBatch`,ids)
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

    const handleTabSelect = (item) => {
        console.log(item)
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

    // 修改后的状态切换方法
    function statusChange(row: any) {
        currentRow.value = row
        if (row.status) { 
            // 当前是启用状态，执行禁用
            banForm.banReason = '' // 清空之前的理由
            banDialogVisible.value = true
        } else { 
            // 当前是禁用状态，执行启用
            axios.put(`/post/status`, { 
                id: row.id,
                status: row.status,
                banReason: null
            }).then(response => {
                var code = response.data.code
                if(code){
                    ElMessage.success("更改成功!")
                    load();
                }else{
                    ElMessage.warning("未知错误!")
                }
            })
        }
    }

    // 确认禁用操作
    function confirmBan() {
        banForm.banReason = banForm.banReason.trim()
        if(!banForm.banReason) {
            ElMessage.error('请填写禁用原因')
            return
        }
        if(banForm.banReason.length > 50){
            ElMessage.error('禁用理由不得超过50字')
        }
        
        axios.put(`/post/status`, {
            id: currentRow.value.id,
            status: currentRow.value.status,
            banReason: banForm.banReason
        }).then(response => {
            var code = response.data.code
            if(code){
                ElMessage.success("更改成功!")
                banDialogVisible.value = false

                load();
            }else{
                ElMessage.warning("未知错误!")
            }
        })
    }

    const tableData = ref()

    // 初始化
    onMounted(() => {
        load()
    })

    // 分页查询
    function load(){
        axios.get('/post/page',{
            params: {
                title: selectTitle.value.trim() || undefined,
                username: selectUser.value.trim() || undefined,
                tabName: selectTab.value.trim() || undefined,
                description: selectDesc.value.trim() || undefined,
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

    .detail-container {
        padding: 10px 20px;
        max-height: 70vh;
        overflow-y: auto;
    }

    .section-title {
        color: #606266;
        font-size: 16px;
        margin: 15px 0 10px;
        padding-left: 8px;
        border-left: 4px solid #409EFF;
    }

    /* 图片区域 */
    .image-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
        gap: 10px;
        max-height: 300px;
        overflow-y: auto;
        padding: 5px;
    }

    .grid-image {
        width: 100%;
        height: 120px;
        border-radius: 4px;
        box-shadow: 0 2px 6px rgba(0,0,0,0.1);
        transition: all 0.3s;
        cursor: pointer;
    }

    .grid-image:hover {
        transform: translateY(-3px);
        box-shadow: 0 4px 12px rgba(0,0,0,0.15);
    }

    .empty-image {
        height: 120px;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        color: #909399;
        border: 1px dashed #DCDFE6;
        border-radius: 4px;
    }

    /* 内容描述 */
    .content-box {
        background: #f8f9fa;
        border-radius: 6px;
        padding: 15px;
        max-height: 200px;
        overflow-y: auto;
        line-height: 1.6;
        pre {
            margin: 0;
            white-space: pre-wrap;
            font-family: inherit;
        }
    }

    /* 右侧信息区域 */
    .info-section,
    .time-section,
    .stats-section {
        margin-bottom: 25px;
    }

    .time-box {
        background: #f8f9fa;
        border-radius: 6px;
        padding: 15px;
        .time-item {
            margin: 8px 0;
            label {
                color: #909399;
                min-width: 70px;
                display: inline-block;
            }
            span {
                color: #606266;
            }
        }
    }

    /* 错误状态 */
    .image-error {
        height: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        background: #f5f7fa;
        color: #909399;
        .el-icon {
            font-size: 24px;
            margin-bottom: 5px;
        }
    }

    .circular {
        display: inline;
        height: 30px;
        width: 30px;
        animation: loading-rotate 2s linear infinite;
    }
    .path {
        animation: loading-dash 1.5s ease-in-out infinite;
        stroke-dasharray: 90, 150;
        stroke-dashoffset: 0;
        stroke-width: 2;
        stroke: var(--el-color-primary);
        stroke-linecap: round;
    }
</style>
