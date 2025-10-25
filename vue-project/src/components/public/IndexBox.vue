<template>
  <div class="b">
    <div class="p" v-for="p in 10">
        <PostBox1 class="post_box"></PostBox1>
        <div class="split"></div>
    </div>
  </div>
</template>

<script lang="ts" setup>
    import PostBox1 from './PostBox1.vue'; 
    import { defineProps, toRefs } from 'vue';
    const props = defineProps({
        type: String
    })

    // 类型(初始化时根据类型获取对应后端数据)
    const {type} = toRefs(props)
</script>

<style scoped>
    .b{

    }
    .p{
        margin-top: 10px;
    }
    .p:first-child{
        margin-top: 0px;
    }
    .post_box{
        margin-bottom: 10px;
    }
    .split{
        border-bottom-style:solid;
        border-width: 1px;
        border-color: rgb(232, 232, 232);
    }
    
</style>