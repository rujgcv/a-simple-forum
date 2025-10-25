<template>
    <div class="b">
        <div v-for="i in 16">
            <div class="tabBox">
                <div class="t1">
                    <el-image class="img" :src="url" fit="cover" />
                </div>
                <div class="t2">
                    <div class="title">fufu吧</div>
                    <div class="content">fufu，你是人间璀璨的星辰，温柔如月华，智慧似星河，一笑倾天下，才华破九霄！举</div>
                </div>
            </div>
        </div>
        <div class="demo-pagination-block">
            <el-pagination
            v-model:current-page="page"
            v-model:page-size="pageSize"
            layout="total, prev, pager, next, jumper"
            :total="total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            />
        </div>
    </div>
</template>

<script lang="ts" setup>
    import PostBox1 from './PostBox1.vue'; 
    import { defineProps, toRefs,ref, onMounted } from 'vue';
    const props = defineProps({
        type: String
    })

    // 类型(初始化时根据类型获取对应后端数据)
    const {type} = toRefs(props)

    // 分页
    var total = ref(32)
    const page = ref(1)
    const pageSize = ref(16)

    // 分页
    const handleSizeChange = (val: number) => {
        pageSize.value = val;
        load()
    };
    const handleCurrentChange = (val: number) => {
        page.value = val;
        load()
    };

    function load(){

    }

    onMounted(() => {
        load();
    })
</script>

<style scoped>
    .b{
        width: 660px;
        gap: 14px;
        display: flex;
        flex-direction: row;
        flex-wrap: wrap;
    }
    .tabBox{
        width: 300px;
        height: 100px;
        /* background-color: yellowgreen; */
        border-radius: 5px;
        display: flex;
        padding: 10px;
        /* border: 1px solid #868686 */
        box-shadow: 2px 5px 5px #dddddd;
    }
    .tabBox:hover{
        cursor: pointer;
        box-shadow: 2px 5px 5px #faadcc;;
    }
    .tabBox:hover .t2 .title{
        color: #e41065;
    }
    .demo-pagination-block{
        margin-bottom: 10px;
    }
    .t1{
        width: 100px;
    }
    .t2{
        width: 190px;
        display: flex;
        flex-direction: column;
        margin-left: 10px;
    }
    .img{
        width: 100px;
        height: 100px;
        border-radius: 5px;
    }
    .title{
        height: 20px;
        margin-bottom: 10px;
        font-size: 18px;
        line-height: 20px;
        overflow: hidden;
        text-overflow:ellipsis; 
        white-space:nowrap;
    }
    .content{
        height: 70px;
        font-size: 14px;
        color: #868686;
    }
</style>