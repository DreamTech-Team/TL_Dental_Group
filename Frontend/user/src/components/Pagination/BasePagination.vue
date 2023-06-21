<script setup lang="ts">
import { ref } from 'vue';
import { ElPagination } from 'element-plus'; // Import the specific component from element-plus
import 'element-plus/dist/index.css';

const total = ref(1000);
const currentPage3 = ref(5);
const pageSize3 = ref(100);
const background = ref(true);
const disabled = ref(false);

const handleSizeChange = (val: number) => {
  console.log(`${val} items per page`);
};
const handleCurrentChange = (val: number) => {
  console.log(`current page: ${val}`);
};

// Export the variable or function using defineExpose
defineExpose({
  total,
  currentPage3,
  pageSize3,
  handleSizeChange,
  handleCurrentChange
});
</script>

<template>
  <div :class="$style['pagination']">
    <el-config-provider namespace="ep">
      <div class="demo-pagination-block">
        <el-pagination
          :value="currentPage3"
          @input="currentPage3 = $event"
          :page-size="pageSize3"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :disabled="disabled"
          :background="background"
          layout="prev, pager, next, jumper"
          :total="1000"
          :class="$style['custom-pagination']"
        />
      </div>
    </el-config-provider>
  </div>
</template>
<style scoped module lang="scss">
@import '../Pagination/Pagination.module.scss';
</style>
