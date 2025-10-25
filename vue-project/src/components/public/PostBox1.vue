<template>
  <div class="postBox" >
    <div class="tab">
      <span class="tab-text">fufu吧</span>
    </div>
    <div class="title">
      <span class="title-text">fufu可爱捏</span>
    </div>
    <div class="contant">
      fufu天下第一可爱
    </div>
    <div class="imgBox" v-show="url_list.length != 0">
      <div v-for="url in url_list">
        <el-image class="img" :src="url" fit="cover" />
      </div>
    </div>
    <div class="detail">
      <!-- author -->
      <div class="d">
        <el-icon class="icon"><UserFilled /></el-icon>
        fufudasiki
      </div>
      <!-- count -->
      <div class="d">
        <el-icon class="icon"><ChatLineSquare /></el-icon>
        20
      </div>
      <!-- time -->
      <div class="d">
        <el-icon class="icon"><Clock /></el-icon>
        50分钟前
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { computed, ref } from 'vue';

  // 最多四张
  const url_list = ref(["","","",""])

</script>

<style scoped>
  .postBox{
    gap: 5px;
    height: auto !important;
    /* padding: 0px 20px; */
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }
  .tab{
    height: 24px;
    line-height: 24px;
  }
  .title{
    height: 24px;
    line-height: 24px;
    color: #cb286c;
  }
  .contant{
    /* height: 28px; */
    /* height: 56px; */
    line-height: 28px;
    color: #666;
    overflow: hidden;
    display: -webkit-box; 
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical; 
    min-height: 28px;
  }
  .imgBox{
    height: 120px;
    display: flex;
    flex-direction: row;
  }
  .img{
    width: 150px;
    height: 120px;
    margin-right: 10px;
  }
  .detail{
    height: 24px;
    line-height: 24px;
    font-size: 14px;
    color: #666;
    display: flex;
    flex-direction: row;
  }
  .d{
    margin-right: 15px;
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
  }
  .icon{
    margin-right: 2px;
  }
  .tab-text:hover {
    cursor: pointer;
    color: #e41065;
  }
  .title-text:hover {
    cursor: pointer;
    text-decoration: underline;
  }
</style>