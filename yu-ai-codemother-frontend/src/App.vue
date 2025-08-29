<script setup lang="ts">
import BasicLayout from '@/layouts/BasicLayout.vue'
import {healthCheck} from '@/api/healthController.ts'

healthCheck().then((res) =>{
  console.log(res)
})
</script>

<template>
  <BasicLayout />
</template>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html,
body {
  height: 100%;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial,
    'Noto Sans', sans-serif, 'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol',
    'Noto Color Emoji';
  font-size: 14px;
  line-height: 1.5715;
  color: rgba(0, 0, 0, 0.85);
  background-color: #fff;
}

#app {
  height: 100%;
}
</style>
