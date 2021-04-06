<template>
  <q-page
    padding
  >
    <file-browser
      v-model="currentPath"
      :file-tree="fileTree"
      @input="$router.push({name: 'browseProject', params: {id: $route.params.id}, query: {path: $event}})"
    />
  </q-page>
</template>

<script>

import FileBrowser from "components/FileBrowser";

export default {
  name: "BrowseProject",
  components: {FileBrowser},
  data: () => {
    return {
      fileTree: [{
        "name": "TestOrdner",
        "folder": true,
        "children": [{
          "name": "TestUnterOrdner",
          "folder": true,
          "children": [{
            "name": "FunnyFile.txt",
            "folder": false
          }]
        },
          {
            "name": "LeererUnterOrdner",
            "folder": true,
            "children": []
          }]
      },
        {
          "name": "RootFile.txt",
          "folder": false
        }
      ],
      currentPath: "test/test"
    }
  },
  created() {
    if(!!this.$route.query.path) {
      this.currentPath = this.$route.query.path;
    } else {
      this.currentPath = "";
    }
  }
}
</script>

<style scoped>

</style>
