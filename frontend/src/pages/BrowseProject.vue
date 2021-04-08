<template>
  <q-page
    padding
  >
    <file-browser
      v-model="browser.currentPath"
      :file-tree="browser.fileTree"
      :loading="browser.loading"
      @input="$router.push({name: 'browseProject', params: {id: $route.params.id}, query: {path: $event? $event: undefined}})"
    >
      <chart-dialog
        v-model="chartDialog.show"
        :loading="chartDialog.loading"
        :chart-options="chartDialog.chartOptions"
      />
    </file-browser>
  </q-page>
</template>

<script>

import FileBrowser from "components/FileBrowser";
import ProjectService from "src/service/ProjectService";
import ChartDialog from "components/ChartDialog";
import BarChartService from "src/service/chartServices/BarChartService";

export default {
  name: "BrowseProject",
  components: {ChartDialog, FileBrowser},
  data: () => {
    return {
      browser: {
        fileTree: [],
        currentPath: "",
        loading: true
      },
      chartDialog: {
        show: true,
        loading: false,
        chartOptions: {}
      }
    }
  },
  created() {
    if(!!this.$route.query.path) {
      this.browser.currentPath = this.$route.query.path;
    } else {
      this.browser.currentPath = "";
    }
    this.chartDialog.chartOptions = BarChartService.exampleBackendData;
    ProjectService.getProjectFileTree(this.projectId)
      .then(fileTree => this.browser.fileTree = fileTree)
      .finally(() => this.browser.loading = false);
  },
  computed: {
    projectId() {
      return this.$route.params.id;
    }
  }
}
</script>

<style scoped>

</style>
