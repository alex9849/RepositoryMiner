<template>
  <q-page
    padding
  >
    <file-browser
      v-model="currentPath"
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
        "isFolder": true,
        "children": [{
          "name": "TestUnterOrdner",
          "isFolder": true,
          "children": [{
            "name": "FunnyFile.txt",
            "isFolder": false
          }]
        },
          {
            "name": "LeererUnterOrdner",
            "isFolder": true,
            "children": []
          }]
      },
        {
          "name": "RootFile.txt",
          "isFolder": false
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
