<template>
  <a-layout>
    <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
      <a-row>
        <a-col :span="6">
          <a-tree
              v-if="level1.length > 0"
              :tree-data="level1"
              @select="onSelect"
              :replaceFields="{title: 'name', key: 'id', value: 'id'}"
              :defaultExpandAll="true"
          >
          </a-tree>
        </a-col>
        <a-col :span="18">
<!--          <div>-->
<!--            <h2>{{doc.name}}</h2>-->
<!--            <div>-->
<!--              <span>阅读数：{{doc.viewCount}}</span>-->
<!--              <span>点赞数：{{doc.voteCount}}</span>-->
<!--            </div>-->
<!--            <a-divider style="height: 2px; background-color: #9999cc"/>-->
<!--          </div>-->
<!--          <div class="wangeditor" :innerHTML="html"></div>-->
<!--          <div class="vote-div">-->
<!--            <a-button type="primary" shape="round" :size="'large'" @click="vote">-->
<!--              <template #icon><LikeOutlined />  点赞：{{doc.voteCount}} </template>-->
<!--            </a-button>-->
<!--          </div>-->
        </a-col>
      </a-row>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, createVNode } from 'vue';
import axios from 'axios';
import {message} from 'ant-design-vue';
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";

export default defineComponent({
  name: 'Doc',
  setup() {
    const route = useRoute();
    const docs = ref();
    // const html = ref();
    // const defaultSelectedKeys = ref();
    // defaultSelectedKeys.value = [];
    // // 当前选中的文档
    // const doc = ref();
    // doc.value = {};

    /**
     * 一级文档树，children属性就是二级文档
     * [{
     *   id: "",
     *   name: "",
     *   children: [{
     *     id: "",
     *     name: "",
     *   }]
     * }]
     */
    const level1 = ref(); // 一级文档树，children属性就是二级文档
    level1.value = [];

    /**
     * 数据查询
     **/
    const handleQuery = () => {
      axios.get("/doc/all/" + route.query.ebookId).then((response) => {
        const data = response.data;
        if (data.success) {
          docs.value = data.content;

          level1.value= [];
          level1.value = Tool.array2Tree(docs.value, 0);
        } else {
          message.error(data.message);
        }
      });
    };



    // const onSelect = (selectedKeys: any, info: any) => {
    //   console.log('selected', selectedKeys, info);
    //   if (Tool.isNotEmpty(selectedKeys)) {
    //     // 选中某一节点时，加载该节点的文档信息
    //     doc.value = info.selectedNodes[0].props;
    //     // 加载内容
    //     handleQueryContent(selectedKeys[0]);
    //   }
    // };

    // 点赞
    // const vote = () => {
    //   axios.get('/doc/vote/' + doc.value.id).then((response) => {
    //     const data = response.data;
    //     if (data.success) {
    //       doc.value.voteCount++;
    //     } else {
    //       message.error(data.message);
    //     }
    //   });
    // };

    onMounted(() => {
      handleQuery();
    });

    return {
      level1,
      // html,
      // onSelect,
      // defaultSelectedKeys,
      // doc,
      // vote
    }
  }
});
</script>
