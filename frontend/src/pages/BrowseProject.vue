<template>
  <q-page
    padding
  >
    <file-browser
      v-model="currentPath"
      :file-tree="fileTree"
      :loading="loadingFileTree"
      @input="$router.push({name: 'browseProject', params: {id: $route.params.id}, query: {path: $event? $event: undefined}})"
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
      currentPath: "test/test",
      loadingFileTree: true
    }
  },
  created() {
    if(!!this.$route.query.path) {
      this.currentPath = this.$route.query.path;
    } else {
      this.currentPath = "";
    }
    setInterval(() => {
      this.loadingFileTree = false;
    }, 2000)

  }
}
</script>

<style scoped>

</style>
