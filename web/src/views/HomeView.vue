<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
          mode="inline"


          :style="{ height: '100%', borderRight: 0 }"
      >
        <a-sub-menu key="sub1">
          <template #title>
              <span>
                <user-outlined />
                subnav 1
              </span>
          </template>
          <a-menu-item key="1">option</a-menu-item>
          <a-menu-item key="2">option2</a-menu-item>
          <a-menu-item key="3">option3</a-menu-item>
          <a-menu-item key="4">option4</a-menu-item>
        </a-sub-menu>
        <a-sub-menu key="sub2">
          <template #title>
              <span>
                <laptop-outlined />
                subnav 2
              </span>
          </template>
          <a-menu-item key="5">option5</a-menu-item>
          <a-menu-item key="6">option6</a-menu-item>
          <a-menu-item key="7">option7</a-menu-item>
          <a-menu-item key="8">option8</a-menu-item>
        </a-sub-menu>
        <a-sub-menu key="sub3">
          <template #title>
              <span>
                <notification-outlined />
                subnav 3
              </span>
          </template>
          <a-menu-item key="9">option9</a-menu-item>
          <a-menu-item key="10">option10</a-menu-item>
          <a-menu-item key="11">option11</a-menu-item>
          <a-menu-item key="12">option12</a-menu-item>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <pre>
{{ebooks}}
{{ebooks2}}
      </pre>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, reactive, toRef } from 'vue'; //下面要用什么方法先在这里引入
// import HelloWorld from '@/components/HelloWorld.vue'; // @ is an alias to /src
import axios from "axios";

export default defineComponent({
  name: 'HomeView',
  setup(){
    console.log("setup");
    const ebooks = ref();
    const ebooks1 = reactive({books:[]}); //reactive后面通常放对象，这里放了一个空的对象，books是自己定义的属性

    onMounted(() => {
      //初始化逻辑都写到onMounted里面，setup就放一些参数定义，方法定义
      console.log("onMounted");
      axios.get("http://localhost:8080/ebook/list?name=Spring").then(function (response){
        const data = response.data;  //response就是回调时会自动带过来的参数，这里只是取了个名字
        ebooks.value = data.content;
        ebooks1.books = data.content;
        console.log(response);  //(response) =>{}写法也是对的
      });
    });

    return {
      ebooks,  //html要拿到响应式变量（ref）需要在setup后面return
      ebooks2 :toRef(ebooks1,"books")   //这里必须要定义一个对象ebooks2作为右边的返回值,跟前面的books无关，只是自己取的名字
    }
  }
});
</script>
